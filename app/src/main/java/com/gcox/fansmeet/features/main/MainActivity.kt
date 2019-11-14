package com.gcox.fansmeet.features.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.*
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.customview.spacenavigationview.SpaceItem
import com.gcox.fansmeet.customview.spacenavigationview.SpaceOnClickListener
import com.gcox.fansmeet.features.challenges.ChallengesScreenFragment
import com.gcox.fansmeet.features.home.HomeScreenFragment
import com.gcox.fansmeet.features.notification.NotificationContentFragment
import com.gcox.fansmeet.features.post.ActivityPostMedia
import com.gcox.fansmeet.features.post.BundleMedia
import com.gcox.fansmeet.features.postchallenge.PostChallengeActivity
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.features.profile.userprofile.FragmentMe
import com.gcox.fansmeet.features.search.SearchScreenFragment
import com.gcox.fansmeet.pushnotification.NotificationPushModel
import com.gcox.fansmeet.services.CheckTransactionPurchaService
import com.gcox.fansmeet.services.RefreshFollowerListService
import com.gcox.fansmeet.util.AnimatorUtils
import com.gcox.fansmeet.util.FileUtility
import com.gcox.fansmeet.util.Utils
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File
import java.util.ArrayList

class MainActivity : BaseToolBarActivity(), View.OnClickListener {

    private var fragmentHome: HomeScreenFragment? = null
    private var challengesFragment: ChallengesScreenFragment? = null
    private var profileFragment: SearchScreenFragment? = null
    private var searchFragment: SearchScreenFragment? = null
    internal var notifyFragment: NotificationContentFragment? = null
    internal var fragmentMe: FragmentMe? = null
    private var mAdapter: TabsPagerAdapter? = null
    private var currentTabId = 0
    private var animList: MutableList<Animator> = ArrayList()
    internal var animSet: AnimatorSet? = null
    private var doubleBackToExitPressedOnce = false
    private val mainViewModel: MainViewModel by viewModel()

