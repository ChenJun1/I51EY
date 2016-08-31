package com.cvnavi.logistics.i51eyun.app.activity.employee.addressbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics.DriverAccountsReceiveMainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeAddressBookFragment extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.test)
    TextView test;

    public static EmployeeAddressBookFragment instantiation() {
        return new EmployeeAddressBookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_addressbook, container, false);
        ButterKnife.bind(this, view);

        titleTv.setText(R.string.cargo_addressbook_name);
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView()
                    .setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }


}
