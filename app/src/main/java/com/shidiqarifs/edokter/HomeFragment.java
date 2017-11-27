package com.shidiqarifs.edokter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.shidiqarifs.edokter.Helper.CustomAdapter_Doctor;
import com.shidiqarifs.edokter.Helper.DatabaseHandler_Doctor;
import com.shidiqarifs.edokter.Helper.Dokter;
import com.shidiqarifs.edokter.Helper.get_url_link;
import com.shidiqarifs.edokter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shidiqarifs on 16/11/2017.
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView lv;
    ProgressDialog pDialog;

    AlertDialog alertDialog;
    SwipeRefreshLayout swipeLayout;
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
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        databaseHelper = new DatabaseHandler_Doctor(getActivity());
        lv = (ListView) rootView.findViewById(android.R.id.list);
        ArrayList<Dokter> Array = databaseHelper.getAllUser();
        adapter = new CustomAdapter_Doctor(getContext(),Array);
        lv.setAdapter(adapter);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new getlist().execute();
                        swipeLayout.setRefreshing(false);
                    }
                }
        );

        return rootView;

    }

    @Override
    public void onRefresh() {

    }


    private class getlist extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        AlertDialog alertDialog;

        @Override
        public String doInBackground(String... params) {
            get_url_link link = new get_url_link();
            String url = link.getUrl_link("api_get_doctorlist");
            String result = null;
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "c3334dfa-e354-719f-1d39-f2f6b673bcf2")
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Login Status");
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            databaseHelper.deleteUser();
            JSONObject jObj = null;
            try
            {
                jObj = new JSONObject(result);
                JSONArray array_data= jObj.getJSONArray("Data");
                for (int i=0;i<array_data.length();i++)
                {
                    dokter = new Dokter();
                    JSONObject explrObject = array_data.getJSONObject(i);
                    String doctor_id,doctor_name,doctor_spesialist,waktu_mulai,waktu_selesai;
                    doctor_id = explrObject.getString("ID_DOKTER");
                    doctor_name = explrObject.getString("NAMA_DOKTER");
                    doctor_spesialist = explrObject.getString("SPESIALIS");
                    waktu_mulai = explrObject.getString("WAKTU_MULAI");
                    waktu_selesai = explrObject.getString("WAKTU_SELESAI");
                    dokter.setId_dokter(doctor_id);
                    dokter.setNama_dokter(doctor_name);
                    dokter.setSpesialis_dokter(doctor_spesialist);
                    dokter.setWaktu_Mulai(waktu_mulai);
                    dokter.setWaktu_Selesai(waktu_selesai);
                    databaseHelper.addUser(dokter);
                }

                ArrayList<Dokter> Array = databaseHelper.getAllUser();
                adapter = new CustomAdapter_Doctor(getContext(),Array);
                lv.setAdapter(adapter);
            }
            catch (JSONException e)
            {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
