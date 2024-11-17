<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="header"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}" />


<!-- Bootstrap -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<!-- font-awesome -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

<!-- Custom CSS -->
<link rel="stylesheet" href="${path}/resources/assets/css/header.css" />

<!-- GOOGLE FONT -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Gamja+Flower&family=Nanum+Pen+Script&display=swap"
	rel="stylesheet">
<!-- 스위트 알랏 코드 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<!-- Header -->
<div class="header">
	<h1>
		<c:out value="${title}" />
	</h1>
</div>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<div class="container-fluid">
		<!-- 사이드바 토글 버튼 -->
		<button id="toggleSidebar" class="btn btn-outline-secondary ml-auto">
			<i class="fas fa-bars"></i>
		</button>

		<!-- 로고 -->
		<a class="navbar-brand" href="main.do"> <img
			src="${path}/resources/assets/images/logo.png" alt="Logo"
			class="logo-img"> <span class="navbar-brand-text">갈빵질빵</span>
		</a>


		<!-- 네비게이션 메뉴 -->
		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav ml-auto">
				<li class="nav-item"><a class="nav-link" href="main.do">메인화면</a>
				</li>
				<li class="nav-item dropdown"><a class="nav-link" href="#"
					id="navbarDropdown" role="button" data-toggle="dropdown"
					aria-haspopup="true" aria-expanded="false">붕어빵 찾기</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<a href="loadListStore.do" class="dropdown-item">가게 검색</a> <a
							href="loadListStoreMap.do" class="dropdown-item">가게 주소 검색</a>
					</div></li>
				<li class="nav-item"><a class="nav-link"
					href="loadListProduct.do">MD 상품</a></li>
				<li class="nav-item dropdown"><a class="nav-link" href="#"
					id="navbarDropdown" role="button" data-toggle="dropdown"
					aria-haspopup="true" aria-expanded="false"> 게시판 </a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<a href="loadListBoards.do?boardCategoryName=boardList"
							class="dropdown-item">일반 게시판</a> <a
							href="loadListBoards.do?boardCategoryName=noticeBoard"
							class="dropdown-item">문의 게시판</a>
					</div></li>
					<c:if test="${empty userPK}">
					    <li class="nav-item">
					        <a href="#" class="nav-link storeReportLink">가게 제보</a>
					    </li>
					</c:if>
					<c:if test="${not empty userPK}">
					    <li class="nav-item">
					        <a href="addStore.do?condition=storeReport" class="nav-link">가게 제보</a>
					    </li>
					</c:if>

					<li class="nav-item"><a class="nav-link" href="goToCart.do">장바구니</a></li>
				
			

				<!-- 로그인/회원가입 or 프로필 이미지 표시 -->
				<c:choose>
					<c:when test="${empty userPK}">
						<%--                  <li class="nav-item">
                     <a href="infoMypage.do">
                        <img src="${path}/resources/assets/images/default_profile.png" class="rounded-circle" style="width: 40px; height: 40px;" alt="Default Profile">
                     </a>
                  </li>--%>
						<li class="nav-item"><a class="nav-link" href="signupPage.do">회원가입</a>
						</li>
						<li class="nav-item"><a class="nav-link" href="login.do">로그인</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="nav-item"><a href="infoMypage.do"> <img
								src="${path}/uploads/${userProfile}" class="rounded-circle"
								style="width: 40px; height: 40px;" alt="MYPAGE">
						</a></li>
						<li class="nav-item"><a class="nav-link" href="logout.do">로그아웃</a>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
</nav>

