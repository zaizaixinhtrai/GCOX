package com.gcox.fansmeet.features.editvideo.videorange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.ksyun.media.shortvideo.capture.AVDecoderCapture;
import com.ksyun.media.shortvideo.kit.KSYEditKit;
import com.ksyun.media.shortvideo.utils.AuthInfoManager;
import com.ksyun.media.shortvideo.utils.FileUtils;
import com.ksyun.media.streamer.capture.AudioPlayerCapture;
import com.ksyun.media.streamer.capture.WaterMarkCapture;
import com.ksyun.media.streamer.encoder.*;
import com.ksyun.media.streamer.filter.audio.AudioFilterMgt;
import com.ksyun.media.streamer.filter.audio.AudioMixer;
import com.ksyun.media.streamer.filter.audio.AudioSpeedFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.filter.imgtex.ImgTexMixer;
import com.ksyun.media.streamer.filter.imgtex.ImgTexScaleFilter;
import com.ksyun.media.streamer.framework.AudioCodecFormat;
import com.ksyun.media.streamer.framework.ImgTexFormat;
import com.ksyun.media.streamer.framework.VideoCodecFormat;
import com.ksyun.media.streamer.publisher.FilePublisher;
import com.ksyun.media.streamer.publisher.Publisher;
import com.ksyun.media.streamer.util.gles.GLRender;

/**
 * Created by thanhbc on 3/21/18.
 */

public class BeLiveComposeKit {

    private static String TAG = "BeLiveComposeKit";
    public static final int INFO_STARTED = 1;
    public static final int INFO_COMPLETED = 2;
    public static final int INFO_ABORTED = 3;
    public static final int FILE_TYPE_MP4 = 1;
    public static final int FILE_TYPE_GIF = 2;
    private GLRender e;
    private Context f;
    private float g = 3.0F;
    private int h = 1;
    private int i = 0;
    private int j = 3;
    private int k = 1;
    private int l = 3;
    private int m = 3;
    protected AudioFilterMgt a;
    private AudioPlayerCapture n;
    protected AudioMixer b;
    private Encoder o;
    private Encoder p;
    private FilePublisher q;
    private WaterMarkCapture r;
    private WaterMarkCapture s;
    private WaterMarkCapture t;
    private ImgTexScaleFilter u;
    private ImgTexMixer v;
    private ImgTexFilterMgt w;
    private ImgTexToBuf x;
    protected AudioSpeedFilter c;
    private AVDecoderCapture y;
    private BeLiveComposeKit.b z;
    private BeLiveComposeKit.a A;
    private Handler B;
    private float C = 15.0F;
    private int D = 600000;
    private int E = 24;
    private String F;
    private String G;
    private int H = 0;
    private int I = 0;
    private int J = 0;
    private int K = 0;
    private int L = 0;
    private float M;
    private int N;
    private int O;
    private int P;
    private int Q;
    private int R;
    private int S;
    private int T;
    private int U;
    public int mIdxAudioOrigin;
    public int mIdxAudioBgm;
    private boolean V;
    private String W;
    private long X;
    private long Y;
    private int Z;
    private float aa;
    private AVDecoderCapture.AVDecoderListener ab;
    private Encoder.EncoderListener ac;

