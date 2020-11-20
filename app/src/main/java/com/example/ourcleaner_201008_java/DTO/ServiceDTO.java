package com.example.ourcleaner_201008_java.DTO;

/* 서비스 데이터 객체
* 1. 1회 서비스, 정기 서비스 포함
* 2. 장소는 객체로 저장
* 3. 필요한 데이터 :
*                   서비스 상태(결제 완료 - 매칭 전 - 매칭 후 - 매니저 출발 - 청소 시작 - 청소 완료 / 매칭 취소 / 매칭 실패)
*
*                   장소 정보
*                   매니저 정보 - 미지정은 미지정
*                   1회 / 정기 여부
*                   방문 날짜 혹은 첫 날짜
*                   요일
*                   소요 시간
*
*                   집중 청소 구역
*                   세탁 선택
*                   세탁 내용
*                   쓰레기 배출 선택
*                   쓰레기 배출 방법
*
*                   주의사항
*
*                   결제 카드
*
*
*                    */

import java.io.Serializable;
import java.util.HashMap;

public class ServiceDTO implements Serializable {

    /* 기본 정보 */
    String currentUser; //서비스 신청자
    String serviceState; // 서비스 상태

    /* Service1_TimeAcitvity */
    MyplaceDTO myplaceDTO;
    String managerName;
    Boolean regularBool; //참이면, 정기 결제, 거짓이면, 1회 서비스
    String visitDate; // 월.일(요일)
    String visitDay; // 1회 서비스인 경우엔, 그냥 요일
    int startTime; // 시작 시간 분으로 계산해서 사용함
    int needDefTime; // 소요시간 -> 필요한 전체 시간 아님.
    int needDefCost; // 필요한 가격

    /* Service2_InfoActivity */
    HashMap servicefocusedhashMap; //집중 청소 구역
    Boolean laundryBool; //세탁 선택 여부
    String laundryCaution; //세탁시 주의사항
    Boolean garbagerecycleBool; //재활용
    Boolean garbagenormalBool; //일반쓰레기
    Boolean garbagefoodBool; //음식물쓰레기
    String garbagehowto; //쓰레기 배출 방법

    /* Service3_CautionActivity */
    HashMap serviceplus; //유료 서비스 선택
    String serviceCaution;

    /* Service1_TimeActivity -> Service2_InfoActivity */

    public ServiceDTO(String currentUser, String serviceState, MyplaceDTO myplaceDTO, String managerName, Boolean regularBool, String visitDate, String visitDay, int startTime, int needDefTime, int needDefCost) {
        this.currentUser = currentUser;
        this.serviceState = serviceState;
        this.myplaceDTO = myplaceDTO;
        this.managerName = managerName;
        this.regularBool = regularBool;
        this.visitDate = visitDate;
        this.visitDay = visitDay;
        this.startTime = startTime;
        this.needDefTime = needDefTime;
        this.needDefCost = needDefCost;
    }


    /* Service2_TimeActivity -> Service3_InfoActivity */

    public ServiceDTO(String currentUser, String serviceState, MyplaceDTO myplaceDTO, String managerName, Boolean regularBool, String visitDate, String visitDay, int startTime, int needDefTime, int needDefCost, HashMap servicefocusedhashMap, Boolean laundryBool, String laundryCaution, Boolean garbagerecycleBool, Boolean garbagenormalBool, Boolean garbagefoodBool, String garbagehowto) {
        this.currentUser = currentUser;
        this.serviceState = serviceState;
        this.myplaceDTO = myplaceDTO;
        this.managerName = managerName;
        this.regularBool = regularBool;
        this.visitDate = visitDate;
        this.visitDay = visitDay;
        this.startTime = startTime;
        this.needDefTime = needDefTime;
        this.needDefCost = needDefCost;
        this.servicefocusedhashMap = servicefocusedhashMap;
        this.laundryBool = laundryBool;
        this.laundryCaution = laundryCaution;
        this.garbagerecycleBool = garbagerecycleBool;
        this.garbagenormalBool = garbagenormalBool;
        this.garbagefoodBool = garbagefoodBool;
        this.garbagehowto = garbagehowto;
    }

