package com.shidiqarifs.edokter.Helper;

/**
 * Created by user on 27/11/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import com.shidiqarifs.edokter.Models.TitleChild;
import com.shidiqarifs.edokter.Models.TitleParent;
import com.shidiqarifs.edokter.R;
import com.shidiqarifs.edokter.ViewHolders.TitleChildViewHolder;
import com.shidiqarifs.edokter.ViewHolders.TitleParentViewHolder;
import com.shidiqarifs.edokter.ViewHolders.TitleParentViewHolder_Keluhan;

/**
 * Created by reale on 23/11/2016.
 */

public class RecycleAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder,TitleChildViewHolder> {

    LayoutInflater inflater;

    public RecycleAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_parent,viewGroup,false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_child,viewGroup,false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent title = (TitleParent)o;

    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {
        TitleChild title = (TitleChild)o;
        titleChildViewHolder.option1.setText(title.getOption1());
        titleChildViewHolder.option2.setText(title.getOption2());

    }




}

