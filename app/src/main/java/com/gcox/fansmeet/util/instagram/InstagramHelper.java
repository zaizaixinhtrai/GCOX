package com.gcox.fansmeet.util.instagram;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.gcox.fansmeet.R;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * 
 * @author Thiago Locatelli <thiago.locatelli@gmail.com>
 * @author Lorensius W. L T <lorenz@londatiga.net>
 * 
 */
public class InstagramHelper implements InstagramDialog.OAuthDialogListener{
	private InstagramDialog mDialog;
	private OAuthAuthenticationListener mListener;
	private ProgressDialog mProgress;
	private HashMap<String, String> userInfo = new HashMap<String, String>();
	private String mAuthUrl;
	private String mTokenUrl;
	private String mAccessToken;
	private Context mCtx;

	private String mClientId;
	private String mClientSecret;

	static int WHAT_FINALIZE = 0;
	static int WHAT_ERROR = 1;
	private static int WHAT_FETCH_INFO = 2;

	/**
	 * Callback url, as set in 'Manage OAuth Costumers' page
	 * (https://developer.github.com/)
	 */

	public static String mCallbackUrl = "";
	private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
	private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
	private static final String API_URL = "https://api.instagram.com/v1";

	private static final String TAG = "InstagramAPI";

	public static final String TAG_DATA = "data";
	public static final String TAG_ID = "id";
	public static final String TAG_PROFILE_PICTURE = "profile_picture";
	public static final String TAG_USERNAME = "username";
	public static final String TAG_BIO = "bio";
	public static final String TAG_WEBSITE = "website";
	public static final String TAG_COUNTS = "counts";
	public static final String TAG_FOLLOWS = "follows";
	public static final String TAG_FOLLOWED_BY = "followed_by";
	public static final String TAG_MEDIA = "media";
	public static final String TAG_FULL_NAME = "full_name";
	public static final String TAG_META = "meta";
	public static final String TAG_CODE = "code";
	public static final String LOGGIN_SCOPE = "basic";

	public InstagramHelper(Context context, String clientId, String clientSecret,
						   String callbackUrl) {

		mClientId = clientId;
		mClientSecret = clientSecret;
		mCtx = context;
		mCallbackUrl = callbackUrl;
		mTokenUrl = TOKEN_URL + "?client_id=" + clientId + "&client_secret="
				+ clientSecret + "&redirect_uri=" + mCallbackUrl
				+ "&grant_type=authorization_code";
		mAuthUrl = AUTH_URL
				+ "?client_id="
				+ clientId
				+ "&redirect_uri="
				+ mCallbackUrl
				+ "&response_type=code&display=touch&scope=" + LOGGIN_SCOPE;


	}

