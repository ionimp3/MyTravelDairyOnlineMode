package ProfileFolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lmh.mytraveldairy.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

import MyProgressDialog.MyProgressDialogActivity;
import OnBoarding.OnBoardingActivity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ProfileImageActivity extends AppCompatActivity {
    ActionBar actionBar;
    Toolbar toolbar;

    ImageView selectedImage,coverImage;
    Button resetProfilePic;
    StorageReference storageReference;

    FirebaseAuth mAuth;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference UserRef;
    DatabaseReference uploadeFileListRef;
    StorageReference UserProfileImageRef;


    ProgressDialog dialog1;
    private MyProgressDialogActivity myProgressDialogActivity;

    final static int GET_GALLERY_IMAGE = 200;
    final static int Gallery_Pick = 1;


    Uri uri;
    Intent intent;

    private String currentUser, uploadFilePath;
    private Uri resultUri;

    private int i = 0;
    private int resetResult = 0;

    private String cropAlready = "n";

    private String nicNameFromProfileActivity, profileImageFromProfileActivity, coverImagefromProfileActivity, coverImageKeyeFromProfileActivity, profileImageKeyfromProfileActivity;

    TextView nicnameView, mailIdView;
    LinearLayout Prestacklayout1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        toolbar = findViewById(R.id.tb_profile_image_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle("닉네임변경");



        mailIdView = (TextView) findViewById(R.id.text_pre_mail_id);
        nicnameView = (TextView) findViewById(R.id.text_pre_nic_name);
        coverImage =  findViewById(R.id.image_header_cover_bg);
        selectedImage = findViewById(R.id.image_pre_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        showAllProFileData();
        currentUser = firebaseAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child(currentUser).child("UserTB");
        uploadeFileListRef = FirebaseDatabase.getInstance().getReference().child(currentUser).child("UpLoadFileListTB").child(profileImageKeyfromProfileActivity);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child(currentUser).child("images");



        if (profileImageFromProfileActivity.trim().equals("NA")) {
            selectedImage.setImageResource(R.drawable.ic_account_circle_black);
            i = 1;
        } else if (profileImageFromProfileActivity.trim().length() > 20 && i < 1) {
            Picasso.get().load(profileImageFromProfileActivity).into(selectedImage);
            i = 5;
            //Toast.makeText(this,"정보있음",Toast.LENGTH_SHORT).show();
        }

        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //github의 라이브러시 사용법을 따라하자..그게 맞음
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setMultiTouchEnabled(true)
                        .start(ProfileImageActivity.this);
            }
        });
    }

    private void showAllProFileData() {
        myProgressDialogActivity = new MyProgressDialogActivity(this);
        myProgressDialogActivity.show();
        Intent intent = getIntent();
        String user = firebaseAuth.getCurrentUser().getEmail();
        nicNameFromProfileActivity = intent.getStringExtra("UserTB_nicName_Send");
        profileImageFromProfileActivity = intent.getStringExtra("UserTB_profileImage_Send");
        coverImagefromProfileActivity = intent.getStringExtra("UserTB_coverImage_Send");
        coverImageKeyeFromProfileActivity = intent.getStringExtra("UserTB_coverImageKey_Send");
        profileImageKeyfromProfileActivity = intent.getStringExtra("UserTB_profileImageKey_Send");

        mailIdView.setText(user);
        nicnameView.setText(nicNameFromProfileActivity);
        if (!coverImagefromProfileActivity.equals("NA")) {
            Picasso.get().load(coverImagefromProfileActivity).into(coverImage);
            // 백그라운드 이미지교체
        } else {
            coverImage.setImageResource(R.drawable.bg_drawer_header);
        }

        myProgressDialogActivity.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //사진선택,크롭까지만
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 0);
            } else {
                final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                resultUri = result.getUri();
                selectedImage.setImageURI(resultUri);
                cropAlready = "y";
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent gobackIntent = new Intent(ProfileImageActivity.this, OnBoardingActivity.class);
            gobackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(gobackIntent);
            finish();
        } else {
            CheckUserExistance();
        }
    }

    private void CheckUserExistance() {
        final String current_User_Id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference RealUserCheck = FirebaseDatabase.getInstance().getReference();
        RealUserCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //실제 데이터베이스를 조회해서 이메일 id 가 있는지 확인
                if (!dataSnapshot.hasChild(current_User_Id)) {
                    Intent goBackIntent = new Intent(ProfileImageActivity.this, OnBoardingActivity.class);
                    goBackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goBackIntent);
                    finish();
                } else {
                    //MessageToast.message(UpdateNicName.this,"실제접속했어요");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showProcessDialog1() {

        dialog1 = new ProgressDialog(ProfileImageActivity.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setTitle("이미지업로드");
        dialog1.setMessage("스토리지,DB에 업로드 중입니다....");
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(true);
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbarcustum, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기 버튼 눌렀을 때
                Intent gotoMain = new Intent(this, ProfileActivity.class);
                gotoMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotoMain);
                finish();
                return true;
            }
            case R.id.curr_changed: { // 오른쪽 상단 버튼 눌렀을 때
                //스토리지 저장
                if (resetResult == 1){
                    showProcessDialog1();
                    HashMap userProfileImageMap = new HashMap();
                    userProfileImageMap.put("UserTB_profileImage", "NA");
                    userProfileImageMap.put("UserTB_timeStampUpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
                    UserRef.updateChildren(userProfileImageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                selectedImage.setImageResource(R.drawable.ic_account_circle_black);
                                Toast.makeText(ProfileImageActivity.this, "초기화 이미지로 저장하였읍니다..", Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                                resetResult = 0;
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(ProfileImageActivity.this, "초기화 이미지 저장 실패!! 다시 진행해주세요" + message, Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                                resetResult = 0;
                            }
                        }
                    });
                    break;
                }
                if (cropAlready.equals("y")) {
                    selectedImage.setImageURI(resultUri);
                    showProcessDialog1();
                    uploadFilePath = (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                    final StorageReference filePath = UserProfileImageRef.child("profileimage.jpg");
                    filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @SuppressLint("NewApi")
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            HashMap ProfilePicUpladMap = new HashMap();
                                            String downloadUrl = uri.toString();
                                            ProfilePicUpladMap.put("UserTB_profileImage", downloadUrl);
                                            ProfilePicUpladMap.put("UserTB_timeStampUpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
                                            UserRef.updateChildren(ProfilePicUpladMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        saveToUploadFileListTB();
                                                        //
                                                        Toast.makeText(ProfileImageActivity.this, "이미지 스토리지/DB저장완료", Toast.LENGTH_SHORT).show();
                                                        Intent gotoMain = new Intent(ProfileImageActivity.this, ProfileActivity.class);
                                                        gotoMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(gotoMain);
                                                        finish();

                                                    } else {
                                                        Picasso.get().load(resultUri).into(selectedImage);
                                                        String message = task.getException().getMessage();
                                                        Toast.makeText(ProfileImageActivity.this, "이미지 DB저장 실패!! 다시 진행해주세요" + message, Toast.LENGTH_SHORT).show();
                                                        dialog1.dismiss();
                                                        Intent gotoMain = new Intent(ProfileImageActivity.this, ProfileActivity.class);
                                                        gotoMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(gotoMain);
                                                        finish();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(ProfileImageActivity.this, "기존 사진과 동일합니다!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent goBackIntent = new Intent(ProfileImageActivity.this, ProfileActivity.class);
        goBackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goBackIntent);
        finish();
        // 현재액티비의 루트 액티비티까지 종료시켜라
        // 루트 설정화면을 부른 이전 메뉴 액티비티
        // 드로워 화면 만들면 드로워 화면으로 변경해라..아니면 그대로 대시보드로 이동
    }

    @SuppressLint("NewApi")
    public void btn_action_reset_image(View view) {
        resetResult = 1;
        selectedImage.setImageResource(R.drawable.ic_account_circle_black);
        Toast.makeText(ProfileImageActivity.this, "이미지 초기화 완료!! 저장하면 변경됩니다.", Toast.LENGTH_SHORT).show();
    }

    private void saveToUploadFileListTB() {
        //showProcessDialog1();
        HashMap saveToFileListMap = new HashMap();
        saveToFileListMap.put("UpLoadFileListTB_fileName", "profileimage.jpg");
        saveToFileListMap.put("UpLoadFileListTB_filePath", "/UserTB");
        saveToFileListMap.put("UpLoadFileListTB_timeStampUpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
        uploadeFileListRef.updateChildren(saveToFileListMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //selectedImage.setImageResource(R.drawable.ic_account_circle_black);
                    Toast.makeText(ProfileImageActivity.this, "업로드 파일 리스트를 업데이트 하였읍니다 !!", Toast.LENGTH_SHORT).show();
                    // dialog1.dismiss();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(ProfileImageActivity.this, "업로드 파일 리스트 저장 실패!!! 다시 진행해주세요" + message, Toast.LENGTH_SHORT).show();
                    //dialog1.dismiss();
                }
            }
        });
    }
}
