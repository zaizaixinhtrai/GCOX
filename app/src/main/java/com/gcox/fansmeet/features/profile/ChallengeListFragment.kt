package com.gcox.fansmeet.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.fragment.BaseFragment
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.TopFansViewHolder
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.UserInfoViewHolder
import com.gcox.fansmeet.models.UserModel
import kotlinx.android.synthetic.main.fragment_profile.*
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders.ChallengersViewHolder
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.features.post.ActivityPostMedia
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityViewModel
import com.gcox.fansmeet.features.topfans.TopFanActivity
import com.gcox.fansmeet.manager.SocialManager
import org.koin.android.viewmodel.ext.android.viewModel


class ChallengeListFragment : BaseFragment(), UserInfoViewHolder.OnClickListener,
    ChallengersViewHolder.OnClickListener,
    TopFansViewHolder.OnClickListener {


    private val celebrityViewModel: CelebrityViewModel by viewModel()

    private var adapter: CelebrityProfileAdapter? = null
    private var userId: Int? = 0
    private val listCelebrity = arrayListOf<DisplayableItem>()

    companion object {
        private const val USER_ID = "userId"
        @JvmStatic
        fun newInstance(userId: Int): ChallengeListFragment {
            val f = ChallengeListFragment()
            val b = Bundle()
            b.putInt(USER_ID, userId)
            f.arguments = b
            return f
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
        userId = arguments?.getInt(USER_ID)
        observeData()
        if (userId != null) {
            showDialog()
            celebrityViewModel.getCelebrityProfile(userId!!)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {

        if (isVisibleToUser && isResumed && userId != null) {
        }
    }

    override fun onImageClicked(item: FansModel) {
    }

    override fun onFollowClicked(item: CelebrityProfileModel) {
    }

    override fun onImageClicked(item: CelebrityModel) {
    }

    override fun onJoinChallengeClicked(item: CelebrityModel) {

        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                context!!,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        val intentGift = ActivityPostMedia.createIntent(context, item.id!!, 1)
        ActivityCompat.startActivityForResult(
            activity!!,
            intentGift,
            Constants.REQUEST_CHALLENGE_DETAIL_ACTIVITY,
            options.toBundle()
        )
    }

    override fun onShareClicked(item: CelebrityModel, position: Int) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                SocialManager.getInstance().getShareContent(
                    context?.applicationContext,
                    item.userName,
                    "",
                    SocialManager.SHARE_TYPE_POST,
                    false
                ) + item.webPostUrl
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share_via)))
    }

    override fun onViewMoreClicked() {
        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                context!!,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
        val intentGift = TopFanActivity.newIntent(context!!, 62)
        startActivityForResult(intentGift, Constants.REQUEST_TOPFAN_ACTIVITY, options.toBundle())
    }

    private fun observeData() {
        celebrityViewModel.getError().observe(this, Observer {
            dismissDialog()
        })

        celebrityViewModel.getCelebrity.observe(this, Observer {
            listCelebrity.addAll(it?.result!!)
            adapter?.notifyDataSetChanged()
            dismissDialog()
        })

        celebrityViewModel.getCelebrityProfile.observe(this, Observer {
            listCelebrity.add(it!!)
            listCelebrity.add(FansModel())
            adapter?.notifyDataSetChanged()
            celebrityViewModel.getCelebrityList(userId!!, "", 1)
        })
    }

    private fun createAdapter() {
        adapter = CelebrityProfileAdapter(null, listCelebrity, this, this, this)
        recyclerView.adapter = adapter
    }

    private fun setupList(): List<DisplayableItem> {

        listCelebrity.add(UserModel())
        listCelebrity.add(FansModel())

        return listCelebrity
    }
}