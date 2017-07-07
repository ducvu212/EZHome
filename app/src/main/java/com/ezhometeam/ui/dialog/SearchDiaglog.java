package com.ezhometeam.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ezhometeam.R;
import com.ezhometeam.ui.base.fragment.HomeFragment;
import com.ezhometeam.ui.main.MainActivity;

/**
 * Created by minhd on 17/07/06.
 */

public class SearchDiaglog extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static View view;
    private Spinner spCity, spDistrict, spPrice, spDientich;
    private String[] arrPrice = {"< 1.000.00", "1.000.000-2.000.000",
            "2.000.000-3.000.000", "3.000.000-4.000.000", "> 4.000.000"};
    private String[] arrDientich = {"< 15M", "15-30M", "30-60M", "> 60M"};
    private String[] arrCity = {"Hà Nội", "Hồ Chí Minh"};
    private String[] arrDistrict = {"Ba Đình", "Hoàn Kiếm", "Đống Đa", "Thanh Xuân"
            , "Cầu Giấy", "Hoàng Mai", "Hai Bà Trưng", "Tây Hồ", "Quận Hoàng Mai",
            "Quận Long Biên", "Quận Từ Liêm", "Quận Hà Đông", "Huyện Thanh Trì", "Quận Gia Lâm"};
    private Button btnSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);
        findViewByIds();
        initComponents();
        creaAdapterSpiner();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void findViewByIds() {

        spPrice = (Spinner) view.findViewById(R.id.spinner_gia_tien);
        spDientich = (Spinner) view.findViewById(R.id.spinner_dien_tich);
        spCity = (Spinner) view.findViewById(R.id.spinner_thanh_pho);
        spDistrict = (Spinner) view.findViewById(R.id.spinner_quan_huyen);
    }

    public void initComponents() {
        view.findViewById(R.id.btn_search).setOnClickListener(this);
    }


    private void creaAdapterSpiner() {

        ArrayAdapter<String> adapterPrice
                = new ArrayAdapter<>(getActivity(), R.layout.spinner_custom, arrPrice);
        ArrayAdapter<String> adapterDientich
                = new ArrayAdapter<>(getActivity(), R.layout.spinner_custom, arrDientich);
        ArrayAdapter<String> adapterThanhPho
                = new ArrayAdapter<>(getActivity(), R.layout.spinner_custom, arrCity);
        ArrayAdapter<String> adapterQuan
                = new ArrayAdapter<>(getActivity(), R.layout.spinner_custom, arrDistrict);

        adapterPrice.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapterDientich.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapterThanhPho.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapterQuan.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


        spPrice.setAdapter(adapterPrice);
        spDientich.setAdapter(adapterDientich);
        spCity.setAdapter(adapterThanhPho);
        spDistrict.setAdapter(adapterQuan);

        spPrice.setOnItemSelectedListener(this);
        spDientich.setOnItemSelectedListener(this);
        spCity.setOnItemSelectedListener(this);
        spDistrict.setOnItemSelectedListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                HomeFragment homeFragment = new HomeFragment();
                MainActivity mainActivity = (MainActivity) getActivity() ;
                android.support.v4.app.FragmentManager manager = mainActivity.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(homeFragment, "Ahihi") ;
                transaction.commit();
                break;

            default:
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
