package com.example.minhd.ezhome.ui.base.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.minhd.ezhome.R;
import com.example.minhd.ezhome.interact.ApiService;
import com.example.minhd.ezhome.interact.FirebaseSever;
import com.example.minhd.ezhome.ui.base.InfomationRegister;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.minhd.ezhome.ui.main.MainActivity.name;
import static com.example.minhd.ezhome.ui.main.MainActivity.personName;

public class RegisterFragment extends BaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText edtRegJob, edtRegAdress, edtRegPhoneNumber, edtRegNote ;
    private TextView tvRegName ;
    private Spinner spPrice, spDientich ;
    private ApiService apiService;
    private String [] arrPrice = {"< 1.000.00", "1.000.000-2.000.000",
        "2.000.000-3.000.000", "3.000.000-4.000.000", "> 4.000.000"} ;
    private String [] arrDientich = {"< 15M", "15-30M", "30-60M", "> 60M"} ;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds();
        creaAdapterSpiner();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_register;
    }

    @Override
    public void findViewByIds() {

        tvRegName = (TextView) getView().findViewById(R.id.RegName) ;
        edtRegJob = (EditText) getView().findViewById(R.id.edt_work) ;
        edtRegAdress = (EditText) getView().findViewById(R.id.edt_address) ;
        edtRegNote = (EditText) getView().findViewById(R.id.edt_note) ;
        edtRegPhoneNumber = (EditText) getView().findViewById(R.id.edt_phoneNumber) ;

        spPrice = (Spinner) getView().findViewById(R.id.spiner_gia) ;
        spDientich = (Spinner) getView().findViewById(R.id.spiner_dientich) ;

    }

    @Override
    public void initComponents() {
        getView().findViewById(R.id.btn_RegHome).setOnClickListener(this); ;

    }

    @Override
    public void setEvents() {

        if (personName != null) {
            tvRegName.setText(personName);
        }
        if (name != null) {
            tvRegName.setText(name);
        }
    }

    private void creaAdapterSpiner() {
        ArrayAdapter<String> adapterPrice
                = new ArrayAdapter<>(getActivity(), R.layout.spinner_custom, arrPrice);
        ArrayAdapter<String> adapterDientich
                = new ArrayAdapter<>(getActivity(), R.layout.spinner_custom, arrDientich);

        adapterPrice.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        adapterDientich.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        spPrice.setAdapter(adapterPrice);
        spDientich.setAdapter(adapterDientich);

        spPrice.setOnItemSelectedListener(this);
        spDientich.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (view.getId()) {

            case R.id.spiner_gia:

                break;

            case R.id.spiner_dientich:

                break;

            default:

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        String address = edtRegAdress.getText().toString();
        String phoneNumber = edtRegPhoneNumber.getText().toString();
        String price = spPrice.getSelectedItem().toString();
        String area = spDientich.getSelectedItem().toString();
        String info = edtRegNote.getText().toString();
        InfomationRegister infomation = new InfomationRegister(address, phoneNumber, price, area, info );

        FirebaseSever re = new FirebaseSever(getContext(), infomation);
    }
}