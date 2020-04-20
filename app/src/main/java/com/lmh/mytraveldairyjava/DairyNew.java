package com.lmh.mytraveldairyjava;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;

import java.util.concurrent.Callable;


public class DairyNew extends AppCompatActivity {

    //변수초기화
    EditText _planname, _plandepartday, _plandays;
    Button _createplanbtn;

    //awesome validation 라이브러리
    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dairy_new);

        // 변수에 맞는 항목 할당(xml에 등록한거) //
        final EditText _planname = findViewById(R.id.editplanname);
        final EditText _plandepartday = findViewById(R.id.editplandepartday);
        final EditText _plandays = findViewById(R.id.editplandays);
        Button _createplanbtn = findViewById(R.id.createplanbtn);


        // AwesomeValidation 에서 제공하는 style 설정
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        // 항목별 validation 정의
        awesomeValidation.addValidation(this, R.id.editplanname, "^[a-zA-Z0-9ㄱ-ㅎ가-힣_ ]+$", R.string.invalidplanname);

        // yyyy-MM-dd
        awesomeValidation.addValidation(this, R.id.editplandepartday, "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
                + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
                + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
                + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$", R.string.invalidplandepartday);


        // dd/MM/yyyy
        //awesomeValidation.addValidation(this, R.id.editplandepartday, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)" +
        //        "(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])" +
        //       "|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$",R.string.invalidplandepartday);

        awesomeValidation.addValidation(this, R.id.editplandays, Range.closed(3, 365), R.string.invaliddays);

        //

        //신규생성버튼클릭시
        _createplanbtn = findViewById(R.id.createplanbtn);

        _createplanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validation 실행
                if (awesomeValidation.validate()) {
                    // validation 성공시
                    // 다이얼로그 화면

                    TextView alertmessage;

                    AlertDialog.Builder alert = new AlertDialog.Builder(DairyNew.this);
                    View mView = getLayoutInflater().inflate(R.layout.alert_dialog, null);
                    alert.setView(mView);

                    alertmessage = (TextView) mView.findViewById(R.id.alert_message);

                    final CheckBox alertcheckbox = (CheckBox) mView.findViewById(R.id.alert_check_box);
                    Button btalertcancel = (Button) mView.findViewById(R.id.bt_alertcancel);
                    final Button btalertok = (Button) mView.findViewById(R.id.bt_alertok);


                    final AlertDialog alertDialog = alert.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertmessage.setText(_planname.getText().toString() + "\n출발일 : " + _plandepartday.getText().toString() + "\n 여행기간은 " + _plandays.getText().toString() + " 일 일정입니다.\n");


                    //버튼 액션

                    //Cancel클릭시
                    btalertcancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(DairyNew.this
                                    , "실패!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();


                        }
                    });


                    //OK버튼클릭
                    btalertok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // db 테이블 업데이트
                            // 설정 액티비티로 이동..개발후 변경
                            Toast.makeText(DairyNew.this
                                    , "DB 에 신규로 여행계획을 생성합니다...!!!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();


                        }
                    });


                    alertcheckbox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (alertcheckbox.isChecked()) {

                                btalertok.setEnabled(true);
                                // 버튼 사용가될때 버튼배경색 바꾸기
                                btalertok.setBackgroundColor(getResources().getColor(android.R.color.black));


                            } else {

                                btalertok.setEnabled(false);
                                btalertok.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                            }

                        }
                    });

                    alertDialog.show();

                } else {
                    // validation 실패시
                    Toast.makeText(DairyNew.this
                            , "실패...입력된 값을 재확인 해주 세요", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


}