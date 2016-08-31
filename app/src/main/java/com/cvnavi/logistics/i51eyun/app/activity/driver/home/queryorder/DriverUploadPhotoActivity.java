package com.cvnavi.logistics.i51eyun.app.activity.driver.home.queryorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cvnavi.logistics.i51eyun.app.BaseActivity;
import com.cvnavi.logistics.i51eyun.app.Constants;
import com.cvnavi.logistics.i51eyun.app.MyApplication;
import com.cvnavi.logistics.i51eyun.app.R;
import com.cvnavi.logistics.i51eyun.app.bean.model.ImageInfo;
import com.cvnavi.logistics.i51eyun.app.bean.request.DataRequestBase;
import com.cvnavi.logistics.i51eyun.app.bean.request.OrderAbnormalPhotoRequest;
import com.cvnavi.logistics.i51eyun.app.bean.request.UpLoadImageRequest;
import com.cvnavi.logistics.i51eyun.app.bean.response.DataResponseBase;
import com.cvnavi.logistics.i51eyun.app.config.TMSService;
import com.cvnavi.logistics.i51eyun.app.utils.BitmapUtil;
import com.cvnavi.logistics.i51eyun.app.utils.DialogUtils;
import com.cvnavi.logistics.i51eyun.app.utils.GsonUtil;
import com.cvnavi.logistics.i51eyun.app.utils.LogUtil;
import com.cvnavi.logistics.i51eyun.app.utils.Utils;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.SweetAlert.SweetAlertDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.ActionSheetDialog;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.ActionSheetItemInfo;
import com.cvnavi.logistics.i51eyun.app.widget.dialog.custom.CustomDialogListener;
import com.google.gson.Gson;
import com.king.photo.activity.AlbumActivity;
import com.king.photo.activity.GalleryActivity;
import com.king.photo.util.Bimp;
import com.king.photo.util.FileUtils;
import com.king.photo.util.ImageItem;
import com.king.photo.util.PublicWay;
import com.king.photo.util.Res;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import volley.VolleyManager;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/25.
 * 上传照片(货单异常)
 */
public class DriverUploadPhotoActivity extends BaseActivity {