    public BeLiveComposeKit(Context var1) {
        this.M = KSYEditKit.DEFAULT_SCALE;
        this.N = 0;
        this.Q = 0;
        this.R = 1;
        this.S = 2;
        this.T = 3;
        this.U = 4;
        this.mIdxAudioOrigin = 0;
        this.mIdxAudioBgm = 1;
        this.X = 0L;
        this.Y = 0L;
        this.Z = 1;
        this.aa = 0.4F;
        this.ab = new AVDecoderCapture.AVDecoderListener() {
            public void onInfo(int var1, long var2) {
                switch(var1) {
                    case 100:
                        AVDecoderCapture.MediaInfo var4 = BeLiveComposeKit.this.y.getMediaInfo();
                        int var5 = var4.width;
                        int var6 = var4.height;
                        int var7 = var4.degree;
                        if(var7 % 180 != 0) {
                            int var8 = var5;
                            var5 = var6;
                            var6 = var8;
                        }

                        BeLiveComposeKit.this.u.setTargetSize(var5, var6);
                        float var9 = (float)var4.frameRate;
                        if(BeLiveComposeKit.this.C > 0.0F) {
                            var9 = BeLiveComposeKit.this.C;
                        }

                        if(var9 <= 0.0F) {
                            var9 = 15.0F;
                        }

                        BeLiveComposeKit.this.O = var5;
                        BeLiveComposeKit.this.P = var6;
                        BeLiveComposeKit.this.b(var5, var6);
                        if(BeLiveComposeKit.this.i == 0) {
                            BeLiveComposeKit.this.i = var4.audioBitrate;
                        }

                        BeLiveComposeKit.this.a(var9);
                        if(var4.isOnlyAudio) {
                            BeLiveComposeKit.this.q.setAudioOnly(true);
                        } else if(var4.isOnlyVideo) {
                            BeLiveComposeKit.this.y.createDummyCapture();
                        }

                        BeLiveComposeKit.this.q.setFramerate(var9);
                        BeLiveComposeKit.this.B.post(new Runnable() {
                            public void run() {
                                if(!TextUtils.isEmpty(BeLiveComposeKit.this.W)) {
                                    BeLiveComposeKit.this.n.setMute(true);
                                    BeLiveComposeKit.this.n.start(BeLiveComposeKit.this.W, true);
                                }

                            }
                        });
                    case 1005:
                    default:
                }
            }

            public void onError(int var1, long var2) {
                BeLiveComposeKit.this.a(-4000, (long)var1);
            }
        };
        this.ac = new Encoder.EncoderListener() {
            public void onError(Encoder var1, int var2) {
                if(var2 != 0) {
                    BeLiveComposeKit.this.f();
                }

                boolean var3 = true;
                if(var1 instanceof MediaCodecAudioEncoder || var1 instanceof AVCodecAudioEncoder) {
                    var3 = false;
                }

                int var4;
                switch(var2) {
                    case -1002:
                        var4 = var3?-1004:-1008;
                        break;
                    case -1001:
                    default:
                        var4 = var3?-1003:-1011;
                }

                if(BeLiveComposeKit.this.A != null) {
                    BeLiveComposeKit.this.A.onError(BeLiveComposeKit.this, var4, 0L);
                }

            }
        };
        if(var1 == null) {
            throw new IllegalArgumentException("Context cannot be null!");
        } else {
            this.B = new Handler(Looper.getMainLooper());
            this.f = var1.getApplicationContext();
            this.a();
        }
    }

