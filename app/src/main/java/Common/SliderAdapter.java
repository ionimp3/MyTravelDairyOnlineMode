package Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.lmh.mytraveldairyjava.R;


public class SliderAdapter extends PagerAdapter {


    Context context;
    LayoutInflater layoutInflater;

    // 이미지를 불러와서 액티비톨 보낸다
    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] = {
            R.drawable.onboarding_img9,
            R.drawable.onboarding_img2,
            R.drawable.onboarding_img5,
            R.drawable.onboarding_img3
    };

    int headings[] = {
            R.string.first_Slide_Title,
            R.string.second_Slide_Title,
            R.string.third_Slide_Title,
            R.string.fourth_Slide_Title
    };

    int descriptions[] = {
            R.string.first_Description,
            R.string.second_Description,
            R.string.third_Description,
            R.string.fourth_Description
    };


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        //슬라이드할 화면 레이아웃 가져오기
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_slides, container, false);

        //실제이미지,텍스트를 HOOK 한다..
        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView heading = view.findViewById(R.id.slider_head);
        TextView desc = view.findViewById(R.id.slider_desc);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout) object);

    }
}
