<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<footer class="footer">
	<!-- 페이지 하단 푸터 -->
	<div class="container-fluid d-flex justify-content-between">
		<!-- 푸터 내용, 플루이드 컨테이너로 좌우 정렬 -->
		<nav class="pull-left">
			<!-- 왼쪽 내비게이션 -->
		</nav>
		<div class="copyright">
			<!-- 저작권 정보 -->
			<img src="${path}/resources/assets/images/favicon.png"> 갈빵질빵
		</div>
		<div>
			<!-- 추가 정보 -->
			붕어빵원정대
			<!-- 배포 정보 -->
		</div>
	</div>
</footer>