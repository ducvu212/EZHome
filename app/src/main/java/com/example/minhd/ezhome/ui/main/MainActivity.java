package com.example.minhd.ezhome.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minhd.ezhome.R;
import com.example.minhd.ezhome.ui.base.activity.BaseActivity;
import com.example.minhd.ezhome.ui.base.activity.MapsActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

public class MainActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static MainActivity mainActivity;
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> loginResult;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private TextView tvLogin, tvLoginGG;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private String personName, personGivenName, personFamilyName, personEmail, personId;
    private Uri personPhoto;
    private FrameLayout frameLayout;
    private Button btnLogin;
    private static GoogleSignInAccount acct;
    public static boolean check;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mainActivity = this;
        initFaceBook();
        findViewByIds();
        LoginManager.getInstance().registerCallback(callbackManager, loginResult);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        tvLoginGG.setOnClickListener(this);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFaceBook();
            }
        });
    }

    @Override
    public int getLayoutMain() {
        return R.layout.activity_main;
    }

    @Override
    public void findViewByIds() {
        tvLogin = (TextView) findViewById(R.id.btnLoginFb);
        tvLoginGG = (TextView) findViewById(R.id.btnLoginGG);
        frameLayout = (FrameLayout) findViewById(R.id.MainAct);
        btnLogin = (Button) findViewById(R.id.btn_login);

    }

    @Override
    public void initComponents() {

    }

    @Override
    public void setEvents() {

    }


    //FaceBook Login

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (acct != null) {
            openMapActivity();
        }
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                personName = acct.getDisplayName();
                personGivenName = acct.getGivenName();
                personFamilyName = acct.getFamilyName();
                personEmail = acct.getEmail();
                personId = acct.getId();
                personPhoto = acct.getPhotoUrl();

                Toast.makeText(mainActivity, personName + "\n" +
                                personGivenName + "\n" +
                                personFamilyName + "\n" +
                                personEmail + "\n" +
                                personId + "\n"
                        , Toast.LENGTH_SHORT).show();
                openMapActivity();

            } else {

                Toast.makeText(mainActivity, "Failed!", Toast.LENGTH_SHORT).show();

            }
        }
    }

    //Login facebook with permisstion
    public void loginFaceBook() {
        LoginManager.getInstance().logInWithReadPermissions(mainActivity, Arrays.asList("public_profile", "user_friends", "email"));
    }

    //Hàm check login facebook
    public static boolean isLoggedInFaceBook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    //Lấy Avatar
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

    public void initFaceBook() {
        if (isLoggedInFaceBook()) {
            openMapActivity();
        } else {
            loginResult = new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {

                    //Login thành công xử lý tại đây
                    GraphRequest request = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object,
                                                        GraphResponse response) {
                                    // Application code
                                    String name = object.optString(getString(R.string.name));
                                    String id = object.optString(getString(R.string.id));
                                    String email = object.optString(getString(R.string.email));
                                    String link = object.optString(getString(R.string.link));
                                    URL imageURL = extractFacebookIcon(id);
                                    Log.d("name: ", name);
                                    Log.d("id: ", id);
                                    Log.d("email: ", email);
                                    Log.d("link: ", link);
                                    Log.d("imageURL: ", imageURL.toString());

                                }
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
    }

    //Google Login
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            acct = result.getSignInAccount();

        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    //Common

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
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
        }
    }

    private void openMapActivity() {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackRoot() {
        super.onBackRoot();
    }

    @Override
    public void onBackPressed() {
        checkLogin();
        finish();
        return;

    }

    public static void checkLogin() {
        if (acct != null || isLoggedInFaceBook()) {
            check = true;
        }
        else check = false;
    }

}
