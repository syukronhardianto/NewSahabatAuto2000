package com.example.bismillah.newsahabatauto2000.Interface;

import com.example.bismillah.newsahabatauto2000.Model.RootObject;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Bismillah on 09/11/2018.
 */

public interface ProspekAPI {
    @FormUrlEncoded
    @POST("getCust.php")
    Observable<RootObject> getProspek(@Field("id_sales") String id_sales);

    @FormUrlEncoded
    @POST("inputCust.php")
    Observable<RootObject> inputProspek(@Field("nama_cust") String nama_cust,
                                        @Field("kategori") String kategori,
                                        @Field("jenis_kel_cust") String jenis_kel_cust,
                                        @Field("jenis_mobil") String jenis_mobil,
                                        @Field("warna_mobil") String warna_mobil,
                                        @Field("alamat_cust") String alamat_cust,
                                        @Field("telp_cust") String telp_cust,
                                        @Field("email_cust") String email_cust,
                                        @Field("agama_cust_do") String agama_cust_do,
                                        @Field("tgl_lahir_cust_do") String tgl_lahir_cust_do,
                                        @Field("id_sales") String id_sales);

    @FormUrlEncoded
    @POST("update_cust.php")
    Observable<RootObject> updateProspek(@Field("id_cust") String id_cust,
                                        @Field("nama_cust") String nama_cust,
                                        @Field("kategori") String kategori,
                                        @Field("jenis_mobil") String jenis_mobil,
                                        @Field("warna_mobil") String warna_mobil,
                                        @Field("alamat_cust") String alamat_cust,
                                        @Field("telp_cust") String telp_cust,
                                        @Field("email_cust") String email_cust,
                                        @Field("agama_cust_do") String agama_cust_do,
                                        @Field("tgl_lahir_cust_do") String tgl_lahir_cust_do);

    @FormUrlEncoded
    @POST("delete_cust.php")
    Observable<RootObject> deleteProspek(@Field("id_cust") String id_cust);

    @FormUrlEncoded
    @POST("search_prospek.php")
    Observable<RootObject> getProspekSearchResult(@Field("search") String search,
                                                  @Field("id_sales") String id_sales);

    @FormUrlEncoded
    @POST("sort_prospek_bydate.php")
    Observable<RootObject> getProspekSortByDate(@Field("id_sales") String id_sales,
                                                @Field("tgl_awal") String tgl_awal,
                                                @Field("tgl_akhir") String tgl_akhir);

    @FormUrlEncoded
    @POST("sort_prospek_bykategori.php")
    Observable<RootObject> getProspekSortByKategori(@Field("kategori") String kategori,
                                                    @Field("id_sales") String id_sales);

    @FormUrlEncoded
    @POST("result_cust_monthly.php")
    Observable<RootObject> getProspekMonthly(@Field("id_sales") String id_sales);

    @FormUrlEncoded
    @POST("result_cust_yearly.php")
    Observable<RootObject> getProspekYearly(@Field("id_sales") String id_sales);

    @FormUrlEncoded
    @POST("result_cust_totally.php")
    Observable<RootObject> getProspekTotally(@Field("id_sales") String id_sales);
}
