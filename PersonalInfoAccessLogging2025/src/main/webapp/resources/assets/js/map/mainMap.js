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
		// 지도의 이동, 확대/축소를 막음
		draggable: false,
		//지도의 레벨(확대, 축소 정도)
		level: 4
	};

	var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

	// 지도 확대 축소를 제어할 수 있는 줌 컨트롤을 생성
	var zoomControl = new kakao.maps.ZoomControl();
	// 생성한 줌 컨트롤러를 지도의 오른쪽에 추가
	map.addControl(zoomControl, kakao.maps.ControlPosition.BOTTOMRIGHT);

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
				return searchStoreNotPrint(address);
			})
			// 주소 검색이 들어가야 함
			.then(positions => {
				console.log('printMap.js : positions : [' + positions + ']');

				// 지도에 마커 출력
				createMapAndMarker(positions, map);
			})
			.catch(error => {
				console.error(error);
			});
	});

	// div의 크기가 변경될 때마다 지도의 중심을 사용자 위치로 다시 설정
	$(window).resize(function() {
		// 내 주소값이 존재한다면
		if (myAddress) {
			console.log('printMap.js : div 크기 변경 - 중심 재설정');
			map.setCenter(myAddress); // 지도의 중심을 내 위치로 재설정
		}
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
function searchStoreNotPrint(address) {
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

				// 주소를 좌표로 변환할 객체 생성
				var geocoder = new kakao.maps.services.Geocoder();

				// 데이터가 비어있다면
				if (data.length === 0) { // 수정: length가 0일 때

				} else {
					// 주소의 좌표를 보관할 배열 생성
					var positions = [];
					// 모든 비동기 작업을 보관할 배열
					var promises = [];

					// 온 data 갯수만큼 하나씩 코드를 작성하여 넣음
					data.forEach(function(item) {
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

// 마커에 마우스를 가져다대면 위에 가게 이름 띄우고, storeName 클래스의 div를 표시하는 함수
function showMarkerNameAndAddress(marker, map, i, positions, search) {
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

				// positions에서 오버레이에 사용할 이름, 주소를 가져옴 
				const storeName = positions[index].store.storeName;
				const storeAddress = positions[index].store.storeAddress;
				const storeAddressDetail = positions[index].store.storeAddressDetail;

				// 오버레이 content 설정
				overlay.setContent('<div id="marker" style="padding:5px; z-index:1; white-space: nowrap; border-radius: 10px; background-color: white; border: 3px solid orange;">'
					+ '<b style="color: orange">' + storeName + '</b><br>'
					+ '<span style="font-size: 12px;">' + storeAddress + '<br>' + storeAddressDetail + '</span></div>');

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

// map을 생성하고 마커 표시
function createMapAndMarker(positions, map) {
	// map에 표시
	for (var i = 0; i < positions.length; i++) {

		// 마커를 생성
		var marker = new kakao.maps.Marker({
			map: map,
			position: positions[i].latlng,
			title: positions[i].title
		});

		// 마커에 마우스가 닿을 시 오버레이 표시
		showMarkerNameAndAddress(marker, map, i, positions, true);
	}
}