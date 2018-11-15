package com.example.bismillah.newsahabatauto2000.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bismillah.newsahabatauto2000.Activity.UbahProfilActivity;
import com.example.bismillah.newsahabatauto2000.R;
import com.example.bismillah.newsahabatauto2000.SessionManager.SessionManager;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment implements View.OnClickListener{
    Button ubahProfilBtn;
    private TextView namaTv, usernameTv, notelpTv, emailTv;
    private String id_sales, foto_sales;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        sessionManager = new SessionManager(getContext());

        namaTv = view.findViewById(R.id.nama_profil_tv);
        usernameTv = view.findViewById(R.id.username_profil_tv);
        notelpTv = view.findViewById(R.id.telp_profil_tv);
        emailTv = view.findViewById(R.id.email_profil_tv);
        ubahProfilBtn = view.findViewById(R.id.ubah_profil_btn);
        ubahProfilBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ubah_profil_btn:
                Intent ubahProfil = new Intent(getContext(), UbahProfilActivity.class);
                ubahProfil.putExtra("id_sales",id_sales);
                ubahProfil.putExtra("nama_sales",namaTv.getText().toString());
                ubahProfil.putExtra("username_sales",usernameTv.getText().toString());
                ubahProfil.putExtra("telp_sales",notelpTv.getText().toString());
                ubahProfil.putExtra("email_sales",emailTv.getText().toString());
                ubahProfil.putExtra("foto_sales",foto_sales);
                startActivity(ubahProfil);
                break;
        }
    }

    private void getDataFromSharedPreference(){
        HashMap map = sessionManager.getDetailLogin();

        id_sales = (String) map.get(SessionManager.KEY_ID_SALES);
        namaTv.setText((String) map.get(SessionManager.KEY_NAMA_SALES));
        usernameTv.setText((String) map.get(SessionManager.KEY_USERNAME_SALES));
        notelpTv.setText((String) map.get(SessionManager.KEY_TELP_SALES));
        emailTv.setText((String) map.get(SessionManager.KEY_EMAIL_SALES));
        foto_sales = (String) map.get(SessionManager.KEY_FOTO_SALES);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromSharedPreference();
    }
}
