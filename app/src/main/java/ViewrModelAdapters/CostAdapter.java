package ViewrModelAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmh.mytraveldairyjava.R;

import java.util.List;

public class CostAdapter extends RecyclerView.Adapter{

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_nav_cost_itemlayout,parent,false);
        return new CostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CostViewHolder) holder).bindView(position);

    }

    @Override
    public int getItemCount() {
        return CostModel.costContent_Ed.length;
    }
    private class CostViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private EditText costContents,costFee;
        public CostViewHolder(@NonNull View itemView) {
            super(itemView);

            costContents = (EditText) itemView.findViewById(R.id.ed_cost_contents);
            costFee = (EditText) itemView.findViewById(R.id.ed_cost_fee);
            itemView.setOnClickListener(this);
        }

        public  void bindView(int position){
            costContents.setText(CostModel.costContent_Ed[position]);
            costFee.setText(CostModel.costFee_Ed[position]);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
