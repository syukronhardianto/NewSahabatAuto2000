package com.example.bismillah.newsahabatauto2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bismillah on 02/11/2018.
 */

public class IdNamaSpv {
    @SerializedName("id_spv")
    @Expose
    private String id_spv;
    @SerializedName("nama_spv")
    @Expose
    private String nama_spv;

    public IdNamaSpv(String id_spv, String nama_spv) {
        this.id_spv = id_spv;
        this.nama_spv = nama_spv;
    }

    public String getId_spv() {
        return id_spv;
    }

    public void setId_spv(String id_spv) {
        this.id_spv = id_spv;
    }

    public String getNama_spv() {
        return nama_spv;
    }

    public void setNama_spv(String nama_spv) {
        this.nama_spv = nama_spv;
    }

    @Override
    public String toString(){
        return "IdNamaSpv{"+
                "id_spv='"+id_spv+'\''+
                ", nama_spv='"+nama_spv+'\''+
                '}';
    }
}
