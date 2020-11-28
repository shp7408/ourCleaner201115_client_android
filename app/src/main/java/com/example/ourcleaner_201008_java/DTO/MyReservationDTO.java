package com.example.ourcleaner_201008_java.DTO;

public class MyReservationDTO implements Comparable<MyReservationDTO>{

    int uid;
    String currentUser; //서비스 신청자
    String serviceState; // 서비스 상태

    String serviceDate; // 서비스 날짜
    String placeName; // 장소 이름
    String serviceTime; // 서비스 시작 시간

    public MyReservationDTO(int uid, String currentUser, String serviceState, String serviceDate, String placeName, String serviceTime) {
        this.uid = uid;
        this.currentUser = currentUser;
        this.serviceState = serviceState;
        this.serviceDate = serviceDate;
        this.placeName = placeName;
        this.serviceTime = serviceTime;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public int getUid() {
        return uid;
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

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public int compareTo(MyReservationDTO myReservationDTO) {
        // ascending order
//        return this.uid - myReservationDTO.uid;
        // descending order
          return myReservationDTO.uid - this.uid;

    }
}
