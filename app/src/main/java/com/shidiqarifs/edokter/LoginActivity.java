package com.shidiqarifs.edokter;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.shidiqarifs.edokter.Helper.DatabaseHandler_Doctor;
import com.shidiqarifs.edokter.Helper.DatabaseHandler_Pasien;
import com.shidiqarifs.edokter.Helper.Dokter;
import com.shidiqarifs.edokter.Helper.Pasien;
import com.shidiqarifs.edokter.Helper.UserSessionManager;
import  com.shidiqarifs.edokter.Helper.get_url_link;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends Activity  {

    Button login;
    TextView register;
    UserSessionManager session;

    EditText username_et, password_et;
    String username,password;
    String getPassword;
    TextView forgotpass,sigup;
    private DatabaseHandler_Doctor databaseHelper;
    private DatabaseHandler_Pasien databaseHandler_pasien;
    private Dokter dokter;
    private Pasien pasien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.btn_login);
        session = new UserSessionManager(getApplicationContext());
        forgotpass = (TextView) findViewById(R.id.forgotpass);
        sigup = (TextView) findViewById(R.id.signup);
        username_et = (EditText) findViewById(R.id.username);
        password_et = (EditText) findViewById(R.id.password);
        databaseHelper = new DatabaseHandler_Doctor(LoginActivity.this);
        databaseHandler_pasien = new DatabaseHandler_Pasien(LoginActivity.this);
        sigup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent forget = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(forget);

            }
        });

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                username = username_et.getText().toString().toLowerCase();
                password = password_et.getText().toString().toLowerCase();
                getPassword= password;
                if (TextUtils.isEmpty(username)) {
                    Toast msg = Toast.makeText(LoginActivity.this, "Username is Empty.", Toast.LENGTH_LONG);
                    msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                    msg.show();
                }else if (TextUtils.isEmpty(password)){
                    Toast msg = Toast.makeText(LoginActivity.this, "Password is Empty.", Toast.LENGTH_LONG);
                    msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
                    msg.show();
                }else{
                    String type = "api_login";
                    new Masuk().execute(type,username,password);
                }
            }
        });
        forgotpass.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent forget = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(forget);

            }
        });


        sigup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent forget = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(forget);

            }
        });



    }

    private class Masuk extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        AlertDialog alertDialog;
        @Override
        public String doInBackground(String... params) {
            String type = params [0];
            get_url_link link = new get_url_link();
            String login_url = link.getUrl_link(type);
            String username = params [1];
            String password = params [2];
            String result = null;
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "username="+username+"&password="+password);
                Request request = new Request.Builder()
                        .url(login_url)
                        .post(body)
                        .addHeader("username", username)
                        .addHeader("password", password)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "1f0ab2d9-c7a8-9bd4-f729-54df41618bf4")
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line="";
            return result;
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Login Status");
            pDialog = new ProgressDialog(LoginActivity.this);
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
                JSONArray jsonArray= jObj.getJSONArray("login");
                JSONObject explrObject = jsonArray.getJSONObject(0);
                status = explrObject.getInt("login_status");
                reason = explrObject.getString("reason");

            }
            catch (JSONException e)
            {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
            if(status == -1){
                alertDialog.setMessage(reason);
                alertDialog.show();
            }else if (status == 0){
                int user_status = 100;
                try
                {
                    pDialog.dismiss();
                    alertDialog.setMessage(reason);
                    alertDialog.show();
                    JSONArray jsonArray= jObj.getJSONArray("Data");
                    JSONObject explrObject = jsonArray.getJSONObject(0);
                    user_status = explrObject.getInt("STATUS");

                    if (user_status==1){
                        String id_user = explrObject.getString("ID_PASIEN");
                        new getlist().execute();
                        Intent Home = new Intent(LoginActivity.this, MainActivity.class);
                        session.createUserLoginSession(id_user,password,String.valueOf(user_status));
                        startActivity(Home);
                        finish();
                    }else if(user_status==0){
                        String id_dokter = explrObject.getString("ID_DOKTER");
                        new getlist_pasien().execute(id_dokter);
                        Intent Home = new Intent(LoginActivity.this, MainActivity.class);
                        session.createUserLoginSession(id_dokter,password,String.valueOf(user_status));
                        startActivity(Home);
                        finish();
                    }
                }catch (JSONException e)
                {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();
                }
            }
        }


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
        }

        @Override
        protected void onPostExecute(String result) {
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
            }
            catch (JSONException e)
            {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
        }
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
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject jObj = null;
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
                    databaseHandler_pasien.addUser(pasien);
                }
            }
            catch (JSONException e)
            {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
