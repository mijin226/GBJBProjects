// 페이지기 다 준비되면 작동
$(document).ready(function() {
	// 값이 그대로일 경우에 검색 방지를 위한 초기값 저장
	var preCity = $('#city').val();
	var preDistrict = $('#district').val();
	console.log('addressSearch.js : preCity : [' + preCity + ']');
	console.log('addressSearch.js : preDistrict : [' + preDistrict + ']');

	// id가 city인 것의 값이 변경되면
	$('#city').change(function() {
		console.log('addressSearch.js : select city가 변경됨');

		// 주소 검색 값들 가져옴
		// id가 city 값인 value 가져오기
		var city = $(this).val();
		console.log('addressSearch.js : city : [' + city + ']');

		// 만약 preCity 값과 city 값이 다르다면
		if (preCity !== city) {
			
			var address = city;
			
			console.log('addressSearch.js : address : [' + address + ']');

			// 검색 및 출력 작업
			selectSearch(address);

			// 값 변경해주기
			preCity = city;
		}
	});

	// id가 city인 것의 값이 변경되면
	$('#district').change(function() {
		console.log('addressSearch.js : select district가 변경됨');
		// id가 district 값인 value 가져오기
		var district = $(this).val();
		console.log('addressSearch.js : district : [' + district + ']');

		// 만약 preCity 값과 city 값이 다르다면
		if (preDistrict !== district) {
			// 나머지 주소 가져오기
			// id가 district 값인 value 가져오기
			var city = $('#city').val();
			console.log('addressSearch.js : city : [' + city + ']');

			// 두 주소 검색값을 하나로 합치기
			var address = city + ' ' + district;
			address = address.trim();
			console.log('addressSearch.js : address : [' + address + ']');

			// 검색 및 출력 작업
			selectSearch(address);

			// 값 변경해주기
			preDistrict = district;
		}
	});
});

function selectSearch(address) {
	// 검색한 가게들 가져오기
	searchStore(address)
		// 주소 검색이 들어가야 함
		.then(positions => {
			console.log('printMap.js : positions : [' + positions + ']');

			// 지도 생성
			var container = document.getElementById('addressSearchMap');
			//지도를 생성할 때 필요한 기본 옵션
			var options = {
				//지도의 중심좌표. 위도 경도 순으로 작성
				center: new kakao.maps.LatLng(33.450701, 126.570667),
				//지도의 레벨(확대, 축소 정도)
				level: 3
			};

			var map = new kakao.maps.Map(container, options);

			// 마커가 다 보이도록 지도 범위 재설정
			searchMapBounds(map, positions);
		})
		.catch(error => {
			console.error(error);
		});
}

// 마커를 생성하고 지도 범위 번경하는 함수
function searchMapBounds(map, positions) {
	console.log('printMap.js : searchMapBounds');
	// point 배열에 사용자의 현재 주소를 넣은 채 생성
	var points = [];
	// marker 배열을 담을 변수
	var markers = [];

	// positions 배열을 반복하여 points에 LatLng 객체를 추가
	for (var i = 0; i < positions.length; i++) {
		points.push(positions[i].latlng);
		console.log('printMap.js : positions[' + i + '].latlng : [' + positions[i].latlng + ']');
	}

	console.log('printMap.js : points : [' + points + ']');

	// LatLngBounds 객체를 생성 (경계 범위 객체)
	var bounds = new kakao.maps.LatLngBounds();


	for (var i = 0; i < points.length; i++) {
		// 배열의 좌표들이 잘 보이게 마커를 지도에 추가
		var markerOptions = { position: points[i] };
		console.log('printMap.js : ' + i + '번째 마커 작성');

		var marker = new kakao.maps.Marker(markerOptions);
		marker.setMap(map);
		markers.push(marker);

		// LatLngBounds 객체에 좌표를 추가
		bounds.extend(points[i]);

		// 마커에 마우스가 닿을 시 오버레이 표시
		showMarkerNameToDiv(marker, map, i, positions, true);
	}

	// map이 출력되는 크기, 위치 조정
	map.setBounds(bounds);

	// div에 마우스가 닿을 시 오버레이 표시
	showDivToMarkerName(map, positions, markers, true);

	console.log('printMap.js : 범위 재설정 완료');
}