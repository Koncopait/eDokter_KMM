package com.shidiqarifs.edokter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shidiqarifs.edokter.Helper.CustomAdapter_Doctor;
import com.shidiqarifs.edokter.Helper.DatabaseHandler_Doctor;
import com.shidiqarifs.edokter.Helper.Dokter;
import com.shidiqarifs.edokter.Helper.Riwayat;
import com.shidiqarifs.edokter.Helper.Riwayat_CustomAdapter;
import com.shidiqarifs.edokter.Helper.UserSessionManager;
import com.shidiqarifs.edokter.Helper.get_url_link;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 26/11/2017.
 */

public class DetailActivity extends AppCompatActivity {


    TextView nameTxt,spesialisTxt, usernameTxt;
    CardView Keluhan,Immun,Prescription;
    ImageButton btnclickme, btnback;
    String token="";
    private DatabaseHandler_Doctor db;
    UserSessionManager session;
    String id_user;
    Riwayat_CustomAdapter riwayat_customAdapter;
    String id_dokter,id_pasien;
    ArrayList<Riwayat> riwayats = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaildokter);
        session = new UserSessionManager(getApplicationContext());
        db = new DatabaseHandler_Doctor(this);
        nameTxt = (TextView) findViewById(R.id.namadet);
        spesialisTxt = (TextView) findViewById(R.id.medicalprob);
        Keluhan = (CardView) findViewById(R.id.cd_inputKeluhan);
        Immun = (CardView) findViewById(R.id.cd_tanyaDokter);
        Prescription = (CardView) findViewById(R.id.cd_riwayatPeriksa);
        if(session.checkLogin())
            finish();
        if (!session.checkLogin()){
            HashMap<String, String> user = session.getUserDetails();
            id_user = user.get(UserSessionManager.KEY_USERNAME);
            id_pasien = id_user;
        }else{
            session.logoutUser();
        }

        btnback = (ImageButton) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        //usernameTxt = (TextView) findViewById(R.id.usernameDetailTxt);

        //GET INTENT
        Intent i= this.getIntent();

        //RECEIVE DATA
        final String id = i.getExtras().getString("ID_DOKTER");
        id_dokter=id;
        String nama =i.getExtras().getString("NAMA_DOKTER");
        String spesialis =i.getExtras().getString("SPESIALIS");

        //BIND DATA
        nameTxt.setText(nama);
        spesialisTxt.setText(spesialis);


        Keluhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.keluhan_input,null);
                final EditText keluhan_et = (EditText) mView.findViewById(R.id.et_keluhan);
                final Button mLogin = (Button) mView.findViewById(R.id.btnSave);
                Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                dialog.setCancelable(false);
                keluhan_et.setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange (View v, boolean hasFocus){
                        if (keluhan_et.getText().toString().equals("")){
                            keluhan_et.setError("Keluhan Masih Kosong.");
                        }else {
                            keluhan_et.setError(null);
                            keluhan_et.clearFocus();
                            mLogin.setEnabled(true);
                        }
                    }
                });
                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.tgl_input,null);
                        final TimePicker tgl = (TimePicker) mView.findViewById(R.id.jam_picker);
                        tgl.setIs24HourView(true);
                        Button mLogin = (Button) mView.findViewById(R.id.btnSave);
                        Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
                        mBuilder.setView(mView);
                        final AlertDialog dialog1 = mBuilder.create();
                        dialog1.show();
                        dialog1.setCancelable(false);

                        mLogin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int jam = tgl.getCurrentHour();
                                int menit = tgl.getCurrentMinute();
                                String pendaftaran = String.valueOf(jam)+":"+String.valueOf(menit)+":00";
                                String keluhan = keluhan_et.getText().toString();
                                new daftar().execute(id_user,id,keluhan,pendaftaran);
                                dialog.dismiss();
                                dialog1.dismiss();
                            }
                        });
                        mCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        //  usernameTxt.setText(username);



        Prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getlist_riwayat().execute(id_user,id);
            }
        });
    }



    private class daftar extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        android.app.AlertDialog alertDialog;

        @Override
        public String doInBackground(String... params) {
            String id_pasien = params[0];
            String id_dokter = params[1];
            String keluhan = params[2];
            String waktu_daftar = params[3];
            get_url_link link = new get_url_link();
            String url = link.getUrl_link("api_daftar_periksa");
            String result = null;
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "id_pasien="+id_pasien+"&id_dokter="+id_dokter+"&keluhan="+keluhan+"&waktu_daftar="+waktu_daftar);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "6b5c67de-2645-04a9-3216-362f634d968a")
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
            alertDialog = new android.app.AlertDialog.Builder(DetailActivity.this).create();
            alertDialog.setTitle("Daftar Status");
            pDialog = new ProgressDialog(DetailActivity.this);
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            JSONObject jObj = null;
            String reason = null;

            int status=100;
            try
            {
                jObj = new JSONObject(result);
                JSONArray jsonArray= jObj.getJSONArray("daftar");
                JSONObject explrObject = jsonArray.getJSONObject(0);
                status = explrObject.getInt("daftar_status");


                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.finish_daftar,null);
                Button mLogin = (Button) mView.findViewById(R.id.finish);
                mBuilder.setView(mView);
                final AlertDialog dialog1 = mBuilder.create();
                dialog1.show();
                dialog1.setCancelable(false);
                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        dialog1.dismiss();
                    }
                });
            }
            catch (JSONException e)
            {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
        }
    }



    private class getlist_riwayat extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        android.app.AlertDialog alertDialog;

        @Override
        public String doInBackground(String... params) {

            get_url_link link = new get_url_link();
            String url = link.getUrl_link("api_get_riwayat");
            String result = null;
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "id="+id_pasien+"&id_dokter="+id_dokter);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "17165fa2-cb84-229a-fd10-1e300ed49cf9")
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
            alertDialog = new android.app.AlertDialog.Builder(DetailActivity.this).create();
            alertDialog.setTitle("Riwayat Pemeriksaan");
            pDialog = new ProgressDialog(DetailActivity.this);
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            JSONObject jObj = null;
            int statusget;
            String reason="";
            try
            {
                jObj = new JSONObject(result);
                JSONArray jsonArray= jObj.getJSONArray("get_pasien");
                JSONObject status_get = jsonArray.getJSONObject(0);
                statusget = status_get.getInt("status");
                reason = status_get.getString("reason");

                if (statusget==-1){
                    alertDialog.setMessage(reason);
                    alertDialog.show();
                }else if(statusget==0){
                    JSONArray array_data= jObj.getJSONArray("Data");
                    riwayats.clear();
                    Riwayat riwayat;
                    for (int i=0;i<array_data.length();i++)
                    {
                        riwayat = new Riwayat();
                        JSONObject explrObject = array_data.getJSONObject(i);
                        String tanggal,keluhan,status;
                        tanggal = explrObject.getString("TANGGAL_PERIKAS");
                        keluhan = explrObject.getString("KELUHAN");
                        status = explrObject.getString("STATUS");
                        if (status.equals("0")){
                            status="Belum Di Terima";
                        }else {
                            status="Diterima";
                        }
                        riwayat.setTanggal(tanggal);
                        riwayat.setKeluhan(keluhan);
                        riwayat.setStatus(status);
                        riwayats.add(riwayat);
                    }
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(DetailActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.riwayat_periksa,null);
                    ListView lv = (ListView) mView.findViewById(R.id.listRiwayat);
                    Button mLogin = (Button) mView.findViewById(R.id.finish);
                    mBuilder.setView(mView);
                    final AlertDialog dialog1 = mBuilder.create();
                    dialog1.show();
                    dialog1.setCancelable(false);
                    riwayat_customAdapter = new Riwayat_CustomAdapter(DetailActivity.this,riwayats);
                    lv.setAdapter(riwayat_customAdapter);
                    mLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog1.dismiss();
                        }
                    });

                }

        } catch (JSONException e) {
                e.printStackTrace();
            }
        }

}
}
