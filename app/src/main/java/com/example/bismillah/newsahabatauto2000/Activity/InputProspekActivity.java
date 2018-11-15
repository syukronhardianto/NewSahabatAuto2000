package com.example.bismillah.newsahabatauto2000.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bismillah.newsahabatauto2000.Interface.ProspekAPI;
import com.example.bismillah.newsahabatauto2000.Model.RootObject;
import com.example.bismillah.newsahabatauto2000.Network.NetworkCLient2;
import com.example.bismillah.newsahabatauto2000.R;
import com.example.bismillah.newsahabatauto2000.SessionManager.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class InputProspekActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener{
    android.support.v7.widget.Toolbar toolbar;
    String[] kategoriList = {"Pilih Kategori:","Suspect","Hot","DO"};
    private Spinner spinnerKategori;
    private String hasilSpinnerString, jenisKelString, id_sales, getTglString;
    private RadioGroup radioSexGroup;
    private RadioButton maleBtn, femaleBtn;
    private EditText namaEdt, jenisMobilEdt, warnaMobilEdt, alamatEdt, telpEdt, emailEdt, agamaEdt, tglLahirEdt;
    private TextInputLayout layoutAgama, layoutTglLahir;
    private ProgressDialog progress;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private SessionManager sessionManager;
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

        sessionManager = new SessionManager(this);

        spinnerKategori = findViewById(R.id.kategori_cust_spinner);
        radioSexGroup = findViewById(R.id.sex_radioGroup);
        maleBtn = findViewById(R.id.pria_cust_rb);
        femaleBtn = findViewById(R.id.wnt_cust_rb);
        namaEdt = findViewById(R.id.nama_cust_edt);
        jenisMobilEdt = findViewById(R.id.jenis_mobil_cust_edt);
        warnaMobilEdt = findViewById(R.id.warna_mobil_cust_edt);
        alamatEdt = findViewById(R.id.alamat_cust_edt);
        telpEdt = findViewById(R.id.telp_cust_edt);
        emailEdt = findViewById(R.id.email_cust_edt);
        agamaEdt = findViewById(R.id.agama_cust_edt);
        tglLahirEdt = findViewById(R.id.tgl_lahir_cust_edt);
        layoutAgama = findViewById(R.id.agama_cust_til);
        layoutTglLahir = findViewById(R.id.tgl_lahir_cust_til);
        Button simpanCustBtn = findViewById(R.id.simpan_cust_btn);
        Button updateCustBtn = findViewById(R.id.update_cust_btn);
        Button delCustBtn = findViewById(R.id.del_cust_btn);

        updateCustBtn.setVisibility(View.GONE);
        delCustBtn.setVisibility(View.GONE);
        tglLahirEdt.setFocusable(false);
        tglLahirEdt.setClickable(true);
        tglLahirEdt.setLongClickable(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, kategoriList);
        spinnerKategori.setAdapter(adapter);

        maleBtn.setOnClickListener(this);
        femaleBtn.setOnClickListener(this);
        simpanCustBtn.setOnClickListener(this);
        tglLahirEdt.setOnClickListener(this);
        spinnerKategori.setOnItemSelectedListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Input Prospek");

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);

        getIdSales();
    }

    private void getIdSales(){
        HashMap map = sessionManager.getDetailLogin();
        id_sales = (String) map.get(SessionManager.KEY_ID_SALES);
    }

    private void simpanProspek(){
        String namaCust = namaEdt.getText().toString();
        String jenisMobilCust = jenisMobilEdt.getText().toString();
        String warnaMobilCust = warnaMobilEdt.getText().toString();
        String alamatCust = alamatEdt.getText().toString();
        String telpCust = telpEdt.getText().toString();
        String emailCust = emailEdt.getText().toString();
        String agamaCust = agamaEdt.getText().toString();

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Menyimpan data prospek...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI = retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.inputProspek(namaCust, hasilSpinnerString, jenisKelString, jenisMobilCust, warnaMobilCust,
                alamatCust, telpCust, emailCust, agamaCust, getTglString, id_sales)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RootObject>() {
                    @Override
                    public void onNext(RootObject rootObject) {
                        progress.dismiss();
                        try{
                            String nilai = rootObject.getValue();
                            String pesan = rootObject.getMessage();
                            switch (nilai){
                                case "0":
                                    AlertDialog.Builder builderCase0 = new AlertDialog.Builder(InputProspekActivity.this);
                                    builderCase0.setMessage(pesan);
                                    builderCase0.setCancelable(false);
                                    builderCase0.setPositiveButton("Coba lagi", (dialog, id) -> simpanProspek());
                                    builderCase0.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                                    AlertDialog alertCase0 = builderCase0.create();
                                    alertCase0.show();
                                    break;
                                case "1":
                                    AlertDialog.Builder builderCase1 = new AlertDialog.Builder(InputProspekActivity.this);
                                    builderCase1.setMessage(pesan);
                                    builderCase1.setCancelable(false);
                                    builderCase1.setPositiveButton("Ok", (dialogInterface, i) -> InputProspekActivity.this.finish());
                                    AlertDialog alertCase1 = builderCase1.create();
                                    alertCase1.show();
                                    break;
                            }
                        } catch (Exception e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(InputProspekActivity.this);
                            builder.setMessage("Gagal menyimpan prospek: "+e);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Coba lagi", (dialog, id) -> simpanProspek());
                            builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(InputProspekActivity.this);
                        builder.setMessage("Gagal menyimpan prospek, onError: "+e);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Coba lagi", (dialog, id) -> simpanProspek());
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String cekSpiner = spinnerKategori.getSelectedItem().toString();
        if (cekSpiner.equals("Pilih Kategori:")){
            layoutAgama.setVisibility(View.GONE);
            layoutTglLahir.setVisibility(View.GONE);
            hasilSpinnerString = "";
            agamaEdt.setText("");
            tglLahirEdt.setText("");
        } else if (cekSpiner.equals("DO")){
            layoutAgama.setVisibility(View.VISIBLE);
            layoutTglLahir.setVisibility(View.VISIBLE);
            hasilSpinnerString = cekSpiner;
        } else if (cekSpiner.equals("Suspect")){
            layoutAgama.setVisibility(View.GONE);
            layoutTglLahir.setVisibility(View.GONE);
            hasilSpinnerString = cekSpiner;
            agamaEdt.setText("");
            tglLahirEdt.setText("");
        } else if (cekSpiner.equals("Hot")){
            layoutAgama.setVisibility(View.GONE);
            layoutTglLahir.setVisibility(View.GONE);
            hasilSpinnerString = cekSpiner;
            agamaEdt.setText("");
            tglLahirEdt.setText("");
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public void onClick(View view) {
        int id = radioSexGroup.getCheckedRadioButtonId();
        switch (id){
            case R.id.pria_cust_rb:
                jenisKelString = maleBtn.getText().toString();
                break;
            case R.id.wnt_cust_rb:
                jenisKelString = femaleBtn.getText().toString();
                break;
        }
        switch (view.getId()){
            case R.id.simpan_cust_btn:
                if (namaEdt.getText().toString().equals("")){namaEdt.setError("Harus diisi");}
                else if (hasilSpinnerString.equalsIgnoreCase("")){Toast.makeText(this, "Pilih kategori", Toast.LENGTH_LONG).show();}
                else if (jenisKelString.equals("")){Toast.makeText(this, "Pilih jenis kelamin", Toast.LENGTH_LONG).show();}
                else if (jenisMobilEdt.getText().toString().equals("")){jenisMobilEdt.setError("Harus diisi");}
                else if (warnaMobilEdt.getText().toString().equals("")){warnaMobilEdt.setError("Harus diisi");}
                else if (alamatEdt.getText().toString().equals("")){alamatEdt.setError("Harus diisi");}
                else if (telpEdt.getText().toString().equals("")){telpEdt.setError("Harus diisi");}
                else if (hasilSpinnerString.equals("DO") && agamaEdt.getText().toString().equals("")){agamaEdt.setError("Harus diisi");}
                else if (hasilSpinnerString.equals("DO") && tglLahirEdt.getText().toString().equals("")){tglLahirEdt.setError("Harus diisi");}
                else {
                    simpanProspek();
                }
                break;
            case R.id.tgl_lahir_cust_edt:
                mDatePicker=new DatePickerDialog(InputProspekActivity.this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
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
                },mYear, mMonth, mDay);

                mDatePicker.setTitle("Pilih tgl lahir");
                mDatePicker.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposables!=null){
            disposables.clear();
            disposables.dispose();
        }
    }
}