    companion object {
        private const val TIME_INTERVAL = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeData()
        checkVerifyIAP()
        snvBottomBar.initWithSaveInstanceState(savedInstanceState)
        configNavigationView()
        if (AppsterApplication.mAppPreferences.userModel.type == UserType.NORMAL_USER) hideChallengeAction()
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this)
//        }
    }

    override fun getLayoutContentId(): Int {
        return R.layout.activity_main
    }

    override fun onResume() {
        super.onResume()
        if (RefreshFollowerListService.shouldRefreshFollowerList()) {
            startService(Intent(this, RefreshFollowerListService::class.java))
        }

//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this)
//        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.e("onNewIntent")
        notificationSwitchTo(intent)
    }

    override fun init() {
        initViewPager()
        setupClickBottomBar()
        setToolbarColor(ContextCompat.getColor(applicationContext, R.color.bottom_bar_background_color))
        menuLayout.setOnClickListener(this)
        btn_arc_comment.setOnClickListener(this)
        btn_arc_image.setOnClickListener(this)
        btn_arc_video.setOnClickListener(this)
        btn_arc_challenge.setOnClickListener(this)
        notificationSwitchTo(intent)

    }

    private fun observeData() {
        mainViewModel.getError().observe(this, Observer {
            dismissDialog()
        })

        mainViewModel.getiAPIsfinishedCheking.observe(this, Observer {
            //            if (it != null && !it) {
//                val model = AppsterApplication.mAppPreferences.verifyIAPRequestModel
//                if (model != null) {
//                    mainViewModel.verifyIAPPurchased(model)
//                }
//            }
        })

        mainViewModel.verifyIAPPurchased.observe(this, Observer {
            if (it != null) {
                Timber.e("Bean =" + it.totalBeanIncrease)
                AppsterApplication.mAppPreferences.userModel.gems = it.totalBeanIncrease
            }
        })
    }

    private fun notificationSwitchTo(intent: Intent?) {
        val bundle = intent?.extras
        if (bundle != null) {
            val notificationEntity =
                bundle.getParcelable<NotificationPushModel>(ConstantBundleKey.BUNDLE_NOTIFICATION_KEY)
            if (notificationEntity != null) {
                redirectNotificationShowing(notificationEntity)
            }
        }
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (handleCloseOpenDraw()) {
            // Handle Open and close menu slider in base activity
        } else if (fm.backStackEntryCount > 0) {
            fm.popBackStack()
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                finish()
                return
            }

            doubleBackToExitPressedOnce = true
            Toast.makeText(applicationContext, getString(R.string.Please_click_back_again_to_exit), Toast.LENGTH_SHORT)
                .show()

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, TIME_INTERVAL.toLong())
        }
    }

    private fun initViewPager() {
        mAdapter = TabsPagerAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit = 4
        viewPager.adapter = mAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position != AppNavigation.PROFILE) invisibleImageEditProfile()
                when (position) {
                    AppNavigation.SEARCH -> setTopBarTile(getString(R.string.tab_bar_notification_title))
                    AppNavigation.CHALLENGES -> setTopBarTile(getString(R.string.tab_bar_challenge_title))
                    AppNavigation.PROFILE -> {
                        setImageEditProfile()
                        usedToolbarImage()
                    }
                    else -> usedToolbarImage()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun configNavigationView() {
        snvBottomBar.addSpaceItem(
            AppNavigation.HOME,
            SpaceItem(getString(R.string.bottom_bar_home), mBeLiveThemeHelper.navHomeIcon)
        )
        snvBottomBar.addSpaceItem(
            AppNavigation.CHALLENGES,
            SpaceItem(getString(R.string.tab_bar_challenge_title), mBeLiveThemeHelper.navChallengesIcon)
        )
        snvBottomBar.addSpaceItem(
            AppNavigation.SEARCH,
            SpaceItem(getString(R.string.tab_bar_notification_title), mBeLiveThemeHelper.navNotificationIcon)
        )
        snvBottomBar.addSpaceItem(
            AppNavigation.PROFILE,
            SpaceItem(getString(R.string.bottom_bar_profile), mBeLiveThemeHelper.navProfileIcon)
        )
        snvBottomBar.shouldShowFullBadgeText(true)
        snvBottomBar.setCentreButtonIconColorFilterEnabled(false)
    }

    private fun hideChallengeAction() {
        btn_arc_challenge.visibility = View.GONE
        arcLayout.invalidate()
    }

    private fun setupBottomBarForInfluencerUer() {
        snvBottomBar.hideCancelButton()
        snvBottomBar.invalidate()
    }

    private fun setupClickBottomBar() {

        snvBottomBar.setSpaceOnClickListener(object : SpaceOnClickListener {

            override fun onItemReselected(itemIndex: Int, itemName: String?) {
                hideMenu()
            }

            override fun onCentreButtonClick() {
                onActionButtonClicked()
            }

            override fun onItemClick(@AppNavigation itemIndex: Int, itemName: String) {
                hideMenu()
                handleClickBottomBar(itemIndex, itemName)
            }
        })
        //        handleStopVideosPlayer();
    }

    private fun handleClickBottomBar(@AppNavigation itemIndex: Int, itemName: String) {
        when (itemIndex) {
            AppNavigation.HOME -> {
                onHomeTabClicked()
            }
            AppNavigation.CHALLENGES -> {
                onChallengesClicked()

            }
            AppNavigation.SEARCH -> {
                onSearchClicked()
            }
            AppNavigation.PROFILE -> {
                onProfileClicked()
            }

            AppNavigation.ACTIONS -> {
            }

            else -> {
            }
        }
    }

    private fun onProfileClicked() {
        if (currentTabId == AppNavigation.PROFILE) {
            // Todo
        } else {

            viewPager.setCurrentItem(AppNavigation.PROFILE, false)
            currentTabId = AppNavigation.PROFILE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_PIC_FROM_LIBRARY -> {
                    val imageCroppedURI: Uri
                    try {
                        imageCroppedURI = getOutputMediaFileUri(FileUtility.MEDIA_TYPE_IMAGE_CROPPED)
                    } catch (e: NullPointerException) {
                        Timber.d(e)
                        return
                    }

                    if (data?.data != null) {
                        fileUri = data.data
                        performCrop(fileUri, imageCroppedURI)
                    }
                }

                Constants.REQUEST_PIC_FROM_CROP -> {
                    if (data != null) {
                        val resultUri = UCrop.getOutput(data)
                        if (resultUri != null) {
                            startPostWithMedia(resultUri, Constants.POST_TYPE_IMAGE)
                        } else {
                            Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                Constants.REQUEST_PIC_FROM_CAMERA -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        startPostWithMedia(fileUri, Constants.POST_TYPE_IMAGE)
                    }
                }

                Constants.CAMERA_VIDEO_REQUEST -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        Timber.e("mRecordUrl $fileUri")
                        startPostWithMedia(fileUri, Constants.POST_TYPE_VIDEO)
                    }
                }

                Constants.PICK_VIDEO_FROM_LIBRARY_REQUEST -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        loadVideoAfterPickFromGallery(fileUri)
                    }
                }

                Constants.VIDEO_TRIMMED_REQUEST -> {
                    if (data != null) {
                        val urlImage = data.getStringExtra(Constants.VIDEO_PATH)
                        val image = Uri.fromFile(File(urlImage))
                        startPostWithMedia(image, Constants.POST_TYPE_VIDEO)
                    }
                }

                Constants.POST_REQUEST, Constants.POST_CHALLENGE_REQUEST -> {
                    if (fragmentMe != null) {
                        fragmentMe?.onActivityResult(requestCode, resultCode, data)
                    }

                    if (viewPager.currentItem != AppNavigation.PROFILE && snvBottomBar != null) {
                        snvBottomBar.changeCurrentItem(AppNavigation.PROFILE)
                    }

                    currentTabId = AppNavigation.PROFILE
                }
            }
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.menuLayout -> {
                hideMenu()
            }

            R.id.btn_arc_comment -> {
                hideMenu()
                startPostWithMedia(Uri.parse(""), Constants.POST_TYPE_QUOTES)
            }

            R.id.btn_arc_image -> {
                hideMenu()
                showPicPopUp()
            }

            R.id.btn_arc_video -> {
                hideMenu()
                showVideosPopUp()
            }

            R.id.btn_arc_challenge -> {
                hideMenu()
                val intent = PostChallengeActivity.newIntent(this, false, CelebrityModel(), -1)
                val options =
                    ActivityOptionsCompat.makeCustomAnimation(this, R.anim.push_in_to_right, R.anim.push_in_to_left)
                ActivityCompat.startActivityForResult(
                    this,
                    intent,
                    Constants.POST_CHALLENGE_REQUEST,
                    options.toBundle()
                )
            }
        }
    }

    private fun onSearchClicked() {
        if (currentTabId == AppNavigation.SEARCH) {
            // Todo

        } else {

            viewPager.setCurrentItem(AppNavigation.SEARCH, false)
            currentTabId = AppNavigation.SEARCH
        }
    }

    private fun onChallengesClicked() {
        if (currentTabId == AppNavigation.CHALLENGES) {
            // Todo
        } else {

            viewPager.setCurrentItem(AppNavigation.CHALLENGES, false)
            currentTabId = AppNavigation.CHALLENGES
        }

    }

    private fun onHomeTabClicked() {
        if (currentTabId == AppNavigation.HOME) {
            // Todo
        } else {

            viewPager.setCurrentItem(AppNavigation.HOME, false)
            currentTabId = AppNavigation.HOME
        }

    }

    internal fun onActionButtonClicked() {
//        val options = ActivityOptionsCompat
//            .makeCustomAnimation(this, R.anim.push_in_to_right, R.anim.push_in_to_left)
//        val intent = Intent(this@MainActivity, ActivityPostMedia::class.java)
//        intent.putExtra(ConstantBundleKey.BUNDLE_TYPE_KEY, CommonDefine.TYPE_QUOTES)
//        ActivityCompat.startActivityForResult(this, intent, Constants.POST_REQUEST, options.toBundle())

        if (!snvBottomBar.isCenterButtonSelected && currentTabId == AppNavigation.ACTIONS) {
            hideMenu()
        } else {
            showMenu()
            currentTabId = AppNavigation.ACTIONS
        }
    }

    private fun hideMenu() {

        val animList = ArrayList<Animator>()

        for (i in arcLayout.childCount - 1 downTo 0) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i))!!)
        }

        val animSetHide = AnimatorSet()
        animSetHide.duration = 400
        animSetHide.interpolator = AnticipateInterpolator()
        animSetHide.playTogether(animList)
        animSetHide.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                snvBottomBar.setIsCenterButtonSelected(false)
                if (animSet != null && !animSet!!.isRunning) {
                    menuLayout.visibility = View.INVISIBLE
                    //                    mBtnComposer.setImageResource(R.drawable.bottom_write);
                    //                    mBtnComposer.setSelected(false);
                }
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                if (animSet != null && animSet!!.isRunning) {
                    animSetHide.cancel()
                }
            }
        })
        animSetHide.start()

    }

    private fun createHideItemAnimator(item: View): Animator? {
        if (snvBottomBar == null || viewPager == null) return null
        val dx = (snvBottomBar.right - Utils.dpToPx(56f)) / 2 - item.x
        val dy = viewPager.height - item.y
        val anim = ObjectAnimator.ofPropertyValuesHolder(
            item,
            AnimatorUtils.rotation(720f, 0f),
            AnimatorUtils.translationX(0f, dx),
            AnimatorUtils.translationY(0f, dy)
        )

        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                item.translationX = 0f
                item.translationY = 0f
            }
        })

        return anim
    }

    private fun showMenu() {
        menuLayout.visibility = View.VISIBLE

        if (animList.size == 0) {
            var i = 0
            val len = arcLayout.childCount
            while (i < len) {
                animList.add(createShowItemAnimator(arcLayout.getChildAt(i))!!)
                i++
            }
        }
        if (animSet == null) {
            animSet = AnimatorSet()
            animSet!!.duration = 400
            animSet!!.interpolator = OvershootInterpolator()
            animSet!!.playTogether(animList)
        }

        animSet!!.cancel()
        animSet!!.start()
    }

    private fun createShowItemAnimator(item: View): Animator? {
        if (snvBottomBar == null || viewPager == null) return null
        val dx = (snvBottomBar.right - Utils.dpToPx(56f)) / 2 - item.x
        val dy = viewPager.height - item.y
        item.rotation = 0f
        item.translationX = dx
        item.translationY = dy

        return ObjectAnimator.ofPropertyValuesHolder(
            item,
            AnimatorUtils.rotation(0f, 720f),
            AnimatorUtils.translationX(dx, 0f),
            AnimatorUtils.translationY(dy, 0f)
        )
    }

    private fun startPostWithMedia(fileUri: Uri, Type: Int) {
        val intent = Intent(this@MainActivity, ActivityPostMedia::class.java)
        val bundleMedia = BundleMedia()
        bundleMedia.setIsPost(true)
        bundleMedia.key = ConstantBundleKey.BUNDLE_MEDIA_KEY
        bundleMedia.type = Type
        bundleMedia.uriPath = fileUri.toString()
        bundleMedia.setIntent(intent)

        val options =
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.push_in_to_right, R.anim.push_in_to_left)
        ActivityCompat.startActivityForResult(this, intent, Constants.POST_REQUEST, options.toBundle())
    }

    private fun checkVerifyIAP() {
//        val model = AppsterApplication.mAppPreferences.verifyIAPRequestModel
//        if (model?.orderId != null) {
//            mainViewModel.iAPIsfinishedChecking(model.orderId)
//        }

        val orderIds = AppsterApplication.mAppPreferences.loadOrderObjectToList()
        if (orderIds != null && orderIds.size > 0) {
            for (i in orderIds.indices) {
                mainViewModel.verifyIAPPurchased(orderIds[i])
            }
        }

        Timber.e("orderIds %s", orderIds.size)
    }

    internal inner class TabsPagerAdapter
        (fmg: FragmentManager) : FragmentStatePagerAdapter(fmg) {

        override fun getItemPosition(`object`: Any): Int {

            return super.getItemPosition(`object`)
        }

        override fun getItem(index: Int): Fragment? {
            when (index) {
                AppNavigation.HOME -> {
                    if (fragmentHome == null) {
                        fragmentHome = HomeScreenFragment.newInstance()
                    }
                    return fragmentHome
                }

                AppNavigation.SEARCH -> {
                    if (notifyFragment == null) {
                        notifyFragment = NotificationContentFragment.newInstance()
                    }
                    return notifyFragment
                }

                AppNavigation.CHALLENGES -> {
                    if (challengesFragment == null) {
                        challengesFragment = ChallengesScreenFragment.newInstance()
                    }
                    return challengesFragment
                }

                AppNavigation.PROFILE -> {
                    if (fragmentMe == null) {
                        fragmentMe = FragmentMe.getInstance(
                            AppsterApplication.mAppPreferences.userModel.userId,
                            AppsterApplication.mAppPreferences.userModel.userName!!, false
                        )
                    }
                    return fragmentMe
                }
            }

            return null
        }

        override fun getCount(): Int {
            return 4
        }

    }
}
