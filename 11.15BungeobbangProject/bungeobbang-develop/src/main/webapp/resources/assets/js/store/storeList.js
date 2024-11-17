// 페이지가 로드 되었을 때 실행
$(document).ready(function() {
	stillCheckboxTag();

	// 체크박스 상태가 변경될 때 호출되는 이벤트 핸들러
	toggleTagByCheckbox();

	// x버튼을 눌렀을 때 해당 태그를 제거
	tagByXbutton();

	// storeList 페이지네이션
	storePagenation();


});


// 함수 목록 ------------------------------------------------------------------------------------------------

// 체크박스 내부 값 로그 함수
function checkBoxCheck() {
	console.log('menuCheckBox : checkBoxCheck() 시작');

	// 체크 박스의 값을 담을 배열 생성
	const checkedValues = [];

	// checkbox 값들 가져오기
	// 메뉴 checkbox 가져오기
	const menuCheckBox = document.querySelectorAll('input[name="storeMenu"]:checked');
	console.log('menuCheckBox : [' + Array.from(menuCheckBox) + ']');

	// 결제방법 checkbox 가져오기
	const paymentCheckBox = document.querySelectorAll('input[name="storePayment"]:checked');
	console.log('paymentCheckBox : [' + Array.from(paymentCheckBox) + ']');

	// 운영상태 checkbox 가져오기
	const stateCheckBox = document.querySelector('input[name="storeClosed"]:checked');
	console.log('stateCheckBox : [' + stateCheckBox + ']');

	// 체크박스의 값을 모두 모을 배열 생성
	menuCheckBox.forEach((checkbox) => {
		console.log('checkbox : [' + checkbox.dataset.role + ']');
		checkedValues.push(checkbox.dataset.role);
	});
	paymentCheckBox.forEach((checkbox) => {
		console.log('checkbox : [' + checkbox.dataset.role + ']');
		checkedValues.push(checkbox.dataset.role);
	});

	// 만약 운영 상태가 null이 아니라면
	if (stateCheckBox != null) {
		// 운영상태도 추가
		checkedValues.push(stateCheckBox.dataset.role);
	}


	console.log('체크된 값들 : [' + checkedValues + ']');

	return checkedValues;
}

// 체크된 값들을 태그로 만들어 놓기
function stillCheckboxTag() {
	console.log('menuCheckBox : stillCheckboxTag() 시작');

	const checkedValues = checkBoxCheck(); // 체크된 값 배열

	// 기존의 데이터 리스트를 가져옴
	var dataList = $('#tagBox');

	// checkedValues 배열 길이만큼 반복
	checkedValues.forEach(function(value) {
		// 데이터 리스트에 이미 존재하는지 확인
		if (dataList.find('.filterOption').filter(function() {
			// value값이 같은 값 반환
			var tag = $(this).text().trim();

			// 뒤에 'X' 지우기
			tag = tag.slice(0, -1).trim(); // 'X'를 잘라내고 공백 제거
			console.log('tag : [' + tag + ']'); // 로그 출력
			return tag === value; // value와 비교
		}).length === 0) { // 같은 값이 없을 경우
			// 기존 박스에 태그 추가
			dataList.append(
				'<span class="filterOption">'
				+ value // 체크된 값을 추가
				+ '<button class="filterButton">X</button>'
				+ '</span>'
			);
		}
	});
}


// 체크박스로 태그값을 변경하는 함수
function toggleTagByCheckbox() {
	$('input[type="checkbox"]').on('change', function() {
		console.log('menuCheckBox : toggleTagByCheckbox() 시작');

		// 기존의 데이터 리스트를 가져옴
		// 데이터가 변경될 것이므로 객체 자체를 가져옴
		var dataList = $('#tagBox');

		// 체크박스가 선택된 경우
		if ($(this).is(':checked')) {
			// 해당 체크박스의 data-role 값 가져오기
			const checkBoxValue = $(this).attr('data-role');
			console.log('checkBoxValue : [' + checkBoxValue + ']')

			// 기존 박스에 태그 추가
			dataList.append(
				'<span class="filterOption">'
				+ checkBoxValue
				+ '<button class="filterButton">X</button>'
				+ '</span>'
			)
		}
		// 체크박스가 해제된 경우
		else {
			// 해당 체크박스의 data-role 값 가져오기
			$(this).attr('data-role');
			const checkedBoxValue = $(this).attr('data-role');
			console.log('checkedBoxValue : [' + checkedBoxValue + ']')

			console.log('텍스트 요소가 같은 요소를 찾음');

			// 텍스트 요소가 같은 요소를 찾아서 제거
			dataList.find('.filterOption').filter(function() {
				// value값이 같은 값 반환
				var tag = $(this).text().trim();

				// 뒤에 x자 지우기 (1글자 생략)
				tag = tag.slice(0, -1);

				console.log('tag : [' + tag + ']');
				return tag === checkedBoxValue;
			}).remove();


			// 체크박스 해제 로그
			console.log(checkedBoxValue + ' 체크 해제')
		}

		checkBoxCheck();

	});
}