    private void a() {
        if(this.e == null) {
            this.e = new GLRender();
        }

        this.y = new AVDecoderCapture(this.e);
        this.y.setAVDecoderListener(this.ab);
        this.y.setNeedSendEos(true);
        this.r = new WaterMarkCapture(this.e);
        this.s = new WaterMarkCapture(this.e);
        this.t = new WaterMarkCapture(this.e);
        this.u = new ImgTexScaleFilter(this.e);
        this.u.setScalingMode(0);
        this.w = new ImgTexFilterMgt(this.f);
        this.v = new ImgTexMixer(this.e);
        this.x = new ImgTexToBuf(this.e);
        this.x.setOutputColorFormat(3);
        this.p = new AVCodecAudioEncoder();
        this.o = new AVCodecVideoEncoder();
        this.p.setEncoderListener(this.ac);
        this.o.setEncoderListener(this.ac);
        this.q = new FilePublisher();
        this.q.setPubListener(new Publisher.PubListener() {
            public void onInfo(int var1, long var2) {
                switch(var1) {
                    case 1:
                        if(BeLiveComposeKit.this.z != null) {
                            BeLiveComposeKit.this.z.onInfo(BeLiveComposeKit.this, 1, (String)null);
                        }
                        break;
                    case 2:
                        BeLiveComposeKit.this.o.start();
                    case 3:
                    default:
                        break;
                    case 4:
                        if(!BeLiveComposeKit.this.V) {
                            BeLiveComposeKit.this.f();
                            if(BeLiveComposeKit.this.z != null) {
                                BeLiveComposeKit.this.z.onInfo(BeLiveComposeKit.this, 2, BeLiveComposeKit.this.G);
                            }
                        } else {
                            BeLiveComposeKit.this.V = false;
                            if(BeLiveComposeKit.this.z != null) {
                                BeLiveComposeKit.this.z.onInfo(BeLiveComposeKit.this, 3, (String)null);
                            }

                            FileUtils.deleteFile(BeLiveComposeKit.this.G);
                        }
                }

            }

            public void onError(int var1, long var2) {
                if(var1 != 0) {
                    BeLiveComposeKit.this.f();
                }

                if(BeLiveComposeKit.this.A != null) {
                    short var4;
                    switch(var1) {
                        case -4004:
                            var4 = -4004;
                            break;
                        case -4003:
                            var4 = -4003;
                            break;
                        case -4002:
                            var4 = -4002;
                            break;
                        case -4001:
                            var4 = -4001;
                            break;
                        default:
                            var4 = -4000;
                    }

                    BeLiveComposeKit.this.A.onError(BeLiveComposeKit.this, var4, (long)((int)var2));
                }

            }
        });
        this.y.getVideoSrcPin().connect(this.u.getSinkPin());
        this.u.getSrcPin().connect(this.w.getSinkPin());
        this.w.getSrcPin().connect(this.v.getSinkPin(this.Q));
        this.r.mLogoTexSrcPin.connect(this.v.getSinkPin(this.R));
        this.r.mTimeTexSrcPin.connect(this.v.getSinkPin(this.S));
        this.s.mLogoTexSrcPin.connect(this.v.getSinkPin(this.T));
        this.t.mLogoTexSrcPin.connect(this.v.getSinkPin(this.U));
        this.n = new AudioPlayerCapture(this.f);
        this.a = new AudioFilterMgt();
        this.b = new AudioMixer();
        this.n.setEnableFastPlay(true);
        this.b.setBlockingMode(true);
        this.y.getAudioSrcPin().connect(this.a.getSinkPin());
        this.a.getSrcPin().connect(this.b.getSinkPin(this.mIdxAudioOrigin));
        this.n.mSrcPin.connect(this.b.getSinkPin(this.mIdxAudioBgm));
        this.b.getSrcPin().connect(this.p.mSinkPin);
        this.v.getSrcPin().connect(this.x.getSinkPin());
        this.x.getSrcPin().connect(this.b().mSinkPin);
        this.b().mSrcPin.connect(this.q.getVideoSink());
        this.p.mSrcPin.connect(this.q.getAudioSink());
        this.e.init(1, 1);
    }

    private void a(final int var1, final String var2) {
        if(this.B != null) {
            this.B.post(new Runnable() {
                public void run() {
                    if(BeLiveComposeKit.this.z != null) {
                        BeLiveComposeKit.this.z.onInfo(BeLiveComposeKit.this, var1, var2);
                    }

                }
            });
        }

    }

    private void a(final int var1, final long var2) {
        if(this.B != null) {
            this.B.post(new Runnable() {
                public void run() {
                    if(BeLiveComposeKit.this.A != null) {
                        BeLiveComposeKit.this.A.onError(BeLiveComposeKit.this, var1, var2);
                    }

                }
            });
        }

    }

    private AVCodecVideoEncoder b() {
        return (AVCodecVideoEncoder)this.o;
    }

    private MediaCodecSurfaceEncoder c() {
        return (MediaCodecSurfaceEncoder)this.o;
    }

    private AVCodecAudioEncoder d() {
        return (AVCodecAudioEncoder)this.p;
    }

    private MediaCodecAudioEncoder e() {
        return (MediaCodecAudioEncoder)this.p;
    }

    public void release() {
        if(this.B != null) {
            this.B.removeCallbacksAndMessages((Object)null);
            this.B = null;
        }

        if(this.n != null) {
            this.n.release();
        }

        this.y.release();
        this.e.release();
    }

    private void a(int var1, int var2) {
        if(var1 > 0 && var2 > 0) {
            this.e.init(var1, var2);
        } else {
            throw new IllegalArgumentException("Invalid offscreen resolution");
        }
    }

    public void setVideoFps(float var1) {
        this.C = var1;
    }

    public float getVideoFps() {
        return this.C;
    }

    public void setVideoCrf(int var1) {
        this.E = var1;
    }

    public void setIFrameInterval(float var1) {
        this.g = var1;
    }

    public float getIFrameInterval() {
        return this.g;
    }

    public void setVideoBitrate(int var1) {
        this.D = var1;
    }

    public int getVideoBitrate() {
        return this.D;
    }

    public void setVideoKBitrate(int var1) {
        this.setVideoBitrate(var1 * 1000);
    }

    public void setAudioBitrate(int var1) {
        if(var1 < 0) {
            throw new IllegalArgumentException("the AudioBitrate must >=0");
        } else {
            this.i = var1;
        }
    }

