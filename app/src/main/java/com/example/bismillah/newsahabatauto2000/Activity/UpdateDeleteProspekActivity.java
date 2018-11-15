package com.example.bismillah.newsahabatauto2000.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bismillah.newsahabatauto2000.Interface.ProspekAPI;
import com.example.bismillah.newsahabatauto2000.Model.RootObject;
import com.example.bismillah.newsahabatauto2000.Network.NetworkCLient2;
import com.example.bismillah.newsahabatauto2000.R;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class UpdateDeleteProspekActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    android.support.v7.widget.Toolbar toolbar;
    String[] kategoriList = {"Pilih Kategori:","Suspect","Hot","DO"};
    private EditText namaEdt, jenisMobilEdt, warnaMobilEdt, alamatEdt, telpEdt, emailEdt, tglCustEdt, agamaEdt, tglLahirEdt;
    private Spinner spinnerKategori;
    private TextView tglLahirTvGone;
    private TextInputLayout layoutAgama, layoutTglLahir;
    private String id_cust, hasilSpinnerString, getTglString;
    private ProgressDialog progress;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private int mYear, mMonth, mDay;
    DatePickerDialog mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_prospek);

        Calendar mcurrentDate = Calendar.getInstance();
        mYear= mcurrentDate.get(Calendar.YEAR);
        mMonth= mcurrentDate.get(Calendar.MONTH);
        mDay= mcurrentDate.get(Calendar.DAY_OF_MONTH);

        spinnerKategori = findViewById(R.id.kategori_cust_spinner);
        RadioGroup radioSexGroup = findViewById(R.id.sex_radioGroup);
        RadioButton maleBtn = findViewById(R.id.pria_cust_rb);
        RadioButton femaleBtn = findViewById(R.id.wnt_cust_rb);
        namaEdt = findViewById(R.id.nama_cust_edt);
        jenisMobilEdt = findViewById(R.id.jenis_mobil_cust_edt);
        warnaMobilEdt = findViewById(R.id.warna_mobil_cust_edt);
        alamatEdt = findViewById(R.id.alamat_cust_edt);
        telpEdt = findViewById(R.id.telp_cust_edt);
        emailEdt = findViewById(R.id.email_cust_edt);
        tglCustEdt = findViewById(R.id.tgl_cust_edt);
        agamaEdt = findViewById(R.id.agama_cust_edt);
        tglLahirEdt = findViewById(R.id.tgl_lahir_cust_edt);
        layoutAgama = findViewById(R.id.agama_cust_til);
        layoutTglLahir = findViewById(R.id.tgl_lahir_cust_til);
        tglLahirTvGone = findViewById(R.id.tgl_lahir_cust_tv_gone);
        TextView jk = findViewById(R.id.jenis_kelamin_tv);
        Button simpanCustBtn = findViewById(R.id.simpan_cust_btn);
        Button updateCustBtn = findViewById(R.id.update_cust_btn);
        Button delCustBtn = findViewById(R.id.del_cust_btn);

        simpanCustBtn.setVisibility(View.GONE);
        tglCustEdt.setVisibility(View.VISIBLE);
        radioSexGroup.setVisibility(View.GONE);
        maleBtn.setVisibility(View.GONE);
        femaleBtn.setVisibility(View.GONE);
        jk.setVisibility(View.GONE);
        tglLahirEdt.setFocusable(false);
        tglLahirEdt.setClickable(true);
        tglLahirEdt.setLongClickable(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategoriList);
        spinnerKategori.setAdapter(adapter);

        updateCustBtn.setOnClickListener(this);
        delCustBtn.setOnClickListener(this);
        tglLahirEdt.setOnClickListener(this);
        spinnerKategori.setOnItemSelectedListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rincian Data Prospek");

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);

        getDataIntentFromProspekAdapter();
    }

    @SuppressLint("SetTextI18n")
    private void getDataIntentFromProspekAdapter(){
        Intent i = getIntent();

        id_cust = i.getStringExtra("idCust");

        hasilSpinnerString = i.getStringExtra("kategoriCust");
        switch (hasilSpinnerString) {
            case "Suspect":
                spinnerKategori.setSelection(1);
                layoutAgama.setVisibility(View.GONE);
                layoutTglLahir.setVisibility(View.GONE);
                break;
            case "Hot":
                spinnerKategori.setSelection(2);
                layoutAgama.setVisibility(View.GONE);
                layoutTglLahir.setVisibility(View.GONE);
                break;
            case "DO":
                spinnerKategori.setSelection(3);
                layoutAgama.setVisibility(View.VISIBLE);
                layoutTglLahir.setVisibility(View.VISIBLE);
                break;
        }

        namaEdt.setText(i.getStringExtra("namaCust"));
        jenisMobilEdt.setText(i.getStringExtra("jenisMobilCust"));
        warnaMobilEdt.setText(i.getStringExtra("warnaMobilCust"));
        alamatEdt.setText(i.getStringExtra("alamatCust"));
        telpEdt.setText(i.getStringExtra("telpCust"));
        emailEdt.setText(i.getStringExtra("emailCust"));
        tglCustEdt.setText(i.getStringExtra("tglCust"));
        agamaEdt.setText(i.getStringExtra("agamaCust"));
        tglLahirTvGone.setText(i.getStringExtra("tglLahirCust"));

        String currentDate = i.getStringExtra("tglLahirCust");
        String[] pisahDate = currentDate.split("-");
        tglLahirEdt.setText(pisahDate[2]+"-"+pisahDate[1]+"-"+pisahDate[0]);

    }

    private void updateProspek(){
        String namaCust = namaEdt.getText().toString();
        String jenisMobilCust = jenisMobilEdt.getText().toString();
        String warnaMobilCust = warnaMobilEdt.getText().toString();
        String alamatCust = alamatEdt.getText().toString();
        String telpCust = telpEdt.getText().toString();
        String emailCust = emailEdt.getText().toString();
        String agamaCust = agamaEdt.getText().toString();
        String tglLahirCust = tglLahirTvGone.getText().toString();

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Perbarui data prospek...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI = retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.updateProspek(id_cust,namaCust,hasilSpinnerString,jenisMobilCust,warnaMobilCust,alamatCust,
                telpCust,emailCust,agamaCust,tglLahirCust)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RootObject>() {
                    @Override
                    public void onNext(RootObject rootObject) {
                        progress.dismiss();
                        try {
                            String nilai = rootObject.getValue();
                            String pesan = rootObject.getMessage();
                            if (nilai.equals("1")){
                                AlertDialog.Builder builderCase1 = new AlertDialog.Builder(UpdateDeleteProspekActivity.this);
                                builderCase1.setMessage(pesan);
                                builderCase1.setCancelable(false);
                                builderCase1.setPositiveButton("Ok", (dialogInterface, i) -> UpdateDeleteProspekActivity.this.finish());
                                AlertDialog alertCase1 = builderCase1.create();
                                alertCase1.show();
                            } else {
                                AlertDialog.Builder builderCase0 = new AlertDialog.Builder(UpdateDeleteProspekActivity.this);
                                builderCase0.setMessage(pesan);
                                builderCase0.setCancelable(false);
                                builderCase0.setPositiveButton("Coba lagi", (dialog, id) -> updateProspek());
                                builderCase0.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                                AlertDialog alertCase0 = builderCase0.create();
                                alertCase0.show();
                            }
                        }catch (Exception e){
                            AlertDialog.Builder builderCase0 = new AlertDialog.Builder(UpdateDeleteProspekActivity.this);
                            builderCase0.setMessage("Gagal update prospek: "+e);
                            builderCase0.setCancelable(false);
                            builderCase0.setPositiveButton("Coba lagi", (dialog, id) -> updateProspek());
                            builderCase0.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                            AlertDialog alertCase0 = builderCase0.create();
                            alertCase0.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();
                        AlertDialog.Builder builderCase0 = new AlertDialog.Builder(UpdateDeleteProspekActivity.this);
                        builderCase0.setMessage("Gagal update prospek, onError: "+e);
                        builderCase0.setCancelable(false);
                        builderCase0.setPositiveButton("Coba lagi", (dialog, id) -> updateProspek());
                        builderCase0.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                        AlertDialog alertCase0 = builderCase0.create();
                        alertCase0.show();
                    }

                    @Override
                    public void onComplete() {progress.dismiss();}
                }));
    }

    private void deleteProspek(){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Menghapus data prospek...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI = retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.deleteProspek(id_cust)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RootObject>() {
                    @Override
                    public void onNext(RootObject rootObject) {
                        progress.dismiss();
                        try{
                            String nilai = rootObject.getValue();
                            String pesan = rootObject.getMessage();
                            if (nilai.equals("1")){
                                AlertDialog.Builder builderCase1 = new AlertDialog.Builder(UpdateDeleteProspekActivity.this);
                                builderCase1.setMessage(pesan);
                                builderCase1.setCancelable(false);
                                builderCase1.setPositiveButton("Ok", (dialogInterface, i) -> UpdateDeleteProspekActivity.this.finish());
                                AlertDialog alertCase1 = builderCase1.create();
                                alertCase1.show();
                            } else {
                                AlertDialog.Builder builderCase0 = new AlertDialog.Builder(UpdateDeleteProspekActivity.this);
                                builderCase0.setMessage(pesan);
                                builderCase0.setCancelable(false);
                                builderCase0.setPositiveButton("Coba lagi", (dialog, id) -> deleteProspek());
                                builderCase0.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                                AlertDialog alertCase0 = builderCase0.create();
                                alertCase0.show();
                            }
                        } catch (Exception e){
                            AlertDialog.Builder builderCase0 = new AlertDialog.Builder(UpdateDeleteProspekActivity.this);
                            builderCase0.setMessage("Gagal hapus prospek: "+e);
                            builderCase0.setCancelable(false);
                            builderCase0.setPositiveButton("Coba lagi", (dialog, id) -> deleteProspek());
                            builderCase0.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                            AlertDialog alertCase0 = builderCase0.create();
                            alertCase0.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();
                        AlertDialog.Builder builderCase0 = new AlertDialog.Builder(UpdateDeleteProspekActivity.this);
                        builderCase0.setMessage("Gagal hapus prospek, onError: "+e);
                        builderCase0.setCancelable(false);
                        builderCase0.setPositiveButton("Coba lagi", (dialog, id) -> deleteProspek());
                        builderCase0.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                        AlertDialog alertCase0 = builderCase0.create();
                        alertCase0.show();
                    }

                    @Override
                    public void onComplete() {progress.dismiss();}
                }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update_cust_btn:
                if (namaEdt.getText().toString().equals("")){namaEdt.setError("Harus diisi");}
                else if (hasilSpinnerString.equals("")){Toast.makeText(this, "Pilih kategori", Toast.LENGTH_LONG).show();}
                else if (jenisMobilEdt.getText().toString().equals("")){jenisMobilEdt.setError("Harus diisi");}
                else if (warnaMobilEdt.getText().toString().equals("")){warnaMobilEdt.setError("Harus diisi");}
                else if (alamatEdt.getText().toString().equals("")){alamatEdt.setError("Harus diisi");}
                else if (telpEdt.getText().toString().equals("")){telpEdt.setError("Harus diisi");}
                else if (hasilSpinnerString.equals("DO") && agamaEdt.getText().toString().equals("")){agamaEdt.setError("Harus diisi");}
                else if (hasilSpinnerString.equals("DO") && tglLahirEdt.getText().toString().equals("")){tglLahirEdt.setError("Harus diisi");}
                else {
                    updateProspek();
                }
                break;
            case R.id.tgl_lahir_cust_edt:
                mDatePicker=new DatePickerDialog(UpdateDeleteProspekActivity.this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                    int day_, month_, year_;
                    String dayString, monthString;

                    day_ = selectedday;
                    month_ = selectedmonth+1;
                    year_ = selectedyear;

                    if (day_ < 10){dayString = "0"+day_;} else {dayString = String.valueOf(day_);}
                    if (month_ < 10){monthString = "0"+month_;} else {monthString = String.valueOf(month_);}

                    getTglString = year_+"-"+monthString+"-"+dayString;
                    String getTglStringLokal = dayString+"-"+monthString+"-"+year_;
                    tglLahirEdt.setText(getTglStringLokal);
                    tglLahirTvGone.setText(getTglString);
                },mYear, mMonth, mDay);

                mDatePicker.setTitle("Pilih tgl lahir");
                mDatePicker.show();
                break;
            case R.id.del_cust_btn:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Peringatan");
                alertDialogBuilder
                        .setMessage("Ingin melanjutkan hapus semua data gejala?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus", (dialogInterface, i) -> deleteProspek())
                        .setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.cancel());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String cekSpiner = spinnerKategori.getSelectedItem().toString();
        switch (cekSpiner) {
            case "Pilih Kategori":
                layoutAgama.setVisibility(View.GONE);
                layoutTglLahir.setVisibility(View.GONE);
                hasilSpinnerString = "";
                agamaEdt.setText("");
                tglLahirEdt.setText("");
                break;
            case "DO":
                layoutAgama.setVisibility(View.VISIBLE);
                layoutTglLahir.setVisibility(View.VISIBLE);
                hasilSpinnerString = cekSpiner;
                break;
            default:
                layoutAgama.setVisibility(View.GONE);
                layoutTglLahir.setVisibility(View.GONE);
                hasilSpinnerString = cekSpiner;
                agamaEdt.setText("");
                tglLahirEdt.setText("");
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables!=null){
            disposables.clear();
            disposables.dispose();
        }
    }
}
