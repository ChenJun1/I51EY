package com.cvnavi.logistics.i51eyun.app.bean.response;

import com.cvnavi.logistics.i51eyun.app.bean.model.MyTask.TaskBean;

import java.util.List;

/**
 * 版权所有势航网络
 * Created by Chuzy on 2016/8/9.
 */
public class GetCarGPSAggregateResponse extends DataResponseBase {

    private DataValueBean DataValue;

    public DataValueBean getDataValue() {
        return DataValue;
    }

    public void setDataValue(DataValueBean DataValue) {
        this.DataValue = DataValue;
    }

    public static class DataValueBean {
        private CarCodeScheduleBean CarCode_Schedule;
        private CarGPSInfoBean CarGPSInfo;
        private Object CarLineNode;
        private TaskBean Letter_List;
        private CarMinleagesBean CarMinleages;


        public TaskBean getLetter_List() {
            return Letter_List;
        }

        public void setLetter_List(TaskBean letter_List) {
            Letter_List = letter_List;
        }

        public CarCodeScheduleBean getCarCode_Schedule() {
            return CarCode_Schedule;
        }

        public void setCarCode_Schedule(CarCodeScheduleBean CarCode_Schedule) {
            this.CarCode_Schedule = CarCode_Schedule;
        }

        public CarGPSInfoBean getCarGPSInfo() {
            return CarGPSInfo;
        }

        public void setCarGPSInfo(CarGPSInfoBean CarGPSInfo) {
            this.CarGPSInfo = CarGPSInfo;
        }

        public Object getCarLineNode() {
            return CarLineNode;
        }

        public void setCarLineNode(Object CarLineNode) {
            this.CarLineNode = CarLineNode;
        }


        public CarMinleagesBean getCarMinleages() {
            return CarMinleages;
        }

        public void setCarMinleages(CarMinleagesBean CarMinleages) {
            this.CarMinleages = CarMinleages;
        }

        public static class CarCodeScheduleBean {
            private String CarCode_No;
            private String BeginDate;
            private String EndDate;
            private String Serial_Oid;
            private String CarCodeSerial_Oid;
            private String CarCode_Date;
            private String Line_Oid;
            private String Forecast_Leave_DateTime;
            private String CarCode_NO;
            private String MainDriverSerial_Oid;
            private String SecondDriverSerial_Oid;
            private String CarSchedule_Note;
            private String Operate_Name;
            private String Operate_Org;
            private String Operate_DateTime;
            private String Driver_Tel;
            private String CarCode;
            private String Line_Type;
            private String Line_Name;
            private String Traffic_Mode;
            private String Schedule_Status;
            private String Leave_DateTime;
            private String MainDriver;
            private String SecondDriver;
            private String LPSCarCode_Key;
            private String BoxCarCode_Key;//挂车Key

            private String BoxCarCode = null;//挂车号

            public String getBoxCarCode_Key() {
                return BoxCarCode_Key;
            }

            public void setBoxCarCode_Key(String boxCarCode_Key) {
                BoxCarCode_Key = boxCarCode_Key;
            }

            public String getBoxCarCode() {
                return BoxCarCode;
            }

            public void setBoxCarCode(String boxCarCode) {
                BoxCarCode = boxCarCode;
            }

            public String getCarCode_No() {
                return CarCode_No;
            }

            public void setCarCode_No(String CarCode_No) {
                this.CarCode_No = CarCode_No;
            }

            public String getBeginDate() {
                return BeginDate;
            }

            public void setBeginDate(String BeginDate) {
                this.BeginDate = BeginDate;
            }

            public String getEndDate() {
                return EndDate;
            }

            public void setEndDate(String EndDate) {
                this.EndDate = EndDate;
            }

            public String getSerial_Oid() {
                return Serial_Oid;
            }

            public void setSerial_Oid(String Serial_Oid) {
                this.Serial_Oid = Serial_Oid;
            }

            public String getCarCodeSerial_Oid() {
                return CarCodeSerial_Oid;
            }

            public void setCarCodeSerial_Oid(String CarCodeSerial_Oid) {
                this.CarCodeSerial_Oid = CarCodeSerial_Oid;
            }

            public String getCarCode_Date() {
                return CarCode_Date;
            }

            public void setCarCode_Date(String CarCode_Date) {
                this.CarCode_Date = CarCode_Date;
            }

            public String getLine_Oid() {
                return Line_Oid;
            }

            public void setLine_Oid(String Line_Oid) {
                this.Line_Oid = Line_Oid;
            }