    public int getAudioBitrate() {
        return this.i;
    }

    public void setAudioKBitrate(int var1) {
        this.setAudioBitrate(var1 * 1000);
    }

    public void setScale(float var1) {
        this.M = var1;
    }

    public float getScale() {
        return this.M;
    }

    public void setScalingMode(int var1) {
        this.N = var1;
        this.v.setScalingMode(0, var1);
    }

    public int getScaleMode() {
        return this.N;
    }

    public void setTargetResolution(int var1) {
        if(var1 >= 0 && var1 <= 4) {
            this.H = var1;
            this.I = 0;
            this.J = 0;
        } else {
            throw new IllegalArgumentException("Invalid resolution index");
        }
    }

    private void b(int var1, int var2) {
        int var3;
        if(this.I == 0 && this.J == 0) {
            var3 = this.a(this.H);
            if(var1 > var2) {
                this.J = var3;
            } else {
                this.I = var3;
            }
        }

        if(var1 != 0 && var2 != 0) {
            if(this.I == 0) {
                this.I = this.J * var1 / var2;
            } else if(this.J == 0) {
                this.J = this.I * var2 / var1;
            }
        }

        this.I = this.c(this.I, 8);
        this.J = this.c(this.J, 8);
        if(this.N != 0) {
            if(this.M == KSYEditKit.DEFAULT_SCALE && this.L != 0 && this.K != 0) {
                this.J = this.I * this.L / this.K;
            } else {
                this.J = (int)((float)this.I / this.M);
            }
        }

        this.I = this.c(this.I, 8);
        this.J = this.c(this.J, 8);
        if(this.N == 0) {
            var3 = var1;
            int var4 = var2;
            int var5 = 0;
            int var6 = 0;
            if(this.I != 0 || this.J != 0) {
                var5 = this.I;
                var6 = this.J;
            }

            if(var5 != 0 || var6 != 0) {
                if(var5 == 0) {
                    var3 = var6 * var1 / var2;
                    var4 = var6;
                } else if(var6 == 0) {
                    var4 = var5 * var2 / var1;
                    var3 = var5;
                } else if(var1 * var6 > var5 * var2) {
                    var4 = var5 * var2 / var1;
                    var3 = var5;
                } else {
                    var3 = var6 * var1 / var2;
                    var4 = var6;
                }

                var3 = this.c(var3, 8);
                var4 = this.c(var4, 8);
            }

            this.I = var3;
            this.J = var4;
        }

    }

    public void setScreenRenderSize(int var1, int var2) {
        this.K = var1;
        this.L = var2;
    }

    private int a(int var1) {
        switch(var1) {
            case 0:
                return 360;
            case 1:
                return 480;
            case 2:
                return 540;
            case 3:
                return 720;
            case 4:
                return 1080;
            default:
                return 720;
        }
    }

    public void setTargetResolution(int var1, int var2) {
        if(var1 >= 0 && var2 >= 0 && (var1 != 0 || var2 != 0)) {
            this.I = var1;
            this.J = var2;
        } else {
            throw new IllegalArgumentException("Invalid resolution");
        }
    }

    public int getTargetWidth() {
        return this.I;
    }

    public int getTargetHeight() {
        return this.J;
    }

    public void setVideoCodecId(int var1) {
        if(var1 != 1 && var1 != 2 && var1 != 3) {
            throw new IllegalArgumentException("input video codecid error");
        } else {
            if(var1 != this.h) {
                if(var1 == 3) {
                    this.x.setOutputColorFormat(6);
                    this.q.setVideoOnly(true);
                    this.y.getAudioSrcPin().disconnect(this.a.getSinkPin(), false);
                    this.Z = 2;
                    if(this.l != 3) {
                        Log.d(TAG, "fallback to software because of gif");
                        this.setVideoEncodeMethod(3);
                    }
                } else {
                    this.x.setOutputColorFormat(3);
                    this.q.setVideoOnly(false);
                    this.Z = 1;
                }
            }

            this.h = var1;
        }
    }

    public int getVideoCodecId() {
        return this.h;
    }

    public void setEncodeMethod(int var1) {
        if(!this.b(var1)) {
            throw new IllegalArgumentException();
        } else {
            if(this.Z == 2 && var1 != 3) {
                Log.d(TAG, "fallback to software because of gif");
                var1 = 3;
            }

            this.setVideoEncodeMethod(var1);
            this.setAudioEncodeMethod(var1);
        }
    }

