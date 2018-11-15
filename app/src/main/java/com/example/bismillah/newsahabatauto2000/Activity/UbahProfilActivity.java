package com.example.bismillah.newsahabatauto2000.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bismillah.newsahabatauto2000.Interface.SalesAPI;
import com.example.bismillah.newsahabatauto2000.Model.RootObject;
import com.example.bismillah.newsahabatauto2000.Network.NetworkCLient2;
import com.example.bismillah.newsahabatauto2000.R;
import com.example.bismillah.newsahabatauto2000.SessionManager.SessionManager;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class UbahProfilActivity extends AppCompatActivity implements View.OnClickListener {
    android.support.v7.widget.Toolbar toolbar;
    private String id_sales, foto_sales;
    private EditText namaEdt, usernameEdt, notelpEdt, emailEdt;
    private ProgressDialog progress;
    private CompositeDisposable disposables = new CompositeDisposable();
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);

        sessionManager = new SessionManager(this);

        namaEdt = findViewById(R.id.nama_edit_edt);
        usernameEdt = findViewById(R.id.username_edit_edt);
        notelpEdt = findViewById(R.id.telp_edit_edt);
        emailEdt = findViewById(R.id.email_edit_edt);
        Button ubahBtn = findViewById(R.id.ubah_edit_btn);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull( getSupportActionBar() ).setTitle("Ubah Profil");

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);

        getIntentDataFromFragmentProfil();
        ubahBtn.setOnClickListener(this);
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

    private void getIntentDataFromFragmentProfil(){
        try{
            Intent intent = getIntent();
            id_sales = intent.getStringExtra("id_sales");
            namaEdt.setText(intent.getStringExtra("nama_sales"));
            usernameEdt.setText(intent.getStringExtra("username_sales"));
            notelpEdt.setText(intent.getStringExtra("telp_sales"));
            emailEdt.setText(intent.getStringExtra("email_sales"));
            foto_sales = intent.getStringExtra("foto_sales");
        } catch (Exception e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ubah_edit_btn:
                String nama_sales = namaEdt.getText().toString();
                String username_sales = usernameEdt.getText().toString();
                String telp_sales = notelpEdt.getText().toString();
                String email_sales = emailEdt.getText().toString();

                progress = new ProgressDialog(this);
                progress.setCancelable(false);
                progress.setMessage("Update profil sales...");
                progress.show();

                Retrofit retrofit = NetworkCLient2.getRetrofitClient();
                SalesAPI salesAPI = retrofit.create(SalesAPI.class);

                disposables.add(salesAPI.ubahSales(id_sales,nama_sales,username_sales,telp_sales,email_sales)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith( new DisposableObserver <RootObject>() {
                            @Override
                            public void onNext(RootObject rootObject) {
                                progress.dismiss();
                                try{
                                    String nilai = rootObject.getValue();
                                    String pesan = rootObject.getMessage();
                                    if (nilai.equals("1")){
                                        sessionManager.storeLogin(id_sales,nama_sales,username_sales,telp_sales,email_sales,foto_sales);

//                                        AlertDialog.Builder builderCase1 = new AlertDialog.Builder(UbahProfilActivity.this);
//                                        builderCase1.setMessage(pesan);
//                                        builderCase1.setCancelable(false);
//                                        builderCase1.setPositiveButton("Ok", (dialogInterface, i) -> UbahProfilActivity.this.finish());
//                                        AlertDialog alertCase1 = builderCase1.create();
//                                        alertCase1.show();
                                    }
                                } catch (Exception e){

                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        } ));
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
