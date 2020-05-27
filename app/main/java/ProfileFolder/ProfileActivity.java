package ProfileFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lmh.mytraveldairy.MainActivity;
import com.lmh.mytraveldairy.R;
import com.lmh.mytraveldairy.databinding.ActivityProfileBinding;
import com.squareup.picasso.Picasso;

import CommonFolder.TravelDairyDescriptionActivity;
import OnBoarding.OnBoardingActivity;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding activityProfileBinding;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    DatabaseReference UserRef;

    private String UserTB_nicName_fromdb, UserTB_coverImage_fromdb, UserTB_profileImage_fromdb,UserTB_coverImageKey_fromdb,UserTB_profileImageKey_fromdb;
    int i = 0;
    int j = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        //뷰바인딩사용하면 id에서 특수문자가 사라짐. 예 test_test -->testtest

        firebaseAuth = FirebaseAuth.getInstance();


    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Intent profileIntent = new Intent(ProfileActivity.this, OnBoardingActivity.class);
            profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(profileIntent);
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
                    Intent profileIntent = new Intent(ProfileActivity.this, OnBoardingActivity.class);
                    profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(profileIntent);
                    finish();
                } else {
                    //Toast.makeText(ProfileActivity.this,"실제접속했어요",Toast.LENGTH_SHORT).show();
                    isUserCallData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void isUserCallData() {
        final String current_User_Id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(current_User_Id + "/UserTB");
        Query checkSeleMailPk = reference.orderByChild("UserTB_Id");
        checkSeleMailPk.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //userDashboard 코드보고, 컬럼이 미존재시 오류 처리 할것,,안하고 컬럼 미존재시 앱크래시 남
                    UserTB_nicName_fromdb = dataSnapshot.child("UserTB_nicName").getValue(String.class);
                    UserTB_coverImage_fromdb = dataSnapshot.child("UserTB_coverImage").getValue(String.class);
                    UserTB_profileImage_fromdb = dataSnapshot.child("UserTB_profileImage").getValue(String.class);
                    UserTB_coverImageKey_fromdb = dataSnapshot.child("UserTB_coverImageKey").getValue(String.class);
                    UserTB_profileImageKey_fromdb = dataSnapshot.child("UserTB_profileImageKey").getValue(String.class);

                    String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    activityProfileBinding.textNicName.setText(UserTB_nicName_fromdb);
                    activityProfileBinding.textEmailId.setText(user);

                    profileImageShow();
                    coverImageShow();

                } else {
                    //
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void profileImageShow() {
        if (UserTB_profileImage_fromdb.trim().equals("NA")) {
            activityProfileBinding.imageProfile.setImageResource(R.drawable.ic_account_circle_black);
            i = 1;
        } else if (UserTB_profileImage_fromdb.trim().length() > 20 && i == 0) {
            Picasso.get().load(UserTB_profileImage_fromdb).into(activityProfileBinding.imageProfile);
            i = 5;
            //Toast.makeText(this,"정보있음",Toast.LENGTH_SHORT).show();
        } else {
            String currentUser = firebaseAuth.getCurrentUser().getUid();
            UserRef = FirebaseDatabase.getInstance().getReference().child(currentUser).child("UserTB");
            UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String image = dataSnapshot.child("UserTB_profileImage").getValue().toString();
                        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                        Picasso.get().load(image).into(activityProfileBinding.imageProfile);
                        i = 5;
                    } else {
                        //
                        i = 0;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }
    private void coverImageShow() {
        String currentUser = firebaseAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child(currentUser).child("UserTB");
        if (UserTB_coverImage_fromdb.trim().equals("NA")) {
            activityProfileBinding.bgCoverImage.setImageResource(R.drawable.bg_drawer_header);
            i = 1;
        } else if (UserTB_coverImage_fromdb.trim().length() > 20 && i == 0) {
            Picasso.get().load(UserTB_coverImage_fromdb).into(activityProfileBinding.bgCoverImage);
            i = 5;
            //Toast.makeText(this,"정보있음",Toast.LENGTH_SHORT).show();
        } else {
            UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String image = dataSnapshot.child("UserTB_coverImage").getValue().toString();
                        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                        Picasso.get().load(image).into(activityProfileBinding.bgCoverImage);
                        i = 5;
                    } else {
                        //
                        i = 0;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }



    public void btn_action_now_user_status(View view) {
        Toast.makeText(this,"개발예정입니다.!!!",Toast.LENGTH_SHORT).show();
    }

    public void btn_action_push_alarm_select(View view) {
        Toast.makeText(this,"개발예정입니다.!!!",Toast.LENGTH_SHORT).show();
    }

    public void btn_action_change_profile_pic(View view) {
        Intent intent = new Intent(ProfileActivity.this,ProfileImageActivity.class);
        intent.putExtra("UserTB_nicName_Send", UserTB_nicName_fromdb);
        intent.putExtra("UserTB_profileImage_Send", UserTB_profileImage_fromdb);
        intent.putExtra("UserTB_coverImage_Send", UserTB_coverImage_fromdb);
        intent.putExtra("UserTB_coverImageKeye_Send", UserTB_coverImageKey_fromdb);
        intent.putExtra("UserTB_profileImageKey_Send", UserTB_profileImageKey_fromdb);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void btn_action_change_nic_name(View view) {
        Intent intent = new Intent(ProfileActivity.this, NicNameActivity.class);
        intent.putExtra("UserTB_nicName_Send", UserTB_nicName_fromdb);
        intent.putExtra("UserTB_profileImage_Send", UserTB_profileImage_fromdb);
        intent.putExtra("UserTB_coverImage_Send", UserTB_coverImage_fromdb);
        intent.putExtra("UserTB_coverImageKey_Send", UserTB_coverImageKey_fromdb);
        intent.putExtra("UserTB_profileImageKey_Send", UserTB_profileImageKey_fromdb);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void btn_action_change_cover_pic(View view) {
        Intent intent = new Intent(ProfileActivity.this, CoverImageActivity.class);
        intent.putExtra("UserTB_nicName_Send", UserTB_nicName_fromdb);
        intent.putExtra("UserTB_profileImage_Send", UserTB_profileImage_fromdb);
        intent.putExtra("UserTB_coverImage_Send", UserTB_coverImage_fromdb);
        intent.putExtra("UserTB_coverImageKey_Send", UserTB_coverImageKey_fromdb);
        intent.putExtra("UserTB_profileImageKey_Send", UserTB_profileImageKey_fromdb);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void btn_action_link_site(View view) {
        Toast.makeText(this,"개발예정입니다.!!!",Toast.LENGTH_SHORT).show();
    }

    public void btn_action_description(View view) {
        Intent intent = new Intent(ProfileActivity.this, TravelDairyDescriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //화면 back 버튼 눌렀을때처리
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent updateIntent = new Intent(ProfileActivity.this, MainActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(updateIntent);
    }

    public void btn_action_none(View view) {
        //임시 로 막기
    }
}