    public void setAudioEncodeMethod(int var1) {
        if(!this.b(var1)) {
            throw new IllegalArgumentException();
        } else if(this.m != var1) {
            if(this.p.isEncoding()) {
                throw new IllegalStateException("Cannot set encode method while recording");
            } else {
                if(this.m == 3) {
                    this.b.getSrcPin().disconnect(this.d().mSinkPin, false);
                    this.d().mSrcPin.disconnect(this.q.getAudioSink(), false);
                    this.d().release();
                    this.p = new MediaCodecAudioEncoder();
                    this.b.getSrcPin().connect(this.e().mSinkPin);
                    this.e().mSrcPin.connect(this.q.getAudioSink());
                } else if(this.m == 2) {
                    this.b.getSrcPin().disconnect(this.e().mSinkPin, false);
                    this.e().mSrcPin.disconnect(this.q.getAudioSink(), false);
                    this.p = new AVCodecAudioEncoder();
                    this.b.getSrcPin().connect(this.d().mSinkPin);
                    this.d().mSrcPin.connect(this.q.getAudioSink());
                }

                this.p.setEncoderListener(this.ac);
                this.m = var1;
            }
        }
    }

    public void setVideoEncodeMethod(int var1) {
        if(!this.b(var1)) {
            throw new IllegalArgumentException("Invalid encode method");
        } else if(this.l != var1) {
            if(this.o.isEncoding()) {
                throw new IllegalStateException("Cannot set encode method while composing!");
            } else {
                if(this.l == 3) {
                    this.v.getSrcPin().disconnect(this.x.getSinkPin(), false);
                    this.b().mSrcPin.disconnect(this.q.getVideoSink(), false);
                    this.x.getSrcPin().disconnect(this.b().mSinkPin, true);
                    this.o = new MediaCodecSurfaceEncoder(this.e);
                    this.v.getSrcPin().connect(this.c().mSinkPin);
                    this.c().mSrcPin.connect(this.q.getVideoSink());
                } else if(this.l == 2) {
                    this.v.getSrcPin().disconnect(this.c().mSinkPin, false);
                    this.c().mSrcPin.disconnect(this.q.getVideoSink(), false);
                    this.o.release();
                    this.o = new AVCodecVideoEncoder();
                    this.v.getSrcPin().connect(this.x.getSinkPin());
                    this.x.getSrcPin().connect(this.b().mSinkPin);
                    this.b().mSrcPin.connect(this.q.getVideoSink());
                }

                this.o.setEncoderListener(this.ac);
                this.l = var1;
            }
        }
    }

    public int getVideoEncodeMethod() {
        return this.l;
    }

    public void setDecodeMethod(int var1) {
        if(var1 != 1 && var1 != 2) {
            throw new IllegalArgumentException("Invalid decode method");
        } else {
            this.y.setVideoDecodeMethod(var1);
            this.y.setAudioDecodeMethod(var1);
        }
    }

    public void setVideoDecodeMethod(int var1) {
        this.y.setVideoDecodeMethod(var1);
    }

    public void setAudioDecodeMethod(int var1) {
        this.y.setAudioDecodeMethod(var1);
    }

    public void setVideoEncodeProfile(int var1) {
        this.j = var1;
    }

    public int getVideoEncodeProfile() {
        return this.j;
    }

    public void setForceVideoFrameFirst(boolean var1) {
        this.q.setForceVideoFrameFirst(var1);
    }

    private int c(int var1, int var2) {
        return (var1 + var2 - 1) / var2 * var2;
    }

    private boolean b(int var1) {
        return var1 == 3 || var1 == 2;
    }

    public void setSrcUrl(String var1) {
        this.F = var1;
    }

    public String getSrcUri() {
        return this.F;
    }

    public void setDesUrl(String var1) {
        this.G = var1;
    }

    public String getDesUrl() {
        return this.G;
    }

    public void setOnInfoListener(BeLiveComposeKit.b var1) {
        this.z = var1;
    }

    public void setOnErrorListener(BeLiveComposeKit.a var1) {
        this.A = var1;
    }

