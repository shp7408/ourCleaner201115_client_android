package com.example.ourcleaner_201008_java.DTO;

import org.json.JSONObject;

import java.util.HashMap;

public class MyplaceDTO {

    String currentUser;

    /* 데이터 저장 및 불러오기 위한 데이터 -> 사용자의 회원 정보에 저장할 것임. 고유 번호가 필요 없음. */
    String placeNameStr;

    /* 사용자가 저장하는 데이터 */
    String address;
    String detailAddress;
    String sizeStr; //평 수
    int sizeIndexint; //index 8평 이하-> 0 / 9평 -> 1 ... 100평 이상-> 92
    //HashMap<String, Boolean> pethashMap; //pet 선택
    boolean petDog;
    boolean petCat;
    boolean petEtc;

    boolean childBool;
    boolean cctvBool;
    boolean parkingBool;

    String petGuideStr; // 없으면 널로 보냄 . 펫이 있는 경우만 데이터 있음
    String parkingGuideStr; // 없으면 널로 보냄. 주차 가능한 경우만 데이터 있음

    /* 장소 목록 보여줄 때, 필요한 생성자임. 수정 삭제 부분에서는 전체를 가져오는 생성자 사용해야 함. */
    public MyplaceDTO(String currentUser, String placeNameStr, String address, String detailAddress, String sizeStr, int sizeIndexint) {
        this.currentUser = currentUser;
        this.placeNameStr = placeNameStr;
        this.address = address;
        this.detailAddress = detailAddress;
        this.sizeStr = sizeStr;
        this.sizeIndexint = sizeIndexint;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getPlaceNameStr() {
        return placeNameStr;
    }

    public void setPlaceNameStr(String placeNameStr) {
        this.placeNameStr = placeNameStr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getSizeStr() {
        return sizeStr;
    }

    public void setSizeStr(String sizeStr) {
        this.sizeStr = sizeStr;
    }

    public int getSizeIndexint() {
        return sizeIndexint;
    }

    public void setSizeIndexint(int sizeIndexint) {
        this.sizeIndexint = sizeIndexint;
    }

    public boolean isPetDog() {
        return petDog;
    }

    public void setPetDog(boolean petDog) {
        this.petDog = petDog;
    }

    public boolean isPetCat() {
        return petCat;
    }

    public void setPetCat(boolean petCat) {
        this.petCat = petCat;
    }

    public boolean isPetEtc() {
        return petEtc;
    }

    public void setPetEtc(boolean petEtc) {
        this.petEtc = petEtc;
    }

    public boolean isChildBool() {
        return childBool;
    }

    public void setChildBool(boolean childBool) {
        this.childBool = childBool;
    }

    public boolean isCctvBool() {
        return cctvBool;
    }

    public void setCctvBool(boolean cctvBool) {
        this.cctvBool = cctvBool;
    }

    public boolean isParkingBool() {
        return parkingBool;
    }

    public void setParkingBool(boolean parkingBool) {
        this.parkingBool = parkingBool;
    }

    public String getPetGuideStr() {
        return petGuideStr;
    }

    public void setPetGuideStr(String petGuideStr) {
        this.petGuideStr = petGuideStr;
    }

    public String getParkingGuideStr() {
        return parkingGuideStr;
    }

    public void setParkingGuideStr(String parkingGuideStr) {
        this.parkingGuideStr = parkingGuideStr;
    }
}
