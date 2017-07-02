package com.example.minhd.ezhome.interact;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minhd.ezhome.R;
import com.example.minhd.ezhome.common.InfomationRegister;
import com.example.minhd.ezhome.ui.dialog.HomeInfomationDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n on 30/06/2017.
 */

public class FirebaseSever {
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    private List<InfomationRegister> listInfo;
    private List<String> address;
    private AdapterHome adapter;
    private Context mContext;

    public FirebaseSever(Context context, InfomationRegister info) {
        register(context, info);
    }

    public FirebaseSever() {
    }

    public FirebaseSever(Context context) {
        mContext = context;
        listInfo = new ArrayList<>();
        address = new ArrayList<>();
    }

    private void register(Context context, InfomationRegister info){
        myRef.child("Master").push().setValue(info, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null){
                    Toast.makeText(context , "Succesfully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getPhongTro(RecyclerView rcView){

        myRef.child("Master").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                InfomationRegister infomation = dataSnapshot.getValue(InfomationRegister.class);
                address.add(infomation.getAddress());
                listInfo.add(infomation);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new AdapterHome();
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rcView.setLayoutManager(manager);
        rcView.setAdapter(adapter);
    }

    public class AdapterHome extends RecyclerView.Adapter<AdapterHome.HolderHome> {

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
            private TextView tvContent;
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

                HomeInfomationDialog dialog = new HomeInfomationDialog(mContext, conent );
                DisplayMetrics display = new DisplayMetrics();
                ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(display);

                int width = display.widthPixels;
                int height = display.heightPixels;

                dialog.getWindow().setLayout(4*width/5, ActionBar.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        }
    }
}