    private void a(float var1) {
        this.C = var1;
        this.r.setPreviewSize(this.O, this.P);
        this.r.setTargetSize(this.I, this.J);
        this.s.setPreviewSize(this.O, this.P);
        this.s.setTargetSize(this.I, this.J);
        this.t.setPreviewSize(this.O, this.P);
        this.t.setTargetSize(this.I, this.J);
        this.v.setTargetSize(this.I, this.J);
        if(this.D == 0 && this.l == 2) {
            this.setVideoBitrate(600000);
        }

        VideoCodecFormat var2 = new VideoCodecFormat(this.h, this.I, this.J, this.D);
        var2.crf = this.E;
        var2.frameRate = var1;
        var2.iFrameInterval = this.g;
        var2.liveStreaming = false;
        var2.scene = 0;
        var2.profile = this.j;
        this.v.onFormatChanged(this.Q, new ImgTexFormat(1, this.O, this.P));
        this.o.setAutoWork(true);
        this.o.configure(var2);
        this.o.setUseSyncMode(true);
        this.a(this.I, this.J);
        AVDecoderCapture.MediaInfo var3 = this.y.getMediaInfo();
        int var4 = var3.audioChannels;
        int var5 = var3.audioSampleRate;
        int var6 = var3.audioSampleFormat;
        AudioCodecFormat var7 = new AudioCodecFormat(256, var6, var5, var4, this.i);
        var7.profile = this.k;
        this.p.setAutoWork(true);
        this.p.configure(var7);
        this.p.setUseSyncMode(true);
        this.q.setFramerate(this.C);
        this.q.setBlockingMode(true);
        this.q.setAutoWork(true);
    }

    public void addMetaOption(String var1, String var2) {
        this.q.addMetaOption(var1, var2);
    }

    public float getProgress() {
        float var1 = this.y.getProgress();
        if(var1 > 100.0F) {
            var1 = 100.0F;
        }

        return var1;
    }

    public void setSpeed(float var1) {
        if(!this.q.isVideoOnly()) {
            this.b(var1);
            this.c.start();
            this.c.setSpeed(var1);
        }

        this.y.setSpeed(var1);
    }

    public void setVoiceVolume(float var1) {
        this.aa = var1;
        this.b.setInputVolume(this.mIdxAudioOrigin, var1);
    }

    public float getVoiceVolume() {
        return this.b.getInputVolume(this.mIdxAudioOrigin);
    }

    public void setVolume(float var1) {
        this.b.setInputVolume(this.mIdxAudioOrigin, var1);
    }

    public void start() {
//        Log.d(TAG, "start compose");
//        if(AuthInfoManager.getInstance().checkAuthFeature(AuthInfoManager.FEA_BASE)) {
            this.V = false;
            this.q.setUrl(this.G);
            this.y.setDataSource(this.F);
//        } else {
//            Log.d(TAG, "auth failed");
//        }

    }

    public void stopCompose() {
        Log.d(TAG, "stop compose");
        this.V = true;
        this.y.stop();
        if(this.n != null) {
            this.n.stop();
        }

        if(this.p != null) {
            this.p.stop();
        }

        this.o.stop();
        this.o.setAutoWork(false);
        if(this.c != null) {
            this.c.stop();
        }

    }

    private void f() {
        Log.d(TAG, "abort compose");
        this.y.stop();
        if(this.n != null) {
            this.n.stop();
        }

        if(this.p != null) {
            this.p.stop();
        }

        this.o.stop();
        this.o.setAutoWork(false);
        if(this.c != null) {
            this.c.stop();
        }

    }

    public ImgTexMixer getImgTexMixer() {
        return this.v;
    }

    public ImgTexFilterMgt getImgTexFilterMgt() {
        return this.w;
    }

    public AudioFilterMgt getAudioFilterMgt() {
        return this.a;
    }

    public GLRender getGLRender() {
        return this.e;
    }

    public int getPlayRanges() {
        return this.y.getDuration();
    }

    public void showWaterMarkLogo(String var1, float var2, float var3, float var4, float var5, float var6) {
        var6 = Math.max(0.0F, var6);
        var6 = Math.min(var6, 1.0F);
        this.v.setRenderRect(this.R, var2, var3, var4, var5, var6);
        this.r.showLogo(this.f, var1, var4, var5);
    }

    public void showWaterMarkLogo(Bitmap var1, float var2, float var3, float var4, float var5, float var6) {
        var6 = Math.max(0.0F, var6);
        var6 = Math.min(var6, 1.0F);
        this.v.setRenderRect(this.R, var2, var3, var4, var5, var6);
        this.r.showLogo(var1, var4, var5);
    }

