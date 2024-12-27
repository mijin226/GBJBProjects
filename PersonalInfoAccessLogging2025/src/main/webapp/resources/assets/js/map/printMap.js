// 페이지가 모두 준비되면 작동
$(document).ready(function() {
	console.log("printMap.js : 페이지 준비 완료");

	// 지도 생성
	//지도를 담을 영역의 DOM 레퍼런스
	var container = document.getElementById('addressSearchMap');
	//지도를 생성할 때 필요한 기본 옵션
	var options = {
		//지도의 중심좌표. 위도 경도 순으로 작성
		center: new kakao.maps.LatLng(33.450701, 126.570667),
		//지도의 레벨(확대, 축소 정도)
		level: 3
	};

	var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

	// map 객체 생성 확인
	console.log('printMap.js : map 생성 확인 : [' + map + ']');

	// 내 위치를 담을 좌표 변수
	var myAddress;

	// 지도 생성 후 작동되는 코드들
	// 맵이 로드가 된 후 작동
	kakao.maps.load(function() {
		console.log("printMap.js : 맵 로드 완료 - map 출력 시작");

		// 내 위치 중심으로 표시
		geolocation(map)
			.then(locPosition => {
				// locPosition에서 위도와 경도를 얻음
				console.log('printMap.js : 사용자의 위치 : [' + locPosition + ']');

				// 내 주소를 담아놓음
				myAddress = locPosition;
				console.log('printMap.js : myAddress : [' + myAddress + ']');

				// 주소 검색이 들어가야 함
				// 주소값을 반환
				return latLngToAddress(locPosition);
			})
			.then(address => {
				// 가게 검색
				return searchStore(address);
			})
			// 주소 검색이 들어가야 함
			.then(positions => {
				console.log('printMap.js : positions : [' + positions + ']');

				// 마커가 다 보이도록 지도 범위 재설정
				mapBounds(map, positions, myAddress);
			})
			.catch(error => {
				console.error(error);
			});

	});
});


// 지도에 마커와 인포윈도우를 표시하는 함수
function displayMarker(locPosition, message, map) {
	console.log("printMap.js : 지도 위에 마커 표시 시작");

	// 현재 위치 마커는 특별하게 표시
	var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
	var imageSize = new kakao.maps.Size(24, 35);
	var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

	// 마커를 생성
	// map이라는 지도 객체에 locPosition에서 지정한 좌표로 설정
	var marker = new kakao.maps.Marker({
		map: map,
		position: locPosition,
		image: markerImage
	});

	// 마커 생성 확인 
	console.log('printMap.js : marker 확인 : [' + marker + ']')

	// 인포 윈도우에 표시할 내용
	// 표시 내용가 인포윈도를 닫을 수 있도록 허용 
	var iwContent = message;

	// 인포윈도우를 생성
	var infowindow = new kakao.maps.InfoWindow({
		content: iwContent
	});

	// 인포윈도우를 마커위에 표시
	infowindow.open(map, marker);

	// 지도 중심좌표를 접속위치로 변경
	map.setCenter(locPosition);
}

