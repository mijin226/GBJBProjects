<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:if test="${not empty userPK}">
	<span id="memberPK" style="display: none;">${userPK}</span>
</c:if>
<!DOCTYPE HTML>
<html>
<head>
<title>갈빵질빵 - 상품 목록 및 검색</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />

<!-- 파비콘 -->
<link rel="icon" href="${path}/resources/assets/images/logo.png"
	type="image/x-icon" />

<!-- css -->
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="${path}/resources/assets/css/main.css" />
<link rel="stylesheet"
	href="${path}/resources/assets/css/product/productList.css" />
<link rel="stylesheet"
	href="https://unpkg.com/swiper/swiper-bundle.min.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link
	href="https://fonts.googleapis.com/css2?family=Gamja+Flower&display=swap"
	rel="stylesheet">
<link rel="stylesheet" href="${path}/resources/assets/css/searchbar.css">
<link rel="stylesheet" href="${path}/resources/assets/css/pagination.css">
</head>
<body>
	<div id="page-wrapper">
		<!-- 헤더 -->
		<custom:header />
		<div class="container mt-5">
			<div class="d-flex justify-content-end">
				<button id="pointButton" class="btn btn-outline-primary">내
					포인트 보기</button>
			</div>
			<div class="d-flex justify-content-end">
				<div id="pointPopup" class="popup">
					<div class="popup-content">
						<button class="close-popup">&times;</button>
						<p id="pointValue">
							<strong>${userPoint}</strong>Point
						</p>
					</div>
				</div>
			</div>

			<!-- 상품 카테고리 버튼 -->
			<div class="category-buttons">
				<c:forEach var="category" items="${productCategory}">
					<a
						href="loadListProduct.do?categoryNum=${category.productCategoryNum}"
						class="category-link">${category.productCategoryName}</a>
				</c:forEach>
			</div>

			<!-- 검색 섹션 -->
			<form action="loadListProduct.do" method="GET">
				<div class="search-container">
					<input type="hidden" name="searchCondition" id="conditionInput"
						value="SELECT_PART_TITLE">
					<div class="searchInput">
						<i class="fas fa-search"></i> <input type="text"
							class="form-control" id="searchInput"
							placeholder="검색 옵션을 열려면 클릭하세요" readonly>
						<button class="btn btn-dark" id="searchInputBTN" type="submit">
							검색
						</button>
					</div>
					<div class="search-options" id="searchOptions"
						style="display: none;">
						<div class="filter-section mt-3">
							<div class="d-flex justify-content-between align-items-center">
							        <h5 class="mb-0">
							            <i class="fas fa-key"></i> 검색 키워드 입력
							        </h5>
							        <button type="button" class="btn btn-dark btn-sm" onclick="toggleSearchOptions()">X</button>
							    </div>
							    </br>
							<input type="text" class="form-control" id="searchKeywordInput"
								name="keyword" placeholder="검색할 키워드를 입력하세요">

							<h5 class="mt-4">검색 설정</h5>
							<div class="form-check">
								<input class="form-check-input" type="radio"
									name="searchCategory" value="CONTENT" id="searchContent" checked>
								<label class="form-check-label" for="searchContent">상품명</label>
							</div>
							<div class="form-check">
								<input class="form-check-input" type="radio"
									name="searchCategory" value="TITLE" id="searchTitle" >
								<label class="form-check-label" for="searchTitle">제목</label>
							</div>
						</div>

						<div class="price-section mt-4">
							<h5>
								<i class="fas fa-dollar-sign"></i>가격 설정
							</h5>
							<div id="minPriceWarning" style="display: none; color: red;">최소
								가격은 0 이상이어야 합니다.</div>
							<div class="input-group mb-2">
								<div class="input-group-prepend">
									<span class="input-group-text">최소</span>
								</div>
								<input type="number" class="form-control" id="minPrice"
									name="minPrice" placeholder="최소 가격 입력" min="0">
							</div>
							<div id="maxPriceWarning" style="display: none; color: red;">최대
								가격은 최소 가격보다 크거나 같아야 합니다.</div>
							<div class="input-group mb-2">
								<div class="input-group-prepend">
									<span class="input-group-text">최대</span>
								</div>
								<input type="number" class="form-control" id="maxPrice"
									name="maxPrice" placeholder="최대 가격 입력" min="0">
							</div>
						</div>

						<div class="category-section mt-4">
							<h5>
								<i class="fas fa-tags"></i>카테고리 설정
							</h5>
							<select class="form-control" id="categorySelect"
								name="categoryNum">
								<option value="">전체 카테고리</option>
								<c:forEach var="category" items="${productCategory}">
									<option value="${category.productCategoryNum}">${category.productCategoryName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="selected-options mt-3" id="selectedOptions1"></div>
						<!-- 추가된 검색 버튼과 닫기 버튼 -->
						<div class="d-flex justify-content-between mt-4">
						    <button type="submit" class="btn btn-dark ml-auto">검색</button>
						</div>
					</div>
				</div>
			</form>

			<div class="selected-options mt-3" id="selectedOptions2"></div>
			<p>다양한 상품을 검색해보세요!</p>
			<div class="empty-photo-album">
				<div class="photo-album">
					<c:forEach var="index" begin="1" end="16">
						<c:choose>
							<c:when test="${index % 2 == 0}">
								<img
									src="${pageContext.request.contextPath}/resources/assets/images/image${index}.png"
									alt="이미지 ${index}" class="stacked-photo even"
									style="--index: ${index};">
							</c:when>
							<c:otherwise>
								<img
									src="${pageContext.request.contextPath}/resources/assets/images/image${index}.png"
									alt="이미지 ${index}" class="stacked-photo odd"
									style="--index: ${index};">
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</div>

			<!-- 추천 상품 섹션 -->
			<section id="recommend-products-section">
				<div class="container">
					<div class="recommend-products">
						<c:choose>
							<c:when test="${not empty recommProduct}">
								<h2>추천 상품</h2>
								<div class="swiper-container swiper-recommend">
									<div class="swiper-wrapper">
										<c:forEach var="product" items="${recommProduct}">
											<div class="swiper-slide">
												<div class="product-card">
													<div class="product-row">
														<div class="product-image">
															<img src="${product.productProfileWay}" alt="상품 이미지" />
														</div>
														<div class="product-title">
														    <p class="product-name">${product.productName}</p>
														</div>
													</div>
													<div class="product-row">
														<span>${product.productPrice}원</span>
														<button class="btn btn-primary">
															<a href="infoProduct.do?productNum=${product.productNum}"
																class="text-white text-decoration-none">상품 보러 가기</a>
														</button>
													</div>
												</div>
											</div>
										</c:forEach>
									</div>
									<div class="swiper-pagination swiper-recommend-pagination"></div>
								</div>
								<div class="swiper-navigation">
									<button class="swiper-button-prev swiper-recommend-prev"></button>
									<button class="swiper-button-next swiper-recommend-next"></button>
								</div>
							</c:when>
							<c:otherwise>
								<h3>상품을 구경하시면 상품을 추천해드립니다.</h3>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</section>
			

			<!-- 최근에 본 상품 섹션 -->
			<section id="recent-products-section">
				<div class="container">
					<div class="recent-products">
						<c:choose>
							<c:when test="${not empty resentProduct}">
								<h2>최근에 본 상품</h2>
								<div class="swiper-container swiper-recent">
									<div class="swiper-wrapper">
										<c:forEach var="product" items="${resentProduct}">
											<div class="swiper-slide">
												<div class="product-card">
													<div class="product-row">
														<div class="product-image">
															<img src="${product.productProfileWay}" alt="상품 이미지" />
														</div>
													<div class="product-title">
													    <p class="product-name">${product.productName}</p>
													</div>
													</div>
													<div class="product-row">
														<span>${product.productPrice}원</span>
														<button class="btn btn-primary">
															<a href="infoProduct.do?productNum=${product.productNum}"
																class="text-white text-decoration-none">상품 보러 가기</a>
														</button>
													</div>
												</div>
											</div>
										</c:forEach>
									</div>
									<div class="swiper-pagination swiper-recent-pagination"></div>
								</div>
								<div class="swiper-navigation">
									<button class="swiper-button-prev swiper-recent-prev"></button>
									<button class="swiper-button-next swiper-recent-next"></button>
								</div>
							</c:when>
							<c:otherwise>
								<h3>최근에 본 상품이 없습니다.</h3>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</section>
			<!-- 상품 목록 -->
			<section>
				<h3>필터링된 상품 목록</h3>
			    <c:choose>
     			<c:when test="${not empty productList}">
				<c:forEach var="product" items="${productList}">
					<div class="product-item">
						<div class="product-image">
							<c:choose>
								<c:when test="${not empty product.productProfileWay}">
									<a href="infoProduct.do?productNum=${product.productNum}">
										<img src="${product.productProfileWay}"
										alt="${product.productName}" class="thumbnail-image" />
									</a>
								</c:when>
								<c:otherwise>
									<a href="infoProduct.do?productNum=${product.productNum}">
										<img src="${path}/resources/assets/images/default.png"
										alt="${product.productName}" class="thumbnail-image" />
									</a>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="product-info">
							<%--<span>제목: ${product.boardTitle}</span> --%>
					<span><strong>카테고리</strong> <span>${product.productCategoryName}</span></span>
					    <br>
					    <span><strong>상품명</strong> 
					    <a href="infoProduct.do?productNum=${product.productNum}">
							<span class="scrolling-text" title="${product.productName}">
							    <c:choose>
							        <c:when test="${fn:length(product.productName) > 20}">
							            <marquee behavior="scroll" direction="left">${product.productName}</marquee>
							        </c:when>
							        <c:otherwise>
							            ${product.productName}
							        </c:otherwise>
							    </c:choose>
							</span>
				        </a>
					    </span>
					    <br>
					    <span class="glow-effect"><strong>가격</strong> <span>${product.productPrice}원</span></span>
						</div>
					</div>
					<hr>
				</c:forEach>
		        </c:when>
		        <c:otherwise>
		            <p>검색된 상품이 없습니다.</p>
		        </c:otherwise>
		    </c:choose>
		</section>


			<!-- 페이지네이션 -->
			<div class="pagination">
				<c:if test="${page > 1}">
					<a
						href="?page=${page - 1}&categoryNum=${param.categoryNum}&minPrice=${minPrice}&maxPrice=${maxPrice}&searchCondition=${searchCondition}&keyword=${keyword}">
						&laquo; 이전 </a>
				</c:if>

				<c:forEach var="i" begin="1" end="${pageCount}">
					<c:choose>
						<c:when test="${i == page}">
							<strong>${i}</strong>
						</c:when>
						<c:otherwise>
							<a
								href="?page=${i}&categoryNum=${param.categoryNum}&minPrice=${minPrice}&maxPrice=${maxPrice}&searchCondition=${searchCondition}&keyword=${keyword}">
								${i} </a>
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<c:if test="${page < pageCount}">
					<a
						href="?page=${page + 1}&categoryNum=${param.categoryNum}&minPrice=${minPrice}&maxPrice=${maxPrice}&searchCondition=${searchCondition}&keyword=${keyword}">
						다음 &raquo; </a>
				</c:if>
			</div>
		</div>
	</div>
	<!-- 푸터 -->
	<custom:footer />

	<!-- 스크립트 -->
	<script src="${path}/resources/assets/js/jquery.min.js"></script>
	<script src="${path}/resources/assets/js/browser.min.js"></script>
	<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<script src="${path}/resources/assets/js/product/productList.js"></script>
</body>
</html>