    public void showSticker(Bitmap var1, float var2, float var3, float var4, float var5, float var6) {
        var6 = Math.max(0.0F, var6);
        var6 = Math.min(var6, 1.0F);
        this.v.setRenderRect(this.T, var2, var3, var4, var5, var6);
        this.s.showLogo(var1, var4, var5);
    }

    public void addPaint(View var1) {
        if(var1 != null && var1.getWidth() != 0 && var1.getHeight() != 0) {
            int var4 = var1.getWidth();
            int var5 = var1.getHeight();
            if(this.I > 0 || this.J > 0) {
                if(this.I == 0) {
                    this.I = this.J * var4 / var5;
                }

                if(this.J == 0) {
                    this.J = this.I * var5 / var4;
                }

                var4 = this.I;
                var5 = this.J;
            }

            float var6 = (float)var4 / (float)var1.getWidth();
            float var7 = (float)var5 / (float)var1.getHeight();
            Bitmap var2 = Bitmap.createBitmap(var4, var5, Bitmap.Config.ARGB_8888);
            Canvas var3 = new Canvas(var2);
            var3.scale(var6, var7);
            var2.eraseColor(0);
            var1.draw(var3);
            this.a(var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private void a(Bitmap var1, float var2, float var3, float var4, float var5, float var6) {
        var6 = Math.max(0.0F, var6);
        var6 = Math.min(var6, 1.0F);
        this.v.setRenderRect(this.U, var2, var3, var4, var5, var6);
        this.t.showLogo(var1, var4, var5);
    }

    public void hideWaterMarkLogo() {
        this.r.hideLogo();
    }

    public void hideSticker() {
        this.s.hideLogo();
    }

    public void enableOriginAudio(boolean var1) {
        if(!var1) {
            this.b.setInputVolume(this.mIdxAudioOrigin, 0.0F);
        } else {
            this.b.setInputVolume(this.mIdxAudioOrigin, this.aa);
        }

    }

    public void setComposeRanges(long var1, long var3) {
        this.X = var1;
        this.Y = var3;
        this.y.setAvDemuxerCaptureRanges(var1, var3);
    }

    public void setBGMRanges(long var1, long var3) {
        if(this.n != null) {
            this.n.setPlayableRanges(var1, var3);
        }

    }

    public AudioPlayerCapture.PlayRanges getBGMRanges() {
        return this.n.getPlayableRanges();
    }

    public long getBGMDuration() {
        return this.n.getFileDuration();
    }

    public void setBgmMusicPath(String var1) {
        if(!AuthInfoManager.getInstance().checkAuthFeature(AuthInfoManager.FEA_EDIT_BGM)) {
            Log.w(TAG, "start bgm failed," + AuthInfoManager.FEA_FAILED_LOG);
        } else {
            this.W = var1;
        }
    }

    public void setAudioVolume(int var1, float var2) {
        this.b.setInputVolume(var1, var2);
    }

    public void setAudioVolume(int var1, float var2, float var3) {
        this.b.setInputVolume(var1, var2, var3);
    }

    public boolean isAudioMuted() {
        return this.b.getMute();
    }

    public void setEnableMp4FastStart(boolean var1) {
        this.q.setEnableMp4FastStart(var1);
    }

    public Object getAudioEncodeFormat() {
        return this.p.getEncodeFormat();
    }

    public AVDecoderCapture getAVDecoderCapture() {
        return this.y;
    }

    private void b(float var1) {
        if(this.c == null) {
            this.c = new AudioSpeedFilter();
        }

        if(var1 != 1.0F) {
            this.y.getAudioSrcPin().disconnect(this.a.getSinkPin(), false);
            this.y.getAudioSrcPin().connect(this.c.getSinkPin());
            this.c.getSrcPin().connect(this.a.getSinkPin());
        } else {
            this.y.getAudioSrcPin().disconnect(this.c.getSinkPin(), false);
            this.y.getAudioSrcPin().connect(this.a.getSinkPin());
        }

    }

    public void setAudioEncodeProfile(int var1) {
        this.k = var1;
    }

    public int getAudioEncodeProfile() {
        return this.k;
    }

    public interface a {
        void onError(BeLiveComposeKit var1, int var2, long var3);
    }

    public interface b {
        void onInfo(BeLiveComposeKit var1, int var2, String var3);
    }
}
