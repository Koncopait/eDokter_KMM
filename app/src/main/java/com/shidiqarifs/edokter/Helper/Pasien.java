package com.shidiqarifs.edokter.Helper;

/**
 * Created by user on 28/11/2017.
 */

public class Pasien {

    private String id_pasien;
    private String nama_pasien;
    private String keluhan;

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public String getNama_pasien() {

        return nama_pasien;
    }

    public void setNama_pasien(String nama_pasien) {
        this.nama_pasien = nama_pasien;
    }

    public String getId_pasien() {

        return id_pasien;
    }

    public void setId_pasien(String id_pasien) {
        this.id_pasien = id_pasien;
    }
}
