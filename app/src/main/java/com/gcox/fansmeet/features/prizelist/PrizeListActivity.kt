package com.gcox.fansmeet.features.prizelist

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.activity.BaseActivity
import com.gcox.fansmeet.core.activity.HandleErrorActivity
import com.gcox.fansmeet.features.prizelist.adapter.SectionsPagerAdapter
import com.gcox.fansmeet.features.prizelist.models.BoxesModel
import com.gcox.fansmeet.util.DialogManager
import kotlinx.android.synthetic.main.activity_prize_list.*
import org.koin.android.viewmodel.ext.android.viewModel


class PrizeListActivity : HandleErrorActivity() {

    private val prizeListActivityViewModel: PrizeListActivityViewModel by viewModel()

    private var ownerId: Int? = 1

    companion object {
        const val ARG_CELEB_NAME = "celeb-name"
        const val ARG_CELEB_USER_ID = "celeb-id"
        @JvmStatic
        fun createIntent(context: Context, userId: Int?, celebName: String?) =
            Intent(context, PrizeListActivity::class.java).apply {
                putExtra(ARG_CELEB_NAME, celebName)
                putExtra(ARG_CELEB_USER_ID, userId)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize_list)
        setSupportActionBar(toolbar)
        ownerId = intent?.extras?.getInt(ARG_CELEB_USER_ID)

        val prizeTitle =
            String.format("%s %s", intent?.extras?.getString(ARG_CELEB_NAME), getString(R.string.prize_list_title))
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            toolbar.findViewById<TextView>(R.id.toolbar_title).text = prizeTitle
        }
        observeData()
        if (ownerId != null) {
            DialogManager.getInstance().showDialog(this, getString(R.string.connecting_msg))
            prizeListActivityViewModel.getPrizeList(ownerId!!)
        }
    }

    private fun observeData() {

        prizeListActivityViewModel.getError().observe(this, Observer {
            DialogManager.getInstance().dismisDialog()

            it?.let {
                handleError(it.message,it.code)
            }
        })

        prizeListActivityViewModel.getBoxesListUseCase.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                AppsterApplication.mAppPreferences.firstTypeOfBox = it[0].type
                showNothingText(it)
                setupViewPager(it)
            }
            DialogManager.getInstance().dismisDialog()
        })
    }

    private fun setupViewPager(listTab: List<BoxesModel>) {
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                listTab,
                this,
                supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.vpPrize)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    private fun showNothingText(listTab: List<BoxesModel>) {
        if (listTab.isNullOrEmpty()) {
            tvNothing.visibility = View.VISIBLE
            vpPrize.visibility = View.GONE
        } else {
            tvNothing.visibility = View.GONE
            vpPrize.visibility = View.VISIBLE
        }
    }
}
