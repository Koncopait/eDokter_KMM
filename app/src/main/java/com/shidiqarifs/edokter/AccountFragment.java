package com.shidiqarifs.edokter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shidiqarifs.edokter.R;

/**
 * Created by shidiqarifs on 16/11/2017.
 */

public class AccountFragment extends Fragment {
    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_home_pasien, container, false);
        return view;
    }

}

