package com.cvnavi.logistics.i51eyun.app.activity.driver.home.transportation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.driver.adapter.home.trans.ChoiceLineListViewAdapter;
import com.cvnavi.logistics.i51eyun.app.bean.model.mLineInfo;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.response.GetLineListResponse;
import com.cvnavi.logistics.i51eyun.app.callback.manager.LineChoiceCallBackManager;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.JsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * 版权所有势航网络
 * Created by ${ChenJ} on 2016/7/25.
 */
public class ChoiceLineActivity extends BaseActivity {
    private static String TAG = ChoiceLineActivity.class.getName();

    @BindView(R.id.back_llayout)
    LinearLayout backLlayout;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.choice_line_lv)
    PullToRefreshListView choiceLineLv;

    private ListView actualListView;
    private ChoiceLineListViewAdapter adapter;
    private List<mLineInfo> dataList;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_line);
        ButterKnife.bind(this);

        mContext = this;
        titleTv.setText(R.string.choice_line);

        dataList = new ArrayList<>();
        adapter = new ChoiceLineListViewAdapter(dataList, this);
        choiceLineLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });

        actualListView = choiceLineLv.getRefreshableView();
        actualListView.setAdapter(adapter);
        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLineInfo lineInfo = dataList.get(position - 1);
                LineChoiceCallBackManager.newInstance().fire(lineInfo);
                ChoiceLineActivity.this.finish();
            }
        });

        loadData();
    }

    private void loadData() {
//        GetLineListRequest lineListRequest = new GetLineListRequest();
//        lineListRequest.Line_Name = "";

        DataRequestBase dataRequestBase = new DataRequestBase();
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;

        dataRequestBase.DataValue = ""; //JsonUtils.toJsonData(lineListRequest);

        VolleyManager.newInstance().PostJsonRequest(TAG, TMSService.GetLineList_Request_Url,
                GsonUtil.newInstance().toJson(dataRequestBase),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GetLineListResponse responseBase = JsonUtils.parseData(response.toString(), GetLineListResponse.class);

                        Message msg = Message.obtain();
                        if (responseBase.Success) {
                            msg.obj = responseBase.DataValue;
                            msg.what = Constants.REQUEST_SUCC;
                            myHandler.sendMessage(msg);
                        } else {
                            msg.obj = responseBase.ErrorText;
                            msg.what = Constants.REQUEST_FAIL;
                            myHandler.sendMessage(msg);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message msg = Message.obtain();
                        msg.obj = error.getMessage();
                        msg.what = Constants.REQUEST_FAIL;
                        myHandler.sendMessage(msg);
                    }
                });
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    if (msg.obj != null) {
                        dataList.clear();
                        List<mLineInfo> list = (List<mLineInfo>) msg.obj;
                        dataList.addAll(list);

                        adapter.notifyDataSetChanged();
                    }
                    choiceLineLv.onRefreshComplete();
                    break;
                case Constants.REQUEST_FAIL:
                    if (String.valueOf(msg.obj).equals("null") || TextUtils.isEmpty(String.valueOf(msg.obj))) {
                        DialogUtils.showFailToast(getResources().getString(R.string.search_fail));
                    } else {
                        DialogUtils.showFailToast(String.valueOf(msg.obj));
                    }
                    break;
            }
        }
    };

    @OnClick(R.id.back_llayout)
    public void onClick() {
        ChoiceLineActivity.this.finish();
    }
}
