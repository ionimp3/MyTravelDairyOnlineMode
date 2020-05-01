package Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lmh.mytraveldairyjava.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class ProfilePicUpload extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    ImageView selectedImage;
    Button cameraBtn, galleryBtn;
    StorageReference storageReference;

    //안드로이드 개발자홈피에서 가져온 코드에서 파일저장위치 선언부만
    //글로벌위치로 이동함함
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilepicuploadlayout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();

        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.galleryBtn);

        //storage 연결을 위한초기화화
        storageReference = FirebaseStorage.getInstance().getReference();

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();

            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);


            }
        });


    }

    //카메라퍼미션체크 및 요청, 리퀘스트코드는 아무거나 정수형 그리고 constant 변수로 변환 선태후 "ctrl + alt + c"
    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }

    }

    //퍼미션요청결과처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //openCamera();
                //위 openCamera()함수로 코딩해서 리퀘스트코드를 constant 변수를 만들때사용하고
                //실제는 안드로이드 개발자홈피에서 가져온 아래 함수를 사용
                dispatchTakePictureIntent();
            } else {
                //Toast.makeText(this,"카메라퍼미션 허용요청",Toast.LENGTH_SHORT).show();
            }
        }
    }

/*    private void openCamera() {
       // Toast.makeText(this,"카메라열기 요청",Toast.LENGTH_SHORT).show();
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }*/

    //카메라열고 찍은후 찍은 사진 캡쳐, 이미지뷰에 사진을 출력
    // selectedImage 이미지뷰변수임..상단정의한 것.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // selectedImage 이미지뷰변수임..상단정의한 것...중간에확인용
                // Bitmap image = (Bitmap) data.getExtras().get("data");
                // selectedImage.setImageBitmap(image);

                //업로드를 위한 코드
                File f = new File(currentPhotoPath);
                //selectedImage.setImageURI(Uri.fromFile(f));
                //디버그를 위한 로그남기기
                //Log.d("tag","이미지 절대경로 : " + Uri.fromFile(f));

                //개발자홈피 코드 복사 - 폰의 미디어 스캐너 호출하여 사진을 DB화 폰갤러이에 보여준다
                //아래가 없으면, 앱설치디렉토리를 찾아가서 봐야함
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                //아래코드 불필요..미리 다른데서 아래줄코드에서 정의해둠
                //File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                uploadImageToFirebase(f.getName(), contentUri);
            }
        }

        //갤러리에서 사진선택후 업로드할때 사용할 파일명생성..실제 사진이 중복생성되지는 않음..
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "선택된 갤러리 이미지 새로운 file name : " + imageFileName);
                //selectedImage.setImageURI(contentUri);

                uploadImageToFirebase(imageFileName, contentUri);
            }
        }
    }

    //업로딩 구문--여기서 파일명이랑 업로드 위치정보를 가로채서 db에 저장해야할듯
    //다른 강좌보고 수정적용용
    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "성공한 url " + uri.toString());
                        //사진보여주는 라이브러리 load 가로안이 가져올 사진 위치..외부링크주소..내부주소..등 기입
                        //아래형태로 업로드주소가 찍혀있음..
                        //https://firebasestorage.googleapis.com/v0/b/mytraveldairy-e52bf.appspot.com/o/pictures%2FJPEG_20200429_140205.jpg?alt=media&token=c69df099-0b35-4b0b-9e15-da442949d66d
                        //이 주소를 db에 넣어두고 프로필 사진과 링크..다른 방법도 찾아보자
                        Picasso.get().load(uri).into(selectedImage);
                    }
                });
                //Toast.makeText(ProfileActivity.this, "사진업로드완료", Toast.LENGTH_SHORT).show();
                Log.d("tag", "업로드성공????? " );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(ProfileActivity.this, "사진업로드실패", Toast.LENGTH_SHORT).show();
                Log.d("tag", "업로드fail????? " );
            }
        });
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(c.getType(contentUri));

    }


    //사진파일이름 만들기..유니크하게
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //촬영한 사진이 저장되는 위치, 앱설치 디렉토리..media store로 저장가능 검색필요
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //현재 공용 갤러리에 사진을 추가하는 구문(디렉토리네임 PICTURE)--29버전부터 사용불가
        // 대안이 나올때까지 앱설치디렉토리에 저장 후 갤러리에 스캔가능한 폴더에 추가 해둔다.
        //File storageDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //사진을 전송할수 있는 메소드 구문..안드로이드 개발자홈피에서 가져옴
    //아래 리퀘스트 선언 불필요...미리 만든 리퀘스코드를 사용
    //static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.lmh.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


}
