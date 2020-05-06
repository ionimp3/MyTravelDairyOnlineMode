package DashBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
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
import com.squareup.picasso.Picasso;

import Common.BackPressHandler;
import Common.MessageToast;
import Common.OnBoarding;
import Common.SignInActivity;
import PostFolder.PostNew;
import Profile.ProfileActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    //네이비게이션애니매이터 용 변수
    static final float END_SCALE = 0.7F;

    //파이어베이스
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    //drawer menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView postList;

    Button menuListIcon;
    TextView textView;
    LinearLayout contentLayout;

    Toolbar toolbar;
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentTransaction ft;
    FragmentManager fm;

    Button btncheck;

    CircleImageView headerImage;
    private TextView headerEmailId, headerNicName;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;
    String currentUserId;
    String psemail,tmps1,tmps2;

    private String showHeaderImage,showHeaderNicName,showHeaderEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboardlayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer_back_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserId = mAuth.getCurrentUser().getUid();
        FirebaseUser user = mAuth.getCurrentUser();
        psemail = user.getEmail();
        tmps1 = psemail.replaceAll("[.]", "");
        tmps2 = tmps1.replaceAll("[@]", "");
        UsersRef = FirebaseDatabase.getInstance().getReference().child(tmps2).child("userProfile");
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child(tmps2).child("userProfileImages");

        //setTitle("대시보드");
        // 툴바 왼쪽 버튼 설정
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        //supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)  // 왼쪽 버튼 이미지 설정
        //supportActionBar!!.setDisplayShowTitleEnabled(false)    // 타이틀 안보이게 하기


        //menu hooks
        menuListIcon = findViewById(R.id.appbar_area_drawerback);
        btncheck = findViewById(R.id.test_btncheck);
        drawerLayout = findViewById(R.id.drawer_layout);
        //뒤로가기 버튼 활성화시 아래문구 사용
        //actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.drawer_open,R.string.drawer_close);
        //drawerLayout.addDrawerListener(actionBarDrawerToggle);
       // actionBarDrawerToggle.syncState();
        navigationView = findViewById(R.id.navigation_view_layout);
        frameLayout = findViewById(R.id.simpleframelayout);
        tabLayout = findViewById(R.id.tab_layout);
        //탭레이웃 안에 탭투가
        tabLayout.addTab(tabLayout.newTab().setText("대시보드"));
        tabLayout.addTab(tabLayout.newTab().setText("여행루트"));
        tabLayout.addTab(tabLayout.newTab().setText("세부일정"));
        //setTitle("대시보드");

        fragment = new DashBoardMain();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.simpleframelayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new DashBoardMain();
                        break;
                    case 1:
                        fragment = new TravelRoute();
                        break;
                    case 2:
                        fragment = new DetailToDo();
                        break;
                }
                fm = getSupportFragmentManager();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.simpleframelayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        //네비 드로어 헤더는 navigationView.getHeaderView(0) 를 써야함.
        headerImage = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.h_circleimage);
        headerNicName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.h_nicname);
        headerEmailId = (TextView) navigationView.getHeaderView(0).findViewById(R.id.h_emailid);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //데이턱 불러올때 불러올 곳의 컬럼이 있는지 확인, 없을때 앱 크래쉬 남
                    if(dataSnapshot.hasChild("displayMailId")){
                        String showHeaderEmailId  = dataSnapshot.child("displayMailId").getValue(String.class);
                        headerEmailId.setText(showHeaderEmailId);
                    }else{
                        MessageToast.message(UserDashboard.this,"메일아이디 정보가 없어 갱신 할수 없읍니다");
                    }
                    if(dataSnapshot.hasChild("nicName")){
                        String showHeaderNicName = dataSnapshot.child("nicName").getValue(String.class);
                        headerNicName.setText(showHeaderNicName);
                    }else{
                        MessageToast.message(UserDashboard.this,"닉네임 정보가 없어 갱신 할수 없읍니다");
                    }
                    if(dataSnapshot.hasChild("profilePicture")){
                        String showHeaderImage  = dataSnapshot.child("profilePicture").getValue(String.class);
                        Picasso.get().load(showHeaderImage).into(headerImage);
                    }else{
                        MessageToast.message(UserDashboard.this,"프로파일사진 정보가 없어 갱신 할수 없읍니다");
                    }
                    //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                    //placeholder(R.drawable.ic_account_circle_black) -- 네트워크 문제로 못가져올때 이걸로 대치
                } else {
                    Toast.makeText(UserDashboard.this,"불러오기 실패2",Toast.LENGTH_SHORT).show();
                    Log.d("불러오기", "불러오기 실패 ");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
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
                Toast.makeText(UserDashboard.this
                        , "로그아웃 하였읍니다.", Toast.LENGTH_SHORT).show();
                Intent logoutIntent = new Intent(UserDashboard.this, SignInActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                finish();
                break;
        }
        return true;
    }

    private void animateNavigationDrawer() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.warningColor));
        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentLayout.setScaleX(offsetScale);
                contentLayout.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentLayout.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentLayout.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// 뒤로가기 버튼 눌렀을 때
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    public void drawerbackStart(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 앱을 종료");
        }
    }

    public void postNewStart(View view) {
        Intent PostNewIntent = new Intent(this, PostNew.class);
        PostNewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(PostNewIntent);
        finish();
    }
}