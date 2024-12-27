<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Fail Info Page</title>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" />
</head>
<body>
	<script>
		// SweetAlert를 사용하여 메시지를 출력하고 확인 버튼 클릭 시 경로로 리디렉션
		swal({
			type : "info", // 정보 타입 설정
			title : "요청 처리 오류", // 제목 설정
			text : "요청을 처리하는 도중 문제 상황이 발생했습니다. 메인으로 되돌아 갑니다.", // 메시지 설정
			confirmButtonText : "확인" // 확인 버튼 텍스트
		}, function() {
			// 확인 버튼 클릭 시 리디렉션
			window.location.href = "main.do"; // 메인 페이지로 리디렉션
		});
	</script>
</body>
</html>
