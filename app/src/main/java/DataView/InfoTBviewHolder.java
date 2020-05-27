package DataView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmh.mytraveldairy.R;

public class InfoTBviewHolder extends RecyclerView.ViewHolder {
    public TextView a_rv_title_name,a_rv_tag,a_rv_depart_date,a_rv_end_date;
    public ImageView a_image_rv_bg;
    public InfoTBviewHolder(@NonNull View itemView) {
        super(itemView);
        a_rv_title_name = itemView.findViewById(R.id.a_rv_title_name);
        a_rv_tag = itemView.findViewById(R.id.a_rv_tag);
        a_rv_depart_date = itemView.findViewById(R.id.a_rv_depart_date);
        a_rv_end_date = itemView.findViewById(R.id.a_rv_end_date);
        a_image_rv_bg = itemView.findViewById(R.id.a_image_rv_bg);
    }
}
