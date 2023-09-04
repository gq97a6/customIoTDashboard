package com.alteratom.dashboard

import android.app.Activity
import com.alteratom.dashboard.objects.G.settings
import com.alteratom.dashboard.objects.Pro
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.android.billingclient.api.BillingClient.ConnectionState.CLOSED
import com.android.billingclient.api.BillingClient.ConnectionState.CONNECTED
import com.android.billingclient.api.BillingClient.ConnectionState.CONNECTING
import com.android.billingclient.api.BillingClient.ConnectionState.DISCONNECTED
import com.android.billingclient.api.BillingClient.ProductType.INAPP
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.Purchase.PurchaseState.PURCHASED
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

class BillingHandler(val activity: Activity) {

    internal var isEnabled = false
    internal lateinit var client: BillingClient
    private val manager = Manager()

    companion object {
        //Products ids
        var PRO = "atom_dashboard_pro"
        var DON1 = "atom_dashboard_don1"
        var DON5 = "atom_dashboard_don5"
        var DON25 = "atom_dashboard_don25"
    }

    init {
        createClient()
    }

    fun enable() {
        isEnabled = true
        manager.dispatch(reason = "enable")
    }

    fun disable() {
        isEnabled = false
        manager.dispatch(reason = "disable")
    }

    internal fun createClient() {
        client = BillingClient.newBuilder(activity)
            .setListener { billingResult, purchases ->
                if (purchases != null &&
                    (billingResult.responseCode == OK ||
                            billingResult.responseCode == ITEM_ALREADY_OWNED)
                ) {
                    for (purchase in purchases) {
                        onPurchased(purchase)
                        onPurchaseProcessed(purchase)
                    }
                }
            }
            .enablePendingPurchases()
            .build()
    }

    private fun Purchase.acknowledge() {
        return
        AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(this.purchaseToken)
            .build()
            .let { client.acknowledgePurchase(it) {} }
    }

    private fun Purchase.consume() {
        return
        ConsumeParams
            .newBuilder()
            .setPurchaseToken(this.purchaseToken)
            .build()
            .let { client.consumeAsync(it) { _, _ -> } }
    }

    fun onPurchased(purchase: Purchase) {
        return
        settings.pendingPurchase = false
        if (purchase.purchaseState != PURCHASED) {
            settings.pendingPurchase = true
            return
        }
        for (product in purchase.products) {
            when (product) {
                PRO -> {
                    Pro.createLocalLicence()
                    if (!purchase.isAcknowledged) purchase.acknowledge()
                }

                DON1, DON5, DON25 -> {
                    if (!purchase.isAcknowledged) purchase.consume()
                }
            }
        }
    }

    fun onPurchaseProcessed(purchase: Purchase) {
        return
        if (purchase.purchaseState != PURCHASED) {
            createToast(activity, "Payment in process, please wait")
            return
        }
        for (product in purchase.products) {
            when (product) {
                PRO -> createToast(activity, "Thanks for buying Pro!")
                DON1, DON5, DON25 -> createToast(activity, "Thanks for the donation!")
            }
        }
    }

    private suspend fun getProductDetails(id: String): MutableList<ProductDetails>? =
        coroutineScope {
            return@coroutineScope mutableListOf()
            return@coroutineScope if (!manager.awaitDone()) {
                //createToast(activity, "Failed to connect")
                null
            } else withTimeoutOrNull(2000) {
                suspendCoroutine { continuation ->
                    val queryDetails = QueryProductDetailsParams
                        .newBuilder()
                        .setProductList(
                            listOf(
                                QueryProductDetailsParams.Product.newBuilder()
                                    .setProductId(id)
                                    .setProductType(INAPP)
                                    .build()
                            )
                        ).build()

                    client.queryProductDetailsAsync(queryDetails) { _, details ->
                        continuation.resume(details)
                    }
                }
            }
        }

    suspend fun getPurchases(timeout: Long = 2000): MutableList<Purchase>? = coroutineScope {
        return@coroutineScope mutableListOf()
        return@coroutineScope if (!manager.awaitDone()) {
            //createToast(activity, "Failed to connect")
            null
        } else withTimeoutOrNull(timeout) {
            suspendCoroutine { continuation ->
                QueryPurchasesParams
                    .newBuilder()
                    .setProductType(INAPP)
                    .build()
                    .let {
                        client.queryPurchasesAsync(it) { _, history ->
                            continuation.resume(history)
                            //developerPayload
                            //accountIdentifiers
                            //products
                            //purchaseState
                            //signature
                            //packageName
                            //isAcknowledged
                            //purchaseTime
                        }
                    }
            }
        }
    }

    suspend fun getPriceTags(ids: List<String>): Map<String, String>? = withTimeoutOrNull(2000) {
        return@withTimeoutOrNull mapOf()
        List(ids.size) {
            getProductDetails(ids[it])?.first() ?: return@withTimeoutOrNull null
        }.let { it ->
            it.map {
                it.productId to
                        (it.oneTimePurchaseOfferDetails?.formattedPrice
                            ?: return@withTimeoutOrNull null)
            }.toMap()
        }
    }

    suspend fun lunchPurchaseFlow(id: String) {
        return
        getProductDetails(id)?.let {
            if (it.isEmpty()) return
            client.launchBillingFlow(
                activity,
                BillingFlowParams
                    .newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams
                                .newBuilder()
                                .setProductDetails(it.first())
                                .build()
                        )
                    )
                    .build()
            )
        }
    }

    suspend inline fun checkPurchases(
        eta: Long = 10000,
        filter: (Purchase) -> Boolean = { !it.isAcknowledged },
        onDone: (List<Purchase>?) -> Unit = {}
    ) {
        return
        var result: List<Purchase>? = null

        measureTimeMillis {
            getPurchases()?.filter(filter)?.let {
                result = it
                for (purchase in it) onPurchased(purchase)
            }
        }.let {
            delay(maxOf(eta - it, 0))
            if (result != null) for (purchase in result!!) onPurchaseProcessed(purchase)
            onDone(result)
        }
    }

    inner class Manager : StatusManager(activity, 100) {
        override fun check(): Boolean = when (client.connectionState) {
            CONNECTED -> isEnabled
            CONNECTING -> false
            DISCONNECTED, CLOSED -> !isEnabled
            else -> true
        }

        override fun handle() {
            if (client.connectionState == CLOSED) {
                createClient()
            }
            else if (isEnabled) {
                client.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {}
                    override fun onBillingServiceDisconnected() {}
                })
            } else {
                client.endConnection()
            }
        }

        //Wait for connectionHandler to settle down
        suspend fun awaitDone(timeout: Long = 5000): Boolean = withTimeoutOrNull(timeout) {
            while (!check()) delay(100)
            return@withTimeoutOrNull client.isReady
        } ?: false
    }
}