package com.example.bismillah.newsahabatauto2000.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Bismillah on 02/11/2018.
 */

public class RootObject {
    private String value;
    private String message;
    private List<IdNamaSpv> result_idnamaspv;
    private List<DataSales> result_login;
    private List<DataProspek> result_getcust;
    private List<DataProspek> result_cari_prospek;
    private List<DataProspek> result_sort_by_tgl;
    private List<DataProspek> result_sort_by_kategori;
    private List<DataProspekMonthly> result_monthly;
    private List<DataProspekYearly> result_yearly;
    private List<DataProspekTotally> result_totally;

    public RootObject(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<IdNamaSpv> getResult_idnamaspv() {
        return result_idnamaspv;
    }

    public void setResult_idnamaspv(ArrayList<IdNamaSpv> result_idnamaspv) {
        this.result_idnamaspv = result_idnamaspv;
    }

    public List<DataSales> getResult_login() {
        return result_login;
    }

    public void setResult_login(List<DataSales> result_login) {
        this.result_login = result_login;
    }

    public List<DataProspek> getResult_getcust() {
        return result_getcust;
    }

    public void setResult_getcust(List<DataProspek> result_getcust) {
        this.result_getcust = result_getcust;
    }

    public List<DataProspek> getResult_cari_prospek() {
        return result_cari_prospek;
    }

    public void setResult_cari_prospek(List<DataProspek> result_cari_prospek) {
        this.result_cari_prospek = result_cari_prospek;
    }

    public List<DataProspek> getResult_sort_by_tgl() {
        return result_sort_by_tgl;
    }

    public void setResult_sort_by_tgl(List<DataProspek> result_sort_by_tgl) {
        this.result_sort_by_tgl = result_sort_by_tgl;
    }

    public List<DataProspek> getResult_sort_by_kategori() {
        return result_sort_by_kategori;
    }

    public void setResult_sort_by_kategori(List<DataProspek> result_sort_by_kategori) {
        this.result_sort_by_kategori = result_sort_by_kategori;
    }

    public List <DataProspekMonthly> getResult_monthly() {
        return result_monthly;
    }

    public void setResult_monthly(List <DataProspekMonthly> result_monthly) {
        this.result_monthly = result_monthly;
    }

    public List <DataProspekYearly> getResult_yearly() {
        return result_yearly;
    }

    public void setResult_yearly(List <DataProspekYearly> result_yearly) {
        this.result_yearly = result_yearly;
    }

    public List <DataProspekTotally> getResult_totally() {
        return result_totally;
    }

    public void setResult_totally(List <DataProspekTotally> result_totally) {
        this.result_totally = result_totally;
    }
}
