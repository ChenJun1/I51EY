package com.cvnavi.logistics.i51eyun.app.activity.driver.home.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.statistics.DriverMyGridtextAdapter;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation.DriverCarSchedulingSearchActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.StowageStatisticsSummary.DetailList;
import com.cvnavi.logistics.i51eyun.app.bean.model.StowageStatisticsSummary.mStowageStatisticsSummary;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.GetStowageStatisticsSummaryRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetStowageStatisticsSummaryResponse;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DateUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.gridview.MyGridView;
import com.cvnavi.logistics.i51eyun.app.widget.popupmenu.DataTimeSearchPopupMenu;
import com.cvnavi.logistics.i51eyun.app.widget.segmentview.SegmentView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * Created by fan on 2016/7/21.
 * 配载统计
 */
public class DriverStowageStatisticsSummaryActivity extends BaseActivity {

    private final List<String> img_text = new ArrayList<String>();
    private final String[] img_texts = {Utils.getResourcesString(R.string.statistic_sum_send_car), Utils.getResourcesString(R.string.statistic_sum_cost), Utils.getResourcesString(R.string.statistic_vehicle), Utils.getResourcesString(R.string.statistic_with_the_car), Utils.getResourcesString(R.string.statistic_sum_income), Utils.getResourcesString(R.string.statistic_sum_profit),};
    private static final int F_REQUEST_CODE = 0x2;
    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.titlt_tv)
    TextView titltTv;
    @BindView(R.id.add_iv)
    ImageView addIv;
    @BindView(R.id.add_ll)
    LinearLayout addLl;
    @BindView(R.id.search_iv)
    ImageView searchIv;
    @BindView(R.id.search_ll)
    LinearLayout searchLl;
    @BindView(R.id.right_ll)
    LinearLayout rightLl;
    @BindView(R.id.mSegmentView)
    SegmentView mSegmentView;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    @BindView(R.id.myGridView)
    MyGridView myGridView;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.check_tv)
    TextView checkTv;
    @BindView(R.id.add)
    LinearLayout add;
    @BindView(R.id.custom_ll)
    LinearLayout customLl;
    @BindView(R.id.data_search_text)
    TextView dataSearchText;
    @BindView(R.id.data_search_img)
    ImageView dataSearchImg;
    @BindView(R.id.data_search_layout)
    LinearLayout dataSearchLayout;


    private boolean mChoose = true;// 判断是否是条件选择查询
    private int NearDate = 7;// 初始请求一周

    private String mStarTime; //开始时间
    private String mEendTime;//结束时间
    private DriverMyGridtextAdapter myAdapter;
    private mStowageStatisticsSummary sList;

    private SweetAlertDialog sweetAlertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deiver_stowage_statistics_summary);
        ButterKnife.bind(this);
        init();
        myAdapter = new DriverMyGridtextAdapter(this, img_text, img_texts);
        myGridView.setAdapter(myAdapter);
