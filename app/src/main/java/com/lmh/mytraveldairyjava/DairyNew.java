package com.lmh.mytraveldairyjava;

import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;



public class DairyNew extends AppCompatActivity {

    //변수초기화화
    EditText _planname, _plandepartday, _plandays;
    Button _createplanbtn;

    //awesome validation 라이브러리
    AwesomeValidation awesomeValidation;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dairy_new);

        // 변수에 맞는 항목 할당(xml에 등록한거) //
        _planname = findViewById(R.id.editplanname);
        _plandepartday = findViewById(R.id.editplandepartday);
        _plandays = findViewById(R.id.editplandays);
        _createplanbtn = findViewById(R.id.createplanbtn);


        // AwesomeValidation 에서 제공하는 style 설정
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // 항목별 validation 정의
        awesomeValidation.addValidation(this, R.id.editplanname, "^[a-zA-Z0-9ㄱ-ㅎ가-힣_ ]+$", R.string.invalidplanname);

        // yyyy-MM-dd
        awesomeValidation.addValidation(this, R.id.editplandepartday, "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$",R.string.invalidplandepartday);

        // dd/MM/yyyy
        //awesomeValidation.addValidation(this, R.id.editplandepartday, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)" +
        //        "(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])" +
        //       "|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$",R.string.invalidplandepartday);
        awesomeValidation.addValidation(this, R.id.editplandays, Range.closed(3, 999), R.string.invaliddays);

        //버튼을 눌렀을때

        _createplanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validation 실행

                if (awesomeValidation.validate()) {
                    //성공
                    Toast.makeText(getApplicationContext()
                            , "성공!!!!!!!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext()
                            , "실패!!!!!!!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }


}
