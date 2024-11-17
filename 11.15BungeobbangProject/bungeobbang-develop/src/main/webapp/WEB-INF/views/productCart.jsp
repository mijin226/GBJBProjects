<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<c:if test="${not empty userPK}">
	<span id="memberPK" style="display: none;">${userPK}</span>
</c:if>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>장바구니</title>
<!-- 파비콘 -->
<link rel="icon" href="${path}/resources/assets/images/logo.png"
	type="image/x-icon" />

<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="${path}/resources/assets/css/main.css" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
<link rel="stylesheet"
	href="${path}/resources/assets/css/product/productCart.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>

<body>
	<div id="page-wrapper">
		<!-- Header -->
		<custom:header />

		<div class="container mt-5">
			<div class="row">
				<h2 class="text-center">
					<i class="fas fa-shopping-cart me-2"></i>장바구니
				</h2>
			</div>

			<!-- 상단: 선택, 포인트, 충전하기 버튼 -->
			<div class="row cart-header">
				<div class="col-md-6 d-flex align-items-center select-all-container">
					<div class="select-all-container d-flex align-items-center">
						<input type="checkbox" id="selectAll"
							class="form-check-input me-2"> <label for="selectAll"
							class="mb-0 allCheck">전체 선택</label>
					</div>
					<button id="deleteAllBtn" class="btn btn-outline-secondary ms-3"
						style="display: none;">선택된 상품 삭제</button>
				</div>

				<div class="col-md-6 text-end">
				<c:if test="${not empty userPK}">
					<span>현재 포인트 : <strong id="myPoint">${userPoint}</strong>Point</span>
					<button id="chargeButton" class="btn btn-outline-secondary ms-3">충전하기</button>
				</c:if>
				</div>
			</div>

			<!-- 장바구니 아이템들이 렌더링될 영역 -->
			<div class="cart-products"></div>

			<!-- 장바구니 하단: 총 금액 및 구매하기 버튼 -->
			<div class="cart-footer">
				<div class="col-md-6">
					<h5>주문 금액</h5>
				</div>
				<div class="col-md-6 text-end">
					<span class="total-price">0P</span>
					<button id="purchaseButton" class="btn btn-outline-secondary ms-3">선택된 상품 주문하기</button>
				</div>
			</div>

			<!-- 배송 정보 및 주문 완료 섹션 (숨김 처리) -->
			<div class="row order-section mt-5" id="orderSection"
				style="display: none;">
				<!-- 1행 1열: 구매한 상품 목록 -->
				<div class="col-md-6">
					<h5>구매할 상품 목록</h5>
					<div class="order-products mt-3">
						<!-- JavaScript에서 제품 목록을 삽입할 공간 -->
					</div>
				</div>

				<!-- 1행 2열: 주소 입력 및 주문 완료 버튼 -->
				<div class="col-md-6">
			        <!-- 총 가격 표시 -->
			        <div class="total-price-section mb-4">
			            <h5>총 사용 포인트</h5>
			            <span id="orderTotalPrice" class="total-price-value">0원</span>
			        </div>
					<h5>배송 정보</h5>
					<div class="form-group">
						<label for="nameInput"><strong>이름</strong></label> <input
							type="text" id="nameInput" name="name" class="form-control"
							placeholder="이름을 입력하세요" value="${privateName}">
					</div>

					<!-- 주소 입력 섹션 -->
					<div class="form-group">
						<label for="addressSearch"><strong>배송지</strong></label>
						<div class="input-group">
							<input type="button" id="addressSearchButton"
								onclick="sample3_execDaumPostcode()" value="주소 검색"
								class="btn btn-outline-primary mb-2"><br> <input
								type="text" id="postcode" placeholder="우편번호" readonly
								class="form-control mb-2"><br> <input type="text"
								id="addressMain" name="address" value="${mainAddress}"
								placeholder="주소 검색을 진행해주세요" readonly class="form-control mb-2"><br>
							<input type="text" id="addressDetail" name="addressDetail"
								value="${detailAddress}" placeholder="상세 주소를 입력해주세요"
								class="form-control mb-2"><br> <input type="tel"
								id="phoneInput" class="form-control" placeholder="전화번호">
						</div>
					</div>
					
					<!-- 모달 구조 -->
					<div id="fullscreenDaumPostcode" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0, 0, 0, 0.8); z-index:10000;">
					    <div id="addressLayer" style="width:100%; height:100%; background:#fff; margin:auto; overflow:auto; position:relative;"></div>
					    <button id="btnCloseLayer" style="position:absolute; top:10px; right:10px; background:red; color:white; border:none; padding:10px; cursor:pointer;">×</button>
					</div>



					<!-- 주문 완료 버튼 -->
					<div class="text-right mt-3">
						<button id="completeOrderButton" class="btn btn-primary"
							style="display: none;">주문 완료</button>
					</div>
				</div>
			</div>
		</div>

		<!-- Footer -->
		<custom:footer />

		<script>
			// JSON 문자열을 JavaScript에서 사용하기 전에 디코딩합니다.
			const encodedCartItemsString = '${fn:escapeXml(cartItemsJson)}';

			// HTML 엔티티를 일반 문자열로 변환하는 함수
			function decodeHTMLEntities(text) {
				const textarea = document.createElement("textarea");
				textarea.innerHTML = text;
				return textarea.value;
			}

			// 디코딩된 JSON 문자열
			const decodedCartItemsString = decodeHTMLEntities(encodedCartItemsString);

			try {
				// JSON 파싱
				var cartItems = JSON.parse(decodedCartItemsString);
				console.log(cartItems); // 파싱된 JSON 데이터 확인
			} catch (e) {
				console.error('JSON 파싱 에러: ', e);
				console.log('파싱 시도한 문자열: ', decodedCartItemsString);
			}
		</script>
		<!-- 다음주소 API -->
		<script
			src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
		<script
			src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
		<script src="${path}/resources/assets/js/jquery.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
		<script src="${path}/resources/assets/js/product/productCart.js"></script>
		<script src="${path}/resources/assets/js/daumPostCode2.js"></script>
	</div>
</body>

</html>