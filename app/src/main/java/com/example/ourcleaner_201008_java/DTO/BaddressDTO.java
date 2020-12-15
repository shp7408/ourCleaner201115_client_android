package com.example.ourcleaner_201008_java.DTO;

import java.io.Serializable;

/* 법정동 주소 검색할 때 사용하는 DTO 리사이클러뷰에 뿌려야 함 */
public class BaddressDTO implements Serializable {

    String bCode, sidoName, sigunguName, eupmyundongName, dongliName, birth;

    public BaddressDTO() {
    }

    public BaddressDTO(String bCode, String sidoName, String sigunguName, String eupmyundongName, String dongliName, String birth) {
        this.bCode = bCode;
        this.sidoName = sidoName;
        this.sigunguName = sigunguName;
        this.eupmyundongName = eupmyundongName;
        this.dongliName = dongliName;
        this.birth = birth;
    }

    public String getbCode() {
        return bCode;
    }

    public void setbCode(String bCode) {
        this.bCode = bCode;
    }

    public String getSidoName() {
        return sidoName;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }

    public String getSigunguName() {
        return sigunguName;
    }

    public void setSigunguName(String sigunguName) {
        this.sigunguName = sigunguName;
    }

    public String getEupmyundongName() {
        return eupmyundongName;
    }

    public void setEupmyundongName(String eupmyundongName) {
        this.eupmyundongName = eupmyundongName;
    }

    public String getDongliName() {
        return dongliName;
    }

    public void setDongliName(String dongliName) {
        this.dongliName = dongliName;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
