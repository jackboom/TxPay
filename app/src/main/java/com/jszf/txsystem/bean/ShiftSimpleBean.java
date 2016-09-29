package com.jszf.txsystem.bean;

import java.util.List;

/**
 * @author jacking
 *         Created at 2016/9/21 15:17 .
 */
public class ShiftSimpleBean {
    private String StartTime;
    private String EndTime;
    private String UserName;
    private double Recive;
    private String classId;
    private List<Child> childList;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public double getRecive() {
        return Recive;
    }

    public void setRecive(double recive) {
        Recive = recive;
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    public static class Child {
        private int shiftType;
        private String shiftAmount;
        private String shiftCount;
        private String shiftState;

        public String getShiftState() {
            return shiftState;
        }

        public void setShiftState(String shiftState) {
            this.shiftState = shiftState;
        }

        public int getShiftType() {
            return shiftType;
        }

        public void setShiftType(int shiftType) {
            this.shiftType = shiftType;
        }

        public String getShiftAmount() {
            return shiftAmount;
        }

        public void setShiftAmount(String shiftAmount) {
            this.shiftAmount = shiftAmount;
        }

        public String getShiftCount() {
            return shiftCount;
        }

        public void setShiftCount(String shiftCount) {
            this.shiftCount = shiftCount;
        }
    }
}
