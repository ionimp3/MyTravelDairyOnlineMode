package ViewrModelAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmh.mytraveldairyjava.R;

import java.util.List;

public class PostNewMainItemAdaptor extends RecyclerView.Adapter<PostNewMainItemAdaptor.ViewHolder> {

    List<PostNewMainModel>  itemList1;

    public PostNewMainItemAdaptor(List<PostNewMainModel> itemList1) {
        this.itemList1 = itemList1;
    }

    @NonNull
    @Override
    public PostNewMainItemAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.postnewmain_writting_itemlayout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostNewMainItemAdaptor.ViewHolder holder, int position) {

        holder.itemImage.setImageResource(itemList1.get(position).getImage());
        holder.itemText.setText(itemList1.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return itemList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.image1);
            itemText = itemView.findViewById(R.id.text1);

        }
    }
}