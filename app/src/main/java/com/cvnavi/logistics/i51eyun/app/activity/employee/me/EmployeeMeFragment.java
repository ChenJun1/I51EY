package com.cvnavi.logistics.i51eyun.app.activity.employee.me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseFragment;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.activity.login.LoginActivity;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.response.DataResponseBase;
import com.cvnavi.logistics.i51eyun.app.config.LoginService;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.SharedPreferencesTool;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

public class EmployeeMeFragment extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.exit_app_btn)
    TextView exitAppBtn;

    public static EmployeeMeFragment instantiation() {
        return new EmployeeMeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_me, container, false);
        ButterKnife.bind(this, view);

        titleTv.setText(R.string.cargo_me_name);
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView()
                    .setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }


    @OnClick(R.id.exit_app_btn)
    public void onClick() {
        DataRequestBase dataRequestBase = new DataRequestBase();
        dataRequestBase.User_Key = SharedPreferencesTool.getString(SharedPreferencesTool.LOGIN_User_Key, null);

        VolleyManager.newInstance().PostJsonRequest(LoginService.ExitLogin_TAG, LoginService.ExitLogin_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->> response:");

                Message message = Message.obtain();
                DataResponseBase dataResponseBase = GsonUtil.newInstance().fromJson(response, DataResponseBase.class);
                if (dataResponseBase.Success) {
                    message.what = Constants.REQUEST_SUCC;

                } else {
                    message.what = Constants.REQUEST_FAIL;
                }

                handler.sendMessage(message);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = Message.obtain();
                message.what = Constants.REQUEST_FAIL;
                handler.sendMessage(message);
            }
        });


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    SharedPreferencesTool.putString(SharedPreferencesTool.LOGIN_Token, null);
                    skipActivity(getActivity(), LoginActivity.class);
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showFailToast(Utils.getResourcesString(R.string.exit_login_fail));
                    break;
            }

        }
    };
}
