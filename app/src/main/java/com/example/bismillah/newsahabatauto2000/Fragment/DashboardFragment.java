package com.example.bismillah.newsahabatauto2000.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bismillah.newsahabatauto2000.Activity.ProspekActivity;
import com.example.bismillah.newsahabatauto2000.Interface.ProspekAPI;
import com.example.bismillah.newsahabatauto2000.Model.DataProspekMonthly;
import com.example.bismillah.newsahabatauto2000.Model.DataProspekTotally;
import com.example.bismillah.newsahabatauto2000.Model.DataProspekYearly;
import com.example.bismillah.newsahabatauto2000.Model.RootObject;
import com.example.bismillah.newsahabatauto2000.Network.NetworkCLient2;
import com.example.bismillah.newsahabatauto2000.R;
import com.example.bismillah.newsahabatauto2000.SessionManager.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener{
    Button daftarProspekBtn;
    private TextView namaSalesTv, suspectMonthly, hotMonthly, doMonthly, suspectYearly, hotYearly, doYearly, suspectTotally, hotTotally, doTotally;
    private String id_sales;
    private SessionManager sessionManager;
    private ProgressDialog progress;
    private List<DataProspekMonthly> dataProspekMonthlyList = new ArrayList <>();
    private List<DataProspekYearly> dataProspekYearlyList = new ArrayList <>();
    private List<DataProspekTotally> dataProspekTotallyList = new ArrayList <>();
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sessionManager = new SessionManager(getContext());

        namaSalesTv = view.findViewById(R.id.nama_sales_tv);
        suspectMonthly = view.findViewById(R.id.total_cust_suspect_monthly_tv);
        hotMonthly = view.findViewById(R.id.total_cust_hot_monthly_tv);
        doMonthly = view.findViewById(R.id.total_cust_do_monthly_tv);
        suspectYearly = view.findViewById( R.id.total_cust_suspect_yearly_tv);
        hotYearly = view.findViewById(R.id.total_cust_hot_yearly_tv);
        doYearly = view.findViewById(R.id.total_cust_do_yearly_tv);
        suspectTotally = view.findViewById(R.id.total_cust_suspect_tv);
        hotTotally = view.findViewById(R.id.total_cust_hot_tv);
        doTotally = view.findViewById(R.id.total_cust_do_tv);
        daftarProspekBtn = view.findViewById(R.id.daftar_prospek_btn);
        daftarProspekBtn.setOnClickListener(this);

        getDataFromSharedPreference();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.daftar_prospek_btn:
                Intent lihatProspekIntent = new Intent(getActivity(), ProspekActivity.class);
                startActivity(lihatProspekIntent);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void getDataFromSharedPreference(){
        HashMap map = sessionManager.getDetailLogin();
        namaSalesTv.setText("Welcome, "+ map.get(SessionManager.KEY_NAMA_SALES) );
        id_sales = (String) map.get(SessionManager.KEY_ID_SALES);
    }

    private void getDataMonthly(){
        progress = new ProgressDialog(getContext());
        progress.setCancelable(false);
        progress.setMessage("Loading...");
        progress.show();

        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI= retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.getProspekMonthly(id_sales)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith( new DisposableObserver <RootObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(RootObject rootObject) {
                        try{
                            String nilai = rootObject.getValue();
                            if (nilai.equals("1")){
                                dataProspekMonthlyList = rootObject.getResult_monthly();
                                suspectMonthly.setText("Kategori Suspect: "+dataProspekMonthlyList.get(0).getHitung());
                                hotMonthly.setText("Kategori Hot: "+dataProspekMonthlyList.get(1).getHitung());
                                doMonthly.setText("Kategori Deal Order: "+dataProspekMonthlyList.get(2).getHitung());
                                getDataYearly();
                            }
                        } catch (Exception e){
                            progress.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Gagal mengambil data: "+e);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Coba lagi", (dialog, id) -> getDataMonthly());
                            builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Gagal mengambil data, onError: "+e);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Coba lagi", (dialog, id) -> getDataMonthly());
                        builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onComplete() {}
                } ));
    }

    private void getDataYearly(){
        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI= retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.getProspekYearly(id_sales)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith( new DisposableObserver <RootObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(RootObject rootObject) {
                        try{
                            String nilai = rootObject.getValue();
                            if (nilai.equals("1")){
                                dataProspekYearlyList = rootObject.getResult_yearly();
                                suspectYearly.setText("Kategori Suspect: "+dataProspekYearlyList.get(0).getHitung());
                                hotYearly.setText("Kategori Hot: "+dataProspekYearlyList.get(1).getHitung());
                                doYearly.setText("Kategori Deal Order: "+dataProspekYearlyList.get(2).getHitung());
                                getDataTotally();
                            }
                        } catch (Exception e){
                            progress.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Gagal mengambil data: "+e);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Coba lagi", (dialog, id) -> getDataYearly());
                            builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Gagal mengambil data, onError: "+e);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Coba lagi", (dialog, id) -> getDataYearly());
                        builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onComplete() {}
                } ));
    }

    private void getDataTotally(){
        Retrofit retrofit = NetworkCLient2.getRetrofitClient();
        ProspekAPI prospekAPI= retrofit.create(ProspekAPI.class);

        disposables.add(prospekAPI.getProspekTotally(id_sales)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith( new DisposableObserver <RootObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(RootObject rootObject) {
                        progress.dismiss();
                        try{
                            String nilai = rootObject.getValue();
                            if (nilai.equals("1")){
                                dataProspekTotallyList = rootObject.getResult_totally();
                                suspectTotally.setText("Kategori Suspect: "+dataProspekTotallyList.get(0).getHitung());
                                hotTotally.setText("Kategori Hot: "+dataProspekTotallyList.get(1).getHitung());
                                doTotally.setText("Kategori Deal Order: "+dataProspekTotallyList.get(2).getHitung());
                            }
                        }catch (Exception e){
                            progress.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Gagal mengambil data: "+e);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Coba lagi", (dialog, id) -> getDataTotally());
                            builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        progress.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Gagal mengambil data, onError: "+e);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Coba lagi", (dialog, id) -> getDataTotally());
                        builder.setNegativeButton("Batal", (dialog, id) -> dialog.cancel());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    @Override
                    public void onComplete() {
                        progress.dismiss();
                    }
                } ));
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataMonthly();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposables!=null){
            disposables.clear();
            disposables.dispose();
        }
    }
}
