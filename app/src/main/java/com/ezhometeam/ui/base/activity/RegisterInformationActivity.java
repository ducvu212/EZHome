package com.ezhometeam.ui.base.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ezhometeam.R;
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

/**
 * Created by n on 06/07/2017.
 */

public class RegisterInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private String userId;
    private ImageView tvPath;
    private int REQUEST_CODE_CAM = 123;
    private String linkImage;


    public RegisterInformationActivity() {
    }

    private EditText edtSdt, edtGhiChu, edtKhuVuc;
    private Spinner spGia, spDientich, spTpho, spQHuyen;
    private String[] arrGia = {"< 1.000.00", "1.000.000-2.000.000",
            "2.000.000-3.000.000", "3.000.000-4.000.000", "> 4.000.000"};
    private String[] arrDientich = {"< 15M", "15-30M", "30-60M", "> 60M"};
    private String[] arrThanhPho = {"Hà Nội", "Thành phố Hồ Chí Minh"};
    private String[] arrQuanHuyenHN = {
            "Quận Ba Đình", "Quận Hoàn Kiếm", "Quận Hai Bà Trưng", "Quận Đống Đa", "Quận Tây Hồ",
            "Quận Cầu Giấy", "Quận Thanh Xuân", "Quận Hoàng Mai", "Quận Long Biên", "Huyện Từ Liêm",
            "Huyện Thanh Trì", "Huyện Gia Lâm", "Huyện Đông Anh", "Huyện Sóc Sơn", "Quận Hà Đông",
            "Thị xã Sơn Tây", "Huyện Ba Vì", "Huyện Phúc Thọ", "Huyện Thạch Thất", "Huyện Quốc Oai",
            "Huyện Chương Mỹ", "Huyện Đan Phượng", "Huyện Hoài Đức", "Huyện Thanh Oai", "Huyện Mỹ Đức",
            "Huyện Ứng Hoà", "Huyện Thường Tín", "Huyện Phú Xuyên", "Huyện Mê Linh"};
    private String[] arrQuanHuyenTPHCM = {
            "Quận 1", "Quận 12", "Quận Thủ Đức", "Quận 9", "Quận Gò Vấp", "Quận Bình Thạnh", "Quận Tân Bình",
            "Quận Tân Phú", "Quận Phú Nhuận", "Quận 2", "Quận 3", "Quận 10", "Quận 11", "Quận 4", "Quận 5",
            "Quận 6", "Quận 8", "Quận Bình Tân", "Quận 7", "Huyện Củ Chi", "Huyện Hóc Môn", "Huyện Bình Chánh",
            "Huyện Nhà Bè",
            "Huyện Cần Giờ"
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_re);
        mContext = this;
        findViewByIds();
        creaAdapterSpiner();

        Intent intent = getIntent();
        userId = intent.getStringExtra("EMAIL");

    }

    public void findViewByIds() {
        spGia = (Spinner) findViewById(R.id.spinner_gia_tien);
        spDientich = (Spinner) findViewById(R.id.spinner_dien_tich);
        spTpho = (Spinner) findViewById(R.id.spinner_thanh_pho);
        spQHuyen = (Spinner) findViewById(R.id.spinner_quan_huyen);

        edtKhuVuc = (EditText) findViewById(R.id.edt_khu_vuc);
        edtSdt = (EditText) findViewById(R.id.edt_sdt);
        edtGhiChu = (EditText) findViewById(R.id.edt_ghi_chu);

        tvPath = (ImageView) findViewById(R.id.tv_Path);

        findViewById(R.id.btn_dang_ki_nha_tro).setOnClickListener(this);
        findViewById(R.id.iv_camera).setOnClickListener(this);
        findViewById(R.id.iv_storage).setOnClickListener(this);

    }

    private void creaAdapterSpiner() {
        ArrayAdapter<String> adapterThanhPho
                = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrThanhPho);
        adapterThanhPho.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spTpho.setAdapter(adapterThanhPho);
        adapterThanhPho.notifyDataSetChanged();


        spTpho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ArrayAdapter adapterQuanHuyen;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Hà Nội")) {
                    adapterQuanHuyen = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, arrQuanHuyenHN);

                } else {
                    adapterQuanHuyen = new ArrayAdapter<>(mContext, R.layout.support_simple_spinner_dropdown_item, arrQuanHuyenTPHCM);
                }
                adapterQuanHuyen.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spQHuyen.setAdapter(adapterQuanHuyen);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


        ArrayAdapter<String> adapterGia
                = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrGia);
        adapterGia.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spGia.setAdapter(adapterGia);


        ArrayAdapter<String> adapterDientich
                = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, arrDientich);
        adapterDientich.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spDientich.setAdapter(adapterDientich);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_dang_ki_nha_tro:
                uploadImg();
                dangKiNhaTro();
                Intent intent = new Intent();
                intent.setClass(this, Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.iv_camera:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, REQUEST_CODE_CAM);
                break;
            case R.id.iv_storage:
                Intent iGallery = new Intent();
                iGallery.setClass(this, GalleryActivity.class);
                startActivityForResult(iGallery, 1);
                break;
            default:
                break;
        }
    }

    private void uploadImg() {

        String tPho = spTpho.getSelectedItem().toString();
        String quanHuyen = spQHuyen.getSelectedItem().toString();
        String address = edtKhuVuc.getText().toString() + ", " + quanHuyen + ", " + tPho;
        String phoneNumber = edtSdt.getText().toString();
        String price = spGia.getSelectedItem().toString();
        String area = spDientich.getSelectedItem().toString();
        String info = edtGhiChu.getText().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ezhome-5f445.appspot.com");

        StorageReference storageRef = storage.getReference();
        Calendar calendar = Calendar.getInstance();
// Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("image"+ calendar.getTimeInMillis()+".png");


        tvPath.setDrawingCacheEnabled(true);
        tvPath.buildDrawingCache();
        Bitmap bitmap = tvPath.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                linkImage = String.valueOf(downloadUrl);
                InfomationRegister infomation = new InfomationRegister(address, phoneNumber, price, area, info, userId, linkImage);
                FirebaseSever re = new FirebaseSever(getBaseContext(), infomation);
            }
        });


    }

    private void dangKiNhaTro() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAM && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            tvPath.setImageBitmap(bitmap);

        }else {
            if (requestCode == 1 && resultCode == RESULT_OK && data != null){
                ItemGallaryImage gallaryImage = (ItemGallaryImage) data.getSerializableExtra(GalleryActivity.KEY_RETURE_GALLERY);
                int width = tvPath.getWidth();
                int height = width * gallaryImage.getPairInt().getSecond() / gallaryImage.getPairInt().getFirst();
                Picasso.with(this)
                        .load(new File(gallaryImage.getPathFile()))
                        .placeholder(android.R.color.darker_gray)
                        .error(android.R.color.darker_gray)
                        .resize(width, height)
                        .into(tvPath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
