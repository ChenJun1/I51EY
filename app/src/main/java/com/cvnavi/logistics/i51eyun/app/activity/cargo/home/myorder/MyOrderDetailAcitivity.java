package com.cvnavi.logistics.i51eyun.app.activity.cargo.home.myorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myorder.DriverExceptionInfoActivity;
import com.cvnavi.logistics.i51eyun.app.activity.driver.home.myorder.DriverMyOrderLogisticsFollowActivity;
import com.cvnavi.logistics.i51eyun.app.bean.model.mOrderDetail;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.OrederDetailedRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetAppLoginResponse;
import com.cvnavi.logistics.i51eyun.app.bean.response.OrederDetailedResponse;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SetViewValueUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.popupwindow.OrderDetailPopWindow;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/21.
 * 货单明细(货主跳到货单明细)
 */
public class MyOrderDetailAcitivity extends BaseActivity {
    public static final String ORDER_ID = "ORDER_ID";

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
    @BindView(R.id.order_num_tv)
    TextView orderNumTv;
    @BindView(R.id.fazhan_tv)
    TextView fazhanTv;
    @BindView(R.id.daozhan_tv)
    TextView daozhanTv;
    @BindView(R.id.pinming_tv)
    TextView pinmingTv;
    @BindView(R.id.baozhuang_tv)
    TextView baozhuangTv;
    @BindView(R.id.jianshu_tv)
    TextView jianshuTv;
    @BindView(R.id.zhongliang_tv)
    TextView zhongliangTv;
    @BindView(R.id.tiji_tv)
    TextView tijiTv;
    @BindView(R.id.consignee_tv)
    TextView consigneeTv;
    @BindView(R.id.consignee_num_tv)
    TextView consigneeNumTv;
    @BindView(R.id.receiving_tv)
    TextView receivingTv;
    @BindView(R.id.receiving_unit_tv)
    TextView receivingUnitTv;
    @BindView(R.id.consignor_tv)
    TextView consignorTv;
    @BindView(R.id.consignor_num_tv)
    TextView consignorNumTv;
    @BindView(R.id.consignor_unit_tv)
    TextView consignorUnitTv;
    @BindView(R.id.consignor_place_tv)
    TextView consignorPlaceTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.layout_sl)
    ScrollView layoutSl;
    private SweetAlertDialog loadingDialog;
    @BindView(R.id.sign_state_btn)
    Button signStateBtn;
    private String allTicketNo;
    @BindView(R.id.ysk_tv)
    TextView ysk_tv;
    private GetAppLoginResponse loginInfo;


    public static void startActivity(Activity activity, int requestCode, String orderId) {
        Intent intent = new Intent(activity, MyOrderDetailAcitivity.class);
        intent.putExtra(ORDER_ID, orderId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_order_detail);
        ButterKnife.bind(this);
        allTicketNo = getIntent().getStringExtra(ORDER_ID);
        loginInfo = MyApplication.getInstance().getLoginInfo();
        titltTv.setText("货单明细");
        contentLl.setVisibility(View.VISIBLE);

        if (loginInfo.DataValue.UserType_Oid.equals("E")) {
            rightTv.setText("物流跟踪");
        } else {
            rightTv.setText("更多");
        }

        getInfo();
    }


    private void getInfo() {
        showDialog();
        OrederDetailedRequest dataValue = new OrederDetailedRequest();
        if (TextUtils.isEmpty(allTicketNo)) {
            DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(MyOrderDetailAcitivity.this, "无效查询码!", new CustomDialogListener() {
                @Override
                public void onDialogClosed(int closeType) {
                    finish();
                }
            });
            return;
        } else {
            dataValue.All_Ticket_No = allTicketNo;
        }

        DataRequestBase dataRequestBase = new DataRequestBase();
        dataRequestBase.DataValue = dataValue;
        dataRequestBase.User_Key = loginInfo.DataValue.User_Key;
        dataRequestBase.UserType_Oid = loginInfo.DataValue.UserType_Oid;
        dataRequestBase.Token = loginInfo.DataValue.Token;
        dataRequestBase.Company_Oid = loginInfo.DataValue.Company_Oid;

        LogUtil.d("-->> respon = " + new Gson().toJson(dataRequestBase));
        VolleyManager.newInstance().PostJsonRequest(TMSService.OrederDetailed_TAG, TMSService.OrederDetailed_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtil.d("-->> respon = " + jsonObject.toString());
                OrederDetailedResponse info = GsonUtil.newInstance().fromJson(jsonObject, OrederDetailedResponse.class);
                Message msg = Message.obtain();
                if (info.Success) {
                    msg.obj = info;
                    msg.what = Constants.REQUEST_SUCC;
                } else {
                    msg.obj = info.ErrorText;
                    msg.what = Constants.REQUEST_FAIL;
                }
                myHandler.sendMessage(msg);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = Message.obtain();
                message.what = Constants.REQUEST_ERROR;
                myHandler.sendMessage(message);

            }
        });


    }

    private void showDialog() {
        if (loadingDialog == null) {
            loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        }
        loadingDialog.show();
    }

    private void dissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dissDialog();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    OrederDetailedResponse info = (OrederDetailedResponse) msg.obj;
                    if (info != null && info.DataValue != null && info.DataValue.size() > 0) {
                        setInfo(info.DataValue.get(0));
                        layoutSl.setVisibility(View.VISIBLE);
                    } else {
                        DialogUtils.showFailToast(Utils.getResourcesString(R.string.check_data_empty));
                    }

                    break;
                case Constants.REQUEST_FAIL:
                    String infoError = (String) msg.obj;
                    if (infoError != null) {
                        DialogUtils.showFailToast(infoError);
                    } else {
                        DialogUtils.showFailToast(Utils.getResourcesString(R.string.get_data_fail));
                    }
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showMessageDialogOfDefaultSingleBtn(MyOrderDetailAcitivity.this, Utils.getResourcesString(R.string.request_error));
                    break;
            }

        }

    };

    private void setInfo(mOrderDetail info) {
        orderNumTv.setText(info.Ticket_No);//货单号
        fazhanTv.setText(info.SendStation);
        daozhanTv.setText(info.ArrStation + " " + info.ArrStation_Area);
        pinmingTv.setText(info.Goods_Breed);
        baozhuangTv.setText(info.Goods_Casing);
        jianshuTv.setText(String.format("%1$s件", info.Goods_Num));
        zhongliangTv.setText(String.format("%1$skg", info.Bulk_Weight));

        consignorTv.setText(info.SendMan_Name);
        consignorNumTv.setText(info.SendMan_Tel);
        consignorUnitTv.setText(info.SendMan_Org);
        consignorPlaceTv.setText(info.SendMan_Address);

        consigneeTv.setText(info.ReceiveMan_Name);
        consigneeNumTv.setText(info.ReceiveMan_Tel);
        receivingTv.setText(info.ReceiveMan_Org);
        receivingUnitTv.setText(info.ReceiveMan_Address);


        signStateBtn.setText(info.Distribution_Status);
//        tijiTv.setText(String.format("%1$sm³", info.Goods_Bulk));
        SetViewValueUtil.setTextViewValue(tijiTv, info.Goods_Bulk, "m³");
        ysk_tv.setText(info.YSK_Fee);
    }

    @OnClick({R.id.back_llayout, R.id.content_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_llayout:
                finish();
                break;
            case R.id.content_ll:

                if (loginInfo.DataValue.UserType_Oid.equals("E")) {
                    DriverMyOrderLogisticsFollowActivity.startActivity(MyOrderDetailAcitivity.this, allTicketNo);
                } else {
                    //跳转到物流跟踪
                    OrderDetailPopWindow popWindow = new OrderDetailPopWindow(MyOrderDetailAcitivity.this);
                    popWindow.showLocation(R.id.content_ll);
                    popWindow.setOnItemClickListener(new OrderDetailPopWindow.OnItemClickListener() {
                        @Override
                        public void onClick(OrderDetailPopWindow.MENUITEM item, String str) {

                            if (str.equals("1")) {
                                DriverMyOrderLogisticsFollowActivity.startActivity(MyOrderDetailAcitivity.this, allTicketNo);
                            } else {
                                DriverExceptionInfoActivity.startActivity(MyOrderDetailAcitivity.this, allTicketNo);

                            }

                        }
                    });
                }


                break;
        }
    }
}
