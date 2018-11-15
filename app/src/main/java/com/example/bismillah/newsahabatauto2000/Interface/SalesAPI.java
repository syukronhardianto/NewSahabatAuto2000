package com.example.bismillah.newsahabatauto2000.Interface;

import com.example.bismillah.newsahabatauto2000.Model.RootObject;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SalesAPI {
    @FormUrlEncoded
    @POST("update_sales.php")
    Observable<RootObject> ubahSales(@Field("id_sales") String id_sales,
                                     @Field("nama_sales") String nama_sales,
                                     @Field("username_sales") String username_sales,
                                     @Field("telp_sales") String telp_sales,
                                     @Field("email_sales") String email_sales);
}