            public String getForecast_Leave_DateTime() {
                return Forecast_Leave_DateTime;
            }

            public void setForecast_Leave_DateTime(String Forecast_Leave_DateTime) {
                this.Forecast_Leave_DateTime = Forecast_Leave_DateTime;
            }

            public String getCarCode_NO() {
                return CarCode_NO;
            }

            public void setCarCode_NO(String CarCode_NO) {
                this.CarCode_NO = CarCode_NO;
            }

            public String getMainDriverSerial_Oid() {
                return MainDriverSerial_Oid;
            }

            public void setMainDriverSerial_Oid(String MainDriverSerial_Oid) {
                this.MainDriverSerial_Oid = MainDriverSerial_Oid;
            }

            public String getSecondDriverSerial_Oid() {
                return SecondDriverSerial_Oid;
            }

            public void setSecondDriverSerial_Oid(String SecondDriverSerial_Oid) {
                this.SecondDriverSerial_Oid = SecondDriverSerial_Oid;
            }

            public String getCarSchedule_Note() {
                return CarSchedule_Note;
            }

            public void setCarSchedule_Note(String CarSchedule_Note) {
                this.CarSchedule_Note = CarSchedule_Note;
            }

            public String getOperate_Name() {
                return Operate_Name;
            }

            public void setOperate_Name(String Operate_Name) {
                this.Operate_Name = Operate_Name;
            }

            public String getOperate_Org() {
                return Operate_Org;
            }

            public void setOperate_Org(String Operate_Org) {
                this.Operate_Org = Operate_Org;
            }

            public String getOperate_DateTime() {
                return Operate_DateTime;
            }

            public void setOperate_DateTime(String Operate_DateTime) {
                this.Operate_DateTime = Operate_DateTime;
            }

            public String getDriver_Tel() {
                return Driver_Tel;
            }

            public void setDriver_Tel(String Driver_Tel) {
                this.Driver_Tel = Driver_Tel;
            }

            public String getCarCode() {
                return CarCode;
            }

            public void setCarCode(String CarCode) {
                this.CarCode = CarCode;
            }

            public String getLine_Type() {
                return Line_Type;
            }

            public void setLine_Type(String Line_Type) {
                this.Line_Type = Line_Type;
            }

            public String getLine_Name() {
                return Line_Name;
            }

            public void setLine_Name(String Line_Name) {
                this.Line_Name = Line_Name;
            }

            public String getTraffic_Mode() {
                return Traffic_Mode;
            }

            public void setTraffic_Mode(String Traffic_Mode) {
                this.Traffic_Mode = Traffic_Mode;
            }

            public String getSchedule_Status() {
                return Schedule_Status;
            }

            public void setSchedule_Status(String Schedule_Status) {
                this.Schedule_Status = Schedule_Status;
            }

            public String getLeave_DateTime() {
                return Leave_DateTime;
            }

            public void setLeave_DateTime(String Leave_DateTime) {
                this.Leave_DateTime = Leave_DateTime;
            }

            public String getMainDriver() {
                return MainDriver;
            }

            public void setMainDriver(String MainDriver) {
                this.MainDriver = MainDriver;
            }

            public String getSecondDriver() {
                return SecondDriver;
            }

            public void setSecondDriver(String SecondDriver) {
                this.SecondDriver = SecondDriver;
            }

            public String getLPSCarCode_Key() {
                return LPSCarCode_Key;
            }

            public void setLPSCarCode_Key(String LPSCarCode_Key) {
                this.LPSCarCode_Key = LPSCarCode_Key;
            }
        }

        public static class CarGPSInfoBean {
            private String CarCode;
            private String CHS_Provice;
            private String CHS_City;
            private String CHS_Address;
            private String Driver;
            private String Driver_Tel;
            private String Ticket_No;
            private String BLng;
            private String BLat;
            private String GLng;
            private String GLat;
            private String Status;
            private String Serial_Oid;
            private String Speed;
            private String Mileage;
            private String StartAddress;
            private String CarStartTime;
            private String CarArrivaTime;
            private String CarleaveTime;
            private String DestinationAddress;
            private String CarCode_Key;

            public String getCarCode_Key() {
                return CarCode_Key;
            }

            public void setCarCode_Key(String carCode_Key) {
                CarCode_Key = carCode_Key;
            }

            public String getCarCode() {
                return CarCode;
            }

            public void setCarCode(String CarCode) {
                this.CarCode = CarCode;
            }

            public String getCHS_Provice() {
                return CHS_Provice;
            }

