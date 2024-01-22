package com.example.myapplication

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderReadResult(
    @SerializedName("Timestamp") var timeStamp: String = "",
    @SerializedName("results") var results: List<OrderRead> = listOf(),
    @SerializedName("__count") var count: Int = 0
)

data class OrderRead(
    @SerializedName("Id") var id: Int = 0,
    @SerializedName("Code") var code: String = "",
    @SerializedName("PurchaseDate") var purchaseDate: String = "",
    @SerializedName("EndPurchaseDate") var endPurchaseDate: String = "",
    @SerializedName("BranchId") var branchId: Int = 0,
    @SerializedName("Description") var description: String = "",
    @SerializedName("Status") var status: Int = 0,
    @SerializedName("ModifiedDate") var modifiedDate: String = "",
    @SerializedName("RetailerId") var retailerId: Int = 0,
    @SerializedName("Discount") var discount: Double = 0.0,
    @SerializedName("SoldById") var soldById: Int = 0,
    @SerializedName("RoomId") var roomId: Int? = null,
    @SerializedName("CreatedDate") var createdDate: String = "",
    @SerializedName("CreatedBy") var createdBy: Int = 0,
    @SerializedName("ModifiedBy") var modifiedBy: Double? = 0.0,
    @SerializedName("DiscountToView") var discountToView: String = "",
    @SerializedName("Total") var total: Double = 0.0,
    @SerializedName("TotalPayment") var totalPayment: Double = 0.0,
    @SerializedName("PartnerId") var partnerId: Int = 0,
    @SerializedName("ExcessCash") var excessCash: Double = 0.0,
    @SerializedName("AmountReceived") var amountReceived: Double = 0.0,
    @SerializedName("ShippingCost") var shippingCost: Double = 0.0,
    @SerializedName("PriceBookId") var priceBookId: String? = null,
    @SerializedName("AccountId") var accountId: Int? = null,
    @SerializedName("Pos") var pos: String = "",
    @SerializedName("NumberOfGuests") var numberOfGuests: Int = 0,
    @SerializedName("VAT") var vAT: Double = 0.0,
    @SerializedName("Voucher") var voucher: Double = 0.0,
    @SerializedName("MoreAttributes") var moreAttributes: String = "",
    @SerializedName("ChannelId") var channelId: Int? = null,
    @SerializedName("TotalAdditionalServices") var totalAdditionalServices: Double = 0.0,
    @SerializedName("date") var date: String = "",
    @SerializedName("BranchName") var branchName: String = "",
    @SerializedName("PartnerCode") var partnerCode: String = "",
) {
}

data class DetailRead(
    @SerializedName("Id") val id: Int = 0,
    @SerializedName("ProductCode") val productCode: String = "",
    @SerializedName("Name") val name: String = "",
    @SerializedName("Quantity") val quantity: Double = 0.0,
    @SerializedName("Price") val price: Double = 0.0,
    @SerializedName("OrderCode") val orderCode: String = "",
    @SerializedName("PurchaseDate") val purchaseDate: String = "",
    @SerializedName("BranchName") val branchName: String = "",
    @SerializedName("PartnerCode") val partnerCode: String = ""
)

data class Products(@SerializedName("LatestSync") @Expose val latestSync: String,
                    @SerializedName("Data") @Expose val data: List<Product>)

data class Product(
    @SerializedName("AttributesName")
    var attributesName: String = "",
    @SerializedName("BlockOfTimeToUseService")
    var blockOfTimeToUseService: Int = 0,
    @SerializedName("BonusPoint")
    var bonusPoint: Double = 0.0,
    @SerializedName("BonusPointForAssistant")
    var bonusPointForAssistant: Double = 0.0,
    @SerializedName("BonusPointForAssistant2")
    var bonusPointForAssistant2: Double = 0.0,
    @SerializedName("BonusPointForAssistant3")
    var bonusPointForAssistant3: Double = 0.0,
    @SerializedName("CategoryId")
    var categoryId: Int = 0,
    @SerializedName("Code")
    var code: String = "",
    @SerializedName("Code2")
    var code2: String = "",
    @SerializedName("Code3")
    var code3: String = "",
    @SerializedName("Code4")
    var code4: String = "",
    @SerializedName("Coefficient")
    var coefficient: Int = 0,
    @SerializedName("ConversionValue")
    var conversionValue: Double = 0.0,
    @SerializedName("Id")
    var id: Int = 0,
    @SerializedName("IsPercentageOfTotalOrder")
    var isPercentageOfTotalOrder: Boolean = false,
    @SerializedName("IsPriceForBlock")
    var isPriceForBlock: Boolean = false,
    @SerializedName("IsSerialNumberTracking")
    var isSerialNumberTracking: Boolean = false,
    @SerializedName("LargeUnit")
    var largeUnit: String = "",
    @SerializedName("LargeUnitCode")
    var largeUnitCode: String = "",
    @SerializedName("Name")
    var name: String = "",
    @SerializedName("OnHand")
    var onHand: Double = 0.0,
    @SerializedName("Position")
    var position: Int = 0,
    @SerializedName("Price")
    var price: Double = 0.0,
    @SerializedName("PriceConfig")
    var priceConfig: String = "",
    @SerializedName("PriceLargeUnit")
    var priceLargeUnit: Double = 0.0,
    @SerializedName("Printer")
    var printer: String = "",
    @SerializedName("ProductImages")
    var productImages: List<Any> = listOf(),
    @SerializedName("ProductType")
    var productType: Int = 0,
    @SerializedName("SplitForSalesOrder")
    var splitForSalesOrder: Boolean = false,
    @SerializedName("Unit")
    var unit: String = ""
)

