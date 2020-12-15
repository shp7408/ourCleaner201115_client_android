package com.example.ourcleaner_201008_java.Model;

/* 레트로핏-리사이클러 뷰 테스트용 : 서버에서 데이터 받아오기 위한 코드 */

public class ModelRecycler {

    private String name, country, city, imgURL;

    public String getImgURL(){
        return imgURL;
    }

    public void setImgURL(String imgURL){
        this.imgURL = imgURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}