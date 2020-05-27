package MyProgressDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.airbnb.lottie.LottieAnimationView;
import com.lmh.mytraveldairy.R;

public class MyProgressDialogActivity extends Dialog {

    public MyProgressDialogActivity(Context context){
        super(context);
        setContentView(R.layout.activity_my_progress_dialog);
        //레이어 배경을 투명하게
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById(R.id.ani_progress_bar);
        lottieAnimationView.playAnimation();

        //중지코드
        //lottieAnimationView.pauseAnimation();
        //lottieAnimationView.setProgress(0);
    }
}

