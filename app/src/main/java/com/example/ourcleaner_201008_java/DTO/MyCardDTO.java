package com.example.ourcleaner_201008_java.DTO;

public class MyCardDTO {
    int uid;
    String currentUser;
    String billing_key;
    String pg_name;
    String pg;
    String method_name;
    String method;
    String card_code;
    String card_name;
    String e_at;
    String c_at;
    String receipt_id;
    String action;

    public MyCardDTO(int uid, String currentUser, String billing_key, String pg_name, String pg, String method_name, String method, String card_code, String card_name, String e_at, String c_at, String receipt_id, String action) {
        this.uid = uid;
        this.currentUser = currentUser;
        this.billing_key = billing_key;
        this.pg_name = pg_name;
        this.pg = pg;
        this.method_name = method_name;
        this.method = method;
        this.card_code = card_code;
        this.card_name = card_name;
        this.e_at = e_at;
        this.c_at = c_at;
        this.receipt_id = receipt_id;
        this.action = action;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getBilling_key() {
        return billing_key;
    }

    public void setBilling_key(String billing_key) {
        this.billing_key = billing_key;
    }

    public String getPg_name() {
        return pg_name;
    }

    public void setPg_name(String pg_name) {
        this.pg_name = pg_name;
    }

    public String getPg() {
        return pg;
    }

    public void setPg(String pg) {
        this.pg = pg;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCard_code() {
        return card_code;
    }

    public void setCard_code(String card_code) {
        this.card_code = card_code;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getE_at() {
        return e_at;
    }

    public void setE_at(String e_at) {
        this.e_at = e_at;
    }

    public String getC_at() {
        return c_at;
    }

    public void setC_at(String c_at) {
        this.c_at = c_at;
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
