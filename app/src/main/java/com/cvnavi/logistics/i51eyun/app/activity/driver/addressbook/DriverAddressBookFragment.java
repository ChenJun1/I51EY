package com.cvnavi.logistics.i51eyun.app.activity.driver.addressbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/6/17.
 * 司机端通讯录
 */
public class DriverAddressBookFragment extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView titleTv;

    public static DriverAddressBookFragment getInstance() {
        return new DriverAddressBookFragment();
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
