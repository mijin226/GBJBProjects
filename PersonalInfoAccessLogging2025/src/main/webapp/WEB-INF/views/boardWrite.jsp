<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.ArrayList, java.util.HashMap, java.util.Map, java.util.List"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML>

<html>
<head>
<title>게시글 작성</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<!-- 파비콘 -->
<link rel="icon" href="${path}/resources/assets/images/logo.png"
	type="image/x-icon" />

<!-- Bootstrap CSS를 로드하여 레이아웃과 스타일을 추가 -->
<link
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
	rel="stylesheet">
<!-- 페이지에 적용할 커스텀 CSS 파일 로드 -->
<link rel="stylesheet" href="${path}/resources/assets/css/main.css" />
<link rel="stylesheet"
	href="${path}/resources/assets/css/pagination.css">
<link rel="stylesheet"
	href="${path}/resources/assets/css/board/boardWrite.css">
<script>
	// JavaScript 변수로 폴더명을 할당
	const boardFolder = "${data.boardFolder}";
</script>
</head>

<body class="subpage">
	<div id="page-wrapper">

		<!-- Header -->
		<custom:header />
		<!-- 커스텀 헤더 태그로 헤더 구성 요소를 삽입 -->
		<div class="container">
			<!-- Content 영역 시작 -->
			<div class="row mt-5">
				<div class="col-md-3">
					<!-- Sidebar: 게시판 목록을 표시하는 사이드바 -->
					<section>
						<h5 class="list-title">게시판 선택하기</h5>
						<ul class="list-group">
							<li class="list-group-item"><a
								href="loadListBoards.do?boardCategoryName=boardList"
								class="sidebar-links">일반 게시판</a></li>
							<li class="list-group-item"><a
								href="loadListBoards.do?boardCategoryName=noticeBoard"
								class="sidebar-links">문의 게시판</a></li>
						</ul>
					</section>
				</div>
				<div class="col-md-9">
					<!-- Main Content: 게시물 작성 영역 -->
					<section class="content-header">
						<h2 class="content-title">게시물 작성</h2>
						<p class="content-description">게시물을 작성하여 정보를 공유 해주세요!</p>
					</section>

					<section>
						<!-- 게시글 작성 폼 -->
						<form id="postForm" action="addBoard.do" method="post"
							enctype="multipart/form-data" class="mt-4">
							<!-- 작성자 ID를 서버에서 전달받아 숨겨진 필드에 저장 -->
							<input type="hidden" name="memberNum" value="${userPK}">
							<!-- 폴더명을 서버에서 전달받아 숨겨진 필드에 저장 -->
							<input type="hidden" name="boardFolder"
								value="${data.boardFolder}">

							<div class="form-group">
								<!-- 게시판 카테고리 선택 필드 -->
								<label for="boardCateName">일반/문의</label> <select
									id="boardCateName" name="boardCategoryName"
									class="form-control">
									<option value="boardList"
										<c:if test="${boardCategoryName == 'boardList'}">selected</c:if>>일반</option>
									<option value="noticeBoard"
										<c:if test="${boardCategoryName == 'noticeBoard'}">selected</c:if>>문의</option>
								</select>
							</div>

							<div id="postSecretGroup"
								class="form-group custom-checkbox-group">
								<!-- 비공개 게시물 체크박스 필드 -->
								<label class="custom-checkbox"> <input type="checkbox"
									id="postSecret" name="boardOpenCheckbox"> <span
									class="checkmark"></span> <span class="checkbox-label">비공개
										게시물</span>
								</label>
								<!-- 체크박스 상태를 감지할 숨겨진 필드 -->
								<input type="hidden" id="hiddenBoardOpen" name="boardOpen"
									value="Y">
							</div>

							<div class="form-group mt-3">
								<!-- 게시글 제목 입력 필드 -->
								<label for="boardTitle">게시글 제목</label> <input type="text"
									id="boardTitle" name="boardTitle" class="form-control"
									placeholder="제목을 입력해주세요!" required />
							</div>

							<div class="form-group mt-3">
								<!-- 게시글 내용 입력 필드 (CKEditor와 연동) -->
								<label for="boardContent">게시글 내용</label>
								<textarea id="boardContent" name="boardContent"
									style="display: none;"></textarea>
							</div>

							<!-- 폼 제출 버튼 -->
							<button class="btn btn-primary btn-block" type="submit">게시글
								등록</button>
						</form>
					</section>
				</div>
			</div>
		</div>

		<!-- Footer -->
		<custom:footer />
		<!-- 커스텀 푸터 태그로 푸터 구성 요소를 삽입 -->

		<!-- CKEditor 5 스크립트 로드 -->
		<script
			src="https://cdn.ckeditor.com/ckeditor5/34.0.0/classic/ckeditor.js"></script>
		<script
			src="https://cdn.ckeditor.com/ckeditor5/34.0.0/classic/translations/ko.js"></script>

		<!-- jQuery 및 기타 스크립트 로드 -->
		<script src="${path}/resources/assets/js/jquery.min.js"></script>
		<script src="${path}/resources/assets/js/browser.min.js"></script>
		<script src="${path}/resources/assets/js/util.js"></script>
		<!-- 게시판 작성 폼에 필요한 커스텀 스크립트 파일 로드 -->
		<script src="${path}/resources/assets/js/board/boardWrite.js"></script>
		<script src="${path}/resources/assets/js/board/boardCate.js"></script>

	</div>
</body>
</html>
