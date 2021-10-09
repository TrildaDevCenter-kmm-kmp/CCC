package com.github.mustafaozhan.billing

import com.android.billingclient.api.PurchaseHistoryRecord
import com.github.mustafaozhan.billing.model.PurchaseMethod

sealed class BillingEffect {
    object SuccessfulPurchase : BillingEffect()

    data class RestorePurchase(
        val purchaseHistoryRecordList: List<PurchaseHistoryRecord>
    ) : BillingEffect()

    data class AddPurchaseMethods(
        val purchaseMethodList: List<PurchaseMethod>
    ) : BillingEffect()

    data class UpdateAddFreeDate(
        val id: String
    ) : BillingEffect()
}
