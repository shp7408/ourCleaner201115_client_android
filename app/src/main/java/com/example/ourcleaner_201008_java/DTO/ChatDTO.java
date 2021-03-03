package com.example.ourcleaner_201008_java.DTO;

public class ChatDTO {

    int viewType;
    String whoSend;
    String whomSent;
    String getDate;
    String getTime;
    String msgStr;

    String profilePathStr;
    String sendNameStr;



    public ChatDTO() {
    }

    public ChatDTO(int viewType, String whoSend, String whomSent, String getDate, String getTime, String msgStr) {
        this.viewType = viewType;
        this.whoSend = whoSend;
        this.whomSent = whomSent;
        this.getDate = getDate;
        this.getTime = getTime;
        this.msgStr = msgStr;
    }

    public ChatDTO(int viewType, String whoSend, String whomSent, String getDate, String getTime, String msgStr, String profilePathStr, String sendNameStr) {
        this.viewType = viewType;
        this.whoSend = whoSend;
        this.whomSent = whomSent;
        this.getDate = getDate;
        this.getTime = getTime;
        this.msgStr = msgStr;
        this.profilePathStr = profilePathStr;
        this.sendNameStr = sendNameStr;
    }

    public String getProfilePathStr() {
        return profilePathStr;
    }

    public void setProfilePathStr(String profilePathStr) {
        this.profilePathStr = profilePathStr;
    }

    public String getSendNameStr() {
        return sendNameStr;
    }

    public void setSendNameStr(String sendNameStr) {
        this.sendNameStr = sendNameStr;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getWhoSend() {
        return whoSend;
    }

    public void setWhoSend(String whoSend) {
        this.whoSend = whoSend;
    }

    public String getWhomSent() {
        return whomSent;
    }

    public void setWhomSent(String whomSent) {
        this.whomSent = whomSent;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }

    public String getMsgStr() {
        return msgStr;
    }

    public void setMsgStr(String msgStr) {
        this.msgStr = msgStr;
    }
}
