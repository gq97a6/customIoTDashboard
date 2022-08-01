package com.alteratom.dashboard

import android.app.Activity
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.android.billingclient.api.BillingClient.ConnectionState.*
import com.android.billingclient.api.BillingClient.FeatureType.PRODUCT_DETAILS
import com.android.billingclient.api.BillingClient.ProductType.INAPP
import com.android.billingclient.api.Purchase.PurchaseState.PURCHASED
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

class BillingHandler(val activity: Activity) {

    internal var isEnabled = false

    internal lateinit var client: BillingClient

    val connectionHandler = BillingConnectionHandler()

    companion object {
        //Products ids
        var PRO = "atom_dashboard_pro"
        var DON0 = "test_product_01"
        var DON1 = "test_product_02"
        var DON2 = "test_product_03"
    }

    init {
        createClient()
    }

    fun onPurchased(purchase: Purchase) {
        if (purchase.purchaseState != PURCHASED) return
        for (product in purchase.products) {
            when (product) {
                PRO -> {
                    ProVersion.createLocalLicence()
                    if (!purchase.isAcknowledged) purchase.acknowledge()
                }
                DON0, DON1, DON2 -> {
                    if (!purchase.isAcknowledged) purchase.consume()
                }
            }
        }
    }

    fun onPurchaseProcessed(purchase: Purchase) {
        if (purchase.purchaseState != PURCHASED) {
            createToast(activity, "Payment in process, please wait")
            return
        }
        for (product in purchase.products) {
            when (product) {
                PRO -> createToast(activity, "Thanks for buying Pro!")
                DON0, DON1, DON2 -> createToast(activity, "Thanks for the donation!")
            }
        }
    }

    suspend fun lunchPurchaseFlow(id: String) {
        if (!connectionHandler.awaitDone()) {
            createToast(activity, "Failed to connect")
            return
        }

        if (client.isFeatureSupported(PRODUCT_DETAILS).responseCode != OK) {
            createToast(activity, "Please update Google Play Store")
            return
        }

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

        client.queryProductDetailsAsync(queryDetails) { result, details ->
            if (details.size == 0) return@queryProductDetailsAsync
            client.launchBillingFlow(
                activity,
                BillingFlowParams
                    .newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams
                                .newBuilder()
                                .setProductDetails(details.first())
                                .build()
                        )
                    )
                    .build()
            )
        }
    }

    suspend fun getPurchases(): MutableList<Purchase>? = coroutineScope {
        return@coroutineScope if (!connectionHandler.awaitDone()) {
            createToast(activity, "Failed to connect")
            null
        } else withTimeoutOrNull(2000) {
            suspendCoroutine { continuation ->
                QueryPurchasesParams
                    .newBuilder()
                    .setProductType(INAPP)
                    .build()
                    .let {
                        client.queryPurchasesAsync(it) { result, history ->
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

    inline suspend fun checkPendingPurchases(onDone: () -> Unit) {
        var result: MutableList<Purchase>? = null

        measureTimeMillis {
            getPurchases()?.let {
                for (purchase in it) onPurchased(purchase)
                result = it
            }
        }.let {
            delay(maxOf(10000 - it, 0))
            if (result != null) for (purchase in result!!) onPurchaseProcessed(purchase)
            else createToast(activity, "No purchase found")
            onDone()
        }
    }

    internal fun Purchase.acknowledge() {
        AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(this.purchaseToken)
            .build()
            .let { client.acknowledgePurchase(it) {} }
    }

    internal fun Purchase.consume() {
        ConsumeParams
            .newBuilder()
            .setPurchaseToken(this.purchaseToken)
            .build()
            .let { client.consumeAsync(it) { _, _ -> } }
    }

    fun enable() {
        isEnabled = true
        connectionHandler.dispatch("enable")
    }

    fun disable() {
        isEnabled = false
        connectionHandler.dispatch("disable")
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

    inner class BillingConnectionHandler : ConnectionHandler() {

        override fun isDone(): Boolean = when (client.connectionState) {
            CONNECTED -> isEnabled
            CONNECTING -> false
            DISCONNECTED, CLOSED -> !isEnabled
            else -> true
        }

        override fun handleDispatch() {
            when (client.connectionState) {
                CONNECTED -> client.endConnection()
                DISCONNECTED -> client.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {}
                    override fun onBillingServiceDisconnected() {}
                })
                CLOSED -> {
                    createClient()
                    handleDispatch()
                }
            }
        }

        //Wait for connectionHandler to settle down
        suspend fun awaitDone(timeout: Long = 5000): Boolean = withTimeoutOrNull(timeout) {
            while (!isDone()) delay(50)
            return@withTimeoutOrNull client.isReady
        } ?: false
    }
}