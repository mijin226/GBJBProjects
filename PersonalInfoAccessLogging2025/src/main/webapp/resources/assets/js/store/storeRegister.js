$(document).ready(function() {

	// 요일 checkbox와 관련된 기능들
	// weekTable에 변화 확인
	$("#weekTable").on("change", function(event) {
		// 그 변화가 checkbox라면
		if (event.target.type === "checkbox") {
			// 영업시간 비활성 함수
			toggleTimeInput(event);
		}
		// 만약 변경된 요소의 id값이 'endTime-'으로 시작한다면
		else if (event.target.id.startsWith("endTime-")) {
			endTimeCheck();
		}
	});

	// 메뉴, 결제방법 체크
	// menuTable에 변화가 생긴다면
	$("#menuTable").on("change", function(event) {
		deleteHiddenCheck(event);
	});
	// paymentTable에 변화가 생긴다면
	$("#paymentTable").on("change", function(event) {
		deleteHiddenCheck(event);
	});

	submitPassCheck();

	// 전화번호 자동 포멧팅
	document.getElementById('phone').addEventListener('input', function(e) {
		e.target.value = formatPhoneNumber(e.target.value);
	});
});

// 영업시간에서 체크하지 않은 요일은 시작시간과 종료시간 input이 비활성화 되도록 하는 함수
function toggleTimeInput(event) {
	console.log('storeRegister.js : toggleTimeInput 시작');
	// 이벤트가 일어난 checkbox의 id값 가져오기
	const week = event.target.id;
	console.log('storeRegister.js : week : [' + week + ']');

	// 체크박스의 요소, 해당 체크박스의 startTime-, endTime- 뒤에 체크박스의 id가 붙은 값의 요소를 가져와라
	// 체크박스 요소
	const checkbox = event.target;
	// 시작시간 요소
	const startTime = document.getElementById(`startTime-${week}`);
	// 종료시간 요소
	const endTime = document.getElementById(`endTime-${week}`);
	console.log('storeRegister.js : checkbox : [' + checkbox + ']');
	console.log('storeRegister.js : startTime : [' + startTime + ']');
	console.log('storeRegister.js : endTime : [' + endTime + ']');

	// 만약 체크박스가 체크되어 있다면
	if (checkbox.checked) {
		// 시작시간, 종료시간 입력 방지를 해제, 입력 강제
		startTime.disabled = false;
		startTime.required = true;
		endTime.disabled = false;
		endTime.required = true;
	} else {
		// 시작시간, 종료시간 입력 방지, 입력 강제 해제
		startTime.disabled = true;
		startTime.required = false;
		endTime.disabled = true;
		endTime.required = false;
	}
}

// 시작 시간이 종료 시간보다 앞의 시간인지 확인하는 코드
function endTimeCheck() {
	// 해당 id값 가져오기
	const endTimeId = event.target.id;
	console.log('storeRegister.js : endTimeId : [' + endTimeId + ']');

	// id값 뒤에서 3문자를 잘라서 보관
	const week = endTimeId.slice(-3);
	console.log('storeRegister.js : week : [' + week + ']');

	// 비교하려는 startTime의 id 값 만들기
	const startTimeId = "startTime-" + week;
	console.log('storeRegister.js : startTimeId : [' + startTimeId + ']');

	// 비교를 위해 startTime 요소와 endTime 요소값 가져오기
	const startTime = document.getElementById(startTimeId).value;
	const endTime = document.getElementById(endTimeId).value;
	console.log('storeRegister.js : startTime : [' + startTime + ']');
	console.log('storeRegister.js : endTime : [' + endTime + ']');

	// 시간 문자열을 직접 비교
	if (startTime >= endTime) {
		Swal.fire({
			icon: 'error',
			title: '영업시간 확인',
			text: '영업 시작 시간이 종료 시간보다 이후일 수 없습니다.',
			confirmButtonText: '확인',
			showCancelButton: false // 취소 버튼 비활성화
		});
		document.getElementById(endTimeId).value = ''; // 값을 빈 문자열로 초기화
		document.getElementById(endTimeId).focus(); // 잘못된 필드에 포커스
	}
}