data class Orders(
    @SerializedName("DontSetTime")
    var dontSetTime: Boolean = false,
    @SerializedName("Duplicate")
    var duplicate: String = "",
    @SerializedName("ExcessCashType")
    var excessCashType: Int = 0,
    @SerializedName("MerchantCode")
    var merchantCode: String = "",
    @SerializedName("MerchantName")
    var merchantName: String = "",
    @SerializedName("Order")
    var order: Order = Order(),
    @SerializedName("QrCodeEnable")
    var qrCodeEnable: Boolean = false
) {
    fun toJson(): String {
        return Gson().toJson(this)
    }
}

data class Order(
    @SerializedName("OfflineId") @Expose var offlineId: String = "",
    @SerializedName("Status") @Expose var status: Int = 2,
    @SerializedName("Discount") @Expose var discount: Double = 0.0,
    @SerializedName("TotalPayment") @Expose var totalPayment: Double = 0.0,
    @SerializedName("AmountReceive") @Expose var amountReceive: Double = 0.0,
    @SerializedName("AmountReceived") @Expose var amountReceived: Double = 0.0,
    @SerializedName("Total") @Expose var total: Double = 0.0,
    @SerializedName("OrderDetails") @Expose var orderDetails: MutableList<OrderDetail> = mutableListOf(),
    @SerializedName("SoldById") @Expose var soldById: Int = 0,
    @SerializedName("ExcessCashType") @Expose var excessCashType: Int? = 0,
    @SerializedName("ExcessCash") @Expose var excessCash: Double = 0.0,
    @SerializedName("RoomId") @Expose var roomId: Int? = null,
    @SerializedName("RoomName") @Expose var roomName: String = "",
    @SerializedName("Pos") @Expose var pos: String = "",
    @SerializedName("NumberOfGuests") @Expose var numberOfGuests: Int = 0,
    @Transient @SerializedName("SyncStatus") @Expose var syncStatus: Int? = 0,
    @SerializedName("VATRates") @Expose var vatRates: String = "",
    @SerializedName("DiscountValue") @Expose var discountValue: Double = 0.0,
    @SerializedName("Voucher") @Expose var voucher: Double = 0.0,
    @SerializedName("DiscountToView") @Expose var discountToView: Any = "", // "10%"
    @SerializedName("VAT") @Expose var vat: Double = 0.0,
    @SerializedName("Description") @Expose var description: String = "", // Note
    @SerializedName("ActiveDate") @Expose var activeDate: String = "",
    @SerializedName("PartnerId") @Expose var partnerId: Int? = null, // Loi DATA tren server neu khong de null.
    @SerializedName(value="Partner", alternate=["Partner1"]) @Expose var partner: Partner? = null,
    @SerializedName("OldDebt") @Expose var oldDebt: Double = 0.0,
    @SerializedName("DiscountRatio") @Expose var discountRatio: Double = 0.0, // ""
    @Transient @SerializedName("VoucherCode") @Expose var voucherCode: Any? = null,
    @Transient @SerializedName("VoucherId") @Expose var voucherId: Any? = null,
    @SerializedName("Id") @Expose var id: Int = 0,
    @SerializedName("Code") @Expose var code: String = "",
    @Transient @SerializedName("initializingTotalPayment") @Expose var isInitializingTotalPayment: Boolean? = false,
    @SerializedName("DeliveryById") @Expose var deliveryById: Int? = null,
    @SerializedName("AccountId") @Expose var accountId: Int? = null,
    @SerializedName("ShippingCost") @Expose var shippingCost: Double? = 0.0,
    @SerializedName("ShippingCostForPartner") @Expose var shippingCostForPartner: Double = 0.0,
    @SerializedName("TotalAdditionalServices") @Expose var totalAdditionalServices: Double = 0.0,
    @SerializedName("DeliveryBy") @Expose var deliveryBy: Any? = null,
    @SerializedName("PurchaseDate") @Expose var purchaseDate: String = "",
    @SerializedName("PriceBookId") @Expose var priceBookId: String? = "0",
    @Transient @SerializedName("Topping") @Expose var topping: String? = "",
    @SerializedName("PointToValue") @Expose var pointToValue: Float = 0F,
    @SerializedName("MoreAttributes") @Expose var moreAttributes: String? = null,
    @SerializedName("Printed") @Expose var printed: Boolean? = false,
    @SerializedName("ChannelId") @Expose var channelId: Int? = null,
    @Transient @SerializedName("CardNumber") @Expose var cardNumber: String? = null
)

