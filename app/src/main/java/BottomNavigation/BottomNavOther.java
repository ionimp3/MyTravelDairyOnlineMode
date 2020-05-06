package BottomNavigation;

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

import com.lmh.mytraveldairyjava.R;

import Common.MessageToast;

public class BottomNavOther extends Fragment {
    public BottomNavOther() {
        // 빈공간필요
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_nav_other,container,false);


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.actionbarcustum,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int  id = item.getItemId();
        if (id == R.id.curr_changed) {
            MessageToast.message(getActivity(), "프래그먼트레이아웃에서 툴바 실행버튼사용");
        }
        if (id == android.R.id.home) {
            MessageToast.message(getActivity(), "프래그먼트레이아웃에서 툴바 뒤로가기버튼사용");
        }
        return super.onOptionsItemSelected(item);
    }
}
