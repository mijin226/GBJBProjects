package com.bungeobbang.app.biz.store;

import java.util.ArrayList;
import java.util.HashMap;

import com.bungeobbang.app.biz.storeWork.StoreWorkDTO;
import lombok.Data;


@Data
public class StoreDTO {
	private int storeNum; // 가게 고유번호(PK)	
	private String storeName; // 가게 상호명
	private String storeAddress; // 가게 기본주소
	private String storeAddressDetail; // 가게 상세주소
	private String storeContact; // 가게 전화번호
	private String storeClosed; // 폐점여부(Y/N)
	private String storeSecret; // 공개여부
	
	// 폐점한 가게 수
	private int storeClosedCnt;
	
	private int maxPK;
	
	//필터 검색을 위한 DTO
	private HashMap<String, String> filterList; //필터검색용 <검색구분용, 검색값>	
	private String storeDeclared; // 신고 여부
	
	private String storeMenuNormal;
	private String storeMenuVeg;
	private String storeMenuMini;
	private String storeMenuPotato;
	private String storeMenuIce;
	private String storeMenuCheese;
	private String storeMenuPastry;
	private String storeMenuOther;
	private String storePaymentCashMoney;
	private String storePaymentCard;
	private String storePaymentAccount;
	

	private String condition; // 
	private int startNum; // 시작 페이지
	private int endNum; // 종료 페이지
	private int cnt; //페이지 네이션 개수 반환용

	//영업일 정보
	private ArrayList<StoreWorkDTO> workList;
	
}