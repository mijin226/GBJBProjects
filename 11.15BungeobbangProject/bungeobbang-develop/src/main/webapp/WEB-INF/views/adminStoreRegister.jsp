<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>
<html>
<head>
<style>
</style>
<title>가게 등록 페이지</title>
<meta content="width=device-width, initial-scale=1.0, shrink-to-fit=no"
	name="viewport" />
	
<!-- 파비콘 -->
<link rel="icon" href="${path}/resources/assets/images/logo.png"
	type="image/x-icon" />

<!-- Core JS Files -->
<script src="${path}/resources/assets/js/core/jquery-3.7.1.min.js"></script>

<!-- JQuery 라이브러리 -->
<script src="${path}/resources/assets/js/core/popper.min.js"></script>

<!-- JQuery Scrollbar -->
<script	src="${path}/resources/assets/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"></script>

<!-- Popper.js (툴팁 및 팝오버를 위한 라이브러리) -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<!-- Datatables -->
<script	src="${path}/resources/assets/js/plugin/datatables/datatables.min.js"></script>

<!-- Kaiadmin JS -->
<script src="${path}/resources/assets/js/kaiadmin.js"></script>

<!-- Fonts and icons -->
<script src="${path}/resources/assets/js/plugin/webfont/webfont.min.js"></script>

<!-- 등록 js 파일 -->
<script src="${path}/resources/assets/js/store/storeRegister.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>

<!-- 스위트알랏 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<!-- 다음 주소 api -->
<script
	src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="${path}/resources/assets/js/daumPostCode.js"></script>

<script>
	WebFont.load({
		google : {
			families : [ "Public Sans:300,400,500,600,700" ]
		},
		custom : {
			families : [ "Font Awesome 5 Solid", "Font Awesome 5 Regular",
					"Font Awesome 5 Brands", "simple-line-icons", ],
			urls : [ "${path}/resources/assets/css/fonts.min.css" ],
		},
		active : function() {
			sessionStorage.fonts = true;
		},
	});
</script>

<!-- CSS -->
<link rel="stylesheet" href="${path}/resources/assets/css/bootstrap.min.css" />
<link rel="stylesheet" href="${path}/resources/assets/css/plugins.min.css" />
<link rel="stylesheet" href="${path}/resources/assets/css/kaiadmin.min.css" />
<link rel="stylesheet" href="${path}/resources/assets/css/store/storeRegister.css">
</head>

