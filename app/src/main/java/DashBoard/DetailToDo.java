package DashBoard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmh.mytraveldairyjava.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailToDo extends Fragment {

    public DetailToDo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmentdetailtodo, container, false);
    }
}
