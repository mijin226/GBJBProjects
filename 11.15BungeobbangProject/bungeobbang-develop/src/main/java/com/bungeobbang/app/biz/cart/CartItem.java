package com.bungeobbang.app.biz.cart;

import java.util.List;

// 장바구니에 담긴 각 상품의 정보와 비동기 요청에서 상품 번호 및 수량을 관리하는 클래스입니다.
public class CartItem {
    private int productNum;           // 상품 번호
    private int quantity;             // 수량
    private String productName;       // 상품명
    private int productPrice;         // 상품 가격
    private String productProfileWay; // 상품 이미지 경로
    private List<Integer> productIds; // 여러 상품 번호를 받을 수 있는 필드 (비동기 요청용)

    // 기본 생성자
    public CartItem() {
    }

    // 전체 필드를 포함한 생성자
    public CartItem(int productNum, int quantity, String productName, int productPrice, String productProfileWay, List<Integer> productIds) {
        this.productNum = productNum;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productProfileWay = productProfileWay;
        this.productIds = productIds;
    }

    // 기본 필드만을 포함한 생성자
    public CartItem(int productNum, int quantity) {
        this.productNum = productNum;
        this.quantity = quantity;
    }

    // Getter와 Setter
    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductProfileWay() {
        return productProfileWay;
    }

    public void setProductProfileWay(String productProfileWay) {
        this.productProfileWay = productProfileWay;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productNum=" + productNum +
                ", quantity=" + quantity +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productProfileWay='" + productProfileWay + '\'' +
                ", productIds=" + productIds +
                '}';
    }
}
