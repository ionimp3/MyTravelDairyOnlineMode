package PostFolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lmh.mytraveldairyjava.R;

import BottomNavigation.BottomNavCost;
import BottomNavigation.BottomNavMap;
import BottomNavigation.BottomNavOther;
import BottomNavigation.BottomNavPhoto;
import BottomNavigation.BottomNavTags;
import BottomNavigation.PostNewMain;
import DashBoard.UserDashboard;

public class PostNew extends AppCompatActivity {
    private Toolbar mToolbar;
    private long pressedTime = 0;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postnewlayout);

        mToolbar = (Toolbar) findViewById(R.id.update_Post_Page_Toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("일정입력화면");

        bottomNavigationView = findViewById(R.id.bottomNav);


        /*BottomNavTest fragment = new BottomNavTest();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.bottomfragmentContainer,fragment);
        fragmentTransaction.commit();*/

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.bottomfragmentContainer, new PostNewMain()).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.bottom_nav_photo:
                        fragment = new BottomNavPhoto();
                        break;
                    case R.id.bottom_nav_cost:
                        fragment = new BottomNavCost();
                        break;
                    case R.id.bottom_nav_map:
                        fragment = new BottomNavMap();
                        break;
                    case R.id.bottom_nav_tags:
                        fragment = new BottomNavTags();
                        break;
                    case R.id.bottom_nav_other:
                        fragment = new BottomNavOther();
                        break;
                }
                //액티비티에 프래그먼트 연결
                getSupportFragmentManager().beginTransaction().replace(R.id.bottomfragmentContainer, fragment).commit();
                return true;
            }
        });


    }

    private void SendToUserDashBoard() {
        Intent SendToUserDashBoardIntent = new Intent(this, UserDashboard.class);
        SendToUserDashBoardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SendToUserDashBoardIntent);
        finish();
    }

}
