package com.example.ourcleaner_201008_java.DTO;

import java.io.Serializable;
import java.util.HashMap;
/* 일단.. 저장하기 위한 dto로만 하자 머리 안돌아감..  */

public class ReviewDTO implements Serializable {

    int idInt; //게시물 id 리사이클러뷰에 뿌려질때
    String createdEmailStr; //작성자 이메일
    String createdNameStr; //작성자 이름
    String regiDateStr; //작성날짜 기본
    String regiDateLookStr; //보여지기 위함 작성날짜

    double likeDouble; //마음에 들어요 ~ 평균 내기 위함
    HashMap<String, Boolean> radioPosMap;
    HashMap<String, Boolean> radioNegMap;

    int serviceIdInt; //서비스 id
    String managerEmailStr; //서비스 매니저 이메일
    String managerNameStr; //서비스 매니저 이름
    String serviceDateStr;
    String serviceDayStr;

    String placeSizeStr; //몇 평인지

    String reviewContentsStr; //리뷰 내용
    String imageListStr; //이미지 리스트를 스트링으로 todo 뭘로 저장하는게 좋을지 모르겠음..

    public ReviewDTO() {
    }



    public String getServiceDateStr() {
        return serviceDateStr;
    }

    public void setServiceDateStr(String serviceDateStr) {
        this.serviceDateStr = serviceDateStr;
    }

    public String getServiceDayStr() {
        return serviceDayStr;
    }

    public void setServiceDayStr(String serviceDayStr) {
        this.serviceDayStr = serviceDayStr;
    }

    public int getIdInt() {
        return idInt;
    }

    public void setIdInt(int idInt) {
        this.idInt = idInt;
    }

    public String getCreatedEmailStr() {
        return createdEmailStr;
    }

    public void setCreatedEmailStr(String createdEmailStr) {
        this.createdEmailStr = createdEmailStr;
    }

    public String getCreatedNameStr() {
        return createdNameStr;
    }

    public void setCreatedNameStr(String createdNameStr) {
        this.createdNameStr = createdNameStr;
    }

    public String getRegiDateStr() {
        return regiDateStr;
    }

    public void setRegiDateStr(String regiDateStr) {
        this.regiDateStr = regiDateStr;
    }

    public String getRegiDateLookStr() {
        return regiDateLookStr;
    }

    public void setRegiDateLookStr(String regiDateLookStr) {
        this.regiDateLookStr = regiDateLookStr;
    }

    public double getLikeDouble() {
        return likeDouble;
    }

    public void setLikeDouble(double likeDouble) {
        this.likeDouble = likeDouble;
    }

    public HashMap<String, Boolean> getRadioPosMap() {
        return radioPosMap;
    }

    public void setRadioPosMap(HashMap<String, Boolean> radioPosMap) {
        this.radioPosMap = radioPosMap;
    }

    public HashMap<String, Boolean> getRadioNegMap() {
        return radioNegMap;
    }

    public void setRadioNegMap(HashMap<String, Boolean> radioNegMap) {
        this.radioNegMap = radioNegMap;
    }

    public int getServiceIdInt() {
        return serviceIdInt;
    }

    public void setServiceIdInt(int serviceIdInt) {
        this.serviceIdInt = serviceIdInt;
    }

    public String getManagerEmailStr() {
        return managerEmailStr;
    }

    public void setManagerEmailStr(String managerEmailStr) {
        this.managerEmailStr = managerEmailStr;
    }

    public String getManagerNameStr() {
        return managerNameStr;
    }

    public void setManagerNameStr(String managerNameStr) {
        this.managerNameStr = managerNameStr;
    }

    public String getPlaceSizeStr() {
        return placeSizeStr;
    }

    public void setPlaceSizeStr(String placeSizeStr) {
        this.placeSizeStr = placeSizeStr;
    }

    public String getReviewContentsStr() {
        return reviewContentsStr;
    }

    public void setReviewContentsStr(String reviewContentsStr) {
        this.reviewContentsStr = reviewContentsStr;
    }

    public String getImageListStr() {
        return imageListStr;
    }

    public void setImageListStr(String imageListStr) {
        this.imageListStr = imageListStr;
    }
}
