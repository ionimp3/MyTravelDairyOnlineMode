package com.lmh.mytraveldairy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewActivity extends AppCompatActivity {

    WebView webView;
    private String myUrl;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_web_view);

        Intent intent = getIntent();
        myUrl = intent.getStringExtra("sendToUrlFromActivity");

        webView =(WebView) findViewById(R.id.layout_web_view);
        webView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(myUrl);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) { // 뒤로가기 눌렀을 때, 뒤로 갈 곳이 있을 경우
            webView.goBack(); // 뒤로가기
        } else {//뒤로 갈 곳이 없는 경우
            new AlertDialog.Builder(MyWebViewActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("앱으로 돌아가기!")
                    .setMessage("웹을 종료하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent goToOfflineMain = new Intent(MyWebViewActivity.this, OfflineMainActivity.class);
                            goToOfflineMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goToOfflineMain);
                            finish();
                        }
                    })
                    .setNegativeButton("아니오", null)
                    .show();
        }
    }
}
