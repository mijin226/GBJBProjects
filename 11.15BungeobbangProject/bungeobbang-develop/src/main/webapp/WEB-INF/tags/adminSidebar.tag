<%@ tag language="java" pageEncoding="UTF-8"%>
<div class="sidebar" data-background-color="dark">
	<!-- 어두운 배경의 사이드바 -->
	<!-- 사이드바의 로고 -->
	<div class="sidebar-logo">
		<!-- Logo Header -->
		<div class="logo-header" data-background-color="dark">
			<!-- 로고 헤더 -->
			<a href="main.do" class="logo"> <!-- 메인 페이지로 이동하는 링크 --> <img
				src="resources/assets/images/logo_name.png" alt="navbar brand"
				class="navbar-brand" height="40" /> <!-- 로고 이미지 -->
			</a>
			<!-- 로고 옆에 사이드바 줄이는 버튼 -->
			<div class="nav-toggle">
				<button class="btn btn-toggle toggle-sidebar">
					<!-- 사이드바 토글 버튼 -->
					<i class="gg-menu-right"></i>
					<!-- 아이콘 -->
				</button>
			</div>
			<button class="topbar-toggler more">
				<!-- 추가 옵션 버튼 -->
				<i class="gg-more-vertical-alt"></i>
				<!-- 아이콘 -->
			</button>
		</div>
		<!-- 로고헤더 끝 -->
	</div>
	<!-- 사이드바 내용 -->
	<div class="sidebar-wrapper scrollbar scrollbar-inner">
		<!-- 스크롤 가능한 사이드바 -->
		<div class="sidebar-content">
			<ul class="nav nav-secondary">
				<!-- 사이드바 내비게이션 메뉴 -->
				<li class="nav-item">
					<!-- 네비게이션 항목 --> <a data-bs-toggle="collapse" href="#dashboard"
					class="collapsed" aria-expanded="false"> <!-- 대시보드 토글 --> <i
						class="fas fa-store"></i> <!-- 집모양 아이콘 -->
						<p>제보받은 가게</p> <span class="caret"></span> <!-- ▼ 아이콘 -->
				</a>
					<div class="collapse" id="dashboard">
						<!-- 가게 제보 하위 목록 -->
						<ul class="nav nav-collapse">
							<li><a href="loadListStoreTipOff.do"> <span
									class="sub-item">가게 등록 제보</span>
							</a></li>
							<li><a href="loadListStoreReport.do"> <span
									class="sub-item">가게 폐점 제보</span>
							</a></li>
						</ul>
					</div>
				</li>
				<!-- 가게 정보 등록 -->
				<li class="nav-item"><a href="addStore.do?condition=adminStoreRegister"> <!-- 가게 등록 페이지 링크 -->
						<i class="fas fa-plus-square"></i> <!-- + 아이콘 -->
						<p>가게 등록</p>
				</a></li>
				<!-- 주문받은 상품 -->
				<li class="nav-item"><a href="loadListOrder.do"> <!-- 주문받은 상품 페이지 링크 -->
						<i class="fas fa-receipt"></i> <!-- 문서 아이콘 -->
						<p>주문받은 상품</p>
				</a></li>
				<!-- 갈빵질빵 페이지 -->
				<li class="nav-item"><a href="main.do"> <!-- 커뮤니티 사이트 이동 -->
						<i class="fas fa-store"></i> <!--집모양 아이콘-->
						<p>갈빵질빵 페이지로</p>
				</a></li>
			</ul>
		</div>
	</div>
</div>
<!-- End Sidebar -->