data class OrderDetail(
    @SerializedName("AttributesName")
    var attributesName: String = "",
    @SerializedName("BasePrice")
    var basePrice: Double = 0.0,
    @SerializedName("BlockOfTimeToUseService")
    var blockOfTimeToUseService: Float = 6F,
    @SerializedName("BonusPoint")
    var bonusPoint: Float = 0F,
    @SerializedName("BonusPointForAssistant")
    var bonusPointForAssistant: Float = 0F,
    @SerializedName("BonusPointForAssistant2")
    var bonusPointForAssistant2: Float = 0F,
    @SerializedName("BonusPointForAssistant3")
    var bonusPointForAssistant3: Float = 0F,
    @SerializedName("Checkin")
    var checkin: String = "",
    @SerializedName("Code")
    var code: String = "",
    @SerializedName("ConversionValue")
    var conversionValue: Double = 1.0,
    @SerializedName("Description")
    var description: String? = null,
    @SerializedName("DiscountRatio")
    var discountRatio: Double = 0.0,
    @SerializedName("IsLargeUnit")
    var isLargeUnit: Boolean = false,
    @SerializedName("IsPercentageOfTotalOrder")
    var isPercentageOfTotalOrder: Boolean = false,
    @SerializedName("IsPriceForBlock")
    var isPriceForBlock: Boolean = false,
    @SerializedName("IsSerialNumberTracking")
    var isSerialNumberTracking: Boolean = false,
    @SerializedName("LabelPrinted")
    var labelPrinted: Int = 0,
    @SerializedName("LargeUnit")
    var largeUnit: String = "",
    @SerializedName("MoreAttributes")
    var moreAttributes: String = "",
    @SerializedName("Name")
    var name: String = "",
    @SerializedName("OnHand")
    var onHand: Double = 0.0,
    @SerializedName("PercentageOfTotalOrderValue")
    var percentageOfTotalOrderValue: Int = 0,
    @SerializedName("Price")
    var price: Double = 0.0,
    @SerializedName("PriceConfig")
    var priceConfig: String = "",
    @SerializedName("PriceLargeUnit")
    var priceLargeUnit: Double = 0.0,
    @SerializedName("Printer")
    var printer: String = "",
    @SerializedName("Processed")
    var processed: Double = 0.0,
    @SerializedName("ProductId")
    var productId: Int = 0,
    @SerializedName("ProductType")
    var productType: Int = 1,
    @SerializedName("Quantity")
    var quantity: Double = 0.0,
    @SerializedName("SerialNumbers")
    var serialNumbers: Any? = null,
    @SerializedName("Serveby")
    var serveby: Double = 0.0,
    @SerializedName("TotalOnItem")
    var totalOnItem: Int = 0,
    @SerializedName("Unit")
    var unit: String = "",
    @SerializedName("UnitPrice")
    var unitPrice: Double = 0.0
)

data class PayResult (
    @SerializedName("Message") @Expose var message: String = "",
    @SerializedName("Id") @Expose var id: Long = 0,
    @SerializedName("Code") @Expose var code: String = "",
    @SerializedName("QRCode") @Expose var qrCode: String = "",
    @SerializedName("Status") @Expose var status: Boolean = false,
    @SerializedName("JsonContent") @Expose var jsonContent: String? = null,
    @SerializedName("HostName") @Expose var hostName: String = "",
    @SerializedName("BranchId") @Expose var branchId: Int = 0,
    @SerializedName("Type") @Expose var type : Int = 0) {

}

data class Partners (
    @SerializedName("LatestSync") @Expose var latestSync: String,
    @SerializedName("Data") @Expose var data: List<Partner>)

data class Partner(
    @SerializedName("Address")
    var address: String = "",
    @SerializedName("BestDiscount")
    var bestDiscount: Int = 0,
    @SerializedName("Code")
    var code: String = "",
    @SerializedName("Debt")
    var debt: Double = 0.0,
    @SerializedName("Gender")
    var gender: Int = 0,
    @SerializedName("Id")
    var id: Int = 0,
    @SerializedName("Keyword")
    var keyword: String = "",
    @SerializedName("Name")
    var name: String = "",
    @SerializedName("PartnerGroupMembers")
    var partnerGroupMembers: List<Any> = listOf(),
    @SerializedName("Phone")
    var phone: String = "",
    @SerializedName("Point")
    var point: Double = 0.0,
    @SerializedName("TotalDebt")
    var totalDebt: Double = 0.0
)

data class User(
    @SerializedName("UserId") @Expose val userId: Int = -1,
    @SerializedName("SessionId") @Expose val sessionId: String = "",
    @SerializedName("UserName") @Expose val userName: String = "",
    @SerializedName("ResponseStatus") @Expose val responseStatus: Any = Any())