// 사용자의 위치를 찾아 표시하는 함수
function geolocation(map) {
	console.log("printMap.js : 맵 로드 완료 - geolocation 시작");

	// promise를 사용하여 위치값 반환
	// 작업이 성공할 시 resolve 실행, 실패 시 reject 실행
	return new Promise((resolve, reject) => {
		// HTML5의 geolocation으로 사용할 수 있는지 확인
		// 사용자의 위치를 확인할 수 있는지 여부를 판단하는 조건
		if (navigator.geolocation) {
			console.log('printMap.js : 사용자 위치 확인 가능');

			// GeoLocation을 이용해서 접속 위치를 얻어옴
			// 사용자의 위치좌표를 콜백 함수로 전달
			navigator.geolocation.getCurrentPosition(function(position) {
				// 현재 좌표값을 가져와 저장
				// lat : 위도, lon : 경도
				var lat = position.coords.latitude,
					lon = position.coords.longitude;

				// 좌표 로그
				console.log('printMap.js : 좌표 : [' + lat, lon + ']');

				// 카카오 지도에서 사용할 수 있는 좌표객체 생성
				// locPosition: geolocation으로부터 마커가 표시될 위치 좌표를 생성
				// message: 인포 윈도우에 표시될 내용
				var locPosition = new kakao.maps.LatLng(lat, lon),
					message = '<div id="marker" style="display: inile-block; padding:5px; z-index:1; white-space: nowrap; border-radius: 10px; background-color: white; border: 3px solid orange;">현재 위치</div>';

				// 좌표 객체 확인
				console.log('printMap.js : 좌표 객체 : [' + locPosition + ']');

				// 마커와 인포윈도우를 표시
				displayMarker(locPosition, message, map);

				// locPosition 반환
				resolve(locPosition);

			}, function(error) {
				// 실패 시
				console.log('printMap.js : 사용자 위치 확인 불가능', error);
				reject('printMap.js : 사용자의 위치를 찾을 수 없습니다.');
			});

		}
		// 만약 확인할 수 없다면
		// 마커 표시 위치와 인포윈도우 내용을 설정
		else {
			console.log('printMap.js : 사용자 위치 확인 불가능');
			// 기본 좌표를 지정
			// 나중에 가게 하나를지정하여 그곳 위치를 찝음
			var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),
				// 
				message = 'geolocation을 사용할수 없어요..'

			displayMarker(locPosition, message, map);
			reject('printMap.js : 사용자의 위치를 찾을 수 없습니다.');
		}

	});
}

// 좌표를 주소로 변경하는 함수
function latLngToAddress(locPosition) {

	// 비동기 함수 순서를 위한 promise 객체
	return new Promise((resolve, reject) => {
		var geocoder = new kakao.maps.services.Geocoder();

		// locPosition에서 경도와 위도를 가져옴
		var lat = locPosition.getLat();
		var lon = locPosition.getLng();

		// 좌표를 주소로 변환
		geocoder.coord2Address(lon, lat, function(result, status) {
			// 주소 변경이 성공하면
			if (status === kakao.maps.services.Status.OK) {
				// 변경된 주소값을 가져옴
				// 깊이 3까지의 주소를 가져오기
				var address = result[0].address.region_1depth_name
					+ ' ' + result[0].address.region_2depth_name;
				console.log('printMap.js : 주소: ', address);

				// 완성된 주소값을 반환
				resolve(address);

			} else {
				console.error('printMap.js : 주소 변환 실패: ', status);
				reject('printMap.js : 주소 변환 실패');
			}
		});
	});
}

