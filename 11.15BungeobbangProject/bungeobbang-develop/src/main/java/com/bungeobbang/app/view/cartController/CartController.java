package com.bungeobbang.app.view.cartController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bungeobbang.app.biz.cart.CartItem;
import com.bungeobbang.app.biz.product.ProductDTO;
import com.bungeobbang.app.biz.product.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller("cart")
public class CartController {
    @Autowired
    private ProductService productService; // 상품 정보를 조회하기 위한 서비스

    // 장바구니에 상품을 추가하는 메서드
    @RequestMapping(value = "/addToCart.do", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> addToCart(@RequestBody CartItem cartItem, HttpSession session) {
        System.out.println("[DEBUG] addToCart 엔드포인트 호출됨");

        Map<String, String> response = new HashMap<>();

        try {
            // 요청된 상품 번호와 수량을 로그로 출력
            System.out.println("[DEBUG] 요청된 상품 번호: " + cartItem.getProductNum());
            System.out.println("[DEBUG] 요청된 수량: " + cartItem.getQuantity());

            // 세션에서 장바구니 정보를 가져옴 (없으면 새로 생성)
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) {
                System.out.println("[DEBUG] 세션에 장바구니가 없어 새로 생성합니다.");
                cart = new ArrayList<>();
            } else {
                System.out.println("[DEBUG] 세션에서 기존 장바구니 로드 완료. 현재 상품 개수: " + cart.size());
            }

            // 동일한 상품이 있는지 확인하고 수량을 업데이트
            boolean itemExists = false;
            for (CartItem item : cart) {
                if (item.getProductNum() == cartItem.getProductNum()) {
                    System.out.println("[DEBUG] 기존에 동일한 상품이 장바구니에 있습니다. 수량을 증가시킵니다.");
                    item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                    itemExists = true;
                    break;
                }
            }

            // 동일한 상품이 없으면 새 항목 추가
            if (!itemExists) {
                System.out.println("[DEBUG] 장바구니에 동일한 상품이 없으므로 새 항목을 추가합니다.");
                cart.add(cartItem);
            }

            // 업데이트된 장바구니를 세션에 저장
            session.setAttribute("cart", cart);
            System.out.println("[DEBUG] 세션에 장바구니가 업데이트되었습니다. 현재 장바구니 상품 개수: " + cart.size());

            response.put("status", "success");
            response.put("message", "상품이 장바구니에 성공적으로 담겼습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("[ERROR] 장바구니 처리 중 오류 발생: " + e.getMessage());
            response.put("status", "error");
            response.put("message", "장바구니 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 장바구니에서 상품을 제거하는 메서드
    @RequestMapping(value = "/removeCart.do", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> removeFromCart(@RequestBody CartItem cartItem, HttpSession session) {
        System.out.println("[DEBUG] removeCart 엔드포인트 호출됨");
        Map<String, String> response = new HashMap<>();
        Integer productNum = cartItem.getProductNum();

        try {
            // 세션에서 장바구니 가져오기
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart != null) {
                // 기존 장바구니를 복사하고 해당 상품을 삭제
                List<CartItem> cartCopy = new ArrayList<>(cart);
                boolean itemRemoved = cartCopy.removeIf(item -> item != null && item.getProductNum() == productNum);

                // 삭제 후 업데이트된 장바구니를 세션에 저장
                if (itemRemoved) {
                    session.setAttribute("cart", cartCopy);
                    response.put("status", "success");
                    response.put("message", "상품이 장바구니에서 성공적으로 삭제되었습니다.");
                } else {
                    response.put("status", "error");
                    response.put("message", "장바구니에 해당 제품이 존재하지 않거나 삭제 중 오류가 발생했습니다.");
                    System.out.println("[DEBUG] 장바구니에 해당 제품이 존재하지 않거나 삭제 중 오류가 발생했습니다. 제품 번호: " + productNum);
                }
            } else {
                response.put("status", "error");
                response.put("message", "세션에 장바구니가 존재하지 않습니다.");
                System.out.println("[DEBUG] 세션에 장바구니가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] 장바구니에서 제품 삭제 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace(); // 예외 스택 트레이스를 출력합니다.
            response.put("status", "error");
            response.put("message", "장바구니에서 제품 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // 장바구니 페이지로 이동하는 메서드
    @RequestMapping(value = "/goToCart.do", method = RequestMethod.GET)
    public String goToCart(HttpSession session, Model model, ProductDTO productDTO) {
        System.out.println("[DEBUG] goToCart 엔드포인트 호출됨");

        // 세션에서 장바구니 정보를 가져옴
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            System.out.println("[DEBUG] 세션에 장바구니가 없어서 새로 생성합니다.");
            cart = new ArrayList<>();
        }

        // 각 productNum에 대한 상품 상세 정보를 조회하여 CartItem에 추가
        List<CartItem> updatedCart = new ArrayList<>();
        for (CartItem item : cart) {
            System.out.println("[DEBUG] 처리 중인 CartItem의 상품 번호: " + item.getProductNum());

            productDTO.setProductNum(item.getProductNum());
            productDTO.setCondition("MD_ONE");

            System.out.println("[DEBUG] 조회할 ProductDTO 설정 - 상품 번호: " + productDTO.getProductNum() + ", 조건: " + productDTO.getCondition());

            // 상품 정보 조회
            try {
                Optional<ProductDTO> productOpt = productService.selectOne(productDTO);
                if (productOpt.isPresent()) {
                    ProductDTO productDetails = productOpt.get();
                    item.setProductName(productDetails.getProductName());
                    item.setProductPrice(productDetails.getProductPrice());
                    item.setProductProfileWay(productDetails.getProductProfileWay());
                    updatedCart.add(item); // 상품이 있을 경우에만 추가
                    System.out.println("[DEBUG] CartItem 업데이트 - 상품명: " + productDetails.getProductName() + ", 가격: " + productDetails.getProductPrice());
                } else {
                    System.out.println("[DEBUG] 해당 상품 번호에 대한 정보를 찾을 수 없음 - 상품 번호: " + item.getProductNum());
                }
            } catch (Exception e) {
                System.out.println("[ERROR] 상품 조회 실패 - 상품 번호: " + item.getProductNum() + ", 오류 메시지: " + e.getMessage());
            }
        }

        // JSON 변환
        try {
            ObjectMapper mapper = new ObjectMapper();
            String cartItemsJson = mapper.writeValueAsString(updatedCart);
            System.out.println("[DEBUG] 장바구니 JSON 데이터: " + cartItemsJson);
            model.addAttribute("cartItemsJson", cartItemsJson);
        } catch (Exception e) {
            System.out.println("[ERROR] JSON 변환 중 오류 발생: " + e.getMessage());
        }

        return "productCart"; // JSP 페이지로 이동
    }

}