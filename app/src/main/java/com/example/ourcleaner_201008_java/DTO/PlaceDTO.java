package com.example.ourcleaner_201008_java.DTO;


/* 안 쓰는 클래스임.. */
public class PlaceDTO {

    String doromyeongJuso; //도로명주소
    String jibeonJuso; //지번주소
    boolean canUse; //서비스 가능 여부

    public PlaceDTO(String doromyeongJuso, String jibeonJuso, boolean canUse) {
        this.doromyeongJuso = doromyeongJuso;
        this.jibeonJuso = jibeonJuso;
        this.canUse = canUse;
    }

    public String getDoromyeongJuso() {
        return doromyeongJuso;
    }

    public void setDoromyeongJuso(String doromyeongJuso) {
        this.doromyeongJuso = doromyeongJuso;
    }

    public String getJibeonJuso() {
        return jibeonJuso;
    }

    public void setJibeonJuso(String jibeonJuso) {
        this.jibeonJuso = jibeonJuso;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }
}