// 주소로 가게들을 검색하는 함수
function searchStore(address) {
	// ajax 실행
	return new Promise((resolve, reject) => { // Promise를 반환하도록 수정
		$.ajax({
			// json 형식으로 전달
			url: 'loadListStoreMap.do',
			type: 'POST',
			// 요청 데이터 application/json
			contentType: 'application/json',
			data: JSON.stringify({ storeAddress: address }),
			dataType: 'json',
			// 성공 시
			success: function(data) {
				// data 존재 확인 로그 (데이터 길이만 확인)
				console.log('printMap.js : data.length : [' + data.length + ']');

				// #storeList 가져오기
				var storeList = $('#storeList');
				console.log('printMap.js : storeList : ', storeList);

				// 기존 데이터 초기화
				storeList.empty();

				// 주소를 좌표로 변환할 객체 생성
				var geocoder = new kakao.maps.services.Geocoder();

				// 데이터가 비어있다면
				if (data.length === 0) { // 수정: length가 0일 때
					storeList.append('<span>검색 결과 없음</span>');
					resolve([]); // 비어있는 배열 반환
				} else {
					// 주소의 좌표를 보관할 배열 생성
					var positions = [];
					// 모든 비동기 작업을 보관할 배열
					var promises = [];

					// 온 data 갯수만큼 하나씩 코드를 작성하여 넣음
					data.forEach(function(item) {
						// <customStore:simpleStoreData/> 출력
						append(storeList, item);

						// 지도에 표시할 주소
						var itemAddress = item.storeAddress + ' ' + item.storeAddressDetail;
						console.log('printMap.js itemAddress :' + itemAddress + ']');

						// 주소로 좌표를 검색
						var promise = new Promise((innerResolve, innerReject) => {
							geocoder.addressSearch(itemAddress, function(result, status) {
								// 정상적으로 검색이 완료됐다면
								if (status === kakao.maps.services.Status.OK) {
									console.log('printMap.js : 주소를 좌표로 변경 완료');

									// positions에 값 추가
									positions.push({
										store: item,
										latlng: new kakao.maps.LatLng(result[0].y, result[0].x)
									});

									innerResolve(); // innerPromise 해결
								} else {
									innerReject(status); // 실패 시 reject
								}
							});
						});
						promises.push(promise); // 모든 비동기 작업을 promises 배열에 추가
					});

					// 모든 비동기 작업이 완료된 후 처리
					Promise.all(promises)
						.then(() => {
							resolve(positions); // 최종적으로 positions 반환
						})
						.catch((error) => {
							console.error('printMap.js : 주소 변환 중 오류 발생: ', error);
							reject(error); // 오류 발생 시 reject
						});
				}
			},
			// ajax 요청 실패 시
			error: function(err) {
				console.error('printMap.js : ajax 요청 실패 : ', err);
				reject(err); // ajax 실패 시 reject
			}
		});
	});
}


// jsp 파일에 추가될 요소 코드
function append(storeList, item) {
	storeList.append(
		'<div class="storeData">'
		+ '<div class="storeDataTitle">'
		+ '<a href="infoStore.do?storeNum=' + item.storeNum + '">'
		+ '<h4 class="text-hover">' + item.storeName + '</h4></a>'
		+ '</div>'
		+ '<div class="storeDataContent">'
		+ '<div class="col-1 nonePadding">'
		+ '<i class="fas fa-map"></i>'
		+ '</div>'
		+ '<div class="col-11 leftPadding text-start">'
		+ '<span  id="address">' + item.storeAddress + ' <br> ' + item.storeAddressDetail
		+ '</span>'
		+ '</div>'
		+ '</div>'
		+ '<div class="storeDataContent">'
		+ '<div class="col-1 nonePadding">'
		+ '<i class="fas fa-phone"></i>'
		+ '</div>'
		+ '<div class="col-11 leftPadding text-start">'
		+ '<span>' + item.storeContact + '</span>'
		+ '</div>'
		+ '</div>'
		+ '</div>'
	);
}

// 마커를 생성하고 지도 범위 번경하는 함수
function mapBounds(map, positions, myAddress) {
	console.log('printMap.js : mapBounds');
	// point 배열초기화
	var points = [];

	// marker 배열을 담을 변수
	var markers = [];

	// 사용자의 현재 주소를 가장 처음 넣음
	points.push(myAddress);
	// myAddress가 잘 들어갔는지 확인
	console.log('printMap.js : points 1 : [' + points + ']');
	console.log('printMap.js : points.length : [' + points.length + ']');

	// positions 배열을 반복하여 points에 LatLng 객체를 추가
	for (var i = 0; i < positions.length; i++) {
		points.push(positions[i].latlng);
		console.log('printMap.js : positions[' + i + '].latlng : [' + positions[i].latlng + ']');
	}

	console.log('printMap.js : points 2 : [' + points + ']');
	console.log('printMap.js : points.length : [' + points.length + ']');

	// LatLngBounds 객체를 생성 (경계 범위 객체)
	var bounds = new kakao.maps.LatLngBounds();

	// 현재 위치 마커를 위한 이미지 설정
	var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
	var imageSize = new kakao.maps.Size(24, 35);
	var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);


	for (var i = 0; i < points.length; i++) {
		// 배열의 좌표들이 잘 보이게 마커를 지도에 추가
		var markerOptions = { position: points[i] };
		// 첫 번째 마커만 특별하게 표시
		if (i == 0) {
			markerOptions.image = markerImage;
		}
		console.log('printMap.js : ' + i + '번째 마커 작성');

		var marker = new kakao.maps.Marker(markerOptions);
		marker.setMap(map);
		markers.push(marker);

		// LatLngBounds 객체에 좌표를 추가
		bounds.extend(points[i]);

		// 마커에 마우스가 닿을 시 오버레이 표시
		showMarkerNameToDiv(marker, map, i, positions, false);
	}

	// map이 출력되는 크기, 위치 조정
	map.setBounds(bounds);

	// div에 마우스가 닿을 시 오버레이 표시
	showDivToMarkerName(map, positions, markers, false);

	console.log('printMap.js : 범위 재설정 완료');
}

