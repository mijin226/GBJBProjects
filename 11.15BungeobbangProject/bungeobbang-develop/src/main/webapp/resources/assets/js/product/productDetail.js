$(document).ready(function() {
    // 페이지 로드 시 버튼 초기화
    $('#addToCartBtn').text('구매하기');
    $('#goToCartForm').hide(); // 장바구니 버튼 숨기기

    // 구매하기 버튼 클릭 시 비동기 요청 처리
    $('#addToCartBtn').on('click', function(event) {
        event.preventDefault(); // 기본 동작 방지
        
        // 수량과 상품 번호 가져오기
        var quantity = $('#quantity').val();
        var productNum = $('#productNum').val();
        console.log(productNum);
		
        // AJAX 요청
        $.ajax({
            url: 'addToCart.do', // 서버 측 경로와 일치하는지 확인 필요
            type: 'POST',
            contentType: 'application/json', // JSON 형식으로 전송
            data: JSON.stringify({
                productNum: productNum,
                quantity: quantity
            }),
            success: function(response) {
                // 서버가 성공적으로 응답했을 때의 처리
                if (response.status === 'success') {
                    $('#addToCartBtn').text('더 추가하기'); // 버튼 텍스트 변경
                    $('#goToCartForm').show(); // 장바구니로 가기 버튼 표시
                    
                    // SweetAlert로 성공 메시지 출력
                    Swal.fire({
                        icon: 'success',
                        title: '성공!',
                        text: response.message,
                        confirmButtonText: '확인'
                    });
                } else {
                    // SweetAlert로 오류 메시지 출력
                    Swal.fire({
                        icon: 'error',
                        title: '오류 발생',
                        text: response.message,
                        confirmButtonText: '확인'
                    });
                }
            },
            error: function(xhr, status, error) {
                // 서버 응답에 오류가 있을 때의 처리
                console.error("Error: " + error);
                
                // SweetAlert로 오류 메시지 출력
                Swal.fire({
                    icon: 'error',
                    title: '오류 발생',
                    text: '장바구니에 담는 중 오류가 발생했습니다. 다시 시도해주세요.',
                    confirmButtonText: '확인'
                });
            }
        });
    });
});
