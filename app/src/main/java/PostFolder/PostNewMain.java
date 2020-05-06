package BottomNavigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lmh.mytraveldairyjava.R;

import java.util.ArrayList;
import java.util.List;

import Common.MessageToast;
import DashBoard.UserDashboard;
import ViewrModelAdapters.PostNewMainItemAdaptor;
import ViewrModelAdapters.PostNewMainModel;


public class PostNewMain extends Fragment implements View.OnClickListener {

    View v;
    RecyclerView recyclerView;
    List<PostNewMainModel> itemList;
    PostNewMainItemAdaptor adaptor;



    public PostNewMain() {
        // 반드시 한개 빈공간필요 파라메터 못가짐...라이프사이클 때문
    }
    @Nullable
    @Override
    //레이아웃셋팅,프래그먼트가 생성될때..여기서
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.postnewmain,container,false);

        recyclerView=v.findViewById(R.id.postnewmain_writting);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //initData();
        recyclerView.setAdapter(new PostNewMainItemAdaptor(initData()));

        return v;

    }

    private List<PostNewMainModel> initData() {

        //임의 data 입력..여기서 db에서 데이터 불러와서 array저장
        itemList=new ArrayList<>();
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제1"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제2"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제3"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제4"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제5"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제6"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제7"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제8"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제9"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제10"));
        itemList.add(new PostNewMainModel(R.drawable.ic_launcher_background,"예제11"));
        return itemList;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.actionbarcustum,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int  id = item.getItemId();

        if (id == android.R.id.home) {
            Intent gotoDashBoard = new Intent(getActivity(), UserDashboard.class);
            gotoDashBoard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(gotoDashBoard);
        }
        if (id == R.id.curr_changed) {
            MessageToast.message(getActivity(), "프래그먼트레이아웃에서 툴바 실행버튼사용");
        }
        return super.onOptionsItemSelected(item);
    }
}


