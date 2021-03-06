package com.cvnavi.logistics.i51eyun.app.activity.cargo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.MainActivity;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.cargo.addressbook.CargoAddressBookFragment;
import com.cvnavi.logistics.i51eyun.app.activity.cargo.home.CargoHomeFragment;
import com.cvnavi.logistics.i51eyun.app.activity.cargo.me.CargoMeFragment;
import com.cvnavi.logistics.i51eyun.app.service.MyCheckLoginService;
import com.cvnavi.logistics.i51eyun.app.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.ActionSheetDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.ActionSheetItemInfo;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *
 */
public class CargoMainActivity extends BaseActivity implements OnCheckedChangeListener {

    @BindView(R.id.container_flayout)
    FrameLayout containerFlayout;
    @BindView(R.id.homepage_rb)
    RadioButton homepageRb;
    @BindView(R.id.addressbook_rb)
    RadioButton addressbooksRb;
    @BindView(R.id.me_rb)
    RadioButton meRb;

    private long exitTime;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_main_cargo);
        ButterKnife.bind(this);

        initview();
        myIntent = new Intent(CargoMainActivity.this, MyCheckLoginService.class);
        CargoMainActivity.this.startService(myIntent);//发送Intent启动Service
    }

    private void initview() {
        homepageRb.setOnCheckedChangeListener(this);
        addressbooksRb.setOnCheckedChangeListener(this);
        meRb.setOnCheckedChangeListener(this);
        Utils.getQueryLength();

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

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case R.id.homepage_rb:
                    return CargoHomeFragment.instantiation();
                case R.id.addressbook_rb:
                    return CargoAddressBookFragment.instantiation();
                case R.id.me_rb:
                    return CargoMeFragment.instantiation();
                default:
                    return CargoHomeFragment.instantiation();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplication(), "再按一次退出!", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityStackManager.getInstance().AppExit(getApplicationContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (LogUtil.debug) {
            ArrayList<ActionSheetItemInfo> list = new ArrayList<>();
            list.add(new ActionSheetItemInfo("测试", new ActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    startActivity(new Intent(CargoMainActivity.this, MainActivity.class));

                }
            }));

            DialogUtils.showActionSheetDialog(this, "测试", list);

        }
        return true;

    }
}
