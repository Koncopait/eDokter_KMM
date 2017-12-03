package com.shidiqarifs.edokter.Models;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleParent implements ParentObject{

    private List<Object> mChildrenList;
    private UUID _id;
    private String title_nama, title_spesialis;

    public TitleParent(String title_nama, String tittle_spesialis) {
        this.title_nama = title_nama;
        this.title_spesialis = tittle_spesialis;
        _id = UUID.randomUUID();
    }

    public String getTitle_spesialis() {
        return title_spesialis;
    }

    public void setTitle_spesialis(String title_spesialis) {
        this.title_spesialis = title_spesialis;
    }

    public String getTitle_nama() {

        return title_nama;
    }

    public void setTitle_nama(String title_nama) {
        this.title_nama = title_nama;
    }

    public UUID get_id() {
        return _id;
    }

    public void set_id(UUID _id) {
        this._id = _id;
    }



    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
            mChildrenList = list;
    }
}
