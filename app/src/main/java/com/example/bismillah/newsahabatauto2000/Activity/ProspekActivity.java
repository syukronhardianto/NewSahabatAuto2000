package com.example.bismillah.newsahabatauto2000.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bismillah.newsahabatauto2000.Adapter.ProspekListAdapter;
import com.example.bismillah.newsahabatauto2000.Interface.ProspekAPI;
import com.example.bismillah.newsahabatauto2000.Model.DataProspek;
import com.example.bismillah.newsahabatauto2000.Model.RootObject;
import com.example.bismillah.newsahabatauto2000.Network.NetworkCLient2;
import com.example.bismillah.newsahabatauto2000.R;
import com.example.bismillah.newsahabatauto2000.SessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ProspekActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private EditText tglAwal, tglAkhir;
    private Spinner spinnerSortKategori;
    String[] kategori = {"Pilih Kategori:", "Suspect", "Hot", "DO"};
    ArrayAdapter<String> adapter;
    private AlertDialog dialog;
    private String getTglStringAwal, getTglStringAkhir, id_sales, searchSpinnerString;
    private ProgressDialog progress;
    private List<DataProspek> dataProspekList = new ArrayList<>();
    private ProspekListAdapter prospekListAdapter;
    private CompositeDisposable disposables = new CompositeDisposable();
    private RecyclerView prospekRecyclerView;
    private SessionManager sessionManager;
    private int mYear, mMonth, mDay;
    DatePickerDialog mDatePicker;
    String mQueryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospek);

        sessionManager = new SessionManager(this);

        Calendar mcurrentDate = Calendar.getInstance();
        mYear= mcurrentDate.get(Calendar.YEAR);
        mMonth= mcurrentDate.get(Calendar.MONTH);
        mDay= mcurrentDate.get(Calendar.DAY_OF_MONTH);

        FloatingActionButton floatingActionButtonAddProspek = findViewById(R.id.float_action_btn_addprospek);
        prospekRecyclerView = findViewById(R.id.prospek_recyclerView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategori);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Prospek");

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);

        prospekListAdapter = new ProspekListAdapter(this, dataProspekList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        prospekRecyclerView.setLayoutManager(layoutManager);
        prospekRecyclerView.setItemAnimator(new DefaultItemAnimator());
        prospekRecyclerView.setAdapter(prospekListAdapter);

        getIdSales();

        floatingActionButtonAddProspek.setOnClickListener(this);
    }

    private void getIdSales(){
        HashMap map = sessionManager.getDetailLogin();
        id_sales = (String) map.get(SessionManager.KEY_ID_SALES);
    }

    private void getProspek(){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Mengambil data prospek...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI = retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.getProspek(id_sales)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RootObject>() {
                    @Override
                    public void onNext(RootObject rootObject) {
                        progress.dismiss();
                        try{
                            String nilai = rootObject.getValue();
                            String pesan = rootObject.getMessage();
                            if (nilai.equals("0")){
                                Toast.makeText(ProspekActivity.this, pesan, Toast.LENGTH_SHORT).show();
                            } else if (nilai.equals("1")){
                                dataProspekList = rootObject.getResult_getcust();
                                prospekListAdapter = new ProspekListAdapter(ProspekActivity.this, dataProspekList);
                                prospekRecyclerView.setAdapter(prospekListAdapter);
                            }
                        } catch (Exception e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProspekActivity.this);
                            builder.setMessage("Gagal ambil data prospek: "+e);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Coba lagi", (dialog, id) -> getProspek());
                            builder.setNegativeButton("Batal", (dialog, id) -> {
                                dialog.cancel();
                                ProspekActivity.this.finish();
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProspekActivity.this);
                        builder.setMessage("Gagal ambil data prospek, onError: "+e);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Coba lagi", (dialog, id) -> getProspek());
                        builder.setNegativeButton("Batal", (dialog, id) -> {
                            dialog.cancel();
                            ProspekActivity.this.finish();
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onComplete() {
                        progress.dismiss();
                    }
                }));
    }

    private void getSortByDate(){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Sorting data prospek...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI = retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.getProspekSortByDate(id_sales,getTglStringAwal, getTglStringAkhir)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<RootObject>() {
                            @Override
                            public void onNext(RootObject rootObject) {
                                progress.dismiss();
                                try{
                                    String nilai = rootObject.getValue();
                                    if (nilai.equals("1")){
                                        dataProspekList = rootObject.getResult_sort_by_tgl();
                                        prospekListAdapter = new ProspekListAdapter(ProspekActivity.this, dataProspekList);
                                        prospekRecyclerView.setAdapter(prospekListAdapter);
                                    }
                                } catch (Exception e){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProspekActivity.this);
                                    builder.setMessage("Gagal sorting data prospek: "+e);
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Coba lagi", (dialog, id) -> getSortByDate());
                                    builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                progress.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(ProspekActivity.this);
                                builder.setMessage("Gagal sorting data prospek, onError: "+e);
                                builder.setCancelable(false);
                                builder.setPositiveButton("Coba lagi", (dialog, id) -> getSortByDate());
                                builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                            @Override
                            public void onComplete() {
                                progress.dismiss();
                            }
                        }));

    }

    private void searchProspek(String query){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Mencari data prospek...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI = retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.getProspekSearchResult(query, id_sales)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<RootObject>() {
                        @Override
                        public void onNext(RootObject rootObject) {
                            progress.dismiss();
                            try{
                                String nilai = rootObject.getValue();
                                if (nilai.equals("1")){
                                    dataProspekList = rootObject.getResult_cari_prospek();
                                    prospekListAdapter = new ProspekListAdapter(ProspekActivity.this, dataProspekList);
                                    prospekRecyclerView.setAdapter(prospekListAdapter);
                                }
                            } catch (Exception e){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProspekActivity.this);
                                builder.setMessage("Gagal cari data prospek: "+e);
                                builder.setCancelable(false);
                                builder.setPositiveButton("Coba lagi", (dialog, id) -> searchProspek(query));
                                builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            progress.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ProspekActivity.this);
                            builder.setMessage("Gagal cari data prospek, onError: "+e);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Coba lagi", (dialog, id) -> searchProspek(query));
                            builder.setNegativeButton("Batal", (dialog, id) -> {
                                dialog.cancel();
                                ProspekActivity.this.finish();
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                        @Override
                        public void onComplete() {
                            progress.dismiss();
                        }
                    }));
    }

    private void getSortByKategori(){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Sorting data prospek...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI = retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.getProspekSortByKategori(searchSpinnerString,id_sales)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<RootObject>() {
                        @Override
                        public void onNext(RootObject rootObject) {
                            progress.dismiss();
                            try{
                                String nilai = rootObject.getValue();
                                if (nilai.equals("1")){
                                    dataProspekList = rootObject.getResult_sort_by_kategori();
                                    prospekListAdapter = new ProspekListAdapter(ProspekActivity.this, dataProspekList);
                                    prospekRecyclerView.setAdapter(prospekListAdapter);
                                }
                            } catch (Exception e){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProspekActivity.this);
                                builder.setMessage("Gagal sorting data prospek: "+e);
                                builder.setCancelable(false);
                                builder.setPositiveButton("Coba lagi", (dialog, id) -> getSortByKategori());
                                builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            progress.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProspekActivity.this);
                            builder.setMessage("Gagal sorting data prospek, onError: "+e);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Coba lagi", (dialog, id) -> getSortByKategori());
                            builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                        @Override
                        public void onComplete() {
                            progress.dismiss();
                        }
                    }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
            case R.id.refresh:
                getProspek();
                break;
            case R.id.sort_kategori:
                AlertDialog.Builder builder0 = new AlertDialog.Builder(this);
                LayoutInflater inflater0 = getLayoutInflater();
                @SuppressLint("InflateParams") View dialogView0 = inflater0.inflate(R.layout.custom_alertdialog_sorting_kategori, null);
                builder0.setView(dialogView0);

                spinnerSortKategori = dialogView0.findViewById(R.id.sorting_kategori_spinner);
                Button sortirBulan = dialogView0.findViewById(R.id.kategori_pilih_btn);

                spinnerSortKategori.setAdapter(adapter);

                spinnerSortKategori.setOnItemSelectedListener(this);
                sortirBulan.setOnClickListener(this);

                dialog = builder0.create();
                dialog.show();
                break;
            case R.id.sort_date:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                LayoutInflater inflater1 = getLayoutInflater();
                @SuppressLint("InflateParams") View dialogView1 = inflater1.inflate(R.layout.custom_alertdialog_sorting_date, null);
                builder1.setView(dialogView1);

                tglAwal = dialogView1.findViewById(R.id.tgl_awal_edt);
                tglAkhir = dialogView1.findViewById(R.id.tgl_akhir_edt);
                Button pilihTglBtn = dialogView1.findViewById(R.id.tgl_pilih_btn);

                tglAwal.setFocusable(false);
                tglAwal.setClickable(true);
                tglAwal.setLongClickable(false);
                tglAkhir.setFocusable(false);
                tglAkhir.setClickable(true);
                tglAkhir.setLongClickable(false);
                pilihTglBtn.setOnClickListener(this);
                tglAwal.setOnClickListener(this);
                tglAkhir.setOnClickListener(this);

                dialog = builder1.create();
                dialog.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menubar, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Cari nama/jenis mobil");
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()){
                    searchView.setIconified(true);
                }

                mQueryString = query;

                searchProspek(mQueryString);

                (menu.findItem(R.id.action_search)).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.collapseActionView(item);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.float_action_btn_addprospek:
                Intent inputProspekIntent = new Intent(ProspekActivity.this, InputProspekActivity.class);
                startActivity(inputProspekIntent);
                break;
            case R.id.tgl_awal_edt:
                mDatePicker=new DatePickerDialog(ProspekActivity.this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                    int day_, month_, year_;
                    String dayString, monthString;

                    day_ = selectedday;
                    month_ = selectedmonth+1;
                    year_ = selectedyear;

                    if (day_ < 10){dayString = "0"+day_;} else {dayString = String.valueOf(day_);}
                    if (month_ < 10){monthString = "0"+month_;} else {monthString = String.valueOf(month_);}

                    getTglStringAwal = year_+"-"+monthString+"-"+dayString;
                    String getTglStringAwalLokal = dayString+"-"+monthString+"-"+year_;
                    tglAwal.setText(getTglStringAwalLokal);
                },mYear, mMonth, mDay);

                mDatePicker.setTitle("Pilih tgl awal");
                mDatePicker.show();
                break;
            case R.id.tgl_akhir_edt:
                mDatePicker=new DatePickerDialog(ProspekActivity.this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                    int day_, month_, year_;
                    String dayString, monthString;

                    day_ = selectedday;
                    month_ = selectedmonth+1;
                    year_ = selectedyear;

                    if (day_ < 10){dayString = "0"+day_;} else {dayString = String.valueOf(day_);}
                    if (month_ < 10){monthString = "0"+month_;} else {monthString = String.valueOf(month_);}

                    getTglStringAkhir = year_+"-"+monthString+"-"+dayString;
                    String getTglStringAkhirLokal = dayString+"-"+monthString+"-"+year_;
                    tglAkhir.setText(getTglStringAkhirLokal);
                },mYear, mMonth, mDay);

                mDatePicker.setTitle("Pilih tgl akhir");
                mDatePicker.show();
                break;
            case R.id.tgl_pilih_btn:
                if (getTglStringAwal.equals("")){tglAwal.setError("Harus diisi");}
                else if (getTglStringAkhir.equals("")){tglAkhir.setError("Harus diisi");}
                else {
                    getSortByDate();
                    dialog.cancel();
                }
                break;
            case R.id.kategori_pilih_btn:
                    if (searchSpinnerString.equalsIgnoreCase("")){
                        Toast.makeText(this, "Pilih kategori", Toast.LENGTH_LONG).show();
                    } else{
                        getSortByKategori();
                        dialog.cancel();
                    }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProspek();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables!=null){
            disposables.clear();
            disposables.dispose();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String cekSpinner = spinnerSortKategori.getSelectedItem().toString();
        if (cekSpinner.equals("Pilih Kategori:")){
            searchSpinnerString = "";
        } else if (cekSpinner.equals("DO")){
            searchSpinnerString = cekSpinner;
        } else if (cekSpinner.equals("Suspect")){
            searchSpinnerString = cekSpinner;
        } else if (cekSpinner.equals("Hot")){
            searchSpinnerString = cekSpinner;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