// 태그의 x 버튼을 클릭 시 태그 삭제
function tagByXbutton() {
	$(document).on('click', '.filterButton', function() {
		console.log('menuCheckBox : tagByXbutton() 시작');

		// 해당 태그의 텍스트 가져옴
		// closest()으로 가장 가까운 부모 중 class가 'filterOption'인 요소를 찾음
		// .content를 통해 span 요소의 모든 자식 노드를 가져옴
		const tag = $(this).parent('.filterOption').contents().filter(function() {
			// 현재 노드 타입이 text 노드인 값만 가져옴
			return this.nodeType === Node.TEXT_NODE;
		}).text().trim();

		// span 태그의 text만 출력
		console.log('tag : [' + tag + ']')

		// 태그 제거
		$(this).parent('.filterOption').remove();

		// 해당 테그와 일치하는 체크박스 해제
		$('input[type="checkbox"]').filter(function() {
			// 해당 체크박스의 data-role 값 가져오기
			const checkBoxValue = $(this).attr('data-role');
			console.log('checkBox의 값 : [' + checkBoxValue + '], tag 값 : [' + tag + ']');

			return checkBoxValue === tag;
		}).prop('checked', false);

		checkBoxCheck();
	});
}

// 가게 페이지네이션을 위한 함수
function storePagenation() {

	// id가 pagenationValue인 것을 클릭했다면
	$('.pagenationValue').on('click', function() {
		console.log('menuCheckBox : storePagenation() 시작');

		// a태그 링크를 잠시 멈춤
		console.log("페이지에이션 버튼 클릭");

		// 값들을 가져오기 위힌 배열
		var menu_arr = [];
		var payment_arr = [];
		var storeClosed = null;
		let checkBoxUrl = '';
		console.log("checkBoxUrl : " + checkBoxUrl);

		// input 태그 중 name 값이 storeMenu이면서 checked 된 값들 menu_arr에 넣음
		$("input[name=storeMenu]:checked").each(function() {
			const menu = document.querySelectorAll('input[name="storeMenu"]:checked');
			menu_arr.push(menu);
			console.log("menu : " + menu);
			console.log("menu_arr : " + menu_arr);
		})
		// input 태그 중 name 값이 storePayment이면서 checked 된 값들 payment_arr에 넣음
		$("input[name=storePayment]:checked").each(function() {
			const payment = document.querySelectorAll('input[name="storePayment"]:checked');
			payment_arr.push(payment);
			console.log("payment : " + payment);
			console.log("payment_arr : " + payment_arr);
		})
		// input 태그 중 name 값이 storeClosed이면서 이면서 checked 된 값들 storeClosed에 넣음
		$("input[name=storeClosed]:checked").each(function() {
			storeClosed = $(this).val().trim();
			console.log("storeClosed : " + storeClosed);
		})

		// url에 넣을 checkBoxUrl 생성 부분
		// menu_arr에 있는 요소들을 '&storeMenu='와 붙여서 넣음
		menu_arr.forEach(function(menu) {
			checkBoxUrl += '&storeMenu=' + encodeURIComponent(menu);
		})
		// payment_arr에 있는 요소들을 '&storePayment='와 붙여서 넣음
		payment_arr.forEach(function(payment) {
			checkBoxUrl += '&storePayment=' + encodeURIComponent(payment);
		})
		// storeClosed가 존재한다면 '&storeClosed='을 붙여서 넣음
		if (storeClosed != null) {
			checkBoxUrl += '&storeClosed=' + encodeURIComponent(storeClosed);
		}

		// 페이지네이션 관련 요소들 가져옴
		let preLink = document.getElementById('pagenationPreValue');
		let nextLink = document.getElementById('pagenationNextValue');
		let pageLinks = document.querySelectorAll('#pagination a[class^="pagenationValue"]');

		console.log('pageLinks : [' + pageLinks + ']');

		if (pageLinks.length > 0) {
			pageLinks.forEach(function(link) {
				link.addEventListener('click', function(event) {
					event.preventDefault();
					link.href += checkBoxUrl; // 검색 조건 추가
					window.location.href = link.href;
				});
			});
		}
		if (preLink) {
			preLink.addEventListener('click', function(event) {
				event.preventDefault();
				preLink.href += checkBoxUrl; // 검색 조건 추가
				window.location.href = preLink.href;
			});
		}
		if (nextLink) {
			nextLink.addEventListener('click', function(event) {
				event.preventDefault();
				nextLink.href += checkBoxUrl; // 검색 조건 추가
				window.location.href = nextLink.href;
			});
		}


		console.log("완료된 checkBoxUrl : " + checkBoxUrl);
	});
}
