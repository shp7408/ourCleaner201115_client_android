package com.example.ourcleaner_201008_java.DTO;

public class ManagerWaitingDTO {

    /* 매칭 대기중인 청소 서비스 목록 보여주는 리사이클러 뷰에 넣을 데이터 객체 클래스 */

    int uidInt; //서비스 uid
    String placeMasterStr; //서비스 신청자
    String dateStr; //서비스 날짜 (11.26(목))
    int startTimeInt;  // 시작 시간
    int needTimeInt; // 필요한 시간
    int needDefCost; // 필요한 비용
    String addressStr; // 주소 (동 까지만 표시하기)
    String myplaceDTO_sizeStr;
    String serviceStateStr; //서비스 상태

    public ManagerWaitingDTO(int uidInt, String placeMasterStr, String dateStr, int startTimeInt, int needTimeInt, int needDefCost, String addressStr, String myplaceDTO_sizeStr, String serviceStateStr) {
        this.uidInt = uidInt;
        this.placeMasterStr = placeMasterStr;
        this.dateStr = dateStr;
        this.startTimeInt = startTimeInt;
        this.needTimeInt = needTimeInt;
        this.needDefCost = needDefCost;
        this.addressStr = addressStr;
        this.myplaceDTO_sizeStr = myplaceDTO_sizeStr;
        this.serviceStateStr = serviceStateStr;
    }

    public int getUidInt() {
        return uidInt;
    }

    public void setUidInt(int uidInt) {
        this.uidInt = uidInt;
    }

    public String getPlaceMasterStr() {
        return placeMasterStr;
    }

    public void setPlaceMasterStr(String placeMasterStr) {
        this.placeMasterStr = placeMasterStr;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getStartTimeInt() {
        return startTimeInt;
    }

    public void setStartTimeInt(int startTimeInt) {
        this.startTimeInt = startTimeInt;
    }

    public int getNeedTimeInt() {
        return needTimeInt;
    }

    public void setNeedTimeInt(int needTimeInt) {
        this.needTimeInt = needTimeInt;
    }

    public int getNeedDefCost() {
        return needDefCost;
    }

    public void setNeedDefCost(int needDefCost) {
        this.needDefCost = needDefCost;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public String getMyplaceDTO_sizeStr() {
        return myplaceDTO_sizeStr;
    }

    public void setMyplaceDTO_sizeStr(String myplaceDTO_sizeStr) {
        this.myplaceDTO_sizeStr = myplaceDTO_sizeStr;
    }

    public String getServiceStateStr() {
        return serviceStateStr;
    }

    public void setServiceStateStr(String serviceStateStr) {
        this.serviceStateStr = serviceStateStr;
    }
}