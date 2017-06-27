package com.example.minhd.ezhome.ui.base.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import com.example.minhd.ezhome.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import static com.example.minhd.ezhome.ui.main.MainActivity.coverPicUrl;
import static com.example.minhd.ezhome.ui.main.MainActivity.email;
import static com.example.minhd.ezhome.ui.main.MainActivity.id;
import static com.example.minhd.ezhome.ui.main.MainActivity.imageURL;
import static com.example.minhd.ezhome.ui.main.MainActivity.name;
import static com.example.minhd.ezhome.ui.main.MainActivity.personCover;
import static com.example.minhd.ezhome.ui.main.MainActivity.personEmail;
import static com.example.minhd.ezhome.ui.main.MainActivity.personName;
import static com.example.minhd.ezhome.ui.main.MainActivity.personPhoto;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvName, tvLink;
    private ImageView imgAva;
    private NavigationView navigationView;
    private LinearLayout navBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main2);
        } catch (RuntimeException e) {
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        try {
            drawer.addDrawerListener(toggle);
            toggle.syncState();

        } catch (NullPointerException e) {
        }

        inflateHeader();
    }

    private void inflateHeader() {

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tvName = (TextView) headerView.findViewById(R.id.tv_name);
        tvLink = (TextView) headerView.findViewById(R.id.tv_link);
        imgAva = (ImageView) headerView.findViewById(R.id.img_ava);
        navBackground = (LinearLayout) headerView.findViewById(R.id.nav_background);

        if (personPhoto != null) {
            tvName.setText(personName);
            tvLink.setText(personEmail);
            Picasso.with(this).load(personPhoto).into(imgAva);
            new setCover(navBackground).execute(personCover);

        }
        if (imageURL != null) {
            tvName.setText(name);
            tvLink.setText(email);

            try {
                setCoverNav(coverPicUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new DownloadImageTask(imgAva).execute(imageURL.toString());

        }
    }

    private void setCoverNav(String CoverUrl) throws IOException {

        URL url = new URL(CoverUrl);
        BitmapDrawable drawable = new BitmapDrawable(url.openConnection().getInputStream());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            navBackground.setBackground(drawable);
        }

    }

    public class setCover extends AsyncTask<String, Void, BitmapDrawable> {

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
                URL url = new URL(personCover);
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
