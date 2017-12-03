package com.shidiqarifs.edokter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.shidiqarifs.edokter.Helper.DatabaseHandler_Doctor;
import com.shidiqarifs.edokter.Helper.UserSessionManager;

import java.util.HashMap;

/**
 * Created by user on 26/11/2017.
 */

public class Pasien_DetailActivity extends AppCompatActivity {


    TextView nameTxt,keluhanTxt, usernameTxt;
    CardView Medi,Immun,Prescription;
    ImageButton btnclickme, btnback;
    String token="";
    private DatabaseHandler_Doctor db;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpasien);
        session = new UserSessionManager(getApplicationContext());
        nameTxt  = (TextView) findViewById(R.id.namadet);
        keluhanTxt  = (TextView) findViewById(R.id.medicalprob);
        /*Medi = (CardView) findViewById(R.id.cd_inputKeluhan);
        Immun = (CardView) findViewById(R.id.cd_tanyaDokter);
        Prescription = (CardView) findViewById(R.id.cd_riwayatPeriksa);
        */if(session.checkLogin())
            finish();
        if (!session.checkLogin()){
            HashMap<String, String> user = session.getUserDetails();
        }else{
            session.logoutUser();
        }

        btnback = (ImageButton) findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Intent i= this.getIntent();

        //RECEIVE DATA
        final String id = i.getExtras().getString("ID_PASIEN");
        String nama =i.getExtras().getString("NAMA_PASIEN");
        String keluhan =i.getExtras().getString("KELUHAN");

        //BIND DATA
        nameTxt.setText(nama);
        keluhanTxt.setText(keluhan);

    }
}
