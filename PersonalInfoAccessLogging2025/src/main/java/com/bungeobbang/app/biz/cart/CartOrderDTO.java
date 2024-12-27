package com.bungeobbang.app.biz.cart;

import java.util.ArrayList;
import java.util.List;

// 여러 상품 번호와 수량을 관리하기 위한 DTO 클래스입니다.
public class CartOrderDTO {
    private int memberNum; // 회원 번호
    private List<ProductDetail> products = new ArrayList<>(); // 상품 세부 정보를 담은 리스트, 빈 리스트로 초기화

    // 기본 생성자
    public CartOrderDTO() {}

    // 전체 필드를 포함한 생성자
    public CartOrderDTO(List<ProductDetail> products) {
        this.products = products;
    }

    // Getter와 Setter
    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public List<ProductDetail> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetail> products) {
        this.products = products;
    }

    // 객체의 상태를 문자열로 반환하는 메서드
    @Override
    public String toString() {
        return "CartOrderDTO{" +
                "memberNum=" + memberNum +
                ", products=" + products +
                '}';
    }

    // 각 상품의 번호와 수량을 담는 내부 클래스입니다.
    public static class ProductDetail {
        private int productNum; // 상품 번호
        private int quantity;    // 수량

        // 기본 생성자
        public ProductDetail() {}

        // 전체 필드를 포함한 생성자
        public ProductDetail(int productNum, int quantity) {
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

        // 객체의 상태를 문자열로 반환하는 메서드
        @Override
        public String toString() {
            return "ProductDetail{" +
                    "productNum=" + productNum +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}
