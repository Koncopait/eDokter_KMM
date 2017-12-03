package com.shidiqarifs.edokter.Helper;

/**
 * Created by user on 21/07/2017.
 */
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.shidiqarifs.edokter.DetailActivity;
import com.shidiqarifs.edokter.Pasien_DetailActivity;
import com.shidiqarifs.edokter.R;
import com.shidiqarifs.edokter.Recyle;
import com.shidiqarifs.edokter.Splash_Activity;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Oclemy on 7/15/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class CustomAdapter_Pasien  extends BaseAdapter{

    Context c;
    ArrayList<Pasien> pasiens;
    ArrayList<Pasien> search_pasien=null;
    UserSessionManager session;
    public CustomAdapter_Pasien(Context c, ArrayList<Pasien> pasien) {
        this.c = c;
        this.pasiens = pasien;
        this.search_pasien = new ArrayList<Pasien>();
        this.search_pasien.addAll(pasiens);
    }

    @Override
    public int getCount() {
        return pasiens.size();
    }

    @Override
    public Object getItem(int i) {
        return pasiens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            view=LayoutInflater.from(c).inflate(R.layout.model,viewGroup,false);

        }

        Button accept = (Button)view.findViewById(R.id.accept);
        Button decline = (Button)view.findViewById(R.id.decline);
        TextView nameTxt= (TextView) view.findViewById(R.id.nameTxt);
        TextView keluhanTxt= (TextView) view.findViewById(R.id.penyakit);
        Pasien pasien= (Pasien) this.getItem(i);

        final String name=pasien.getNama_pasien();
        final String keluhan= pasien.getKeluhan();
        final String id_pasien = pasien.getId_pasien();


        nameTxt.setText(name);
        keluhanTxt.setText(keluhan);


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OPEN DETAIL ACTIVITY
                openDetailActivity(id_pasien,name,keluhan);

            }
        });

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OPEN DETAIL ACTIVITY
                openDetailActivity(Fullname,diag);

            }
        });*/

        if (i % 2 == 0) {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            view.setBackgroundColor(Color.parseColor("#f2efef"));
        }
        return view;
    }
    ////open activity
    private void openDetailActivity(String...details)
    {
        Intent i= new Intent(c,Pasien_DetailActivity.class);
        i.putExtra("ID_PASIEN",details[0]);
        i.putExtra("NAMA_PASIEN",details[1]);
        i.putExtra("KELUHAN",details[2]);
        c.startActivity(i);

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        pasiens.clear();
        if (charText.length() == 0) {
            pasiens.addAll(search_pasien);
        }
        else
        {
            for (Pasien wp : search_pasien) {
                if (wp.getNama_pasien().toLowerCase(Locale.getDefault()).contains(charText)) {
                    pasiens.add(wp);
                }else if (wp.getKeluhan().toLowerCase(Locale.getDefault()).contains(charText)) {
                    pasiens.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
