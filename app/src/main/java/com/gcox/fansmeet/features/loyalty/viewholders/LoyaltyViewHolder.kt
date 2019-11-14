package com.gcox.fansmeet.features.loyalty.viewholders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate
import com.gcox.fansmeet.R
import com.gcox.fansmeet.features.loyalty.LoyaltyModel
import com.gcox.fansmeet.features.main.UserType
import com.gcox.fansmeet.util.SetDateTime
import kotlinx.android.synthetic.main.loyalty_row_layout.view.*


class LoyaltyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): LoyaltyViewHolder {
            return LoyaltyViewHolder(parent.inflate(layout))
        }
    }

    init {
    }

    fun bindTo(item: LoyaltyModel, listener: OnClickListener?) {
        with(itemView) {
            tvDisplayName.text = item.displayName
            item.loyalty?.let {
                if (item.loyalty!! < 0) {
                    tvLoyalty.text = item.loyalty.toString()
                } else {
                    tvLoyalty.text = "+" + item.loyalty.toString()
                }
            }

            if (item.userType != null) {
                when (item.userType) {
                    UserType.CELEBRITY_USER -> {
                        tvType.setBackgroundResource(R.drawable.loyalty_celebrity_background)
                        tvType.text = context.getString(R.string.user_type_celebrity)
                    }

                    UserType.MERCHANT_USER -> {
                        tvType.setBackgroundResource(R.drawable.loyalty_merchant_background)
                        tvType.text = context.getString(R.string.user_type_merchant)
                    }

                    UserType.INFLUENCER_USER -> {
                        tvType.setBackgroundResource(R.drawable.loyalty_influency_background)
                        tvType.text = context.getString(R.string.user_type_influencer)
                    }
                }
            }

            tvTime.text = SetDateTime.parseTimeForTransactionItem(item.timer)
        }
    }

    interface OnClickListener
}