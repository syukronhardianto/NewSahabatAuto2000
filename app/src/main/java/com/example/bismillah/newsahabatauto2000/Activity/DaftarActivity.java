package com.example.bismillah.newsahabatauto2000.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bismillah.newsahabatauto2000.Adapter.IdNamaSpvAdapter;
import com.example.bismillah.newsahabatauto2000.Adapter.NothingSelectedSpinnerAdapter;
import com.example.bismillah.newsahabatauto2000.Interface.DaftarandLoginAPI;
import com.example.bismillah.newsahabatauto2000.Model.IdNamaSpv;
import com.example.bismillah.newsahabatauto2000.Model.RootObject;
import com.example.bismillah.newsahabatauto2000.Network.NetworkCLient2;
import com.example.bismillah.newsahabatauto2000.R;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class DaftarActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    android.support.v7.widget.Toolbar toolbar;
    private ProgressDialog progress;
    private List<IdNamaSpv> idNamaSpvList = new ArrayList<>();
    private IdNamaSpvAdapter idNamaSpvAdapter;
    private EditText namaEdt, usernameEdt, passwordEdt, noTelpEdt, emailEdt;
    private Spinner idNamaSpvSpinner;
    private TextView warningPassMinChar;
    Button daftarBtn;
    private String id_spv;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        namaEdt = findViewById(R.id.nama_sales_edt);
        usernameEdt = findViewById(R.id.username_sales_edt);
        passwordEdt = findViewById(R.id.pass_sales_edt);
        noTelpEdt = findViewById(R.id.telp_sales_edt);
        emailEdt = findViewById(R.id.email_sales_edt);
        idNamaSpvSpinner = findViewById(R.id.id_spv_spinner);
        warningPassMinChar = findViewById(R.id.warning_pass_min_char);
        daftarBtn = findViewById(R.id.daftar_sales_btn);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar");

        warningPassMinChar.setVisibility(View.INVISIBLE);

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);

        checkMinPasswordChar();
        getIdNamaSpv();

        idNamaSpvSpinner.setOnItemSelectedListener(this);
        daftarBtn.setOnClickListener(this);
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

    private void getIdNamaSpv(){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Mengambil data SPV...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        DaftarandLoginAPI daftarandLoginAPI = retrofit.create(DaftarandLoginAPI.class);

        disposables.add(daftarandLoginAPI.getIdNamaSpv()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<RootObject>() {
            @Override
            public void onNext(RootObject rootObject) {
                progress.dismiss();
                try{
                    String nilai = rootObject.getValue();
                    if (nilai.equals("1")){
                        idNamaSpvList = rootObject.getResult_idnamaspv();
                        idNamaSpvAdapter = new IdNamaSpvAdapter(DaftarActivity.this, R.layout.custom_spinner_namaspv, idNamaSpvList);
                        idNamaSpvSpinner.setAdapter(
                                new NothingSelectedSpinnerAdapter(
                                        idNamaSpvAdapter, R.layout.spinner_nothing_selected_layout_idnamaspv,
                                        getApplicationContext()));
                    }
                } catch (Exception e){
                        AlertDialog.Builder builder = new AlertDialog.Builder(DaftarActivity.this);
                        builder.setMessage("Gagal ambil data spv: "+e);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Coba lagi", (dialog, id) -> getIdNamaSpv());
                        builder.setNegativeButton("Batal Daftar", (dialog, id) -> {
                            dialog.cancel();
                            DaftarActivity.this.finish();
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                }
            }

            @Override
            public void onError(Throwable e) {
                progress.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(DaftarActivity.this);
                builder.setMessage("Gagal ambil data spv, onError: "+e);
                builder.setCancelable(false);
                builder.setPositiveButton("Coba lagi", (dialog, id) -> getIdNamaSpv());
                builder.setNegativeButton("Batal Daftar", (dialog, id) -> {
                    dialog.cancel();
                    DaftarActivity.this.finish();
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

            @Override
            public void onComplete() {
                progress.dismiss();}
        }));
    }

    private void checkMinPasswordChar(){
        Observable<Boolean> passwordStream = RxTextView.textChanges(passwordEdt)
                .map(charSequence -> !TextUtils.isEmpty(charSequence)
                        && charSequence.toString().trim().length() < 6);
        Observer<Boolean> passwordObserver = new Observer<Boolean>(){

            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Boolean aBoolean) {
                showPasswordMinimalAlert(aBoolean);
            }

            @Override
            public void onError(Throwable e) {}
            @Override
            public void onComplete() {}
        };

        passwordStream.subscribe(passwordObserver);
    }

    public void showPasswordMinimalAlert(boolean value){
        if(value) {
            warningPassMinChar.setVisibility(View.VISIBLE);
        } else {
            warningPassMinChar.setVisibility(View.INVISIBLE);
        }
    }

    private void simpanSales(){
        String nama_sales = namaEdt.getText().toString();
        String username_sales = usernameEdt.getText().toString();
        String pass_sales = passwordEdt.getText().toString();
        String telp_sales = noTelpEdt.getText().toString();
        String email_sales = emailEdt.getText().toString();

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Menyimpan data sales...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        DaftarandLoginAPI daftarandLoginAPI = retrofit.create(DaftarandLoginAPI.class);

        disposables.add(daftarandLoginAPI.saveDataSales(nama_sales, username_sales, pass_sales, telp_sales, email_sales, id_spv)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RootObject>() {
                    @Override
                    public void onNext(RootObject rootObject) {
                        progress.dismiss();
                        try{
                            String nilai = rootObject.getValue();
                            String pesan = rootObject.getMessage();

                            switch (nilai) {
                                case "0":
                                    AlertDialog.Builder builderCase0 = new AlertDialog.Builder(DaftarActivity.this);
                                    builderCase0.setMessage(pesan);
                                    builderCase0.setCancelable(false);
                                    builderCase0.setPositiveButton("Oke", (dialog, id) -> dialog.cancel());
                                    AlertDialog alertCase0 = builderCase0.create();
                                    alertCase0.show();
                                    break;
                                case "1": {
                                    AlertDialog.Builder builderCase1 = new AlertDialog.Builder(DaftarActivity.this);
                                    builderCase1.setMessage(pesan);
                                    builderCase1.setCancelable(false);
                                    builderCase1.setPositiveButton("Oke", (dialog, id) -> DaftarActivity.this.finish());
                                    AlertDialog alertCase1 = builderCase1.create();
                                    alertCase1.show();
                                    break;
                                }
                                case "2": {
                                    AlertDialog.Builder builderCase2 = new AlertDialog.Builder(DaftarActivity.this);
                                    builderCase2.setMessage(pesan);
                                    builderCase2.setCancelable(false);
                                    builderCase2.setPositiveButton("Coba lagi", (dialog, id) -> simpanSales());
                                    builderCase2.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                                    AlertDialog alertCase2 = builderCase2.create();
                                    alertCase2.show();
                                    break;
                                }
                            }

                        } catch (Exception e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(DaftarActivity.this);
                            builder.setMessage("Gagal menyimpan: "+e);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Coba lagi", (dialog, id) -> simpanSales());
                            builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(DaftarActivity.this);
                        builder.setMessage("Gagal menyimpan, onError: "+e);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Coba lagi", (dialog, id) -> simpanSales());
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.daftar_sales_btn:
                if (namaEdt.getText().toString().trim().equals("")){namaEdt.setError("Harus diisi");}
                else if (usernameEdt.getText().toString().trim().equals("")){usernameEdt.setError("Harus diisi");}
                else if (passwordEdt.getText().toString().trim().equals("")){passwordEdt.setError("Harus diisi");}
                else if (noTelpEdt.getText().toString().trim().equals("")){noTelpEdt.setError("Harus diisi");}
                else if (emailEdt.getText().toString().trim().equals("")){emailEdt.setError("Harus diisi");}
                else if (id_spv.equalsIgnoreCase("")){Toast.makeText(this, "Harus pilih SPV", Toast.LENGTH_SHORT).show();}
                else{
                    if (warningPassMinChar.isShown()){
                        Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show();
                    } else {
                        simpanSales();
                    }
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i!=0){
            id_spv = ((TextView)view.findViewById(R.id.id_spv_tv)).getText().toString();
        } else {
            id_spv = "";
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
