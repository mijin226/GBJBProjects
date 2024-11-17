<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>주문받은 상품 페이지</title>
    <meta content="width=device-width, initial-scale=1.0, shrink-to-fit=no"
          name="viewport"/>
    <link rel="icon" href="${path}/resources/assets/images/logo.png"
          type="image/x-icon"/>

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

<!--1. 페이지 로드 시, 체크박스의 값이 Y인지 N인지를 구분.
2. Y이라면 체크 표시 및 부모요소 <tr>의 배경색(해당 행) 회색처리
3. N이라면 미체크 표시 및 부모요소 <tr>의 배경색 없음.(기본)
4. 사용자가 미체크 상태에서 체크할 시, 체크상태로 전환
5. 사용자가 체크상태에서 체크할 시, 미체크상태로 전환
2,4 / 3,5세트 (색상 전환 미진행)
-->

<script>
    document.addEventListener('DOMContentLoaded', () => {
        const checkboxes = document.querySelectorAll('input[type="checkbox"]');
        checkboxes.forEach(checkbox => {
            const row = checkbox.closest('tr');
            if (checkbox.checked) {
                row.style.backgroundColor = '#d4edda'; // 초기 체크된 경우의 배경색
            }
        });
    });

    /*	주문 내역 확인 완료(체크박스) 표시 전환
        숨김 처리된 체크박스의 값
        1: 체크완료(처리)로 전환	|	0: 체크취소(미처리)로 전환		*/
    function adminchecking(checkbox, orderNum) {
        // const hiddenInput = document.querySelector('#checkValue');
        // hiddenInput.value = checkbox === 'Y' ? "Y" : "N";
        //colorChecked(checkbox);
        // form태그 제출
        document.getElementById('adminchecking'+orderNum).submit();
    }

    function colorChecked(checkbox) {
        const row = checkbox.closest('tr'); // 체크박스의 부모 요소<tr> 확인
        if (checkbox.checked) {
            row.style.backgroundColor = '#d4edda'; // 체크된 경우의 배경색
        } else {
            row.style.backgroundColor = ''; // 체크 해제된 경우의 기본 배경색
        }
    }
</script>
<div class="wrapper">
    <!-- 전체 페이지를 감싸는 wrapper -->
    <!-- Sidebar -->
    <custom:adminSidebar></custom:adminSidebar>
    <!-- End Sidebar -->
    <!-- 페이지의 메인 부분 -->
    <div class="main-panel">
        <div class="container">
            <div class="page-inner">
                <!-- 페이지 내부 -->
                <div class="page-header">
                    <h3 class="fw-bold mb-3">주문받은 상품 목록</h3>
                    <!-- 페이지 제목 -->
                    <ul class="breadcrumbs mb-3">
                        <!-- 관리자 페이지 내의 페이지 경로 -->
                        <!-- 첫 번째 경로 -->
                        <li class="nav-home"><a href="loadListOrder.do"> <!-- 홈 링크 -->
                            <i class="icon-home"></i> <!-- 홈 아이콘 -->
                        </a></li>
                        <li class="separator"><i class="icon-arrow-right"></i> <!-- > 아이콘 -->
                        </li>
                        <!-- 두 번째 경로 -->
                        <li class="nav-item"><a href="loadListOrder.do">주문받은 상품
                        </a></li>
                    </ul>
                </div>

                <!-- 메인 작성 부분-->
                <div class="row">
                    <div class="col-md-12">
                        <!-- 하얀 배경 부분-->
                        <div class="card">
                            <div class="card-body">
                                <div class="table-responsive" style="text-align: center;">
                                    <!-- 테이블을 감싸는 div 추가 -->

                                        <table class="table table-hover">
                                            <!--현재 확인하고 있는 행 확인 위한 호버-->
                                            <tr>
                                                <!--1열 : 상품주문내역 정보 카테고리-->
                                                <th scope="col">확인여부</th>
                                                <th scope="col">주문번호</th>
                                                <th scope="col">구매일시</th>
                                                <th scope="col">구매회원</th>
                                                <th scope="col">배송주소</th>
                                                <th scope="col">금액</th>
                                            </tr>
                                            <c:forEach var="order" items="${orderList}">
                                                <!-- 만약 체크박스 표시된 상태라면 해당 블록의 배경색을 변경 -->
                                                <tr>
                                                    <!--주문내역 확인 여부-->
                                                    <!-- 1: 주문내역 확인 완료-->
                                                    <!-- 2: 주문내역 미확인 -->
                                                    <td>
                                                        <input class="form-check-input" type="checkbox"
                                                               id="flexCheckDefault"
                                                               name="adminChecked"
                                                               onchange="adminchecking(event, ${order.orderNum})"
                                                        <c:if test="${order.adminChecked == 'Y'}"> checked </c:if>>
                                                        <form id="adminchecking${order.orderNum}" method="post" action="updateOrderCheck.do">
                                                            <input type="hidden" name="orderNum" value="${order.orderNum}">
                                                            <input type="hidden" name="adminChecked" value="${order.adminChecked}">
                                                        </form>
                                                    </td>
                                                    <!--주문번호-->
                                                    <td>${order.orderNum}</td>
                                                    <!--구매일시-->
                                                    <td>${order.orderDate}</td>
                                                    <!--구매회원-->
                                                    <td>${order.memberEmail}</td>
                                                    <!--배송주소-->
                                                    <td>${order.orderAddress}</td>
                                                    <!--금액-->
                                                    <td>${order.totalPrice}</td>
                                                </tr>
                                            </c:forEach>
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
<script src="${path}/resources/assets/js/plugin/jquery-scrollbar/jquery.scrollbar.min.js"></script>
<!-- jQuery 스크롤바 플러그인 -->
<!-- Datatables -->
<script src="${path}/resources/assets/js/plugin/datatables/datatables.min.js"></script>
<!-- DataTables 플러그인 -->

<!-- Kaiadmin JS -->
<script src="${path}/resources/assets/js/kaiadmin.js"></script>
</body>

</html>