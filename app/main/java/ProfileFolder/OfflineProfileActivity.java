package ProfileFolder;


import android.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lmh.mytraveldairy.OfflineMainActivity;
import com.lmh.mytraveldairy.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import CommonFolder.OfflineTravelDairyDescriptionActivity;
import RoomDataFolder.RoomMyTravelDairyDatabase;
import RoomDataFolder.RoomUserTB;

;import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class OfflineProfileActivity extends AppCompatActivity {
    private static final String TAG = "OfflineProfileActivity";
    //db 초기값세팅
    //Room관련 선언
    private RoomUserTB roomUserTB;
    private String timeStampUpdateTime,timeStampCreateTime;

    // 레이아웃
    private TextView mText_offline_nic_name,mText_offline_now_user_status,mText_offline_header_push_alarm_count;
    private TextView mText_offline_plan_sum,mText_offline_todo_sum,mText_offline_done_sum;
    private ImageView mImage_offline_profile,mBg_offline_cover_image;
    private Switch mSwitch_offline_user_status_selected,mSwitch_offline_push_alarm_selected;

    private String UserTB_nicName_fromdb, UserTB_coverImage_fromdb, UserTB_profileImage_fromdb,UserTB_coverImageKey_fromdb,UserTB_profileImageKey_fromdb;
    int i = 0;
    int j = 0;

    private Uri resultUri,uri;
    private int resetResult = 0;
    private String cropAlready = "n";
    private String select_image ="";
    private String saveFileName;

    private Bitmap bitmapProfile,bitmapCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_profile);

        mText_offline_nic_name = findViewById(R.id.text_offline_nic_name);
        mText_offline_now_user_status = findViewById(R.id.text_offline_now_user_status);
        mText_offline_header_push_alarm_count = findViewById(R.id.text_offline_header_push_alarm_count);
        mText_offline_plan_sum = findViewById(R.id.text_offline_plan_sum);
        mText_offline_todo_sum = findViewById(R.id.text_offline_todo_sum);
        mText_offline_done_sum = findViewById(R.id.text_offline_done_sum);
        mImage_offline_profile = findViewById(R.id.image_offline_profile);
        mBg_offline_cover_image = findViewById(R.id.bg_offline_cover_image);
        mSwitch_offline_user_status_selected = findViewById(R.id.switch_offline_user_status_selected);
        mSwitch_offline_push_alarm_selected = findViewById(R.id.switch_offline_push_alarm_selected);

        //라이브데이터
        LiveData<List<RoomUserTB>> roomUserTBLiveDataList = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                .getRoomUserTBdao()
                .getAllLiveData();
        roomUserTBLiveDataList.observe(this, new Observer<List<RoomUserTB>>() {
            @Override
            public void onChanged(List<RoomUserTB> roomUserTBS) {
                RoomUserTB roomUserTB = roomUserTBS.get(0);
                Log.d(TAG,""+roomUserTB);
                mText_offline_nic_name.setText(roomUserTB.getUserTB_nicName());
                mText_offline_now_user_status.setText(roomUserTB.getUserTB_nowUserStatus());
                if ((roomUserTB.getUserTB_nowUserStatus().trim()).equals("여행중")){
                    mSwitch_offline_user_status_selected.setChecked(true);
                }
                if ((roomUserTB.getUserTB_pushAlarmSelected().trim()).equals("Y")){
                    mSwitch_offline_push_alarm_selected.setChecked(true);
                }
                profileImageShow(roomUserTBS);
            }
        });
    }
    private void profileImageShow(List<RoomUserTB> roomUserTBS) {
        RoomUserTB roomUserTB = roomUserTBS.get(0);
        if (roomUserTB.UserTB_profileImage.trim().equals("NA")) {
            mImage_offline_profile.setImageResource(R.drawable.ic_account_circle_black);
        } else if (roomUserTB.UserTB_profileImage.trim().length() > 5) {
            RequestOptions myOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
           Glide.with(this).load(roomUserTB.UserTB_profileImage).apply(myOptions).into(mImage_offline_profile);
        }
        coverImageShow(roomUserTBS);
    }

    private void coverImageShow(List<RoomUserTB> roomUserTBS) {
        RoomUserTB roomUserTB = roomUserTBS.get(0);
        if (roomUserTB.UserTB_coverImage.trim().equals("NA")) {
           mBg_offline_cover_image.setImageResource(R.drawable.bg_profile_image);
        } else if (roomUserTB.UserTB_coverImage.trim().length() > 5) {
            RequestOptions myOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
            Glide.with(this).load(roomUserTB.UserTB_coverImage).apply(myOptions).into(mBg_offline_cover_image);
        }
    }

    public void btn_action_offline_change_nic_name(View view) {

        final AlertDialog dialog1 = new AlertDialog.Builder(this).create();
        View mView = getLayoutInflater().inflate(R.layout.custum_dialog_change_nicname,null);
        final EditText mChangeNicName = mView.findViewById(R.id.edit_dialog_nicname);
        Button mBtn_dialog_nicname_ok = mView.findViewById(R.id.btn_dialog_nicname_ok);
        Button mBtn_dialog_nicname_reset = mView.findViewById(R.id.btn_dialog_nicname_reset);
        dialog1.setView(mView);
        dialog1.show();

        mBtn_dialog_nicname_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStampUpdateTime = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
                }

                // 업데이트
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RoomUserTB roomUserTBUpdate = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                .getRoomUserTBdao()
                                .findRoomUserTBById(1);

                        // db에 변경할 내용
                        roomUserTBUpdate.setUserTB_nicName("Traveler");
                        roomUserTBUpdate.setUserTB_timeStampUpdateTime(timeStampUpdateTime);

                        RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                .getRoomUserTBdao()
                                .update(roomUserTBUpdate);

                        dialog1.dismiss();

                    }
                }).start();

            }
        });

        mBtn_dialog_nicname_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mChangeNicName.getText().toString().isEmpty()){
                    //다이얼로그 입력값 가져오기
                   final String changeNicName = mChangeNicName.getText().toString();
                    //화면에 표시하기
                    //mText_offline_nic_name.setText(changeNicName);

                    timeStampUpdateTime = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
                    }

                    // 업데이트
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            RoomUserTB roomUserTBUpdate = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                    .getRoomUserTBdao()
                                    .findRoomUserTBById(1);

                            // db에 변경할 내용
                            roomUserTBUpdate.setUserTB_nicName(changeNicName);
                            roomUserTBUpdate.setUserTB_timeStampUpdateTime(timeStampUpdateTime);

                            RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                    .getRoomUserTBdao()
                                    .update(roomUserTBUpdate);

                            dialog1.dismiss();

                        }
                    }).start();

                    Toast.makeText(OfflineProfileActivity.this, "성공", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(OfflineProfileActivity.this, "공백은 허용하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //사진선택,크롭
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                resultUri = result.getUri();
                if (select_image.equals("profile")){
                    mImage_offline_profile.setImageURI(resultUri);
                }
                if (select_image.equals("cover")){
                    mBg_offline_cover_image.setImageURI(resultUri);
                }
                cropAlready = "y";
            }
            //저장
            if (cropAlready.equals("y")) {
                if (select_image.equals("profile")){
                   bitmapProfile = ((BitmapDrawable)mImage_offline_profile.getDrawable()).getBitmap();
                    saveFileName = "profileimage.jpg";
                }
                if (select_image.equals("cover")){
                    bitmapCover = ((BitmapDrawable)mBg_offline_cover_image.getDrawable()).getBitmap();
                    saveFileName = "coverimage.jpg";
                }
                File path = getFilesDir();
                final File dir = new File(path,"pic");
                dir.mkdir();
                final File file = new File(dir,saveFileName);
                OutputStream out ;

                if (select_image.equals("profile")) {
                    timeStampUpdateTime = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
                    }
                    try {
                        out = new FileOutputStream(file,false);
                        bitmapProfile.compress(Bitmap.CompressFormat.JPEG,100,out);
                        try {
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 업데이트
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            RoomUserTB roomUserTBUpdate = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                    .getRoomUserTBdao()
                                    .findRoomUserTBById(1);

                            // db에 변경할 내용
                            roomUserTBUpdate.setUserTB_profileImage(dir +"/profileimage.jpg");
                            roomUserTBUpdate.setUserTB_timeStampUpdateTime(timeStampUpdateTime);

                            RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                    .getRoomUserTBdao()
                                    .update(roomUserTBUpdate);

                        }
                    }).start();
                }
                if (select_image.equals("cover")) {
                    timeStampUpdateTime = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
                    }
                    try {
                        out = new FileOutputStream(file,false);
                        bitmapCover.compress(Bitmap.CompressFormat.JPEG,100,out);
                        try {
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 업데이트
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            RoomUserTB roomUserTBUpdate = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                    .getRoomUserTBdao()
                                    .findRoomUserTBById(1);

                            // db에 변경할 내용
                            roomUserTBUpdate.setUserTB_coverImage(dir +"/coverimage.jpg");
                            roomUserTBUpdate.setUserTB_timeStampUpdateTime(timeStampUpdateTime);

                            RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                    .getRoomUserTBdao()
                                    .update(roomUserTBUpdate);

                        }
                    }).start();
                }

            } else {
                Toast.makeText(OfflineProfileActivity.this, "변경 할 사진을 선택해주세요 !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void btn_action_change_cover_pic(View view) {
        mBg_offline_cover_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //github의 라이브러시 사용법을 따라하자..그게 맞음
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setMultiTouchEnabled(true)
                        .start(OfflineProfileActivity.this);
                select_image = "cover";
            }
        });
    }

    public void btn_action_change_profile_pic(View view) {
        mImage_offline_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //github의 라이브러시 사용법을 따라하자..그게 맞음
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setMultiTouchEnabled(true)
                        .start(OfflineProfileActivity.this);
                select_image = "profile";
            }
        });
    }

    public void btn_action_offline_now_user_status(View view) {
        if (mSwitch_offline_user_status_selected.isChecked()){
            mText_offline_now_user_status.setText("여행중");
        }
        else {
            mText_offline_now_user_status.setText("여행전");
        }
        timeStampUpdateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
        }

        // 업데이트
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoomUserTB roomUserTBUpdate = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                        .getRoomUserTBdao()
                        .findRoomUserTBById(1);

                // db에 변경할 내용
                String mChangedNowUserStatus = mText_offline_now_user_status.getText().toString();
                roomUserTBUpdate.setUserTB_nowUserStatus(mChangedNowUserStatus);
                roomUserTBUpdate.setUserTB_timeStampUpdateTime(timeStampUpdateTime);

                RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                        .getRoomUserTBdao()
                        .update(roomUserTBUpdate);

            }
        }).start();

        Toast.makeText(this,"개발중입니다.!!!",Toast.LENGTH_SHORT).show();
    }

    public void btn_action_offline_push_alarm_select(View view) {
        String mChangedPushAlarm = null;
        if (mSwitch_offline_push_alarm_selected.isChecked()){
            mChangedPushAlarm = "Y";
        }
        else {
            mChangedPushAlarm = "N";
        }
        timeStampUpdateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
        }

        // 업데이트
        final String finalMChangedPushAlarm = mChangedPushAlarm;
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoomUserTB roomUserTBUpdate = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                        .getRoomUserTBdao()
                        .findRoomUserTBById(1);

                // db에 변경할 내용

                roomUserTBUpdate.setUserTB_pushAlarmSelected(finalMChangedPushAlarm);
                roomUserTBUpdate.setUserTB_timeStampUpdateTime(timeStampUpdateTime);

                RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                        .getRoomUserTBdao()
                        .update(roomUserTBUpdate);

            }
        }).start();
        Toast.makeText(this,"개발중입니다.!!!",Toast.LENGTH_SHORT).show();
    }

    public void btn_action_offline_description(View view) {
        Intent intent = new Intent(OfflineProfileActivity.this, OfflineTravelDairyDescriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void btn_action_offline_reset(View view) {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(OfflineProfileActivity.this);
        alert_confirm.setMessage("프로파일,배경이미지가 초기값으로 설정됩니다..").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timeStampUpdateTime = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                RoomUserTB roomUserTBUpdate = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                        .getRoomUserTBdao()
                                        .findRoomUserTBById(1);

                                roomUserTBUpdate.setUserTB_profileImage("NA");
                                roomUserTBUpdate.setUserTB_coverImage("NA");
                                roomUserTBUpdate.setUserTB_timeStampUpdateTime(timeStampUpdateTime);

                                RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                        .getRoomUserTBdao()
                                        .update(roomUserTBUpdate);
                            }
                        }).start();
                    }
                });
        alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(OfflineProfileActivity.this, "취소", Toast.LENGTH_LONG).show();
            }
        });
        alert_confirm.show();
    }

    //화면 back 버튼 눌렀을때처리
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToOfflineMain = new Intent(OfflineProfileActivity.this, OfflineMainActivity.class);
        goToOfflineMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToOfflineMain);
        finish();
    }

    public void btn_action_none(View view) {
        //임시 로 막기
    }

}
