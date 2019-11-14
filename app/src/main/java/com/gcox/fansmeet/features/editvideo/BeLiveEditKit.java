package com.gcox.fansmeet.features.editvideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import com.gcox.fansmeet.features.editvideo.videorange.BeLiveComposeKit;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaMeta;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.misc.KSYProbeMediaInfo;
import com.ksyun.media.shortvideo.capture.AVDecoderCapture;
import com.ksyun.media.shortvideo.kit.KSYMergeKit;
import com.ksyun.media.shortvideo.sticker.KSYStickerView;
import com.ksyun.media.shortvideo.timereffect.TimerEffectFilter;
import com.ksyun.media.shortvideo.timereffect.TimerEffectInfo;
import com.ksyun.media.shortvideo.utils.AuthInfoManager;
import com.ksyun.media.shortvideo.utils.FileUtils;
import com.ksyun.media.streamer.capture.AudioPlayerCapture;
import com.ksyun.media.streamer.capture.WaterMarkCapture;
import com.ksyun.media.streamer.filter.audio.*;
import com.ksyun.media.streamer.filter.imgtex.*;
import com.ksyun.media.streamer.framework.AudioCodecFormat;
import com.ksyun.media.streamer.util.gles.GLRender;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by thanhbc on 7/14/17.
 */

