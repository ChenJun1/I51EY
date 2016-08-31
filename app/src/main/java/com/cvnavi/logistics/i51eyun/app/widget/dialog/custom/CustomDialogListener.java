package com.cvnavi.logistics.i51eyun.app.widget.dialog.custom;

/**
 * 
 *
 */
public interface CustomDialogListener {
	/**
	 * 确定按钮
	 */
	public final static int BUTTON_OK = 1;
	// /**
	// * 中立的
	// */
	// public final static int BUTTON_NEGATIVE = 2;
	/**
	 * 取消
	 */
	public final static int BUTTON_NO = 3;

	/**
	 *
	 * @param closeType
     */
	public void onDialogClosed(int closeType);
}
