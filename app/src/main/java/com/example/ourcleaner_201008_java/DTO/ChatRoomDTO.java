package com.example.ourcleaner_201008_java.DTO;

public class ChatRoomDTO implements Comparable<ChatRoomDTO>{

    String currentUser;

    String lastMessage; //마지막 메세지 (내가 / 상대방이 상관없이 그냥 마지막 메세지)
    String whenSend; //마지막 메세지 시간

    String whomSent1, whomSent2, whomSent3; //이메일 / 이름 / 프로필이미지(프로필이미지는 매니저인 경우에만 필요함)

    int unreadNum; //읽지 않은 메세지 수

    String roomId; //db에서 1, 2 와 같은 것..
    String alldateStr; //2021-01-21 18:48:14 형태. 정렬 위함

    public ChatRoomDTO() {
    }

    public String getAlldateStr() {
        return alldateStr;
    }

    public void setAlldateStr(String alldateStr) {
        this.alldateStr = alldateStr;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getWhenSend() {
        return whenSend;
    }

    public void setWhenSend(String whenSend) {
        this.whenSend = whenSend;
    }

    public String getWhomSent1() {
        return whomSent1;
    }

    public void setWhomSent1(String whomSent1) {
        this.whomSent1 = whomSent1;
    }

    public String getWhomSent2() {
        return whomSent2;
    }

    public void setWhomSent2(String whomSent2) {
        this.whomSent2 = whomSent2;
    }

    public String getWhomSent3() {
        return whomSent3;
    }

    public void setWhomSent3(String whomSent3) {
        this.whomSent3 = whomSent3;
    }

    public int getUnreadNum() {
        return unreadNum;
    }

    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }

    /* 마지막 메세지의 시간으로 객체 정렬하기 */
    @Override
    public int compareTo(ChatRoomDTO o) {
        return o.getAlldateStr().compareTo(this.alldateStr);
    }

}
