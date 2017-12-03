package com.shidiqarifs.edokter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.shidiqarifs.edokter.Helper.RecycleAdapter;

import java.util.ArrayList;
import java.util.List;
import com.shidiqarifs.edokter.Models.TitleChild;
import com.shidiqarifs.edokter.Models.TitleCreator;
import com.shidiqarifs.edokter.Models.TitleParent;

public class Recyle extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView nameTxt,spesialisTxt, usernameTxt;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((RecycleAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaildokter);
        nameTxt = (TextView) findViewById(R.id.namadet);
        spesialisTxt = (TextView) findViewById(R.id.medicalprob);
        Intent i= this.getIntent();

        //RECEIVE DATA
        final String id = i.getExtras().getString("ID_DOKTER");
        String nama =i.getExtras().getString("NAMA_DOKTER");
        String spesialis =i.getExtras().getString("SPESIALIS");


        nameTxt.setText(nama);
        spesialisTxt.setText(spesialis);


        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecycleAdapter adapter = new RecycleAdapter(this,initData(nama,spesialis));
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);
    }

    private List<ParentObject> initData(String nama, String spesialis) {

        TitleCreator titleCreator = new TitleCreator (this,nama,spesialis );
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        for(TitleParent title:titles)
        {
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChild("Add to contacts","Send message"));
            title.setChildObjectList(childList);
            parentObject.add(title);
        }
        return parentObject;

    }
}
