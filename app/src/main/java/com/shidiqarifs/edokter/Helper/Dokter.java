package com.shidiqarifs.edokter.Helper;

/**
 * Created by user on 20/11/2017.
 */

public class Dokter {

    private String id_dokter;
    private String nama_dokter;
    private String spesialis_dokter;
    private String Waktu_Mulai;
    private String Waktu_Selesai;

    public String getWaktu_Selesai() {
        return Waktu_Selesai;
    }

    public void setWaktu_Selesai(String waktu_Selesai) {
        Waktu_Selesai = waktu_Selesai;
    }

    public String getWaktu_Mulai() {

        return Waktu_Mulai;
    }

    public void setWaktu_Mulai(String waktu_Mulai) {
        Waktu_Mulai = waktu_Mulai;
    }

    public String getSpesialis_dokter() {
        return spesialis_dokter;
    }

    public void setSpesialis_dokter(String spesialis_dokter) {
        this.spesialis_dokter = spesialis_dokter;
    }

    public String getNama_dokter() {

        return nama_dokter;
    }

    public void setNama_dokter(String nama_dokter) {
        this.nama_dokter = nama_dokter;
    }

    public String getId_dokter() {

        return id_dokter;
    }

    public void setId_dokter(String id_dokter) {
        this.id_dokter = id_dokter;
    }



}