	private void getAccessToken(final String code) {
		mProgress.setMessage(mCtx.getString(R.string.message_get_access_token));
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Getting access token");
				int what = WHAT_FETCH_INFO;
				InstagramModel session = null;
				try {
					URL url = new URL(TOKEN_URL);
					// URL url = new URL(mTokenUrl + "&code=" + code);
					Log.i(TAG, "Opening Token URL " + url.toString());
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(true);
					// urlConnection.connect();
					OutputStreamWriter writer = new OutputStreamWriter(
							urlConnection.getOutputStream());
					writer.write("client_id=" + mClientId + "&client_secret="
							+ mClientSecret + "&grant_type=authorization_code"
							+ "&redirect_uri=" + mCallbackUrl + "&code=" + code);
					writer.flush();
					String response = streamToString(urlConnection
							.getInputStream());
					Log.i(TAG, "response " + response);
					JSONObject jsonObj = (JSONObject) new JSONTokener(response)
							.nextValue();

					mAccessToken = jsonObj.getString("access_token");
					Log.i(TAG, "Got access token: " + mAccessToken);

					String id = jsonObj.getJSONObject("user").getString("id");
					String username = jsonObj.getJSONObject("user").getString("username");
					String fullName = jsonObj.getJSONObject("user").getString("full_name");
					String website = jsonObj.getJSONObject("user").getString("website");
					String profilePicture = jsonObj.getJSONObject("user").getString("profile_picture");
					String bio = jsonObj.getJSONObject("user").getString("bio");

					session = new InstagramModel();
					session.setId(id);
					session.setUsername(username);
					session.setFullName(fullName);
					session.setProfilePicture(profilePicture);
					session.setBio(bio);
					session.setWebsite(website);

				} catch (Exception ex) {
					what = WHAT_ERROR;
					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0, session));
			}
		}.start();
	}

	public void fetchUserName(final Handler handler, InstagramModel session) {
		mProgress = new ProgressDialog(mCtx);
		mProgress.setMessage("Loading ...");
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user info");
				int what = WHAT_FINALIZE;
				try {
					URL url = new URL(API_URL + "/users/" + session.getId()
							+ "/?access_token=" + mAccessToken);

					Log.d(TAG, "Opening URL " + url.toString());
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					urlConnection.connect();
					String response = streamToString(urlConnection
							.getInputStream());
					System.out.println(response);
					JSONObject jsonObj = (JSONObject) new JSONTokener(response)
							.nextValue();

					// String name = jsonObj.getJSONObject("data").getString(
					// "full_name");
					// String bio =
					// jsonObj.getJSONObject("data").getString("bio");
					// Log.i(TAG, "Got name: " + name + ", bio [" + bio + "]");
					JSONObject data_obj = jsonObj.getJSONObject(TAG_DATA);
					userInfo.put(TAG_ID, data_obj.getString(TAG_ID));

					userInfo.put(TAG_PROFILE_PICTURE,
							data_obj.getString(TAG_PROFILE_PICTURE));

					userInfo.put(TAG_USERNAME, data_obj.getString(TAG_USERNAME));

					userInfo.put(TAG_BIO, data_obj.getString(TAG_BIO));

					userInfo.put(TAG_WEBSITE, data_obj.getString(TAG_WEBSITE));

					JSONObject counts_obj = data_obj.getJSONObject(TAG_COUNTS);

					userInfo.put(TAG_FOLLOWS, counts_obj.getString(TAG_FOLLOWS));

					userInfo.put(TAG_FOLLOWED_BY,
							counts_obj.getString(TAG_FOLLOWED_BY));

					userInfo.put(TAG_MEDIA, counts_obj.getString(TAG_MEDIA));

					userInfo.put(TAG_FULL_NAME,
							data_obj.getString(TAG_FULL_NAME));

					JSONObject meta_obj = jsonObj.getJSONObject(TAG_META);

					userInfo.put(TAG_CODE, meta_obj.getString(TAG_CODE));
				} catch (Exception ex) {
					what = WHAT_ERROR;
					ex.printStackTrace();
				}
				mProgress.dismiss();
				handler.sendMessage(handler.obtainMessage(what, 2, 0));
			}
		}.start();

	}

	private String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == WHAT_ERROR) {
				mProgress.dismiss();
				if (msg.arg1 == 1) {
					mListener.onInstagramLoginFail("Failed to get access token");
				} else if (msg.arg1 == 2) {
					mListener.onInstagramLoginFail("Failed to get user information");
				}
			} else if (msg.what == WHAT_FETCH_INFO) {
				// fetchUserName();
				InstagramModel model = (InstagramModel) msg.obj;
				mProgress.dismiss();
				mListener.onInstagramLoginSuccessfully(model);
			}
		}
	};

	public HashMap<String, String> getUserInfo() {
		return userInfo;
	}

	public boolean hasAccessToken() {
		return mAccessToken != null;
	}


	public void setListener(OAuthAuthenticationListener listener) {
		mListener = listener;
	}

	@Override
	public void onComplete(String code) {
		getAccessToken(code);
	}

	@Override
	public void onError(String error) {
		mListener.onInstagramLoginFail("Authorization failed");
	}

	public void authorize() {
		// Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
		// webAuthIntent.setData(Uri.parse(AUTH_URL));
		// mCtx.startActivity(webAuthIntent);
		FragmentManager fragmentManager = ((AppCompatActivity)mCtx).getSupportFragmentManager();
		mDialog = InstagramDialog.newInstance(mAuthUrl);
		mDialog.setListener(this);
		mDialog.show(fragmentManager, InstagramDialog.class.getName());
		mProgress = new ProgressDialog(mCtx);
		mProgress.setCancelable(false);
	}

	public interface OAuthAuthenticationListener {
		void onInstagramLoginSuccessfully(InstagramModel instagramSession);
		void onInstagramLoginFail(String error);
	}


}