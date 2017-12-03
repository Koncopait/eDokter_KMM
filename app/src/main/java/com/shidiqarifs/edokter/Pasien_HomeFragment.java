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
import android.support.v7.widget.ListViewCompat;

import com.shidiqarifs.edokter.Helper.CustomAdapter_Doctor;
import com.shidiqarifs.edokter.Helper.CustomAdapter_Pasien;
import com.shidiqarifs.edokter.Helper.DatabaseHandler_Doctor;
import com.shidiqarifs.edokter.Helper.DatabaseHandler_Pasien;
import com.shidiqarifs.edokter.Helper.Dokter;
import com.shidiqarifs.edokter.Helper.Pasien;
import com.shidiqarifs.edokter.Helper.UserSessionManager;
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

public class Pasien_HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView lv;
    ProgressDialog pDialog;

    AlertDialog alertDialog;
    SwipeRefreshLayout swipeLayout;
    private DatabaseHandler_Pasien databaseHelper;
    private Pasien pasien;
    UserSessionManager session;
    CustomAdapter_Pasien adapter;
    public static Pasien_HomeFragment newInstance() {
        return new Pasien_HomeFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.activity_fragment_home,container,false);
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        databaseHelper = new DatabaseHandler_Pasien(getActivity());
        lv = (ListView) rootView.findViewById(R.id.listpasien);

        HashMap<String, String> user = session.getUserDetails();
        final String status = user.get(UserSessionManager.KEY_USERNAME);

        ArrayList<Pasien> Array = databaseHelper.getAllUser();
        adapter = new CustomAdapter_Pasien(getContext(),Array);
        lv.setAdapter(adapter);
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new getlist_pasien().execute(status);
                        swipeLayout.setRefreshing(false);
                    }
                }
        );

        return rootView;

    }

    @Override
    public void onRefresh() {

    }

    private class getlist_pasien extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        AlertDialog alertDialog;

        @Override
        public String doInBackground(String... params) {
            get_url_link link = new get_url_link();
            String id = params[0];
            String url = link.getUrl_link("api_get_pasienlist");
            String result = null;
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "id="+id);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "4f580677-eb57-8c2c-f86f-c64ed651c6e0")
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
            JSONObject jObj = null;
            databaseHelper.deleteUser();
            try
            {
                jObj = new JSONObject(result);
                JSONArray array_data= jObj.getJSONArray("Data");
                for (int i=0;i<array_data.length();i++)
                {
                    pasien = new Pasien();
                    JSONObject explrObject = array_data.getJSONObject(i);
                    String pasien_id,pasien_name,pasien_keluhan;
                    pasien_id = explrObject.getString("ID_PASIEN");
                    pasien_name = explrObject.getString("NAMA_PASIEN");
                    pasien_keluhan = explrObject.getString("KELUHAN");
                    pasien.setId_pasien(pasien_id);
                    pasien.setNama_pasien(pasien_name);
                    pasien.setKeluhan(pasien_keluhan);
                    databaseHelper.addUser(pasien);
                }

                ArrayList<Pasien> Array = databaseHelper.getAllUser();
                adapter = new CustomAdapter_Pasien(getContext(),Array);
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
