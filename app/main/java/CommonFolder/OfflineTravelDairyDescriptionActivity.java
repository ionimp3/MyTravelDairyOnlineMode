package CommonFolder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lmh.mytraveldairy.R;

import ProfileFolder.OfflineProfileActivity;
import ProfileFolder.ProfileActivity;

public class OfflineTravelDairyDescriptionActivity extends AppCompatActivity {
    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_dairy_description);
    }
    public void onBackPressed() {
        Intent goToOfflineProfileintent = new Intent(this, OfflineProfileActivity.class);
        goToOfflineProfileintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToOfflineProfileintent);
        finish();
    }
}