    public final static String All_Ticket_No = "All_Ticket_No";
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
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.noScrollgridview)
    GridView noScrollgridview;
    @BindView(R.id.select_tv)
    TextView selectTv;
    @BindView(R.id.ok_btn)
    TextView okBtn;
    @BindView(R.id.type_rl)
    RelativeLayout typeRl;
    @BindView(R.id.note_et)
    EditText noteEt;
    @BindView(R.id.arrow_tv)
    TextView arrowTv;
    private GridAdapter adapter;
    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    private String selectType = "提货";
    private String Exception_Mode_Oid;
    private String allTicke;

    private SweetAlertDialog loading;


    public static void startActivity(Activity activity, String allTickNo) {
        Intent intent = new Intent(activity, DriverUploadPhotoActivity.class);
        intent.putExtra(All_Ticket_No, allTickNo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.activity_driver_upload_photo, null);
        setContentView(R.layout.activity_driver_upload_photo);
        ButterKnife.bind(this);
        allTicke = getIntent().getStringExtra(All_Ticket_No);
        if (TextUtils.isEmpty(allTicke)) {
            return;
        }
        titltTv.setText("异常上报");
        Res.init(this);
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
        PublicWay.activityList.add(this);

        //初始化图片上传 的方法
        showBottomWinDow();
        event();

    }

    private void event() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(noteEt.getText().toString())) {
                    DialogUtils.showWarningToast("请填写拍照记事！");
                    return;
                }

                if (Bimp.tempSelectBitmap.size() == 0) {
                    DialogUtils.showWarningToast("请上传照片！");
                    return;
                }

                if (TextUtils.isEmpty(selectTv.getText().toString())) {
                    DialogUtils.showWarningToast("请选择异常类型");
                    return;
                }
                upLoad();
            }
        });

        backLlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void upLoad() {
        loading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        loading.show();
        OrderAbnormalPhotoRequest request = new OrderAbnormalPhotoRequest();
        request.All_Ticket_No = allTicke;
        request.Exception_Mode_Oid = Exception_Mode_Oid;
        request.TrackRecord_Note = noteEt.getText().toString();

        List<ImageInfo> IMGList = new ArrayList<ImageInfo>();
        for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
            IMGList.add(new ImageInfo(allTicke, null, allTicke + "-" + new Random().nextInt(100) + ".png", ".png", BitmapUtil.encodeToBase64(Bimp.tempSelectBitmap.get(i).getBitmap(), Bitmap.CompressFormat.PNG, 100 / 100)));
        }
        request.IMGList = IMGList;
        UpLoadImageRequest dataRequestBase = new UpLoadImageRequest();
        dataRequestBase.DataValue = request;
        dataRequestBase.User_Key = MyApplication.getInstance().getLoginInfo().DataValue.User_Key;
        dataRequestBase.UserType_Oid = MyApplication.getInstance().getLoginInfo().DataValue.UserType_Oid;
        dataRequestBase.Company_Oid = MyApplication.getInstance().getLoginInfo().DataValue.Company_Oid;
        dataRequestBase.Token = MyApplication.getInstance().getLoginInfo().DataValue.Token;
        dataRequestBase.Org_Code = MyApplication.getInstance().getLoginInfo().DataValue.Org_Code;
        dataRequestBase.User_Name = MyApplication.getInstance().getLoginInfo().DataValue.User_Name;
        LogUtil.d("-->>request = " + new Gson().toJson(dataRequestBase));

        VolleyManager.newInstance().PostJsonRequest(TMSService.OrderAbnormalPhoto_TAG, TMSService.OrderAbnormalPhoto_Request_Url, GsonUtil.newInstance().toJson(dataRequestBase), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.d("-->>respo = " + response.toString());
                DataResponseBase dataResponseBase = GsonUtil.newInstance().fromJson(response, DataResponseBase.class);
                Message msg = Message.obtain();
                if (dataResponseBase != null) {
                    if (dataResponseBase.Success) {
                        msg.what = Constants.REQUEST_SUCC;
                    } else {
                        msg.what = Constants.REQUEST_FAIL;
                    }
                } else {
                    msg.what = Constants.REQUEST_ERROR;
                }
                myHandler.sendMessage(msg);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message msg = Message.obtain();
                msg.what = Constants.REQUEST_ERROR;
                myHandler.sendMessage(msg);

            }
        });


    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loading.dismiss();
            switch (msg.what) {
                case Constants.REQUEST_SUCC:
                    DialogUtils.showMessageDialogOfDefaultSingleBtnNoCancel(DriverUploadPhotoActivity.this, "上传成功！", new CustomDialogListener() {
                        @Override
                        public void onDialogClosed(int closeType) {
                            finish();
                        }
                    });
                    break;
                case Constants.REQUEST_FAIL:
                    DialogUtils.showFailToast("上传失败！");
                    break;
                case Constants.REQUEST_ERROR:
                    DialogUtils.showFailToast(Utils.getResourcesString(R.string.request_error));
                    break;
            }

        }
    };


    @OnClick(R.id.type_rl)
    public void onClick() {

        ArrayList<ActionSheetItemInfo> list = new ArrayList<ActionSheetItemInfo>();
//        list.add(new ActionSheetItemInfo("提货", new ActionSheetDialog.OnSheetItemClickListener() {
//            @Override
//            public void onClick(int which) {
//                selectType = "提货";
//                Exception_Mode_Oid = "HH";
//                selectTv.setText(selectType);
//
//            }
//        }));
//        list.add(new ActionSheetItemInfo("装车",
//                new ActionSheetDialog.OnSheetItemClickListener() {
//                    @Override
//                    public void onClick(int which) {
//                        selectType = "装车";
//                        Exception_Mode_Oid = "HJ";
//                        selectTv.setText(selectType);
//                    }
//                }));
//        list.add(new ActionSheetItemInfo("到达",
//                new ActionSheetDialog.OnSheetItemClickListener() {
//                    @Override
//                    public void onClick(int which) {
//                        selectType = "到达";
//                        Exception_Mode_Oid = "HI";
//                        selectTv.setText(selectType);
//                    }
//                }));
        list.add(new ActionSheetItemInfo("缺件", new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                selectType = "缺件";
                Exception_Mode_Oid = "HK";
                selectTv.setText(selectType);
            }
        }));
        list.add(new ActionSheetItemInfo("货物破损", new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                selectType = "货物破损";
                Exception_Mode_Oid = "HA";
                selectTv.setText(selectType);
            }
        }));
        list.add(new ActionSheetItemInfo("签收异常", new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                selectType = "签收异常";
                Exception_Mode_Oid = "HC";
                selectTv.setText(selectType);
            }
        }));


        DialogUtils.showActionSheetDialog(this, "请选择拍照类型", list);
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 3) {
                return 3;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                holder.bt = (Button) convertView.findViewById(R.id.item_grida_bt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
                holder.bt.setVisibility(View.GONE);
                if (position == 3) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
                holder.bt.setVisibility(View.VISIBLE);
                holder.bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bimp.tempSelectBitmap.remove(position);
                        Bimp.max--;
                        notifyDataSetChanged();

                    }
                });
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
            public Button bt;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }


    /**
     *
     */
    public void showBottomWinDow() {
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
//        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    new ActionSheetDialog(DriverUploadPhotoActivity.this).builder().setTitle("图片类型").setCancelable(false).setCanceledOnTouchOutside(false).addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            photo();
                        }
                    }).addSheetItem("从相册中选取", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            Intent intent = new Intent(DriverUploadPhotoActivity
                                    .this, AlbumActivity.class);
                            startActivity(intent);
                        }
                    }).show();


                } else {
                    Intent intent = new Intent(DriverUploadPhotoActivity.this, GalleryActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });


    }


