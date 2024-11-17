package com.bungeobbang.app.biz.storeMenu;

import lombok.Data;

@Data
public class StoreMenuDTO {
	private int storeMenuNum;
	private int storeNum;
	private String storeMenuNormal;
	private String storeMenuVeg;
	private String storeMenuMini;
	private String storeMenuPotato;
	private String storeMenuIce;
	private String storeMenuCheese;
	private String storeMenuPastry;
	private String storeMenuOther;

	//검색 개수 카운팅용
	private String condition; //구분 값
	private int storeMenuNormalCnt; //팥슈붕 개수
	private int storeMenuVegCnt; //야채 김치 만두 개수
	private int storeMenuMiniCnt; //미니 개수
	private int storeMenuPotatoCnt; //고구마 개수
	private int storeMenuIceCnt; //아이스크림 초코 개수
	private int storeMenuCheeseCnt; //치즈 개수
	private int storeMenuPastryCnt; //패스츄리 개수
	private int storeMenuOtherCnt; //기타 개수

}
