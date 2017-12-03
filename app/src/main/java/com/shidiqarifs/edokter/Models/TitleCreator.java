package com.shidiqarifs.edokter.Models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleCreator {
    static TitleCreator _titleCreator;
    List<TitleParent> _titleParents;

    public TitleCreator(Context context, String nama, String spesialis) {
        _titleParents = new ArrayList<>();
            TitleParent title = new TitleParent(nama,spesialis);
            _titleParents.add(title);
    }

    public TitleCreator get(Context context,String nama, String spesialis)
    {
        if(_titleCreator == null)
            _titleCreator = new TitleCreator(context,nama,spesialis);
        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }
}