    /* Service3_InfoActivity ->  저장할 때, */

    public ServiceDTO(String currentUser, String serviceState, MyplaceDTO myplaceDTO, String managerName, Boolean regularBool, String visitDate, String visitDay, int startTime, int needDefTime, int needDefCost, HashMap servicefocusedhashMap, Boolean laundryBool, String laundryCaution, Boolean garbagerecycleBool, Boolean garbagenormalBool, Boolean garbagefoodBool, String garbagehowto, HashMap serviceplus, String serviceCaution) {
        this.currentUser = currentUser;
        this.serviceState = serviceState;
        this.myplaceDTO = myplaceDTO;
        this.managerName = managerName;
        this.regularBool = regularBool;
        this.visitDate = visitDate;
        this.visitDay = visitDay;
        this.startTime = startTime;
        this.needDefTime = needDefTime;
        this.needDefCost = needDefCost;
        this.servicefocusedhashMap = servicefocusedhashMap;
        this.laundryBool = laundryBool;
        this.laundryCaution = laundryCaution;
        this.garbagerecycleBool = garbagerecycleBool;
        this.garbagenormalBool = garbagenormalBool;
        this.garbagefoodBool = garbagefoodBool;
        this.garbagehowto = garbagehowto;
        this.serviceplus = serviceplus;
        this.serviceCaution = serviceCaution;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getServiceState() {
        return serviceState;
    }

    public void setServiceState(String serviceState) {
        this.serviceState = serviceState;
    }

    public MyplaceDTO getMyplaceDTO() {
        return myplaceDTO;
    }

    public void setMyplaceDTO(MyplaceDTO myplaceDTO) {
        this.myplaceDTO = myplaceDTO;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Boolean getRegularBool() {
        return regularBool;
    }

    public void setRegularBool(Boolean regularBool) {
        this.regularBool = regularBool;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getVisitDay() {
        return visitDay;
    }

    public void setVisitDay(String visitDay) {
        this.visitDay = visitDay;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getNeedDefTime() {
        return needDefTime;
    }

    public void setNeedDefTime(int needDefTime) {
        this.needDefTime = needDefTime;
    }

    public int getNeedDefCost() {
        return needDefCost;
    }

    public void setNeedDefCost(int needDefCost) {
        this.needDefCost = needDefCost;
    }

    public HashMap getServicefocusedhashMap() {
        return servicefocusedhashMap;
    }

    public void setServicefocusedhashMap(HashMap servicefocusedhashMap) {
        this.servicefocusedhashMap = servicefocusedhashMap;
    }

    public Boolean getLaundryBool() {
        return laundryBool;
    }

    public void setLaundryBool(Boolean laundryBool) {
        this.laundryBool = laundryBool;
    }

    public String getLaundryCaution() {
        return laundryCaution;
    }

    public void setLaundryCaution(String laundryCaution) {
        this.laundryCaution = laundryCaution;
    }


    public Boolean getGarbagerecycleBool() {
        return garbagerecycleBool;
    }

    public void setGarbagerecycleBool(Boolean garbagerecycleBool) {
        this.garbagerecycleBool = garbagerecycleBool;
    }

    public Boolean getGarbagenormalBool() {
        return garbagenormalBool;
    }

    public void setGarbagenormalBool(Boolean garbagenormalBool) {
        this.garbagenormalBool = garbagenormalBool;
    }

    public Boolean getGarbagefoodBool() {
        return garbagefoodBool;
    }

    public void setGarbagefoodBool(Boolean garbagefoodBool) {
        this.garbagefoodBool = garbagefoodBool;
    }

    public String getGarbagehowto() {
        return garbagehowto;
    }

    public void setGarbagehowto(String garbagehowto) {
        this.garbagehowto = garbagehowto;
    }

    public HashMap getServiceplus() {
        return serviceplus;
    }

    public void setServiceplus(HashMap serviceplus) {
        this.serviceplus = serviceplus;
    }

    public String getServiceCaution() {
        return serviceCaution;
    }

    public void setServiceCaution(String serviceCaution) {
        this.serviceCaution = serviceCaution;
    }




}