public class BeLiveEditKit {
    private static final String TAG = "BeLiveEditKit";
    private static final boolean b = true;
    private static final long c = -1L;
    private static final int d = 2;
    public static final int SCALING_MODE_BEST_FIT = 1;
    public static final int SCALING_MODE_CROP = 3;
    public static final int SCALING_MODE_FULL_FILL = 0;
    public static int SCALE_TYPE_9_16 = 1;
    public static int SCALE_TYPE_3_4 = 2;
    public static int SCALE_TYPE_1_1 = 3;
    public static float DEFAULT_SCALE = 0.5625F;
    private static float e = 2.0F;
    private static float f = 0.5F;
    private Context g;
    private int h = 0;
    private int i = 1;
    private String j;
    private int k = 0;
    private int l = 0;
    private int m = 0;
    private int n = 0;
    private GLRender o;
    private WaterMarkCapture p;
    private ImgTexMixer q;
    private ImgTexScaleFilter r;
    private ImgTexFilterMgt s;
    private com.ksyun.media.shortvideo.capture.b t;
    private ImgTexPreview u;
    private AudioFilterMgt v;
    private AudioPlayerCapture w;
    private AudioPreview x;
    private AudioMixer y;
    private Handler z;
    private boolean A = false;
    private boolean B = false;
    private long C = 0L;
    private float D = 1.0F;
    private BeLiveEditKit.OnInfoListener mOnInfoListener;
    private BeLiveEditKit.OnErrorListener mOnErrorListener;
    private BeLiveComposeKit mKSYComposeKit;
    private KSYMergeKit H;
    private String I = "/sdcard/dest.mp4";
    private String J = "/sdcard/dest.mp4";
    private List<ImgFilterBase> K;
    private List<ImgFilterBase> L;
    private List<AudioFilterBase> M;
    private KSYStickerView N;
    private Timer O;
    private com.ksyun.media.shortvideo.sticker.c P;
    private volatile RectF Q;
    private View R;
    private String S = null;
    private BeLiveEditKit.b T = null;
    private long U;
    private long V;
    private com.ksyun.media.shortvideo.a.b.a W;
    private com.ksyun.media.shortvideo.a.c X;
    private String Y;
    private String Z;
    private volatile boolean aa = false;
    private volatile boolean ab = true;
    private TreeMap<Long, List<com.ksyun.media.shortvideo.timereffect.b>> ac = new TreeMap();
    private TreeMap<Integer, TimerEffectInfo> ad = new TreeMap();
    private volatile boolean ae = false;
    private volatile boolean af = true;
    private AtomicLong ag = new AtomicLong(-1L);
    private AtomicLong ah = new AtomicLong(-1L);
    private TreeMap<Integer, com.ksyun.media.shortvideo.timereffect.b> ai = new TreeMap();
    private TreeMap<Integer, com.ksyun.media.shortvideo.timereffect.b> aj = new TreeMap();
    public com.ksyun.media.player.IMediaPlayer.OnInfoListener mMediaPlayerOnInfoListener = new com.ksyun.media.player.IMediaPlayer.OnInfoListener() {
        public boolean onInfo(IMediaPlayer var1, int var2, int var3) {
            Log.d(TAG, "Media Player info:" + var2 + "_" + var3);
            BeLiveEditKit.this.a(6, String.valueOf(var2));
            return false;
        }
    };
    public com.ksyun.media.player.IMediaPlayer.OnErrorListener mMediaPlayerOnErrorListener = new com.ksyun.media.player.IMediaPlayer.OnErrorListener() {
        public boolean onError(IMediaPlayer var1, int var2, int var3) {
            Log.d(TAG, "Media Player error:" + var2 + "_" + var3);
            BeLiveEditKit.this.a(-4, (long)var2);
            return false;
        }
    };
    public IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer var1) {
            KSYMediaPlayer var2 = (KSYMediaPlayer)var1;
            KSYMediaMeta var3 = var2.getMediaInfo().mMeta;
            if (BeLiveEditKit.this.C == 0L) {
                BeLiveEditKit.this.C = var2.getDuration();
                if (BeLiveEditKit.this.mOnInfoListener != null) {
                    BeLiveEditKit.this.mOnInfoListener.onInfo(1);
                }
            }

            boolean var4 = false;
            boolean var5 = false;
            boolean var6 = false;
            boolean var7 = false;
            if (var3.mVideoStream == null) {
                Log.d(TAG, "audio only");
                var7 = true;
            } else {
                int var9 = var2.getVideoWidth();
                int var10 = var2.getVideoHeight();
                if (BeLiveEditKit.this.t.j() % 180 != 0) {
                    int var8 = var9;
                    var9 = var10;
                    var10 = var8;
                }

                BeLiveEditKit.this.m = var9;
                BeLiveEditKit.this.n = var10;
            }

            BeLiveEditKit.this.r.setTargetSize(BeLiveEditKit.this.m, BeLiveEditKit.this.n);
            BeLiveEditKit.this.p.setPreviewSize(BeLiveEditKit.this.m, BeLiveEditKit.this.n);
            if (var3.mAudioStream == null) {
                var6 = true;
                Log.d(TAG, "video only");
            }

            BeLiveEditKit.this.ab = false;
        }
    };
    private BeLiveComposeKit.b ak = new BeLiveComposeKit.b() {
        public void onInfo(BeLiveComposeKit var1, int var2, String var3) {
            Log.d(TAG, "compose info:" + var2);
            switch(var2) {
                case 1:
                    BeLiveEditKit.this.a(2, var3);
                    break;
                case 2:
                    BeLiveEditKit.this.a(false, 0);
                    BeLiveEditKit.this.d();
                    if ((!TextUtils.isEmpty(BeLiveEditKit.this.Z) || !TextUtils.isEmpty(BeLiveEditKit.this.Y)) && !FileUtils.getMimeType(new File(var3)).equals(FileUtils.MIME_TYPE_GIF)) {
                        BeLiveEditKit.this.a(var3);
                    } else {
                        BeLiveEditKit.this.a(3, var3);
                    }
                    break;
                case 3:
                    BeLiveEditKit.this.a(4, var3);
                    BeLiveEditKit.this.d();
                    break;
                default:
                    BeLiveEditKit.this.a(var2, var3);
            }

        }
    };
    private BeLiveComposeKit.a al = new BeLiveComposeKit.a() {
        @Override
        public void onError(BeLiveComposeKit var1, int var2, long var3) {
            BeLiveEditKit.this.a(true, var2);
            BeLiveEditKit.this.a(var2, var3);
            BeLiveEditKit.this.d();
        }
    };
    private com.ksyun.media.shortvideo.capture.b.a am = new com.ksyun.media.shortvideo.capture.b.a() {
        public void onVideoPtsChanged(long var1) {
            BeLiveEditKit.this.a(var1);
            BeLiveEditKit.this.c(var1);
        }
    };
    private AVDecoderCapture.OnVideoPtsChangedListener an = new AVDecoderCapture.OnVideoPtsChangedListener() {
        public void onVideoPtsChanged(long var1) {
            BeLiveEditKit.this.d(var1);
            BeLiveEditKit.this.b(var1);
        }
    };
    public GLRender.GLRenderListener mGLRenderListener = new GLRender.GLRenderListener() {
        public void onReady() {
        }

        public void onSizeChanged(int var1, int var2) {
            BeLiveEditKit.this.k = var1;
            BeLiveEditKit.this.l = var2;
            Log.d(TAG, "screenWidth * screenHeight:" + BeLiveEditKit.this.k + "*" + BeLiveEditKit.this.l);
            BeLiveEditKit.this.g();
            if (BeLiveEditKit.this.T != null) {
                BeLiveEditKit.this.showWaterMarkLogo(BeLiveEditKit.this.T.a, BeLiveEditKit.this.T.b, BeLiveEditKit.this.T.c, BeLiveEditKit.this.T.d, BeLiveEditKit.this.T.e, BeLiveEditKit.this.T.f);
            }

            if (BeLiveEditKit.this.A) {
                BeLiveEditKit.this.A = false;
                BeLiveEditKit.this.startEditPreview();
            }

            if (!BeLiveEditKit.this.t.a().isPlaying()) {
                BeLiveEditKit.this.t.k();
            }

        }

        public void onDrawFrame() {
        }

        public void onReleased() {
        }
    };
    private com.ksyun.media.shortvideo.kit.KSYMergeKit.OnInfoListener ao = new com.ksyun.media.shortvideo.kit.KSYMergeKit.OnInfoListener() {
        public void onInfo(int var1, String var2) {
            Log.d(TAG, "merge info:" + var1);
            switch(var1) {
                case 2:
                    BeLiveEditKit.this.a(4, var2);
                    BeLiveEditKit.this.j();
                    break;
                case 3:
                    if (BeLiveEditKit.this.Y != null && BeLiveEditKit.this.H.getCurrentTransFileId() == 0) {
                        BeLiveEditKit.this.a(7, var2);
                    } else if (BeLiveEditKit.this.Y == null && BeLiveEditKit.this.H.getCurrentTransFileId() == 1 || BeLiveEditKit.this.H.getCurrentTransFileId() == 2) {
                        BeLiveEditKit.this.a(8, var2);
                    }
                    break;
                case 100:
                    BeLiveEditKit.this.a(3, var2);
                    if (BeLiveEditKit.this.J != null) {
                        File var3 = new File(BeLiveEditKit.this.J);
                        if (var3.exists()) {
                            var3.delete();
                        }
                    }

                    BeLiveEditKit.this.j();
            }

        }
    };
    private com.ksyun.media.shortvideo.kit.KSYMergeKit.OnErrorListener ap = new com.ksyun.media.shortvideo.kit.KSYMergeKit.OnErrorListener() {
        public void onError(int var1, int var2, long var3) {
            Log.e("BeLiveEditKit", "merge error:" + var1);
            switch(var1) {
                case -100:
                case -1:
                    BeLiveEditKit.this.a(-4005, (long)var1);
                    BeLiveEditKit.this.j();
                default:
            }
        }
    };

    public BeLiveEditKit(Context var1) {
        if (var1 == null) {
            throw new IllegalArgumentException("Context cannot be null!");
        } else {
            this.g = var1.getApplicationContext();
            this.z = new BeLiveEditKit.a(this, Looper.getMainLooper());
            com.ksyun.media.shortvideo.a.c.a().a(this.g);
            this.a();
        }
    }

    private void a() {
        this.o = new GLRender();
        this.p = new WaterMarkCapture(this.o);
        this.r = new ImgTexScaleFilter(this.o);
        this.t = new com.ksyun.media.shortvideo.capture.b(this.g, this.o);
        this.t.c(false);
        this.t.a(true);
        this.s = new ImgTexFilterMgt(this.g);
        this.q = new ImgTexMixer(this.o);
        this.u = new ImgTexPreview();
        ImgTexScaleFilter var10001 = this.r;
        this.r.setScalingMode(0);
        this.q.setScalingMode(this.h, 1);
        this.t.b.connect(this.r.getSinkPin());
        this.r.getSrcPin().connect(this.s.getSinkPin());
        this.s.getSrcPin().connect(this.q.getSinkPin(this.h));
        this.p.mLogoTexSrcPin.connect(this.q.getSinkPin(this.i));
        this.q.getSrcPin().connect(this.u.getSinkPin());
        this.t.a(this.am);
        this.w = new AudioPlayerCapture(this.g);
        this.v = new AudioFilterMgt();
        this.x = new AudioPreview(this.g);
        this.y = new AudioMixer();
        this.t.a.connect(this.v.getSinkPin());
        this.v.getSrcPin().connect(this.y.getSinkPin(0));
        this.y.getSrcPin().connect(this.x.getSinkPin());
        this.x.setBlockingMode(true);
        this.mKSYComposeKit = new BeLiveComposeKit(this.g);
        this.mKSYComposeKit.setOnInfoListener(this.ak);
        this.mKSYComposeKit.setOnErrorListener(this.al);
        this.K = new LinkedList();
        this.L = new LinkedList();
        this.M = new LinkedList();
        this.t.a(this.mMediaPlayerOnInfoListener);
        this.t.a(this.mOnPreparedListener);
        this.t.a(this.mMediaPlayerOnErrorListener);
        this.t.a(new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer var1) {
                if (BeLiveEditKit.this.w.getMediaPlayer().isPlaying()) {
                    BeLiveEditKit.this.w.restart();
                }

                BeLiveEditKit.this.ab = true;
                if (BeLiveEditKit.this.ac.size() > 1) {
                    Long var2 = (Long)BeLiveEditKit.this.ac.higherKey(BeLiveEditKit.this.ag.get());
                    if (var2 != null) {
                        List var3 = (List)BeLiveEditKit.this.ac.get(var2);
                        Iterator var4 = var3.iterator();

                        while(var4.hasNext()) {
                            com.ksyun.media.shortvideo.timereffect.b var5 = (com.ksyun.media.shortvideo.timereffect.b)var4.next();
                            if (!var5.b) {
                                BeLiveEditKit.this.b(BeLiveEditKit.this.s, BeLiveEditKit.this.ai, var5);
                            }
                        }
                    }
                }

                BeLiveEditKit.this.ag.set(-1L);
                BeLiveEditKit.this.ah.set(-1L);
                if (BeLiveEditKit.this.mOnInfoListener != null) {
                    BeLiveEditKit.this.mOnInfoListener.onInfo(9);
                }

                Log.d(TAG, "complete mLastEventKey:" + BeLiveEditKit.this.ag.get());
            }
        });
        this.o.addListener(new GLRender.GLRenderListener() {
            public void onReady() {
                BeLiveEditKit.this.u.setEGL10Context(BeLiveEditKit.this.o.getEGL10Context());
            }

            public void onSizeChanged(int var1, int var2) {
            }

            public void onDrawFrame() {
            }

            public void onReleased() {
            }
        });
        this.o.init(1, 1);
        this.W = new com.ksyun.media.shortvideo.a.b.a();
        this.X = com.ksyun.media.shortvideo.a.c.a();
    }

    public GLRender getGLRender() {
        return this.o;
    }

    public boolean getIsLandscape() {
        return this.m >= this.n;
    }

    public ImgTexFilterMgt getImgTexFilterMgt() {
        return this.s;
    }

    public AudioFilterMgt getAudioFilterMgt() {
        return this.v;
    }

    public int getVideoEncodeMethod() {
        return this.mKSYComposeKit.getVideoEncodeMethod();
    }

    public ImgTexMixer getImgTexPreviewMixer() {
        return this.q;
    }

    public AudioPlayerCapture getAudioPlayerCapture() {
        return this.w;
    }

    public void setDisplayPreview(GLSurfaceView var1) {
        this.u.setDisplayPreview(var1);
        this.u.getGLRender().addListener(this.mGLRenderListener);
    }

    public void setDisplayPreview(TextureView var1) {
        this.u.setDisplayPreview(var1);
        this.u.getGLRender().addListener(this.mGLRenderListener);
    }

    public void setSpeed(float var1) {
        if (var1 > e) {
            var1 = e;
        }

        if (var1 < f) {
            var1 = f;
        }

        this.t.a(var1);
    }

    public float getSpeed() {
        return this.t.b();
    }

    public float getMAXSpeed() {
        return 2.0F;
    }

    public float getNomalSpeed() {
        return 1.0F;
    }

    public float getMINSpeed() {
        return 0.5F;
    }

    public void updateSpeed(boolean var1) {
        float var2 = this.t.b();
        if (var1) {
            var2 += 0.1F;
            if ((double)var2 > 2.0D) {
                var2 = 2.0F;
            }

            this.t.a(var2);
        } else {
            var2 = (float)((double)var2 - 0.1D);
            if ((double)var2 < 0.5D) {
                var2 = 0.5F;
            }

            this.t.a(var2);
        }

    }

    public void setEditPreviewUrl(String var1) {
        if (TextUtils.isEmpty(var1)) {
            throw new IllegalArgumentException("url can not be null");
        } else if (!FileUtils.isSupportedFile(var1)) {
            throw new IllegalArgumentException("the file must be mp4 or 3gpp or mov");
        } else {
            this.j = var1;
        }
    }

    public void setPreviewCrop(float var1, float var2, float var3, float var4) {
        if (this.getCropScale() != 0.0F) {
            float var5 = this.getCropScale();
            if (!this.getIsLandscape()) {
                var3 = 1.0F;
                var4 = 1.0F - var5;
            } else {
                var3 = 1.0F - var5;
                var4 = 1.0F;
            }

            this.q.setRectForCrop(0, var1, var2, var3, var4);
            this.mKSYComposeKit.getImgTexMixer().setRectForCrop(0, var1, var2, var3, var4);
            if (!this.t.a().isPlaying()) {
                this.t.k();
            }

        }
    }

    public void queueLastFrame() {
        this.t.k();
    }

    public RectF getPreviewCropRect() {
        return this.q.getRectForCrop(0);
    }

    public void setScalingMode(int var1) {
        this.q.setScalingMode(this.h, var1);
        this.mKSYComposeKit.setScalingMode(var1);
        if (this.T != null) {
            this.p.hideLogo();
            this.mKSYComposeKit.hideWaterMarkLogo();
            this.showWaterMarkLogo(this.T.a, this.T.b, this.T.c, this.T.d, this.T.e, this.T.f);
        }

    }

    public void setScaleType(int var1) {
        float var2 = DEFAULT_SCALE;
        if (var1 == SCALE_TYPE_9_16) {
            var2 = 0.5625F;
        } else if (var1 == SCALE_TYPE_3_4) {
            var2 = 0.75F;
        } else if (var1 == SCALE_TYPE_1_1) {
            var2 = 1.0F;
        }

        if (var2 != this.mKSYComposeKit.getScale()) {
            this.mKSYComposeKit.setScale(var2);
            if (this.T != null) {
                this.p.hideLogo();
                this.mKSYComposeKit.hideWaterMarkLogo();
            }

            if (this.mKSYComposeKit.getScaleMode() == 3) {
                this.q.setRectForCrop(0, 0.0F, 0.0F, 1.0F, 1.0F);
                this.mKSYComposeKit.getImgTexMixer().setRectForCrop(0, 0.0F, 0.0F, 1.0F, 1.0F);
            }
        }

    }

    public void setScale(float var1) {
        if (var1 != this.mKSYComposeKit.getScale()) {
            this.mKSYComposeKit.setScale(var1);
            this.p.hideLogo();
            this.mKSYComposeKit.hideWaterMarkLogo();
            if (this.mKSYComposeKit.getScaleMode() == 3) {
                this.q.setRectForCrop(0, 0.0F, 0.0F, 1.0F, 1.0F);
                this.mKSYComposeKit.getImgTexMixer().setRectForCrop(0, 0.0F, 0.0F, 1.0F, 1.0F);
            }
        }

    }

    public float getCropScale() {
        float var1 = (float)this.m / (float)this.n;
        float var2 = (float)this.k / (float)this.l;
        float var3;
        if (var1 > var2) {
            var3 = 1.0F - var2 / var1;
        } else {
            var3 = 1.0F - var1 / var2;
        }

        return var3;
    }

    public String getEditUrl() {
        return this.j;
    }

    public void startEditPreview() {
        if (TextUtils.isEmpty(this.j)) {
            throw new IllegalArgumentException("url can not be null");
        } else if (!FileUtils.isSupportedFile(this.j)) {
            throw new IllegalArgumentException("the file must be mp4 or 3gpp or mov");
        } else {
            if (this.k != 0 && this.l != 0) {
                this.g();
                this.t.b(false);
                this.t.a(1);
                this.t.a(this.j);
                this.x.start();
            } else {
                this.A = true;
            }

            if (this.N != null) {
                this.N.stopPreview(false);
            }

        }
    }

    public long getEditDuration() {
        return this.C;
    }

    public String getVideoCodecMeta() {
        if (this.getMediaPlayer() != null) {
            Bundle var1 = this.getMediaPlayer().getMediaMeta();
            KSYMediaMeta var2 = KSYMediaMeta.parse(var1);
            return var2.getVideoCodec();
        } else {
            return null;
        }
    }

    public void seekEditPreview(long var1) {
        if (this.t.a() != null && this.t.a().isPlaying()) {
            com.ksyun.media.shortvideo.capture.b.b var3 = this.t.c();
            if (var3 != null) {
                if (var1 < var3.startTime) {
                    var1 = var3.startTime;
                }

                if (var1 > var3.endTime) {
                    var1 = var3.endTime;
                }
            }

            if (var1 > this.C) {
                var1 = this.C;
            }

            if (var1 > this.C) {
                var1 = this.C;
            }

            if (var1 < 0L) {
                var1 = 0L;
            }

            this.t.a().seekTo(var1);
        }

    }

    public long getEditPreviewCurrentPosition() {
        return this.t.a() != null ? this.t.a().getCurrentPosition() : 0L;
    }

    public Bitmap getVideoThumbnailAtTime(long var1, int var3, int var4, boolean var5) {
        KSYProbeMediaInfo var6 = new KSYProbeMediaInfo();
        return var6.getVideoThumbnailAtTime(this.j, var1, var3, var4, var5);
    }

    public void setEditPreviewRanges(long var1, long var3, boolean var5) {
        if (var1 >= 0L && var3 >= var1) {
            if (var5) {
                this.x.stop();
                this.t.a(var1, var3);
                this.mKSYComposeKit.setComposeRanges(var1, var3);
                this.t.h();
                this.x.start();
            } else {
                this.t.a(var1, var3);
                this.mKSYComposeKit.setComposeRanges(var1, var3);
            }
        }

    }

    public void stopEditPreview() {
        if (this.N != null) {
            this.N.stopPreview(true);
        }

        this.x.stop();
        this.w.stop();
        this.t.g();
    }

    public void startCompose(String fileUrl) {
        this.I = fileUrl;
//        if (AuthInfoManager.getInstance().checkAuthFeature(AuthInfoManager.FEA_BASE)) {
//            Log.d(TAG, "auth success start compose:" + this.I);
            this.mKSYComposeKit.getAVDecoderCapture().setOnVideoPtsChangedListener(this.an);
            this.mKSYComposeKit.setScreenRenderSize(this.k, this.l);
            this.mKSYComposeKit.addPaint(this.R);
            this.c();
            this.f();
            this.b();
            this.mKSYComposeKit.setSpeed(this.t.b());
            this.mKSYComposeKit.setSrcUrl(this.j);
            File var2 = new File(this.I);
            if (TextUtils.isEmpty(this.Z) && TextUtils.isEmpty(this.Y) || !FileUtils.getMimeType(var2).equals(FileUtils.MIME_TYPE_MP4) && !FileUtils.getMimeType(var2).equals(FileUtils.MIME_TYPE_EXT_MP4)) {
                this.mKSYComposeKit.setDesUrl(this.I);
            } else {
                this.J = this.getTmpComposePath(this.I);
                this.mKSYComposeKit.setDesUrl(this.J);
            }

            this.i();
            this.U = System.currentTimeMillis();
            this.mKSYComposeKit.start();
            this.aa = true;
//        } else {
//            Log.e("BeLiveEditKit", "auth failed");
//            this.a(-1, 0L);
//        }

    }

    private void b() {
        this.W.k = 0;
        this.W.j = 0;
        this.mKSYComposeKit.getAudioFilterMgt().setFilter((AudioFilterBase)null);
        this.M.clear();
        List var1 = this.v.getFilter();
        if (var1 != null) {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
                if (var1.get(var2) instanceof KSYAudioEffectFilter) {
                    int var3 = ((KSYAudioEffectFilter)var1.get(var2)).getAudioEffectType();
                    KSYAudioEffectFilter var4 = new KSYAudioEffectFilter(var3);
                    if (var3 == KSYAudioEffectFilter.AUDIO_EFFECT_TYPE_PITCH) {
                        var4.setPitchLevel(var4.getPitchLevel());
                    }

                    this.M.add(var4);
                    this.W.k = 1;
                } else if (var1.get(var2) instanceof AudioReverbFilter) {
                    AudioReverbFilter var5 = new AudioReverbFilter();
                    var5.setReverbLevel(((AudioReverbFilter)var1.get(var2)).getReverbType());
                    this.M.add(var5);
                    this.W.j = 1;
                } else {
                    this.M.add((AudioFilterBase) var1.get(var2));
                }
            }

            this.mKSYComposeKit.getAudioFilterMgt().setFilter(this.M);
        }

    }

    private void c() {
        this.mKSYComposeKit.getImgTexFilterMgt().setFilter((ImgFilterBase)null);
        this.mKSYComposeKit.getImgTexFilterMgt().setExtraFilter((ImgFilterBase)null);
        this.e();
        this.K.clear();
        this.L.clear();
        List var1 = this.s.getFilter();
        List var2 = this.s.getExtraFilters();
        ImgBeautyStylizeFilter var3 = null;
        ImgBeautyStylizeFilter var4 = null;
        int var5;
        if (var1 != null) {
            for(var5 = 0; var5 < var1.size(); ++var5) {
                if (var1.get(var5) instanceof ImgBeautyStylizeFilter) {
                    var3 = (ImgBeautyStylizeFilter)var1.get(var5);
                } else {
                    this.K.add((ImgFilterBase) var1.get(var5));
                }
            }
        }

        if (var2 != null) {
            for(var5 = 0; var5 < var2.size(); ++var5) {
                if (var1.get(var5) instanceof ImgBeautyStylizeFilter) {
                    var4 = (ImgBeautyStylizeFilter)var1.get(var5);
                } else {
                    this.L.add((ImgFilterBase) var2.get(var5));
                }
            }
        }

        this.s.setFilter((ImgFilterBase)null);
        this.s.setExtraFilter((ImgFilterBase)null);
        ImgTexFilter var6;
        if (this.K != null) {
            for(var5 = 0; var5 < this.K.size(); ++var5) {
                if (this.K.get(var5) instanceof ImgTexFilterBase) {
                    var6 = (ImgTexFilter)this.K.get(var5);
                    var6.setGLRender(this.mKSYComposeKit.getGLRender());
                }

                if (this.K.get(var5) instanceof ImgBeautyProFilter) {
                    ((ImgBeautyProFilter)this.K.get(var5)).setGLRender(this.mKSYComposeKit.getGLRender());
                }

                if (this.K.get(var5) instanceof ImgBeautySmoothFilter) {
                    ((ImgBeautySmoothFilter)this.K.get(var5)).setGLRender(this.mKSYComposeKit.getGLRender());
                }

                if (this.K.get(var5) instanceof ImgBeautyStylizeFilter) {
                    ((ImgBeautyStylizeFilter)this.K.get(var5)).setGLRender(this.mKSYComposeKit.getGLRender());
                }
            }
        }

        if (this.L != null) {
            for(var5 = 0; var5 < this.L.size(); ++var5) {
                if (this.L.get(var5) instanceof ImgTexFilter) {
                    var6 = (ImgTexFilter)this.L.get(var5);
                    var6.setGLRender(this.mKSYComposeKit.getGLRender());
                }

                if (this.L.get(var5) instanceof ImgBeautyProFilter) {
                    ((ImgBeautyProFilter)this.K.get(var5)).setGLRender(this.mKSYComposeKit.getGLRender());
                }

                if (this.L.get(var5) instanceof ImgBeautySmoothFilter) {
                    ((ImgBeautySmoothFilter)this.K.get(var5)).setGLRender(this.mKSYComposeKit.getGLRender());
                }

                if (this.L.get(var5) instanceof ImgBeautyStylizeFilter) {
                    ((ImgBeautyStylizeFilter)this.K.get(var5)).setGLRender(this.mKSYComposeKit.getGLRender());
                }
            }
        }

        ImgBeautyStylizeFilter var7;
        if (var3 != null) {
            var7 = new ImgBeautyStylizeFilter(this.mKSYComposeKit.getGLRender(), this.g, var3.getStyleFilterId());
            this.K.add(var7);
        }

        if (var4 != null) {
            var7 = new ImgBeautyStylizeFilter(this.mKSYComposeKit.getGLRender(), this.g, var4.getStyleFilterId());
            this.L.add(var7);
        }

        this.mKSYComposeKit.getImgTexFilterMgt().setFilter(this.K);
        this.mKSYComposeKit.getImgTexFilterMgt().setExtraFilter(this.L);
        if (this.K != null && this.K.size() > 0 || this.L != null && this.L.size() > 0) {
            this.W.i = 1;
        } else {
            this.W.i = 0;
        }

    }

    private void d() {
        this.mKSYComposeKit.getImgTexFilterMgt().setFilter((ImgFilterBase)null);
        this.mKSYComposeKit.getImgTexFilterMgt().setExtraFilter((ImgFilterBase)null);
        this.ac.clear();
        this.ad.clear();
        this.ai.clear();
        this.ag.set(-1L);
        this.aa = false;
        this.mKSYComposeKit.getAVDecoderCapture().setOnVideoPtsChangedListener((AVDecoderCapture.OnVideoPtsChangedListener)null);
        this.P = null;
    }

    private void e() {
        if (this.ad != null && this.ad.size() > 0) {
            Iterator var1 = this.ad.values().iterator();

            while(var1.hasNext()) {
                TimerEffectInfo var2 = (TimerEffectInfo)var1.next();
                TimerEffectFilter var3 = (TimerEffectFilter)var2.effectData;
                ImgFilterBase var4 = var3.filter;
                if (this.s.getFilter().contains(var4)) {
                    this.s.replaceFilter(var4, (ImgFilterBase)null, false);
                }

                if (var4 instanceof ImgTexFilterBase) {
                    ((ImgTexFilterBase)var4).setGLRender(this.mKSYComposeKit.getGLRender());
                }

                if (var4 instanceof ImgBeautyProFilter) {
                    ((ImgBeautyProFilter)var4).setGLRender(this.mKSYComposeKit.getGLRender());
                }

                if (var4 instanceof ImgBeautySmoothFilter) {
                    ((ImgBeautySmoothFilter)var4).setGLRender(this.mKSYComposeKit.getGLRender());
                }
            }

            this.aj.clear();
        }
    }

    private void f() {
        if (this.N != null) {
            this.N.configSticker();
            TreeMap var1 = this.N.getStickerEvents();
            if (var1 != null && var1.size() > 0) {
                if ((this.N.getStickerUsingState() & 1) == 1) {
                    this.W.r = 1;
                }

                if ((this.N.getStickerUsingState() & 2) == 2) {
                    this.W.u = 1;
                }
            } else {
                this.mKSYComposeKit.hideSticker();
                this.W.r = 0;
                this.W.u = 0;
            }
        }

    }

    private void a(TreeMap<Long, List<com.ksyun.media.shortvideo.sticker.b>> var1, long var2) {
        if (var1 != null && var1.size() > 0) {
            long var4 = (Long)var1.firstKey();
            if (var4 <= var2) {
                List var6 = (List)var1.get(var4);
                LinkedList var7 = new LinkedList();
                boolean var8 = false;
                Iterator var9 = var6.iterator();

                while(var9.hasNext()) {
                    com.ksyun.media.shortvideo.sticker.b var10 = (com.ksyun.media.shortvideo.sticker.b)var9.next();
                    if (var10.a.c() == 4) {
                        var8 = true;
                    }

                    if (var10.c) {
                        var7.add(var10.a);
                    }
                }

                com.ksyun.media.shortvideo.sticker.c var18 = this.N.saveStickers(var7);
                if (var18 != null) {
                    if (this.a(var18)) {
                        if (this.Q == null) {
                            this.Q = new RectF(0.0F, 0.0F, 1.0F, 1.0F);
                        }

                        float var19 = var18.b / (float)this.k;
                        float var11 = var18.c / (float)this.l;
                        float var12 = var18.d / (float)this.k;
                        float var13 = var18.e / (float)this.l;
                        float var14 = var19;
                        float var15 = var11;
                        if (this.mKSYComposeKit.getScaleMode() == 0 && this.m != 0 && this.k != 0) {
                            float var16 = (float)this.m / (float)this.n;
                            float var17 = (float)this.k / (float)this.l;
                            if (var16 < var17) {
                                var14 = (var18.b * 2.0F - (float)this.k + (float)this.l * var16) / ((float)(2 * this.l) * var16);
                                var12 = 0.0F;
                            } else if (var16 > var17) {
                                var15 = (var18.c * var16 * 2.0F - (float)this.l * var16 + (float)this.k) / (float)(2 * this.k);
                                var13 = 0.0F;
                            }
                        }

                        this.Q.set(var14, var15, var14 + var12, var15 + var13);
                    }

                    this.P = var18;
                    if (this.P != null && this.Q != null) {
                        this.mKSYComposeKit.showSticker(this.P.a, this.Q.left, this.Q.top, this.Q.width(), this.Q.height(), 1.0F);
                    } else {
                        this.mKSYComposeKit.hideSticker();
                    }

                    if (!var8) {
                        var1.remove(var4);
                    } else {
                        this.a(var1, var4, var2);
                    }
                }
            }
        }

    }

    private boolean a(com.ksyun.media.shortvideo.sticker.c var1) {
        if (this.P != null) {
            if (this.P.b != var1.b) {
                return true;
            } else if (this.P.c != var1.c) {
                return true;
            } else if (this.P.d != var1.d) {
                return true;
            } else {
                return this.P.e != var1.e;
            }
        } else {
            return true;
        }
    }

    private void a(TreeMap<Long, List<com.ksyun.media.shortvideo.sticker.b>> var1, long var2, long var4) {
        List var6 = (List)var1.get(var2);
        int var7 = 0;
        long var8 = 9223372036854775807L;
        Iterator var10 = var1.keySet().iterator();

        while(var10.hasNext()) {
            long var11 = (Long)var10.next();
            ++var7;
            if (var7 == 2) {
                if (((List)var1.get(var11)).size() > 0) {
                    var8 = var11;
                }
                break;
            }
        }

        if (var8 <= var4) {
            var1.remove(var2);
        } else {
            var10 = var6.iterator();

            while(var10.hasNext()) {
                com.ksyun.media.shortvideo.sticker.b var13 = (com.ksyun.media.shortvideo.sticker.b)var10.next();
                if (var13.a.k() < var4) {
                    var13.a.e();
                    var10.remove();
                }
            }

            if (var6.size() <= 0) {
                var1.remove(var2);
            }

        }
    }

    public int getProgress() {
        return this.mKSYComposeKit != null ? (int)this.mKSYComposeKit.getProgress() : 0;
    }

    public void stopCompose() {
        if (this.mKSYComposeKit != null) {
            this.mKSYComposeKit.stopCompose();
        }

        if (this.H != null) {
            this.H.stop();
        }

    }

    public void setVideoFps(float var1) {
        this.mKSYComposeKit.setVideoFps(var1);
    }

    public void setIFrameInterval(float var1) {
        if (var1 <= 0.0F) {
            throw new IllegalArgumentException("I frame interval must be positive");
        } else {
            this.mKSYComposeKit.setIFrameInterval(var1);
        }
    }

    public void setVideoCrf(int var1) {
        this.mKSYComposeKit.setVideoCrf(var1);
    }

    public void setVideoBitrate(int var1) {
        this.mKSYComposeKit.setVideoBitrate(var1);
    }

    public void setVideoKBitrate(int var1) {
        this.setVideoBitrate(var1 * 1000);
    }

    public void setAudioBitrate(int var1) {
        this.mKSYComposeKit.setAudioBitrate(var1);
    }

    public void setAudioKBitrate(int var1) {
        this.setAudioBitrate(var1 * 1000);
    }

    public void setTargetResolution(int var1, int var2) {
        this.mKSYComposeKit.setTargetResolution(var1, var2);
    }

    public void setTargetResolution(int var1) {
        this.mKSYComposeKit.setTargetResolution(var1);
    }

    public void setVideoCodecId(int var1) {
        this.mKSYComposeKit.setVideoCodecId(var1);
    }

    public void setEncodeMethod(int var1) {
        this.mKSYComposeKit.setEncodeMethod(var1);
    }

    public void setAudioEncodeMethod(int var1) {
        this.mKSYComposeKit.setAudioEncodeMethod(var1);
    }

    public void setVideoEncodeMethod(int var1) {
        this.mKSYComposeKit.setVideoEncodeMethod(var1);
    }

    public void setDecoderMethod(int var1) {
        this.mKSYComposeKit.setVideoDecodeMethod(var1);
        this.mKSYComposeKit.setAudioDecodeMethod(var1);
    }

    public void setVideoDecodeMethod(int var1) {
        this.mKSYComposeKit.setVideoDecodeMethod(var1);
    }

    public void setAuidoDecodeMethod(int var1) {
        this.mKSYComposeKit.setAudioDecodeMethod(var1);
    }

    public void setDecodeMethod(int var1) {
        this.mKSYComposeKit.setDecodeMethod(var1);
    }

    public void setVideoEncodeProfile(int var1) {
        this.mKSYComposeKit.setVideoEncodeProfile(var1);
    }

    public void setForceVideoFrameFirst(boolean var1) {
        this.mKSYComposeKit.setForceVideoFrameFirst(var1);
    }

    public void setOnInfoListener(BeLiveEditKit.OnInfoListener var1) {
        this.mOnInfoListener = var1;
    }

    public void setOnErrorListener(BeLiveEditKit.OnErrorListener var1) {
        this.mOnErrorListener = var1;
    }

    private void a(final int var1, final long var2) {
        if (this.z != null) {
            this.z.post(new Runnable() {
                public void run() {
                    if (BeLiveEditKit.this.mOnErrorListener != null) {
                        BeLiveEditKit.this.mOnErrorListener.onError(var1, var2);
                    }

                }
            });
        }

    }

    private void a(final int var1, final String var2) {
        this.z.post(new Runnable() {
            public void run() {
                if (BeLiveEditKit.this.mOnInfoListener != null) {
                    String[] var1x = new String[]{var2};
                    BeLiveEditKit.this.mOnInfoListener.onInfo(var1, var1x);
                }

            }
        });
    }

    private void g() {
        this.p.setTargetSize(this.k, this.l);
        this.q.setTargetSize(this.k, this.l);
        this.r.setTargetSize(this.m, this.n);
    }

    public void onResume() {
        Log.d(TAG, "onResume");
        if (this.N != null) {
            this.N.stopPreview(false);
        }

        this.u.onResume();
        this.x.start();
        if (this.t.a() != null && this.t.a().isPlayable() && !this.t.a().isPlaying() && this.B) {
            this.B = false;
            this.t.a().start();
        }

    }

    public void onPause() {
        Log.d(TAG, "onPause");
        if (this.N != null) {
            this.N.stopPreview(true);
        }

        this.u.onPause();
        this.x.stop();
        if (this.t.a() != null && this.t.a().isPlaying()) {
            this.B = true;
            this.t.a().pause();
        }

    }

    public void resumeEditPreview() {
        if (this.N != null) {
            this.N.stopPreview(false);
        }

        this.u.onResume();
        this.t.a().start();
        this.w.resume();
        this.x.start();
    }

    public void pauseEditPreview() {
        if (this.N != null) {
            this.N.stopPreview(true);
        }

        this.u.onPause();
        this.t.a().pause();
        this.w.pause();
        this.x.stop();
    }

    public void enableOriginAudio(boolean var1) {
        this.t.e(!var1);
        this.mKSYComposeKit.enableOriginAudio(var1);
    }

    public void setOriginAudioVolume(float var1) {
        this.D = var1;
        this.y.setInputVolume(0, var1);
        this.mKSYComposeKit.setAudioVolume(this.mKSYComposeKit.mIdxAudioOrigin, var1);
    }

    public void setOriginAudioVolume(float var1, float var2) {
        this.D = var1;
        this.y.setInputVolume(0, var1, var2);
        this.mKSYComposeKit.setAudioVolume(this.mKSYComposeKit.mIdxAudioOrigin, var1, var2);
    }

    public float getOriginAudioVolume() {
        return this.y.getInputVolume(0);
    }

    public boolean startBgm(String var1, boolean var2) {
        if (!AuthInfoManager.getInstance().checkAuthFeature(AuthInfoManager.FEA_EDIT_BGM)) {
            Log.w("BeLiveEditKit", "start bgm failed," + AuthInfoManager.FEA_FAILED_LOG);
            return false;
        } else {
            this.S = var1;
            if (TextUtils.isEmpty(var1)) {
                this.w.stop();
            } else {
                this.w.start(var1, var2);
            }

            this.mKSYComposeKit.setBgmMusicPath(var1);
            return true;
        }
    }

    public void stopBgm() {
        this.w.stop();
        this.mKSYComposeKit.setBgmMusicPath((String)null);
    }

    public void setBGMRanges(long var1, long var3, boolean var5) {
        if (var1 >= 0L && var3 >= var1) {
            this.w.setPlayableRanges(var1, var3);
            this.mKSYComposeKit.setBGMRanges(var1, var3);
            if (var5) {
                this.w.restart();
            }
        }

    }

    public void seekBGM(long var1) {
        if (this.w.getMediaPlayer() != null && this.t.a().isPlaying()) {
            AudioPlayerCapture.PlayRanges var3 = this.w.getPlayableRanges();
            if (var3 != null) {
                if (var1 < var3.startTime) {
                    var1 = var3.startTime;
                }

                if (var1 > var3.endTime) {
                    var1 = var3.endTime;
                }
            }

            if (var1 > this.C) {
                var1 = this.C;
            }

            if (var1 < 0L) {
                var1 = 0L;
            }

            this.w.getMediaPlayer().seekTo(var1);
        }

    }

    public void setBgmVolume(float var1) {
        this.w.setVolume(var1);
        this.mKSYComposeKit.setAudioVolume(this.mKSYComposeKit.mIdxAudioBgm, var1);
    }

    public float getBgmVolume() {
        return this.w.getVolume();
    }

    public void setLooping(boolean var1) {
        if (this.t != null) {
            this.t.d(var1);
        }

    }

    public void pausePlay(boolean var1) {
        if (var1) {
            if (this.ab) {
                this.t.g(false);
            }

            this.t.a().pause();
            this.w.getMediaPlayer().pause();
            this.x.pause();
        } else {
            if (this.ab) {
                this.t.h();
                this.t.g(true);
            } else {
                this.t.a().start();
            }

            this.w.getMediaPlayer().start();
            this.x.resume();
        }

        this.u.setKeepFrameOnResume(var1);
    }

    public KSYMediaPlayer getMediaPlayer() {
        return this.t.a();
    }

    public void showWaterMarkLogo(String var1, float var2, float var3, float var4, float var5, float var6) {
        if (this.T == null) {
            this.T = new BeLiveEditKit.b(null);
        }

        this.T.a = var1;
        this.T.b = var2;
        this.T.c = var3;
        this.T.d = var4;
        this.T.e = var5;
        this.T.f = var6;
        var6 = Math.max(0.0F, var6);
        var6 = Math.min(var6, 1.0F);
        float var7 = var2;
        float var8 = var3;
        if (this.m != 0 && this.k != 0) {
            float var9 = (float)this.m / (float)this.n;
            float var10 = (float)this.k / (float)this.l;
            float var11;
            if (var9 < var10) {
                var11 = (1.0F - var9 / var10) / 2.0F;
                var7 = var2 * var9 / var10 + var11;
            } else if (var9 > var10) {
                var11 = (1.0F - var10 / var9) / 2.0F;
                var8 = var3 * var10 / var9 + var11;
            }
        }

        this.p.showLogo(this.g, var1, var4, var5);
        if (this.mKSYComposeKit.getScaleMode() == 0) {
            this.q.setRenderRect(this.i, var7, var8, var4, var5, var6);
            this.mKSYComposeKit.showWaterMarkLogo(var1, var2, var3, var4, var5, var6);
        } else if (this.mKSYComposeKit.getScaleMode() == 1) {
            this.q.setRenderRect(this.i, var7, var8, var4, var5, var6);
            this.mKSYComposeKit.showWaterMarkLogo(var1, var7, var8, var4, var5, var6);
        } else {
            this.q.setRenderRect(this.i, var2, var3, var4, var5, var6);
            this.mKSYComposeKit.showWaterMarkLogo(var1, var2, var3, var4, var5, var6);
        }

    }

    public void addStickerView(KSYStickerView var1) {
        this.N = var1;
    }

    private void a(long var1) {
        if (this.N != null) {
            if (this.t.a().isPlaying()) {
                boolean var3 = this.N.updateStickerDraw(var1);
                if (var3) {
                    Message var4 = this.z.obtainMessage(2);
                    this.z.sendMessage(var4);
                }
            }

        }
    }

    public void updateStickerDraw() {
        if (this.N != null) {
            boolean var1 = this.N.updateStickerDraw(this.t.a().getCurrentPosition());
            if (var1) {
                Message var2 = this.z.obtainMessage(2);
                this.z.sendMessage(var2);
            }

        }
    }

    private void b(long var1) {
        if (this.N != null) {
            TreeMap var3 = this.N.getStickerEvents();
            if (var3 != null && var3.size() > 0) {
                this.a(var3, var1);
            }

        }
    }

    private void c(long var1) {
        if (this.af) {
            if (this.ad != null) {
                Iterator var10 = this.ad.values().iterator();

                while(true) {
                    while(var10.hasNext()) {
                        TimerEffectInfo var12 = (TimerEffectInfo)var10.next();
                        TimerEffectFilter var14 = (TimerEffectFilter)var12.effectData;
                        if (var12.startTime <= var1 && var12.endTime > var1) {
                            if (!this.s.getFilter().contains(var14.filter)) {
                                this.s.addFilter(var14.filter);
                            }
                        } else if (this.s.getFilter().contains(var14.filter)) {
                            this.s.replaceFilter(var14.filter, (ImgFilterBase)null, false);
                        }
                    }

                    return;
                }
            }
        } else if (!this.ae) {
            if (this.ac.size() > 0) {
                if (!this.aa) {
                    Iterator var13;
                    if (this.ah.get() != -1L && var1 < this.ah.get()) {
                        Iterator var4 = this.ai.keySet().iterator();

                        label156:
                        while(true) {
                            TimerEffectInfo var6;
                            TimerEffectFilter var7;
                            do {
                                if (!var4.hasNext()) {
                                    var13 = this.ad.values().iterator();

                                    while(true) {
                                        while(true) {
                                            do {
                                                do {
                                                    do {
                                                        if (!var13.hasNext()) {
                                                            Iterator var15 = this.ac.keySet().iterator();

                                                            while(var15.hasNext()) {
                                                                Long var17 = (Long)var15.next();
                                                                if (var17 > var1) {
                                                                    Long var19 = (Long)this.ac.lowerKey(var17);
                                                                    if (var19 != null) {
                                                                        this.ag.set((Long)this.ac.lowerKey(var17));
                                                                        Log.d(TAG, "seek set mLastEventKey:" + this.ag.get());
                                                                    } else {
                                                                        this.ag.set(-1L);
                                                                        Log.d(TAG, "seek set mLastEventKey:" + this.ag.get());
                                                                    }
                                                                    break label156;
                                                                }
                                                            }
                                                            break label156;
                                                        }

                                                        var6 = (TimerEffectInfo)var13.next();
                                                        var7 = (TimerEffectFilter)var6.effectData;
                                                    } while(var6.startTime >= var1);
                                                } while(var6.endTime <= var1);

                                                if (!this.s.getFilter().contains(var7.filter)) {
                                                    if (this.ai.size() >= 1) {
                                                        com.ksyun.media.shortvideo.timereffect.b var8 = (com.ksyun.media.shortvideo.timereffect.b)this.ai.get(this.ai.lastKey());
                                                        if (var8.c.getId() < var6.id) {
                                                            ImgFilterBase var9 = ((TimerEffectFilter)var8.c).filter;
                                                            this.s.replaceFilter(var9, var7.filter, false);
                                                            Log.d(TAG, "seek show replace filter:" + var8.c.getId() + "~" + var6.id);
                                                        } else if (var8.c.getId() == var6.id) {
                                                            this.s.addFilter(var7.filter);
                                                            Log.d(TAG, "seek add filter:" + var6.id);
                                                        }
                                                    } else {
                                                        this.s.addFilter(var7.filter);
                                                        Log.d(TAG, "seek add filter:" + var6.id);
                                                    }
                                                }
                                            } while(this.ai.containsKey(var6.id));

                                            List var18 = (List)this.ac.get(var6.startTime);

                                            for(int var20 = 0; var20 < var18.size(); ++var20) {
                                                if (((com.ksyun.media.shortvideo.timereffect.b)var18.get(var20)).c.getId() == var6.id && ((com.ksyun.media.shortvideo.timereffect.b)var18.get(var20)).b) {
                                                    this.ai.put(var6.id, (com.ksyun.media.shortvideo.timereffect.b) var18.get(var20));
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                                int var5 = (Integer)var4.next();
                                var6 = (TimerEffectInfo)this.ad.get(var5);
                                var7 = (TimerEffectFilter)var6.effectData;
                            } while(var6.startTime <= var1 && var6.endTime >= var1);

                            if (this.s.getFilter().contains(var7.filter)) {
                                this.s.replaceFilter(var7.filter, (ImgFilterBase)null, false);
                            }

                            Log.d(TAG, "seek hide replace" + var6.id + ":" + var6.startTime + "~" + var6.endTime);
                            var4.remove();
                        }
                    }

                    Long var3;
                    if (this.ag.get() == -1L) {
                        var3 = (Long)this.ac.firstKey();
                    } else {
                        var3 = (Long)this.ac.higherKey(this.ag.get());
                        if (var3 == null) {
                            return;
                        }
                    }

                    if (var3 <= var1) {
                        Log.d(TAG, "update timer effect filter:" + var3 + "~" + var1);
                        List var11 = (List)this.ac.get(var3);
                        var13 = var11.iterator();

                        while(var13.hasNext()) {
                            com.ksyun.media.shortvideo.timereffect.b var16 = (com.ksyun.media.shortvideo.timereffect.b)var13.next();
                            if (var16.b) {
                                this.a(this.s, this.ai, var16);
                            } else {
                                this.b(this.s, this.ai, var16);
                            }
                        }

                        this.ag.set(var3);
                        Log.d(TAG, "update set mLastEventKey:" + this.ag.get());
                    }

                    this.ah.set(var1);
                    if (!this.t.a().isPlaying()) {
                        this.t.k();
                    }

                }
            }
        }
    }

    private void d(long var1) {
        if (this.ac != null && this.ac.size() > 0) {
            long var3 = (Long)this.ac.firstKey();
            if (var3 <= var1) {
                List var5 = (List)this.ac.get(var3);
                if (var5 != null) {
                    for(int var6 = 0; var6 < var5.size(); ++var6) {
                        com.ksyun.media.shortvideo.timereffect.b var7 = (com.ksyun.media.shortvideo.timereffect.b)var5.get(var6);
                        if (var7.b) {
                            this.a(this.mKSYComposeKit.getImgTexFilterMgt(), this.aj, var7);
                        } else {
                            this.b(this.mKSYComposeKit.getImgTexFilterMgt(), this.aj, var7);
                        }
                    }
                }

                this.ac.remove(var3);
            }

        }
    }

    private void a(ImgTexFilterMgt var1, TreeMap<Integer, com.ksyun.media.shortvideo.timereffect.b> var2, com.ksyun.media.shortvideo.timereffect.b var3) {
        ImgFilterBase var4 = ((TimerEffectFilter)var3.c).filter;
        if (!var1.getFilter().contains(var4)) {
            if (!this.af) {
                if (var2.size() > 0) {
                    com.ksyun.media.shortvideo.timereffect.b var5 = (com.ksyun.media.shortvideo.timereffect.b)var2.get(var2.lastKey());
                    if (var5.c.getId() < var3.c.getId()) {
                        ImgFilterBase var6 = ((TimerEffectFilter)var5.c).filter;
                        var1.replaceFilter(var6, var4, false);
                        Log.d(TAG, "update replace filter:" + var5.c.getId() + "~" + var3.c.getId());
                    }
                } else {
                    var1.addFilter(var4);
                    Log.d(TAG, "update add Filter:" + var3.c.getId());
                }
            } else {
                var1.addFilter(var4);
            }
        }

        if (!var2.containsKey(var3.c.getId())) {
            var2.put(var3.c.getId(), var3);
        }

    }

    private void b(ImgTexFilterMgt var1, TreeMap<Integer, com.ksyun.media.shortvideo.timereffect.b> var2, com.ksyun.media.shortvideo.timereffect.b var3) {
        ImgFilterBase var4 = ((TimerEffectFilter)var3.c).filter;
        if (var1.getFilter().contains(var4)) {
            if (!this.af) {
                if (var2.size() > 1) {
                    com.ksyun.media.shortvideo.timereffect.b var5 = (com.ksyun.media.shortvideo.timereffect.b)var2.get(var2.lowerKey(var3.c.getId()));
                    ImgFilterBase var6 = ((TimerEffectFilter)var5.c).filter;
                    var1.replaceFilter(var4, var6, false);
                    Log.d(TAG, "update replace filter:" + var5.c.getId() + "~" + var3.c.getId());
                } else {
                    var1.replaceFilter(var4, (ImgFilterBase)null, false);
                }
            } else {
                var1.replaceFilter(var4, (ImgFilterBase)null, false);
            }
        }

        if (var2.containsKey(var3.c.getId())) {
            var2.remove(var3.c.getId());
        }

    }

    public void setTimerEffectOverlying(boolean var1) {
        this.af = var1;
    }

    public int addTimerEffectFilter(TimerEffectInfo var1) {
        if (!AuthInfoManager.getInstance().checkAuthFeature(AuthInfoManager.FEA_EFFECT_FILTER)) {
            Log.w("BeLiveEditKit", " you do not have permission for effect filter");
            return -1;
        } else if (var1.effectData instanceof TimerEffectFilter) {
            this.ae = true;
            var1.effectData.setId(var1.id);
            this.ad.put(var1.id, var1);
            long var2 = var1.startTime;
            long var4 = var1.endTime;
            com.ksyun.media.shortvideo.timereffect.b var6 = new com.ksyun.media.shortvideo.timereffect.b(var2, true, var1.effectData);
            com.ksyun.media.shortvideo.timereffect.b var7 = new com.ksyun.media.shortvideo.timereffect.b(var4, false, var1.effectData);
            LinkedList var8;
            List var9;
            if (!this.ac.containsKey(var2)) {
                var8 = new LinkedList();
                var8.add(var6);
                this.ac.put(var2, var8);
            } else {
                var9 = (List)this.ac.get(var2);
                var9.add(var6);
            }

            Log.d(TAG, "mLastEventKey:" + this.ag.get() + "~" + var2);
            if (this.ag.get() != -1L && this.ag.get() <= var2) {
                this.a(this.s, this.ai, var6);
                this.ag.set(var2);
            }

            if (!this.ac.containsKey(var4)) {
                var8 = new LinkedList();
                var8.add(var7);
                this.ac.put(var4, var8);
            } else {
                var9 = (List)this.ac.get(var4);
                var9.add(var7);
            }

            this.ae = false;
            return var1.id;
        } else {
            return -1;
        }
    }

    public TimerEffectInfo getTimerEffectInfo(int var1) {
        return this.ad.containsKey(var1) ? (TimerEffectInfo)this.ad.get(var1) : null;
    }

    public void updateTimerEffectEndTime(int var1, long var2) {
        this.ae = true;
        TimerEffectInfo var4 = (TimerEffectInfo)this.ad.get(var1);
        if (var4 == null) {
            Log.w("BeLiveEditKit", "the wrong update filter index");
        } else {
            long var5 = var4.endTime;
            var4.endTime = var2;
            List var7 = (List)this.ac.get(var5);
            Iterator var8 = var7.iterator();
            com.ksyun.media.shortvideo.timereffect.b var9 = null;

            while(var8.hasNext()) {
                com.ksyun.media.shortvideo.timereffect.b var10 = (com.ksyun.media.shortvideo.timereffect.b)var8.next();
                if (var10.c.getId() == var1 && !var10.b) {
                    var8.remove();
                    var9 = var10;
                    break;
                }
            }

            if (var7.size() <= 0) {
                this.ac.remove(var5);
            }

            if (this.af) {
                if (var9 != null) {
                    var9.a = var2;
                    if (!this.ac.containsKey(var2)) {
                        LinkedList var19 = new LinkedList();
                        var19.add(var9);
                        this.ac.put(var2, var19);
                    } else {
                        List var20 = (List)this.ac.get(var2);
                        var20.add(var9);
                    }
                }

                this.ae = false;
            } else {
                long var18 = this.t.a().getCurrentPosition();
                Log.d(TAG, "update filter end time " + var1 + ":" + var2 + "~" + var18);
                if (var2 <= var18) {
                    ImgFilterBase var12 = ((TimerEffectFilter)var4.effectData).filter;
                    if (this.ai.size() > 0) {
                        Integer var13 = (Integer)this.ai.lastKey();
                        if (var13 != null && var1 == var13) {
                            if (this.s.getFilter().contains(var12)) {
                                if (this.ai.size() > 1) {
                                    com.ksyun.media.shortvideo.timereffect.b var14 = (com.ksyun.media.shortvideo.timereffect.b)this.ai.get(var13);
                                    Integer var15 = (Integer)this.ai.lowerKey(var13);
                                    com.ksyun.media.shortvideo.timereffect.b var16 = (com.ksyun.media.shortvideo.timereffect.b)this.ai.get(var15);
                                    ImgFilterBase var17 = ((TimerEffectFilter)var16.c).filter;
                                    this.s.replaceFilter(var12, var17, false);
                                    Log.d(TAG, "replace filter:" + var14.c.getId() + "~" + var16.c.getId());
                                } else {
                                    this.s.replaceFilter(var12, (ImgFilterBase)null, false);
                                }
                            }

                            this.ai.remove(var13);
                            this.ag.set(var2);
                            Log.d(TAG, "update end mLastEventKey:" + this.ag.get());
                        }
                    }
                }

                if (var9 != null) {
                    var9.a = var2;
                    if (!this.ac.containsKey(var2)) {
                        LinkedList var21 = new LinkedList();
                        var21.add(var9);
                        this.ac.put(var2, var21);
                    } else {
                        List var22 = (List)this.ac.get(var2);
                        var22.add(var9);
                    }
                }

                this.ae = false;
            }
        }
    }

    public void removeAllTimeEffectFilter() {
        if (this.ad.size() > 0) {
            this.ae = true;
            Iterator var1 = this.ai.values().iterator();

            while(var1.hasNext()) {
                com.ksyun.media.shortvideo.timereffect.b var2 = (com.ksyun.media.shortvideo.timereffect.b)var1.next();
                ImgFilterBase var3 = ((TimerEffectFilter)var2.c).filter;
                if (this.s.getFilter().contains(var3)) {
                    this.s.replaceFilter(var3, (ImgFilterBase)null);
                }
            }

            this.ai.clear();
            this.ac.clear();
            this.ad.clear();
            this.ag.set(-1L);
            this.ae = false;
        }
    }

    public void removeTimerEffectFilter(int var1) {
        if (this.ad.size() > 0) {
            TimerEffectInfo var2 = (TimerEffectInfo)this.ad.get(var1);
            if (var2 == null) {
                Log.d(TAG, "the wrong remove filter index");
            } else {
                this.ae = true;
                TimerEffectFilter var3 = (TimerEffectFilter)var2.effectData;
                if (this.s.getFilter().contains(var3.filter)) {
                    this.s.replaceFilter(var3.filter, (ImgFilterBase)null, false);
                }

                if (this.ad.size() > 1) {
                    int var4 = (Integer)this.ad.lowerKey(var1);
                    TimerEffectInfo var5 = (TimerEffectInfo)this.ad.get(var4);
                    this.seekTo(var5.endTime);
                } else {
                    this.seekTo(var2.startTime);
                }

                this.ad.remove(var1);
                List var10 = (List)this.ac.get(var2.startTime);
                List var11 = (List)this.ac.get(var2.endTime);
                Iterator var6 = var10.iterator();

                com.ksyun.media.shortvideo.timereffect.b var8;
                while(var6.hasNext()) {
                    com.ksyun.media.shortvideo.timereffect.b var7 = (com.ksyun.media.shortvideo.timereffect.b)var6.next();
                    if (var7.c.getId() == var2.id) {
                        var6.remove();
                        if (this.ai.containsKey(var7.c.getId())) {
                            this.ai.remove(var7.c.getId());
                        }

                        if (this.ai.size() > 0) {
                            var8 = (com.ksyun.media.shortvideo.timereffect.b)this.ai.get(this.ai.lastKey());
                            ImgFilterBase var9 = ((TimerEffectFilter)var8.c).filter;
                            if (!this.s.getFilter().contains(var9)) {
                                this.s.getFilter().add(var9);
                            }
                        }
                        break;
                    }
                }

                if (var10.size() <= 0) {
                    this.ac.remove(var2.startTime);
                }

                Iterator var12 = var11.iterator();

                while(var12.hasNext()) {
                    var8 = (com.ksyun.media.shortvideo.timereffect.b)var12.next();
                    if (var8.c.getId() == var2.id) {
                        var12.remove();
                        break;
                    }
                }

                if (var11.size() <= 0) {
                    this.ac.remove(var2.endTime);
                }

                this.ae = false;
            }
        }
    }

    public int getTimeEffectCount() {
        return this.ad.size();
    }

    public void setEnableMp4FastStart(boolean var1) {
        this.mKSYComposeKit.setEnableMp4FastStart(var1);
    }

    public void addPaintView(View var1) {
        this.R = var1;
    }

    public void seekTo(long var1) {
        if (var1 > this.C) {
            var1 = this.C;
        }

        if (var1 < 0L) {
            var1 = 0L;
        }

        if (this.t.a() != null) {
            this.t.a().seekTo(var1, true);
        }

    }

    public void hideWaterMarkLogo() {
        this.T = null;
        this.p.hideLogo();
        this.mKSYComposeKit.hideWaterMarkLogo();
    }

    public static String getVersion() {
        return "2.2.1.3";
    }

    public void release() {
        if (this.z != null) {
            this.z.removeCallbacksAndMessages((Object)null);
            this.z = null;
        }

        if (this.O != null) {
            this.O.cancel();
            this.O = null;
        }

        if (this.N != null) {
            this.N.release();
        }

        if (this.H != null) {
            this.H.setOnInfoListener((com.ksyun.media.shortvideo.kit.KSYMergeKit.OnInfoListener)null);
            this.H.setOnErrorListener((com.ksyun.media.shortvideo.kit.KSYMergeKit.OnErrorListener)null);
            this.H.release();
        }

        this.mKSYComposeKit.setOnInfoListener((BeLiveComposeKit.b)null);
        this.mKSYComposeKit.setOnErrorListener((BeLiveComposeKit.a)null);
        this.mKSYComposeKit.release();
        this.p.release();
        this.w.release();
        this.t.a((IMediaPlayer.OnPreparedListener)null);
        this.t.a((com.ksyun.media.player.IMediaPlayer.OnInfoListener)null);
        this.t.a((IMediaPlayer.OnCompletionListener)null);
        this.t.a((com.ksyun.media.player.IMediaPlayer.OnErrorListener)null);
        this.t.i();
        this.o.release();
    }

    private void h() {
        this.mKSYComposeKit.release();
        this.mKSYComposeKit = new BeLiveComposeKit(this.g);
        this.mKSYComposeKit.setOnErrorListener(this.al);
        this.mKSYComposeKit.setOnInfoListener(this.ak);
    }

    private void i() {
        if ((long)this.mKSYComposeKit.getPlayRanges() < this.C) {
            this.W.s = 1;
        } else {
            this.W.s = 0;
        }

        if (!TextUtils.isEmpty(this.S) && this.mKSYComposeKit.getBGMRanges() != null && this.mKSYComposeKit.getBGMRanges().endTime - this.mKSYComposeKit.getBGMRanges().startTime < this.w.getFileDuration()) {
            this.W.t = 1;
        } else {
            this.W.t = 0;
        }

        this.W.a = this.X.a(this.mKSYComposeKit.getVideoBitrate());
        this.W.b = this.X.a(this.mKSYComposeKit.getAudioBitrate());
        this.W.d = this.mKSYComposeKit.getVideoFps();
        this.W.p = this.X.d(this.mKSYComposeKit.getVideoEncodeMethod(), this.mKSYComposeKit.getVideoEncodeProfile());
        this.W.e = this.X.b(this.mKSYComposeKit.getVideoEncodeMethod(), this.mKSYComposeKit.getVideoCodecId());
        this.W.v = this.t.b();
    }

    private void a(boolean var1, int var2) {
        this.W.c = this.X.a(this.mKSYComposeKit.getTargetWidth(), this.mKSYComposeKit.getTargetHeight());
        this.V = System.currentTimeMillis() - this.U;
        long var3 = this.C;
        if (this.mKSYComposeKit.getPlayRanges() > 0) {
            var3 = (long)this.mKSYComposeKit.getPlayRanges();
        }

        this.X.a(var1, var2, var3, this.V, this.W);
    }

    public void setTitleUrl(String var1) {
        this.Y = var1;
    }

    public void setTailUrl(String var1) {
        this.Z = var1;
    }

    private void a(String var1) {
        HashMap var2 = new HashMap();
        LinkedList var3 = new LinkedList();
        var3.add(var1);
        var2.put(var1, false);
        File var4;
        if (!TextUtils.isEmpty(this.Y)) {
            var4 = new File(this.Y);
            if (var4.exists()) {
                var3.add(0, this.Y);
                var2.put(this.Y, true);
            }
        }

        if (!TextUtils.isEmpty(this.Z)) {
            var4 = new File(this.Z);
            if (var4.exists()) {
                var3.add(this.Z);
                var2.put(this.Z, true);
            }
        }

        AudioCodecFormat var5 = (AudioCodecFormat)this.mKSYComposeKit.getAudioEncodeFormat();
        if (this.H == null) {
            this.H = new KSYMergeKit(this.g);
            this.H.setOnInfoListener(this.ao);
            this.H.setOnErrorListener(this.ap);
        }

        this.H.setEncodeMethod(this.mKSYComposeKit.getVideoEncodeMethod());
        this.H.setTargetResolution(this.mKSYComposeKit.getTargetWidth(), this.mKSYComposeKit.getTargetHeight());
        this.H.setVideoBitrate(this.mKSYComposeKit.getVideoBitrate());
        this.H.setAudioBitrate(this.mKSYComposeKit.getAudioBitrate());
        this.H.setVideoFps(this.mKSYComposeKit.getVideoFps());
        this.H.setToTranscodeFiles(var2);
        this.H.setAudioChannels(var5.channels);
        this.H.setAudioSampleRate(var5.sampleRate);
        this.H.setVideoCodecId(this.mKSYComposeKit.getVideoCodecId());
        this.H.setVideoProfile(this.mKSYComposeKit.getVideoEncodeProfile());
        this.H.setIFrameInterval(this.mKSYComposeKit.getIFrameInterval());
        this.H.setAudioEncodeProfile(this.mKSYComposeKit.getAudioEncodeProfile());
        this.H.start(var3, this.I, (String)null, false);
    }

    private void j() {
        this.H.release();
        this.H.setOnErrorListener((com.ksyun.media.shortvideo.kit.KSYMergeKit.OnErrorListener)null);
        this.H.setOnInfoListener((com.ksyun.media.shortvideo.kit.KSYMergeKit.OnInfoListener)null);
        this.H = null;
    }

    public String getTmpComposePath(String var1) {
        String var2 = null;
        int var3 = var1.lastIndexOf("/");
        if (var3 >= 0) {
            var2 = var1.substring(0, var3);
        }

        if (var2 != null) {
            File var4 = new File(var2);
            if (!var4.exists()) {
                var4.mkdir();
            }
        }

        StringBuilder var5 = (new StringBuilder(var2)).append("/").append(System.currentTimeMillis());
        var5.append(".mp4");
        return var5.toString();
    }

    public void setAudioEncodeProfile(int var1) {
        this.mKSYComposeKit.setAudioEncodeProfile(var1);
    }

    private class b {
        String a;
        float b;
        float c;
        float d;
        float e;
        float f;

        private b() {
        }

        public b(Object o) {
        }
    }

    private static class a extends Handler {
        private final WeakReference<BeLiveEditKit> a;

        a(BeLiveEditKit var1, Looper var2) {
            super(var2);
            this.a = new WeakReference(var1);
        }

        public void handleMessage(Message var1) {
            BeLiveEditKit var2 = (BeLiveEditKit)this.a.get();
            switch(var1.what) {
                case 2:
                    if (var2.N != null) {
                        var2.N.draw();
                    }
                default:
            }
        }
    }

    public interface OnErrorListener {
        void onError(int var1, long var2);
    }

    public interface OnInfoListener {
        void onInfo(int var1, String... var2);
    }
}
