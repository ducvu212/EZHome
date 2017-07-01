package com.example.minhd.ezhome.ui.base.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.minhd.ezhome.R;
import com.example.minhd.ezhome.ui.base.activity.Main2Activity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import static com.example.minhd.ezhome.ui.main.MainActivity.coverPicUrl;
import static com.example.minhd.ezhome.ui.main.MainActivity.email;
import static com.example.minhd.ezhome.ui.main.MainActivity.imageURL;
import static com.example.minhd.ezhome.ui.main.MainActivity.name;
import static com.example.minhd.ezhome.ui.main.MainActivity.personCover;
import static com.example.minhd.ezhome.ui.main.MainActivity.personEmail;
import static com.example.minhd.ezhome.ui.main.MainActivity.personName;
import static com.example.minhd.ezhome.ui.main.MainActivity.personPhoto;

/**
 * Created by minhd on 17/06/27.
 */

public class InfoFragment extends BaseFragment {

    private LinearLayout LinearCover;
    private ImageView imgAva;
    private TextView tvName, tvEmail ;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds();
        initComponents();
    }

    @Override
    public int getLayoutMain() {
        return R.layout.info_fragment;
    }

    @Override
    public void findViewByIds() {
        LinearCover = (LinearLayout) getView().findViewById(R.id.Infocover);
        imgAva = (ImageView) getView().findViewById(R.id.InforAva);
        tvName = (TextView) getView().findViewById(R.id.InfoName);
        tvEmail = (TextView) getView().findViewById(R.id.InfoEmail);

    }

    @Override
    public void initComponents() {
        if (personPhoto != null) {
            tvName.setText(personName);
            tvEmail.setText(personEmail);
            Picasso.with(getContext()).load(personPhoto).into(imgAva);
            new Main2Activity.setCover(LinearCover).execute(personCover);
            Log.d("TAGG", personEmail + "\n" + personCover +"\n" + personPhoto );

        }
        if (imageURL != null) {
            tvName.setText(name);
            tvEmail.setText(email);
            Log.d("TAGG", name + "\n" + imageURL + "\n" + coverPicUrl) ;

            new InfoCover(LinearCover).execute(coverPicUrl);
            new Main2Activity.DownloadImageTask(imgAva).execute(imageURL.toString());

        }
    }

    public class InfoCover extends AsyncTask<String , Void, BitmapDrawable> {

        LinearLayout layout;
        URL coverUrl;

        public InfoCover(LinearLayout linearLayout) {
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
                URL url = new URL(coverPicUrl);
                BitmapDrawable drawable = new BitmapDrawable(url.openConnection().getInputStream());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.setBackground(drawable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setEvents() {

    }

    @Override
    public void onBackPressed() {

    }
}
