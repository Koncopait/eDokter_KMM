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
public class Riwayat_CustomAdapter  extends BaseAdapter{

    Context c;
    ArrayList<Riwayat> riwayats;
    ArrayList<Riwayat> search_riwayat=null;
    UserSessionManager session;
    public Riwayat_CustomAdapter(Context c, ArrayList<Riwayat> Riwayat_list) {
        this.c = c;
        this.riwayats = Riwayat_list;
        this.search_riwayat = new ArrayList<Riwayat>();
        this.search_riwayat.addAll(riwayats);
    }

    @Override
    public int getCount() {
        return riwayats.size();
    }

    @Override
    public Object getItem(int i) {
        return riwayats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            view=LayoutInflater.from(c).inflate(R.layout.model_riwayat,viewGroup,false);

        }

        TextView tanggalTxt= (TextView) view.findViewById(R.id.tanggal);
        TextView keluhanTxt= (TextView) view.findViewById(R.id.keluhan);
        TextView statusTxt= (TextView) view.findViewById(R.id.status);
        Riwayat riwayat= (Riwayat) this.getItem(i);

        final String tanggal=riwayat.getTanggal();
        final String keluhan= riwayat.getKeluhan();
        final String status = riwayat.getStatus();

        tanggalTxt.setText(tanggal);
        keluhanTxt.setText(keluhan);
        statusTxt.setText(status);

        if (i % 2 == 0) {
            view.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            view.setBackgroundColor(Color.parseColor("#f2efef"));
        }
        return view;
    }


}
