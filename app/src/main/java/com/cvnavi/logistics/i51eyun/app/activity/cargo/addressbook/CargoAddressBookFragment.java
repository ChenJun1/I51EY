package com.cvnavi.logistics.i51eyun.app.activity.cargo.addressbook;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CargoAddressBookFragment extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView titleTv;

    public static CargoAddressBookFragment instantiation() {
        return new CargoAddressBookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cargo_addressbook, container, false);
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
