package com.ezhometeam.ui.base.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ezhometeam.R;
import com.ezhometeam.common.Constants;
import com.ezhometeam.common.InfomationRegister;
import com.ezhometeam.gallery.GalleryActivity;
import com.ezhometeam.gallery.utils.ItemGallaryImage;
import com.ezhometeam.interact.FirebaseSever;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        View.OnClickListener, Constants {
    private ImageView IvImage;
    private FirebaseSever svRe;
    private String linkImage;
    private FirebaseStorage storage;
    private String userId;
    private EditText edtSdt, edtGhiChu, edtKhuVuc;
    private Spinner spGia, spDientich, spTpho, spQHuyen;

    public RegisterFragment() {
    }

    public RegisterFragment(String user) {
        userId = user;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        creaAdapterSpiner();
    }


    @Override
    public int getLayoutMain() {
        return R.layout.layout_re ;
    }

    @Override
    public void findViewByIds() {
        spGia = (Spinner) getView().findViewById(R.id.spinner_gia_tien);
        spDientich = (Spinner) getView().findViewById(R.id.spinner_dien_tich);
        spTpho = (Spinner) getView().findViewById(R.id.spinner_thanh_pho);
        spQHuyen = (Spinner) getView().findViewById(R.id.spinner_quan_huyen);

        edtKhuVuc = (EditText) getView().findViewById(R.id.edt_dia_chi);
        edtSdt = (EditText) getView().findViewById(R.id.edt_sdt);
        edtGhiChu = (EditText) getView().findViewById(R.id.edt_ghi_chu);

        IvImage = (ImageView) getView().findViewById(R.id.tv_Path);

        getView().findViewById(R.id.btn_dang_ki_nha_tro).setOnClickListener(this);
        getView().findViewById(R.id.iv_camera).setOnClickListener(this);
        getView().findViewById(R.id.iv_storage).setOnClickListener(this);

    }

    @Override
    public void initComponents() {
        getView().findViewById(R.id.btn_dang_ki_nha_tro).setOnClickListener(this);

    }

    @Override
    public void setEvents() {

//        if (personName != null) {
//            tvRegName.setText(personName);
//        }
//        if (name != null) {
//            tvRegName.setText(name);
//        }
    }

    private void creaAdapterSpiner() {
        ArrayAdapter<String> adapterThanhPho
                = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrThanhPho);
        adapterThanhPho.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spTpho.setAdapter(adapterThanhPho);
        adapterThanhPho.notifyDataSetChanged();


        spTpho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ArrayAdapter adapterQuanHuyen;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Hà Nội")) {
                    adapterQuanHuyen = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrQuanHuyenHN);

                } else {
                    adapterQuanHuyen = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrQuanHuyenTPHCM);
                }
                adapterQuanHuyen.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spQHuyen.setAdapter(adapterQuanHuyen);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        ArrayAdapter<String> adapterGia
                = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrGia);
        adapterGia.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spGia.setAdapter(adapterGia);


        ArrayAdapter<String> adapterDientich
                = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrDientich);
        adapterDientich.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spDientich.setAdapter(adapterDientich);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        switch (view.getId()) {
//
//            default:
//                break;
//        }
    }

    private void uploadImg() {
        svRe = new FirebaseSever(getContext());
        storage = FirebaseStorage.getInstance("gs://ezhome-5f445.appspot.com");

        String tPho = spTpho.getSelectedItem().toString();
        String quanHuyen = spQHuyen.getSelectedItem().toString();
        String address = edtKhuVuc.getText().toString() + ", " + quanHuyen + ", " + tPho;
        String phoneNumber = edtSdt.getText().toString();
        String price = spGia.getSelectedItem().toString();
        String area = spDientich.getSelectedItem().toString();
        String info = edtGhiChu.getText().toString();


        StorageReference storageRef = storage.getReference();
        Calendar calendar = Calendar.getInstance();
// Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");


        IvImage.setDrawingCacheEnabled(true);
        IvImage.buildDrawingCache();
        Bitmap bitmap = IvImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                linkImage = String.valueOf(downloadUrl);
                InfomationRegister infomation = new InfomationRegister(address, phoneNumber, price, area, info, userId, linkImage);
                svRe.register(infomation);
                edtGhiChu.setText("");
                edtKhuVuc.setText("");
                edtSdt.setText("");
                Bitmap image = Bitmap.createBitmap(122, 122, Bitmap.Config.ARGB_8888);
                image.eraseColor(Color.parseColor("#ECEFF1"));
                IvImage.setImageBitmap(image);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dang_ki_nha_tro:
                uploadImg();
                break;
            case R.id.iv_camera:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, REQUEST_CODE_CAM);
                break;
            case R.id.iv_storage:
                Intent iGallery = new Intent();
                iGallery.setClass(getContext(), GalleryActivity.class);
                startActivityForResult(iGallery, REQUEST_CODE_GALLERY);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAM && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            IvImage.setImageBitmap(bitmap);

        } else {
            if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
                ItemGallaryImage gallaryImage = (ItemGallaryImage) data.getSerializableExtra(GalleryActivity.KEY_RETURE_GALLERY);
                int width = IvImage.getWidth();
                int height = width * gallaryImage.getPairInt().getSecond() / gallaryImage.getPairInt().getFirst();
                Picasso.with(getContext())
                        .load(new File(gallaryImage.getPathFile()))
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.color.darker_gray)
                        .resize(width, height)
                        .into(IvImage);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