//        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        Intent intent = new Intent(DriverStowageStatisticsSummaryActivity.this, DriverStowageStatisticsListActivity.class);
//                        intent.putExtra("mStarTime", mStarTime);
//                        intent.putExtra("mEendTime", mEendTime);
//                        startActivity(intent);
//                        break;
//                }
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sweetAlertDialog.show();
        requestHttp();
    }

    private void init() {
        titltTv.setText("配载统计");
        customLl.setVisibility(View.VISIBLE);
        checkTv.setText("明细");
        add.setVisibility(View.GONE);
        dataSearchText.setText(DateUtil.getNowTime(DateUtil.FORMAT_YM_CN));

        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        mStarTime = DateUtil.getBeforeDayNsTime(3);
//        mEendTime = DateUtil.getNowTime();
        mStarTime = DateUtil.getStartYMTime(DateUtil.FORMAT_YMDHM,0);
        mEendTime = DateUtil.getEndYMTime(DateUtil.FORMAT_YMDHM,0);

        mSegmentView.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {

            @Override
            public void onSegmentViewClick(TextView v, int position) {

                switch (position) {
                    case 0:
                        orderNearDate(3);
                        break;
                    case 1:
                        orderNearDate(7);
                        break;
                    case 2:
                        orderNearDate(30);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initChart() {

        lineChart.animateX(0);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDescription("配载统计");
        lineChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        lineChart.getXAxis().setGridColor(getResources().getColor(R.color.transparent));//隐藏Y轴线

        //从X轴进入的动画
        lineChart.animateX(1000);
        lineChart.animateY(1000);   //从Y轴进入的动画
        lineChart.animateXY(1000, 1000);    //从XY轴一起进入的动画
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
//        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setAxisLineColor(Color.parseColor("#ffffff"));//设置轴线颜色
        lineChart.getAxisLeft().setDrawAxisLine(false);//隐藏轴线只显示数字标签

//        MyMarkView myMarkView = new MyMarkView(this,R.layout.custom_marker_view);
//        myMarkView.getXOffset(-myMarkView.getMeasuredWidth()/2);
//        myMarkView.getYOffset(-myMarkView.getMeasuredHeight());
//        lineChart.setMarkerView(myMarkView);

        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<Entry> yValue = new ArrayList<>();
        LineDataSet dataSet = new LineDataSet(yValue, "合计发车");
        if (sList.DetailList != null && sList.DetailList.size() > 0) {
            for (int i = 0; i < sList.DetailList.size(); i++) {
                DetailList list = sList.DetailList.get(i);
                xValues.add(DateUtil.strOldFormat2NewFormat(list.Letter_Date, DateUtil.FORMAT_YMD, DateUtil.FORMAT_MD));
                yValue.add(new Entry(list.Letter_Count, i));
            }
        }
        dataSet.setCircleSize(4f);
        dataSet.setLineWidth(1.75f); // 线宽
        dataSet.setDrawFilled(true);// 填充
        dataSet.setDrawCubic(true);  //设置曲线为圆滑的线
        dataSet.setValueTextSize(7f);//设置标注数据显示的大小
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(xValues, dataSets);
        lineChart.setData(lineData);

    }

    @OnClick({R.id.back_llayout, R.id.custom_ll, R.id.data_search_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;
            case R.id.custom_ll:
                Intent intent = new Intent(DriverStowageStatisticsSummaryActivity.this, DriverStowageStatisticsListActivity.class);
                intent.putExtra("mStarTime", mStarTime);
                intent.putExtra("mEendTime", mEendTime);
                startActivity(intent);
//                DriverCarSchedulingSearchActivity.startActivity(DriverStowageStatisticsSummaryActivity.this, F_REQUEST_CODE);
                break;
            case R.id.data_search_layout:
                DataTimeSearchPopupMenu popupMenu = new DataTimeSearchPopupMenu(DriverStowageStatisticsSummaryActivity.this);
                popupMenu.showLocation(R.id.data_search_img);
                popupMenu.setOnItemClickListener(new DataTimeSearchPopupMenu.OnItemClickListener() {
                    @Override
                    public void onClick(DataTimeSearchPopupMenu.MENUITEM item, String str) {
                        switch (str){
                            case "0":
                                orderNearDate(0);
                                break;
                            case "1":
                                orderNearDate(1);
                                break;
                            case "2":
                                orderNearDate(2);
                                break;
                            case "3":
                                DriverCarSchedulingSearchActivity.startActivity(DriverStowageStatisticsSummaryActivity.this, F_REQUEST_CODE);
                                break;
                        }
                    }
                });
                break;
        }
    }

    private void orderNearDate(int NearDate) {
        this.NearDate = NearDate;

        if (NearDate == 0) {
            dataSearchText.setText(DateUtil.strOldFormat2NewFormat(DateUtil.getStartYMTime(DateUtil.FORMAT_YMDHM,-2),DateUtil.FORMAT_YMDHM,DateUtil.FORMAT_YM_CN));
            mStarTime = DateUtil.getStartYMTime(DateUtil.FORMAT_YMDHM,-2);
            mEendTime = DateUtil.getEndYMTime(DateUtil.FORMAT_YMDHM,-2);

        } else if (NearDate == 1) {
            dataSearchText.setText(DateUtil.strOldFormat2NewFormat(DateUtil.getStartYMTime(DateUtil.FORMAT_YMDHM,-1),DateUtil.FORMAT_YMDHM,DateUtil.FORMAT_YM_CN));
            mStarTime = DateUtil.getStartYMTime(DateUtil.FORMAT_YMDHM,-1);
            mEendTime = DateUtil.getEndYMTime(DateUtil.FORMAT_YMDHM,-1);
        } else if (NearDate == 2) {
            dataSearchText.setText(DateUtil.strOldFormat2NewFormat(DateUtil.getStartYMTime(DateUtil.FORMAT_YMDHM,-0),DateUtil.FORMAT_YMDHM,DateUtil.FORMAT_YM_CN));
            mStarTime = DateUtil.getStartYMTime(DateUtil.FORMAT_YMDHM,-0);
            mEendTime = DateUtil.getEndYMTime(DateUtil.FORMAT_YMDHM,-0);
        }
        mChoose = true;
        sweetAlertDialog.show();
        requestHttp();
    }


    private void requestHttp() {
        DataRequestBase requestBase = new DataRequestBase();

        final GetStowageStatisticsSummaryRequest request = new GetStowageStatisticsSummaryRequest();
        request.BeginDate = mStarTime;
        request.EndDate = mEendTime;

        requestBase.DataValue = request;
        requestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        requestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        requestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        requestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        requestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;

        final JSONObject jsonObject = GsonUtil.newInstance().toJson(requestBase);
        VolleyManager.newInstance().PostJsonRequest("--->", TMSService.GetStowageStatisticsSummary_Request_Url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                LogUtil.d("-->>onResponse============>>" + response.toString());
                mStowageStatisticsSummary carLocusAnaly = null;

                GetStowageStatisticsSummaryResponse analysisRequest = JsonUtils.parseData(response.toString(), GetStowageStatisticsSummaryResponse.class);

                Message msg = Message.obtain();
                if (analysisRequest != null) {
                    if (analysisRequest.Success) {
                        if (analysisRequest.DataValue != null) {
                            sList = analysisRequest.DataValue;
                            msg.what = Constants.REQUEST_SUCC;
                            msg.obj = sList;
                            mHandler.sendMessage(msg);
                        }

                    } else {
                        msg.what = Constants.REQUEST_FAIL;
                        mHandler.sendMessage(msg);
                    }
                } else {
                    msg.what = Constants.REQUEST_FAIL;
                    mHandler.sendMessage(msg);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "ErrorListener============>>" + error.getMessage(), error);
                Message msg = Message.obtain();
                msg.what = Constants.REQUEST_ERROR;
                mHandler.sendMessage(msg);
            }
        });

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            sweetAlertDialog.dismiss();
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.REQUEST_SUCC:

                    mStowageStatisticsSummary datalist = (mStowageStatisticsSummary) msg.obj;
                    if (datalist != null) {
                        Success(datalist);
                    } else {
                        DialogUtils.showNormalToast("暂无数据");
                    }

                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showNormalToast(msg.obj == null ? Utils.getResourcesString(R.string.request_Fill) : msg.obj.toString());
                    break;

                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(DriverStowageStatisticsSummaryActivity.this, Utils.getResourcesString(R.string.request_error));
                    break;
            }
        }
    };

    public void Success(mStowageStatisticsSummary mList) {
        this.sList = mList;

        if (mList != null) {
            if (img_text != null && img_text.size() > 0) {
                img_text.clear();
            }


            if (!TextUtils.isEmpty(mList.Letter_Count)) {
                img_text.add(mList.Letter_Count);//合计发车
            }
            if (!TextUtils.isEmpty(mList.Shuttle_Fee)) {
                img_text.add(mList.Shuttle_Fee);//合计成本
            }
            if (!TextUtils.isEmpty(mList.EntiretyCount)) {
                img_text.add(mList.EntiretyCount);//整车发车
            }

            if (!TextUtils.isEmpty(mList.BulkloadCount)) {
                img_text.add(mList.BulkloadCount);//配货发车
            }
            if (!TextUtils.isEmpty(mList.Ticket_Fee)) {
                img_text.add(mList.Ticket_Fee);//合计收入
            }
            if (!TextUtils.isEmpty(mList.Profit_Fee)) {
                img_text.add(mList.Profit_Fee);//合计利润
            }


            myAdapter.notifyDataSetChanged();
            initChart();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == F_REQUEST_CODE) {

            mStarTime = data.getStringExtra(DriverCarSchedulingSearchActivity.BEGIN_DATA);
            mEendTime = data.getStringExtra(DriverCarSchedulingSearchActivity.END_DATA);

            dataSearchText.setText(mStarTime+" -- "+mEendTime);


            mSegmentView.setSegmentTextCooler();
        }

    }
}