<body>
	<div class="wrapper">
		<!-- 전체 페이지를 감싸는 wrapper -->
		<!-- Sidebar -->
		<custom:adminSidebar />
		<!-- End Sidebar -->
		<!-- 페이지의 메인 부분 -->
		<div class="main-panel">
			<div class="container">
				<div class="page-inner">
					<!-- 페이지 내부 -->
					<div class="page-header">
						<h3 class="fw-bold mb-3">가게 등록</h3>
						<!-- 페이지 제목 -->
						<ul class="breadcrumbs mb-3">
							<!-- 관리자 페이지 내의 페이지 경로 -->
							<!-- 첫 번째 경로 -->
							<li class="nav-home">
								<!-- 홈 링크 --> <a href="#"> <i class="icon-home"></i> <!--홈 아이콘-->
							</a>
							</li>
							<li class="separator"><i class="icon-arrow-right"></i> <!-- > 아이콘 --></li>
							<!-- 두 번째 경로 -->
							<li class="nav-item"><a href="#">가게 등록</a></li>
						</ul>
					</div>
					<!-- 메인 작성 부분-->
					<div class="row">
						<div class="col-md-12">
							<!-- 하얀 배경 부분-->
							<div class="card">
								<!-- 카드 내용 부분 시작-->
								<div class="card-body">
									<br>

									<form id="storeRegister" action="addStore.do" method="POST">
										<input type="hidden" name="storeSecret" value="N"> 
										<input type="hidden" name="storeClosed" value="N">

										<!--1. 상호명 입력란-->
										<div class="row">
											<section>
												<h3>
													<b>가게명<span class="import"> *</span></b>
												</h3>
												<div>
													<input type="text" name="storeName" required placeholder="가게 이름을 입력해주세요."></input>
												</div>
											</section>
										</div>

										<!--2. 전화번호 입력란-->
										<div class="row">
											<section>
												<h3>
													<b>전화번호(선택)</b>
												</h3>
												<div>
													<input type="tel" name="storeContact" id="phone" placeholder="전화번호를 입력해주세요."></input>
												</div>
											</section>
										</div>

										<!--3. 주소 입력란-->
										<div class="row">
											<section>
												<div id="addressLayer" style="display: none; position: absolute; overflow: hidden; z-index: 1; -webkit-overflow-scrolling: touch;">
													<img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnCloseLayer"
														style="cursor: pointer; position: absolute; right: -3px; top: -3px; z-index: 1"
														onclick="closeDaumPostcode()" alt="닫기 버튼">
												</div>
												<!--1행 주소검색 -->
												<div class="containerFluid d-flex justify-content-between">
													<!-- 컨테이너로 좌우 정렬 -->
													<h3 class="pull-left">
														<b>주소 입력<span class="import"> *</span></b>
													</h3>
													<div class="copyright"></div>
													<div>
														<button type="button" class="btn btn-black" id="addressSearchBtn"
															onclick="sample2_execDaumPostcode()">주소 검색</button>
													</div>
												</div>
												<!--2행 주소검색데이터 불러오기-->
												<div>
													<input type="text" name="storeAddress" id="addressMain"
														placeholder="주소검색버튼을 눌러주세요." required readonly></input> <br>
													<br> 
													<input type="text" id="postcode" required 
														placeholder="우편번호 입력" readonly></input> 
													<br>
													<br>
													<input type="text" name="storeAddressDetail"
														id="addressDetail" required placeholder="상세주소 입력"></input>
												</div>
											</section>
										</div>

										<!--메뉴, 결제방식 선택란-->
										<div class="row">
											<!--4. 메뉴 선택란-->
											<div class="col-6">
												<section>
													<h3>
														<b>메뉴 선택<span class="import"> *</span></b>
													</h3>
													<div class="col-12 col-md-6">
														<table class="menuTable" id="menuTable">
															<tr>
																<td class=menuTd>
																	<input type="checkbox" name="storeMenuNormal" id="normal" value="Y" data-role="businessMenusGroup">팥/슈크림 
																	<input type="hidden" name="storeMenuNormal"	id="nonChecked-normal" value="N"></td>
																<td class=menuTd>
																	<input type="checkbox"	name="storeMenuIce" id="ice" value="Y" data-role="businessMenusGroup">아이스크림/초코 
																	<input type="hidden" name="storeMenuIce" id="nonChecked-ice" value="N"></td>
															</tr>
															<tr>
																<td>
																	<input type="checkbox" name="storeMenuVeg" id="veg" value="Y" data-role="businessMenusGroup">야채/김치/만두
																	<input type="hidden" name="storeMenuVeg" id="nonChecked-veg" value="N"></td>
																<td><input type="checkbox" name="storeMenuCheese" id="cheese" value="Y" data-role="businessMenusGroup">치즈
																	<input type="hidden" name="storeMenuCheese" id="nonChecked-cheese" value="N"></td>
															</tr>
															<tr>
																<td>
																	<input type="checkbox" name="storeMenuMini" id="mini" value="Y" data-role="businessMenusGroup">미니 붕어빵 <input type="hidden" name="storeMenuMini" id="nonChecked-mini" value="N"></td>
																<td><input type="checkbox" name="storeMenuPastry" id="pastry" value="Y" data-role="businessMenusGroup">패스츄리
																	<input type="hidden" name="storeMenuPastry"	id="nonChecked-pastry" value="N"></td>
															</tr>
															<tr>
																<td><input type="checkbox" name="storeMenuPotato"
																	id="potato" value="Y" data-role="businessMenusGroup">고구마
																	<input type="hidden" name="storeMenuPotato"
																	id="nonChecked-potato" value="N"></td>
																<td><input type="checkbox" name="storeMenuOther"
																	id="other" value="Y" data-role="businessMenusGroup">기타
																	<input type="hidden" name="storeMenuOther"
																	id="nonChecked-other" value="N"></td>
															</tr>
														</table>
													</div>
												</section>
											</div>
											<!--5. 결제방식 선택란-->
											<div class="col-12 col-md-6">
												<section>
													<h3>
														<b>결제방법 선택<span class="import"> *</span></b>
													</h3>
													<table class="paymentTable" id="paymentTable">
														<tr>
															<td><input type="checkbox"
																name="storePaymentCashmoney" id="cash" value="Y"
																data-role="businessPaymentsGroup">현금결제 <input
																type="hidden" name="storePaymentCashmoney"
																id="nonChecked-cash" value="N"></td>
														</tr>
														<tr>
															<td><input type="checkbox" name="storePaymentCard"
																id="card" value="Y" data-role="businessPaymentsGroup">카드결제
																<input type="hidden" name="storePaymentCard"
																id="nonChecked-card" value="N"></td>
														</tr>
														<tr>
															<td><input type="checkbox"
																name="storePaymentAccount" id="account" value="Y"
																data-role="businessPaymentsGroup">계좌이체 <input
																type="hidden" name="storePaymentAccount"
																id="nonChecked-account" value="N"></td>
														</tr>
													</table>
												</section>
											</div>
										</div>

										<!--6. 영업요일-->
										<div class="row">
											<section>
												<h3>
													<b>요일 선택<span class="import"> *</span></b>
												</h3>
												<table id="weekTable">
													<tr>
														<td><input type="checkbox" name="workWeek" id="Mon"
															value="MON" data-role="businessDaysGroup">월요일</td>
														<td><input type="time" id="startTime-Mon"
															name="workStartTime" disabled></td>
														<td><input type="time" id="endTime-Mon"
															name="workEndTime" disabled></td>
													</tr>
													<tr>
														<td><input type="checkbox" name="workWeek" id="Tue"
															value="TUE" data-role="businessDaysGroup">화요일</td>
														<td><input type="time" id="startTime-Tue"
															name="workStartTime" disabled></td>
														<td><input type="time" id="endTime-Tue"
															name="workEndTime" disabled></td>
													</tr>
													<tr>
														<td><input type="checkbox" name="workWeek" id="Wed"
															value="WED" data-role="businessDaysGroup">수요일</td>
														<td><input type="time" id="startTime-Wed"
															name="workStartTime" disabled></td>
														<td><input type="time" id="endTime-Wed"
															name="workEndTime" disabled></td>
													</tr>
													<tr>
														<td><input type="checkbox" name="workWeek" id="Thu"
															value="THU" data-role="businessDaysGroup">목요일</td>
														<td><input type="time" id="startTime-Thu"
															name="workStartTime" disabled></td>
														<td><input type="time" id="endTime-Thu"
															name="workEndTime" disabled></td>
													</tr>
													<tr>
														<td><input type="checkbox" name="workWeek" id="Fri"
															value="FRI" data-role="businessDaysGroup">금요일</td>
														<td><input type="time" id="startTime-Fri"
															name="workStartTime" disabled></td>
														<td><input type="time" id="endTime-Fri"
															name="workEndTime" disabled></td>
													</tr>
													<tr>
														<td><input type="checkbox" name="workWeek" id="Sat"
															value="SAT" data-role="businessDaysGroup">토요일</td>
														<td><input type="time" id="startTime-Sat"
															name="workStartTime" disabled></td>
														<td><input type="time" id="endTime-Sat"
															name="workEndTime" disabled></td>
													</tr>
													<tr>
														<td><input type="checkbox" name="workWeek" id="Sun"
															value="SUN" data-role="businessDaysGroup">일요일</td>
														<td><input type="time" id="startTime-Sun"
															name="workStartTime" disabled></td>
														<td><input type="time" id="endTime-Sun"
															name="workEndTime" disabled></td>
													</tr>
												</table>
											</section>
										</div>
										<div class="row">
											<input type="submit" value="등록하기">
										</div>
									</form>
								</div>
								<!--카드 내용 부분 끝-->
							</div>
						</div>
					</div>
				</div>
			</div>
        <custom:adminfooter/>
		</div>
		<!-- 메인 패널 종료 -->
	</div>
	<!-- 전체 wrapper 종료 -->	
</body>

</html>