package com.ezhometeam.ui.base.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezhometeam.R;
import com.ezhometeam.ui.base.animation.ScreenAnimation;
import com.ezhometeam.ui.base.fragment.BaseFragment;
import com.ezhometeam.ui.base.fragment.InfoFragment;
import com.ezhometeam.ui.base.fragment.RegisterFragment;
import com.ezhometeam.ui.main.MainActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import static com.ezhometeam.ui.base.fragment.MapFragment.googleMap;
import static com.ezhometeam.ui.main.MainActivity.coverPicUrl;
import static com.ezhometeam.ui.main.MainActivity.email;
import static com.ezhometeam.ui.main.MainActivity.id;
import static com.ezhometeam.ui.main.MainActivity.imageURL;
import static com.ezhometeam.ui.main.MainActivity.name;
import static com.ezhometeam.ui.main.MainActivity.personCover;
import static com.ezhometeam.ui.main.MainActivity.personEmail;
import static com.ezhometeam.ui.main.MainActivity.personName;
import static com.ezhometeam.ui.main.MainActivity.personPhoto;

//import static com.ezhometeam.ui.main.GalleryActivity.mGoogleApiClient;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private TextView tvName, tvLink;
    private ImageView imgAva;
    private NavigationView navigationView;
    private static LinearLayout navBackground;
    private RegisterFragment registerFragment;
    private InfoFragment infoFragment;
    private ImageView locationButton;
    private TextView tvSearch;
    private ImageView imgSearch;
    private GoogleApiClient mGoogleApiClient;
    private boolean isLogout;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        userId = intent.getStringExtra("EMAIL");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationButton = (ImageView) findViewById(R.id.location);
        locationButton.setOnClickListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        try {
            drawer.addDrawerListener(toggle);
            toggle.syncState();

        } catch (NullPointerException e) {
        }

        inflateHeader();

        FacebookSdk.sdkInitialize(getApplicationContext());

        imgSearch = (ImageView) findViewById(R.id.img_search);
        tvSearch = (TextView) findViewById(R.id.tv_search);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


    private void inflateHeader() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        try {
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);
            tvName = (TextView) headerView.findViewById(R.id.tv_name);
            tvLink = (TextView) headerView.findViewById(R.id.tv_link);
            imgAva = (ImageView) headerView.findViewById(R.id.img_ava);
            navBackground = (LinearLayout) headerView.findViewById(R.id.nav_background);

            if (personName != null) {
                tvName.setText(personName + "");
                tvLink.setText(personEmail + "");
                if (personPhoto != null)
                    Picasso.with(this).load(personPhoto).into(imgAva);
                new setCover(navBackground).execute(personCover);
            }
            if (name != null && imageURL != null) {
                tvName.setText(name);
                tvLink.setText(email);

                try {
                    setCoverNav(coverPicUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new DownloadImageTask(imgAva).execute(imageURL.toString());

            }
        } catch (NullPointerException e) {
        }
    }

    public static void setCoverNav(String CoverUrl) throws IOException {

        URL url = new URL(CoverUrl);
        BitmapDrawable drawable = new BitmapDrawable(url.openConnection().getInputStream());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            navBackground.setBackground(drawable);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if ( isLogout ) {
            isLogout = false;
            Toast.makeText(this, "can not logout", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (isLogout) {
            isLogout = false;
            logout();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public static class setCover extends AsyncTask<String, Void, BitmapDrawable> {

        LinearLayout layout;
        URL coverUrl;

        public setCover(LinearLayout linearLayout) {
            this.layout = linearLayout;
        }

        @Override
        protected BitmapDrawable doInBackground(String... strings) {
            BitmapDrawable drawable = null;
            try {
                if (coverUrl != null)
                    drawable = new BitmapDrawable(coverUrl.openConnection().getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable bitmapDrawable) {

            try {
                URL url = new URL(personCover.toString());
                BitmapDrawable drawable = new BitmapDrawable(url.openConnection().getInputStream());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.setBackground(drawable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL coverFBURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(coverFBURL.openConnection().getInputStream());

        return bitmap;
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        URL imageURL;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... userID) {
            Bitmap bitmap = null;

            try {
                if (imageURL != null)
                    bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            try {
                Bitmap mBitmap = getFacebookProfilePicture(id);
                bmImage.setImageBitmap(mBitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            imgSearch.setVisibility(View.VISIBLE);
            tvSearch.setVisibility(View.VISIBLE);
            if ((infoFragment != null && infoFragment.isVisible())
                    || (registerFragment != null && registerFragment.isVisible())) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                if (infoFragment != null) {
                    transaction.hide(infoFragment);
                }
                if (registerFragment != null) {
                    transaction.hide(registerFragment);
                }
                transaction.commit();
            }

        } else if (id == R.id.user) {
            infoFragment = new InfoFragment();
            startFragments(infoFragment);

        } else if (id == R.id.reg) {
            registerFragment = new RegisterFragment(userId);
            startFragments(registerFragment);

        } else if (id == R.id.logout) {
            disconnectFromFacebook();
            signOutGG();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void startFragments(BaseFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ScreenAnimation screenAnimation = ScreenAnimation.OPEN_FULL;
        transaction.add(R.id.map, fragment);
        transaction.addToBackStack("Ahihi");
        imgSearch.setVisibility(View.INVISIBLE);
        tvSearch.setVisibility(View.INVISIBLE);
        transaction.setCustomAnimations(
                screenAnimation.getEnterToRight(), screenAnimation.getExitToRight(),
                screenAnimation.getEnterToLeft(), screenAnimation.getExitToLeft());
        transaction.commit();

    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();
    }

    private void signOutGG() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            isLogout = true;
            logout();
        } else {
            mGoogleApiClient.connect();
        }
    }

    private void logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(Main2Activity.this, "Success", Toast.LENGTH_SHORT).show();

//                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        googleMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(googleMap.getMyLocation().getLatitude(),
                googleMap.getMyLocation().getLongitude());
        //co chuc nang di chuyen den vi tri position
        CameraPosition cameraPosition =
                new CameraPosition(latLng, 13, 0, 0);
        //dua camera position vao google map
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }
}
