//로그인하기 위한 비밀번호 찾기(회원 여부 확인)

/*
모달창1
1. 이름 이메일 입력 후 회원정보확인 클릭
2. 비동기처리 : 이름, 이메일 기반 회원정보 확인(checkPwFind.do)
3. 일치(true)한다면 두번째 모달창으로 이동(이때, 회원 고유번호 유지)
3. 불일치(false)한다면 재입력 안내

모달창2
1. let 전역변수로 memberNum(비밀번호 변경할 회원 고유번호) 유지
2. 두번째 모달창에서 신규 비밀번호와 비밀번호 확인 서로 일치하는지 비동기 확인
3. 비밀번호 일치여부 비동기 : 인풋값이 있다면 이벤트리스너 작동
4. 두 비밀번호가 일치하면 비밀번호가 서로 일치합니다. 아니라면 비밀번호가 서로 다릅니다. 텍스트 안내
5. 비밀번호 변경 버튼 누르면 폼태그(setPw.do)로 백단에게 전달
*/

$(document).ready(function() {
	console.log('loginPwFind.js start');
	
	// 전역 변수로 memberNum 정의
	let memberNum;

	//첫번째 모달창 : 회원 정보 확인하기 //////////////////////////////////////////
	$('#loginPwFind').on('click', function() {//회원정보 확인 버튼 누르면
		console.log("log: loginPwFind clicked");
		//이벤트가 발생하게 만든 주체, 즉 사용자가 입력한 상태값을 가져와 상태로 변수로 지칭
		const name = document.getElementById('name').value;
		const email = document.getElementById('email').value;

		console.log('name: ' + name);
		console.log('email: ' + email);

		//ajax를 통해 비동기처리 진행
		$.ajax({
			url: 'checkPWFind.do',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({
				memberName: name,
				memberEmail: email
			}),
			dataType: 'json',
			success: function(result) {
				console.log("response : " + result);
				if (result > 0) { // 키 값이 true인 경우
					Swal.fire({
						icon: 'success',
						title: '회원 정보 확인',
						text: '회원정보가 확인되었습니다. 새 비밀번호를 입력해주세요.',
						confirmButtonText: '새로운 비밀번호로 변경하기'
					}).then(function() {
						// MemberDTO 고유번호 가지고 새로운 비밀번호 입력 모달 열기
						openSetPasswordModal(result); // memberNum을 넘겨주는 함수
					});
				} else { // 키 값이 false인 경우
					Swal.fire({
						icon: 'error',
						title: '회원 정보 없음',
						text: '회원정보가 없습니다. 다시 정보를 입력해주시거나 회원가입해주세요.',
						confirmButtonText: '확인'
					});
				}
			},
			error: function(error) {
				console.error('AJAX 요청 실패:', error);
				Swal.fire({
					icon: 'error',
					title: '오류',
					text: '서버와의 통신에 실패했습니다.',
					confirmButtonText: '확인'
				});
			}
		});
	});

	// 2번째 모달창 : 비밀번호 변경  //////////////////////////////////////////
	document.getElementById('completePw').addEventListener('click', function(event) {
		event.preventDefault(); // 선 작동 방지

		//값 받아오기
		const password = document.getElementById('changPassword').value;
		const passwordCheck = document.getElementById('passwordCheck').value;

		console.log("log: password : [" + password + "]");
		console.log("log: passwordCheck : [" + passwordCheck + "]");


		if (password === passwordCheck) {
			$('#setPw').submit(); // 비밀번호가 일치하면 전송
		}
	});
});


//두번째 모달창(새비밀번호입력) 열기
function openSetPasswordModal(memberNum) {
	// memberNum 저장
	document.getElementById('passwordFindResult').value = memberNum;
	$('#forgetModal').modal('hide');//첫번째 모달 닫기
	$('#setPwModal').modal('show');
	console.log("loginPwFind.js second modal open");
	// 비밀번호 일치 여부 확인 초기화
	// document.getElementById('resultPW').textContent = '';
}
