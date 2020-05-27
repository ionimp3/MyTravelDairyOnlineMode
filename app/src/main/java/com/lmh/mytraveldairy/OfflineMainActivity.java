package com.lmh.mytraveldairy;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


import CommonFolder.BackPressHandler;
import MyProgressDialog.MyProgressDialogActivity;
import OfflineHomeFolder.FragmentOfflineHome;
import OfflineLink.FragmentOfflineLink;
import OfflinePlaceFolder.FragmentOfflinePlace;
import OfflineRouteFolder.FragmentOfflineRoute;
import OfflineTagsFolder.FragmentOfflineTags;
import OfflineInformationFolder.FragmentOfflineInformation;
import PlanFolder.OfflineNewPlanActivity;
import ProfileFolder.OfflineProfileActivity;
import RoomDataFolder.RoomInfoTB;
import RoomDataFolder.RoomMyTravelDairyDatabase;
import RoomDataFolder.RoomUserTB;


public class OfflineMainActivity extends AppCompatActivity {

    private static final String TAG = "OfflineMainActivity";

    private BackPressHandler backPressHandler = new BackPressHandler(this);
    private long backKeyPressedTime = 1;

    private MyProgressDialogActivity myProgressDialogActivity;
    public static MyProgressDialogActivity myPD;

    //db 초기값세팅
    //Room관련 선언
    public static RoomMyTravelDairyDatabase db ;
    private String timeStampUpdateTime, timeStampCreateTime;

    private CoordinatorLayout mRootLayout;
    private NestedScrollView mNetScrollView;
    private Toolbar toolBar;
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private Fragment fragment = null;
    private FragmentTransaction ft;
    private FragmentManager fm;

    private FloatingActionButton fab1;
    private Button mHeaderGoogleLink;
    private int mChangeBg = 1;

