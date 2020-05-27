package PlanFolder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lmh.mytraveldairy.OfflineMainActivity;
import com.lmh.mytraveldairy.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import MyProgressDialog.MyProgressDialogActivity;
import RoomDataFolder.RoomInfoTB;
import RoomDataFolder.RoomMyTravelDairyDatabase;
import RoomDataFolder.RoomUserTB;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class OfflineNewPlanActivity extends AppCompatActivity {
    private static final String TAG = "OfflineNewPlanActivity";
    MyProgressDialogActivity myProgressDialogActivity;

    private Toolbar toolbar;
    ImageView imagePlanBg;
    EditText edPlanTitleName,edPlanBudget;
    ImageButton btnPlanDepartDateSelected,btnPlanEndDateSelected;
    TextView textPlanDepartDate,textPlanEndDate,textCountryTag;

    private Uri ImageUri,resultUri;
    private Uri uri;
    private String cropAlready;

    private String medPlanTitleName,mtextPlanDepartDate,mtextPlanEndDate,mtextCountryTag,mtextCityTag,mtextPlaceTag;
    private String saveCurrentDateForFileName,saveCurrentTimeForFileName,saveCurrentForFileName;
    private String currentUserId,downloadUrl,UserTB_nicNameFromDB,UserTB_profileImageFromDB,UserTB_nowUserStatusFromDB;
    private String edPlanTitleNameFromView,edPlanBudgetFromView,textPlanDepartDateFromView,textPlanEndDateFromView;
    private String textCountryTagFromView;
    private String timeStampUpdateTime,timeStampCreateTime ;

    private String departDate,endDate,resultDecimalFormat="";;
    private int i = 0;
    private int j = 0;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");

    String mUserTBnicName, mUserTBnowUserStatus,mUserTBprofileImage;

    String[] listCountry;
    boolean[] checkedCountry;
    boolean[] RecoveryCheckedCountry;
    ArrayList<Integer> mSelectedCountry = new ArrayList<>();
    ArrayList<Integer> mDefaultIndex = new ArrayList<>(Collections.nCopies(51,0));
    ArrayList<Integer> recoveryCountry = new ArrayList<>(Collections.nCopies(51,0));
    private String textCount = "";
    int defaultFromDB = 0,defaultFromDB1 ;

    Bitmap bitmap;
    File file;
    String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_new_plan);

        toolbar =(Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagePlanBg = (ImageView) findViewById(R.id.image_click_plan_bg);
        edPlanTitleName = (EditText) findViewById(R.id.ed_click_plan_title_name);
        edPlanBudget = (EditText) findViewById(R.id.ed_click_plan_budget);
        textPlanDepartDate = (TextView) findViewById(R.id.text_click_plan_depart_date);
        textPlanEndDate = (TextView) findViewById(R.id.text_click_plan_end_date);
        btnPlanDepartDateSelected = (ImageButton) findViewById(R.id.btn_click_plan_depart_date_selected);
        btnPlanEndDateSelected = (ImageButton) findViewById(R.id.btn_click_plan_end_date_selected);
        textCountryTag = (TextView) findViewById(R.id.text_click_country_tag);
        edPlanBudget.addTextChangedListener(textWatcher);

        //초기날짜지정
        setDefaultDate();

        //
        listCountry = getResources().getStringArray(R.array.countryList);
        checkedCountry = new boolean[listCountry.length];
        RecoveryCheckedCountry = new boolean[listCountry.length];

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RoomUserTB> roomUserTBViewList = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                        .getRoomUserTBdao()
                        .getAllUserTBList();
                if (roomUserTBViewList.size() != 0) {
                    mUserTBnicName = roomUserTBViewList.get(0).getUserTB_nicName();
                    mUserTBnowUserStatus = roomUserTBViewList.get(0).getUserTB_nowUserStatus();
                    mUserTBprofileImage = roomUserTBViewList.get(0).getUserTB_profileImage();
                    Log.d(TAG,mUserTBnicName+":"+mUserTBnowUserStatus+":"+mUserTBprofileImage);
                }
            }
        }).start();
    }

    //경비예산입력시  콤마 추가
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!TextUtils.isEmpty(s.toString()) && !s.toString().equals(resultDecimalFormat)){
                resultDecimalFormat = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",","")));
                edPlanBudget.setText(resultDecimalFormat);
                edPlanBudget.setSelection(resultDecimalFormat.length());
                String originalBuget = edPlanBudget.getText().toString();
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    //오늘날짜로 초기갑세팅
    private void setDefaultDate() {
        if (i == 0){
            Calendar calendarDate1 = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd (EEE)");
            textPlanDepartDate.setText(simpleDateFormat.format(calendarDate1.getTime()));

            Calendar calendarDate2 = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
            departDate = simpleDateFormatDate.format(calendarDate2.getTime());
        }
        if (j == 0){
            Calendar calendarDate1 = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd (EEE)");
            textPlanEndDate.setText(simpleDateFormat.format(calendarDate1.getTime()));

            Calendar calendarDate2 = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
            endDate = simpleDateFormatDate.format(calendarDate2.getTime());
        }
    }

    //출발일선택 : 다이얼로그
    public void btn_action_depart_date_selected(View view) {
        final Calendar departDateCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                departDateCalendar.set(Calendar.YEAR,year);
                departDateCalendar.set(Calendar.MONTH,month);
                departDateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd (EEE)");
                textPlanDepartDate.setText(simpleDateFormat.format(departDateCalendar.getTime()));
                i =1;
            }
        };
        new DatePickerDialog(this,dateSetListener,departDateCalendar.get(Calendar.YEAR),departDateCalendar.get(Calendar.MONTH),departDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    //도착일선택 : 다이얼로그
    public void btn_action_end_date_selected(View view) {
        final Calendar endDateCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDateCalendar.set(Calendar.YEAR,year);
                endDateCalendar.set(Calendar.MONTH,month);
                endDateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd (EEE)");
                textPlanEndDate.setText(simpleDateFormat.format(endDateCalendar.getTime()));
                j=1;
            }
        };
        new DatePickerDialog(this,dateSetListener,endDateCalendar.get(Calendar.YEAR),endDateCalendar.get(Calendar.MONTH),endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    //카메라 구동, 이미지 가져오기
    public void image_action_plan_bg(View view) {
        openGallery();
    }
    private void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setMultiTouchEnabled(true)
                .start(OfflineNewPlanActivity.this);
    }

    //크롭후 화면 리플래쉬
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 0);
            } else {
                final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                resultUri = result.getUri();
                imagePlanBg.setImageURI(resultUri);
                cropAlready = "y";
            }
        }
    }

    public void btn_action_select_country(View view) {
        for (int z = 0; z < listCountry.length;z++){
            recoveryCountry.set(z , mDefaultIndex.get(z));
        }
        AlertDialog.Builder dialogCountryList = new AlertDialog.Builder(this);
        dialogCountryList.setTitle("여행할 나라를 선택해주세요");
/*        if (defaultFromDB < 1){
            mSelectedCountry.add(0);
            checkedCountry[0] = true;
            mDefaultIndex.set(0,1);
            defaultFromDB = 1;
        }*/

        dialogCountryList.setMultiChoiceItems(listCountry, checkedCountry, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if (isChecked){
/*                    if (defaultFromDB1 < 1){
                        mSelectedCountry.clear();
                        mSelectedCountry.add(0);
                        defaultFromDB1 = 1;
                    }*/
                    if(!mSelectedCountry.contains(position)){
                        mSelectedCountry.add(position);
                        mDefaultIndex.set(position, position + 1);
                    }
                } else if (mSelectedCountry.contains(position)) {
                    mSelectedCountry.remove((Integer.valueOf(position)));
                    mDefaultIndex.set(position,0);
                }
                textCount = "";
                for (int u = 0 ; u < listCountry.length ; u++){
                    textCount += String.valueOf(mDefaultIndex.get(u));
                    if (u != listCountry.length-1) {
                        textCount += ",";
                    }
                }
                Log.d(TAG,""+ textCount);
            }
        });
        dialogCountryList.setCancelable(false);
        dialogCountryList.setPositiveButton("선택완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String country = "";
                String countryReplace = "";
                for (int u = 0 ; u < listCountry.length ; u++){
                    if (mDefaultIndex.get(u) != 0) {
                        country = country + listCountry[u]+",";
                    }
                }
                if (!country.isEmpty()){
                    countryReplace = country.substring(0,(country.length()-1));
                } else if (country.trim().isEmpty() ) {
                    countryReplace = "미선택";
                }
                textCountryTag.setText(countryReplace);
                Log.d(TAG,""+ countryReplace);
                Log.d(TAG,""+ textCount);
            }
        });
        dialogCountryList.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                textCount = "";
                for (int z = 0; z < listCountry.length;z++){
                    mDefaultIndex.set(z , recoveryCountry.get(z));
                    if (recoveryCountry.get(z) != 0) {
                        checkedCountry[z] = true;
                        textCount += String.valueOf(z+1)+",";
                    } else {
                        checkedCountry[z] = false;
                        textCount += String.valueOf(0)+",";
                    }
                }
                textCount = textCount.substring(0,(textCount.length()-1));
                Log.d(TAG,"복원"+ mDefaultIndex);
                Log.d(TAG,""+ textCount);
                dialog.dismiss();
            }
        });
        dialogCountryList.setNeutralButton("초기화", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textCount = "";
                for (int k = 0; k < checkedCountry.length; k++){
                    checkedCountry[k] = false;
                    mDefaultIndex.set(k,0);
                    textCount += "0,";
                    mSelectedCountry.clear();
                    textCountryTag.setText("미선택");
                }
                textCount = textCount.substring(0,(textCount.length()-1));
                Log.d(TAG,"저장해놓을 변수초기화"+textCount);
            }
        });
        AlertDialog mDialog = dialogCountryList.create() ;
        mDialog.show();
        //다이얼로그사이즈 조정
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mDialog.getWindow().getAttributes());
        layoutParams.width = 900;
        //layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mDialog.getWindow().setAttributes(layoutParams);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_offline_actionbar_new_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                Intent goToOfflineMain = new Intent(OfflineNewPlanActivity.this, OfflineMainActivity.class);
                goToOfflineMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToOfflineMain);
                finish();
                break;}
            case R.id.curr_offline_new_plan_changed: {
                validationSaveToInfoTB();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void validationSaveToInfoTB() {
        //VALIDATION 구문추가


        newPlanSaveToInfoTB();
    }

    private void newPlanSaveToInfoTB()  {
        myProgressDialogActivity = new MyProgressDialogActivity(this);
        myProgressDialogActivity.show();

        timeStampUpdateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
        }
        timeStampCreateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeStampCreateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
            mFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+ "InfoTB_BG.jpg";
        }
        final RoomInfoTB roomInfoTB = new RoomInfoTB();
        roomInfoTB.InfoTB_planName = edPlanTitleName.getText().toString();
        roomInfoTB.InfoTB_planDepartDate = textPlanDepartDate.getText().toString();
        roomInfoTB.InfoTB_planEndDate = textPlanEndDate.getText().toString();
        roomInfoTB.InfoTB_budget = edPlanBudget.getText().toString();
        roomInfoTB.InfoTB_planCountry = textCountryTag.getText().toString();
        String[] strSubCurrency = (textCountryTag.getText().toString()).split(",");
        String exCurrency ="";
        String exCountry = "";
        for (int lo = 0; lo < strSubCurrency.length ; lo++){
            String tempCurrency = strSubCurrency[lo];
            int tempCurrencySize = tempCurrency.length();
            exCurrency += tempCurrency.substring(tempCurrencySize-4,tempCurrencySize-1);
            exCountry += tempCurrency.substring(0,tempCurrencySize-5);
            if(lo != strSubCurrency.length-1){
                exCurrency = exCurrency+",";
                exCountry = exCountry+",";
            }
        }
        roomInfoTB.InfoTB_subCurrency = exCurrency;
        roomInfoTB.InfoTB_nowUserStatus = "여행전";
        roomInfoTB.InfoTB_planTags = exCountry;
        roomInfoTB.InfoTB_titleImage = "/data/data/com.lmh.mytraveldairy/files/pic/"+mFileName;
        roomInfoTB.InfoTB_viewCount = 0;
        roomInfoTB.InfoTB_timeStampCreateTime = timeStampCreateTime;
        roomInfoTB.InfoTB_timeStampUpdateTime = timeStampUpdateTime;
        roomInfoTB.InfoTB_UserTB_nowUserStatus = mUserTBnowUserStatus;
        roomInfoTB.InfoTB_UserTB_nicName = mUserTBnicName;
        roomInfoTB.InfoTB_UserTB_profileImage = mUserTBprofileImage;

        Log.d(TAG,"통화:"+ exCurrency +"국가"+exCountry);
        InsertInfoTbAsyncTask insertInforTbAsyncTask = new InsertInfoTbAsyncTask();
        insertInforTbAsyncTask.execute(roomInfoTB);

        //테스트용
        final int pos_db = 1;
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                List<RoomInfoTB> roomInfoTBViewList = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                        .getRoomInfoTBdao()
                        .getAllInfoTBList();
                int pos_db = roomInfoTBViewList.size();
                Log.d(TAG,"infoTB 행갯수"+String.valueOf(roomInfoTBViewList.size()));
            }
        });
        thread1.start();
        try {
            thread1.join();
            Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    RoomInfoTB roomInfoTBViewList2 = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                            .getRoomInfoTBdao()
                            .findRoomInfoTBById(pos_db);
                    Log.d(TAG,roomInfoTB.getInfoTB_planName()+roomInfoTB.getInfoTB_planDepartDate()+roomInfoTB.getInfoTB_UserTB_nicName()+roomInfoTB.getInfoTB_UserTB_nowUserStatus());
                }
            });
            thread2.start();
            try {
                thread2.join();
                myProgressDialogActivity.dismiss();
            } catch (InterruptedException e){
                e.printStackTrace();
                myProgressDialogActivity.dismiss();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //

        saveImageTostorage();
    }

    class InsertInfoTbAsyncTask extends AsyncTask<RoomInfoTB, Void, Void> {

        @Override
        protected Void doInBackground(RoomInfoTB... roomInfoTBS) {
            RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                    .getRoomInfoTBdao()
                    .insert(roomInfoTBS[0]);
            return null;
        }
    }
    private void saveImageTostorage() {
        bitmap = ((BitmapDrawable)imagePlanBg.getDrawable()).getBitmap();
        File path = getFilesDir();
        File dir = new File(path,"pic");
        dir.mkdir();
        file = new File (dir,mFileName);
        OutputStream out;

        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
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
            Toast.makeText(this,"저장성공",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"저장실패 : 여행일정 정보 수정에서 배경이미지를 다시 설정해주세요",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        Intent goBackIntent = new Intent(OfflineNewPlanActivity.this, OfflineMainActivity.class);
        goBackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goBackIntent);
    }
}
