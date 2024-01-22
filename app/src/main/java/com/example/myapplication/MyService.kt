package com.example.myapplication

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

const val ORDERS_LINK_LIVE = "api/orders?format=json&IncludeSummary=true&%24inlinecount=allpages&%24filter=BranchId%20eq%20149186"
const val ORDERS_LINK_TEST = "api/orders?format=json&IncludeSummary=true&%24inlinecount=allpages&%24filter=BranchId%20eq%20211917"
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

    @GET(ORDERS_LINK_LIVE)
    @Headers("Content-Type: application/json")
    fun getOrders(@Header("Cookie") sessionId: String,
                  @Query("\$top") top: Int,
                  @Query("\$skip") skip: Int)
    : Call<OrderReadResult>
}