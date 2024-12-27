// 페이지기 다 준비되면 작동
$(document).ready(function() {

	//폐점 신고버튼이 눌리면 작동
	$('#closedReport').on('click', function() {
		// 가게 번호를 받아옴
		// id가 storeNum인 요소에서 value 값을 가져옴
		var storeNum = document.getElementById('storeInfoNum').value;
		console.log('storeNum : [' + storeNum + ']');

		// 가게 번호가 존재할 때
		if (storeNum) {
			console.log('가게 번호가 존재함');

			// 비동기 처리 시작
			$.ajax({
				url: 'addReport.do',
				// POST 전송
				type: 'POST',
				// 가게 번호 전송
				data: { storeNum: storeNum },
				// 성공 시 
				success: function(data) {
					// 신고 결과를 받아옴
					console.log('폐점 신고 결과 : [' + data + ']');

					// 신고 결과에 따라 다른 알랏창을 띄움
					if (data == 'true') {
						console.log('가게 폐점 신고 완료');
						// 무사히 폐점 신고를 완료했다는 알랏창
						Swal.fire({
							icon: 'success',
							title: '폐점 신고 완료',
							html: '폐점 신고 완료했습니다. <br>관리자 확인 후 반영 됩니다.',
							confirmButtonText: '확인'
						});
					}
					else {
						console.log('가게 폐점 신고 완료');
						// 폐점 신고가 실패했다는 알랏창				
						Swal.fire({
							icon: 'error',
							title: '폐점 신고 실패',
							html: '폐점 신고에 실패했습니다. <br>다시 시도해 주세요.',
							confirmButtonText: '확인'
						});
					}
				}
			});
		}
		// 태그에 값이 존재하지 않을 때
		else {
			console.log('가게 번호가 존재하지 않음');

			// 알랏창으로 폐점신고 불가를 알림		
			Swal.fire({
				icon: 'error',
				title: '폐점 신고 실패',
				html: '폐점 신고에 실패했습니다. <br>다시 시도해 주세요.',
				confirmButtonText: '확인'
			});
		}
	});

	$('#deleteStore').on('submit', function(event) {
		event.preventDefault();//전송 방지
		Swal.fire({
			title: '가게 삭제 안내',
			text: "가게를 삭제 하시겠습니까?",
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: '확인',
			cancelButtonText: '취소'
		}).then((result) => {
			if (result.isConfirmed) {  // '확인' 버튼 클릭 시에만
				event.target.submit(); // 폼 제출
			}
		})
	});
});