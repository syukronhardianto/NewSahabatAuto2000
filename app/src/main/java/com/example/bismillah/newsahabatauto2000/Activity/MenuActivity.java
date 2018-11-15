package com.example.bismillah.newsahabatauto2000.Activity;

import android.app.AlertDialog;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bismillah.newsahabatauto2000.Fragment.DashboardFragment;
import com.example.bismillah.newsahabatauto2000.Fragment.ProfilFragment;
import com.example.bismillah.newsahabatauto2000.Fragment.ReportFragment;
import com.example.bismillah.newsahabatauto2000.R;
import com.example.bismillah.newsahabatauto2000.SessionManager.SessionManager;

import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    android.support.v7.widget.Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sessionManager = new SessionManager(this);

        //Toolbar & Drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //Fragment inizialitation & Set home fragment as first
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new DashboardFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Dashboard");

        //Navigation View
        navigationView = findViewById(R.id.navigation_view);

        View headerView = navigationView.getHeaderView(0);
        TextView headerText = headerView.findViewById(R.id.nama_header_tv);
        ImageView headerImage = headerView.findViewById(R.id.pp_header_iv);
        HashMap map = sessionManager.getDetailLogin();
        headerText.setText((String) map.get(SessionManager.KEY_NAMA_SALES));

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_home:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, new DashboardFragment());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Dashboard");
                    drawerLayout.closeDrawers();
                    item.setChecked(true);
                    break;
                case R.id.menu_grafik:
                    Toast.makeText(MenuActivity.this, "Fitur belum tersedia", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    break;
                case R.id.menu_profil:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, new ProfilFragment());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Profil");
                    drawerLayout.closeDrawers();
                    item.setChecked(true);
                    break;
                case R.id.menu_report:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.main_container, new ReportFragment());
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("Report Bug");
                    drawerLayout.closeDrawers();
                    item.setChecked(true);
                    break;
                case R.id.menu_logout:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                    builder.setMessage("Anda yakin ingin logout?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Ya", (dialog, id) -> {
                        MenuActivity.this.finish();
                        sessionManager.logoutUser();
                    });
                    builder.setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
            builder.setMessage("Ingin keluar aplikasi?");
            builder.setCancelable(true);
            builder.setPositiveButton("Ya", (dialog, id) -> MenuActivity.this.finish());
            builder.setNegativeButton("Tidak", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
