package com.gcox.fansmeet.features.editvideo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation;
import com.gcox.fansmeet.features.editvideo.recordclip.RecordProgressController;
import com.gcox.fansmeet.util.BitmapUtil;
import com.gcox.fansmeet.util.StringUtil;
import com.ksyun.media.shortvideo.kit.KSYRecordKit;
import com.ksyun.media.shortvideo.utils.FileUtils;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.filter.audio.AudioFilterBase;
import com.ksyun.media.streamer.filter.audio.AudioReverbFilter;
import com.ksyun.media.streamer.filter.audio.KSYAudioEffectFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyProFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyToneCurveFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Record ShortVideo
 */

public class RecordActivity extends Activity implements
        ActivityCompat.OnRequestPermissionsResultCallback {
    private static String TAG = "RecordActivity";

    public static final int MAX_DURATION = Constants.MAX_VIDEO_DURATION_TIME * 1000;  //最长拍摄时长
    public static final int MIN_DURATION = Constants.MIN_VIDEO_DURATION_TIME * 1000;  //最短拍摄时长
    private static final int REQUEST_CODE = 10010;
    private static final int AUDIO_FILTER_DISABLE = 0;

    private static final int INDEX_BEAUTY_TITLE_BASE = 0;
    private static final int INDEX_BGM_TITLE_BASE = 10;
    private static final int INDEX_BGM_ITEM_BASE = 0;
    private static final int INDEX_SOUND_EFFECT_BASE = 10;

    private int mAudioEffectType = AUDIO_FILTER_DISABLE;
    private int mAudioReverbType = AUDIO_FILTER_DISABLE;

    private GLSurfaceView mCameraPreviewView;
    //private TextureView mCameraPreviewView;
    private CameraHintView mCameraHintView;
    private Chronometer mChronometer;
    private View mSwitchCameraView;
    private View mFlashView;
    private View mCloseCameraView;
    private RelativeLayout mPreviewLayout;
    private RelativeLayout mBarBottomLayout;
    private ImageView mRecordView;
    private ImageView mBackView;
    private ImageView mNextView;
    private CheckBox mFrontMirrorCheckBox;
    private ImageView mBgmMusicView;
    private ImageView mBeautyView;
    private CheckBox mWaterMarkCheckBox;
    private View mDefaultRecordBottomLayout;
    private View mBeautyLayout;
    private View mBgmLayout;
    private TextView mBeautyFilter;
    private TextView mDynSticker;
    //背景音乐和音效标题栏控件集合
    private TextView[] mSoundEffectTitle;
    private ImageView mPitchMinus;
    private ImageView mPitchPlus;
    private TextView mPitchText;
    //背景音乐和音效布局集合
    private View[] mSoundEffectLayout;
    private ImageView mCancelBgm;
    private ImageView mImportBgm;
    private ImageView mCancelSoundChange;
    private ImageView mCancelReverberation;
    private ImageView mBeautyBack;
    private ImageView mBgmBack;

    private DownloadAndHandleTask mBgmLoadTask;
    private Thread mInitFaceunityThread;

    private AppCompatSeekBar mMicAudioVolumeSeekBar;
    private AppCompatSeekBar mBgmVolumeSeekBar;

    private View mBeautyChooseView;
    private AppCompatSpinner mBeautySpinner;
    private LinearLayout mBeautyGrindLayout;
    private TextView mGrindText;
    private AppCompatSeekBar mGrindSeekBar;
    private LinearLayout mBeautyWhitenLayout;
    private TextView mWhitenText;
    private AppCompatSeekBar mWhitenSeekBar;
    private LinearLayout mBeautyRuddyLayout;
    private TextView mRuddyText;
    private AppCompatSeekBar mRuddySeekBar;

    //断点拍摄进度控制
    private RecordProgressController mRecordProgressCtl;

    private View mStickerChooseview;
    private AppCompatSpinner mStickerSpinner;
    private ImgFaceunityFilter mImgFaceunityFilter;

    private ButtonObserver mObserverButton;
    private BgmButtonObserver mBgmButtonObserver;
    private CheckBoxObserver mCheckBoxObserver;
    private SeekBarChangedObserver mSeekBarChangedObsesrver;

    private KSYRecordKit mKSYRecordKit;
    private Handler mMainHandler;

    private boolean mIsFileRecording = false;
    private boolean mIsFlashOpened = false;
    private String mRecordUrl;

    private int mPitchValue = 0;
    private int mPreBeautyTitleIndex = 0;
    private int mPreBgmTitleIndex = 0;
    private int mPreBgmItemIndex = 0;
    private int mPreBgmEffectIndex = 0;
    private int mPreBgmReverbIndex = 0;
    private static final int[] SOUND_EFFECT_CONST = {KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_MALE, KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_FEMALE,
            KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_HEROIC, KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_ROBOT,
            AudioReverbFilter.AUDIO_REVERB_LEVEL_1, AudioReverbFilter.AUDIO_REVERB_LEVEL_3,
            AudioReverbFilter.AUDIO_REVERB_LEVEL_4, AudioReverbFilter.AUDIO_REVERB_LEVEL_2};

    //美颜、背景音乐标题和布局自定义内容集合
    private SparseArray<BottomTitleViewInfo> mRecordTitleArray = new SparseArray<>();
    private SparseArray<BgmItemViewHolder> mBgmEffectArray = new SparseArray<>();

    private String mLogoPath = "assets://KSYLogo/logo.png";

    private boolean mHWEncoderUnsupported;
    private boolean mSWEncoderUnsupported;
    private String[] mBgmLoadPath = {"https://ks3-cn-beijing.ksyun.com/ksy.vcloud.sdk/ShortVideo/faded.mp3",
            "https://ks3-cn-beijing.ksyun.com/ksy.vcloud.sdk/ShortVideo/Hotel_California.mp3",
            "https://ks3-cn-beijing.ksyun.com/ksy.vcloud.sdk/ShortVideo/Immortals.mp3"};
    private final static int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;

    public final static String MEDIA_PLAYER_CONFIG = "media_player_config";
    public final static String REQUEST_BY_CODE = "request_by_code";

    private int requestByCode;
    CompositeDisposable compositeDisposable;
    RxPermissions mRxPermissions;


    public static void startActivity(Fragment fragment, int requestCode, ShortVideoConfig config) {
        Intent intent = new Intent(fragment.getContext(), RecordActivity.class);
        if (config == null) {
            config = createConfig();
        }
        intent.putExtra(MEDIA_PLAYER_CONFIG, config);
        intent.putExtra(REQUEST_BY_CODE, requestCode);
        fragment.startActivityForResult(intent, requestCode);
    }


    public static void startActivity(Activity activity, int requestCode, ShortVideoConfig config) {
        Intent intent = new Intent(activity, RecordActivity.class);
        if (config == null) {
            config = createConfig();
        }
        intent.putExtra(MEDIA_PLAYER_CONFIG, config);
        intent.putExtra(REQUEST_BY_CODE, requestCode);
        activity.startActivityForResult(intent, requestCode);
    }

    public static ShortVideoConfig createConfig() {
        ShortVideoConfig shortVideoConfig = new ShortVideoConfig();

        shortVideoConfig.fps = 15;
        shortVideoConfig.videoBitrate = 1000;
        shortVideoConfig.audioBitrate = 64;

        shortVideoConfig.resolution = StreamerConstants.VIDEO_RESOLUTION_540P;
        shortVideoConfig.encodeType = AVConst.CODEC_ID_AVC;
        shortVideoConfig.encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_BALANCE;
        shortVideoConfig.encodeMethod = StreamerConstants.ENCODE_METHOD_SOFTWARE;
        return shortVideoConfig;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.record_acitvity);

        //must set
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //默认设置为竖屏，当前暂时只支持竖屏，后期完善
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        mObserverButton = new ButtonObserver();
        mBgmButtonObserver = new BgmButtonObserver();
        mCheckBoxObserver = new CheckBoxObserver();
        mSeekBarChangedObsesrver = new SeekBarChangedObserver();
        mSwitchCameraView = findViewById(R.id.switch_cam);
        mSwitchCameraView.setOnClickListener(mObserverButton);
        mCameraHintView = (CameraHintView) findViewById(R.id.camera_hint);
        mFlashView = findViewById(R.id.flash);
        mCloseCameraView = findViewById(R.id.button_close_camera_view);


        mFlashView.setOnClickListener(mObserverButton);
        mCloseCameraView.setOnClickListener(mObserverButton);
        mPreviewLayout = (RelativeLayout) findViewById(R.id.preview_layout);
        mBarBottomLayout = (RelativeLayout) findViewById(R.id.bar_bottom);
        mCameraPreviewView = (GLSurfaceView) findViewById(R.id.camera_preview);
        //美颜及背景音乐界面控件
        mDefaultRecordBottomLayout = findViewById(R.id.default_bottom_layout);
        mBeautyView = (ImageView) findViewById(R.id.record_beauty);
        mBeautyView.setOnClickListener(mObserverButton);
        mBgmMusicView = (ImageView) findViewById(R.id.record_bgm);
        mBgmMusicView.setOnClickListener(mObserverButton);
        mBeautyLayout = findViewById(R.id.item_beauty_select);
        mBgmLayout = findViewById(R.id.item_bgm_select);
        mBeautyFilter = (TextView) findViewById(R.id.item_beauty);
        mDynSticker = (TextView) findViewById(R.id.item_dyn_sticker);
        mBeautyBack = (ImageView) findViewById(R.id.item_beauty_back);
        mBeautyBack.setOnClickListener(mObserverButton);
        mBgmBack = (ImageView) findViewById(R.id.item_bgm_back);
        mBgmBack.setOnClickListener(mObserverButton);
        mFrontMirrorCheckBox = (CheckBox) findViewById(R.id.record_front_mirror);
        mFrontMirrorCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);
        mWaterMarkCheckBox = (CheckBox) findViewById(R.id.record_watermark);
        mWaterMarkCheckBox.setOnCheckedChangeListener(mCheckBoxObserver);
        mMicAudioVolumeSeekBar = (AppCompatSeekBar) findViewById(R.id.record_mic_audio_volume);
        mMicAudioVolumeSeekBar.setOnSeekBarChangeListener(mSeekBarChangedObsesrver);
        mBgmVolumeSeekBar = (AppCompatSeekBar) findViewById(R.id.record_music_audio_volume);
        mBgmVolumeSeekBar.setOnSeekBarChangeListener(mSeekBarChangedObsesrver);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mRecordView = (ImageView) findViewById(R.id.click_to_record);
        mRecordView.getDrawable().setLevel(1);
        mRecordView.setOnClickListener(mObserverButton);
        mBackView = (ImageView) findViewById(R.id.click_to_back);
        mBackView.setOnClickListener(mObserverButton);
        mNextView = (ImageView) findViewById(R.id.click_to_next);
        mNextView.setOnClickListener(mObserverButton);

        int width = screenWidth;
        int height = screenWidth;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth,
                height);
        mPreviewLayout.setLayoutParams(params);

        params = new LinearLayout.LayoutParams(screenWidth,
                screenHeight - height);
        mBarBottomLayout.setLayoutParams(params);

        mBeautyChooseView = findViewById(R.id.record_beauty_choose);
        BottomTitleViewInfo mFilterInfo = new BottomTitleViewInfo(mBeautyFilter,
                mBeautyChooseView, mObserverButton);
        mRecordTitleArray.put(INDEX_BEAUTY_TITLE_BASE, mFilterInfo);
        mBeautySpinner = (AppCompatSpinner) findViewById(R.id.beauty_spin);
        mBeautyGrindLayout = (LinearLayout) findViewById(R.id.beauty_grind);
        mGrindText = (TextView) findViewById(R.id.grind_text);
        mGrindSeekBar = (AppCompatSeekBar) findViewById(R.id.grind_seek_bar);
        mBeautyWhitenLayout = (LinearLayout) findViewById(R.id.beauty_whiten);
        mWhitenText = (TextView) findViewById(R.id.whiten_text);
        mWhitenSeekBar = (AppCompatSeekBar) findViewById(R.id.whiten_seek_bar);
        mBeautyRuddyLayout = (LinearLayout) findViewById(R.id.beauty_ruddy);
        mRuddyText = (TextView) findViewById(R.id.ruddy_text);
        mRuddySeekBar = (AppCompatSeekBar) findViewById(R.id.ruddy_seek_bar);

        mStickerChooseview = findViewById(R.id.record_sticker_choose);
        BottomTitleViewInfo mStickerInfo = new BottomTitleViewInfo(mDynSticker,
                mStickerChooseview, mObserverButton);
        mRecordTitleArray.put(INDEX_BEAUTY_TITLE_BASE + 1, mStickerInfo);
        mStickerSpinner = (AppCompatSpinner) findViewById(R.id.sticker_spin);

        //断点拍摄UI初始化
        //mBarBottomLayout为拍摄进度显示的父控件
        mRecordProgressCtl = new RecordProgressController(mBarBottomLayout);
        //拍摄时长变更回调
        mRecordProgressCtl.setRecordingLengthChangedListener(mRecordLengthChangedListener);
        mRecordProgressCtl.start();

