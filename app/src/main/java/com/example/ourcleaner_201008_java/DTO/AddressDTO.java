package com.example.ourcleaner_201008_java.DTO;

import java.io.Serializable;

public class AddressDTO implements Serializable {

    /* 다음 주소검색 웹뷰 서버에서 받아온 주소 데이터 담는 변수 */
    //bname1Str : ("동"지역일 경우에는 공백, "리"지역일 경우에는 "읍" 또는 "면" 정보가 들어갑니다.)

    String zonecodeStr;
    String addrStr;
    String buildingNameStr;
    String sidoStr;
    String bcodeStr;
    String bnameStr;
    String bname1Str;
    String bname2Str;
    String detailStr;


    /* 상세 주소 입력 전 */
    public AddressDTO(String zonecodeStr, String addrStr, String buildingNameStr, String sidoStr, String bcodeStr, String bnameStr, String bname1Str, String bname2Str) {
        this.zonecodeStr = zonecodeStr;
        this.addrStr = addrStr;
        this.buildingNameStr = buildingNameStr;
        this.sidoStr = sidoStr;
        this.bcodeStr = bcodeStr;
        this.bnameStr = bnameStr;
        this.bname1Str = bname1Str;
        this.bname2Str = bname2Str;
    }

    /* 상세 주소 입력 후 */
    public AddressDTO(String zonecodeStr, String addrStr, String buildingNameStr, String sidoStr, String bcodeStr, String bnameStr, String bname1Str, String bname2Str, String detailStr) {
        this.zonecodeStr = zonecodeStr;
        this.addrStr = addrStr;
        this.buildingNameStr = buildingNameStr;
        this.sidoStr = sidoStr;
        this.bcodeStr = bcodeStr;
        this.bnameStr = bnameStr;
        this.bname1Str = bname1Str;
        this.bname2Str = bname2Str;
        this.detailStr = detailStr;
    }

    public String getZonecodeStr() {
        return zonecodeStr;
    }

    public void setZonecodeStr(String zonecodeStr) {
        this.zonecodeStr = zonecodeStr;
    }

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public String getBuildingNameStr() {
        return buildingNameStr;
    }

    public void setBuildingNameStr(String buildingNameStr) {
        this.buildingNameStr = buildingNameStr;
    }

    public String getSidoStr() {
        return sidoStr;
    }

    public void setSidoStr(String sidoStr) {
        this.sidoStr = sidoStr;
    }

    public String getBcodeStr() {
        return bcodeStr;
    }

    public void setBcodeStr(String bcodeStr) {
        this.bcodeStr = bcodeStr;
    }

    public String getBnameStr() {
        return bnameStr;
    }

    public void setBnameStr(String bnameStr) {
        this.bnameStr = bnameStr;
    }

    public String getBname1Str() {
        return bname1Str;
    }

    public void setBname1Str(String bname1Str) {
        this.bname1Str = bname1Str;
    }

    public String getBname2Str() {
        return bname2Str;
    }

    public void setBname2Str(String bname2Str) {
        this.bname2Str = bname2Str;
    }

    public String getDetailStr() {
        return detailStr;
    }

    public void setDetailStr(String detailStr) {
        this.detailStr = detailStr;
    }
}