//    /**
//     * 初始化图片上传的方法
//     */
//    public void Init() {
//
//        pop = new PopupWindow(DriverUploadPhotoActivity.this);
//
//        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
//
//        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
//
//        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        pop.setBackgroundDrawable(new BitmapDrawable());
//        pop.setFocusable(true);
//        pop.setOutsideTouchable(true);
//        pop.setContentView(view);
//
//        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
//        Button bt1 = (Button) view
//                .findViewById(R.id.item_popupwindows_camera);
//        Button bt2 = (Button) view
//                .findViewById(R.id.item_popupwindows_Photo);
//        Button bt3 = (Button) view
//                .findViewById(R.id.item_popupwindows_cancel);
//        parent.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                pop.dismiss();
//                ll_popup.clearAnimation();
//            }
//        });
//        bt1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                photo();
//                pop.dismiss();
//                ll_popup.clearAnimation();
//            }
//        });
//        bt2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(DriverUploadPhotoActivity.this,
//                        AlbumActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.activity_translate_in, R.anim
// .activity_translate_out);
//                pop.dismiss();
//                ll_popup.clearAnimation();
//            }
//        });
//        bt3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                pop.dismiss();
//                ll_popup.clearAnimation();
//            }
//        });
//
//        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
//        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        adapter = new GridAdapter(this);
//        adapter.update();
//        noScrollgridview.setAdapter(adapter);
//        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                if (arg2 == Bimp.tempSelectBitmap.size()) {
//                    Log.i("ddddddd", "----------");
//                    ll_popup.startAnimation(AnimationUtils.loadAnimation
// (DriverUploadPhotoActivity.this, R.anim.activity_translate_in));
//                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
//                } else {
//                    Intent intent = new Intent(DriverUploadPhotoActivity.this,
//                            GalleryActivity.class);
//                    intent.putExtra("position", "1");
//                    intent.putExtra("ID", arg2);
//                    startActivity(intent);
//                }
//            }
//        });
//
//    }


    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    private static final int TAKE_PICTURE = 0x000001;

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 3 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

    /**
     * 点击空白处，软键盘消失
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
        Bimp.max = 0;


    }
}