// 메뉴와 결제방법 테이블의 체크박스가 체크 될 때 hidden input 요소는 제거하는 함수
// 가져올 테이블이 2개이므로 변화 확인 이벤트는 따로 작성하고, 공통으로 들어가는 hidden input 제거 함수만 사용
function deleteHiddenCheck(event) {
	console.log('storeRegister.js : deleteHiddenCheck 시작');

	// 만약 그 변화가 checkbox라면
	if (event.target.type === "checkbox") {
		// 이벤트가 일어난 checkbox의 id 가져오기
		const id = event.target.id;
		console.log('storeRegister.js : id : [' + id + ']');

		// 이벤트가 일어난 checkbox의 name 값 가져오기
		const name = event.target.name;
		console.log('storeRegister.js : name : [' + name + ']');

		// 제거할 hidden 요소의 id값으로 요소 가져오기
		// hidden 요소의 id : nonChecked-${id}
		const hiddenInputId = `nonChecked-${id}`;
		console.log('storeRegister.js : hiddenInputId : [' + hiddenInputId + ']');
		// hiddenInputId 값으로 요소 가져오기
		const hiddenInput = document.getElementById(hiddenInputId);
		console.log('storeRegister.js : hiddenInput : [' + hiddenInput + ']');

		// 만약 체크박스가 체크되어 있다면
		if (event.target.checked) {
			// hiddentInput 요소가 존재한다면
			if (hiddenInput) {
				// hiddentInput 요소 지우기
				hiddenInput.remove();
				console.log('storeRegister.js : hiddenInput 요소 삭제');
			}
			else {
				console.log('storeRegister.js : hiddenInput 요소가 존재하지 않음, 삭제 실패');
			}
		}
		// 체크박스가 해제되어있고, hiddentInput 요소가 존재하지 않는다면
		else if (!event.target.checked && !hiddenInput) {
			console.log('storeRegister.js : hiddenInput 요소 추가');

			// 숨겨진 요소 추가
			// input 타입 요소 추가
			const newHiddenInput = document.createElement('input');
			// type 속성은 hidden으로 설정
			newHiddenInput.type = 'hidden';
			// name 속성은 hidden으로 설정
			newHiddenInput.name = name;
			// id 속성은 hiddenInputId으로 설정
			newHiddenInput.id = hiddenInputId;
			// value 속성은 'N'으로 설정
			newHiddenInput.value = 'N';
			// checkbox의 부모요소에 자식요소로 추가
			event.target.parentNode.appendChild(newHiddenInput);

			// 추가 확인용 로그
			if (hiddenInput) {
				console.log('storeRegister.js : hiddenInput 요소 추가 완료');
			}
		}
	}
}


// 가게 등록 검증
function submitPassCheck() {
	document.getElementById('storeRegister').addEventListener('submit', function(event) {
		// 이벤트 핸들러의 기본 동작을 막음
		event.preventDefault();

		// 주소 값 확인
		const mainAddress = document.getElementById('addressMain').value;
		console.log('storeRegister.js : mainAddress : [' + mainAddress + ']');
		if (!mainAddress) {
			Swal.fire({
				icon: 'error',
				title: '주소 확인',
				text: '주소를 입력해야 합니다.',
				confirmButtonText: '확인',
				showCancelButton: false // 취소 버튼 비활성화
			});
			return; // 검증 실패 시 폼 제출 중단
		}

		// 메뉴 체크박스 검증
		// menuTable id값을 가진 요소 선택
		const businessMenusGroup = document.getElementById('menuTable');
		console.log('storeRegister.js : businessMenusGroup : [' + businessMenusGroup + ']');
		console.log('storeRegister.js : businessMenusGroup : [' + businessMenusGroup + ']');
		const menuCheckboxes = businessMenusGroup.querySelectorAll('input[type="checkbox"]');
		console.log('storeRegister.js : menuCheckboxes : [' + menuCheckboxes + ']');
		const isMenuChecked = Array.from(menuCheckboxes).some(checkbox => checkbox.checked);
		console.log('storeRegister.js : isMenuChecked : [' + isMenuChecked + ']');
		if (!isMenuChecked) {
			Swal.fire({
				icon: 'error',
				title: '메뉴 확인',
				text: '메뉴를 하나 이상 선택해야 합니다.',
				confirmButtonText: '확인',
				showCancelButton: false // 취소 버튼 비활성화
			});
			return; // 검증 실패 시 폼 제출 중단
		}

		// 결제 체크박스 검증
		// paymentTable id값을 가진 요소 선택
		const businessPaymentsGroup = document.getElementById('paymentTable');
		console.log('storeRegister.js : businessPaymentsGroup : [' + businessPaymentsGroup + ']');
		const payCheckboxes = businessPaymentsGroup.querySelectorAll('input[type="checkbox"]');
		console.log('storeRegister.js : payCheckboxes : [' + payCheckboxes + ']');
		const isPayChecked = Array.from(payCheckboxes).some(checkbox => checkbox.checked);
		console.log('storeRegister.js : isPayChecked : [' + isPayChecked + ']');
		if (!isPayChecked) {
			Swal.fire({
				icon: 'error',
				title: '결제방식 확인',
				text: '결제 방식을 하나 이상 선택해야 합니다.',
				confirmButtonText: '확인',
				showCancelButton: false // 취소 버튼 비활성화
			});
			return; // 검증 실패 시 폼 제출 중단
		}

		// 요일 체크박스 검증
		// weekTable id값을 가진 요소 선택
		const businessDaysGroup = document.getElementById('weekTable');
		console.log('storeRegister.js : businessDaysGroup : [' + businessDaysGroup + ']');
		const dayCheckboxes = businessDaysGroup.querySelectorAll('input[type="checkbox"]');
		console.log('storeRegister.js : dayCheckboxes : [' + dayCheckboxes + ']');
		const isDayChecked = Array.from(dayCheckboxes).some(checkbox => checkbox.checked);
		console.log('storeRegister.js : isDayChecked : [' + isDayChecked + ']');
		if (!isDayChecked) {
			Swal.fire({
				icon: 'error',
				title: '영업요일 확인',
				text: '영업 요일을 하나 이상 선택해야 합니다.',
				confirmButtonText: '확인',
				showCancelButton: false // 취소 버튼 비활성화
			});
			return; // 검증 실패 시 폼 제출 중단
		}

		// 모든 검증 통과 시 폼 제출
		this.submit();
	});
}

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