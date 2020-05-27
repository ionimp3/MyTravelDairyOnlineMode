package DataView;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmh.mytraveldairy.MainActivity;
import com.lmh.mytraveldairy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import PlanFolder.ClickPlanActivity;

public class InfoTBviewAdapterFragment extends RecyclerView.Adapter<InfoTBviewAdapterFragment.MyviewHolder>{

    private ArrayList<InfoTBmodelFragment> arrayList;
    private Context context;

    public InfoTBviewAdapterFragment(ArrayList<InfoTBmodelFragment> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_plan_list_layout,parent,false);
        MyviewHolder holder = new MyviewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder holder, final int position) {

        holder.a_rv_tag.setText(arrayList.get(position).getInfoTB_planTags());
        holder.a_rv_title_name.setText(arrayList.get(position).getInfoTB_planName());
        holder.a_rv_depart_date.setText(arrayList.get(position).getInfoTB_planDepartDate());
        holder.a_rv_end_date.setText(arrayList.get(position).getInfoTB_planEndDate());
        Picasso.get().load(arrayList.get(position).getInfoTB_titleImage()).into(holder.a_image_rv_bg);

        //final String planKey = holder.itemView.getTag().toString();
        holder.itemView.setTag(position); //리사이클뷰클릭한 포지션가져오기기
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String planKey = holder.itemView.getTag(position);
                //Log.e("테스트",""+planKey+" "+);
                //Intent clickPlanIntent = new Intent(v.getContext(), ClickPlanActivity.class);
                //clickPlanIntent.putExtra("planKeyFromMain", planKey);
                //context.startActivity(clickPlanIntent);
            }
        });



/*                       final String planKey = getRef(position).getKey();
                holder.a_rv_tag.setText(model.getInfoTB_planTags());
                holder.a_rv_title_name.setText(model.getInfoTB_planName());
                holder.a_rv_depart_date.setText(model.getInfoTB_planDepartDate());
                holder.a_rv_end_date.setText(model.getInfoTB_planEndDate());
                Picasso.get().load(model.getInfoTB_titleImage()).into(holder.a_image_rv_bg);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickPlanIntent = new Intent(MainActivity.this, ClickPlanActivity.class);
                        clickPlanIntent.putExtra("planKeyFromMain", planKey);
                        startActivity(clickPlanIntent);
                    }
                });
                // 롱클릭시 세부내용으로 이동 하는 리스너 추가
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        Intent clickLongDetailPlanIntent = new Intent(MainActivity.this, ClickLongDetailPlanActivity.class);
                        clickLongDetailPlanIntent.putExtra("planKeyFromMain", planKey);
                        startActivity(clickLongDetailPlanIntent);
                        return false;
                    }
                });*/

    }

    @Override
    public int getItemCount() {
        return (arrayList !=null ? arrayList.size() : 0);
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        public TextView a_rv_title_name,a_rv_tag,a_rv_depart_date,a_rv_end_date;
        public ImageView a_image_rv_bg;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            a_rv_title_name = itemView.findViewById(R.id.a_rv_title_name);
            a_rv_tag = itemView.findViewById(R.id.a_rv_tag);
            a_rv_depart_date = itemView.findViewById(R.id.a_rv_depart_date);
            a_rv_end_date = itemView.findViewById(R.id.a_rv_end_date);
            a_image_rv_bg = itemView.findViewById(R.id.a_image_rv_bg);
        }
    }
}
