package DashBoard;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lmh.mytraveldairyjava.R;

import Common.BackPressHandler;
import Common.OnBoarding;
import Common.SignInActivity;
import Profile.ProfileActivity;


public class UserDashboard extends AppCompatActivity {
    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView postList;
    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;
    String currentUserId;
    String psemail,tmps1,tmps2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardlayout);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        FirebaseUser user = mAuth.getCurrentUser();
        psemail = user.getEmail();
        tmps1 = psemail.replaceAll("[.]", "");
        tmps2 = tmps1.replaceAll("[@]", "");
        UsersRef = FirebaseDatabase.getInstance().getReference().child(tmps2).child("userProfile");
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child(tmps2).child("userProfileImages");


        mToolbar = (Toolbar)findViewById(R.id.dashboard_main_page_toolbar);
        setSupportActionBar(mToolbar);
        //임시
        getSupportActionBar().setTitle("Dairy");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //툴바의 뒤로가기 버튼 기능부여를 위한 세팅
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //
        //임시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view_layout);

        View navView = navigationView.inflateHeaderView(R.layout.menu_header);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });

    }
    //실제등록접속되었는지 DB에 쿼리 확인
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent userDashBoardIntent = new Intent(UserDashboard.this, OnBoarding.class);
            userDashBoardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(userDashBoardIntent);
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
                    Intent userDashBoardIntent = new Intent(UserDashboard.this, OnBoarding.class);
                    userDashBoardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(userDashBoardIntent);
                    finish();
                }
                else{
                    //MessageToast.message(UpdateNicName.this,"실제접속했어요");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //툴바 뒤로가기 버튼에 동작을 부여
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_setting:
                Intent ProfileIntent = new Intent(this, ProfileActivity.class);
                ProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ProfileIntent);
                finish();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(UserDashboard.this, SignInActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 앱을 종료");
        }
    }
}
