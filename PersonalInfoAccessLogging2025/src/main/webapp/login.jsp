<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>로그인 페이지</title>
<!-- 파비콘 -->
<link rel="icon" href="${path}/resources/assets/images/logo.png"
	type="image/x-icon" />

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<link rel="stylesheet" href="${path}/resources/assets/css/main.css">
<link rel="stylesheet"
	href="${path}/resources/assets/css/member/loginAndSign.css">
<link rel="stylesheet" href="${path}/resources/assets/css/modal.css">
<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<!-- 스위트 알랏 코드 -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="${path}/resources/assets/js/member/loginPwFind.js"></script>
<script src="${path}/resources/assets/js/member/loginGoogleAPI.js"></script>
</head>
<body>
	<div id="page-wrapper">

		<custom:header />

		<div class="container">
			<!-- 로그인 페이지 -->
			<div class="container text-center">
				<br> <br> <br>
				<div class="row justify-content-center">
					<h2>로그인</h2>
				</div>
				<br> <br>
				<div class="row">
					<div class="col-sm-12 col-md-6 text-center align-content-center">
						<img src="${path}\resources\assets\images\breadfishmiddle.jpg"
							class="img">
					</div>
					<div class="col-sm-12 col-md-6 text-left">
						<!-- 로그인 폼태그 -->
						<form id="login" action="login.do" method="POST">

							<div class="input-container">

								<!-- 1. 이메일 입력 안내 -->
								<span class="label">이메일</span>
								<div class="input-containerbox">

									<!-- 우편 모양 아이콘 -->
									<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
										fill="currentColor" class="bi bi-envelope" viewBox="0 0 16 16">
                           		<path
											d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1zm13 2.383-4.708 2.825L15 11.105zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741M1 11.105l4.708-2.897L1 5.383z" />
                        		</svg>

									<!-- 이메일 입력란 -->
									<input type="text" class="inputbox" id="memberEmail"
										name="memberEmail" placeholder="이메일을 입력해주세요" required>
								</div>

								<!-- 2. 비밀번호 입력 안내 -->
								<br> <span class="label">비밀번호</span>
								<div class="input-containerbox">

									<!-- 자물쇠 아이콘 -->
									<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
										fill="currentColor" class="bi bi-lock" viewBox="0 0 16 16">
                            	<path
											d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2m3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2M5 8h6a1 1 0 0 1 1 1v5a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V9a1 1 0 0 1 1-1" />
                        		</svg>

									<!-- 비밀번호 입력란 -->
									<input type="password" class="inputbox" id="memberPassword"
										name="memberPassword" placeholder="비밀번호를 입력해주세요" required>
								</div>
								<div>
									<button class="btn aTagStyle text-nowrap" data-toggle="modal"
										type="button" data-target="#forgetModal">비밀번호를
										잊어버리셨나요?</button>
								</div>
							</div>
							<div class="d-grid gap-2 col-mx-auto">
								<button class="btn btn-warning">로그인</button>
							</div>
							<div>
								<br>
								<!-- 구글 API 미진행 -->
								<%-- 구글 로그인 버튼 시작 : ** client_id를 변경해 사용 --%>
								<div id="g_id_onload"
									data-client_id="730285026476-3dh5pad8cclbr7gsvi75rrmejnemf58l.apps.googleusercontent.com"
									data-context="signin" data-ux_mode="popup"
									data-callback="handleCredentialResponse"
									data-auto_prompt="false"></div>

								<div class="g_id_signin" data-type="icon" data-shape="circle"
									data-theme="outline" data-text="signin_with" data-size="large">
								</div>
								<%--구글 로그인 버튼 끝 --%>
							</div>
							<br>
							<div class="aLink">
								아직 회원이 아니신가요? <a href="signupPage.do">회원가입</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
