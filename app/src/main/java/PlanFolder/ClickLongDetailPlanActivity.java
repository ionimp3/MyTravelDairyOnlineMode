package PlanFolder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.lmh.mytraveldairy.R;

public class ClickLongDetailPlanActivity extends AppCompatActivity {
    private String planKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_long_detail_plan);
        planKey = getIntent().getExtras().get("planKeyFromMain").toString();

        Toast.makeText(this,""+planKey,Toast.LENGTH_SHORT).show();
    }
}
