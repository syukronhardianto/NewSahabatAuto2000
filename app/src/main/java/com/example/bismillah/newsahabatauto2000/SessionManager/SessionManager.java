package com.example.bismillah.newsahabatauto2000.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.example.bismillah.newsahabatauto2000.Activity.LoginActivity;
import com.example.bismillah.newsahabatauto2000.Activity.MenuActivity;

import java.util.HashMap;

/**
 * Created by Bismillah on 08/11/2018.
 */

public class SessionManager {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context _context;

    private static final String SESSIONMANAGER_NAME = "loginsession";
    //LOGIN SALES
    public static final String IS_LOGIN = "is_login";
    public static final String KEY_ID_SALES = "id_sales";
    public static final String KEY_NAMA_SALES = "nama_sales";
    public static final String KEY_USERNAME_SALES = "username_sales";
    public static final String KEY_TELP_SALES = "telp_sales";
    public static final String KEY_EMAIL_SALES = "email_sales";
    public static final String KEY_FOTO_SALES = "foto_sales";
    //LOGIN SPV
    public static final String KEY_ID_SPV = "id_spv";

    public SessionManager (Context context){
        this._context = context;
        int MODE_PRIVATE = 0;
        sp = _context.getSharedPreferences(SESSIONMANAGER_NAME,MODE_PRIVATE);
        editor = sp.edit();
    }

    public void storeLogin(String id_sales, String nama_sales, String username_sales,
                           String telp_sales, String email_sales, String foto_sales){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID_SALES, id_sales);
        editor.putString(KEY_NAMA_SALES, nama_sales);
        editor.putString(KEY_USERNAME_SALES, username_sales);
        editor.putString(KEY_TELP_SALES, telp_sales);
        editor.putString(KEY_EMAIL_SALES, email_sales);
        editor.putString(KEY_FOTO_SALES, foto_sales);

        editor.commit();
    }

    public HashMap getDetailLogin(){
        HashMap<String, String> map = new HashMap<>();
        map.put(KEY_ID_SALES, sp.getString(KEY_ID_SALES,null));
        map.put(KEY_NAMA_SALES, sp.getString(KEY_NAMA_SALES,null));
        map.put(KEY_USERNAME_SALES, sp.getString(KEY_USERNAME_SALES,null));
        map.put(KEY_TELP_SALES, sp.getString(KEY_TELP_SALES,null));
        map.put(KEY_EMAIL_SALES, sp.getString(KEY_EMAIL_SALES,null));
        map.put(KEY_FOTO_SALES, sp.getString(KEY_FOTO_SALES,null));

        return map;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent logoutIntent = new Intent(_context, LoginActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(logoutIntent);
    }

    public boolean isLoggedIn(){
        return sp.getBoolean(IS_LOGIN, false);
    }
}
