package com.lmh.mytraveldairyjava;

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


public class CurrencySelect extends AppCompatActivity {

    RadioGroup rgcurr;
    RadioButton rbcurr;
    ActionBar actionBar;
    private Toolbar toolbar;

    Button rgSlectedReturn;
    RadioGroup rgGroup;
    RadioButton radioButton;

    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currencyselectlayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("통화 선택");


        //rgSlectedReturn = findViewById(R.id.action_btn1);
        rgGroup = findViewById(R.id.RgCurrGroup);

        //rgSlectedReturn.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View v) {

         //   }
       // });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarcustum, menu);
        return true;
    }


    public void rgCurrChecked(View view) {

        int radioId = rgGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "Selected Radio Button: " + radioButton.getText(),Toast.LENGTH_SHORT).show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        if (item.getItemId() == R.id.action_btn1) {

            Toast.makeText(this, "Selected Radio Button: " + radioButton.getText(),
                    Toast.LENGTH_SHORT).show();

            return true;
        } return super.onOptionsItemSelected(item);
    }
}
