// JSON 파일에서 도시 리스트를 가져와 select에 option으로 추가
document.addEventListener('DOMContentLoaded', () => {
    // JSON 파일 경로
    const jsonFilePath = 'resources/assets/json/address.json';

    // JSON 파일에서 데이터를 가져올 변수
    let cityList;

    // JSON 파일 경로
    fetch(jsonFilePath)
        .then(response => {
            console.log('selectList.js : address.json 파일 요청');
            if (!response.ok) {
                console.log('selectList.js : json 파일 요청 실패');
                throw new Error('Network response was not ok, status: ' + response.status);
            }
            console.log('selectList.js : json 파일 요청 성공');
            return response.json();
        })
        .then(data => {
            // JSON 데이터를 cityList 변수에 할당
            cityList = data;
            // 확인용으로 출력
            console.log('selectList.js : city : [' + cityList + ']');

            // select의 각 요소 값 가져오기
            const citySelect = document.getElementById('city');
            const districtSelect = document.getElementById('district');
            console.log('selectList.js : citySelect : [' + citySelect + ']');
            console.log('selectList.js : districtSelect : [' + districtSelect + ']');

            // select에 넣기
            // cityList의 key 값을 city라는 이름으로 하나씩 넣기
            Object.keys(cityList).forEach(city => {
                const option = document.createElement('option');
                option.value = city;
                option.textContent = city;
                citySelect.appendChild(option);
            });

            // 구는 초기에는 비활성화
            districtSelect.disabled = true;

            // 도시 선택 시 구 옵션 추가
            citySelect.addEventListener('change', function() {
                const selectedCity = citySelect.value;
                const districts = cityList[selectedCity] || [];
                districtSelect.innerHTML = ''; // 기존 구 옵션 제거

                // 구 옵션 추가
                districts.forEach(district => {
                    const option = document.createElement('option');
                    option.value = district;
                    option.textContent = district;
                    districtSelect.appendChild(option);
                });

                // 구가 선택 가능하도록 활성화
                districtSelect.disabled = false;
            });
        })
        .catch(error => {
            console.log('selectList.js : selectList.js 실행 중 에러 발생');
            console.error('There has been a problem with your fetch operation:', error);
        });
});
