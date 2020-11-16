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

public class ServiceDTO implements Serializable {

    /* 기본 정보 */
    String currentUser; //서비스 신청자
    String serviceState; // 서비스 상태

    /* Service1_TimeAcitvity */
    MyplaceDTO myplaceDTO;
    String managerName;
    Boolean regularBool; //참이면, 정기 결제, 거짓이면, 1회 서비스
    String visitDate; // 정기결제인 경우엔, 첫 날짜
    String visitDay; // 1회 서비스인 경우엔, 그냥 요일
    String estimatedTime; //소요시간

    /* Service2_InfoActivity */
    String reqRoom; //집중 청소 구역
    Boolean laundryBool; //세탁 선택 여부
    String laundryCaution; //세탁시 주의사항
    String garbage1; //재활용
    String garbage2; //일반쓰레기
    String garbage3; //음식물쓰레기
    String garbagehowto; //쓰레기 배출 방법

    /* Service3_CautionActivity */
    String serviceCaution;

    public ServiceDTO(String currentUser, String serviceState, MyplaceDTO myplaceDTO, String managerName, Boolean regularBool, String visitDate, String visitDay, String estimatedTime) {
        this.currentUser = currentUser;
        this.serviceState = serviceState;
        this.myplaceDTO = myplaceDTO;
        this.managerName = managerName;
        this.regularBool = regularBool;
        this.visitDate = visitDate;
        this.visitDay = visitDay;
        this.estimatedTime = estimatedTime;
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

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getReqRoom() {
        return reqRoom;
    }

    public void setReqRoom(String reqRoom) {
        this.reqRoom = reqRoom;
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

    public String getGarbage1() {
        return garbage1;
    }

    public void setGarbage1(String garbage1) {
        this.garbage1 = garbage1;
    }

    public String getGarbage2() {
        return garbage2;
    }

    public void setGarbage2(String garbage2) {
        this.garbage2 = garbage2;
    }

    public String getGarbage3() {
        return garbage3;
    }

    public void setGarbage3(String garbage3) {
        this.garbage3 = garbage3;
    }

    public String getGarbagehowto() {
        return garbagehowto;
    }

    public void setGarbagehowto(String garbagehowto) {
        this.garbagehowto = garbagehowto;
    }

    public String getServiceCaution() {
        return serviceCaution;
    }

    public void setServiceCaution(String serviceCaution) {
        this.serviceCaution = serviceCaution;
    }




}
