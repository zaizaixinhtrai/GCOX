package com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders

import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate
import com.appster.extensions.loadImg
import com.appster.extensions.toUserName
import com.gcox.fansmeet.R
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel
import kotlinx.android.synthetic.main.user_info_item_layout.view.*


/**
 * Created by Ngoc on 5/16/18.
 */

class UserInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): UserInfoViewHolder {
            return UserInfoViewHolder(parent.inflate(layout))
        }
    }

    init {
    }

    fun bindTo(item: CelebrityProfileModel, listener: OnClickListener?) {
        with(itemView) {
            useImage.loadImg(item.userImage)
            tvDisplayName.text = item.displayName
            tvUsername.text = item.userName?.toUserName()
//            txtAbout.text = item.about
//            followersCount.text = "59k"
//            tvChallengesCount.text = "56900"
//            tvGiftCount.text = "56345"

            if (item.isFollow!!) {
                btFollow.setBackgroundResource(R.drawable.home_following_btn_selector)
                btFollow.setTextColor(ContextCompat.getColor(context, R.color.color_2587E7))
                btFollow.text = context.getString(R.string.profile_following_title)
            } else {
//                btFollow.setBackgroundResource(R.drawable.home_follow_btn_selector)
//                btFollow.setTextColor(ContextCompat.getColor(context, R.color.color_9a9b9a))
//                btFollow.text = context.getString(R.string.follow)

                btFollow.setBackgroundResource(R.drawable.home_following_btn_selector)
                btFollow.setTextColor(ContextCompat.getColor(context, R.color.color_2587E7))
                btFollow.text = context.getString(R.string.profile_following_title)
            }
        }
    }

    interface OnClickListener {
        fun onFollowClicked(item: CelebrityProfileModel)
    }
}
