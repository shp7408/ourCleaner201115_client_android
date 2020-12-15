package com.example.ourcleaner_201008_java.DTO;

import java.io.Serializable;
import java.util.HashMap;

public class ReviewDTO implements Serializable {
    int id;
    String createdEmailStr;
    String createdNameStr;
    String regiDateStr;

    float starScoreLong;
    HashMap<String, Float> detailScoreHashmap;

    int serviceIdInt;
    String managerEmailStr;
    String managerNameStr;

    String placeSizeStr;

    String reviewContentsStr;

    public ReviewDTO() {
    }

    public ReviewDTO(int id, String createdEmailStr, String createdNameStr, String regiDateStr, float starScoreLong, HashMap<String, Float> detailScoreHashmap, int serviceIdInt, String managerEmailStr, String managerNameStr, String placeSizeStr, String reviewContentsStr) {
        this.id = id;
        this.createdEmailStr = createdEmailStr;
        this.createdNameStr = createdNameStr;
        this.regiDateStr = regiDateStr;
        this.starScoreLong = starScoreLong;
        this.detailScoreHashmap = detailScoreHashmap;
        this.serviceIdInt = serviceIdInt;
        this.managerEmailStr = managerEmailStr;
        this.managerNameStr = managerNameStr;
        this.placeSizeStr = placeSizeStr;
        this.reviewContentsStr = reviewContentsStr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public float getStarScoreLong() {
        return starScoreLong;
    }

    public void setStarScoreLong(long starScoreLong) {
        this.starScoreLong = starScoreLong;
    }

    public HashMap<String, Float> getDetailScoreHashmap() {
        return detailScoreHashmap;
    }

    public void setDetailScoreHashmap(HashMap<String, Float> detailScoreHashmap) {
        this.detailScoreHashmap = detailScoreHashmap;
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
}
