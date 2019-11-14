package com.gcox.fansmeet.features.postchallenge

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseActivity
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.PostChallengeViewModel
import com.gcox.fansmeet.webservice.request.CreateChallengeRequestModel
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_post_challenge.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.text.InputFilter
import android.text.TextUtils
import com.appster.extensions.decodeEmoji
import com.appster.extensions.loadImg
import com.gcox.fansmeet.customview.InputFilterMinMax
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.util.*
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class PostChallengeActivity : BaseActivity() {

    private val postChallengeViewModel: PostChallengeViewModel by viewModel()
    private var bitmapSend: Bitmap? = null
    private var mediaType = 1
    private var startTime = ""
    private var endedTime = ""
    private var utcYear = 0
    private var utcMonth = 0
    private var utcDay = 0
    private var startTimeUTC = ""
    private var endedTimeUTC = ""
    private var titleBefore = ""
    private var descriptionBefore = ""
    private var prizeBefore = ""
    private var item: CelebrityModel? = null
    private var isEdit: Boolean = false
    private var positionInList: Int = -1

    companion object {
        const val TITLE_LIMITED = 50
        const val DESCRIPTION_LIMITED = 300
        const val PRIZE_LIMITED = 300
        const val CHALLENGE_MODEL = "challenge_model"
        const val POSITION = "position"
        private const val IS_EDITING = "is_editing"
        @JvmStatic
        fun newIntent(context: Context, isEdit: Boolean, item: CelebrityModel, position: Int): Intent {
            val intent = Intent(context, PostChallengeActivity::class.java)
            intent.putExtra(IS_EDITING, isEdit)
            if (isEdit) {
                intent.putExtra(CHALLENGE_MODEL, item)
                intent.putExtra(POSITION, position)
            }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_challenge)
        val bundle = getIntent()?.extras
        if (bundle != null) {
            isEdit = bundle.getBoolean(IS_EDITING)
            if (isEdit) {
                item = bundle.getParcelable(CHALLENGE_MODEL)
                positionInList = bundle.getInt(POSITION)
            }
            if (item != null) initEditingData(item!!)
        }
        init()
    }

    override fun onResume() {
        super.onResume()
        UiUtils.hideSoftKeyboard(this)
    }

    fun init() {
        edMaxSubmission.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "10"))
        iv_backBtn.setOnClickListener { finish() }
        btCreatePost.setOnClickListener { createPost() }
        imAddBanner.setOnClickListener { showPicPopUp() }
        radioButtonText.setOnClickListener { mediaType = Constants.POST_TYPE_QUOTES }
        radioButtonPhoto.setOnClickListener { mediaType = Constants.POST_TYPE_IMAGE }
        radioButtonVideo.setOnClickListener { mediaType = Constants.POST_TYPE_VIDEO }
        tvStartTime.setOnClickListener { showDatePicker(true) }
        tvEndTime.setOnClickListener { showDatePicker(false) }
        observeData()
        rootView.setOnClickListener { UiUtils.hideSoftKeyboard(this) }
        setEdtWatcher()
        if (!isEdit) {
            tvStartTime.text = getCurrentDay()
            tvEndTime.text = getTomorrowDay()
        }
    }

    private fun initEditingData(item: CelebrityModel) {
        if (item.title != null) edtChallengeTitle.setText(item.title?.decodeEmoji())
        when (item.mediaType) {
            Constants.POST_TYPE_QUOTES -> radioButtonText.isChecked = true
            Constants.POST_TYPE_IMAGE -> radioButtonPhoto.isChecked = true
            Constants.POST_TYPE_VIDEO -> radioButtonVideo.isChecked = true
        }
        tvStartTime.text = SetDateTime.partTimeForEditChallenge(item.startedAt, applicationContext)
        tvEndTime.text = SetDateTime.partTimeForEditChallenge(item.endedAt, applicationContext)
        if (item.startedAt != null) startTimeUTC = item.startedAt!!
        if (item.endedAt != null) endedTimeUTC = item.endedAt!!
        edtDescription.setText(item.description?.decodeEmoji())
        edtPrizes.setText(item.prizeText?.decodeEmoji())
        imAddBanner.loadImg(item.image)
        edMaxSubmission.setText(item.maxSubmission.toString())
        btCreatePost.text = getString(R.string.edit)
        pageTitle.text = getString(R.string.edit_challenge_title)
    }

    private fun setEdtWatcher() {
        compositeDisposable.add(
            RxTextView.textChangeEvents(edtChallengeTitle)
                .observeOn(Schedulers.computation())
                .debounce(100, TimeUnit.MILLISECONDS) // default Scheduler is Computation
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onChallengeTitleCounterChanged(it.text().toString().trim())
                }, { error -> Timber.e(error) })
        )

        compositeDisposable.add(
            RxTextView.textChangeEvents(edtDescription)
                .observeOn(Schedulers.computation())
                .debounce(100, TimeUnit.MILLISECONDS) // default Scheduler is Computation
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onDescriptionCounterChanged(it.text().toString().trim())
                }, { error -> Timber.e(error) })
        )

        compositeDisposable.add(
            RxTextView.textChangeEvents(edtPrizes)
                .observeOn(Schedulers.computation())
                .debounce(100, TimeUnit.MILLISECONDS) // default Scheduler is Computation
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onPrizeCounterChanged(it.text().toString().trim())
                }, { error -> Timber.e(error) })
        )
    }

    private fun onChallengeTitleCounterChanged(textInput: String) {
        val tex = StringUtil.encodeString(textInput)
        if (tex.length > TITLE_LIMITED) {
            val des = StringUtil.decodeString(titleBefore)
            edtChallengeTitle.setText(des)
            val selection = if (TextUtils.isEmpty(des)) 0 else des.length - 1
            edtChallengeTitle.setSelection(selection)
        } else {
            titleBefore = tex
        }
        val newLength = titleBefore.length
        val counter = "$newLength/$TITLE_LIMITED"
        tvLimitTitle.text = counter
    }

    private fun onDescriptionCounterChanged(textInput: String) {
        val tex = StringUtil.encodeString(textInput)
        if (tex.length > DESCRIPTION_LIMITED) {
            val des = StringUtil.decodeString(descriptionBefore)
            edtDescription.setText(des)
            val selection = if (TextUtils.isEmpty(des)) 0 else des.length - 1
            edtDescription.setSelection(selection)
        } else {
            descriptionBefore = tex
        }
        val newLength = descriptionBefore.length
        val counter = "$newLength/$DESCRIPTION_LIMITED"
        tvLimitDescription.text = counter
    }

    private fun onPrizeCounterChanged(textInput: String) {
        val tex = StringUtil.encodeString(textInput)
        if (tex.length > PRIZE_LIMITED) {
            val des = StringUtil.decodeString(prizeBefore)
            edtPrizes.setText(des)
            val selection = if (TextUtils.isEmpty(des)) 0 else des.length - 1
            edtPrizes.setSelection(selection)
        } else {
            prizeBefore = tex
        }
        val newLength = prizeBefore.length
        val counter = "$newLength/$PRIZE_LIMITED"
        tvLimitPrizes.text = counter
    }

    private fun observeData() {
        postChallengeViewModel.getError().observe(this, Observer {
            dismissDialog()
            handleError(it?.message, it?.code!!)
        })

        postChallengeViewModel.getPostChallengeResponse.observe(this, Observer {
            dismissDialog()
            val intent = getIntent()
            if (isEdit && item != null && intent != null) {
                item!!.title = it?.title
                item!!.prizeText = it?.prizeText
                item!!.description = it?.description
                item!!.entryCount = it?.maxSubmission
                item!!.startedAt = it?.startedAt
                item!!.endedAt = it?.endedAt
                item!!.mediaType = it?.mediaType
                if (bitmapSend != null) item!!.image = it?.mediaImageThumbnail
                intent.putExtra(CHALLENGE_MODEL, item)
                intent.putExtra(POSITION, positionInList)
                setResult(Activity.RESULT_OK, intent)
                Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_LONG).show()
            } else {
                setResult(Activity.RESULT_OK)
            }

            finish()
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        if (bitmapSend != null && !bitmapSend!!.isRecycled) bitmapSend!!.recycle()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BaseToolBarActivity.RESULT_OK) {
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
                            fileUri = resultUri
                            onImageChanged(fileUri)
                        } else {
                            Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                Constants.REQUEST_PIC_FROM_CAMERA -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        onImageChanged(fileUri)
                    }
                }
            }
        }
    }

    private fun onImageChanged(fileUri: Uri) {
        bitmapSend = Utils.getBitmapFromURi(this, fileUri)
        if (bitmapSend != null) {
            imAddBanner.setImageBitmap(
                Bitmap.createScaledBitmap(
                    bitmapSend,
                    Constants.BITMAP_THUMBNAIL_WIDTH, Constants.BITMAP_THUMBNAIL_HEIGHT, false
                )
            )
        }
    }

    private fun getCurrentDay(): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val sdf = SimpleDateFormat("d MMMM yyyy, 00:00", Locale.getDefault())
        calendar.set(currentYear, currentMonth, currentDay)
        utcYear = currentYear
        utcMonth = currentMonth
        utcDay = currentDay + 1
        getTimeZone(true, utcYear, utcMonth, utcDay, 0, 0)
        return sdf.format(calendar.time)
    }

    private fun getTomorrowDay(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        val dateFormat = SimpleDateFormat("d MMMM yyyy, 00:00", Locale.getDefault())
        utcYear = calendar.get(Calendar.YEAR)
        utcMonth = calendar.get(Calendar.MONTH)
        utcDay = calendar.get(Calendar.DAY_OF_MONTH) + 1
        getTimeZone(false, utcYear, utcMonth, utcDay, 0, 0)
        return dateFormat.format(tomorrow)
    }

    private fun showDatePicker(isStartDate: Boolean) {
        // Get Current Date
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.set(year, monthOfYear, dayOfMonth)
                val dateString = sdf.format(calendar.time)
                if (isStartDate)
                    startTime = dateString
                else
                    endedTime = dateString

                utcYear = year
                utcMonth = monthOfYear
                utcDay = dayOfMonth
                showTimerPicker(isStartDate)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    private fun showTimerPicker(isStartDate: Boolean) {
        // Get Current Time
        val c = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMinute = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

                var hour = hourOfDay.toString()
                if (hourOfDay < 10) {
                    hour = "0$hourOfDay"
                }
                var min = minute.toString()
                if (minute < 10)
                    min = "0$minute"
                val timeString = " $hour:$min"
                if (isStartDate) {
                    startTime = "$startTime, $timeString"
                    tvStartTime.text = startTime
                } else {
                    endedTime = "$endedTime, $timeString"
                    tvEndTime.text = endedTime
                }

                getTimeZone(isStartDate, utcYear, utcMonth, utcDay, hourOfDay, minute)
            },
            mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    private fun getTimeZone(isStartTime: Boolean, year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val sdf = SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss'.000Z'", Locale.getDefault())
        val date1 = GregorianCalendar(year, month, day, hour, minute, 0)
        val utcTimeStamp = TimeUtility.toUTC(date1.timeInMillis, date1.timeZone)
        val utcCal = Calendar.getInstance()
        utcCal.timeInMillis = utcTimeStamp

        if (isStartTime) startTimeUTC = sdf.format(utcCal.time)
        else endedTimeUTC = sdf.format(utcCal.time)
    }

    private fun createPost() {
        val title = edtChallengeTitle.text.toString().trim()
        if (title.isEmpty()) {
            Toast.makeText(applicationContext, "Please add Title!", Toast.LENGTH_SHORT).show()
            return
        }
        val description = edtDescription.text.toString().trim()
        if (description.isEmpty()) {
            Toast.makeText(applicationContext, "Please add Description!", Toast.LENGTH_SHORT).show()
            return
        }
        val prizes = edtPrizes.text.toString().trim()
        if (prizes.isEmpty()) {
            Toast.makeText(applicationContext, "Please add Prizes!", Toast.LENGTH_SHORT).show()
            return
        }
        val maxSubmission = edMaxSubmission.text.toString()
        var parsedInt = 0
        maxSubmission.toIntOrNull()?.let {
            parsedInt = maxSubmission.toInt()
        }
        if (parsedInt < 0) {
            Toast.makeText(applicationContext, "Please add Submission number!", Toast.LENGTH_SHORT).show()
            return
        }
        if (startTimeUTC.isEmpty()) {
            Toast.makeText(applicationContext, "Please input start time!", Toast.LENGTH_SHORT).show()
            return
        }
        if (endedTimeUTC.isEmpty()) {
            Toast.makeText(applicationContext, "Please input ended time!", Toast.LENGTH_SHORT).show()
            return
        }
        if (bitmapSend == null && !isEdit) {
            Toast.makeText(applicationContext, "Please add banner photo!", Toast.LENGTH_SHORT).show()
            return
        }

        var fileSend: File? = null
        if (bitmapSend != null) {
            fileSend = Utils.getFileFromBitMap(this, bitmapSend)
        }

        val request = CreateChallengeRequestModel(
            "",
            1.0,
            1.0,
            StringUtil.encodeString(title),
            mediaType,
            fileSend,
            startTimeUTC,
            endedTimeUTC,
            StringUtil.encodeString(description),
            StringUtil.encodeString(prizes),
            parsedInt
        )
        showDialog(this, getString(R.string.connecting_msg))
        postChallengeViewModel.submissionChallenge(request.build(), isEdit, if (isEdit) item?.id!! else 0)
    }
}