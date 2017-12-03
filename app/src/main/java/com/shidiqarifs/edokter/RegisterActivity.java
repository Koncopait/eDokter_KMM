package com.shidiqarifs.edokter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shidiqarifs.edokter.Helper.DatabaseHandler_Doctor;
import com.shidiqarifs.edokter.Helper.Dokter;
import com.shidiqarifs.edokter.Helper.UserSessionManager;
import com.shidiqarifs.edokter.Helper.get_url_link;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shidiqarifs on 21/11/2017.
 */

public class RegisterActivity extends AppCompatActivity {
    Button sigup;
    UserSessionManager session;
    EditText email_et,username_et, password_et, nm_depan, nm_belakang,con_password_et;
    TextView login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sigup = (Button) findViewById(R.id.btn_signup);
        login = (TextView) findViewById(R.id.signin);
        username_et = (EditText) findViewById(R.id.sg_username);
        email_et = (EditText) findViewById(R.id.sg_email);
        password_et = (EditText) findViewById(R.id.ed_password);
        con_password_et = (EditText) findViewById(R.id.sg_confirmpassword);
        nm_depan = (EditText) findViewById(R.id.nmdepan);
        nm_belakang = (EditText) findViewById(R.id.nmbelakang);
        final TextInputLayout username_lay = (TextInputLayout) findViewById(R.id.textInputUsername);
        final TextInputLayout password_lay = (TextInputLayout) findViewById(R.id.textInputPass);
        final TextInputLayout email_lay = (TextInputLayout) findViewById(R.id.textInputemail);
        final TextInputLayout nm_dpn_lay = (TextInputLayout) findViewById(R.id.textInputnmdepan);
        final TextInputLayout nm_blkng_lay = (TextInputLayout) findViewById(R.id.textInputnmbelakang);
        final TextInputLayout con_pass_lay = (TextInputLayout) findViewById(R.id.textInputconfirpass);




        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent forget = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(forget);
                finish();
            }
        });
        sigup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = username_et.getText().toString().toLowerCase();
                String email = email_et.getText().toString().toLowerCase();
                String namadepan = nm_depan.getText().toString().toLowerCase();
                String namabelakang = nm_belakang.getText().toString().toLowerCase();
                String password = password_et.getText().toString().toLowerCase();
                    new register().execute(username,email,namadepan,namabelakang,password);
            }
        });

        password_et.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (password_et.getText().length()< 6 || password_et.getText().length()> 15 ){
                    password_lay.setError("Password between 6 and 10 alphanumeric characters.");
                    sigup.setEnabled(false);
                }else {
                    password_lay.setError(null);
                    sigup.setEnabled(true);
                }
            }
        });

        email_et.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (TextUtils.isEmpty(email_et.getText().toString()) || !email_et.getText().toString().matches(emailPattern)){
                    email_lay.setError("Enter a valid email address.");
                    sigup.setEnabled(false);
                }else {
                    email_lay.setError(null);
                    sigup.setEnabled(true);
                }
            }
        });

        username_et.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (username_et.getText().toString().equals("")){
                    username_lay.setError("Username is Empty.");
                    sigup.setEnabled(false);
                }else {
                    username_lay.setError(null);
                    sigup.setEnabled(true);
                }
            }
        });

        con_password_et.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (!con_password_et.getText().toString().equals(password_et.getText().toString())){
                    con_pass_lay.setError("Password Doesn't Match.");
                    sigup.setEnabled(false);
                }else {
                    con_pass_lay.setError(null);
                    sigup.setEnabled(true);
                }
            }
        });


        nm_depan.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (nm_depan.getText().toString().equals("")){
                    nm_dpn_lay.setError("Nama Depan is Empty.");
                    sigup.setEnabled(false);
                }else {
                    nm_dpn_lay.setError(null);
                    sigup.setEnabled(true);
                }
            }
        });

        nm_belakang.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange (View v, boolean hasFocus){
                if (nm_belakang.getText().toString().equals("")){
                    nm_blkng_lay.setError("Nama Belakang is Empty.");
                    sigup.setEnabled(false);
                }else {
                    nm_blkng_lay.setError(null);
                    sigup.setEnabled(true);
                }
            }
        });

    }



    private class register extends AsyncTask<String, Void, String>
    {
        ProgressDialog pDialog;

        AlertDialog alertDialog;

        @Override
        public String doInBackground(String... params) { //mengirim parameter ke api
            String username = params[0];
            String email = params[1];
            String nm_depan = params[2];
            String nm_belakang = params[3];
            String password = params[4];
            get_url_link link = new get_url_link();
            String url = link.getUrl_link("api_register");
            String result = null;
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "username="+username+"&email="+email+"&nm_depan="+nm_depan+"&nm_belakang="+nm_belakang+"&password="+password);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "a74e4b36-604d-5c8a-0512-e7dca84cc23f")
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
        @Override
        protected void onPreExecute() { //menampilkan dialog proggress

            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Register Status");
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Wait a Moment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) { //respon

            JSONObject jObj = null;
            String reason;
            int register_status=100;
            try
            {
                jObj = new JSONObject(result);
                JSONArray array_data= jObj.getJSONArray("daftar");
                JSONObject explrObject = array_data.getJSONObject(0);
                int status = explrObject.getInt("daftar_status");
                reason = explrObject.getString("reason");

                if (status==0){
                    pDialog.dismiss();
                    alertDialog.setMessage(reason);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    new CountDownTimer(5000, 1000) {
                        public void onFinish() {
                            Intent forget = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(forget);
                            finish();
                        }

                        public void onTick(long millisUntilFinished) {
                            // millisUntilFinished    The amount of time until finished.
                        }
                    }.start();
                }else {
                    alertDialog.setMessage(reason);
                    alertDialog.show();
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
