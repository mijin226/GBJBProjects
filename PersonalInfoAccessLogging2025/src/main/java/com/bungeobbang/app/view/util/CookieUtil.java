package com.bungeobbang.app.view.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**@author 한지윤 */
@Slf4j
public class CookieUtil {
    //해당하는 쿠키 반환 (쿠키데이터 전체에서 cookieName을 가진 쿠키 반환)
    public static Cookie cookieData (Cookie[] cookies, String cookieName){
        log.info("log: cookieData start");
        Cookie data = null; //쿠키 변수
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    // URL 인코딩된 값을 디코딩하여 쿠키에서 저장된 상품 목록을 가져옴
                    data = cookie;
                    log.info("log: cookieData data : [{}]", data);
                    break; //상품 목록 확인 시 종료
                }
            }
        }
        log.info("log: cookieData end");
        return data;
    }

    //쿠키 값에 넣을 데이터 생성 후 인코딩해서 반환 (cookieData에서 찾은 쿠키로부터 list를 만들어 productNum 데이터 추가(쿠키 추가할 준비))
    public static String cookiesListCreate (Cookie cookie, int productNum) throws NullPointerException {
        log.info("log: cookiesListCreate start");
        List<String> viewedProductList; //리스트로 변환할 데이터를 담을 리스트
        String cookieslist = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8); //디코딩
        String product = "" + productNum; //형변환
        if (cookieslist == null || cookieslist.isEmpty()) {
            log.warn("log: cookiesListCreate null or Empty return : [{}]", product);
            //쿠키에 저장된 값이 없을 때 (== 처음으로 상품을 본 경우)
            // 해당 상품 번호만 반환
            log.info("log: cookiesListCreate end");
            return URLEncoder.encode(product, StandardCharsets.UTF_8); //인코딩
        }
        //쿠키에 이미 값이 있을 때 (==이미 본 상품이 있는 경우)
        //기존에 본 상품 목록이 있을 경우 기존 값을 리스트로 변환
        viewedProductList = new ArrayList<>(Arrays.asList(cookieslist.split(",")));
        if(viewedProductList.remove(product)){ //이미 리스트에 있는 값인 경우 (==이미 조회한 상품) 리스트에서 제거 (없다면 제거X)
            log.info("log: cookiesListCreate remove product : [{}]", product);
        }
        viewedProductList.add(product); //해당 상품을 리스트에 추가

        //10개까지만 저장
        while (true) {
            //종료조건
            if (viewedProductList.size() <= 10) {
                log.info("log: cookiesListCreate list size is 10 down");
                break;
            }
            viewedProductList.remove(0); //가장 먼저 저장된 값 삭제
            log.warn("log: cookiesListCreate remove index 0");
        }
        cookieslist = String.join(",", viewedProductList);
        log.info("log: cookiesListCreate end retrun : [{}]", cookieslist);
        return URLEncoder.encode(cookieslist, StandardCharsets.UTF_8); //인코딩
    }

    //쿠키 데이터 수정 (유효하지 않은 데이터가 있는 경우 호출해서 해당 데이터 쿠키에서 제거 후 재 등록)
    public static boolean cookieDataDelete (HttpServletResponse response, Cookie cookie, ArrayList<String> productNums){
        log.info("log: cookieDataDelete start");
        List<String> viewedProductList = null; //리스트로 변환할 데이터를 담을 리스트
        String cookieslist = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8); //디코딩
        if (cookieslist == null || cookieslist.isEmpty()) {
            //쿠키에 저장된 값이 없을 때
            log.info("log: cookieDataDelete end null or Empty false");
            return false;
        }
        //쿠키에 이미 값이 있을 때 (==이미 본 상품이 있는 경우)
        //기존에 본 상품 목록이 있을 경우 기존 값을 리스트로 변환
        viewedProductList = new ArrayList<>(Arrays.asList(cookieslist.split(",")));
        for(String num : productNums){
            viewedProductList.remove(num);
            log.info("log: cookieDelete delete num : " + num);
        }
        cookieslist = String.join(",", viewedProductList);
        
        String encodedCookiesList = URLEncoder.encode(cookieslist, StandardCharsets.UTF_8);
        Cookie modifyCookie = new Cookie(cookie.getName(), encodedCookiesList);
        if(viewedProductList.isEmpty()){//쿠키에 넣을 데이터가 없다면
            modifyCookie.setMaxAge(0);//데이터 만료
            log.info("log: cookieDelete empty cookie delete");
        }
        else {
            modifyCookie.setMaxAge(cookie.getMaxAge()); // 쿠키 만료 시간
        }
        modifyCookie.setPath("/"); // 애플리케이션 전체에서 사용 가능하도록 설정
        response.addCookie(modifyCookie); //쿠키에 등록
        log.info("log: cookieDelete end true");
        return true;
    }

    //쿠키에 데이터 추가 (새로운 쿠키를 추가하거나 기존 쿠키를 업데이트(cookiesListCreate) 진행)
    public static boolean cookieAdd(HttpServletResponse response, String cookieName, String value, int day) throws NullPointerException {
        log.info("log: cookieAdd start");
        Cookie cookie = new Cookie(cookieName, value); //쿠키로 변환
        cookie.setMaxAge(60 * 60 * 24 * day); // 쿠키 만료 시간
        cookie.setPath("/"); // 애플리케이션 전체에서 사용 가능하도록 설정
        response.addCookie(cookie); //쿠키에 등록
        log.info("log: cookieAdd end cookie : [{}]", cookie);
        return true;
    }

    //(통합기능) 쿠키에 새 데이터 추가
    //쿠키에서 해당 하는 이름을 가진 쿠키를 찾아 리스트를 뽑아내 해당 리스트에 새로운 데이터를 추가하고 해당 데이터를 다시 쿠키에 등록하는 메서드
    public static boolean cookieAddNewData(HttpServletResponse response, String cookieName, Cookie[] cookies, int productNum, int day) {
        log.info("log: cookieAddNewData add product : [{}], cookieName : [{}]", productNum, cookieName);

        // 기존 쿠키 데이터 가져오기
        Cookie existingCookie = cookieData(cookies, cookieName);

        // 쿠키가 존재하지 않을 경우, 새로운 데이터를 생성하여 쿠키 추가
        if (existingCookie == null) {
            log.warn("log: cookieAddNewData - No existing cookie found. Creating a new cookie for productNum: [{}]", productNum);
            String newValue = URLEncoder.encode(String.valueOf(productNum), StandardCharsets.UTF_8); // 상품 번호만 인코딩해서 추가
            return cookieAdd(response, cookieName, newValue, day);
        }

        // 기존 쿠키가 있을 경우, 리스트 업데이트 후 쿠키 추가
        String updatedValue = cookiesListCreate(existingCookie, productNum);
        return cookieAdd(response, cookieName, updatedValue, day);
    }
    //오버로딩
    //쿠키에서 해당 하는 이름을 가진 쿠키를 찾아 리스트를 뽑아내 해당 리스트에 새로운 데이터를 추가하고 해당 데이터를 다시 쿠키에 등록하는 메서드를 호출하며 30일 기간 설정을 디폴트로 전달, 비체크 예외이므로 사용 시 try catch 잊지말기
    public static boolean cookieAddNewData(HttpServletResponse response, String cookieName, Cookie[] cookies, int productNum) throws NullPointerException {
        log.info("log: cookieAddNewData day 30 version");
        return cookieAddNewData(response, cookieName, cookies, productNum, 30);
    }

}
