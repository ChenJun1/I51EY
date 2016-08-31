package com.cvnavi.logistics.i51eyun.app;


public class Constants {
    //引导页资源
    public static final int[] GuidResImgs = {R.drawable.guid_one, R.drawable.guid_two, R.drawable.guid_three};
    // 对话框对于屏幕的比例
    public static final float DIALOG_WIDTH_SCALE = 0.85f;

    public static final int REQUEST_SUCC = 0;
    public static final int REQUEST_FAIL = 1;
    public static final int REQUEST_ERROR = 2;
    public static final int DELETE_SUCC = 3;
    public static final int ADD_SUCC = 4;
    public static final int EDIT_SUCC = 5;


    //=====================回调常量========================
    public static final String CHOOSE_CAR = "CHOOSE_CAR";
    public static final String CHOOSE_MONITOR_CAR = "CHOOSE_MONITOR_CAR";
    public static final String CHOOSE_HISTOR_CAR = "CHOOSE_HISTOR_CAR";
    public static final String MyTASK_CHOICE_TIME = "MyTASK_CHOICE_TIME";

    //=====================页面传参常量=====================
    public static final String TODRIVERMONITORMAPACTIVITY = "TODRIVERMONITORMAPACTIVITY";
    public static final String EDIT_CAR_SCHEDULING = "EDIT_CAR_SCHEDULING";
    public static final String CAR_KEY = "CAR_KEY";
    public static final String OPERATE = "OPERATE";
    public static final String DriverList = "DriverList";
    public static final String DriverInfo = "DriverInfo";
    public static final String TASKINFO = "TASKINFO";
    public static final String DRIVER_LINE_LOOK_PIC = "DRIVER_LINE_LOOK_PIC";
    public static final String TaskDetailedOrder = "TaskDetailedOrder";
    public static final String Letter_Oid = "Letter_Oid";
    public static final String TaskBean = "TaskBean";
    public static final String PictureBeanList = "PictureBeanList";
    public static final String POSITION = "POSITION";
    public static final String All_Ticket_No = "All_Ticket_No";
    public static final String ExceptionInfoBean = "ExceptionInfoBean";
    public static final String LogisticsFollowNoteBean = "LogisticsFollowNoteBean";
    public static final String DriverStowageStatisticsListActivity = "DriverStowageStatisticsActivity";
    public static final String MyTaskExceptionInfoActivity = "MyTaskExceptionInfoActivity";
    public static final String HOME = "HOME";


    public static final String STOWAGELISt = "STOWAGELISt";


    //==================首页菜单SeriviceType===============
    public static final int HOME_SERVICE_TYPE_BUSINISS = 1;//业务
    public static final int HOME_SERVICE_TYPE_LOCATION = 2;//定位


    // =================首页菜单SeivrceID===================
    public static final int HOME_LOCATION_JK = 0;//车辆监控
    public static final int HOME_LOCATION_GJ = 1;//车辆轨迹
    public static final int HOME_LOCATION_YS = 2;//调度运输
    public static final int HOME_LOCATION_BJ = 3;//车辆报警
    public static final int HOME_LOCATION_CL = 4;//车辆管理
    public static final int HOME_LOCATION_CLT = 5;//车辆统计
    public static final int HOME_LOCATION_YC = 6;//车辆异常
    public static final int HOME_BUSINISS_HD = 7;//货单查询
    public static final int HOME_BUSINISS_TJ = 8;//统计
    public static final int HOME_BUSINISS_SJG = 9;//司机管理
    public static final int HOME_BUSINISS_RW = 10;//任务
    public static final int HOME_BUSINISS_HDE = 11;//我的货单
    public static final int HOME_BUSINISS_CD = 16;//我的车队
    // =================权限菜单列表===================
    public static final String OPERATE_CD_DELE = "18";//删除
    public static final String OPERATE_CD_MODIF = "19";//修改
    public static final String OPERATE_CD_ADD = "20";//增加
    public static final String OPERATE_CD_EXCP_UP = "21";//异常上传
    public static final String OPERATE_CD_CONFIRM_SIGN = "22";//确认签收
    public static final String OPERATE_CD_CONFIRM_ARRVIED = "23";//确认到车
    public static final String OPERATE_CD_CONFIRM_TI_HUO = "24";//确认提货
    public static final String OPERATE_CD_EDIT_DRIVER = "25";//编辑司机
    public static final String OPERATE_CD_ADD_DRIVER = "26";//添加司机
    public static final String OPERATE_CD_DELEC_DRIVER = "27";//删除司机

    public static final int BANNER_TIME = 1 * 1000;//1秒
    // =================登录帮助Url===================
    public static String HelpManual_URL = "http://app.eh-56.com/help.html";
//    public static String HelpManual_URL = "http://10.10.11.100:8090/help.html";

    //====================分页常量=========================


//    public static ArrayList<String> getDrivingLicenseType() {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("A1");
//        list.add("A2");
//        list.add("B1");
//        list.add("B2");
//        list.add("C1");
//        list.add("C2");
//
//        return list;
//    }

}
