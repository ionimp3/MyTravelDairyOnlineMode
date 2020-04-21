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
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.common.collect.Range;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class DairyNew extends AppCompatActivity {



    //변수초기화
    FirebaseDatabase rootNode;
    DatabaseReference reference;

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
        // 정규표현식에 표현된 문자가 한개라도 있으면 true
        awesomeValidation.addValidation(this, R.id.editplanname, "^[a-zA-Z0-9ㄱ-ㅎ가-힣_ ]+$", R.string.invalidplanname);
        // 공백만 존재할경우 false...NOT_EMPTY 표현식 유용할듯
        awesomeValidation.addValidation(this, R.id.editplanname, RegexTemplate.NOT_EMPTY, R.string.invalidplanname);

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

                            alertDialog.dismiss();


                        }
                    });


                    //OK버튼클릭
                    btalertok.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {


                            // 다음 액티비로 넘겨줄 data 초기화 및 세팅
                            //this.Initdata();
                            // db 테이블 업데이트

                            //데이터베이스에서 data 모두불러오기
                            rootNode = FirebaseDatabase.getInstance();
                            //테이터베이스의 noder지정(첫벗재컬럼이 node 인데, 테이블개념)
                            reference = rootNode.getReference("basic_info");

                            //data 가져오기
                            //앞에서 미리 처리됨,더 필요한건 여기에 추가
                            //
                            //

                            String aplanname = _planname.getEditableText().toString();
                            String aplandepartday = _plandepartday.getEditableText().toString();
                            String aplandays = _plandays.getEditableText().toString();
                            String update_timestamp = LocalDateTime.now().toString();
                            String create_timestamp = LocalDateTime.now().toString();


                            BaiscinfoHelper baiscinfoHelper = new BaiscinfoHelper(aplanname, aplandepartday, aplandays, update_timestamp, create_timestamp);

                            reference.child(aplanname).setValue(baiscinfoHelper);


                            // 설정 액티비티로 이동..개발후 변경

                            //Toast.makeText(DairyNew.this
                            //        , "DB 에 신규로 여행계획을 생성합니다...!!!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();


                        }

                        // 다음액티비티로 넘겨줄  data 초기화 및 설정
                        //private void Initdata() {

                        //}
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