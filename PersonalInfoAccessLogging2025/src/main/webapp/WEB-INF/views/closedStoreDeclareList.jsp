<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>가게 폐점 제보 페이지</title>
    <meta content="width=device-width, initial-scale=1.0, shrink-to-fit=no"
          name="viewport"/>
          
    <!-- 파비콘 -->
    <link rel="icon" href="${path}/resources/assets/images/logo.png"
          type="image/x-icon"/>

	<!-- JQeury -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

	<!-- 스위트알랏 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.7.32/dist/sweetalert2.all.min.js"></script>

    <!-- 웹 폰트 및 아이콘 -->
    <script src="${path}/resources/assets/js/plugin/webfont/webfont.min.js"></script>
    <script>
        WebFont.load({
            google: {
                families: ["Public Sans:300,400,500,600,700"]
            },
            custom: {
                families: ["Font Awesome 5 Solid", "Font Awesome 5 Regular",
                    "Font Awesome 5 Brands", "simple-line-icons",],
                urls: ["resources/assets/css/fonts.min.css"],
            },
            active: function () {
                sessionStorage.fonts = true;
            },
        });
    </script>

    <!-- CSS -->
    <link rel="stylesheet" href="${path}/resources/assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${path}/resources/assets/css/plugins.min.css"/>
    <link rel="stylesheet" href="${path}/resources/assets/css/kaiadmin.min.css"/>
    <link rel="stylesheet" href="${path}/resources/assets/css/admin.css">
</head>

<body>
<!-- JavaScript -->
<script src="${path}/resources/assets/js/closedStoreDeclareList.js" defer></script>

<div class="wrapper">
    <!-- 전체 페이지를 감싸는 wrapper -->
    <!-- Sidebar -->
    <custom:adminSidebar/>
    <!-- End Sidebar -->
    <!-- 페이지의 메인 부분 -->
    <div class="main-panel">
        <div class="container">
            <div class="page-inner">
                <!-- 페이지 내부 -->
                <div class="page-header">
                    <h3 class="fw-bold mb-3">가게 폐점 제보</h3>
                    <!-- 페이지 제목 -->
                    <ul class="breadcrumbs mb-3">
                        <!-- 관리자 페이지 내의 페이지 경로 -->
                        <!-- 첫 번째 경로 -->
                        <li class="nav-home">
                            <!-- 홈 링크 --> <a href="loadListOrder.do"> <i
                                class="icon-home"></i> <!--홈 아이콘-->
                        </a>
                        </li>
                        <li class="separator"><i class="icon-arrow-right"></i> <!-- > 아이콘 --></li>
                        <!-- 두 번째 경로 -->
                        <li class="nav-item">제보받은 가게</li>
                        <li class="separator">
                            <!-- > 아이콘 --> <i class="icon-arrow-right"></i>
                        </li>
                        <!-- 세 번재 경로 -->
                        <li class="nav-item"><a href="loadListStoreReport.do">가게
                            폐점 제보</a></li>
                    </ul>
                </div>

                <!-- 메인 작성 부분-->
                <div class="row">
                    <div class="col-md-12">
                        <!-- 하얀 배경 부분-->
                        <div class="card">
                            <!-- 카드 내용 부분 -->
                            <div class="card-body">
                                <div class="table-responsive" style="text-align: center;">
                                    <!-- 테이블을 감싸는 div 추가 -->
                                    <table class="table table-hover">
                                        <!--현재 확인하고 있는 행 확인 위한 호버-->
                                        <thead>
                                        <tr>
                                            <!--1열 : 상품주문내역 정보 카테고리-->
                                            <th scope="col">고유번호</th>
                                            <th scope="col">가게명</th>
                                            <th scope="col">가게 전화번호</th>
                                            <th scope="col">가게 주소</th>
                                            <th scope="col">제보승인 및 제보삭제</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="storeReport" items="${storeReportList}">
                                            <tr>
                                                <!--고유번호-->
                                                <td>${storeReport.storeNum}</td>

                                                <!--가게명-->
                                                <td>${storeReport.storeName}</td>

                                                <!--가게 전화번호-->
                                                <td>${storeReport.storeContact}</td>

                                                <!--가게주소-->
                                                <td>${storeReport.storeAddress} ${storeReport.storeAddressDetail}</td>
                                                <!--승인 -->
                                                <td><!-- 	제보 승인 : 정말 폐점 처리하겠습니까? 확인을 누르면 폐점 상태로 전환됩니다. (확인/취소)
																	제보 삭제 : 폐점 신고된 글을 삭제하겠습니까? 확인을 누르면 신고된 제보는 삭제됩니다.(확인/취소)	-->
                                                    <div class="d-grid gap-2 d-md-block">
                                                        <button class="btn btn-danger" type="button" onclick="addReport(${storeReport.storeNum})"
                                                                id="addReport">제보승인
                                                        </button>
                                                        <button class="btn btn-primary" type="button" onclick="deleteReport(${storeReport.storeNum})"
                                                                id="deleteReport">제보삭제
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
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

<!-- Core JS Files -->
<script src="${path}/resources/assets/js/core/jquery-3.7.1.min.js"></script>
<!-- jQuery 라이브러리 -->
<script src="${path}/resources/assets/js/core/popper.min.js"></script>
<!-- Popper.js (툴팁 및 팝오버를 위한 라이브러리) -->
<script src="${path}/resources/assets/js/core/bootstrap.min.js"></script>
<!-- Bootstrap JavaScript -->

<!-- jQuery Scrollbar -->
<script
        src="${path}/resources/assets/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"></script>
<!-- jQuery 스크롤바 플러그인 -->
<!-- Datatables -->
<script
        src="${path}/resources/assets/js/plugin/datatables/datatables.min.js"></script>
<!-- DataTables 플러그인 -->

<!-- Kaiadmin JS -->
<script src="${path}/resources/assets/js/kaiadmin.js"></script>
</body>

</html>