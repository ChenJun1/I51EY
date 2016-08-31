package com.cvnavi.logistics.i51eyun.app.activity.driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.MainActivity;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.addressbook.DriverAddressBookFragment;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.DriverHomeFragment;
import com.cvnavi.logistics.i51eyun.app.activity.driver.me.DriverMeFragement;
import com.cvnavi.logistics.i51eyun.app.service.MyCheckLoginService;
import com.cvnavi.logistics.i51eyun.app.ui.ActivityStackManager;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.ActionSheetDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.ActionSheetItemInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 版权所有势航网络
 * Created by ${ChenJ} on 2016/7/25.
 */
public class DriverMainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.container_flayout)
    FrameLayout containerFlayout;
    @BindView(R.id.homepage_rb)
    RadioButton homepageRb;
    @BindView(R.id.addressbooks_rb)
    RadioButton addressbooksRb;
    @BindView(R.id.me_rb)
    RadioButton meRb;
    private long mTime;
    private Intent myIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("-->>onCreate");

        setContentView(R.layout.activity_main_driver);
        ButterKnife.bind(this);
        initview();
        myIntent = new Intent(DriverMainActivity.this, MyCheckLoginService.class);
        DriverMainActivity.this.startService(myIntent);//发送Intent启动Service

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
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

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case R.id.homepage_rb:
                    LogUtil.d("-->>buttommenu_homepage");
                    return DriverHomeFragment.getInstance();
                case R.id.addressbooks_rb:
//                    Intent intent=new Intent(DriverMainActivity.this, MyFleetRecordActivity.class);
//                    Intent intent=new Intent(DriverMainActivity.this, MyFleetAlarmInfoActivity.class);
//                    Intent intent=new Intent(DriverMainActivity.this, MyFleetMonitorMapActivity.class);
//                    startActivity(intent);
//                    LogUtil.d("-->>buttommenu_addressbooks");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mTime > 2000) {
                Toast.makeText(getApplication(), "再按一次退出!", Toast.LENGTH_SHORT).show();
                mTime = System.currentTimeMillis();
            } else {
                ActivityStackManager.getInstance().AppExit(getApplicationContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (LogUtil.debug) {
            ArrayList<ActionSheetItemInfo> list = new ArrayList<>();
            list.add(new ActionSheetItemInfo("测试", new ActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    startActivity(new Intent(DriverMainActivity.this, MainActivity.class));

                }
            }));

            DialogUtils.showActionSheetDialog(this, "测试", list);

        }
        return true;

    }
}