            public void setCHS_Provice(String CHS_Provice) {
                this.CHS_Provice = CHS_Provice;
            }

            public String getCHS_City() {
                return CHS_City;
            }

            public void setCHS_City(String CHS_City) {
                this.CHS_City = CHS_City;
            }

            public String getCHS_Address() {
                return CHS_Address;
            }

            public void setCHS_Address(String CHS_Address) {
                this.CHS_Address = CHS_Address;
            }

            public String getDriver() {
                return Driver;
            }

            public void setDriver(String Driver) {
                this.Driver = Driver;
            }

            public String getDriver_Tel() {
                return Driver_Tel;
            }

            public void setDriver_Tel(String Driver_Tel) {
                this.Driver_Tel = Driver_Tel;
            }

            public String getTicket_No() {
                return Ticket_No;
            }

            public void setTicket_No(String Ticket_No) {
                this.Ticket_No = Ticket_No;
            }

            public String getBLng() {
                return BLng;
            }

            public void setBLng(String BLng) {
                this.BLng = BLng;
            }

            public String getBLat() {
                return BLat;
            }

            public void setBLat(String BLat) {
                this.BLat = BLat;
            }

            public String getGLng() {
                return GLng;
            }

            public void setGLng(String GLng) {
                this.GLng = GLng;
            }

            public String getGLat() {
                return GLat;
            }

            public void setGLat(String GLat) {
                this.GLat = GLat;
            }

            public String getStatus() {
                return Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }

            public String getSerial_Oid() {
                return Serial_Oid;
            }

            public void setSerial_Oid(String Serial_Oid) {
                this.Serial_Oid = Serial_Oid;
            }

            public String getSpeed() {
                return Speed;
            }

            public void setSpeed(String Speed) {
                this.Speed = Speed;
            }

            public String getMileage() {
                return Mileage;
            }

            public void setMileage(String Mileage) {
                this.Mileage = Mileage;
            }

            public String getStartAddress() {
                return StartAddress;
            }

            public void setStartAddress(String StartAddress) {
                this.StartAddress = StartAddress;
            }

            public String getCarStartTime() {
                return CarStartTime;
            }

            public void setCarStartTime(String CarStartTime) {
                this.CarStartTime = CarStartTime;
            }

            public String getCarArrivaTime() {
                return CarArrivaTime;
            }

            public void setCarArrivaTime(String CarArrivaTime) {
                this.CarArrivaTime = CarArrivaTime;
            }

            public String getCarleaveTime() {
                return CarleaveTime;
            }

            public void setCarleaveTime(String CarleaveTime) {
                this.CarleaveTime = CarleaveTime;
            }

            public String getDestinationAddress() {
                return DestinationAddress;
            }

            public void setDestinationAddress(String DestinationAddress) {
                this.DestinationAddress = DestinationAddress;
            }
        }

        public static class LetterListBean {
            private String Num;
            private String Letter_Oid;
            private String Ticket_Count;
            private String Goods_Num;
            private String Goods_Weight;
            private String Bulk_Weight;
            private String Letter_Type;
            private String Letter_Type_Oid;
            private String TicketStatus;
            private String Letter_Date;
            private String Line_Oid;
            private String Line_Name;
            private String Line_Type;
            private String Traffic_Mode;
            private String AllReceivableAccount;
            private String Shuttle_Fee;
            private String Ticket_Fee;
            private String Profit_Fee;
            private String Prepayments;
            private String CarCode_No;
            private String ZX_Fee;
            private String Settlement_Fee;
            private String Pay_Type_Oid;
            private String Pay_Type;
            private String DriverSerial_Oid;
            private String Driver;
            private String Driver_Tel;
            private String CarCodeSerial_Oid;
            private String CarCode;
            private String GPS_Key;
            private String GPS;
            private String BoxCarCode;
            private String BoxGPS_Key;
            private String Operate_Name;
            private String Operate_Org;
            private String Operate_DateTime;
            private String IsAddLetter;
            private String Transaction_Status_Oid;
            private String fullCar_Destination;
            private String BLat;
            private String BLng;
            private String Transaction_Status;

            public String getNum() {
                return Num;
            }

            public void setNum(String Num) {
                this.Num = Num;
            }

            public String getLetter_Oid() {
                return Letter_Oid;
            }

            public void setLetter_Oid(String Letter_Oid) {
                this.Letter_Oid = Letter_Oid;
            }

            public String getTicket_Count() {
                return Ticket_Count;
            }

