package com.ezhometeam.interact;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ezhometeam.R;
import com.ezhometeam.common.InfomationRegister;
import com.ezhometeam.ui.base.fragment.InfoFragment;
import com.ezhometeam.ui.dialog.HomeInfomationDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    private List<InfomationRegister> listInfoHome;
    private List<InfomationRegister> listInfoOfUser;
    private List<String> address;
    private AdapterHome adapter;
    private Context mContext;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public FirebaseSever() {
    }

    public FirebaseSever(Context context) {
        mContext = context;
        listInfoHome = new ArrayList<>();
        listInfoOfUser = new ArrayList<>();
        address = new ArrayList<>();
    }


    public void register(InfomationRegister info){
        registerHome(info);
    }

    private void registerHome(InfomationRegister info) {
        myRef.child("Master").push().setValue(info, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getPhongTro(RecyclerView rcView, String place){

        myRef.child("Master").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                InfomationRegister infomation = dataSnapshot.getValue(InfomationRegister.class);
                address.add(infomation.getAddress());
                if (infomation.getAddress().contains(place)){
                    listInfoHome.add(infomation);
                }
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
            holder.tvContent.setText(listInfoHome.get(position).getAddress()+ "\n"
                    + listInfoHome.get(position).getArea() + "\n"
                    + listInfoHome.get(position).getPrice());
        }


        @Override
        public int getItemCount() {
            return listInfoHome.size();
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
                String conent = "Địa chỉ : " + listInfoHome.get(position).getAddress()  + "\n"
                        + "Diện tích : " + listInfoHome.get(position).getArea() + "\n"
                        + "Giá : " + listInfoHome.get(position).getPrice() + "\n"
                        + "Thông tin : " + listInfoHome.get(position).getInfomation() + "\n"
                        + "Điện thoại: " + listInfoHome.get(position).getPhone() + "\n";

                //link anh
                listInfoHome.get(position).getLinkImg();

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

    public List<InfomationRegister> getHomeUser(String user, InfoFragment.AdapterOfUser adapterOfUser){
        myRef.child("Master").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                InfomationRegister infomation = dataSnapshot.getValue(InfomationRegister.class);
                if (infomation.getUser().equals(user)){
                    listInfoOfUser.add(infomation);

                }
                adapterOfUser.notifyDataSetChanged();

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
        return listInfoOfUser;
    }

    public void removeData(String user, String addraess, InfoFragment.AdapterOfUser adapterOfUser){
        myRef.child("Master").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String address) {
                InfomationRegister infomation = dataSnapshot.getValue(InfomationRegister.class);
                if (infomation.getAddress().equals(addraess) && infomation.getUser().equals(user)){
                    DatabaseReference re = dataSnapshot.getRef();
                    re.removeValue();
                }

                adapterOfUser.notifyDataSetChanged();

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
    }


    public void signInAcc(String email, String pass, ISignIn mInterf) {
        signIn(email, pass, mInterf);
    }

    public void forgotPass(String email){
//        mAuth.sendPasswordResetEmail(email);
        mAuth.sendPasswordResetEmail(email);
    }

    private void signIn(String email, String pass, final ISignIn mInterf) {
        mAuth.signInWithEmailAndPassword( email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext, "Succesfully", Toast.LENGTH_SHORT).show();
                            mInterf.afterSignIn();
                        }else {
                            Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public interface ISignIn{
        void afterSignIn();
    }

}
