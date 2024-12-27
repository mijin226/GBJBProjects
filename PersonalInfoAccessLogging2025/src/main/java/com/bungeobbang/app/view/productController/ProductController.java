package com.bungeobbang.app.view.productController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bungeobbang.app.view.util.CookieUtil;
import com.bungeobbang.app.view.util.PaginationUtils;
import com.bungeobbang.app.biz.product.ProductDTO;
import com.bungeobbang.app.biz.product.ProductService;
import com.bungeobbang.app.biz.productCate.ProductCateDTO;
import com.bungeobbang.app.biz.productCate.ProductCateService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller("product")
@Slf4j
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCateService productCateService;

	private final int PAGE_SIZE = 6; // 페이지당 항목 수 설정
	private final String COOKIE_NAME = "viewedProducts"; // 최근 본 상품 쿠키 이름 설정

	// 상품 상세보기
	@RequestMapping("/infoProduct.do")
	public String infoProduct(
			HttpServletRequest request, 
			Model model, 
			@RequestParam("productNum") int productNum,
			HttpServletResponse response) {
		log.info("[INFO] /infoProduct.do - 상세보기 시작");

		// productNum 유효성 검사 (0인 경우 유효하지 않음)
		if (productNum == 0) {
			log.error("[ERROR] /infoProduct.do - productNum이 유효하지 않습니다.");
			return "errorPage"; // 적절한 에러 페이지 또는 메시지를 반환
		}

		// ProductDTO 객체 생성 후 productNum과 조회 조건 설정
		ProductDTO productDTO = new ProductDTO();
		productDTO.setProductNum(productNum);
		productDTO.setCondition("MD_ONE"); // 조회조건 설정

		// 상품 정보 조회
	    Optional<ProductDTO> retrievedProductOpt = productService.selectOne(productDTO);
	    if (retrievedProductOpt.isEmpty()) {
	    	log.error("로그: [오류] infoProduct - productNum {}에 해당하는 상품을 찾을 수 없습니다.", productNum);
	        return "errorPage"; // 조회 결과 없으면 에러 페이지로 이동
	    }

	    ProductDTO retrievedProduct = retrievedProductOpt.get();

		// 쿠키에 조회 (개발자 확인용)
	    if (!manageProductViewHistory(request, response, retrievedProduct.getProductNum())) {
	    	log.error("로그: [오류] infoProduct - 조회 기록 쿠키 추가 실패!");
	    } else {
	    	log.info("로그: infoProduct - 조회 기록 쿠키 추가 성공");
	    }

		// 데이터 전달
		model.addAttribute("product", retrievedProduct);
		// 데이터 확인
		log.info("로그: infoProduct - 전송된 상품 데이터 [{}]", retrievedProduct);

		log.info("로그: /infoProduct.do infoProduct - 종료 및 화면 이동");
		return "productDetail"; // "productDetail" 뷰 이름 반환
	}
	
	//(infoProduct.do) 쿠키에 조회 기록 추가하는 함수
	private boolean manageProductViewHistory(HttpServletRequest request, HttpServletResponse response, int productNum) {
	    Cookie[] cookies = request.getCookies(); // 모든 쿠키 가져오기
	    // 쿠키에 productNum 추가 (기존 조회 목록에 추가하는 방식)
	    return CookieUtil.cookieAddNewData(response, COOKIE_NAME, cookies, productNum);
	}

	// 상품 검색
	@RequestMapping("/loadListProduct.do")
	public String loadListProduct(
	        HttpServletResponse response, 
	        Model model, 
	        ProductDTO productDTO,
	        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
	        @RequestParam(value = "minPrice", required = false, defaultValue = "0") int minPrice,
	        @RequestParam(value = "maxPrice", required = false, defaultValue = "0") int maxPrice,
	        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
	        @RequestParam(value = "searchCondition", required = false, defaultValue = "") String searchCondition,
	        @RequestParam(value = "categoryNum", required = false, defaultValue = "0") int categoryNum,  
	        HttpServletRequest request) {

		log.info("log: /loadListProduct.do loadListProduct - start");

		// 시작 시점에 뷰에서 받는 값 로그 찍기
	    log.info("log: Received parameters - page: [{}], minPrice: [{}], maxPrice: [{}], keyword: [{}], searchCondition: [{}], categoryNum: [{}]", 
	            page, minPrice, maxPrice, keyword, searchCondition, categoryNum);
	    
		// 카테고리 목록 조회
	    ArrayList<ProductCateDTO> productCategory; //카테고리 목록
		productCategory = productCateService.selectAll(null); // 전체 카테고리 목록 조회
		
		//(C->V) 카테고리 목록
		model.addAttribute("productCategory", productCategory); // 카테고리 목록
	    
	    // 필터 리스트 생성 및 설정
		HashMap<String, String> filterList = new HashMap<>(); // 필터 검색용
	    // 카테고리 번호 필터 추가
	    if (categoryNum > 0) {
	        filterList.put("SELECT_PART_CATEGORY", String.valueOf(categoryNum));
	    }
	    // 최소 및 최대 가격 필터 추가
	    if (minPrice > 0) {
	        filterList.put("SELECT_PART_PRICE_MIN", String.valueOf(minPrice));
	    }
	    if (maxPrice > 0) {
	        filterList.put("SELECT_PART_PRICE_MAX", String.valueOf(maxPrice));
	    }

	    // searchCondition에 따라 keyword 필터 추가
	    if (!keyword.isEmpty()) {
	        if ("SELECT_PART_NAME".equals(searchCondition)) {
	            filterList.put("SELECT_PART_NAME", keyword);
	        } else if ("SELECT_PART_TITLE".equals(searchCondition)) {
	            filterList.put("SELECT_PART_TITLE", keyword);
	        }
	    }
	    productDTO.setFilterList(filterList);
	    
	    // 페이지네이션을 위한 총 데이터 정보
	    productDTO.setCondition("FILTER_CNT");
	    Optional<ProductDTO> countResultOpt = productService.selectOne(productDTO);
	    int allDataCnt = countResultOpt.map(ProductDTO::getCnt).orElse(0);

	    // PaginationUtils를 사용하여 ProductDTO에 페이지네이션 정보 설정
	    PaginationUtils.setPagination(page, PAGE_SIZE, allDataCnt, productDTO);
	    int totalPages = PaginationUtils.calTotalPages(allDataCnt, PAGE_SIZE);

	    // 필터검색용 데이터 조회
	    List<ProductDTO> productList = productService.selectAll(productDTO);
	    
	    //(C->V 필터링 상품 리스트)
	    model.addAttribute("productList", productList); // 페이지 별 상품 리스트
	    
	    //(C->V 검색어 유지 데이터)
	    model.addAttribute("minPrice", minPrice);
	    model.addAttribute("maxPrice", maxPrice);
	    model.addAttribute("keyword", keyword); // 검색 입력값
	    model.addAttribute("searchCondition", searchCondition); // 제목-상품명 검색 컨디션
	    model.addAttribute("page", page); //현재 페이지
	    model.addAttribute("pageCount", totalPages); // 갯수

	    // 최신상품 목록/ 추천상품 목록 생성 메서드
	    Map<String, Object> resultMap = handleViewedProductsCookie(request, response, productDTO);
	    
	    //(C->V 최근/추천 쿠키 상품) 
	    model.addAttribute("resentProduct", resultMap.get("resentProduct")); // 최근에 본 상품
	    model.addAttribute("recommProduct", resultMap.get("recommendedProduct")); // 추천 상품
	    

	    // 확인
	    log.info("log: listProduct - send resentProducts : [{}]", resultMap.get("resentProduct"));
	    log.info("log: listProduct - send recommendedProducts : [{}]", resultMap.get("recommendedProduct"));
	    log.info("log: listProduct - send productCategories : [{}]", productCategory);
	    log.info("log: listProduct - send productList : [{}]", productList);
	    log.info("log: listProduct - send keyword : [{}]", keyword);
	    log.info("log: listProduct - send minPrice : [{}]", minPrice);
	    log.info("log: listProduct - send maxPrice : [{}]", maxPrice);
	    log.info("log: listProduct - send keyword : [{}]", keyword);
	    log.info("log: listProduct - send page : [{}]", page);
	    log.info("log: listProduct - send pageCount : [{}]", totalPages);

	    log.info("log: /listProduct.do loadListProduct - end / forward");
	    return "productList";
	}
	
	
	// ("loadListProduct.do") 최신상품 추천상품 관련 메서드
	private Map<String, Object> handleViewedProductsCookie(HttpServletRequest request, HttpServletResponse response, ProductDTO productDTO) {
		ArrayList<ProductDTO> resentProduct = new ArrayList<>(); // 최근에 본 상품 목록 리스트 저장용
	    List<ProductDTO> recommendedProduct = null; // 추천 상품 목록 저장용
	    ArrayList<String> deleteList = new ArrayList<>(); // 쿠키 삭제용 리스트
	    List<String> viewedProductList; // 상품 조회용
	    Map<Integer, Integer> categoryCount = new HashMap<>(); // 카테고리별 상품 수 계산용

	    // 쿠키 가져와서 디코딩
	    Cookie[] cookies = request.getCookies();
	    Cookie cookie = CookieUtil.cookieData(cookies, COOKIE_NAME);

	    // 쿠키 값 디코딩
	    String viewedProducts = (cookie != null) ? URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8) : "";
	    log.info("log: loadListProduct - viewedProducts : [{}]", viewedProducts);

	    if (!viewedProducts.isEmpty()) {
	        viewedProductList = Arrays.asList(viewedProducts.split(","));
	        log.info("log: loadListProduct - viewedProductList : [{}]", viewedProductList);

	        // 쿠키에서 가져온 상품 ID로 상품을 조회하고 카테고리별로 개수 카운팅
	        for (String pNum : viewedProductList) {
	            productDTO.setCondition("MD_ONE");
	            productDTO.setProductNum(Integer.parseInt(pNum));
	            Optional<ProductDTO> productOpt = productService.selectOne(productDTO); // 상품 정보 조회
	            log.info("log: loadListProduct - viewedProductList for product : [{}]", productOpt);

	            if (productOpt.isEmpty()) {
	                log.error("log: loadListProduct - deleteList.add : [{}]", pNum);
	                deleteList.add(pNum);
	                continue;
	            }

	            productOpt.ifPresent(product -> {
	                resentProduct.add(product); // 최근에 본 상품 추가
	                log.info("log: loadListProduct - resentProducts.add");

	                // 카테고리별 상품 수 계산
	                int categoryNum = product.getProductCategoryNum();
	                categoryCount.put(categoryNum, categoryCount.getOrDefault(categoryNum, 0) + 1);
	            });
	        }

	        // 쿠키에서 존재하지 않는 상품 제거
	        if (CookieUtil.cookieDataDelete(response, cookie, deleteList)) {
	            log.error("log: [ERROR] loadListProduct - delete history Cookie fail!");
	        }

	        // 가장 많이 본 카테고리의 추천 상품 조회
	        Optional<Map.Entry<Integer, Integer>> mostViewedCategoryOpt = categoryCount.entrySet().stream()
	                .max(Map.Entry.comparingByValue());

	        if (mostViewedCategoryOpt.isPresent()) {
	            int mostViewedCategory = mostViewedCategoryOpt.get().getKey();
	            HashMap<String, String> filterList = new HashMap<>();
	            filterList.put("SELECT_PART_CATEGORY", String.valueOf(mostViewedCategory));

	            productDTO.setFilterList(filterList);
	            productDTO.setStartNum(1);
	            productDTO.setEndNum(10);

	            recommendedProduct = productService.selectAll(productDTO);
	            log.info("log: loadListProduct - mostViewedCategory : [{}]", mostViewedCategory);
	            log.info("log: loadListProduct - recommendedProduct retrieved successfully");
	        }
	    } else {
	        log.warn("log: loadListProduct - 쿠키가 존재하지 않습니다. 기본값으로 초기화합니다.");
	        // 쿠키가 없을 경우 기본 동작을 수행하도록 설정 (예: 빈 목록, 기본값 등)
	        viewedProductList = new ArrayList<>();
	    }

	    // 결과를 Map으로 반환하여 필요한 데이터만 전달
	    Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("resentProduct", resentProduct);
	    resultMap.put("recommendedProduct", recommendedProduct);

	    return resultMap;
	}

}