            public void setTicket_Count(String Ticket_Count) {
                this.Ticket_Count = Ticket_Count;
            }

            public String getGoods_Num() {
                return Goods_Num;
            }

            public void setGoods_Num(String Goods_Num) {
                this.Goods_Num = Goods_Num;
            }

            public String getGoods_Weight() {
                return Goods_Weight;
            }

            public void setGoods_Weight(String Goods_Weight) {
                this.Goods_Weight = Goods_Weight;
            }

            public String getBulk_Weight() {
                return Bulk_Weight;
            }

            public void setBulk_Weight(String Bulk_Weight) {
                this.Bulk_Weight = Bulk_Weight;
            }

            public String getLetter_Type() {
                return Letter_Type;
            }

            public void setLetter_Type(String Letter_Type) {
                this.Letter_Type = Letter_Type;
            }

            public String getLetter_Type_Oid() {
                return Letter_Type_Oid;
            }

            public void setLetter_Type_Oid(String Letter_Type_Oid) {
                this.Letter_Type_Oid = Letter_Type_Oid;
            }

            public String getTicketStatus() {
                return TicketStatus;
            }

            public void setTicketStatus(String TicketStatus) {
                this.TicketStatus = TicketStatus;
            }

            public String getLetter_Date() {
                return Letter_Date;
            }

            public void setLetter_Date(String Letter_Date) {
                this.Letter_Date = Letter_Date;
            }

            public String getLine_Oid() {
                return Line_Oid;
            }

            public void setLine_Oid(String Line_Oid) {
                this.Line_Oid = Line_Oid;
            }

            public String getLine_Name() {
                return Line_Name;
            }

            public void setLine_Name(String Line_Name) {
                this.Line_Name = Line_Name;
            }

            public String getLine_Type() {
                return Line_Type;
            }

            public void setLine_Type(String Line_Type) {
                this.Line_Type = Line_Type;
            }

            public String getTraffic_Mode() {
                return Traffic_Mode;
            }

            public void setTraffic_Mode(String Traffic_Mode) {
                this.Traffic_Mode = Traffic_Mode;
            }

            public String getAllReceivableAccount() {
                return AllReceivableAccount;
            }

            public void setAllReceivableAccount(String AllReceivableAccount) {
                this.AllReceivableAccount = AllReceivableAccount;
            }

            public String getShuttle_Fee() {
                return Shuttle_Fee;
            }

            public void setShuttle_Fee(String Shuttle_Fee) {
                this.Shuttle_Fee = Shuttle_Fee;
            }

            public String getTicket_Fee() {
                return Ticket_Fee;
            }

            public void setTicket_Fee(String Ticket_Fee) {
                this.Ticket_Fee = Ticket_Fee;
            }

            public String getProfit_Fee() {
                return Profit_Fee;
            }

            public void setProfit_Fee(String Profit_Fee) {
                this.Profit_Fee = Profit_Fee;
            }

            public String getPrepayments() {
                return Prepayments;
            }

            public void setPrepayments(String Prepayments) {
                this.Prepayments = Prepayments;
            }

            public String getCarCode_No() {
                return CarCode_No;
            }

            public void setCarCode_No(String CarCode_No) {
                this.CarCode_No = CarCode_No;
            }

            public String getZX_Fee() {
                return ZX_Fee;
            }

            public void setZX_Fee(String ZX_Fee) {
                this.ZX_Fee = ZX_Fee;
            }

            public String getSettlement_Fee() {
                return Settlement_Fee;
            }

            public void setSettlement_Fee(String Settlement_Fee) {
                this.Settlement_Fee = Settlement_Fee;
            }

            public String getPay_Type_Oid() {
                return Pay_Type_Oid;
            }

            public void setPay_Type_Oid(String Pay_Type_Oid) {
                this.Pay_Type_Oid = Pay_Type_Oid;
            }

            public String getPay_Type() {
                return Pay_Type;
            }

            public void setPay_Type(String Pay_Type) {
                this.Pay_Type = Pay_Type;
            }

            public String getDriverSerial_Oid() {
                return DriverSerial_Oid;
            }

            public void setDriverSerial_Oid(String DriverSerial_Oid) {
                this.DriverSerial_Oid = DriverSerial_Oid;
            }

            public String getDriver() {
                return Driver;
            }

            public void setDriver(String Driver) {
                this.Driver = Driver;
            }

            public String getDriver_Tel() {
                return Driver_Tel;
            }

            public void setDriver_Tel(String Driver_Tel) {
                this.Driver_Tel = Driver_Tel;
            }

