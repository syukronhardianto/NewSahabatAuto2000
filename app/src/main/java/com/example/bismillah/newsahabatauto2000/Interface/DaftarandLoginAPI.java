package com.example.bismillah.newsahabatauto2000.Interface;

import com.example.bismillah.newsahabatauto2000.Model.RootObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Bismillah on 02/11/2018.
 */

public interface DaftarandLoginAPI {

    @GET("ambil_id_nama_spv.php")
    Observable<RootObject> getIdNamaSpv();

    @FormUrlEncoded
    @POST("register_sales2.php")
    Observable<RootObject> saveDataSales(@Field("nama_sales") String nama_sales,
                                   @Field("username_sales") String username_sales,
                                   @Field("pass_sales") String pass_sales,
                                   @Field("telp_sales") String telp_sales,
                                   @Field("email_sales") String email_sales,
                                   @Field("id_spv") String id_spv);

    @FormUrlEncoded
    @POST("login_sales.php")
    Observable<RootObject> loginValidation(@Field("username_sales") String username_sales,
                                           @Field("pass_sales") String pass_sales);
}