package com.ezhometeam.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ezhometeam.R;
import com.ezhometeam.ui.base.activity.BaseActivity;
import com.ezhometeam.ui.base.activity.Main2Activity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;

import java.net.URL;
import java.util.Arrays;

//import com.google.android.gms.plus.Plus;
//import com.google.android.gms.plus.model.people.Person;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "MainActivity";

    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> loginResult;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private TextView tvLoginFB, tvLoginGG;
    private static final int RC_SIGN_IN = 9001;
    public static String personName, personGivenName, personFamilyName, personEmail, personId, personCover;
    public static Uri personPhoto;
    private Button btnLogin;
    public static GoogleSignInAccount acct;
    public static String name, id, email, link, coverPicUrl;
    public static URL imageURL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        validateServerClientID();
        initFaceBook();
        findViewByIds();
        hideProgressDialog();
        LoginManager.getInstance().registerCallback(callbackManager, loginResult);
//        String SCOPE = "audience:server:client_id:" + SERVER_CLIENT_ID
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
        mGoogleApiClient.connect();

        tvLoginGG.setOnClickListener(this);

        tvLoginFB.setOnClickListener(this);

//        Log.d(TAG, printKeyHash(this));

    }

    @Override
    public int getLayoutMain() {
        return R.layout.activity_main;
    }

    @Override
    public void findViewByIds() {
        tvLoginFB = (TextView) findViewById(R.id.btnLoginFb);
        tvLoginGG = (TextView) findViewById(R.id.btnLoginGG);
        btnLogin = (Button) findViewById(R.id.btn_login);

    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {

    }

    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with "
                    + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }


    }

    //FaceBook Login


    //Login facebook with permisstion
    public void loginFaceBook() {
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "user_friends", "email"));
    }

    //Hàm check login facebook
    public static boolean isLoggedInFaceBook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void initFaceBook() {

        loginResult = new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                //Login thành công xử lý tại đây
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        (object, response) -> {

                            // Application code
                            name = object.optString(getString(R.string.name));
                            id = object.optString(getString(R.string.id));
                            email = object.optString(getString(R.string.email));
                            link = object.optString(getString(R.string.link));
                            imageURL = extractFacebookIcon(id);
                            Log.d(TAG, name + "\n" + imageURL + "\n" + coverPicUrl);
                            try {
                                coverPicUrl = object.getJSONObject("cover").getString("source");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            openMapActivity();
                        });

                Bundle parameters = new Bundle();
                parameters.putString(getString(R.string.fields), getString(R.string.fields_name));
                request.setParameters(parameters);
                request.executeAsync();


                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
    }

    //Lay ava FB
    public URL extractFacebookIcon(String id) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageURL = new URL("http://graph.facebook.com/" + id
                    + "/picture?type=large");
            return imageURL;
        } catch (Throwable e) {
            return null;
        }
    }

    //Google Login

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {

                GoogleSignInAccount acct = result.getSignInAccount();
                People.LoadPeopleResult loadPeopleResult = null;

                personName = acct.getDisplayName();
                personEmail = acct.getEmail();
                personId = acct.getId();
                personPhoto = acct.getPhotoUrl();
                Log.d(TAG, String.valueOf(personPhoto));
                Uri personPhoto = acct.getPhotoUrl();
                personCover = personPhoto.toString();


                Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                if (person.hasCover()) {
                    Person.Cover.CoverPhoto cover = person.getCover().getCoverPhoto();
                    personCover = cover.getUrl();

                    Log.d(TAG, cover.getUrl());
                }

                openMapActivity();

            } else {

                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Common

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed...");
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginGG:
                signIn();
                break;

            case R.id.btnLoginFb:
                loginFaceBook();
                break;
            default:
        }
    }

    private void openMapActivity() {

        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        checkLogin();
        finish();

    }


    public static boolean checkLogin() {
        if (acct != null || isLoggedInFaceBook()) {
            return true;
        } else return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected.......");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended.......");
    }

    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    //    public String printKeyHash(Activity context) {
//        PackageInfo packageInfo;
//        String key = null;
//        try {
//            //getting application package name, as defined in manifest
//            String packageName = context.getApplicationContext().getPackageName();
//
//            //Retriving package info
//            packageInfo = context.getPackageManager().getPackageInfo(packageName,
//                    PackageManager.GET_SIGNATURES);
//
//            Log.e("Package Name=", context.getApplicationContext().getPackageName());
//
//            for (android.content.pm.Signature signature : packageInfo.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                key = new String(Base64.encode(md.digest(), 0));
//
//                // String key = new String(Base64.encodeBytes(md.digest()));
//                Log.e("Key Hash=", key);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("Name not found", e1.toString());
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("No such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("Exception", e.toString());
//        }
//
//        return key;
//    }

}
