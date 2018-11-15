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
import android.widget.TextView;

import com.example.bismillah.newsahabatauto2000.Interface.DaftarandLoginAPI;
import com.example.bismillah.newsahabatauto2000.Model.DataSales;
import com.example.bismillah.newsahabatauto2000.Model.RootObject;
import com.example.bismillah.newsahabatauto2000.Network.NetworkCLient2;
import com.example.bismillah.newsahabatauto2000.R;
import com.example.bismillah.newsahabatauto2000.SessionManager.SessionManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    android.support.v7.widget.Toolbar toolbar;
    private List<DataSales> dataSales = new ArrayList<>();
    private EditText usernameEdt, passEdt;
    private TextView loginSalesTv, loginSpvTv;
    private Button loginSalesBtn, loginSpvBtn, daftarSalesBtn;
    private ProgressDialog progress;
    private CompositeDisposable disposables = new CompositeDisposable();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        passToMenuActivity();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);

        usernameEdt = findViewById(R.id.username_edt);
        passEdt = findViewById(R.id.pass_edt);
        loginSalesTv = findViewById(R.id.login_sales_tv);
        loginSpvTv = findViewById(R.id.login_spv_tv);
        loginSalesBtn = findViewById(R.id.login_sales_btn);
        loginSpvBtn = findViewById(R.id.login_spv_btn);
        daftarSalesBtn = findViewById(R.id.daftar_btn);

        loginSalesTv.setOnClickListener(this);
        loginSpvTv.setOnClickListener(this);
        daftarSalesBtn.setOnClickListener(this);
        loginSalesBtn.setOnClickListener(this);
    }

    private void login(){
        String username_sales = usernameEdt.getText().toString();
        String pass_sales = passEdt.getText().toString();

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Login...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        DaftarandLoginAPI daftarandLoginAPI = retrofit.create(DaftarandLoginAPI.class);

        disposables.add(daftarandLoginAPI.loginValidation(username_sales, pass_sales)
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(pesan);
                                builder.setCancelable(false);
                                builder.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else if (nilai.equals("1")){
                                dataSales = rootObject.getResult_login();
                                sessionManager.storeLogin(dataSales.get(0).getId_sales(), dataSales.get(0).getNama_sales(),
                                        dataSales.get(0).getUsername_sales(), dataSales.get(0).getTelp_sales(),
                                        dataSales.get(0).getEmail_sales(), dataSales.get(0).getFoto_sales());
                                LoginActivity.this.finish();
                                Intent loginIntent = new Intent(LoginActivity.this, MenuActivity.class);
                                startActivity(loginIntent);
                            }
                        } catch (Exception e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("Gagal login: "+e);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Coba lagi", (dialog, id) -> login());
                            builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Gagal login, onError: "+e);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Coba lagi", (dialog, id) -> login());
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

    private void passToMenuActivity(){
        if(sessionManager.isLoggedIn()){
            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            this.finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_sales_btn:
                if (usernameEdt.getText().toString().trim().equals("")){usernameEdt.setError("Harus diisi");}
                else if (passEdt.getText().toString().trim().equals("")){passEdt.setError("Harus diisi");}
                else{
                    login();
                }
                break;
            case R.id.login_spv_btn:

                break;
            case R.id.daftar_btn:
                Intent daftarSales = new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(daftarSales);
                break;
            case R.id.login_sales_tv:
                loginSpvBtn.setVisibility(View.GONE);
                loginSalesBtn.setVisibility(View.VISIBLE);
                loginSalesTv.setVisibility(View.GONE);
                loginSpvTv.setVisibility(View.VISIBLE);
                daftarSalesBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.login_spv_tv:
                loginSpvBtn.setVisibility(View.VISIBLE);
                loginSalesBtn.setVisibility(View.GONE);
                loginSalesTv.setVisibility(View.VISIBLE);
                loginSpvTv.setVisibility(View.GONE);
                daftarSalesBtn.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Ingin keluar aplikasi?");
        builder.setCancelable(true);
        builder.setPositiveButton("Ya", (dialog, id) -> LoginActivity.this.finish());
        builder.setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Ingin keluar aplikasi?");
                builder.setCancelable(true);
                builder.setPositiveButton("Ya", (dialog, id) -> LoginActivity.this.finish());
                builder.setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
