//취소 클릭 시 스위트알랏창 종료
$('.cancel').on('click', function(){
	console.log('clicked');
})

//운영중인 붕어빵 가게 제보 승인 및 취소 처리
//미등록되어 있는 붕어빵 가게 제보글 승인(=등록+공개상태 전환)
$('.storeTipOffList').on('click', function() {
	console.log('storeDeclareList.js storeTipOffList start');

	//[1] 승인버튼 클릭한 가게 고유번호 불러오기
	const storeNum = $(this).data('storeNum');
	console.log('storeDeclareList.js storeTipOffList storeNum load');

	//[2] 정말 승인절차 진행할 지 안내창 띄우기
	Swal.fire({
		icon: 'warning',
		title: '미등록된 가게 제보승인',
		text: '정말 가게 등록 처리하겠습니까? 확인을 누르면 해당 가게가 공개상태로 전환되며 사용자에게 보여집니다.',
		showCancelButton: true,//취소 버튼 생성
		confirmButtonText: '확인',
		cancelButtonText: '취소'

		//[3] 	확인 : 해당 가게 공개상태 전환+사용자에게 가게 정보 공개(updateStoreVisible.do) 
		//		취소 : 가게 폐점제보 페이지로(loadListStoreTipOff.do)
	}).then((result) => {
		if (result.isConfirmed) {
			//updateStoreVisible.do 비동기 진행
			//비동기 결과 true시, 해당 가게가 공개 상태로 전환되었습니다.
			//처리도중 문제가 발생해 실행되지 않았습니다. 다시 시도해주세요. 재발생 시 본사에 문의 바랍니다.
			//ajax를 통해 비동기처리 진행
			$.ajax({
				url: 'updateStoreVisible.do',
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify({
					storeNum: storeNum,
				}),
				dataType: 'json',
				success: function(result) {
					if (result.true) {	//반환값 boolean true인 경우
						Swal.fire({
							icon: 'success',
							title: '가게 공개상태 전환',
							text: '해당 가게가 공개 상태로 전환되었습니다. 제보글이 삭제됩니다.',
							confirmButtonText: '확인',
						})
					} else {	//반환값 boolean false인 경우
						Swal.fire({
							icon: 'error',
							title: '에러 발생',
							text: '처리도중 문제가 발생해 실행되지 않았습니다. 다시 시도해주세요. 재발생 시 본사에 문의 바랍니다.',
							confirmButtonText: '확인'
						});
					}
				},
				error: function() {
					console.error('AJAX 요청 실패:', error);
					Swal.fire({
						icon: 'error',
						title: '오류',
						text: '서버와의 통신에 실패했습니다.',
						confirmButtonText: '확인'
					});
				}
			});
		}
	})
});


//미등록되어 있는 붕어빵 가게 제보글 취소(삭제)
$('#deleteStoreTipOffList').on('click', function() {
	console.log('storeDeclareList.js deleteStoreTipOffList start');

	//[1] 취소 버튼 클릭한 가게 고유번호 불러오기
	const storeNum = null//document.getElementByid('storeNum').value;
	console.log('storeDeclareList.js deleteStoreTipOffList storeNum load');

	//[2] 정말 취소절차 진행할 지 안내창 띄우기
	Swal.fire({
		icon: 'warning',
		title: '미등록된 가게 제보글 삭제',
		text: '정말 미등록되니 가게 제보글을 삭제하겠습니까? 확인을 누르면 해당 가게 제보는 삭제됩니다.',
		showCancelButton: true,//취소 버튼 생성
		confirmButtonText: '확인',
		cancelButtonText: '취소'

		//[3]	확인 : 해당 가게 고유번호 넘겨서 제보글 삭제(deleteReport.do)
		//		취소 : 가게 제보 페이지로(storeTipOffList.do)
	}).then((result) => {
		if (result.isConfirmed) {
			//deleteReport.do 비동기 진행
			//비동기 결과 true시, 제보글이 삭제되었습니다.
			//처리도중 문제가 발생해 실행되지 않았습니다. 다시 시도해주세요.
			//ajax를 통해 비동기처리 진행
			$.ajax({
				url: 'deleteReport.do', //폐점 제보글 삭제(closedStoreDeclareList.js)와 동일
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify({
					storeNum: storeNum,
				}),
				dataType: 'json',
				success: function(result) {
					if (result.true) {	//반환값 boolean true인 경우
						Swal.fire({
							icon: 'success',
							title: '미등록된 가게 제보글 삭제',
							text: '가게 제보글이 삭제되었습니다.',
							confirmButtonText: '확인',
						})
					} else {	//반환값 boolean false인 경우
						Swal.fire({
							icon: 'error',
							title: '에러 발생',
							text: '처리도중 문제가 발생해 실행되지 않았습니다. 다시 시도해주세요. 재발생 시 본사에 문의 바랍니다.',
							confirmButtonText: '확인'
						});
					}
				},
				error: function() {
					console.error('AJAX 요청 실패:', error);
					Swal.fire({
						icon: 'error',
						title: '오류',
						text: '서버와의 통신에 실패했습니다.',
						confirmButtonText: '확인'
					});
				}
			});
		}
	})
});