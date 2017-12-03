package com.shidiqarifs.edokter.ViewHolders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import com.shidiqarifs.edokter.R;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleParentViewHolder extends ParentViewHolder {
    public TextView _textView_nama;
    public TextView _textView_spesialis;
    public ImageButton _imageButton;

    public TitleParentViewHolder(View itemView) {
        super(itemView);
        _textView_nama = (TextView)itemView.findViewById(R.id.inputdiagnosa);
    }
}