//        mBackView.getDrawable().setLevel(1);
        mBackView.setSelected(false);

        //init
        mMainHandler = new Handler();
        mKSYRecordKit = new KSYRecordKit(this);
        requestByCode = getIntent().getExtras().getInt(REQUEST_BY_CODE);
        ShortVideoConfig shortVideoConfig = getIntent().getParcelableExtra(MEDIA_PLAYER_CONFIG);
        setMediaPlayerConfig(shortVideoConfig);

        mKSYRecordKit.setDisplayPreview(mCameraPreviewView);

        mKSYRecordKit.setEnableRepeatLastFrame(false);
        mKSYRecordKit.setCameraFacing(CameraCapture.FACING_FRONT);
        mKSYRecordKit.setFrontCameraMirror(mFrontMirrorCheckBox.isChecked());
        mKSYRecordKit.setOnInfoListener(mOnInfoListener);
        mKSYRecordKit.setOnErrorListener(mOnErrorListener);
        mKSYRecordKit.setOnLogEventListener(mOnLogEventListener);
        initBeautyUI();
        initStickerUI();
        initBgmUI();
        initBottomTitleUI();
        initHideView();
        // touch focus and zoom support
        CameraTouchHelper cameraTouchHelper = new CameraTouchHelper();
        cameraTouchHelper.setCameraCapture(mKSYRecordKit.getCameraCapture());
        mCameraPreviewView.setOnTouchListener(cameraTouchHelper);
        // set CameraHintView to show focus rect and zoom ratio
        cameraTouchHelper.setCameraHintView(mCameraHintView);
        startCameraPreviewWithPermCheck();

        mKSYRecordKit.toggleTorch(false);
        mIsFlashOpened = false;
        mFlashView.setVisibility(View.GONE);
    }

    private int align(int val, int align) {
        return (val + align - 1) / align * align;
    }

    @Override
    public void onResume() {
        super.onResume();

        mKSYRecordKit.setDisplayPreview(mCameraPreviewView);
        mKSYRecordKit.onResume();
        mCameraHintView.hideAll();

        // camera may be occupied by other app in background
        startCameraPreviewWithPermCheck();

    }

    @Override
    public void onPause() {
        super.onPause();
        mKSYRecordKit.onPause();
        if (!mKSYRecordKit.isRecording() && !mKSYRecordKit.isFileRecording()) {
            mKSYRecordKit.stopCameraPreview();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onStickerChecked(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (mBgmLoadTask != null && mBgmLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
            mBgmLoadTask.cancel(true);
        }
        if (mImgFaceunityFilter != null && mImgFaceunityFilter.getTask() != null &&
                mImgFaceunityFilter.getTask().getStatus() == AsyncTask.Status.RUNNING) {
            mImgFaceunityFilter.getTask().cancel(true);
        }
        mRecordProgressCtl.stop();
        mRecordProgressCtl.setRecordingLengthChangedListener(null);
        mRecordProgressCtl.release();

        mKSYRecordKit.release();

        if (mInitFaceunityThread != null) {
            mInitFaceunityThread.interrupt();
            try {
                mInitFaceunityThread.join();
            } catch (InterruptedException e) {
                Timber.e(e);
            } finally {
                mInitFaceunityThread = null;
            }
        }
        if (!compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//                onBackoffClick();
                handleBackPressed();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setMediaPlayerConfig(ShortVideoConfig config) {
        if (config != null) {

            int frameRate = config.fps;
            if (frameRate > 0) {
                mKSYRecordKit.setPreviewFps(frameRate);
                mKSYRecordKit.setTargetFps(frameRate);
            }

            int videoBitrate = config.videoBitrate;
            if (videoBitrate > 0) {
                mKSYRecordKit.setVideoKBitrate(videoBitrate);
            }

            int audioBitrate = config.audioBitrate;
            if (audioBitrate > 0) {
                mKSYRecordKit.setAudioKBitrate(audioBitrate);
            }

            int videoResolution = config.resolution;
            mKSYRecordKit.setPreviewResolution(videoResolution);
            mKSYRecordKit.setTargetResolution(videoResolution);

            int encode_type = config.encodeType;
            mKSYRecordKit.setVideoCodecId(encode_type);

            int encode_method = config.encodeMethod;
            mKSYRecordKit.setEncodeMethod(encode_method);

            int encodeProfile = config.encodeProfile;
            mKSYRecordKit.setVideoEncodeProfile(encodeProfile);

            mKSYRecordKit.setRotateDegrees(0);
        }
    }

    void handleBackPressed() {
        if (mIsFileRecording)
            showCancelDialog();
        else
            setActivityResultForRecordVideo(false);
    }

    void showCancelDialog() {
        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        builder.title(getString(R.string.app_name))
                .message(getString(R.string.do_you_want_stop_record))
                .confirmText(getString(R.string.btn_text_ok))
                .singleAction(false)
                .onConfirmClicked(() -> {
                    setActivityResultForRecordVideo(false);
                })
                .build().show(this);
    }

    //start recording to a local file
    private void startRecord() {
        String fileFolder = getRecordFileFolder();
        mRecordUrl = fileFolder + "/" + System.currentTimeMillis() + ".mp4";
        float val = mMicAudioVolumeSeekBar.getProgress() / 100.f;
        mKSYRecordKit.setVoiceVolume(val);
        mKSYRecordKit.startRecord(mRecordUrl);
        mIsFileRecording = true;
        mRecordView.getDrawable().setLevel(2);
    }

    /**
     * 停止拍摄
     *
     * @param finished 代表是否结束断点拍摄
     */
    private void stopRecord(boolean finished) {
        //录制完成进入编辑
        //若录制文件大于1则需要触发文件合成
        if (finished) {
            if (getSecondRecorded() >= MIN_DURATION) {
                String fileFolder = getRecordFileFolder();
                String outFile = fileFolder + "/" + "merger_" + System.currentTimeMillis() + ".mp4";
                //合成过程为异步，需要block下一步处理
                final MegerFilesAlertDialog dialog = new MegerFilesAlertDialog(this, R.style.media_kit_dialog);
                dialog.setCancelable(false);
                dialog.show();
                mKSYRecordKit.stopRecord(outFile, filePath -> runOnUiThread(() -> {
                    dialog.dismiss();
                    mRecordUrl = filePath;
                    // TODO return file path
                    setActivityResultForRecordVideo(true);
                }));
            } else {
                Toast.makeText(getApplicationContext(), "Record must least 3 second", Toast.LENGTH_SHORT).show();
                mIsFileRecording = false;
                mKSYRecordKit.stopRecord();
                mRecordProgressCtl.rollback();
            }
        } else {
            //普通录制停止
            mKSYRecordKit.stopRecord();
        }
        //更新进度显示
        mRecordProgressCtl.stopRecording();
        mRecordView.getDrawable().setLevel(1);
        updateDeleteView();

        mIsFileRecording = false;
        stopChronometer();
    }

    private int getSecondRecorded() {
        long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        return (int) elapsedMillis;
    }

    private void setActivityResultForRecordVideo(boolean valid) {
        Intent resultIntent = new Intent();
        int resultCode;
        if (!StringUtil.isNullOrEmptyString(mRecordUrl) && valid) {
            resultCode = RESULT_OK;
            resultIntent.setData(Uri.parse(mRecordUrl));
        } else
            resultCode = RESULT_CANCELED;

        setResult(resultCode, resultIntent);
        finish();
    }

    private void stopChronometer() {
        if (mIsFileRecording) {
            return;
        }

        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.stop();
    }

    // Example to handle camera related operation
    private void setCameraAntiBanding50Hz() {
        Camera.Parameters parameters = mKSYRecordKit.getCameraCapture().getCameraParameters();
        if (parameters != null) {
            parameters.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
            mKSYRecordKit.getCameraCapture().setCameraParameters(parameters);
        }
    }

    private KSYStreamer.OnInfoListener mOnInfoListener = new KSYStreamer.OnInfoListener() {
        @Override
        public void onInfo(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
                    Timber.e("KSY_STREAMER_CAMERA_INIT_DONE");
                    setCameraAntiBanding50Hz();
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_FACEING_CHANGED:
                    updateFaceunitParams();
                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_FILE_SUCCESS:
                    Timber.e("KSY_STREAMER_OPEN_FILE_SUCCESS");
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                    mRecordProgressCtl.startRecording();
                    break;
                default:
                    Timber.e("OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
                    break;
            }
        }
    };

    private void handleEncodeError() {
        int encodeMethod = mKSYRecordKit.getVideoEncodeMethod();
        if (encodeMethod == StreamerConstants.ENCODE_METHOD_HARDWARE) {
            mHWEncoderUnsupported = true;
            if (mSWEncoderUnsupported) {
                mKSYRecordKit.setEncodeMethod(
                        StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE_COMPAT mode");
            } else {
                mKSYRecordKit.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE mode");
            }
        } else if (encodeMethod == StreamerConstants.ENCODE_METHOD_SOFTWARE) {
            mSWEncoderUnsupported = true;
            if (mHWEncoderUnsupported) {
                mKSYRecordKit.setEncodeMethod(
                        StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
                Log.e(TAG, "Got SW encoder error, switch to SOFTWARE_COMPAT mode");
            } else {
                mKSYRecordKit.setEncodeMethod(StreamerConstants.ENCODE_METHOD_HARDWARE);
                Log.e(TAG, "Got SW encoder error, switch to HARDWARE mode");
            }
        }
    }

    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {
        @Override
        public void onError(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
                    Timber.e("KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                    Timber.e("KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                    Timber.e("KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
                    Timber.e("KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
                    Timber.e("KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                    Timber.e("KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    Timber.e("KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                    Timber.e("KSY_STREAMER_CAMERA_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                    Timber.e("KSY_STREAMER_CAMERA_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    Timber.e("KSY_STREAMER_CAMERA_ERROR_SERVER_DIED");
                    break;
                //Camera was disconnected due to use by higher priority user.
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
                    Timber.e("KSY_STREAMER_CAMERA_ERROR_EVICTED");
                    break;
                default:
                    Timber.e("what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
                    break;
            }
            switch (what) {
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    mKSYRecordKit.stopCameraPreview();
                    break;
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_CLOSE_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_OPEN_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_FORMAT_NOT_SUPPORTED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_WRITE_FAILED:
                    stopRecord(false);
                    rollBackClipForError();
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN: {
                    handleEncodeError();
                    stopRecord(false);
                    rollBackClipForError();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startRecord();
                        }
                    }, 3000);
                }
                break;
                default:
                    break;
            }
        }
    };

    private StatsLogReport.OnLogEventListener mOnLogEventListener =
            new StatsLogReport.OnLogEventListener() {
                @Override
                public void onLogEvent(StringBuilder singleLogContent) {
                    Log.i(TAG, "***onLogEvent : " + singleLogContent.toString());
                }
            };

    private void onSwitchCamera() {
        mKSYRecordKit.switchCamera();

    }

    private void onFlashClick() {
        if (mIsFlashOpened) {
            mKSYRecordKit.toggleTorch(false);
            mIsFlashOpened = false;
        } else {
            mKSYRecordKit.toggleTorch(true);
            mIsFlashOpened = true;

        }
    }

    /**
     * back按钮作为返回上一级和删除按钮
     * 当录制文件>=1时 作为删除按钮，否则作为返回上一级按钮
     * 作为删除按钮时，初次点击时先设置为待删除状态，在带删除状态下再执行文件回删
     */
    private void onBackoffClick() {
        if (mDefaultRecordBottomLayout.getVisibility() != View.VISIBLE) {
            if (mBeautyLayout.getVisibility() == View.VISIBLE) {
                mBeautyLayout.setVisibility(View.INVISIBLE);
            }
            if (mBgmLayout.getVisibility() == View.VISIBLE) {
                mBgmLayout.setVisibility(View.INVISIBLE);
            }
            mDefaultRecordBottomLayout.setVisibility(View.VISIBLE);
        } else {
            if (mKSYRecordKit.getRecordedFilesCount() >= 1) {
                if (!mBackView.isSelected()) {
                    mBackView.setSelected(true);
                    //设置最后一个文件为待删除文件
                    mRecordProgressCtl.setLastClipPending();
                } else {
                    mBackView.setSelected(false);
                    //删除文件时，若文件正在录制，则需要停止录制
                    if (mIsFileRecording) {
                        stopRecord(false);
                    }
                    //删除录制文件
                    mKSYRecordKit.deleteRecordFile(mKSYRecordKit.getLastRecordedFiles());
                    mRecordProgressCtl.rollback();
                    updateDeleteView();
                    mRecordView.setEnabled(true);
                }
            } else {
                mChronometer.stop();
                mIsFileRecording = false;
                RecordActivity.this.finish();
            }
        }
    }

    /**
     * 开始/停止录制
     */
    private void onRecordClick() {
        if (mIsFileRecording) {
            stopRecord(true);
        } else {
            startRecord();
        }
        //清除back按钮的状态
        clearBackoff();
    }

    private void screenShot() {
        mRecordView.getDrawable().setLevel(2);
        mKSYRecordKit.requestScreenShot(bitmap -> {
            Uri path = saveToInternalStorage(bitmap);
            if (path != null) {
                setActivityResultForTakePick(true, path);
                recycleBitmap(bitmap);
                finish();
            }
        });
    }

    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }


    private Uri saveToInternalStorage(Bitmap bitmapImage) {

        String fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".jpg";
        File mkDir = new File(Constants.SDROOT, Constants.CAMERA_DIR);
        if (!mkDir.exists()) {
            mkDir.mkdirs();
        }
        File pictureFile = new File(Constants.SDROOT, Constants.CAMERA_DIR + fileName);
        String path = pictureFile.getAbsolutePath();

        try {
            BitmapUtil.saveBitmap(bitmapImage, path);
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }

        return Uri.fromFile(pictureFile);
    }

    private void setActivityResultForTakePick(boolean valid, Uri uriMediaPath) {
        Intent resultIntent = new Intent();
        int resultCode;
        if (valid) {
            resultCode = RESULT_OK;
            resultIntent.setData(uriMediaPath);
        } else
            resultCode = RESULT_CANCELED;

        setResult(resultCode, resultIntent);
    }

    /**
     * 进入编辑页面
     */
    private void onNextClick() {
        clearBackoff();
        clearRecordState();
        mRecordView.getDrawable().setLevel(1);
        //进行编辑前需要停止录制，并且结束断点拍摄
        stopRecord(true);
    }

    private void initHideView() {
        if (requestByCode == Constants.REQUEST_PIC_FROM_CAMERA) {
            mChronometer.setVisibility(View.GONE);
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
        }
    }

    private void initBgmUI() {
        int[] mBgmTitleId = {R.id.bgm_title_music, R.id.bgm_title_soundChange,
                R.id.bgm_title_reverberation};
        int[] mBgmLayoutId = {R.id.bgm_select_layout, R.id.soundEffect_change,
                R.id.soundEffect_reverberation};
        mSoundEffectTitle = new TextView[mBgmTitleId.length];
        mSoundEffectLayout = new View[mBgmLayoutId.length];
        for (int i = 0; i < mBgmTitleId.length; i++) {
            mSoundEffectTitle[i] = (TextView) findViewById(mBgmTitleId[i]);
            mSoundEffectLayout[i] = findViewById(mBgmLayoutId[i]);
            BottomTitleViewInfo mTitleInfo = new BottomTitleViewInfo(mSoundEffectTitle[i],
                    mSoundEffectLayout[i], mObserverButton);
            mRecordTitleArray.put(INDEX_BGM_TITLE_BASE + i, mTitleInfo);
        }
        mPitchMinus = (ImageView) findViewById(R.id.pitch_minus);
        mPitchMinus.setOnClickListener(mObserverButton);
        mPitchPlus = (ImageView) findViewById(R.id.pitch_plus);
        mPitchPlus.setOnClickListener(mObserverButton);
        mPitchText = (TextView) findViewById(R.id.pitch_text);
        mMicAudioVolumeSeekBar.setProgress((int) (mKSYRecordKit.getVoiceVolume() * 100));
        mBgmVolumeSeekBar.setProgress((int) (mKSYRecordKit.getVoiceVolume() * 100));
        setEnableBgmEdit(false);
        mCancelBgm = (ImageView) findViewById(R.id.bgm_music_close);
        mCancelBgm.setOnClickListener(mBgmButtonObserver);
        mImportBgm = (ImageView) findViewById(R.id.bgm_music_import);
        mImportBgm.setOnClickListener(mBgmButtonObserver);
        int[] mBgmItemImageId = {R.id.bgm_music_iv_faded, R.id.bgm_music_iv_hotel,
                R.id.bgm_music_iv_immortals};
        int[] mBgmItemNameId = {R.id.bgm_music_tv_faded, R.id.bgm_music_tv_hotel,
                R.id.bgm_music_tv_immortals};
        for (int i = 0; i < mBgmItemImageId.length; i++) {
            BgmItemViewHolder holder = new BgmItemViewHolder((ImageView) findViewById(mBgmItemImageId[i]),
                    (TextView) findViewById(mBgmItemNameId[i]), mBgmButtonObserver);
            mBgmEffectArray.put(INDEX_BGM_ITEM_BASE + i, holder);
        }
        mCancelSoundChange = (ImageView) findViewById(R.id.effect_iv_close);
        mCancelSoundChange.setOnClickListener(mBgmButtonObserver);
        mCancelReverberation = (ImageView) findViewById(R.id.reverberation_iv_close);
        mCancelReverberation.setOnClickListener(mBgmButtonObserver);
        int[] effectImageId = {R.id.effect_iv_uncle, R.id.effect_iv_lolita,
                R.id.effect_iv_solemn, R.id.effect_iv_robot, R.id.effect_iv_studio,
                R.id.effect_iv_woodWing, R.id.effect_iv_concert, R.id.effect_iv_ktv};
        int[] effectNameId = {R.id.effect_tv_uncle, R.id.effect_tv_lolita,
                R.id.effect_tv_solemn, R.id.effect_tv_robot, R.id.effect_tv_studio,
                R.id.effect_tv_woodWing, R.id.effect_tv_concert, R.id.effect_tv_ktv};
        for (int j = 0; j < effectImageId.length; j++) {
            BgmItemViewHolder holder = new BgmItemViewHolder((ImageView) findViewById(effectImageId[j]),
                    (TextView) findViewById(effectNameId[j]), mBgmButtonObserver);
            mBgmEffectArray.put(INDEX_SOUND_EFFECT_BASE + j, holder);
        }
    }

    /**
     * 美颜滤镜设置
     */
    private void initBeautyUI() {
        String[] items = new String[]{"DISABLE", "BEAUTY_SOFT", "SKIN_WHITEN", "BEAUTY_ILLUSION",
                "BEAUTY_DENOISE", "BEAUTY_SMOOTH", "BEAUTY_PRO", "DEMO_FILTER", "GROUP_FILTER",
                "ToneCurve", "复古", "胶片"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBeautySpinner.setAdapter(adapter);
        mBeautySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((TextView) parent.getChildAt(0));
                if (textView != null) {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                if (position == 0) {
                    mKSYRecordKit.getImgTexFilterMgt().setFilter((ImgFilterBase) null);
                } else if (position <= 5) {
                    mKSYRecordKit.getImgTexFilterMgt().setFilter(
                            mKSYRecordKit.getGLRender(), position + 15);
                } else if (position == 6) {
                    mKSYRecordKit.getImgTexFilterMgt().setFilter(mKSYRecordKit.getGLRender(),
                            ImgTexFilterMgt.KSY_FILTER_BEAUTY_PRO);
                } else if (position == 7) {
                    mKSYRecordKit.getImgTexFilterMgt().setFilter(
                            new DemoFilter(mKSYRecordKit.getGLRender()));
                } else if (position == 8) {
                    List<ImgFilterBase> groupFilter = new LinkedList<>();
                    groupFilter.add(new DemoFilter2(mKSYRecordKit.getGLRender()));
                    groupFilter.add(new DemoFilter3(mKSYRecordKit.getGLRender()));
                    groupFilter.add(new DemoFilter4(mKSYRecordKit.getGLRender()));
                    mKSYRecordKit.getImgTexFilterMgt().setFilter(groupFilter);
                } else if (position == 9) {
                    ImgBeautyToneCurveFilter acvFilter = new ImgBeautyToneCurveFilter(mKSYRecordKit.getGLRender());
                    acvFilter.setFromCurveFileInputStream(
                            RecordActivity.this.getResources().openRawResource(R.raw.tone_cuver_sample));

                    mKSYRecordKit.getImgTexFilterMgt().setFilter(acvFilter);
                } else if (position == 10) {
                    ImgBeautyToneCurveFilter acvFilter = new ImgBeautyToneCurveFilter(mKSYRecordKit
                            .getGLRender());
                    acvFilter.setFromCurveFileInputStream(
                            RecordActivity.this.getResources().openRawResource(R.raw.fugu));

                    mKSYRecordKit.getImgTexFilterMgt().setFilter(acvFilter);
                } else if (position == 11) {
                    ImgBeautyToneCurveFilter acvFilter = new ImgBeautyToneCurveFilter(mKSYRecordKit
                            .getGLRender());
                    acvFilter.setFromCurveFileInputStream(
                            RecordActivity.this.getResources().openRawResource(R.raw.jiaopian));

                    mKSYRecordKit.getImgTexFilterMgt().setFilter(acvFilter);
                }
                List<ImgFilterBase> filters = mKSYRecordKit.getImgTexFilterMgt().getFilter();
                if (filters != null && !filters.isEmpty()) {
                    final ImgFilterBase filter = filters.get(0);
                    mBeautyGrindLayout.setVisibility(filter.isGrindRatioSupported() ?
                            View.VISIBLE : View.GONE);
                    mBeautyWhitenLayout.setVisibility(filter.isWhitenRatioSupported() ?
                            View.VISIBLE : View.GONE);
                    mBeautyRuddyLayout.setVisibility(filter.isRuddyRatioSupported() ?
                            View.VISIBLE : View.GONE);
                    SeekBar.OnSeekBarChangeListener seekBarChangeListener =
                            new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress,
                                                              boolean fromUser) {
                                    if (!fromUser) {
                                        return;
                                    }
                                    float val = progress / 100.f;
                                    if (seekBar == mGrindSeekBar) {
                                        filter.setGrindRatio(val);
                                    } else if (seekBar == mWhitenSeekBar) {
                                        filter.setWhitenRatio(val);
                                    } else if (seekBar == mRuddySeekBar) {
                                        if (filter instanceof ImgBeautyProFilter) {
                                            val = progress / 50.f - 1.0f;
                                        }
                                        filter.setRuddyRatio(val);
                                    }
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                }
                            };
                    mGrindSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mWhitenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mRuddySeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mGrindSeekBar.setProgress((int) (filter.getGrindRatio() * 100));
                    mWhitenSeekBar.setProgress((int) (filter.getWhitenRatio() * 100));
                    int ruddyVal = (int) (filter.getRuddyRatio() * 100);
                    if (filter instanceof ImgBeautyProFilter) {
                        ruddyVal = (int) (filter.getRuddyRatio() * 50 + 50);
                    }
                    mRuddySeekBar.setProgress(ruddyVal);
                } else {
                    mBeautyGrindLayout.setVisibility(View.GONE);
                    mBeautyWhitenLayout.setVisibility(View.GONE);
                    mBeautyRuddyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        mBeautySpinner.setPopupBackgroundResource(R.color.transparent);
        mBeautySpinner.setSelection(4);
    }

    /*********************************Faceunity sticker begin***********************************/
    private void onStickerChecked(boolean isChecked) {
        if (mImgFaceunityFilter != null) {
            if (isChecked) {
                //需要输入camera数据用于人脸识别
                mKSYRecordKit.getCameraCapture().mImgBufSrcPin.connect(mImgFaceunityFilter.getBufSinkPin());
            } else {
                if (mImgFaceunityFilter != null) {
                    mKSYRecordKit.getCameraCapture().mImgBufSrcPin.disconnect(mImgFaceunityFilter.getBufSinkPin(),
                            false);
                    mImgFaceunityFilter.setPropType(-1);
                }
            }
        }
    }

    /**
     * 贴纸选择
     */
    private void initStickerUI() {
        String[] items = new String[]{"DISABLE", "BEAGLEDOG", "COLORCROWN", "DEER",
                "HAPPYRABBI", "HARTSHORN"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStickerSpinner.setAdapter(adapter);
        mStickerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((TextView) parent.getChildAt(0));
                if (textView != null) {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                if (position == 0) {
                    //disable
                    if (mImgFaceunityFilter != null) {
                        mImgFaceunityFilter.setPropType(-1);
                    }
                } else {
                    mImgFaceunityFilter.setPropType(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        mStickerSpinner.setPopupBackgroundResource(R.color.transparent);
        mStickerSpinner.setSelection(0);
    }

    /**
     * 初始化faceuniy，以美颜方式设置
     */
    private void initFaceunity() {
        if (mImgFaceunityFilter == null) {
            //add faceunity filter
            mImgFaceunityFilter = new ImgFaceunityFilter(this, mKSYRecordKit.getGLRender());
            mKSYRecordKit.getImgTexFilterMgt().setExtraFilter(mImgFaceunityFilter);
        }

        updateFaceunitParams();
    }

    /**
     * 前后摄像头切换时，需要更新摄像头信息
     * 分辨率发生变化时，需要更新分辨率信息
     */
    private void updateFaceunitParams() {
        if (mImgFaceunityFilter != null) {
            mImgFaceunityFilter.setTargetSize(mKSYRecordKit.getTargetWidth(),
                    mKSYRecordKit.getTargetHeight());

            if (mKSYRecordKit.isFrontCamera()) {
                mImgFaceunityFilter.setMirror(true);
            } else {
                mImgFaceunityFilter.setMirror(false);
            }
        }
    }

    /*********************************Faceunity sticker end***********************************/

    private void onBgmItemClick(int index) {
        clearPitchState();
        BgmItemViewHolder curHolder = mBgmEffectArray.get(INDEX_BGM_ITEM_BASE + index);
        BgmItemViewHolder preHolder = mBgmEffectArray.get(INDEX_BGM_ITEM_BASE + mPreBgmItemIndex);
        if (index == -1) {
            mKSYRecordKit.stopBgm();
            preHolder.setBottomTextActivated(false);
            setEnableBgmEdit(false);
        } else {
            if (index < 3) {
                mKSYRecordKit.stopBgm();
                mKSYRecordKit.setEnableAudioMix(true);
                String fileName = mBgmLoadPath[index].substring(mBgmLoadPath[index].lastIndexOf('/'));
                final String filePath = FileUtils.getCacheDirectory(getApplicationContext()) + fileName;
                File file = new File(filePath);
                if (!file.exists()) {
                    if (mBgmLoadTask != null && mBgmLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
                        mBgmLoadTask.cancel(true);
                    }
                    DownloadAndHandleTask.DownloadListener listener = new DownloadAndHandleTask.DownloadListener() {
                        @Override
                        public void onCompleted() {
                            mKSYRecordKit.startBgm(filePath, true);
                        }
                    };
                    mBgmLoadTask = new DownloadAndHandleTask(filePath, listener);
                    mBgmLoadTask.execute(mBgmLoadPath[index]);
                } else {
                    mKSYRecordKit.startBgm(filePath, true);
                }
            }
            preHolder.setBottomTextActivated(false);
            curHolder.setBottomTextActivated(true);
            mPreBgmItemIndex = index;
            setEnableBgmEdit(true);
        }
    }

    private void onSoundEffectItemClick(int index) {
        BgmItemViewHolder curHolder = mBgmEffectArray.get(INDEX_SOUND_EFFECT_BASE + index);
        BgmItemViewHolder preHolder1 = mBgmEffectArray.get(INDEX_SOUND_EFFECT_BASE + mPreBgmEffectIndex);
        BgmItemViewHolder preHolder2 = mBgmEffectArray.get(INDEX_SOUND_EFFECT_BASE + mPreBgmReverbIndex);
        if (index == -1) {
            preHolder1.setBottomTextActivated(false);
            mAudioEffectType = AUDIO_FILTER_DISABLE;
        } else if (index == -2) {
            preHolder2.setBottomTextActivated(false);
            mAudioReverbType = AUDIO_FILTER_DISABLE;
        } else {
            if (index < 4) {
                preHolder1.setBottomTextActivated(false);
                mPreBgmEffectIndex = index;
                mAudioEffectType = SOUND_EFFECT_CONST[index];
            } else {
                preHolder2.setBottomTextActivated(false);
                mPreBgmReverbIndex = index;
                mAudioReverbType = SOUND_EFFECT_CONST[index];
            }
            curHolder.setBottomTextActivated(true);
        }
        addAudioFilter();
    }

    private void addAudioFilter() {
        KSYAudioEffectFilter effectFilter;
        AudioReverbFilter reverbFilter;
        List<AudioFilterBase> filters = new LinkedList<>();
        if (mAudioEffectType != AUDIO_FILTER_DISABLE) {
            effectFilter = new KSYAudioEffectFilter
                    (mAudioEffectType);
            filters.add(effectFilter);
        }
        if (mAudioReverbType != AUDIO_FILTER_DISABLE) {
            reverbFilter = new AudioReverbFilter();
            reverbFilter.setReverbLevel(mAudioReverbType);
            filters.add(reverbFilter);
        }
        if (filters.size() > 0) {
            mKSYRecordKit.getAudioFilterMgt().setFilter(filters);
        } else {
            mKSYRecordKit.getAudioFilterMgt().setFilter((AudioFilterBase) null);
        }
    }

    private void clearRecordState() {
        mBeautySpinner.setSelection(0);
        mStickerSpinner.setSelection(0);
        mKSYRecordKit.getImgTexFilterMgt().setFilter((ImgFilterBase) null);
        onStickerChecked(false);
        onBgmItemClick(-1);
        onSoundEffectItemClick(-1);
        onSoundEffectItemClick(-2);
    }

    private void setEnableBgmEdit(boolean enable) {
        if (mPitchMinus != null) {
            mPitchMinus.setEnabled(enable);
        }
        if (mPitchPlus != null) {
            mPitchPlus.setEnabled(enable);
        }
        if (mBgmVolumeSeekBar != null) {
            mBgmVolumeSeekBar.setEnabled(enable);
        }
    }

    private class BgmButtonObserver implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bgm_music_close:
                    onBgmItemClick(-1);
                    break;
                case R.id.bgm_music_iv_faded:
                    onBgmItemClick(0);
                    break;
                case R.id.bgm_music_iv_hotel:
                    onBgmItemClick(1);
                    break;
                case R.id.bgm_music_iv_immortals:
                    onBgmItemClick(2);
                    break;
                case R.id.bgm_music_import:
                    onBgmItemClick(-1);
                    importMusicFile();
                    break;
                case R.id.effect_iv_close:
                    onSoundEffectItemClick(-1);
                    break;
                case R.id.effect_iv_uncle:
                    onSoundEffectItemClick(0);
                    break;
                case R.id.effect_iv_lolita:
                    onSoundEffectItemClick(1);
                    break;
                case R.id.effect_iv_solemn:
                    onSoundEffectItemClick(2);
                    break;
                case R.id.effect_iv_robot:
                    onSoundEffectItemClick(3);
                    break;
                case R.id.reverberation_iv_close:
                    onSoundEffectItemClick(-2);
                    break;
                case R.id.effect_iv_studio:
                    onSoundEffectItemClick(4);
                    break;
                case R.id.effect_iv_woodWing:
                    onSoundEffectItemClick(5);
                    break;
                case R.id.effect_iv_concert:
                    onSoundEffectItemClick(6);
                    break;
                case R.id.effect_iv_ktv:
                    onSoundEffectItemClick(7);
                    break;
            }
        }
    }

    private class ButtonObserver implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.switch_cam:
                    if (mKSYRecordKit.isFrontCamera()) {
                        mFlashView.setVisibility(View.VISIBLE);
                    } else {
                        mFlashView.setVisibility(View.GONE);
                    }
                    onSwitchCamera();
                    break;
                case R.id.flash:
                    onFlashClick();
                    break;
                case R.id.click_to_record:
                    if (requestByCode == Constants.CAMERA_VIDEO_REQUEST) {
                        onRecordClick();
                    } else if (requestByCode == Constants.REQUEST_PIC_FROM_CAMERA) {
                        screenShot();
                    }
                    break;
                case R.id.button_close_camera_view:
                    onBackoffClick();
                    break;
                case R.id.click_to_next:
                    onNextClick();
                    break;
                case R.id.record_beauty:
                    onBeautyClick();
                    break;
                case R.id.record_bgm:
                    onBgmClick();
                    break;
                case R.id.item_bgm_back:
                    mBgmLayout.setVisibility(View.GONE);
                    mDefaultRecordBottomLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.item_beauty:
                    onBeautyTitleClick(0);
                    break;
                case R.id.item_beauty_back:
                    mBeautyLayout.setVisibility(View.GONE);
                    mDefaultRecordBottomLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.item_dyn_sticker:
                    onBeautyTitleClick(1);
                    break;
                case R.id.bgm_title_music:
                    onBgmTitleClick(0);
                    break;
                case R.id.pitch_minus:
                    onPitchClick(-1);
                    break;
                case R.id.pitch_plus:
                    onPitchClick(1);
                    break;
                case R.id.bgm_title_soundChange:
                    onBgmTitleClick(1);
                    break;
                case R.id.bgm_title_reverberation:
                    onBgmTitleClick(2);
                    break;
                default:
                    break;
            }
        }
    }

    private void clearPitchState() {
        mPitchValue = 0;
        mPitchText.setText("0");
        KSYAudioEffectFilter audioFilter = new KSYAudioEffectFilter(KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_PITCH);
        audioFilter.setPitchLevel(mPitchValue);
        mKSYRecordKit.getBGMAudioFilterMgt().setFilter(audioFilter);
    }

    private void onPitchClick(int sign) {
        if (sign < 0) {
            if (mPitchValue > -3) {
                mPitchValue--;
            }
        } else {
            if (mPitchValue < 3) {
                mPitchValue++;
            }
        }
        mPitchText.setText(mPitchValue + "");
        KSYAudioEffectFilter audioFilter = new KSYAudioEffectFilter(KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_PITCH);
        audioFilter.setPitchLevel(mPitchValue);
        mKSYRecordKit.getBGMAudioFilterMgt().setFilter(audioFilter);
    }

    private void onFrontMirrorChecked(boolean isChecked) {
        mKSYRecordKit.setFrontCameraMirror(isChecked);
    }

    private void onBeautyClick() {
        mDefaultRecordBottomLayout.setVisibility(View.INVISIBLE);
        mBgmLayout.setVisibility(View.GONE);
        if (mBeautyLayout.getVisibility() != View.VISIBLE) {
            mBeautyLayout.setVisibility(View.VISIBLE);
        }
    }

    private void onBgmClick() {
        mDefaultRecordBottomLayout.setVisibility(View.INVISIBLE);
        mBeautyLayout.setVisibility(View.GONE);
        if (mBgmLayout.getVisibility() != View.VISIBLE) {
            mBgmLayout.setVisibility(View.VISIBLE);
        }
    }

    private void onBeautyTitleClick(int index) {
        BottomTitleViewInfo curInfo = mRecordTitleArray.get(INDEX_BEAUTY_TITLE_BASE + index);
        BottomTitleViewInfo preInfo = mRecordTitleArray.get(INDEX_BEAUTY_TITLE_BASE + mPreBeautyTitleIndex);
        if (index != mPreBeautyTitleIndex) {
            curInfo.setChosenState(true);
            preInfo.setChosenState(false);
            mPreBeautyTitleIndex = index;
        }
        if (index == 1) {
            if (mInitFaceunityThread == null) {
                mInitFaceunityThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initFaceunity();
                        onStickerChecked(true);
                    }
                });
                mInitFaceunityThread.start();
            } else {
                onStickerChecked(true);
            }
        }
    }

    private void onBgmTitleClick(int index) {
        BottomTitleViewInfo curInfo = mRecordTitleArray.get(INDEX_BGM_TITLE_BASE + index);
        BottomTitleViewInfo preInfo = mRecordTitleArray.get(INDEX_BGM_TITLE_BASE + mPreBgmTitleIndex);
        if (index != mPreBgmTitleIndex) {
            curInfo.setChosenState(true);
            preInfo.setChosenState(false);
            mPreBgmTitleIndex = index;
        }
    }

    private void initBottomTitleUI() {
        BottomTitleViewInfo initBeautyInfo = mRecordTitleArray.get(INDEX_BEAUTY_TITLE_BASE + mPreBeautyTitleIndex);
        initBeautyInfo.setChosenState(true);
        BottomTitleViewInfo initBgmInfo = mRecordTitleArray.get(INDEX_BGM_TITLE_BASE + mPreBgmTitleIndex);
        initBgmInfo.setChosenState(true);
    }

    private void importMusicFile() {
        Intent target = FileUtils.createGetContentIntent();
        Intent intent = Intent.createChooser(target, "ksy_import_music_file");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            mKSYRecordKit.startBgm(path, true);
                            setEnableBgmEdit(true);
                        } catch (Exception e) {
                            Log.e(TAG, "File select error:" + e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onWaterMarkChecked(boolean isChecked) {
        if (isChecked) {
            mKSYRecordKit.showWaterMarkLogo(mLogoPath, 0.08f, 0.04f, 0.20f, 0, 0.8f);
        } else {
            mKSYRecordKit.hideWaterMarkLogo();
        }

    }


    private class CheckBoxObserver implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.record_front_mirror:
                    onFrontMirrorChecked(isChecked);
                    break;
                case R.id.record_watermark:
                    onWaterMarkChecked(isChecked);
                    break;
                default:
                    break;
            }
        }
    }

    private class SeekBarChangedObserver implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (!fromUser) {
                return;

            }
            float val = progress / 100.f;
            switch (seekBar.getId()) {
                case R.id.record_mic_audio_volume:
                    mKSYRecordKit.setVoiceVolume(val);
                    break;
                case R.id.record_music_audio_volume:
                    mKSYRecordKit.getAudioPlayerCapture().getMediaPlayer().setVolume(val, val);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    }

    private void startCameraPreviewWithPermCheck() {
        mRxPermissions = new RxPermissions(this);
        compositeDisposable = new CompositeDisposable();
        int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int audioPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (cameraPerm != PackageManager.PERMISSION_GRANTED ||
                audioPerm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Timber.e("No CAMERA or AudioRecord permission, please check");
                Toast.makeText(getApplicationContext(), "No CAMERA or AudioRecord permission, please check",
                        Toast.LENGTH_LONG).show();
            } else {
//                String[] permissions = {Manifest.permission.CAMERA,
//                        Manifest.permission.RECORD_AUDIO,
//                        Manifest.permission.READ_EXTERNAL_STORAGE};
//                ActivityCompat.requestPermissions(this, permissions,
//                        PERMISSION_REQUEST_CAMERA_AUDIOREC);
                String[] permissions;
                if (requestByCode == Constants.CAMERA_VIDEO_REQUEST){
                    permissions = new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE};
                }else {
                    permissions = new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE};
                }
                compositeDisposable.add(mRxPermissions.request(
                        permissions
                ).subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        mKSYRecordKit.startCameraPreview();
                    } else {
                        Timber.e("No CAMERA or AudioRecord permission");
                        Toast.makeText(getApplicationContext(), "No CAMERA or AudioRecord permission",
                                Toast.LENGTH_LONG).show();
                    }
                }));
            }
        } else {
            mKSYRecordKit.startCameraPreview();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA_AUDIOREC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mKSYRecordKit.startCameraPreview();
                } else {
                    Timber.e("No CAMERA or AudioRecord permission");
                    Toast.makeText(getApplicationContext(), "No CAMERA or AudioRecord permission",
                            Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    /**
     * 开始拍摄更新，删除按钮状态
     */
    private void updateDeleteView() {
        if (mKSYRecordKit.getRecordedFilesCount() >= 1) {
//            mBackView.getDrawable().setLevel(2);
        } else {
//            mBackView.getDrawable().setLevel(1);
        }
    }

    /**
     * 清除back按钮的状态（删除），并设置最后一个录制的文件为普通文件
     *
     * @return
     */
    private boolean clearBackoff() {
        if (mBackView.isSelected()) {
            mBackView.setSelected(false);
            //设置最后一个文件为普通文件
            mRecordProgressCtl.setLastClipNormal();
            return true;
        }
        return false;
    }

    /**
     * 拍摄错误停止后，删除多余文件的进度
     */
    private void rollBackClipForError() {
        //当拍摄异常停止时，SDk内部会删除异常文件，如果ctl比SDK返回的文件小，则需要更新ctl中的进度信息
        int clipCount = mRecordProgressCtl.getClipListSize();
        int fileCount = mKSYRecordKit.getRecordedFilesCount();
        if (clipCount > fileCount) {
            int diff = clipCount - fileCount;
            for (int i = 0; i < diff; i++) {
                mRecordProgressCtl.rollback();
            }
        }
    }

    private RecordProgressController.RecordingLengthChangedListener mRecordLengthChangedListener =
            new RecordProgressController.RecordingLengthChangedListener() {
                @Override
                public void passMinPoint(boolean pass) {
                    if (pass) {
                        //超过最短时长显示下一步按钮，否则不能进入编辑，最短时长可自行设定，Demo中当前设定为5s
//                        mNextView.setVisibility(View.VISIBLE);
                    } else {
                        mNextView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void passMaxPoint() {
                    runOnUiThread(() -> {
                        //到达最大拍摄时长时，需要主动停止录制
                        stopRecord(true);
                        mRecordView.getDrawable().setLevel(1);
                        mRecordView.setEnabled(false);

                        // TODO return file path
                    });
                }
            };

    private class MegerFilesAlertDialog extends AlertDialog {

        protected MegerFilesAlertDialog(Context context, int themID) {
            super(context, themID);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.meger_record_files_layout);
        }
    }

    private String getRecordFileFolder() {
        String fileFolder = "/sdcard" + CONSTANTS.VIDEO_FOLDER;
        File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdir();
        }
        return fileFolder;
    }

    public class BottomTitleViewInfo {
        private TextView titleView;
        private View relativeLayout;

        public BottomTitleViewInfo(TextView tv, View v,
                                   View.OnClickListener onClickListener) {
            this.titleView = tv;
            this.relativeLayout = v;
            if (titleView != null) {
                titleView.setOnClickListener(onClickListener);
            }
        }

        public void setChosenState(boolean isChosen) {
            if (isChosen) {
                relativeLayout.setVisibility(View.VISIBLE);
                titleView.setActivated(true);
                titleView.setBackgroundColor(Color.parseColor("#616060"));
            } else {
                relativeLayout.setVisibility(View.GONE);
                titleView.setActivated(false);
                titleView.setBackgroundColor(Color.parseColor("#919191"));
            }
        }
    }

    public class BgmItemViewHolder {
        public ImageView mBgmItemImage;
        public TextView mBgmItemName;

        public BgmItemViewHolder(ImageView iv, TextView tv,
                                 View.OnClickListener onClickListener) {
            this.mBgmItemImage = iv;
            this.mBgmItemName = tv;
            if (mBgmItemImage != null) {
                mBgmItemImage.setOnClickListener(onClickListener);
            }
        }

        public void setBottomTextActivated(boolean isSelected) {
            if (mBgmItemName != null) {
                mBgmItemName.setActivated(isSelected);
            }
        }
    }

}