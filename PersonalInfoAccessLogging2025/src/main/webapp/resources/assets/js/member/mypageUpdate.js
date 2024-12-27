/*
1. 이메일 중복검사 및 닉네임 중복검사해야만 수정완료 버튼 작용
*/
//아래로직과 동일 추후 제거. 컴파일 꼬임 발생으로 2개 생성함
function a(event) {
	// 선택된 파일을 가져옴
	const file = event.target.files[0];
	console.log('mypageUpdate.js previewImge(event) load complete');

	// FileReader 객체를 생성
	const reader = new FileReader();
	console.log('mypageUpdate.js previewImge(event) read');

	// 파일이 로드되었을 때 실행되는 이벤트 핸들러
	reader.onload = function() {
		console.log('mypageUpdate.js previewImge(event) loading');

		// 미리보기 이미지 태그의 src 속성을 선택된 파일의 데이터로 설정
		const previewImage = document.getElementById('previewImage');
		previewImage.src = reader.result; // 파일의 데이터 URL
		//reviewImage.style.display = 'block'; // 이미지 태그를 보이도록 설정
		console.log('mypageUpdate.js previewImge(event) show');

	};

	// 파일 읽기 시작
	reader.readAsDataURL(file);
	console.log('mypageUpdate.js previewImge(event) show complete');

}

$(document).ready(function() {
	console.log("mypageUpdate.js start");

	// 파일 입력 필드에서 파일이 선택되었을 때 호출되는 함수
	function previewImage(event) {
		// 선택된 파일을 가져옴
		const file = event.target.files[0];
		console.log('mypageUpdate.js previewImge(event) load complete');

		// FileReader 객체를 생성
		const reader = new FileReader();
		console.log('mypageUpdate.js previewImge(event) read');

		// 파일이 로드되었을 때 실행되는 이벤트 핸들러
		reader.onload = function() {
			console.log('mypageUpdate.js previewImge(event) loading');

			// 미리보기 이미지 태그의 src 속성을 선택된 파일의 데이터로 설정
			const previewImage = document.getElementById('previewImage');
			previewImage.src = reader.result; // 파일의 데이터 URL
			previewImage.style.display = 'block'; // 이미지 태그를 보이도록 설정
			console.log('mypageUpdate.js previewImge(event) show');

		};

		// 파일 읽기 시작
		reader.readAsDataURL(file);
		console.log('mypageUpdate.js previewImge(event) show complete');

	}

	// 초기 닉네임과 전화번호 저장
	var initialNickname = $('#nickname').val();
	console.log("초기 닉네임:", initialNickname);
	var initialPhoneNum = $('#phoneNum').val();
	console.log("초기 전화번호:", initialPhoneNum);

	//유효성 체크 여부 확인 변수
	var isNicknameChecked = true;
	var isPhoneNumChecked = true;
	var isPasswordChecked = true;

	// 닉네임 입력 시 변경 확인
	$('#nickname').on('input', function() {
		var currentNickname = $(this).val();
		console.log("닉네임 변경됨:", currentNickname);
		isNicknameChecked = (currentNickname === initialNickname);
		console.log("닉네임 중복 확인 필요 여부:", !isNicknameChecked);
	});

	// 전화번호 입력 시 변경 확인
	$('#phoneNum').on('input', function() {
		var currentPhoneNum = $(this).val();
		var phoneRegex = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;
		isPhoneNumChecked = (currentPhoneNum === initialPhoneNum || phoneRegex.test(currentPhoneNum));
		console.log("전화번호 변경됨:", currentPhoneNum);
		console.log("전화번호 확인 필요 여부:", !isPhoneNumChecked);
	});

	// 비밀번호 입력 시 보안성 확인
	$('#mypagePassword').on('input', function() {
		var password = $(this).val();
		var passwordRegex = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;
		isPasswordChecked = (password === '' || passwordRegex.test(password));
		console.log("비밀번호 입력됨");
		console.log("비밀번호 보안 확인 필요 여부:", !isPasswordChecked);
	});

	// 닉네임 중복 확인 버튼 클릭
	$('#checkNicknameBtn').on('click', function() {
		console.log("닉네임 중복 확인 버튼 클릭됨");
		if (!isNicknameChecked) {
			checkNicknameFunction();
		} else {
			Swal.fire({
				icon: 'info',
				title: '알림',
				text: '닉네임이 변경되지 않았습니다.',
				confirmButtonText: '확인'
			});
		}
	});

	// 닉네임 중복 확인 함수
	function checkNicknameFunction() {
		var nickname = $('#nickname').val();
		console.log("닉네임 중복 확인 중:", nickname);
		$.ajax({
			url: 'checkNickname.do',
			method: 'POST',
			data: { memberNickname: nickname },
			success: function(data) {
				if (data === 'true') {
					console.log("닉네임 사용 가능");
					$("#checkNicknameMsg").text("사용 가능한 닉네임입니다.").css('color', 'green');
					isNicknameChecked = true;
				} else {
					console.log("닉네임 사용 중");
					$("#checkNicknameMsg").text("이미 사용 중인 닉네임입니다.").css('color', 'red');
					$('#nickname').focus();
				}
			},
			error: function() {
				console.error("닉네임 중복 확인 오류 발생");
				Swal.fire({
					icon: 'error',
					title: '오류',
					text: '닉네임 중복 확인 중 오류가 발생했습니다',
					confirmButtonText: '확인'
				});
			}
		});
	}

	// 폼 제출 시 회원체크(유효성검사) 진행했는지 확인
	$('#submit-button').on('click', function(event) {
		console.log("제출 버튼 클릭됨");
		if (!isNicknameChecked) {
			console.warn("닉네임 중복 확인 필요");
			Swal.fire({
				icon: 'error',
				title: '닉네임 중복 확인 필요',
				text: '닉네임 중복 확인을 해주세요',
				confirmButtonText: '확인'
			});
			event.preventDefault();
			return false;
		}
		if (!isPhoneNumChecked) {
			console.warn("전화번호 형식 확인 필요");
			Swal.fire({
				icon: 'error',
				title: '전화번호 형식 확인 필요',
				text: '유효한 전화번호를 입력해주세요',
				confirmButtonText: '확인'
			});
			event.preventDefault();
			return false;
		}
		if (!isPasswordChecked) {
			console.warn("비밀번호 보안성 확인 필요");
			Swal.fire({
				icon: 'error',
				title: '비밀번호 보안성 확인 필요',
				text: '비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.',
				confirmButtonText: '확인'
			});
			event.preventDefault();
			return false;
		}
		console.log("폼 제출 가능");
	});
});