// 마커에 마우스를 가져다대면 위에 가게 이름 띄우고, storeName 클래스의 div를 표시하는 함수
function showMarkerNameToDiv(marker, map, i, positions, search) {
	console.log('printMap.js : showMarkerNameToDiv');

	// 오버레이 생성
	let overlay = new kakao.maps.InfoWindow({
	});

	// 검색한 후인지 처음 페이지를 시작한 것인지에 따라 index 값이 달라짐
	var index = i;

	// 마커에 마우스가 닿았을 때
	kakao.maps.event.addListener(marker, 'mouseover', (function(marker, overlay) {
		return function() {

			// 처음 페이지에 시작한 경우 index 값을 -1 함
			if (!search) {
				index = i - 1;
			}
			console.log('printMap.js : search : [' + search + ']');

			// 오버레이 작동은 언제나 index값이 0이상일 때 작동
			if (index >= 0) {

				console.log('printMap.js : 마우스에 닿은 마커 i 값 : [' + index + ']');

				// positions에서 오버레이에 사용할 이름을 가져옴 
				const storeName = positions[index].store.storeName;

				// 오버레이 content 설정
				overlay.setContent('<div id="marker" style="padding:5px; z-index:1; white-space: nowrap; border-radius: 10px; background-color: white; border: 3px solid orange;">'
								+ '<b style="color: orange">' + storeName + '</b></div>');


				// position의 주소값 가져오기
				var markerAddress = positions[index].store.storeAddress + positions[index].store.storeAddressDetail;
				var markerName = positions[index].store.storeName;

				// 비교를 위해 공백 지우기
				markerAddress = markerAddress.replace(/\s+/g, '');
				markerName = markerName.replace(/\s+/g, '');
				console.log('printMap.js : markerAddress : [' + markerAddress + ']');
				console.log('printMap.js : markerName : [' + markerName + ']');

				// storeData 클래스인 요소 불러와서 반복
				$('.storeData').each(function() {
					// 요소의 주소, 가게이름 가져오기
					var childElAddress = $(this).find('span[id="address"]');
					var childElName = $(this).find('h4').text().trim();

					// text 형식으로 변경 후 비교를 위한 공백 제거
					var childAddressText = childElAddress.text().replace(/\s+/g, '');
					childElName = childElName.replace(/\s+/g, '');
					console.log('printMap.js : childAddressText : [' + childAddressText + ']');
					console.log('printMap.js : childElName : [' + childElName + ']');

					// 마커와 요소의 이름, 주소 비교
					const isNameMatch = childElName === markerName;
					const isAddressMatch = childAddressText === markerAddress;
					console.log('printMap.js : childAddressText, childElName : [' + isNameMatch + ', ' + isAddressMatch + ']');

					// 요소의 주소가 존재하고 이름, 주소 둘 다 존재하는 값이 있다면
					if (childElAddress.length && isAddressMatch && isNameMatch) {
						// 굵기를 굵게 변경
						$(this).css('border', '5px solid #d8c15a');

						// scrollContainer 클래스의 스크롤이 해당 요소가 보이도록 움직임
						$('.scrollContainer').animate({
							scrollTop: $(this).position().top + $('.scrollContainer').scrollTop()
							// 800의 속도로 이동
						}, 800);

						// 반복문 종료
						return false;
					} else {
						console.log('printMap.js : 일치하지 않음');
					}
				});

				// 오버레이 열기
				overlay.open(map, marker);
			}
		};
	})(marker, overlay));

	// 마커에 마우스가 벗어난다면
	kakao.maps.event.addListener(marker, 'mouseout', function() {
		// 오버레이 닫기
		overlay.close();
		// 굵기 원래대로 변경
		$('.storeData').css('border', '');
		console.log('printMap.js : 요소들 굵기 원래대로');
	});
}

