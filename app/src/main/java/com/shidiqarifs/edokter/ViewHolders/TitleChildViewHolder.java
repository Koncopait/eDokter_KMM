package com.shidiqarifs.edokter.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import com.shidiqarifs.edokter.R;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleChildViewHolder extends ChildViewHolder {
    public TextView option1,option2;
    public TitleChildViewHolder(View itemView) {
        super(itemView);
        option1 = (TextView)itemView.findViewById(R.id.option1);
        option2 = (TextView)itemView.findViewById(R.id.option2);
    }
}