            public String getCarCodeSerial_Oid() {
                return CarCodeSerial_Oid;
            }

            public void setCarCodeSerial_Oid(String CarCodeSerial_Oid) {
                this.CarCodeSerial_Oid = CarCodeSerial_Oid;
            }

            public String getCarCode() {
                return CarCode;
            }

            public void setCarCode(String CarCode) {
                this.CarCode = CarCode;
            }

            public String getGPS_Key() {
                return GPS_Key;
            }

            public void setGPS_Key(String GPS_Key) {
                this.GPS_Key = GPS_Key;
            }

            public String getGPS() {
                return GPS;
            }

            public void setGPS(String GPS) {
                this.GPS = GPS;
            }

            public String getBoxCarCode() {
                return BoxCarCode;
            }

            public void setBoxCarCode(String BoxCarCode) {
                this.BoxCarCode = BoxCarCode;
            }

            public String getBoxGPS_Key() {
                return BoxGPS_Key;
            }

            public void setBoxGPS_Key(String BoxGPS_Key) {
                this.BoxGPS_Key = BoxGPS_Key;
            }

            public String getOperate_Name() {
                return Operate_Name;
            }

            public void setOperate_Name(String Operate_Name) {
                this.Operate_Name = Operate_Name;
            }

            public String getOperate_Org() {
                return Operate_Org;
            }

            public void setOperate_Org(String Operate_Org) {
                this.Operate_Org = Operate_Org;
            }

            public String getOperate_DateTime() {
                return Operate_DateTime;
            }

            public void setOperate_DateTime(String Operate_DateTime) {
                this.Operate_DateTime = Operate_DateTime;
            }

            public String getIsAddLetter() {
                return IsAddLetter;
            }

            public void setIsAddLetter(String IsAddLetter) {
                this.IsAddLetter = IsAddLetter;
            }

            public String getTransaction_Status_Oid() {
                return Transaction_Status_Oid;
            }

            public void setTransaction_Status_Oid(String Transaction_Status_Oid) {
                this.Transaction_Status_Oid = Transaction_Status_Oid;
            }

            public String getFullCar_Destination() {
                return fullCar_Destination;
            }

            public void setFullCar_Destination(String fullCar_Destination) {
                this.fullCar_Destination = fullCar_Destination;
            }

            public String getBLat() {
                return BLat;
            }

            public void setBLat(String BLat) {
                this.BLat = BLat;
            }

            public String getBLng() {
                return BLng;
            }

            public void setBLng(String BLng) {
                this.BLng = BLng;
            }

            public String getTransaction_Status() {
                return Transaction_Status;
            }

            public void setTransaction_Status(String Transaction_Status) {
                this.Transaction_Status = Transaction_Status;
            }
        }

        public static class CarMinleagesBean {
            private String StarTime;
            private String EndTime;
            private String TotalMileage;
            private String CarCode;
            private String CarCode_Key;
            /**
             * Summary_Date : 2016-07-10
             * Summary_Mileage : 12
             */

            private List<ListMileageBean> ListMileage;

            public String getStarTime() {
                return StarTime;
            }

            public void setStarTime(String StarTime) {
                this.StarTime = StarTime;
            }

            public String getEndTime() {
                return EndTime;
            }

            public void setEndTime(String EndTime) {
                this.EndTime = EndTime;
            }

            public String getTotalMileage() {
                return TotalMileage;
            }

            public void setTotalMileage(String TotalMileage) {
                this.TotalMileage = TotalMileage;
            }

            public String getCarCode() {
                return CarCode;
            }

            public void setCarCode(String CarCode) {
                this.CarCode = CarCode;
            }

            public String getCarCode_Key() {
                return CarCode_Key;
            }

            public void setCarCode_Key(String CarCode_Key) {
                this.CarCode_Key = CarCode_Key;
            }

            public List<ListMileageBean> getListMileage() {
                return ListMileage;
            }

            public void setListMileage(List<ListMileageBean> ListMileage) {
                this.ListMileage = ListMileage;
            }

            public static class ListMileageBean {
                private String Summary_Date;
                private String Summary_Mileage;

                public String getSummary_Date() {
                    return Summary_Date;
                }

                public void setSummary_Date(String Summary_Date) {
                    this.Summary_Date = Summary_Date;
                }

                public String getSummary_Mileage() {
                    return Summary_Mileage;
                }

                public void setSummary_Mileage(String Summary_Mileage) {
                    this.Summary_Mileage = Summary_Mileage;
                }
            }
        }
    }
}
