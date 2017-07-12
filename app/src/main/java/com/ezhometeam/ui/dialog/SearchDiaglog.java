package com.ezhometeam.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ezhometeam.R;
import com.ezhometeam.common.Constants;
import com.ezhometeam.ui.base.activity.Main2Activity;
import com.ezhometeam.ui.base.fragment.HomeFragment;

import static com.ezhometeam.ui.base.activity.Main2Activity.btnMenu;
import static com.ezhometeam.ui.base.activity.Main2Activity.imgLocation;
import static com.ezhometeam.ui.base.activity.Main2Activity.imgSearch;
import static com.ezhometeam.ui.base.activity.Main2Activity.tvSearch;

/**
 * Created by minhd on 17/07/06.
 */

public class SearchDiaglog extends DialogFragment implements Constants,View.OnClickListener {

    public static View view;
    private Spinner spCity, spDistrict, spPrice, spDientich;
    private Button btnSearch;
    private SearchDiaglog searchDiaglog;
    public static String address;

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
        searchDiaglog = new SearchDiaglog();
        try {
            imgSearch.setVisibility(View.VISIBLE);
            tvSearch.setVisibility(View.VISIBLE);
            btnMenu.setVisibility(View.VISIBLE);
            imgLocation.setVisibility(View.VISIBLE);
        }catch (NullPointerException e) {}

    }


    private void creaAdapterSpiner() {

        ArrayAdapter<String> adapterPrice
                = new ArrayAdapter<>(getActivity(), R.layout.spinner_custom, arrGia);
        ArrayAdapter<String> adapterDientich
                = new ArrayAdapter<>(getActivity(), R.layout.spinner_custom, arrDientich);
        ArrayAdapter<String> adapterThanhPho
                = new ArrayAdapter<>(getActivity(), R.layout.spinner_custom, arrThanhPho);

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ArrayAdapter adapterQuanHuyen;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Hà Nội")) {
                    adapterQuanHuyen = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrQuanHuyenHN);

                } else {
                    adapterQuanHuyen = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, arrQuanHuyenTPHCM);
                }
                address = arrQuanHuyenHN[position] ;
                adapterQuanHuyen.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                spDistrict.setAdapter(adapterQuanHuyen);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        adapterPrice.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapterDientich.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapterThanhPho.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


        spPrice.setAdapter(adapterPrice);
        spDientich.setAdapter(adapterDientich);
        spCity.setAdapter(adapterThanhPho);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                dismiss();
                String place = spDistrict.getSelectedItem().toString() +
                        ", " +spCity.getSelectedItem().toString();

                Bundle bundle = new Bundle();
                bundle.putString("PLACE", place);

                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                Main2Activity mainActivity = (Main2Activity) getActivity();
                android.support.v4.app.FragmentManager manager = mainActivity.getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.map, homeFragment, HomeFragment.class.getName()) ;
                transaction.addToBackStack("Ahihi");
                try {
                    imgSearch.setVisibility(View.INVISIBLE);
                    tvSearch.setVisibility(View.INVISIBLE);
                    btnMenu.setVisibility(View.INVISIBLE);
                    imgLocation.setVisibility(View.INVISIBLE);
                } catch (NullPointerException e) {
                }
                transaction.commit();
                break;

            default:
        }
    }



}