<!-- 사이드바 -->
<div id="sidebar" class="bg-light border-right"
	style="left: -250px; position: fixed; top: 0; height: 100%; width: 250px; transition: left 0.3s ease;">
	<div
		class="sidebar-header d-flex align-items-center justify-content-between px-3 py-2">
		<a class="navbar-brand d-flex align-items-center" href="main.do">
			<img src="${path}/resources/assets/images/logo.png" alt="Logo"
			style="width: 40px; height: auto; margin-right: 10px;"> <span
			class="navbar-brand-text">갈빵질빵</span>
		</a>
		<!-- 사이드바 닫기 버튼 -->
		<button id="closeSidebar" class="btn btn-light">
			<i class="fas fa-times"></i>
		</button>
	</div>
	<ul class="navbar-nav">
		<li class="nav-item"><a class="nav-link" href="main.do">메인화면</a>
		</li>
		<li class="nav-item"><a class="nav-link" href="#"
			id="navbarDropdown" role="button" data-toggle="dropdown"
			aria-haspopup="true" aria-expanded="false">붕어빵 찾기</a>
			<div class="dropdown-menu" aria-labelledby="navbarDropdown">
				<a href="loadListStore.do" class="dropdown-item">가게 검색</a> <a
					href="loadListStoreMap.do" class="dropdown-item">가게 주소 검색</a>
			</div></li>
		<li class="nav-item"><a class="nav-link"
			href="loadListProduct.do">MD 상품</a> <a class="nav-link" href="#"
			id="navbarDropdown" role="button" data-toggle="dropdown"
			aria-haspopup="true" aria-expanded="false"> 게시판 </a>
			<div class="dropdown-menu" aria-labelledby="navbarDropdown">
				<a href="loadListBoards.do?boardCategoryName=boardList"
					class="dropdown-item">일반 게시판</a> <a
					href="loadListBoards.do?boardCategoryName=noticeBoard"
					class="dropdown-item">문의 게시판</a>
			</div></li>
				<c:if test="${empty userPK}">
				    <li class="nav-item">
				        <a href="#" class="nav-link storeReportLink">가게 제보</a>
				    </li>
				</c:if>
				<c:if test="${not empty userPK}">
				    <li class="nav-item">
				        <a href="addStore.do?condition=storeReport" class="nav-link">가게 제보</a>
				    </li>
				</c:if>

		<li class="nav-item"><a class="nav-link" href="goToCart.do">장바구니</a>
		</li>
		<c:choose>
			<c:when test="${empty userPK}">
				<li class="nav-item"><a class="nav-link" href="signupPage.do">회원가입</a>
				</li>
				<li class="nav-item"><a class="nav-link" href="login.do">로그인</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="nav-item"><a class="nav-link" href="logout.do">로그아웃</a>
				</li>
			</c:otherwise>
		</c:choose>
	</ul>
</div>

<!-- Optional JavaScript for Bootstrap -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>

<script src="${path}/resources/assets/js/header.js"></script>
<!-- 사이드바 스크립트 -->
<script>
	// 사이드바 토글
	document.getElementById("toggleSidebar").addEventListener("click",
			function() {
				var sidebar = document.getElementById("sidebar");
				var body = document.body;

				// 사이드바가 열려 있으면 닫고, 닫혀 있으면 여는 조건 처리
				if (sidebar.style.left === "0px") {
					sidebar.style.left = "-250px"; // 사이드바 닫기
					body.style.marginLeft = "0"; // 본문 원래 위치로
				} else {
					sidebar.style.left = "0"; // 사이드바 열기
					body.style.marginLeft = "250px"; // 본문을 오른쪽으로 밀기
				}
			});

	// 사이드바 닫기
	document.getElementById("closeSidebar").addEventListener("click",
			function() {
				var sidebar = document.getElementById("sidebar");
				var body = document.body;

				sidebar.style.left = "-250px"; // 사이드바 닫기
				body.style.marginLeft = "0"; // 본문 원래 위치로
			});

	// 화면 크기가 일정 이하로 작아질 때 자동으로 사이드바 열기
	window.addEventListener("resize", function() {
		var sidebar = document.getElementById("sidebar");
		var body = document.body;

		// 화면이 768px 이하로 작아질 때 사이드바 자동 열기
		if (window.innerWidth <= 768) {
			sidebar.style.left = "0"; // 사이드바 열기
			body.style.marginLeft = "250px"; // 본문을 오른쪽으로 밀기
		} else {
			sidebar.style.left = "-250px"; // 사이드바 닫기
			body.style.marginLeft = "0"; // 본문 원래 위치로
		}
	});

	// 페이지가 로드될 때 화면 크기를 확인하고 초기 사이드바 상태 설정
	window.addEventListener("load", function() {
		var sidebar = document.getElementById("sidebar");
		var body = document.body;

		if (window.innerWidth <= 768) {
			sidebar.style.left = "0"; // 사이드바 열기
			body.style.marginLeft = "250px"; // 본문을 오른쪽으로 밀기
		} else {
			sidebar.style.left = "-250px"; // 사이드바 닫기
			body.style.marginLeft = "0"; // 본문 원래 위치로
		}
	});
</script>
