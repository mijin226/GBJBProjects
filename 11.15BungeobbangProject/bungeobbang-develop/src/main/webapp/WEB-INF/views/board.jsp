<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.ArrayList, java.util.HashMap, java.util.Map, java.util.List"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<!-- css 변경을 위한 캐시 방지 -->
<meta http-equiv="Cache-Control"
	content="no-cache, no-store, must-revalidate">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Expires" content="0">
<!-- 파비콘 -->
<link rel="icon" href="${path}/resources/assets/images/logo.png"
	type="image/x-icon" />

<meta charset="UTF-8">
<title>게시글 상세</title>
<link rel="stylesheet" href="${path}/resources/assets/css/main.css">
<link rel="stylesheet"
	href="${path}/resources/assets/css/board/board.css">
<link rel="stylesheet"
	href="${path}/resources/assets/css/board/reply.css">
<link rel="stylesheet"
	href="${path}/resources/assets/css/pagination.css">

<!-- bootstrap -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous">
	
<!-- bootstrap icon 사용 -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
</head>
<body>
	<div id="page-wrapper">
		<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
		<script src="${path}/resources/assets/js/board/board.js"></script>
		<script src="${path}/resources/assets/js/board/replyList.js"></script>

		<div class="container">
			<custom:header />
			<div class="row justify-content-center">
				<div class="col-12 col-md-10">

					<!-- 두 번째 행 : 게시글 정보 -->
					<div class="row">
						<div class="col-12">
							<br> <br>
							<button id="back-button" class="btn button-yello"
							onclick="location.href='<%= request.getHeader("Referer") %>'">게시판 돌아가기</button>
							<table class="customTable">
								<thead>
									<tr>
										<th class="tableTitle">제목</th>
										<th class="tableTitleValue">${board.boardTitle}</th>

										<!-- 좋아요 수 -->
										<th class="tableLike">
											<button class="${userLiked eq true ? 'like-true' : 'like-false'}" 
												data-board-id="${board.boardNum}" data-user-pk="${userPK}" id="likeBtn">
												<i class="bi bi-hand-thumbs-up-fill"></i>&nbsp; 좋아요 &nbsp; 
												<span id="likeCount">${board.likeCnt}</span>
											</button>
										</th>

										<!-- 작성자명 -->
										<th class="tableWriter">${board.memberNickname}<input
											type="hidden" id="memberNum" name="memberNum"
											value="${board.memberNum}"> <input type="hidden"
											id="boardNum" name="boardNum" value="${board.boardNum}">
										</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td colspan="4" align="left" id="boardContent">${board.boardContent}</td>
									<tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="button-container col-12 d-flex justify-content-end">
						<!-- 작성자가 지금 로그인한 유저라면 게시물수정버튼 생성/아니라면 숨김-->
						<c:if test="${board.memberNum eq userPK}">
							<div id="edit-button-container" style="display: inline;">
								<br> <a href="updateBoard.do?boardNum=${board.boardNum}"
									class="btn button-orange" role="button">수정 </a>
								<button type="button" id="deleteButton" class="btn btn-danger"
									role="button">삭제</button>
							</div>
						</c:if>
					</div>
					<!-- 세 번째 행 : 댓글 -->
					<div class="row">
						<div class="col-12">
							<div class="replyTop">
								<br> <br> <br>
								<div class="row">
									<div class="col-12 col-md-2" id="replyTitle">댓글쓰기</div>
									<div class="col-0 col-md-7"></div>
									<div class="col-12 col-md-3" id="replyCnt">댓글
										${replyCnt}개</div>
								</div>
								<hr>
							</div>
							<div class="replyMid">
								<!-- 댓글 등록 -->
								<div class="profile">
									<span id="userNickname"><strong>${userNickname}</strong></span>
								</div>
								<div class="replyInput">
									<input type="hidden" id="replyMemberNum" name="memberNum"
										value="${userPK}">
									<!-- 댓글 입력 창 :replyContent로 form데이터 전송-->
									<textarea id="myReplyContent" name="replyContent"
										placeholder="댓글을 입력해주세요" required>${replyList.replyContent}</textarea>
								</div>
								<!-- 댓글 작성 버튼 -->
								<div class="replyButtom">
									<c:choose>
										<c:when test="${empty userPK}">
											<%--로그인 상태가 아니라면 로그인으로 이동--%>
											<button class="btn button-orange" type="button"
												onclick="location.href='login.do'">댓글작성</button>
										</c:when>
										<c:otherwise>
											<%--로그인 상태라면 댓글 작성 진행--%>
											<button class="btn button-orange" type="button"
												id="insertReply" name="insertReply">댓글작성</button>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div id="replyList" class="replyList">
								<%--댓글이 들어갈 공간 / 비동기--%>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<form action="deleteBoard.do" id="deleteForm" method="POST">
			<input type="hidden" name="boardNum" value="${board.boardNum}">
		</form>
		<custom:footer />
	</div>
</body>
</html>