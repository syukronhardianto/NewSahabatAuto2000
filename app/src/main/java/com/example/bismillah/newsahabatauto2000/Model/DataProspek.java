package com.example.bismillah.newsahabatauto2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bismillah on 09/11/2018.
 */

public class DataProspek {
    @SerializedName("id_cust")
    @Expose
    private String id_cust;
    @SerializedName("nama_cust")
    @Expose
    private String nama_cust;
    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("jenis_kel_cust")
    @Expose
    private String jenis_kel_cust;
    @SerializedName("jenis_mobil")
    @Expose
    private String jenis_mobil;
    @SerializedName("warna_mobil")
    @Expose
    private String warna_mobil;
    @SerializedName("alamat_cust")
    @Expose
    private String alamat_cust;
    @SerializedName("telp_cust")
    @Expose
    private String telp_cust;
    @SerializedName("email_cust")
    @Expose
    private String email_cust;
    @SerializedName("tgl_cust")
    @Expose
    private String tgl_cust;
    @SerializedName("agama_cust_do")
    @Expose
    private String agama_cust_do;
    @SerializedName("tgl_lahir_cust_do")
    @Expose
    private String tgl_lahir_cust_do;

    public DataProspek(String id_cust, String nama_cust, String kategori, String jenis_kel_cust, String jenis_mobil,
                       String warna_mobil, String alamat_cust, String telp_cust, String email_cust, String tgl_cust,
                       String agama_cust_do, String tgl_lahir_cust_do) {
        this.id_cust = id_cust;
        this.nama_cust = nama_cust;
        this.kategori = kategori;
        this.jenis_kel_cust = jenis_kel_cust;
        this.jenis_mobil = jenis_mobil;
        this.warna_mobil = warna_mobil;
        this.alamat_cust = alamat_cust;
        this.telp_cust = telp_cust;
        this.email_cust = email_cust;
        this.tgl_cust = tgl_cust;
        this.agama_cust_do = agama_cust_do;
        this.tgl_lahir_cust_do = tgl_lahir_cust_do;
    }

    public String getId_cust() {
        return id_cust;
    }

    public void setId_cust(String id_cust) {
        this.id_cust = id_cust;
    }

    public String getNama_cust() {
        return nama_cust;
    }

    public void setNama_cust(String nama_cust) {
        this.nama_cust = nama_cust;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJenis_kel_cust() {
        return jenis_kel_cust;
    }

    public void setJenis_kel_cust(String jenis_kel_cust) {
        this.jenis_kel_cust = jenis_kel_cust;
    }

    public String getJenis_mobil() {
        return jenis_mobil;
    }

    public void setJenis_mobil(String jenis_mobil) {
        this.jenis_mobil = jenis_mobil;
    }

    public String getWarna_mobil() {
        return warna_mobil;
    }

    public void setWarna_mobil(String warna_mobil) {
        this.warna_mobil = warna_mobil;
    }

    public String getAlamat_cust() {
        return alamat_cust;
    }

    public void setAlamat_cust(String alamat_cust) {
        this.alamat_cust = alamat_cust;
    }

    public String getTelp_cust() {
        return telp_cust;
    }

    public void setTelp_cust(String telp_cust) {
        this.telp_cust = telp_cust;
    }

    public String getEmail_cust() {
        return email_cust;
    }

    public void setEmail_cust(String email_cust) {
        this.email_cust = email_cust;
    }

    public String getTgl_cust() {
        return tgl_cust;
    }

    public void setTgl_cust(String tgl_cust) {
        this.tgl_cust = tgl_cust;
    }

    public String getAgama_cust_do() {
        return agama_cust_do;
    }

    public void setAgama_cust_do(String agama_cust_do) {
        this.agama_cust_do = agama_cust_do;
    }

    public String getTgl_lahir_cust_do() {
        return tgl_lahir_cust_do;
    }

    public void setTgl_lahir_cust_do(String tgl_lahir_cust_do) {
        this.tgl_lahir_cust_do = tgl_lahir_cust_do;
    }

    @Override
    public String toString(){
        return "DataProspek{"+
                "id_cust='"+id_cust+'\''+
                ", nama_cust='"+nama_cust+'\''+
                ", kategori='"+kategori+'\''+
                ", jenis_kel_cust='"+jenis_kel_cust+'\''+
                ", jenis_mobil='"+jenis_mobil+'\''+
                ", warna_mobil='"+warna_mobil+'\''+
                ", alamat_cust='"+alamat_cust+'\''+
                ", telp_cust='"+telp_cust+'\''+
                ", email_cust='"+email_cust+'\''+
                ", tgl_cust='"+tgl_cust+'\''+
                ", agama_cust_do='"+agama_cust_do+'\''+
                ", tgl_lahir_cust_do='"+tgl_lahir_cust_do+'\''+
                '}';
    }
}
