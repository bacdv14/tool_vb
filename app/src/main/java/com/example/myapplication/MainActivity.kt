package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "lmao"
        private const val BASE_URL_LIVE = "https://shopminhnhien.pos365.vn/"
        private const val BASE_URL_TEST = "https://test100011.pos365.vn/"
    }

    private lateinit var binding: ActivityMainBinding
    private val disposable = CompositeDisposable()
    private lateinit var myService: MyService
    private lateinit var adapter: StatusAdapter
    private var ssid = ""
    private val publishSubjectStatus = PublishSubject.create<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val okhttp = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain
                    .withConnectTimeout(4, TimeUnit.HOURS)
                    .withReadTimeout(4, TimeUnit.HOURS)
                    .withWriteTimeout(4, TimeUnit.HOURS)
                    .request()
                    .newBuilder()
                    .build()
                chain.proceed(requestBuilder)
            }
            .addInterceptor(HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_LIVE)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okhttp)
            .build()
        myService = retrofit.create(MyService::class.java)

        adapter = StatusAdapter(this)
        binding.rcvStatus.layoutManager = LinearLayoutManager(this)
        binding.rcvStatus.adapter = adapter

        disposable.add(
            publishSubjectStatus
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        if (it is Pair<*,*>) {
                            binding.progressBar.isIndeterminate = false
                            binding.progressBar.max = (it.first as Int)
                            adapter.addItem(it.second as String)
                        } else if (it is String) {
                            adapter.addItem(it)
                        } else {
                            binding.progressBar.progress = it as Int
                        }
                    },
                    onComplete = {

                    },
                    onError = {
                        adapter.addItem(it.toString())
                    }
                )
        )

        binding.btnRun.setOnClickListener {
            runOneTime()
        }

        binding.btnTest.setOnClickListener {
            runTest()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }

        })
    }

    private fun runOneTime() {
        val username = "quantri@pos365.vn"
        val password = "IT@P0s365kmS"
        var listProduct = listOf<Product>()
        var listPartner = listOf<Partner>()
        var listOrderRead = emptyList<OrderRead>()
        var listOrderReadFromWeb = emptyList<OrderRead>()
        var done = 0
        var error = 0
        var progress = 0
        binding.progressBar.isIndeterminate = true
        disposable.add(
            myService.auth(username, password)
                .doOnComplete {
                    publishSubjectStatus.onNext("Get session complete")
                }
                .flatMap {
                    ssid = it.sessionId
                    myService.syncPartner("ss-id=$ssid")
                }.doOnNext {
                    publishSubjectStatus.onNext("Get partner complete ${it.data.size}")
                }
                .flatMap {
                    listPartner = it.data
                    myService.syncProducts("ss-id=$ssid")
                }.doOnNext {
                    publishSubjectStatus.onNext("Get product complete ${it.data.size}")
                }
                .flatMap {
                    listProduct = it.data
                    getListOrderReadFromWeb("ss-id=$ssid")
                }
                .doOnNext {
                    publishSubjectStatus.onNext("Get order from web complete ${it.size}")
                }
                .flatMap {
                    listOrderReadFromWeb = it
                    readFile1()
                }.doOnNext {
                    publishSubjectStatus.onNext("read file 1 complete ${it.size}")
                }
                .flatMap {
                    listOrderRead = it
                    readFile2()
                }
                .doOnNext {
                    publishSubjectStatus.onNext("read file 2 complete ${it.size}")
                }
                .flatMap {
                    mergeDataAsync(listProduct, listPartner, listOrderRead, listOrderReadFromWeb, it)
                }
                .doOnNext {
                    publishSubjectStatus.onNext(Pair(it.size, "merge file complete ${it.size}"))
                }
                .flatMap {
                    Observable.concat(
                        it.map {
                            myService.postOrders("ss-id=$ssid", it)
                                .onErrorResumeNext {
                                    ++error
                                    publishSubjectStatus.onNext(++progress)
                                    Log.e(TAG, "upToServer: onError $it")
                                    Observable.empty()
                                }
                        }
                    )
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onNext = {
                        Log.e(TAG, "up: onNext")
                        publishSubjectStatus.onNext(++progress)
                        ++done
                    },
                    onComplete = {
                        Log.e(TAG, "up: onComplete")
                        publishSubjectStatus.onNext("Up completely: ${done} success - ${error} error")
                    },
                    onError = {
                        Log.e(TAG, "up: onError $it")
                        publishSubjectStatus.onError(it)
                    }
                )
        )
    }

    fun runTest() {
        val username = "quantri@pos365.vn"
        val password = "IT@P0s365kmS"
        var listProduct = listOf<Product>()
        var listPartner = listOf<Partner>()
        var listOrderRead = emptyList<OrderRead>()
        var listOrderReadFromWeb = emptyList<OrderRead>()
        var done = 0
        var error = 0
        var progress = 0
        binding.progressBar.isIndeterminate = true
        disposable.add(
            myService.auth(username, password)
                .doOnComplete {
                    publishSubjectStatus.onNext("Get session complete")
                }
                .flatMap {
                    ssid = it.sessionId
                    myService.syncPartner("ss-id=$ssid")
                }.doOnNext {
                    publishSubjectStatus.onNext("Get partner complete ${it.data.size}")
                }
                .flatMap {
                    listPartner = it.data
                    myService.syncProducts("ss-id=$ssid")
                }.doOnNext {
                    publishSubjectStatus.onNext("Get product complete ${it.data.size}")
                }
                .flatMap {
                    listProduct = it.data
                    getListOrderReadFromWeb("ss-id=$ssid")
                }
                .doOnNext {
                    publishSubjectStatus.onNext("Get order from web complete ${it.size}")
                }
                .flatMap {
                    listOrderReadFromWeb = it
                    readFile1()
                }.doOnNext {
                    publishSubjectStatus.onNext("read file 1 complete ${it.size}")
                }
                .flatMap {
                    listOrderRead = it
                    readFile2()
                }
                .doOnNext {
                    publishSubjectStatus.onNext("read file 2 complete ${it.size}")
                }
                .flatMap {
                    mergeDataAsync(listProduct, listPartner, listOrderRead, listOrderReadFromWeb, it)
                }
                .doOnNext {
                    publishSubjectStatus.onNext(Pair(it.size, "merge file complete ${it.size}"))
                    Log.i(TAG, it.toString())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onNext = {
                        Log.e(TAG, "up: onNext")
                        publishSubjectStatus.onNext(++progress)
                        ++done
                    },
                    onComplete = {
                        Log.e(TAG, "up: onComplete")
                        publishSubjectStatus.onNext("Up completely: ${done} success - ${error} error")
                    },
                    onError = {
                        Log.e(TAG, "up: onError $it")
                        publishSubjectStatus.onError(it)
                    }
                )
        )
    }

    private fun mergeDataAsync(
        listProduct: List<Product>,
        listPartner: List<Partner>,
        listOrderRead: List<OrderRead>,
        listOrderReadFromWeb: List<OrderRead>,
        listDetailRead: List<DetailRead>
    ): Observable<List<Orders>> {
        return Observable.create { emitter ->
            if (listOrderRead.isEmpty()) {
                emitter.onError(Throwable(message = "list order empty"))
                return@create
            }
            if (listDetailRead.isEmpty()) {
                emitter.onError(Throwable(message = "list order empty"))
                return@create
            }
            if(listOrderReadFromWeb.isEmpty()) {
                emitter.onError(Throwable(message = "list order from web empty"))
                return@create
            }
            val listOrder = mutableListOf<Order>()
            listOrderRead.forEach { orderRead ->
                val order = Order()
                val findDetail = listDetailRead.filter { it.orderCode == orderRead.code }
                val orderFromWeb = listOrderReadFromWeb.firstOrNull { it.code == orderRead.code }
                findDetail.forEach { detailRead ->
                    val findProduct = listProduct.find { it.code == detailRead.productCode }
                    val orderDetail = OrderDetail(
                        productId = findProduct?.id ?: 0,
                        code = detailRead.productCode,
                        name = detailRead.name,
                        quantity = detailRead.quantity,
                        attributesName = findProduct?.attributesName ?: "",
                        price = detailRead.price,
                        isSerialNumberTracking = findProduct?.isSerialNumberTracking ?: false,
                        basePrice = findProduct?.price ?: 0.0,
                        unit = findProduct?.unit ?: "",
                        productType = findProduct?.productType ?: 1,
                        priceConfig = findProduct?.priceConfig ?: "",
                        serveby = orderRead.soldById.toDouble()
                    )
                    order.orderDetails.add(orderDetail)
                }
                val findPartner = listPartner.find { it.id == orderRead.partnerId }
                if (findPartner != null) {
                    order.partnerId = orderRead.partnerId
                    order.partner = findPartner
                } else {
                    order.partner = null
                    order.partnerId = null
                }
                order.purchaseDate = orderRead.purchaseDate
                order.status = orderRead.status
                order.activeDate = orderRead.createdDate
                order.discount = orderRead.discount
                order.accountId = if (orderRead.accountId == 0) null else orderRead.accountId
                order.amountReceived = orderRead.amountReceived
                order.description = orderRead.description
                order.discount = orderRead.discount
                order.discountToView = orderRead.discountToView
                order.excessCash = orderRead.excessCash
                order.moreAttributes = orderRead.moreAttributes
                order.numberOfGuests = orderRead.numberOfGuests
                order.pos = orderRead.pos
                order.roomId = orderRead.roomId
                order.shippingCost = orderRead.shippingCost
                order.soldById = orderRead.soldById
                order.total = orderRead.total
                order.totalAdditionalServices = orderRead.totalAdditionalServices
                order.totalPayment = orderRead.totalPayment
                order.vat = orderRead.vAT
                order.voucher = orderRead.voucher
                order.channelId = orderRead.channelId
                order.code = orderRead.code

                if (orderFromWeb != null) {
                    order.id = orderFromWeb.id
                }

                listOrder.add(order)
            }
            Log.i("Before filter", listOrder.toString())
            Log.i("Before filter", "Size = ${listOrder.size}")
            val listOrderFiltered = listOrder.filter { order -> order.id != 0 }
            Log.i("After filter", listOrderFiltered.toString())
            Log.i("After filter", "Size = ${listOrderFiltered.size}")
            emitter.onNext(listOrderFiltered.map {
                val orders = Orders()
                orders.order = it
                if ((it.excessCash) < 0.0) {
                    it.excessCashType = 1
                    orders.excessCashType = 1
                }
                orders.dontSetTime = it.purchaseDate.isEmpty()
                orders
            })
            emitter.onComplete()
        }
    }

    private fun getListOrderReadFromWeb(ssId: String): Observable<List<OrderRead>> {
        val testCount = 58
        val liveCount = 66
        return Observable.create {emitter ->
            val listOrderRead = mutableListOf<OrderRead>()

            val top = 100
            var skip = 0
            var count = 0

            while (count < liveCount) {
                val response = myService.getOrders(ssId, top, skip).execute()
                response.body()!!.results.forEach {
                    listOrderRead.add(it)
                }

                Log.i(TAG, listOrderRead.size.toString())

                skip += 100
                count++
            }

            emitter.onNext(listOrderRead.distinct())
            emitter.onComplete()
        }
    }

    private fun readFile1(): Observable<List<OrderRead>> {
        return Observable.create { emmitter ->
            val csvFile = "order.csv"
            val listOrder = mutableListOf<OrderRead>()
            var count = 1
            try {
                val inputStream = assets.open(csvFile)
                val bufferReader = BufferedReader(InputStreamReader(inputStream))
                var line: String?

                while (bufferReader.readLine().also { line = it } != null) {
                    val column = line?.split(",") ?: emptyList()
                    if (column[0] == "Id") {
                        continue
                    }
                    val orderRead = OrderRead(
                        id = if (column[0].isEmpty()) 0 else column[0].toInt(),
                        code = column[1],
                        purchaseDate = column[2],
                        endPurchaseDate = column[3],
                        branchId = column[4].toInt(),
                        description = column[5],
                        status = column[6].toInt(),
                        modifiedDate = column[7],
                        retailerId = column[8].toInt(),
                        discount = column[9].toDouble(),
                        soldById = column[10].toInt(),
                        roomId = if (column[11].isEmpty()) null else column[11].toInt(),
                        createdDate = column[12],
                        createdBy = column[13].toInt(),
                        modifiedBy = if (column[14].isEmpty()) null else column[14].toDouble(),
                        discountToView = column[15],
                        total = column[16].toDouble(),
                        totalPayment = column[17].toDouble(),
                        partnerId = column[18].toInt(),
                        excessCash = if (column[19].contains('-'))
                            column[19].replace("-", "").toDouble() * -1.0
                        else column[19].toDouble(),
                        amountReceived = column[20].toDouble(),
                        shippingCost = column[21].toDouble(),
                        priceBookId = column[22],
                        accountId = if (column[23].isEmpty()) null else column[23].toInt(),
                        pos = if (column[24].isEmpty()) "" else column[24],
                        numberOfGuests = column[25].toInt(),
                        vAT = column[26].toDouble(),
                        voucher = column[27].toDouble(),
                        moreAttributes = column[28].replace(".", ","),
                        channelId = if (column[29].isEmpty()) null else column[29].toDouble()
                            .toInt(),
                        totalAdditionalServices = column[30].toDouble(),
                        date = column[31],
                        branchName = column[32],
                        partnerCode = column[33]
                    )
                    count++
                    listOrder.add(orderRead)
                }
                bufferReader.close()
                inputStream.close()
                Log.e(TAG, "readFile1 done $count")
                emmitter.onNext(listOrder)
                emmitter.onComplete()
            } catch (e: Exception) {
                Log.e(TAG, "readFile1 error row ${count}: $e")
                emmitter.onError(e)
            }
        }
    }

    private fun readFile2(): Observable<List<DetailRead>> {
        return Observable.create { emmitter ->
            val csvFile = "hanghoa.csv"
            val listDetail = mutableListOf<DetailRead>()
            var count = 1
            try {
                val inputStream = assets.open(csvFile)
                val bufferReader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (bufferReader.readLine().also { line = it } != null) {
                    val column = line?.split(",") ?: emptyList()
                    if (column[0] == "Id") {
                        continue
                    }
                    val detail = DetailRead(
                        id = column[0].toInt(),
                        productCode = column[1],
                        name = column[2].replace(".", ","),
                        quantity = column[3].toDouble(),
                        price = column[4].toDouble(),
                        orderCode = column[5],
                        purchaseDate = column[6],
                        branchName = column[7],
                        partnerCode = column[8]
                    )
                    count++
                    listDetail.add(detail)
                }
                bufferReader.close()
                inputStream.close()
                Log.e(TAG, "readFile2 done $count")
                emmitter.onNext(listDetail)
                emmitter.onComplete()
            } catch (e: Exception) {
                Log.e(TAG, "readFile2 error row ${count}: $e")
                emmitter.onError(e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

}