// div에 마우스를 가져다 댔을 때 마커에도 오버레이가 표시되는 함수
function showDivToMarkerName(map, positions, markers, search) {
	var overlay = null; // overlay를 초기화하여 함수 범위에서 정의

	$('.storeData').on('mouseover', function() {
		// 선 굵기를 변경
		$(this).css('border', '5px solid #d8c15a');

		var childElName = $(this).find('h4').text().trim();
		var childAddressText = $(this).find('span[id="address"]').text().trim();

		childElName = childElName.replace(/\s+/g, '');
		childAddressText = childAddressText.replace(/\s+/g, '');

		markers.forEach((marker, i) => {
			let index = search ? i : i - 1; // index 정의

			// index 유효성 체크
			if (index < 0 || index >= positions.length) {
				console.warn(`printMap.js : 유효하지 않은 index 값: ${index}`);
				return; // 유효하지 않으면 다음으로 넘어감
			}

			// positions[index]가 undefined인지 체크
			const position = positions[index];
			if (!position || !position.store) {
				console.warn(`printMap.js : position이 유효하지 않음: ${position}`);
				return; // position이 유효하지 않으면 다음으로 넘어감
			}

			// 마커의 이름, 주소 가져오기
			var markerName = position.store.storeName;
			var markerAddress = position.store.storeAddress + position.store.storeAddressDetail;

			// 비교를 위해 공백 지우기
			markerName = markerName.replace(/\s+/g, '');
			markerAddress = markerAddress.replace(/\s+/g, '');
			console.log('printMap.js : markerAddress : [' + markerAddress + ']');
			console.log('printMap.js : markerName : [' + markerName + ']');

			// 마커와 요소의 이름, 주소 비교
			const isNameMatch = childElName === markerName;
			const isAddressMatch = childAddressText === markerAddress;
			console.log('printMap.js : childAddressText, childElName : [' + isNameMatch + ', ' + isAddressMatch + ']');

			// 이름, 주소 둘 다 존재하는 값이 있다면
			if (isNameMatch && isAddressMatch) {
				// overlay가 없을 때만 생성
				if (!overlay) {
					overlay = new kakao.maps.InfoWindow({});
				}

				// 오버레이의 내용 설정
				overlay.setContent('<div id="marker" style="padding:5px; z-index:1; white-space: nowrap; border-radius: 10px; background-color: white; border: 3px solid orange;">'
												+ '<b style="color: orange">' + markerName + '</b></div>');
				// 마커 위에 오버레이 표시
				overlay.open(map, marker);
				console.log('printMap.js : 일치하는 마커에 오버레이 표시');

				// forEach 종료
				return false; // 반복문 종료
			}
		});
		// 마우스가 벗어났다면
	}).on('mouseout', function() {
		if (overlay) {
			// 오버레이 닫기
			overlay.close();
			// overlay를 null로 초기화하여 재사용 방지
			overlay = null;
		}
		// 굵기 요소를 원래대로
		$('.storeData').css('border', '');
	});
}
