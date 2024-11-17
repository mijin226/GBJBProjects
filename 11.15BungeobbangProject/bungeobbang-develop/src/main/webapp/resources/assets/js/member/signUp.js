/*
이메일 : DB 중복확인 진행 		-	인증번호 메일 전송 text 안내
인증번호 : 인증번호 보내기 클릭 		-	인증번호 전송 text 안내
		인증번호 확인 클릭 		-	인증번호 일치여부 text 안내
비밀번호 : 영문, 숫자, 특수문자 조합 	-	조건 충족 시 유효성 검사 text 안내				
닉네임 : DB 중복확인 진행			- 	사용 가능 닉네임 여부 text 안내
전화번호 : 선택사항으로 입력
회원가입 버튼 클릭 시 유효성 검사 진행 후, 모두 충족시 회원 처리
*/

//회원가입 확인절차 요소 전역변수 세팅
let checkNickname = false;
let checkEmail = false;
let sendEmailResult = false;
let checkNumResult = false;
let isPasswordValid = false;
let isPasswordMatched = false;


$(document).ready(()=>{

//이메일 값 있다면 중복확인 진행 가능
//이메일 값 없다면 입력 안내
$('#checkEmailBtn').on('click', function() {
	console.log('log: #checkEmailBtn.onclick');
	//버튼 클릭시 메세지 초기화
	$('#checkEmailMsg').text("");
	let email = $('#email').val();
	console.log('email: ['+email+']');

	//유효한 값인지 검증해 유효하다면 계속 진행
	if (email.trim().includes('@')) { //JS는 contains대신 includes
		console.log('email input tag email : ['+email+']');
		//중복 닉네임 확인
		checkEmailFunction(email);
	}
	else {
		Swal.fire({
			icon: 'error',
			title: '유효하지 않은 이메일',
			text: '이메일 형식에 맞춰 작성바랍니다.',
			confirmButtonText: '확인'
		});
	}
});

// 이메일 중복확인
function checkEmailFunction(email) {
	console.log("signUp.js emailCheck start")
	console.log('email: ['+email+']');
	$.ajax({
		method: "POST",
		url: "checkEmailName.do",
		data: {
			memberEmail: email
		},
		success: function(data) {
			console.log("data [" + data + "]");
			console.log("typeof data [" + typeof data + "]");
			checkEmail = data; //true or false
			if (checkEmail === 'false') {
				//$("#checkEmailMsg").text("사용 가능한 이메일입니다.").css('color', 'green')
				//$('input[name ="memberEmail"]').prop('disabled', true);
				sendEmail(email);
			}
			else {
				$("#sendEmailMsg").text("사용 불가한 이메일입니다.").css('color', 'red');
				// 이메일이 중복인 경우, 입력란을 비우고, 입력창으로 이동
				$('#email').val('').focus();
			}
			console.log("signUp.js emailCheck end")
		},
		error: function(error) {
			//비동기 통신 실패 시
			console.error("signUp.js emailCheck error")
		}
	});

}
	
// 이메일 전송 기능
function sendEmail(email) {
	console.log("signUp.js sendEmail() start");

	$.ajax({
		url: "sendEmail.do",
		method: "POST",
		data: {
			email: email
		},
		success: function(data) {
			console.log(data);
			sendEmailResult = (data === true || data === 'true');  // boolean
			if (sendEmailResult) {
				$("#sendEmailMsg").text("인증번호 전송 완료").css('color', 'green');
			} else {
				$("#sendEmailMsg").text("인증번호 전송 실패").css('color', 'red');
			}
		},
		error: function(error) {
			Swal.fire({
				icon: 'error',
				title: '인증번호 전송 오류',
				text: '인증번호 전송 중 오류가 발생했습니다.',
				confirmButtonText: '확인'
			});
			console.log("signUp.js sendEmail() error");
		}
	});
}

//버튼을 클릭 시 인증번호 확인 함수 실행
	$('#checkNumBtn').on('click', function() {
		//버튼 클릭시 메세지 초기화
		$('#checkNumResult').text("");
		if ($('input[name="checkNum"]').val() !== '') {
			checkNum();//위 인증번호 일치 여부 확인 비동기 실행
		} else {
			Swal.fire({
				icon: 'error',
				title: '인증번호 확인',
				text: '인증번호를 입력해주세요.',
				confirmButtonText: '확인'
			});	}
	});
	
// 사용자가 인증번호 입력한 후 버튼을 통한 인증번호 확인
function checkNum() {
	console.log("signUp.js checkNum() ");
	// 인증번호 저장
	var checkNum = $('input[name="checkNum"]').val();
	console.log(checkNum);

	$.ajax({
		url: "checkEmailNum.do",
		method: "POST",
		data: {
			checkNum: checkNum
		},
		success: function(data) {
			console.log("[" + data + "]");
			checkNumResult = (data === true || data === 'true');  // boolean
			console.log(checkNumResult);
			if (checkNumResult) { 
				$("#checkNumMsg").text("인증번호 일치").css('color', 'green');;
			} else {
				$("#checkNumMsg").text("인증번호 일치하지 않음").css(	'color', 'red');
			}
		},
		error: function(error) {
			Swal.fire({
				icon: 'error',
				title: '인증번호 확인 오류',
				text: '인증번호 확인 중 오류가 발생했습니다.',
				confirmButtonText: '확인'
			}); console.log("signUp.js checkNum() error");
		}
	});
}

// 비밀번호 정규식 검사 및 일치 여부 확인
const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;

document.getElementById('password1').addEventListener('blur', function() {
    const password = this.value;
    const passwordErrorMsg = document.getElementById('passwordErrorMsg');

    if (passwordRegex.test(password)) {
        passwordErrorMsg.textContent = "유효성 검사 완료";
		passwordErrorMsg.style.color = 'green';
        isPasswordValid = true; // 유효성 통과
		console.log(isPasswordValid);
    } else {
        passwordErrorMsg.textContent = "비밀번호는 8~16자의 영문, 숫자, 특수문자 조합이어야 합니다.";
        isPasswordValid = false; // 유효성 미통과
    }
});

// 비밀번호 일치 여부 확인
$('#password2').on('input', function() {
    const password1 = $('#password1').val();
    const password2 = this.value;
    const resultPW = document.getElementById('resultPW');

    if (password1 !== password2) {
        resultPW.textContent = "비밀번호가 일치하지 않습니다.";
        resultPW.style.color = 'red';
        isPasswordMatched = false; // 비밀번호가 일치하지 않음
    } else {
        resultPW.textContent = "비밀번호가 일치합니다.";
        resultPW.style.color = 'green';
        isPasswordMatched = true; // 비밀번호가 일치함
    }
});


//닉네임 값이 있다면 중복확인 진행 가능
//닉네임값이 없다면 입력 안내
$('#checkNicknameBtn').on('click', function() {
	//버튼 클릭시 메세지 초기화
	$('#checkNicknameMsg').text("");
	if ($('#memberNicknameInput').val() !== '') {
		checkNicknameFunction(); //닉네임값이 있다면 중복확인 진행
	} else {
		Swal.fire({
			icon: 'error',
			title: '닉네임 미기재',
			text: '닉네임을 입력해주세요',
			confirmButtonText: '확인'
		});
	}
});


//닉네임 중복확인 실행
function checkNicknameFunction() {
	console.log("로그 : 닉네임 중복확인 시작");
	var nickname = $('#memberNicknameInput').val();
	// 닉네임 입력값 저장
	$.ajax({
		url: "checkNickname.do",
		method: "POST",
		data: {
			memberNickname: nickname
		},
		success: function(data) {
			console.log("[" + data + "]");
			checkNickname = (data === true || data === 'true');  // boolean
			if (checkNickname) {
				$("#checkNicknameMsg")
					.text("사용가능한 닉네임입니다.").css('color',	'green');
			} else {
				$("#checkNicknameMsg").text("이미 사용중인 닉네임입니다.").css('color',	'red');
				// 닉네임이 중복인 경우, 입력란을 비우고, 입력창으로 이동
				$("#nickname").val('').focus();
			}
			console.log("signUp.js nicknameCheck end");
		},
		error: function(error) {
		    console.log("signUp.js nicknameCheck error");
		}
	});
}

// 비밀번호 보기 기능
function togglePasswordVisibility(inputId, iconId) {
    const passwordField = document.getElementById(inputId);
    const icon = document.getElementById(iconId);

    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        passwordField.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

document.getElementById('togglePassword1').addEventListener('click', function() {
    togglePasswordVisibility('password1', 'togglePassword1');
});

document.getElementById('togglePassword2').addEventListener('click', function() {
    togglePasswordVisibility('password2', 'togglePassword2');
});

//전화번호 형식
function formatPhoneNumber(input) {
    // 숫자만 추출
    let cleaned = input.replace(/\D/g, '');

    // 숫자를 형식에 맞게 변환
    let formatted = cleaned;

    if (cleaned.length > 3 && cleaned.length <= 7) {
        formatted = `${cleaned.slice(0, 3)}-${cleaned.slice(3)}`;
    } else if (cleaned.length > 7) {
        formatted = `${cleaned.slice(0, 3)}-${cleaned.slice(3, 7)}-${cleaned.slice(7, 11)}`;
    }
    
    return formatted;
}

	// 전화번호 자동 포멧팅
	document.getElementById('phone').addEventListener('input', function (e) {
	    e.target.value = formatPhoneNumber(e.target.value);
	});
	
	$('#joinbutton').on('click', function(event) {
	    if (!validateForm()) {
	        event.preventDefault(); // 조건 미충족 시 폼 제출 막기
	    }
	});

		
})

function validateForm() {
	console.log(checkEmail);
    if (!checkEmail || !sendEmailResult || !checkNumResult || !checkNickname || !isPasswordValid || !isPasswordMatched) {
        Swal.fire({
            icon: 'error',
            title: '회원가입 실패',
            text: '모든 인증 과정을 완료해 주세요.',
            confirmButtonText: '확인'
        });
        return false; // 폼 제출 막기
    }
    return true; // 모든 조건이 만족되면 제출 허용
}

//이미지 업로드
function previewImageSignUp(event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = function() {
        const previewImage = document.getElementById('previewImage');
        previewImage.src = reader.result;
        previewImage.style.display = 'block';
    };

    reader.readAsDataURL(file);
}

// 이미지 제거 함수
function deleteImageSignUp() {
    document.getElementById('previewImage').src = 'uploads/default_profile.png';
    document.getElementById('file').value = ''; // 파일 input 초기화
}



