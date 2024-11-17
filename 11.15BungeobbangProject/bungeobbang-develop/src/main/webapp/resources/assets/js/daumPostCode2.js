$(document).ready(function() {
    // 모달을 열고 Daum 주소 API 실행
    window.sample3_execDaumPostcode = function() {
        $('#fullscreenDaumPostcode').show(); // 전체 화면을 덮는 모달 열기

        // Daum 주소 API 실행
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                // 주소 유형에 따라 처리
                if (data.userSelectedType === 'R') {
                    addr = data.roadAddress;
                } else {
                    addr = data.jibunAddress;
                }

                // 도로명 주소일 때 참고 항목 추가
                if (data.userSelectedType === 'R') {
                    if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    if (data.buildingName !== '' && data.apartment === 'Y') {
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    if (extraAddr !== '') {
                        extraAddr = ' (' + extraAddr + ')';
                    }
                }

                // 주소 정보 세팅
                document.getElementById('postcode').value = data.zonecode;
                document.getElementById("addressMain").value = addr + extraAddr;
                document.getElementById("addressDetail").focus();

                // 모달 닫기
                $('#fullscreenDaumPostcode').hide();
            },
            width: '100%',
            height: '100%',
            maxSuggestItems: 5
        }).embed(document.getElementById('addressLayer'));
    };

    // 모달 닫기 버튼
    $('#btnCloseLayer').click(function() {
        $('#fullscreenDaumPostcode').hide();
    });
});
