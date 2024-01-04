package com.example.myapplication

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface MyService {
    @GET("api/auth/credentials?format=json")
    fun auth(@Query("username") username: String, @Query("password") password: String): Observable<User>

    @GET("api/products/sync?format=json")
    fun syncProducts(@Header("Cookie") sessionId: String) : Observable<Products>

    @GET("api/partners/sync?format=json")
    fun syncPartner(@Header("Cookie") sessionId: String): Observable<Partners>

    @POST("api/orders?format=json")
    @Headers("Content-Type: application/json")
    fun postOrders(@Header("Cookie") sessionId: String, @Body orders: Orders) : Observable<PayResult>
}