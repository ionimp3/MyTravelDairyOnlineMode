package PlanFolder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lmh.mytraveldairy.MainActivity;
import com.lmh.mytraveldairy.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import MyProgressDialog.MyProgressDialogActivity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ClickPlanActivity extends AppCompatActivity {
    MyProgressDialogActivity myProgressDialogActivity;

    private Toolbar toolbar;

    ImageView imageClickPlanBg;
    EditText edClickPlanTitleName,edClickPlanBudget;
    ImageButton btnClickPlanDepartDateSelected,btnClickPlanEndDateSelected,btnEditMode;
    TextView textClickPlanDepartDate,textClickPlanEndDate,textClickCountryTag,textClickCityTag,textClickPlaceTag;

    private String departDate,endDate,resultDecimalFormat="";;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    int i = 0, j = 0;

    private String planKey;
    private String mInfoTB_Id,mInfoTB_budget,mInfoTB_planDepartDate,mInfoTB_planEndDate,mInfoTB_planName,mInfoTB_planTags,mInfoTB_titleImage,mInfoTB_titleImageFileName;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    private Uri ImageUri,resultUri;
    private Uri uri;
    private String cropAlready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_plan);

        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        //메인액티비티에서 선택한 여행일정 key 값 가져오기
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        planKey = getIntent().getExtras().get("planKeyFromMain").toString();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("InfoTB").child(planKey);

        imageClickPlanBg = (ImageView) findViewById(R.id.image_click_plan_bg);
        edClickPlanTitleName = (EditText) findViewById(R.id.ed_click_plan_title_name);
        edClickPlanBudget = (EditText) findViewById(R.id.ed_click_plan_budget);
        textClickPlanDepartDate = (TextView) findViewById(R.id.text_click_plan_depart_date);
        textClickPlanEndDate = (TextView) findViewById(R.id.text_click_plan_end_date);
        btnClickPlanDepartDateSelected = (ImageButton) findViewById(R.id.btn_click_plan_depart_date_selected);
        btnClickPlanEndDateSelected = (ImageButton) findViewById(R.id.btn_click_plan_end_date_selected);
        textClickCountryTag = (TextView) findViewById(R.id.text_click_country_tag);
        edClickPlanBudget.addTextChangedListener(textWatcher);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    mInfoTB_Id = dataSnapshot.child("InfoTB_Id").getValue().toString();
                    mInfoTB_budget = dataSnapshot.child("InfoTB_budget").getValue().toString();
                    mInfoTB_planDepartDate = dataSnapshot.child("InfoTB_planDepartDate").getValue().toString();
                    mInfoTB_planEndDate = dataSnapshot.child("InfoTB_planEndDate").getValue().toString();
                    mInfoTB_planName = dataSnapshot.child("InfoTB_planName").getValue().toString();
                    mInfoTB_planTags = dataSnapshot.child("InfoTB_planTags").getValue().toString();
                    mInfoTB_titleImage = dataSnapshot.child("InfoTB_titleImage").getValue().toString();
                    mInfoTB_titleImageFileName = dataSnapshot.child("InfoTB_titleImageFileName").getValue().toString();

                    //태그분리
                   // String[] arrayTag = mInfoTB_planTags.split(":");

                    edClickPlanTitleName.setText(mInfoTB_planName);
                    textClickPlanDepartDate.setText(mInfoTB_planDepartDate);
                    textClickPlanEndDate.setText(mInfoTB_planEndDate);
                    edClickPlanBudget.setText(mInfoTB_budget);
                    textClickCountryTag.setText(mInfoTB_planTags);

                    Picasso.get().load(mInfoTB_titleImage).into(imageClickPlanBg);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                edClickPlanBudget.setText(resultDecimalFormat);
                edClickPlanBudget.setSelection(resultDecimalFormat.length());
                String originalBuget = edClickPlanBudget.getText().toString();
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //시작시 버튼잠그기
        //btnEditMode.setBackgroundResource(R.drawable.ic_lock);
        imageClickPlanBg.setClickable(false);
        edClickPlanTitleName.setEnabled(false);
        edClickPlanBudget.setEnabled(false);
        btnClickPlanDepartDateSelected.setClickable(false);
        btnClickPlanEndDateSelected.setClickable(false);
        textClickCountryTag.setClickable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_click_plan, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent gotoMainIntent = new Intent(this, MainActivity.class);
                //gotoMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotoMainIntent);
                finish();
                break;
            case R.id.click_curr_changed:

                //validateNewPlanInfo();
                break;
            case R.id.btn_appbar_setting:
                //validateNewPlanInfo();
                if (!btnClickPlanDepartDateSelected.isClickable()){
                    Toast.makeText(this,"현재 뷰모드입니다.(자물쇠아이콘)",Toast.LENGTH_SHORT).show();
                } else {
                    DeleteCurrentPlan();
                }
                break;

            case R.id.click_edit_Mode:
                btn_action_edit_mode();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteCurrentPlan() {
        //업로드 이미지 삭제..아직 미구현
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference deletePath = storageReference.child(currentUserId).child("images").child(mInfoTB_titleImageFileName);
        deletePath.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //UpLoadFileListTB 데이터 먼저삭제 후 db 진행
                    DatabaseReference removeFileListAtUploadFileListTB = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UpLoadFileListTB").child(mInfoTB_Id);
                    removeFileListAtUploadFileListTB.removeValue();
                    //infoTb 데이터 삭제
                    databaseReference.removeValue();
                    Toast.makeText(ClickPlanActivity.this,"여행일정을 삭제하였읍니다.",Toast.LENGTH_SHORT).show();
                    Intent goBackIntent = new Intent(ClickPlanActivity.this, MainActivity.class);
                    //goBackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(goBackIntent);
                    finish();
                } else {
                    Toast.makeText(ClickPlanActivity.this,"여행일정을 삭제실패, 재 시도해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void btn_action_edit_mode() {
        //edit모드 뷰모드전환 : 버튼들 상태 변경
        if (!btnClickPlanDepartDateSelected.isClickable()){
            //btnEditMode.setBackgroundResource(R.drawable.ic_unlock);
            imageClickPlanBg.setClickable(true);
            edClickPlanTitleName.setEnabled(true);
            edClickPlanBudget.setEnabled(true);
            btnClickPlanDepartDateSelected.setClickable(true);
            btnClickPlanEndDateSelected.setClickable(true);
            textClickCountryTag.setClickable(true);

        } else {
            //btnEditMode.setBackgroundResource(R.drawable.ic_lock);
            imageClickPlanBg.setClickable(false);
            edClickPlanTitleName.setEnabled(false);
            edClickPlanBudget.setEnabled(false);
            btnClickPlanDepartDateSelected.setClickable(false);
            btnClickPlanEndDateSelected.setClickable(false);
            textClickCountryTag.setClickable(false);

        }
    }

    public void image_action_click_plan_bg(View view) {
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
                .start(ClickPlanActivity.this);
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
                imageClickPlanBg.setImageURI(resultUri);
                cropAlready = "y";
            }
        }
    }

    public void btn_action_click_end_date_selected(View view) {
        final Calendar endDateCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDateCalendar.set(Calendar.YEAR,year);
                endDateCalendar.set(Calendar.MONTH,month);
                endDateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd (EEE)");
                textClickPlanEndDate.setText(simpleDateFormat.format(endDateCalendar.getTime()));
                j=1;
            }
        };
        new DatePickerDialog(this,dateSetListener,endDateCalendar.get(Calendar.YEAR),endDateCalendar.get(Calendar.MONTH),endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void btn_action_click_depart_date_selected(View view) {
        final Calendar departDateCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                departDateCalendar.set(Calendar.YEAR,year);
                departDateCalendar.set(Calendar.MONTH,month);
                departDateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd (EEE)");
                textClickPlanDepartDate.setText(simpleDateFormat.format(departDateCalendar.getTime()));
                i =1;
            }
        };
        new DatePickerDialog(this,dateSetListener,departDateCalendar.get(Calendar.YEAR),departDateCalendar.get(Calendar.MONTH),departDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent goBackIntent = new Intent(ClickPlanActivity.this, MainActivity.class);
        goBackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goBackIntent);
    }
}
