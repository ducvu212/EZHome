package com.ezhometeam.ui.base.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezhometeam.R;
import com.ezhometeam.common.InfomationRegister;
import com.ezhometeam.interact.FirebaseSever;
import com.ezhometeam.ui.base.activity.Main2Activity;
import com.ezhometeam.ui.dialog.HomeInfomationDialog;
import com.ezhometeam.ui.dialog.InforHomeUserDialog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ezhometeam.ui.main.MainActivity.coverPicUrl;
import static com.ezhometeam.ui.main.MainActivity.email;
import static com.ezhometeam.ui.main.MainActivity.imageURL;
import static com.ezhometeam.ui.main.MainActivity.link;
import static com.ezhometeam.ui.main.MainActivity.name;
import static com.ezhometeam.ui.main.MainActivity.personCover;
import static com.ezhometeam.ui.main.MainActivity.personEmail;
import static com.ezhometeam.ui.main.MainActivity.personName;
import static com.ezhometeam.ui.main.MainActivity.personPhoto;

/**
 * Created by minhd on 17/06/27.
 */


public class InfoFragment extends BaseFragment {

    private LinearLayout LinearCover;
    private ImageView imgAva;
    private TextView tvName, tvEmail ;
    private RecyclerView rcContactUser;
    private List<InfomationRegister> listInfo;
    private AdapterOfUser adapter;
    private String userId;

    public InfoFragment(String userId) {
       this.userId = userId;

    }

    public InfoFragment() {
    }

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
        adapter = new AdapterOfUser();
        listInfo = new ArrayList<>();
        FirebaseSever sv = new FirebaseSever(getContext());
        listInfo = sv.getHomeUser(userId, adapter);
        rcContactUser = (RecyclerView) getView().findViewById(R.id.rc_contact_user);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rcContactUser.setLayoutManager(manager);

        rcContactUser.setAdapter(adapter);

    }

    @Override
    public void initComponents() {
        if (personName != null) {
            tvName.setText(personName);
            tvEmail.setText(personEmail);
            Picasso.with(getContext()).load(personPhoto).into(imgAva);
            new Main2Activity.setCover(LinearCover).execute(personCover);
            Log.d("TAGG", personEmail + "\n" + personCover +"\n" + personPhoto );

        }
        if (name != null) {
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

    public class AdapterOfUser extends RecyclerView.Adapter<AdapterOfUser.HolderHome> {

        @Override
        public HolderHome onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent, false);
            return new HolderHome(view);
        }

        @Override
        public void onBindViewHolder(HolderHome holder, int position) {
            holder.tvContent.setText(listInfo.get(position).getAddress()+ "\n"
                    + listInfo.get(position).getArea() + "\n"
                    + listInfo.get(position).getPrice());
        }



        @Override
        public int getItemCount() {
            return listInfo.size();
        }


        class HolderHome extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView tvContent;
            public HolderHome(View itemView) {
                super(itemView);
                tvContent = (TextView) itemView.findViewById(R.id.tv_info_home);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                int position = getLayoutPosition();
                String conent = "Địa chỉ : " + listInfo.get(position).getAddress()  + "\n"
                        + "Diện tích : " + listInfo.get(position).getArea() + "\n"
                        + "Giá : " + listInfo.get(position).getPrice() + "\n"
                        + "Thông tin : " + listInfo.get(position).getInfomation() + "\n"
                        + "Điện thoại: " + listInfo.get(position).getPhone() + "\n";

                String user = listInfo.get(position).getUser();
                String address = listInfo.get(position).getAddress();

                InforHomeUserDialog dialog = new InforHomeUserDialog(getContext(), conent, new InforHomeUserDialog.ITrash() {
                    @Override
                    public void trash() {
                        FirebaseSever sever = new FirebaseSever(getContext());
                        sever.removeData(user, address, adapter);
                        listInfo.remove(position);
                        notifyDataSetChanged();
                    }
                });
                DisplayMetrics display = new DisplayMetrics();
                ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(display);

                int width = display.widthPixels;
                int height = display.heightPixels;

                dialog.getWindow().setLayout(4*width/5, ActionBar.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        }
    }

}
