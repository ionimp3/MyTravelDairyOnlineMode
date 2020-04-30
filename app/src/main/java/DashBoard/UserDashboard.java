package DashBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.lmh.mytraveldairyjava.R;
import com.lmh.mytraveldairyjava.TabCall;
import com.lmh.mytraveldairyjava.TabChat;

import Common.BackPressHandler;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    //네이비게이션애니매이터 용 변수
    static final float END_SCALE = 0.7F;

    //drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ImageView menuListIcon;
    TextView textView;
    LinearLayout contentLayout;

    Toolbar toolbar;
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentTransaction ft;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboardlayout);

        //hooks
        //textView = findViewById(R.id.textView1);
        //menuListIcon = findViewById(R.id.btn_drawerlist);
        //contentLayout = findViewById(R.id.contentlayout);

        //menu hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_layout);
        frameLayout = findViewById(R.id.simpleframelayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("첫번째탭"));
        tabLayout.addTab(tabLayout.newTab().setText("두번째탭"));
        setTitle("대시보드");

        fragment = new TabChat();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.simpleframelayout,fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()){
                    case 0:
                        fragment = new TabChat();
                        break;
                    case 1 :
                        fragment = new TabCall();
                        break;
                }
                fm = getSupportFragmentManager();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.simpleframelayout,fragment);
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
        navigationView.setCheckedItem(R.id.nav_home);

        navigationDrawer();



    }

    //navigation 기능메소드드
   private void navigationDrawer() {
        //navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        navigationView.setCheckedItem(R.id.nav_home);
        //상단에 list 버튼 만들어 누르면 드로어 열어 주는 구문 ..
/*        menuListIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });*/

        //animateNavigationDrawer();

    }

/*    private void animateNavigationDrawer() {
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
    }*/

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);

        }else{
            super.onBackPressed();
        }
        // Default
        //backPressHandler.onBackPressed();
        // Toast 메세지 사용자 지정
        //backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 앱을 종료");
        // 뒤로가기 간격 사용자 지정
        //backPressHandler.onBackPressed(3000);
        // Toast, 간격 사용자 지정
        //backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 종료", 3000);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return true;
    }
}