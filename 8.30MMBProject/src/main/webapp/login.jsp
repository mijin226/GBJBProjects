<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<div id="g_id_onload" data-client_id="YOUR_GOOGLE_CLIENT_ID"
		data-login_uri="https://your.domain/your_login_endpoint"
		data-auto_prompt="false"></div>
	<div class="g_id_signin" data-type="standard" data size="large"
		data-theme="outline" data-text="sign_in_with" data-shape="rectangular"
		data-logo_alignment="left"></div>

	<script src="https://accounts.google.com/gsi/client" async defer></script>
	<script>
		function handleCredentialResponse(response) {
			console.log("Encoded JWT ID token: " + response.credential);
		}
		window.onload = function() {
			// Google 계정으로 로그인 클라이언트를 만드는 메서드
			//  동일한 웹페이지의 모든 모듈에서 암시적으로 사용할 수 있다.
			google.accounts.id.initialize({
				// 내가 생성한 로그인  클라이언트 id
				client_id : "938977314941-4q87501po9mih6871h5490tr9n2tqaqs.apps.googleusercontent.com",
				// ID 토큰을 처리하는 JS 함수
				callback : handleCredentialResponse
			});
			
			// Google 계정으로 로그인을 렌더링
			// 렌더링 : 웹 하면에 그리는 것을 도와주는 것
			google.accounts.id.renderButton(
				document.getElementById("buttonDiv"), {
				theme : "outline",
				size : "large"
			} // customization attributes
			);
			google.accounts.id.prompt(); // also display the One Tap dialog
		}
	</script>

</body>
</html>