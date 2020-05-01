package Profile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import Common.BackPressHandler;

import com.lmh.mytraveldairyjava.R;


public class CurrencySelect extends AppCompatActivity {

    RadioGroup rgcurr;
    RadioButton rbcurr;
    ActionBar actionBar;
    Toolbar toolbar;

    RadioGroup rgGroup;
    RadioButton radioButton;


    Intent intent;

    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currencyselectlayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //rgSlectedReturn = findViewById(R.id.action_btn1);
        rgGroup = findViewById(R.id.RgCurrGroup);

        //rgSlectedReturn.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View v) {

         //   }
       // });
    }

    public void rgCurrChecked(View view) {

        int radioId = rgGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "Selected Radio Button: " + radioButton.getText(),Toast.LENGTH_SHORT).show();

    }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.actionbarcustum, menu);
      return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
          case android.R.id.home: { // 뒤로가기 버튼 눌렀을 때
              Intent intent = new Intent(this, ProfileActivity.class);
              startActivity(intent);
              finish();
              return true;
          }
          case R.id.curr_changed: { // 오른쪽 상단 버튼 눌렀을 때
              Toast.makeText(this, "DB에 저장진행한다", Toast.LENGTH_SHORT).show();
              return true;
          }
      }
      return super.onOptionsItemSelected(item);
  }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
        finish();
        // Default
        //backPressHandler.onBackPressed();
        // Toast 메세지 사용자 지정
        //backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면, 선택한것을 저장하지 않고 앱을 종료");
        // 뒤로가기 간격 사용자 지정
        //backPressHandler.onBackPressed(3000);
        // Toast, 간격 사용자 지정
        //backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 종료", 3000);
    }

}
