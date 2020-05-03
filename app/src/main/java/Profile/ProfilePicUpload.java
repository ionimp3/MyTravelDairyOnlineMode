package Profile;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lmh.mytraveldairyjava.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.time.LocalDateTime;

import Common.OnBoarding;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ProfilePicUpload extends AppCompatActivity {
    ActionBar actionBar;
    Toolbar toolbar;

    ImageView selectedImage;
    Button cameraBtn, galleryBtn;
    StorageReference storageReference;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference UserRef;
    StorageReference UserProfileImageRef;

    ProgressDialog dialog1;

    final static int GET_GALLERY_IMAGE = 200;
    final static int Gallery_Pick = 1;

    //안드로이드 개발자홈피에서 가져온 코드에서 파일저장위치 선언부만
    //글로벌위치로 이동함함

    Uri uri;
    Intent intent;

    String akey, ckey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepicuploadlayout);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle("닉네임변경");

        Intent intent = getIntent();
        akey = intent.getStringExtra("profilePicture_Send");
        ckey = intent.getStringExtra("selectMailPrimaryKey_Send");



        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child(ckey).child("userProfile");
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child(ckey).child("userProfileImages");


        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);


        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //github의 라이브러시 사용법을 따라하자..그게 맞음
                CropImage.activity()
                        .start(ProfilePicUpload.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 0);
            } else {
                CropImage.activity(imageuri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setMultiTouchEnabled(true)
                        .start(this);
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    //StorageReference filePath = UserProfileImageRef.child(nameUnique+"Profileimag");
                    selectedImage.setImageURI(result.getUri());


                    Toast.makeText(this, "이미지 크롭후 가져오기 성공", Toast.LENGTH_SHORT).show();
                    //Intent goBackProfileIntent = new Intent(ProfilePicUpload.this,ProfileActivity.class);
                    //goBackProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivity(goBackProfileIntent);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent userdashboardIntent = new Intent(ProfilePicUpload.this, OnBoarding.class);
            userdashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(userdashboardIntent);
            finish();
        } else {
            //실제접속이되엇는지 확인하는 메소드..getUid() 함수 오류
        }
    }

    private void showProcessDialog1() {

        dialog1 = new ProgressDialog(ProfilePicUpload.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setTitle("닉네임변경");
        dialog1.setMessage("DB에 닉네임을 변경중입니다...");
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(true);
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarcustum, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 뒤로가기 버튼 눌렀을 때
                Intent UpdateNicNameiIntent = new Intent(this, ProfileActivity.class);
                UpdateNicNameiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(UpdateNicNameiIntent);
                return true;
            }
            case R.id.curr_changed: { // 오른쪽 상단 버튼 눌렀을 때
                showProcessDialog1();
                Toast.makeText(ProfilePicUpload.this, "프로파일포토 업로드진행 ", Toast.LENGTH_SHORT).show();
                dialog1.dismiss();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
