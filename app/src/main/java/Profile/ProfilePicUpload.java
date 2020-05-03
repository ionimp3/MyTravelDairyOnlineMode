package Profile;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.lmh.mytraveldairyjava.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Common.MessageToast;
import Common.OnBoarding;
import DashBoard.UserDashboard;

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


    Uri uri;
    Intent intent;

    private String akey, tmps1, ckey, aname, timeStampUpdateTime;
    private Uri resultUri;
    private Uri test12;

    private  int i = 0;

    String profilePicture_FromDB;


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

        if (akey.trim().length() < 20) {
            selectedImage.setImageResource(R.drawable.ic_account_circle_black);
            i = 1;
        }
        else if (akey.trim().length() > 20 && i < 1)
        {
            Picasso.get().load(akey).into(selectedImage);
            i = 5;
            //Toast.makeText(this,"정보있음",Toast.LENGTH_SHORT).show();
        }
        else
            {
                UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String image = dataSnapshot.child("profilePicture").getValue().toString();
                            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                            Picasso.get().load(image).into(selectedImage);
                            i = 5;
                        } else {
                            //
                            Log.d("tag", "불러오기 실패 ");
                            i = 0;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        }

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
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                CropImage.activity(imageuri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setMultiTouchEnabled(true)
                        .start(this);
                final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                resultUri = result.getUri();
                selectedImage.setImageURI(resultUri);
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
        dialog1.setTitle("이미지업로드");
        dialog1.setMessage("스토리지,DB에 업로중입니다....");
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
                //스토리지 저장
                selectedImage.setImageURI(resultUri);
                showProcessDialog1();
                final StorageReference filePath = UserProfileImageRef.child("Profileimage.jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata()!=null){
                            if (taskSnapshot.getMetadata().getReference() != null){
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downloadUrl = uri.toString();
                                        //해시맵으로 timeStampUpdateTime..도 함꼐 업데이트..추가할것
                                        UserRef.child("profilePicture").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    i = 5;
                                                    Toast.makeText(ProfilePicUpload.this, "이미지 스토리지/DB저장완료", Toast.LENGTH_SHORT).show();
                                                    dialog1.dismiss();
                                                } else {
                                                    selectedImage.setImageResource(R.drawable.ic_account_circle_black);
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(ProfilePicUpload.this, "이미지 DB저장 실패!! 다시 진행해주세요" + message, Toast.LENGTH_SHORT).show();
                                                    dialog1.dismiss();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent UpdateNicNameiIntent = new Intent(ProfilePicUpload.this, ProfileActivity.class);
        UpdateNicNameiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(UpdateNicNameiIntent);
        // 현재액티비의 루트 액티비티까지 종료시켜라
        // 루트 설정화면을 부른 이전 메뉴 액티비티
        // 드로워 화면 만들면 드로워 화면으로 변경해라..아니면 그대로 대시보드로 이동
    }
}