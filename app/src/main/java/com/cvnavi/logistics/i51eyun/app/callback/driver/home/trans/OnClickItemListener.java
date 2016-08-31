package com.cvnavi.logistics.i51eyun.app.callback.driver.home.trans;

import com.cvnavi.logistics.i51eyun.app.bean.model.mCarShift;

/**
 * 版权所有势航网络
 * Created by ${chuzy} on 2016/7/7.
 */
public interface OnClickItemListener {
    public void onClick(int position);

    public void onLongClick(mCarShift info,int position);

}
