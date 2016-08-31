package com.cvnavi.logistics.i51eyun.app.activity.driver.home.myorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.addressbook.DriverAddressBookFragment;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.DriverHomeFragment;
import com.cvnavi.logistics.i51eyun.app.activity.driver.me.DriverMeFragement;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/1.
 */
public class DriverMyOrderLastDayFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.container_flayout)
    FrameLayout containerFlayout;
    @BindView(R.id.homepage_rb)
    RadioButton homepageRb;
    @BindView(R.id.addressbooks_rb)
    RadioButton addressbooksRb;
    @BindView(R.id.me_rb)
    RadioButton meRb;
    private long mTime;

    public static DriverMyOrderLastDayFragment getInstance() {
        return new DriverMyOrderLastDayFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order_has_sign, container, false);
        ButterKnife.bind(this, view);
        initview();
        return view;
    }


    private void initview() {
        homepageRb.setOnCheckedChangeListener(this);
        addressbooksRb.setOnCheckedChangeListener(this);
        meRb.setOnCheckedChangeListener(this);

        homepageRb.performClick();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Fragment fragment = (Fragment) mFragmentPagerAdapter.instantiateItem(containerFlayout, buttonView.getId());
            mFragmentPagerAdapter.setPrimaryItem(containerFlayout, 0, fragment);
            mFragmentPagerAdapter.finishUpdate(containerFlayout);
        }
    }

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case R.id.homepage_rb:
                    LogUtil.d("-->>buttommenu_homepage");
                    return DriverHomeFragment.getInstance();
                case R.id.addressbooks_rb:
                    LogUtil.d("-->>buttommenu_addressbooks");
                    return DriverAddressBookFragment.getInstance();
                case R.id.me_rb:
                    LogUtil.d("-->>buttommenu_me");
                    return DriverMeFragement.getInstance();
                default:
                    return DriverHomeFragment.getInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

}
