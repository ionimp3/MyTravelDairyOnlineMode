package SignFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lmh.mytraveldairy.MainActivity;
import com.lmh.mytraveldairy.R;
import com.lmh.mytraveldairy.TestMainActivity;
import com.lmh.mytraveldairy.databinding.ActivitySignInBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

import MyProgressDialog.MyProgressDialogActivity;
import OnBoarding.OnBoardingActivity;

public class SignInActivity extends AppCompatActivity {

    ProgressDialog dialog, dialog1;
    MyProgressDialogActivity myProgressDialogActivity;

    private FirebaseAuth firebaseAuth,mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    ActivitySignInBinding activitySignInBinding;
    // 구글sign in
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference uploadeFileListRef;
    private String currentUserId,fileUpLoadKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(activitySignInBinding.getRoot());
        //setContentView(R.layout.activity_sign_in);

        //firebase 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        // 버튼텍스트에 밑줄긋기
        activitySignInBinding.btnRestPassword.setPaintFlags(activitySignInBinding.btnRestPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //


    }

    //validation 메일
    private Boolean validateEmailid() {
        String validemail = activitySignInBinding.editEmailId.getEditableText().toString().trim();
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]";
        if (validemail.isEmpty()) {
            activitySignInBinding.editEmailId.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(validemail).matches()) {
            activitySignInBinding.editEmailId.setError("메일형식이 잘 못 되었읍니다.");
            return false;
        } else {
            activitySignInBinding.editEmailId.setError(null);
            return true;
        }

    }

    //validation  패스워드
    private Boolean validatePassword() {
        String validPassword = activitySignInBinding.editPassword.getEditableText().toString().trim();
        String passwordPattern = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z@#$%^&+=_~!])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (validPassword.isEmpty()) {
            activitySignInBinding.editPassword.setError("공백은 허용하지 않습니다.");
            return false;
        } else if (!validPassword.matches(passwordPattern)) {
            activitySignInBinding.editPassword.setError("패스워드는 최소6자리이상입니다..");
            return false;
        } else {
            activitySignInBinding.editPassword.setError(null);
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
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
                if (dataSnapshot.hasChild(current_User_Id)) {
                    Intent gotoMainIntent = new Intent(SignInActivity.this, MainActivity.class);
                    gotoMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(gotoMainIntent);
                    finish();
                } else {
                    //로그인기록이 폰에 남아있으나, 실제 탈퇴한 회원;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void btn_action_sign_in(View view) {
        if (!validateEmailid() | !validatePassword()) {
            return;
        }
        final String email = activitySignInBinding.editEmailId.getText().toString().trim();
        String pwd = activitySignInBinding.editPassword.getText().toString().trim();

        myProgressDialogActivity = new MyProgressDialogActivity(this);
        myProgressDialogActivity.show();
        firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "이메일 로그인 성공 !!!", Toast.LENGTH_SHORT).show();
                            //Intent goToMainIntent = new Intent(SignInActivity.this, TestMainActivity.class);
                            Intent goToMainIntent = new Intent(SignInActivity.this, MainActivity.class);
                            goToMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goToMainIntent);
                            finish();
                            myProgressDialogActivity.dismiss();
                        } else {
                            myProgressDialogActivity.dismiss();
                            Toast.makeText(SignInActivity.this, "미 가입 사용자이거나 입력정보가 틀렸읍니다!! ", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(SignInActivity.this, "등록되지 않은 사용자이거나, 메일아이디 또는 비밀번호가 틀렸읍니다.!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void btn_action_reset_password(View view) {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SignInActivity.this);
        alert_confirm.setTitle("패스워드리셋요청");
        alert_confirm.setMessage("입력한 메일아이디로 패스워드 리셋 요청 메일을 보냅니다!!");
        alert_confirm.setCancelable(false);
        alert_confirm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String emailAddress = "" + activitySignInBinding.editEmailId.getEditableText().toString();
                        myProgressDialogActivity = new MyProgressDialogActivity(SignInActivity.this);
                        myProgressDialogActivity.show();
                        if (!validateEmailid()) {
                            myProgressDialogActivity.dismiss();
                            Toast.makeText(SignInActivity.this, "메일아이디 입력 후 요청해주세요....", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // [START send_password_reset]
                        firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            myProgressDialogActivity.dismiss();
                                            Toast.makeText(SignInActivity.this, "비밀번호재설정메일을 발송하였읍니다....", Toast.LENGTH_SHORT).show();
                                        } else {
                                            myProgressDialogActivity.dismiss();
                                            Toast.makeText(SignInActivity.this, "존재하지 않는 사용자 메일입니다...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
        );
        alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SignInActivity.this, "취소", Toast.LENGTH_LONG).show();
            }
        });
        alert_confirm.show();
    }


    public void btn_action_google_sign_in(View view) {
        myProgressDialogActivity = new MyProgressDialogActivity(this);
        myProgressDialogActivity.show();
        //Toast.makeText(SignInActivity.this, "아직 미구현 상태입니다 !!!", Toast.LENGTH_SHORT).show();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                myProgressDialogActivity.dismiss();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,"구글계정을 확인 해주세요",Toast.LENGTH_SHORT).show();
                myProgressDialogActivity.dismiss();
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        myProgressDialogActivity = new MyProgressDialogActivity(this);
        myProgressDialogActivity.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignInActivity.this,"구글계정으로 로그인 성공",Toast.LENGTH_SHORT).show();
                            CheckUserExistanceForGoogle();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this,"구글인증에 실패 하였읍니다. 다시 진행해주세요",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void CheckUserExistanceForGoogle() {
        final String current_User_Id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference GoogleUserCheck = FirebaseDatabase.getInstance().getReference();
        myProgressDialogActivity = new MyProgressDialogActivity(this);
        myProgressDialogActivity.show();
        GoogleUserCheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //실제 데이터베이스를 조회해서 이메일 id 가 있는지 확인
                if (dataSnapshot.hasChild(current_User_Id)) {
                    myProgressDialogActivity.dismiss();
                    Intent gotoMainIntent = new Intent(SignInActivity.this, MainActivity.class);
                    gotoMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(gotoMainIntent);
                    finish();
                } else {
                    //구글로 회원등록시 기본 정보 세팅
                    firstUserSetData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void firstUserSetData() {
        //회원가입 완료 후 후 DB_SETTING_TB 신규 레크드 등록
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
        String timeStampCreateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy",Locale.ENGLISH));
        fileUpLoadKey = (LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //displayMailId = user.getEmail();

        //db노드 저장할 노드지정
        databaseReference = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UserTB");
        HashMap userProfileMap = new HashMap();
        userProfileMap.put("UserTB_Id",currentUserId);
        userProfileMap.put("UserTB_coverImage","NA");
        userProfileMap.put("UserTB_coverImageKey",fileUpLoadKey+"cover");
        userProfileMap.put("UserTB_emailId",firebaseUser.getEmail());
        userProfileMap.put("UserTB_nicName","닉네임");
        userProfileMap.put("UserTB_nowUserStatus","1");
        userProfileMap.put("UserTB_profileImage","NA");
        userProfileMap.put("UserTB_profileImageKey",fileUpLoadKey+"profileimage");
        userProfileMap.put("UserTB_pushAlarmSelected","N");
        userProfileMap.put("UserTB_timeStampCreateTime",timeStampCreateTime);
        userProfileMap.put("UserTB_timeStampUpdateTime",timeStampUpdateTime);
        databaseReference.updateChildren(userProfileMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                        saveToUploadFileListTB1();
                        saveToUploadFileListTB2();
                        GotoMain();
                        //Toast.makeText(getApplicationContext(),"회원가입 및 기본정설정완료!!!!!!",Toast.LENGTH_SHORT).show();
                        myProgressDialogActivity.dismiss();
                }
                else {
                    String message = task.getException().getMessage();
                    Toast.makeText(SignInActivity.this,"기본정보 설정실패,회원탈퇴후 다시 가입해주세요  : " + message,Toast.LENGTH_SHORT).show();
                    GotoMain();
                    myProgressDialogActivity.dismiss();
                }
            }
        });
    }
    private void saveToUploadFileListTB1() {
        //showProcessDialog1();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        HashMap saveToFileListMap = new HashMap();
        saveToFileListMap.put("UpLoadFileListTB_fileName","profileimage.jpg");
        saveToFileListMap.put("UpLoadFileListTB_filePath","/UserTB");
        saveToFileListMap.put("UpLoadFileListTB_UserTB_profileImageKey",fileUpLoadKey+"profileimage");
        saveToFileListMap.put("UpLoadFileListTB_timeStampUpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
        uploadeFileListRef = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UpLoadFileListTB").child(fileUpLoadKey+"profileimage");
        uploadeFileListRef.updateChildren(saveToFileListMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(SignInActivity.this, "업로드파일리스트를 생성완료!!", Toast.LENGTH_SHORT).show();
                    // dialog1.dismiss();
                } else {
                    Toast.makeText(SignInActivity.this, "업로드파일리스트 생성실패, 다시 진행해주세요" , Toast.LENGTH_SHORT).show();
                    // dialog1.dismiss();
                }
            }
        });
    }
    private void saveToUploadFileListTB2() {
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        //showProcessDialog1();
        HashMap saveToFileListMap = new HashMap();
        saveToFileListMap.put("UpLoadFileListTB_fileName","coverimage.jpg");
        saveToFileListMap.put("UpLoadFileListTB_filePath","/UserTB");
        saveToFileListMap.put("UpLoadFileListTB_UserTB_coverImageKey",fileUpLoadKey+"cover");
        saveToFileListMap.put("UpLoadFileListTB_timeStampUpdateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH)));
        uploadeFileListRef = FirebaseDatabase.getInstance().getReference().child(currentUserId).child("UpLoadFileListTB").child(fileUpLoadKey+"cover");
        uploadeFileListRef.updateChildren(saveToFileListMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(SignInActivity.this, "업로드파일리스트 생성완료!!", Toast.LENGTH_SHORT).show();
                    //dialog1.dismiss();
                } else {
                    Toast.makeText(SignInActivity.this, "업로드파일리스트 생성실패, 다시 진행해주세요" , Toast.LENGTH_SHORT).show();
                    // dialog1.dismiss();
                }
            }
        });
    }
    private void GotoMain() {
        Intent gotoMainIntent = new Intent(SignInActivity.this, MainActivity.class);
        gotoMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotoMainIntent);
        finish();
    }


    public void btn_action_sign_up(View view) {
        Intent goToSignUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
        goToSignUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToSignUpIntent);
        finish();
    }

    //화면 back 버튼 눌렀을때처리
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //맨처음화면으로 이동 하거나 앱종료
        Intent goToOnboardingIntent = new Intent(SignInActivity.this, OnBoardingActivity.class);
        startActivity(goToOnboardingIntent);
        finish();
    }
}