    //신규여행일정 추가/선택 다이얼로그
    private Button mDialogNewPlanBtnAdd;
    private RecyclerView mDialogNewPlanRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_main);

        //Room db 초기화 및 db 확인 : 저장소에 저장된 db 명 MyTravelDairyDatabase

        db = RoomMyTravelDairyDatabase.getInstance(getApplicationContext());
        myPD = new MyProgressDialogActivity(this);

        toolBar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.fragmentContainer);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        mRootLayout = findViewById(R.id.rootLayout);
        mNetScrollView = findViewById(R.id.nestedScrollView);
        mHeaderGoogleLink = findViewById(R.id.btn_google_search);
        fab1 = findViewById(R.id.fab_main);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("지도/루트"));
        tabLayout.addTab(tabLayout.newTab().setText("여행지"));
        tabLayout.addTab(tabLayout.newTab().setText("정보"));
        tabLayout.addTab(tabLayout.newTab().setText("Tags"));
        tabLayout.addTab(tabLayout.newTab().setText("Link"));

        //처음 열어줄 탭 레이아웃 지정정
        fragment = new FragmentOfflineHome();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fragmentContainer, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:

                        //fragment = new FragmentOfflineHome();
                        mChangeBg = 1;
                                List<RoomInfoTB> roomUserTBViewList1 = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                                        .getRoomInfoTBdao()
                                        .getAllInfoTBList();
                                if (roomUserTBViewList1.size() == 0) {
                                    fragment = new FragmentOfflineHome();
                                } else {
                                    fragment = new FragmentOfflineHome();
                                }

                        fab1.show();
                        break;
                    case 1:
                        fragment = new FragmentOfflineRoute();
                        mChangeBg = 0;
                        fab1.hide();
                        break;
                    case 2:
                        fragment = new FragmentOfflinePlace();
                        mChangeBg = 0;
                        fab1.hide();
                        break;
                    case 3:
                        fragment = new FragmentOfflineInformation();
                        mChangeBg = 0;
                        fab1.hide();
                        break;
                    case 4:
                        fragment = new FragmentOfflineTags();
                        mChangeBg = 0;
                        fab1.hide();
                        break;
                    case 5:
                        fragment = new FragmentOfflineLink();
                        mChangeBg = 1;
                        fab1.hide();
                        break;
                }
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragment);
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
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChangeBg == 1) {
                    Toast.makeText(OfflineMainActivity.this, "배경변경기능 추가", Toast.LENGTH_SHORT).show();
                    toolBar.setTitleTextColor(Color.WHITE);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Room db 초기화 및 db 확인 : 저장소에 저장된 db 명 MyTravelDairyDatabase
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RoomUserTB> roomUserTBViewList = RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                        .getRoomUserTBdao()
                        .getAllUserTBList();
                if (roomUserTBViewList.size() == 0) {
                    firstDataSaving();
                }
            }
        }).start();
    }

    private void firstDataSaving() {
        timeStampUpdateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeStampUpdateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
        }
        timeStampCreateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeStampCreateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, a hh:mm:ss , MM/dd/yy", Locale.ENGLISH));
        }
        RoomUserTB roomUserTB = new RoomUserTB();
        roomUserTB.UserTB_coverImage = "NA";
        roomUserTB.UserTB_coverImageKey = "NA";
        roomUserTB.UserTB_nicName = "Traveler";
        roomUserTB.UserTB_nowUserStatus = "여행전";
        roomUserTB.UserTB_profileImage = "NA";
        roomUserTB.UserTB_profileImageKey = "NA";
        roomUserTB.UserTB_pushAlarmSelected = "N";
        roomUserTB.UserTB_timeStampCreateTime = timeStampUpdateTime;
        roomUserTB.UserTB_timeStampUpdateTime = timeStampCreateTime;
        InsertAsyncTask insertAsyncTask = new InsertAsyncTask();
        insertAsyncTask.execute(roomUserTB);
    }



    public void btn_action_google_search(View view) {
        Intent goToGoogle = new Intent(this, MyWebViewActivity.class);
        goToGoogle.putExtra("sendToUrlFromActivity","https://google.com");
        startActivity(goToGoogle);
        finish();
    }

    public void fab_action_show_plan_list(View view) {
        final AlertDialog dialogPlanList = new AlertDialog.Builder(this).create();
        View mViewPlanList = getLayoutInflater().inflate(R.layout.custum_dialog_plan_list,null);
        mDialogNewPlanBtnAdd = mViewPlanList.findViewById(R.id.btn_offline_dialog_add_plan);
        mDialogNewPlanRecyclerView = mViewPlanList.findViewById(R.id.rv_offline_dialog_plan_list);
        //다이얼로그화면에 넣을 거 처리

        // 다이얼로그 화면 표시하기
        dialogPlanList.setView(mViewPlanList);
        dialogPlanList.show();
        //다이얼로그사이즈 조정
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogPlanList.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogPlanList.getWindow().setAttributes(layoutParams);

        mDialogNewPlanBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToOfflineNewPlan = new Intent(OfflineMainActivity.this, OfflineNewPlanActivity.class);
                goToOfflineNewPlan.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToOfflineNewPlan);
                finish();
            }
        });
    }

    class InsertAsyncTask extends AsyncTask<RoomUserTB, Void, Void> {
        @Override
        protected Void doInBackground(RoomUserTB... roomUserTBS) {
            RoomMyTravelDairyDatabase.getInstance(getApplicationContext())
                    .getRoomUserTBdao()
                    .insert(roomUserTBS[0]);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_offline_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                tabLayout.getTabAt(0).select();
                fragment = new FragmentOfflineHome();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.fragmentContainer, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
                //drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.btn_appbar_setting:
                Intent goToSetting = new Intent(this, OfflineProfileActivity.class);
                goToSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToSetting);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 앱을 종료");
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼 한번 더 누르면 앱을 종료", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);                        // 태스크를 백그라운드로 이동
            finishAndRemoveTask();                        // 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());    // 앱 프로세스 종료
        }
    }
}
