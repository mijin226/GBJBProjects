package com.bungeobbang.app.biz.payment;

import lombok.ToString;

import java.net.http.HttpResponse;

@ToString
public class PaymentInfoDTO {
    private String token;
    private String imp_uid; // api가 요청한 명칭
    private String merchant_uid;
    private int amount;
    private HttpResponse<String> response;
    private String GW;

    public String getGW() {
        return GW;
    }
    public void setGW(String GW) {
        this.GW = GW;
    }
    public HttpResponse<String> getResponse() {
        return response;
    }
    public void setResponse(HttpResponse<String> response) {
        this.response = response;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getImp_uid() {
        return imp_uid;
    }
    public void setImp_uid(String imp_uid) {
        this.imp_uid = imp_uid;
    }
    public String getMerchant_uid() {
        return merchant_uid;
    }
    public void setMerchant_uid(String merchant_uid) {
        this.merchant_uid = merchant_uid;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }


}