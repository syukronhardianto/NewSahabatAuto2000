package com.example.bismillah.newsahabatauto2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataProspekMonthly {
    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("hitung")
    @Expose
    private String hitung;

    public DataProspekMonthly(String kategori , String hitung) {
        this.kategori = kategori;
        this.hitung = hitung;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getHitung() {
        return hitung;
    }

    public void setHitung(String hitung) {
        this.hitung = hitung;
    }

    @Override
    public String toString(){
        return "DataProspekMonthly{"+
                "kategori='"+kategori+'\''+
                ", hitung='"+hitung+'\''+
                '}';
    }
}
