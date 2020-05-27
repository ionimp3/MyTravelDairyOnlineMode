package OfflineProfileFolder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmh.mytraveldairy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfflineProfile extends Fragment {

    public FragmentOfflineProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offline_profile, container, false);
    }
}
