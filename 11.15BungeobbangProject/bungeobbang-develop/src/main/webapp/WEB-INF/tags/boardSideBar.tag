<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="boardSideBar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<link rel="stylesheet"
	href="${path}/resources/assets/css/member/myPageSidebar.css">

<br>
<div class="sidebar">
	<a href="loadListMyBoard.do" class="btn myPageBtn" role="button"
		id="my-write-board">작성한 게시글</a>
	<form action="loadListPayment.do" method="POST">
		<button type="submit" class="btn myPageBtn">포인트 구매내역</button>
	</form>
	<form action="loadListPoint.do" method="POST">
		<button type="submit" class="btn myPageBtn">포인트 사용내역</button>
	</form>
</div>

<script>
	// 이전 너비 저장
	let preInnerWidth = window.innerWidth;
	console.log('화면 넓이 : ' + preInnerWidth);

	// 화면 크기에 따라 사이드바의 위치를 조정하는 함수
	function sidebarChange() {
		const sidebar = document.querySelector('.sidebar');
		const rowSidebar = document.querySelector('.row-sidebar');

		if (window.innerWidth <= 800) {
			console.log('화면이 800 이하가 됨');
			if (sidebar) {
				sidebar.classList.add('row', 'row-sidebar');
				sidebar.classList.remove('sidebar');
			}
		} else {
			console.log('화면이 800 초과가 됨');
			if (rowSidebar) {
				rowSidebar.classList.remove('row', 'row-sidebar');
				rowSidebar.classList.add('sidebar');
			}
		}
	}

	// 화면 크기 감지 시
	window.addEventListener('resize', () => {
		// 현재 화면 크기 받아옴
		const currentInnerWidth = window.innerWidth;

		// 상태가 변화할 때만 사이드바 변환 함수 호출
		if ((currentInnerWidth <= 800 && preInnerWidth > 800) || (currentInnerWidth > 800 && preInnerWidth <= 800)) {
			sidebarChange();
		}

		// 현재 너비를 이전 너비로 저장
		preInnerWidth = currentInnerWidth;
		console.log('화면 넓이 : ' + preInnerWidth);
	});

	// 페이지 로드 시 함수 호출
	window.addEventListener('load', sidebarChange);
</script>
