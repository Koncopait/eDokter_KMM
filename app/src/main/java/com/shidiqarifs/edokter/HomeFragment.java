package com.shidiqarifs.edokter;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shidiqarifs.edokter.R;

/**
 * Created by shidiqarifs on 16/11/2017.
 */

public class HomeFragment extends Fragment {
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_home, container, false);
        return view;
    }
}
