package OfflineHomeFolder;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lmh.mytraveldairy.OfflineMainActivity;
import com.lmh.mytraveldairy.R;

import java.time.Instant;
import java.util.List;

import MyProgressDialog.MyProgressDialogActivity;
import PlanFolder.OfflineNewPlanActivity;
import RoomDataFolder.RoomInfoTB;
import RoomDataFolder.RoomMyTravelDairyDatabase;
import RoomDataFolder.RoomUserTB;

import static com.lmh.mytraveldairy.OfflineMainActivity.db;
import static com.lmh.mytraveldairy.OfflineMainActivity.myPD;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfflineHome extends Fragment {
    private static final String TAG = "FragmentOfflineHome";
    private int mLayoutSelect = 0;
    private int mCheckAlready = 0;
    View v;

    public FragmentOfflineHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myPD.show();
        Thread thread = new Thread (new Runnable() {
            @Override
            public void run() {

                List<RoomInfoTB> roomInfoTBViewList = db
                        .getRoomInfoTBdao()
                        .getAllInfoTBList();
                Log.d(TAG, "infoTB 행갯수" + String.valueOf(roomInfoTBViewList.size()));
                if (roomInfoTBViewList.size() > 0) {
                    mLayoutSelect = 1;
                }
                Log.d(TAG, "레이아웃" + mLayoutSelect);
            }
        });
        thread.start();
        try {
            thread.join();
            if (mLayoutSelect < 1) {
                v =  inflater.inflate(R.layout.fragment_offline_home_oops, container, false);
                Log.d(TAG, "infoTB 데이터없음"+mLayoutSelect);
            }
            else {
                v =  inflater.inflate(R.layout.fragment_offline_home, container, false);
                Log.d(TAG, "infoTB 데이터있음"+mLayoutSelect);
            }
            myPD.dismiss();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}

