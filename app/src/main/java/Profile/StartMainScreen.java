package Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lmh.mytraveldairyjava.R;

public class StartMainScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_main_screen);


    }


    public void callRegist(View view) {
        Toast.makeText(StartMainScreen.this, "로그인", Toast.LENGTH_SHORT).show();

    }

    public void callLogin(View view) {
        Toast.makeText(StartMainScreen.this, "회원가입", Toast.LENGTH_SHORT).show();
    }
}
