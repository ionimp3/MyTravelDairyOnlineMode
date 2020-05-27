package PlanFolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lmh.mytraveldairy.MainActivity;
import com.lmh.mytraveldairy.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import MyProgressDialog.MyProgressDialogActivity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class NewPlanActivity extends AppCompatActivity {
    MyProgressDialogActivity myProgressDialogActivity;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference UserRf,InfoRf,uploadeFileListRef;

    private Toolbar toolbar;
    ImageView imagePlanBg;
    EditText edPlanTitleName,edPlanBudget;
    ImageButton btnPlanDepartDateSelected,btnPlanEndDateSelected;
    TextView textPlanDepartDate,textPlanEndDate,textCountryTag,textCityTag,textPlaceTag;

    private static final int GALLERY_PICK = 1;
    private Uri ImageUri,resultUri;
    private Uri uri;
    private String cropAlready;

    private String medPlanTitleName,mtextPlanDepartDate,mtextPlanEndDate,mtextCountryTag,mtextCityTag,mtextPlaceTag;
    private String saveCurrentDateForFileName,saveCurrentTimeForFileName,saveCurrentForFileName;
    private String currentUserId,downloadUrl,UserTB_nicNameFromDB,UserTB_profileImageFromDB,UserTB_nowUserStatusFromDB;
    private String edPlanTitleNameFromView,edPlanBudgetFromView,textPlanDepartDateFromView,textPlanEndDateFromView;
    private String textCountryTagFromView;

    private String departDate,endDate,resultDecimalFormat="";;
    private int i = 0;
    private int j = 0;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);

        toolbar =(Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase 초기화
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getUid();
        UserRf = FirebaseDatabase.getInstance().getReference().child(currentUserId);
        InfoRf = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("InfoTB");
        uploadeFileListRef = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UpLoadFileListTB");

        imagePlanBg = (ImageView) findViewById(R.id.image_click_plan_bg);
        edPlanTitleName = (EditText) findViewById(R.id.ed_click_plan_title_name);
        edPlanBudget = (EditText) findViewById(R.id.ed_click_plan_budget);
        textPlanDepartDate = (TextView) findViewById(R.id.text_click_plan_depart_date);
        textPlanEndDate = (TextView) findViewById(R.id.text_click_plan_end_date);
        btnPlanDepartDateSelected = (ImageButton) findViewById(R.id.btn_click_plan_depart_date_selected);
        btnPlanEndDateSelected = (ImageButton) findViewById(R.id.btn_click_plan_end_date_selected);
        textCountryTag = (TextView) findViewById(R.id.text_click_country_tag);
        edPlanBudget.addTextChangedListener(textWatcher);

        //초기값지정
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

    //통화표시 할때 콤마 추가
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_new_plan, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent gotoMainIntent = new Intent(this, MainActivity.class);
                gotoMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotoMainIntent);
                finish();
                break;
            case R.id.new_plan_curr_changed:
                validateNewPlanInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
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
    private void validateNewPlanInfo() {
        medPlanTitleName = edPlanTitleName.getText().toString();
        mtextPlanDepartDate = textPlanDepartDate.getText().toString();
        mtextPlanEndDate = textPlanEndDate.getText().toString();
        mtextCountryTag = textCountryTag.getText().toString();

        //SimpleDateFormat compareDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date departSelected = null;
        Date endSelected = null;
        try {
            SimpleDateFormat compareDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
            departSelected = compareDateFormatDate.parse(mtextPlanDepartDate);
            endSelected = compareDateFormatDate.parse(mtextPlanEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int compareDate = departSelected.compareTo(endSelected);

        if (resultUri == null){
            Toast.makeText(this,"배경이미지를 선택해주세요.",Toast.LENGTH_SHORT).show();
            //InfoTB_titleImage 에서 넣을 값으로 "NA"
        }
        else if (TextUtils.isEmpty(medPlanTitleName)){
            Toast.makeText(this,"여행일정 제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
            //InfoTB_titleImage 에서 넣을 값으로 "NA"
        }
        else if (TextUtils.isEmpty(mtextPlanDepartDate)){
            Toast.makeText(this,"출발일을 선택해주세요.",Toast.LENGTH_SHORT).show();
            //InfoTB_titleImage 에서 넣을 값으로 "NA"
        }
        else if (TextUtils.isEmpty(mtextPlanEndDate)){
            Toast.makeText(this,"도착일을 선택해주세요.",Toast.LENGTH_SHORT).show();
        }
        else if (compareDate > 0){
            Toast.makeText(this,"도착일이 출발일 보다 이전 입니다..다시 선택해주세요",Toast.LENGTH_SHORT).show();
        }
/*        else if (TextUtils.isEmpty(mtextCountryTag)){
            Toast.makeText(this,"여행할 나라를 선택해주세요.",Toast.LENGTH_SHORT).show();
            //InfoTB_titleImage 에서 넣을 값으로 "NA"
        }
        else if (TextUtils.isEmpty(mtextCityTag)){
            Toast.makeText(this,"여행할 도시를 선택해주세요.",Toast.LENGTH_SHORT).show();
            //InfoTB_titleImage 에서 넣을 값으로 "NA"
        }*/
/*        else if (TextUtils.isEmpty(mtextPlaceTag)){
            Toast.makeText(this,"여행지를 선택해주세요.",Toast.LENGTH_SHORT).show();
            //InfoTB_titleImage 에서 넣을 값으로 "NA"
        }*/
        else {
            //trim 안되는 공백제거 아래문구
            String validPlanTitleName = medPlanTitleName.replaceAll("\\p{Z}", "");
            String titlePattern = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣_-]*$";
            //Boolean validResult = validPlanTitleName.matches(titlePattern);
            if (validPlanTitleName.isEmpty()) {
                edPlanTitleName.setError("공백 제외한 최소 한글자 이상 입력해 주세요.");
            } else if (!validPlanTitleName.matches(titlePattern)==true){
                edPlanTitleName.setError("사용할수없는 특수문자가 있읍니다.");
            } else {
                //validResult = false;
                storingImageToFirebaseStorage();
                edPlanTitleName.setError(null);
            }
        }
    }
    private void storingImageToFirebaseStorage() {
        //렌덤하게 유니크 파일명이나 id 만들기 -> 테이블명+날짜시간+지정파일명
        Calendar calendarDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyyyMMdd");
        saveCurrentDateForFileName = simpleDateFormatDate.format(calendarDate.getTime());
        Calendar calendarTime = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HHmmss");
        saveCurrentTimeForFileName = simpleDateFormatTime.format(calendarTime.getTime());
        saveCurrentForFileName = "InfoTB"+saveCurrentDateForFileName+saveCurrentTimeForFileName;
        //
        myProgressDialogActivity = new MyProgressDialogActivity(this);
        myProgressDialogActivity.show();

        currentUserId = firebaseAuth.getUid();
        StorageReference filePath = storageReference.child(currentUserId).child("images").child(saveCurrentForFileName+"plancoverimage_"+resultUri.getLastPathSegment());
        filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null){
                    if (taskSnapshot.getMetadata().getReference() != null){
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                Toast.makeText(NewPlanActivity.this,"일정커버이미지를 업로드 완료 하였읍니다.",Toast.LENGTH_SHORT).show();
                                savingNewPlanToInfoTbAtDatabase();
                            }
                        });
                    } else {
                        Toast.makeText(NewPlanActivity.this,"업로드 실패 : 재진행해주세요",Toast.LENGTH_SHORT).show();
                        myProgressDialogActivity.dismiss();
                    }
                } else {
                    Toast.makeText(NewPlanActivity.this,"업로드 실패 : 재진행해주세요",Toast.LENGTH_SHORT).show();
                    myProgressDialogActivity.dismiss();
                }
            }
        });
    }

    private void savingNewPlanToInfoTbAtDatabase() {

        UserRf.child("UserTB").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                edPlanTitleNameFromView = edPlanTitleName.getText().toString();
                edPlanBudgetFromView = edPlanBudget.getText().toString();
                textPlanDepartDateFromView = textPlanDepartDate.getText().toString();
                textPlanEndDateFromView = textPlanEndDate.getText().toString();
                textCountryTagFromView = textCountryTag.getText().toString();

                if (dataSnapshot.exists()){
                    UserTB_nowUserStatusFromDB = dataSnapshot.child("UserTB_nowUserStatus").getValue().toString();
                    UserTB_nicNameFromDB = dataSnapshot.child("UserTB_nicName").getValue().toString();
                    UserTB_profileImageFromDB = dataSnapshot.child("UserTB_profileImage").getValue().toString();

                    HashMap newPlanMap = new HashMap();
                    newPlanMap.put("InfoTB_Id",saveCurrentForFileName+currentUserId);
                    newPlanMap.put("InfoTB_planName",edPlanTitleNameFromView);
                    newPlanMap.put("InfoTB_planDepartDate",textPlanDepartDateFromView);
                    newPlanMap.put("InfoTB_planEndDate",textPlanEndDateFromView);
                    newPlanMap.put("InfoTB_budget", edPlanBudgetFromView);
                    newPlanMap.put("InfoTB_planCountry",textCountryTagFromView);
                    newPlanMap.put("InfoTB_subCurrency","NA");
                    newPlanMap.put("InfoTB_planTags",textCountryTagFromView);
                    newPlanMap.put("InfoTB_titleImagekey",saveCurrentDateForFileName+saveCurrentTimeForFileName+"titleimage");
                    newPlanMap.put("InfoTB_titleImage",downloadUrl);
                    newPlanMap.put("InfoTB_titleImageFileName",saveCurrentForFileName+"plancoverimage_"+resultUri.getLastPathSegment());
                    newPlanMap.put("InfoTB_viewCount","1");
                    newPlanMap.put("InfoTB_timeStampUpdateTime",LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
                    newPlanMap.put("InfoTB_timeStampCreateTime",LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
                    //USERTB에서 가져와 저장
                    newPlanMap.put("InfoTB_UserTB_nicName",UserTB_nicNameFromDB);
                    newPlanMap.put("InfoTB_UserTB_nowUserStatus",UserTB_nowUserStatusFromDB);
                    newPlanMap.put("InfoTB_UserTB_profileImage",UserTB_profileImageFromDB);

                    InfoRf.push().updateChildren(newPlanMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){

                                        saveToUploadFileListTB();

                                    } else {
                                        Toast.makeText(NewPlanActivity.this,"ERROR : 새로운 여행일정 생성실패 재 실행해주세요",Toast.LENGTH_SHORT).show();
                                        myProgressDialogActivity.dismiss();
                                    }
                                }
                            });
                }
                myProgressDialogActivity.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void saveToUploadFileListTB() {
        //showProcessDialog1();
        HashMap saveToFileListMap = new HashMap();
        saveToFileListMap.put("UpLoadFileListTB_UserTB_profileImageKey", "NA");
        saveToFileListMap.put("UpLoadFileListTB_fileName", saveCurrentForFileName+"plancoverimage_"+resultUri.getLastPathSegment());
        saveToFileListMap.put("UpLoadFileListTB_filePath", "/InfoTB");
        saveToFileListMap.put("UpLoadFileListTB_timeStampUpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
        uploadeFileListRef.child(saveCurrentForFileName+currentUserId).updateChildren(saveToFileListMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(NewPlanActivity.this,"새로운 여행일정을 만들었읍니다.",Toast.LENGTH_SHORT).show();
                    myProgressDialogActivity.dismiss();
                    Intent goBackIntent = new Intent(NewPlanActivity.this, MainActivity.class);
                    //goBackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goBackIntent);
                    finish();
                    //Toast.makeText(NewPlanActivity.this, "업로드 파일 리스트를 업데이트 하였읍니다 !!", Toast.LENGTH_SHORT).show();

                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(NewPlanActivity.this, "업로드 파일 리스트 저장 실패!!! 다시 진행해주세요" + message, Toast.LENGTH_SHORT).show();
                    myProgressDialogActivity.dismiss();
                }
            }
        });
    }

    //카메라 구동, 이미지 가져오기
    public void image_action_plan_bg(View view) {
        openGallery();
    }
    private void openGallery() {
/*        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_PICK);*/
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setMultiTouchEnabled(true)
                .start(NewPlanActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            imagePlanBg.setImageURI(ImageUri);
        }*/
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
    private void sendToMainActivity() {
        Intent goBackIntent = new Intent(NewPlanActivity.this, MainActivity.class);
        goBackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goBackIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent goBackIntent = new Intent(NewPlanActivity.this, MainActivity.class);
        goBackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goBackIntent);
    }
}
