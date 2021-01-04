package com.example.ourcleaner_201008_java.DTO;

import java.util.ArrayList;

public class MymanagerDTO {

    int id;
    String email;
    String nameStr;
    String phoneNumStr;
    String addressStr;
    ArrayList<String> desiredWorkAreaList;
    String imagePathStr;

    long reviewStarLong;
    ArrayList<ReviewDTO> reviewDTOArrayList;

    public MymanagerDTO() {
    }

    public MymanagerDTO(int id, String email, String nameStr, String phoneNumStr, String addressStr, ArrayList<String> desiredWorkAreaList, String imagePathStr, long reviewStarLong, ArrayList<ReviewDTO> reviewDTOArrayList) {
        this.id = id;
        this.email = email;
        this.nameStr = nameStr;
        this.phoneNumStr = phoneNumStr;
        this.addressStr = addressStr;
        this.desiredWorkAreaList = desiredWorkAreaList;
        this.imagePathStr = imagePathStr;
        this.reviewStarLong = reviewStarLong;
        this.reviewDTOArrayList = reviewDTOArrayList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameStr() {
        return nameStr;
    }

    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }

    public String getPhoneNumStr() {
        return phoneNumStr;
    }

    public void setPhoneNumStr(String phoneNumStr) {
        this.phoneNumStr = phoneNumStr;
    }

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {
        this.addressStr = addressStr;
    }

    public ArrayList<String> getDesiredWorkAreaList() {
        return desiredWorkAreaList;
    }

    public void setDesiredWorkAreaList(ArrayList<String> desiredWorkAreaList) {
        this.desiredWorkAreaList = desiredWorkAreaList;
    }

    public String getImagePathStr() {
        return imagePathStr;
    }

    public void setImagePathStr(String imagePathStr) {
        this.imagePathStr = imagePathStr;
    }

    public long getReviewStarLong() {
        return reviewStarLong;
    }

    public void setReviewStarLong(long reviewStarLong) {
        this.reviewStarLong = reviewStarLong;
    }

    public ArrayList<ReviewDTO> getReviewDTOArrayList() {
        return reviewDTOArrayList;
    }

    public void setReviewDTOArrayList(ArrayList<ReviewDTO> reviewDTOArrayList) {
        this.reviewDTOArrayList = reviewDTOArrayList;
    }
}