<!-- 
1. 이름 이메일 입력 후 회원정보확인 클릭
2. 비동기처리 : 이름, 이메일 기반 회원정보 확인(checkPwFind.do)
3. 일치(true)한다면 두번째 모달창으로 이동(이때, 회원 고유번호 유지)
3. 불일치(false)한다면 재입력 안내
 -->

		<!-- 1차 회원정보 확인 모달창 -->
		<div class="modal" id="forgetModal" style="display: none;">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title">비밀번호를 잊어버리셨나요?</h4>
						<button type="button" class="close" onclick="closedModal()"
							data-dismiss="modal">&times;</button>
					</div>

					<!-- 회원여부확인 내용 -->
					<div class="modal-body">

						<!-- 회원여부 확인 후 비밀번호 재설정 버튼 누를 시 작동 -->

						<!-- 이름 입력 -->
						<div class="form-group">
							<%--@declare id="username"--%>
							<label for="username">이름</label> <input type="text"
								class="form-control" id="name" name="memberNickname" required
								placeholder="이름을 입력해주세요">
						</div>

						<!-- 이메일 입력 -->
						<div class="form-group">
							<%--@declare id="email"--%>
							<label for="email">이메일</label> <input type="email"
								class="form-control" id="email" name="memberEmail" required
								placeholder="이메일을 입력해주세요">

						</div>
						<!-- 인증번호가 일치할 경우, 비밀번호 재설정 버튼 open-->
						<div class="d-grid gap-2 text-center">
							<button type="button" class="btn btn-orange" name="resetPw"
								data-bs-target="#setPwModal" data-bs-toggle="modal"
								id="loginPwFind">회원정보 확인</button>
						</div>
						<div class="text-center mt-3">
							<a href="main.do">메인으로 돌아가기</a> <br>
						</div>
						<div>
							<br>
						</div>
					</div>
				</div>
			</div>
		</div>

<!-- 
1. let 전역변수로 memberNum(비밀번호 변경할 회원 고유번호) 유지
2. 두번째 모달창에서 신규 비밀번호와 비밀번호 확인 서로 일치하는지 비동기 확인
3. 비밀번호 일치여부 비동기 : 인풋값이 있다면 이벤트리스너 작동
4. 두 비밀번호가 일치하면 비밀번호가 서로 일치합니다. 아니라면 비밀번호가 서로 다릅니다. 텍스트 안내
5. 비밀번호 변경 버튼 누르면 폼태그(setPw.do)로 백단에게 전달
 -->
		<!-- 2차 새비밀번호로 변경 모달창 -->
		<div class="modal" id="setPwModal" style="display: none;">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title">새로운 비밀번호로 변경</h4>
						<button type="button" class="close" onclick="closedModal()"
							data-dismiss="modal">&times;</button>
					</div>

					<!-- 신규 비밀번호 설정 및 비밀번호 업데이트 -->
					<form id="setPw" action="updatePassword.do" method="POST">
						<input type="hidden" id="passwordFindResult" name="memberNum">
						<!-- 새 비밀번호 입력 및 변경 완료 내용 -->
						<div class="modal-body">
							<!-- 1. 신규 비밀번호 입력 -->
							<div class="form-group">
								<%--@declare id="newpassword"--%>
								<br> <label for="newPassword" class="text-start">신규
									비밀번호</label> <input type="password" class="form-control"
									id="changPassword" name="memberPassword" required
									placeholder="변경할 비밀번호를 입력해주세요"> <br>
							</div>

							<!-- 2. 비밀번호 재확인차 입력 -->
							<div class="form-group">
								<label for="passwordCheck" class="text-start">비밀번호 확인</label> <input
									type="password" class="form-control" id="passwordCheck"
									name="passwordCheck" required placeholder="비밀번호를 확인해주세요">
								<br> <br>
							</div>

							<div class="d-grid gap-2 text-center">
								<button type="button" class="btn btn-orange" id="completePw"
									name="completePw">비밀번호 변경하기</button>
							</div>

							<div class="text-center mt-3">
								<a href="main.do">메인으로 돌아가기</a> <br>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div>
			<br> <br> <br> <br> <br>
			<custom:footer />
		</div>
		<script>
			//모달창 닫기. 로그인창으로
			function closedModal() {//첫번째, 두번째 모달창 숨김.
				$('#loginModal').modal('hide');
				$('#setPwModal').modal('hide');
				$('.modal-backdrop').remove();
			}
		</script>
	</div>
</body>
</html>
