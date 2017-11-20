package com.shidiqarifs.edokter;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.shidiqarifs.edokter.Helper.CustomAdapter_Doctor;
import com.shidiqarifs.edokter.Helper.DatabaseHandler_Doctor;
import com.shidiqarifs.edokter.Helper.Dokter;
import com.shidiqarifs.edokter.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by shidiqarifs on 16/11/2017.
 */

public class HomeFragment extends Fragment {
    ListView lv;
    private DatabaseHandler_Doctor databaseHelper;
    private Dokter dokter;
    CustomAdapter_Doctor adapter;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.activity_fragment_home_pasien,container,false);
        databaseHelper = new DatabaseHandler_Doctor(getActivity());
        lv = (ListView) rootView.findViewById(android.R.id.list);
        ArrayList<Dokter> Array = databaseHelper.getAllUser();
        adapter = new CustomAdapter_Doctor(getContext(),Array);
        lv.setAdapter(adapter);
        return rootView;
    }
}
