package com.example.bismillah.newsahabatauto2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bismillah on 08/11/2018.
 */

public class DataSales {
    @SerializedName("id_sales")
    @Expose
    private String id_sales;
    @SerializedName("nama_sales")
    @Expose
    private String nama_sales;
    @SerializedName("username_sales")
    @Expose
    private String username_sales;
    @SerializedName("telp_sales")
    @Expose
    private String telp_sales;
    @SerializedName("email_sales")
    @Expose
    private String email_sales;
    @SerializedName("foto_sales")
    @Expose
    private String foto_sales;

    public DataSales(String id_sales, String nama_sales, String username_sales, String telp_sales, String email_sales, String foto_sales) {
        this.id_sales = id_sales;
        this.nama_sales = nama_sales;
        this.username_sales = username_sales;
        this.telp_sales = telp_sales;
        this.email_sales = email_sales;
        this.foto_sales = foto_sales;
    }

    public String getId_sales() {
        return id_sales;
    }

    public void setId_sales(String id_sales) {
        this.id_sales = id_sales;
    }

    public String getNama_sales() {
        return nama_sales;
    }

    public void setNama_sales(String nama_sales) {
        this.nama_sales = nama_sales;
    }

    public String getUsername_sales() {
        return username_sales;
    }

    public void setUsername_sales(String username_sales) {
        this.username_sales = username_sales;
    }

    public String getTelp_sales() {
        return telp_sales;
    }

    public void setTelp_sales(String telp_sales) {
        this.telp_sales = telp_sales;
    }

    public String getEmail_sales() {
        return email_sales;
    }

    public void setEmail_sales(String email_sales) {
        this.email_sales = email_sales;
    }

    public String getFoto_sales() {
        return foto_sales;
    }

    public void setFoto_sales(String foto_sales) {
        this.foto_sales = foto_sales;
    }

    @Override
    public String toString(){
        return "DataSales{"+
                "id_sales='"+id_sales+'\''+
                ", nama_sales='"+nama_sales+'\''+
                ", username_sales='"+username_sales+'\''+
                ", telp_sales='"+telp_sales+'\''+
                ", email_sales='"+email_sales+'\''+
                ", foto_sales='"+foto_sales+'\''+
                '}';
    }
}
