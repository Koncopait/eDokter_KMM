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


import com.shidiqarifs.edokter.R;
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
public class CustomAdapter_Doctor  extends BaseAdapter{

    Context c;
    ArrayList<Dokter> dokters;
    ArrayList<Dokter> search_dokter=null;
    UserSessionManager session;
    public CustomAdapter_Doctor(Context c, ArrayList<Dokter> Dokter_list) {
        this.c = c;
        this.dokters = Dokter_list;
        this.search_dokter = new ArrayList<Dokter>();
        this.search_dokter.addAll(dokters);
    }

    @Override
    public int getCount() {
        return dokters.size();
    }

    @Override
    public Object getItem(int i) {
        return dokters.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            view=LayoutInflater.from(c).inflate(R.layout.model_dokter,viewGroup,false);

        }

        TextView nameTxt= (TextView) view.findViewById(R.id.nameTxt);
        TextView spesTxt= (TextView) view.findViewById(R.id.sp_dokter);
        Dokter dokter= (Dokter) this.getItem(i);

        final String name=dokter.getNama_dokter();
        final String spesialis=dokter.getSpesialis_dokter();
        final String id_dokter = dokter.getId_dokter();


        nameTxt.setText("Dr. "+name);
        spesTxt.setText("Spesialis : "+spesialis);


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
        Intent i=new Intent(c,Splash_Activity.class);
        i.putExtra("NAME_KEY",details[0]);
        i.putExtra("EMAIL_KEY",details[1]);
        i.putExtra("ID",details[2]);
        c.startActivity(i);

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dokters.clear();
        if (charText.length() == 0) {
            dokters.addAll(search_dokter);
        }
        else
        {
            for (Dokter wp : search_dokter) {
                if (wp.getNama_dokter().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dokters.add(wp);
                }else if (wp.getSpesialis_dokter().toLowerCase(Locale.getDefault()).contains(charText)) {
                    dokters.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
