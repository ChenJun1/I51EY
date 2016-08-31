package com.cvnavi.logistics.i51eyun.app.activity.driver.home.mytask.preview;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.PictureBean;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.ScreenSupport;
import com.cvnavi.logistics.i51eyun.app.widget.view.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import volley.VolleyManager;


public class PreviewPicPagerActivity extends BaseActivity implements OnPageChangeListener {

    @BindView(R.id.viewpager)
    HackyViewPager mViewPager;
    @BindView(R.id.position_tv)
    TextView positionTv;
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.title_tv)
    TextView titleTv;


    private PreviewPicPagerAdapter mGuidViewPagerAdapter;
    private List<View> mViews;

    /**
     * 底部小点图片
     */
    /**
     * 记录当前选中位置
     **/
    private int currentIndex;

    private List<PictureBean> list;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pic);
        ButterKnife.bind(this);

        ScreenSupport.setFullScreen(this, true);
        initData();
        initview();
    }

    private void initData() {
        if (getIntent().getSerializableExtra(Constants.PictureBeanList) != null) {
            list = (List<PictureBean>) getIntent().getSerializableExtra(Constants.PictureBeanList);
        }
        if (getIntent().getSerializableExtra(Constants.POSITION) != null) {
            position = getIntent().getIntExtra(Constants.POSITION, 0);
        }
    }

    private void initview() {
        backLlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViews = new ArrayList<View>();
        titleTv.setText("预览");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //初始化引导图片列表
        for (int i = 0; i < list.size(); i++) {
//            ImageView imageView = new ImageView(this);
            PhotoView imageView=new PhotoView(this);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            VolleyManager.newInstance().ImageLoaderRequest(imageView, list.get(i).FilePath, R.drawable.cry, R.drawable.cry);
            mViews.add(imageView);
        }

        positionTv.setText((1) + "/" + mViews.size());
        //初始化adapter
        mGuidViewPagerAdapter = new PreviewPicPagerAdapter(mViews);

        mViewPager.setAdapter(mGuidViewPagerAdapter);
        //绑定回调
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(position);
    }





    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    //当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
        positionTv.setText((arg0 + 1) + "/" + mViews.size());

    }

}
