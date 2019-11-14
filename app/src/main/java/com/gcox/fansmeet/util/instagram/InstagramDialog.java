package com.gcox.fansmeet.util.instagram;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.*;
import android.widget.ProgressBar;
import com.gcox.fansmeet.R;
import timber.log.Timber;

/**
 * Display 37Signals authentication dialog.
 * 
 * @author Thiago Locatelli <thiago.locatelli@gmail.com>
 * @author Lorensius W. L T <lorenz@londatiga.net>
 * 
 */
public class InstagramDialog extends DialogFragment {

	private static final String ARG_URL = "ARG_URL";

	private String mUrl;
	OAuthDialogListener mListener;
	private WebView mWebView;
	private ProgressBar mProgressBar;


	public void setListener(OAuthDialogListener listener) {
		mListener = listener;
	}

	public static InstagramDialog newInstance(String url) {
		Bundle args = new Bundle();
		args.putString(ARG_URL, url);
		InstagramDialog fragment = new InstagramDialog();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getArguments();
		if (bundle != null){
			mUrl = bundle.getString(ARG_URL);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_NoActionBar);
		} else {
			setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_NoActionBar);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		return dialog;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.instagram_login_dialog, container, false);

		getWidgets(root);
		setUpWebView();

		CookieSyncManager.createInstance(getContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();

		return root;
	}

	private void getWidgets(View root){
		mWebView = (WebView) root.findViewById(R.id.webview);
		mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);
	}

	private void setUpWebView() {
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new OAuthWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.loadUrl(mUrl);
	}

	private class OAuthWebViewClient extends WebViewClient {

		OAuthWebViewClient() {
		}

		@TargetApi(Build.VERSION_CODES.N)
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			String url = request.getUrl().toString();
			Timber.d("Redirecting URL " + url);
			if(url!=null) {
				if(url.contentEquals("https://www.instagram.com/")){
					if(mWebView!=null) mWebView.loadUrl(mUrl);
				}else if (url.startsWith(InstagramHelper.mCallbackUrl)) {
					String urls[] = url.split("=");
					if (urls.length > 1 && mListener != null) mListener.onComplete(urls[1]);
					InstagramDialog.this.dismiss();
					return true;
				}
			}

			return false;
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Timber.d("Redirecting URL " + url);

			if(url.contentEquals("https://www.instagram.com/")){
				if(mWebView!=null) mWebView.loadUrl(mUrl);
			}else if (url.startsWith(InstagramHelper.mCallbackUrl)) {
				String urls[] = url.split("=");
				if(urls.length > 1 && mListener!=null)mListener.onComplete(urls[1]);
				InstagramDialog.this.dismiss();
				return true;
			}
			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl) {
			Timber.d("Page error: " + description);

			super.onReceivedError(view, errorCode, description, failingUrl);
			if(mListener!=null) mListener.onError(description);
			InstagramDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Timber.d("Loading URL: " + url);

			super.onPageStarted(view, url, favicon);
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Timber.d("onPageFinished URL: " + url);
			mProgressBar.setVisibility(View.INVISIBLE);
		}

	}

	interface OAuthDialogListener {
		void onComplete(String accessToken);
		void onError(String error);
	}

}