package Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import Common.BackPressHandler;
import Common.MessageToast;
import Common.SignUpActivity;
import DashBoard.UserDashboard;

import com.airbnb.lottie.L;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lmh.mytraveldairyjava.R;

import java.util.HashMap;


public class CurrencySelect extends AppCompatActivity {

    RadioGroup rgcurr;
    RadioButton rbcurr;
    ActionBar actionBar;
    Toolbar toolbar;

    RadioGroup rgGroup;
    RadioButton radioButton;

    ProgressDialog dialog1;

    Intent intent;

    FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference UserRef;

    String ckey, rBselect, selectedRb, selectedNm;
    Integer numInt;
    String  msG ;

    // BackPressHandler 객체 선언, 할당
    private BackPressHandler backPressHandler = new BackPressHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currencyselectlayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        //rgSlectedReturn = findViewById(R.id.action_btn1);
        rgGroup = findViewById(R.id.RgCurrGroup);
        intent = getIntent();
        ckey = intent.getStringExtra("selectMailPrimaryKey_Send");
        rBselect = intent.getStringExtra("baseCurrencyCode_Send");
        numInt = Integer.parseInt(rBselect);

        UserRef = FirebaseDatabase.getInstance().getReference();

        SetDefaultCurrency();

    }

    private void SetDefaultCurrency() {

        switch (numInt) {
            case 1:
                radioButton = findViewById(R.id.RgCurr01);
                radioButton.setChecked(true);
                break;
            case 2:
                radioButton = findViewById(R.id.RgCurr02);
                radioButton.setChecked(true);
                break;
            case 3:
                radioButton = findViewById(R.id.RgCurr03);
                radioButton.setChecked(true);
                break;
            case 4:
                radioButton = findViewById(R.id.RgCurr04);
                radioButton.setChecked(true);
                break;
            case 5:
                radioButton = findViewById(R.id.RgCurr05);
                radioButton.setChecked(true);
                break;
            case 6:
                radioButton = findViewById(R.id.RgCurr06);
                radioButton.setChecked(true);
                break;
            case 7:
                radioButton = findViewById(R.id.RgCurr07);
                radioButton.setChecked(true);
                break;
            case 8:
                radioButton = findViewById(R.id.RgCurr08);
                radioButton.setChecked(true);
                break;
            case 9:
                radioButton = findViewById(R.id.RgCurr09);
                radioButton.setChecked(true);
                break;
            case 10:
                radioButton = findViewById(R.id.RgCurr10);
                radioButton.setChecked(true);
                break;
            case 11:
                radioButton = findViewById(R.id.RgCurr11);
                radioButton.setChecked(true);
                break;
            case 12:
                radioButton = findViewById(R.id.RgCurr12);
                radioButton.setChecked(true);
                break;
            case 13:
                radioButton = findViewById(R.id.RgCurr13);
                radioButton.setChecked(true);
                break;
            case 14:
                radioButton = findViewById(R.id.RgCurr14);
                radioButton.setChecked(true);
                break;
            case 15:
                radioButton = findViewById(R.id.RgCurr15);
                radioButton.setChecked(true);
                break;
            case 16:
                radioButton = findViewById(R.id.RgCurr16);
                radioButton.setChecked(true);
                break;
            case 17:
                radioButton = findViewById(R.id.RgCurr17);
                radioButton.setChecked(true);
                break;
            case 18:
                radioButton = findViewById(R.id.RgCurr18);
                radioButton.setChecked(true);
                break;
            case 19:
                radioButton = findViewById(R.id.RgCurr19);
                radioButton.setChecked(true);
                break;
            case 20:
                radioButton = findViewById(R.id.RgCurr20);
                radioButton.setChecked(true);
                break;
            case 21:
                radioButton = findViewById(R.id.RgCurr21);
                radioButton.setChecked(true);
                break;
            case 22:
                radioButton = findViewById(R.id.RgCurr22);
                radioButton.setChecked(true);
                break;
            case 23:
                radioButton = findViewById(R.id.RgCurr23);
                radioButton.setChecked(true);
                break;
            case 24:
                radioButton = findViewById(R.id.RgCurr24);
                radioButton.setChecked(true);
                break;
            case 25:
                radioButton = findViewById(R.id.RgCurr25);
                radioButton.setChecked(true);
                break;
            case 26:
                radioButton = findViewById(R.id.RgCurr26);
                radioButton.setChecked(true);
                break;
            case 27:
                radioButton = findViewById(R.id.RgCurr27);
                radioButton.setChecked(true);
                break;
            case 28:
                radioButton = findViewById(R.id.RgCurr28);
                radioButton.setChecked(true);
                break;
            case 29:
                radioButton = findViewById(R.id.RgCurr29);
                radioButton.setChecked(true);
                break;
            case 30:
                radioButton = findViewById(R.id.RgCurr30);
                radioButton.setChecked(true);
                break;
            case 31:
                radioButton = findViewById(R.id.RgCurr31);
                radioButton.setChecked(true);
                break;
            case 32:
                radioButton = findViewById(R.id.RgCurr32);
                radioButton.setChecked(true);
                break;
            case 33:
                radioButton = findViewById(R.id.RgCurr33);
                radioButton.setChecked(true);
                break;
            case 34:
                radioButton = findViewById(R.id.RgCurr34);
                radioButton.setChecked(true);
                break;
            case 35:
                radioButton = findViewById(R.id.RgCurr35);
                radioButton.setChecked(true);
                break;
            case 36:
                radioButton = findViewById(R.id.RgCurr36);
                radioButton.setChecked(true);
                break;
            case 37:
                radioButton = findViewById(R.id.RgCurr37);
                radioButton.setChecked(true);
                break;
            case 38:
                radioButton = findViewById(R.id.RgCurr38);
                radioButton.setChecked(true);
                break;
            case 39:
                radioButton = findViewById(R.id.RgCurr39);
                radioButton.setChecked(true);
                break;
            case 40:
                radioButton = findViewById(R.id.RgCurr40);
                radioButton.setChecked(true);
                break;
            case 41:
                radioButton = findViewById(R.id.RgCurr41);
                radioButton.setChecked(true);
                break;
            case 42:
                radioButton = findViewById(R.id.RgCurr42);
                radioButton.setChecked(true);
                break;
            case 43:
                radioButton = findViewById(R.id.RgCurr43);
                radioButton.setChecked(true);
                break;
            case 44:
                radioButton = findViewById(R.id.RgCurr44);
                radioButton.setChecked(true);
                break;
            case 45:
                radioButton = findViewById(R.id.RgCurr45);
                radioButton.setChecked(true);
                break;
            case 46:
                radioButton = findViewById(R.id.RgCurr46);
                radioButton.setChecked(true);
                break;
            case 47:
                radioButton = findViewById(R.id.RgCurr47);
                radioButton.setChecked(true);
                break;
            case 48:
                radioButton = findViewById(R.id.RgCurr48);
                radioButton.setChecked(true);
                break;
            case 49:
                radioButton = findViewById(R.id.RgCurr49);
                radioButton.setChecked(true);
                break;
            case 50:
                radioButton = findViewById(R.id.RgCurr50);
                radioButton.setChecked(true);
                break;
        }


    }

    public void rgCurrChecked(View view) {
        int radioId = rgGroup.getCheckedRadioButtonId();
        switch (radioId) {
            case R.id.RgCurr01:
                selectedRb = "1";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr02:
                selectedRb = "2";
                selectedNm = "USD";
                break;
            case R.id.RgCurr03:
                selectedRb = "3";
                selectedNm = "EUR";
                break;
            case R.id.RgCurr04:
                selectedRb = "4";
                selectedNm = "HKD";
                break;
            case R.id.RgCurr05:
                selectedRb = "5";
                selectedNm = "TWD";
                break;
            case R.id.RgCurr06:
                selectedRb = "6";
                selectedNm = "THB";
                break;
            case R.id.RgCurr07:
                selectedRb = "7";
                selectedNm = "GBP";
                break;
            case R.id.RgCurr08:
                selectedRb = "8";
                selectedNm = "CAD";
                break;
            case R.id.RgCurr09:
                selectedRb = "9";
                selectedNm = "CHF";
                break;
            case R.id.RgCurr10:
                selectedRb = "10";
                selectedNm = "SEK";
                break;
            case R.id.RgCurr11:
                selectedRb = "11";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr12:
                selectedRb = "12";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr13:
                selectedRb = "13";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr14:
                selectedRb = "14";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr15:
                selectedRb = "15";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr16:
                selectedRb = "16";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr17:
                selectedRb = "17";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr18:
                selectedRb = "18";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr19:
                selectedRb = "19";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr20:
                selectedRb = "20";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr21:
                selectedRb = "21";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr22:
                selectedRb = "22";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr23:
                selectedRb = "23";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr24:
                selectedRb = "24";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr25:
                selectedRb = "25";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr26:
                selectedRb = "26";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr27:
                selectedRb = "27";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr28:
                selectedRb = "28";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr29:
                selectedRb = "29";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr30:
                selectedRb = "30";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr31:
                selectedRb = "31";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr32:
                selectedRb = "32";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr33:
                selectedRb = "33";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr34:
                selectedRb = "34";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr35:
                selectedRb = "35";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr36:
                selectedRb = "36";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr37:
                selectedRb = "37";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr38:
                selectedRb = "38";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr39:
                selectedRb = "39";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr40:
                selectedRb = "40";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr41:
                selectedRb = "41";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr42:
                selectedRb = "42";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr43:
                selectedRb = "43";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr44:
                selectedRb = "44";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr45:
                selectedRb = "45";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr46:
                selectedRb = "46";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr47:
                selectedRb = "47";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr48:
                selectedRb = "48";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr49:
                selectedRb = "49";
                selectedNm = "KRW";
                break;
            case R.id.RgCurr50:
                selectedRb = "50";
                selectedNm = "KRW";
                break;


        }
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
                Intent currencyIntent = new Intent(CurrencySelect.this, ProfileActivity.class);
                currencyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(currencyIntent);
                return true;
            }
            case R.id.curr_changed: { // 오른쪽 상단 버튼 눌렀을 때
                Intent intent = getIntent();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference((ckey + "/userProfile/"));

                showProcessDialog1();

                UserRef = FirebaseDatabase.getInstance().getReference().child(ckey).child("userProfile");
                HashMap userCurrencyMap = new HashMap();
                userCurrencyMap.put("baseCurrencyCode",selectedRb);
                userCurrencyMap.put("baseCurrencyName",selectedNm);
                UserRef.updateChildren(userCurrencyMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful())
                        {
                            MessageToast.message(CurrencySelect.this,"기본통화변경완료!!!!!!");
                            dialog1.dismiss();
                        }
                        else {
                            String message = task.getException().getMessage();
                            Toast.makeText(CurrencySelect.this,"통화변경작업이 실패하였읍니다. : " + message,Toast.LENGTH_SHORT).show();
                            dialog1.dismiss();
                        }
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void showProcessDialog1() {
        dialog1 = new ProgressDialog(CurrencySelect.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setTitle("기본통화변경");
        dialog1.setMessage("DB업데이트 중입니다..");
        dialog1.show();
        dialog1.setCanceledOnTouchOutside(true);
        //
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent currencyIntent = new Intent(CurrencySelect.this, ProfileActivity.class);
        currencyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(currencyIntent);
        //Intent intent = new Intent(this, ProfileActivity.class);
        //startActivity(intent);
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
