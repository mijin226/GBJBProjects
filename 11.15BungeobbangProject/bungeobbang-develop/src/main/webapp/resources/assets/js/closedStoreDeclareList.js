//폐점 신고 제보 승인 및 취소 처리 스크립트
/*
필요한 값 : 가게 고유번호
제보 승인 : 정말 폐점 처리하겠습니까? 확인을 누르면 폐점 상태로 전환됩니다. (확인/취소)
제보 삭제 : 폐점 신고된 글을 삭제하겠습니까? 확인을 누르면 신고된 제보는 삭제됩니다.(확인/취소)
 */

//폐점신고 제보승인
//인자값 : 승인버튼 클릭한 가게 고유번호 불러오기
function addReport(storeNum) {
	console.log('closedStoreDeclareList.js addReport start');
	console.log('closedStoreDeclareList.js addReport storeNum load');

	//정말 승인절차 진행할 지 안내창 띄우기
	Swal.fire({
		icon: 'warning',
		title: '폐점신고 제보승인',
		text: '정말 폐점 처리하겠습니까? 확인을 누르면 폐점상태로 전환됩니다.',
		showCancelButton: true,		//취소 버튼 생성
		confirmButtonText: '확인',
		cancelButtonText: '취소'

		// 	확인 : 해당 가게 폐점 전환+제보글 삭제
		//	취소 : 가게 폐점제보 페이지로
	}).then((result) => {
		if (result.isConfirmed) {

			console.log('log: 폐점처리 진행');
			sendDate('updateStoreClose.do', storeNum);
		}
	})
}


//폐점 신고 제보삭제
//인자값 : 제보삭제 클릭한 가게 고유번호 불러오기
function deleteReport(storeNum) {
	console.log('closedStoreDeclareList.js deleteReport start');
	console.log('closedStoreDeclareList.js deleteReport storeNum load');

	//정말 취소절차 진행할 지 안내창 띄우기
	Swal.fire({
		icon: 'warning',
		title: '폐점신고 제보취소',
		text: '폐점 신고된 글을 삭제하겠습니까? 확인을 누르면 신고된 제보는 삭제됩니다.',
		showCancelButton: true,		//취소 버튼 생성
		confirmButtonText: '확인',
		cancelButtonText: '취소'

		//	확인 : 해당 가게 고유번호 넘겨서 제보글 삭제
		//	취소 : 가게 폐점제보 페이지로
	}).then((result) => {
		if (result.isConfirmed) {
			console.log("log: 폐점 신고 취소처리");
			sendDate('deleteReport.do', storeNum)
		}
	})
}

//받아온 값을 보내는 함수
			//action : deleteReport.do
			function sendDate(action, storeNum) {
				
				//form 변수 : 전달방식 설정
				let form = document.createElement("form");
				form.style.display = "none";
				form.method = "POST"; // POST 요청 방식
				form.action = action; // 요청을 보낼 URL

				//data 변수 : 보낼 데이터 설정(가게 고유번호)
				let data = document.createElement("input");
				data.type = "hidden";
				data.name = "storeNum";
				data.value = storeNum;

				//data를 인자값으로 하여 form에 넣기
				form.appendChild(data);
				//form을 현재 페이지에 추가한 후 전송
				document.body.appendChild(form);
				form.submit();
			}

//addReport.do 비동기 진행
			//비동기 결과 true시, 폐점된 가게로 전환되었습니다. 제보글이 삭제됩니다.
			//처리도중 문제가 발생해 실행되지 않았습니다. 다시 시도해주세요. 재발생 시 본사에 문의 바랍니다.
			//ajax를 통해 비동기처리 진행
			// $.ajax({
			// 	url: 'addReport.do',
			// 	type: 'POST',
			// 	contentType: 'application/json',
			// 	data: JSON.stringify({
			// 		storeNum: storeNum,
			// 	}),
			// 	dataType: 'json',
			// 	success: function(result) {
			// 		if (result.true) {	//반환값 boolean true인 경우
			// 			Swal.fire({
			// 				icon: 'success',
			// 				title: '폐점 전환 완료',
			// 				text: '폐점된 가게로 전환되었습니다. 제보글이 삭제됩니다.',
			// 				confirmButtonText: '확인',
			// 			})
			// 		} else {	//반환값 boolean false인 경우
			// 			Swal.fire({
			// 				icon: 'error',
			// 				title: '에러 발생',
			// 				text: '처리도중 문제가 발생해 실행되지 않았습니다. 다시 시도해주세요. 재발생 시 본사에 문의 바랍니다.',
			// 				confirmButtonText: '확인'
			// 			});
			// 		}
			// 	},
			// 	error: function() {
			// 		console.error('AJAX 요청 실패:', error);
			// 		Swal.fire({
			// 			icon: 'error',
			// 			title: '오류',
			// 			text: '서버와의 통신에 실패했습니다.',
			// 			confirmButtonText: '확인'
			// 		});
			// 	}
			// });
			
			//deleteReport.do 비동기 진행
			//비동기 결과 true시, 제보글이 삭제되었습니다.
			//처리도중 문제가 발생해 실행되지 않았습니다. 다시 시도해주세요.
			//ajax를 통해 비동기처리 진행
			// $.ajax({
			// 	url: 'deleteReport.do',
			// 	type: 'POST',
			// 	contentType: 'application/json',
			// 	data: JSON.stringify({
			// 		storeNum: storeNum,
			// 	}),
			// 	dataType: 'json',
			// 	success: function(result) {
			// 		if (result.true) {	//반환값 boolean true인 경우
			// 			Swal.fire({
			// 				icon: 'success',
			// 				title: '폐점 제보글 삭제',
			// 				text: '폐점 제보글이 삭제되었습니다.',
			// 				confirmButtonText: '확인',
			// 			})
			// 		} else {	//반환값 boolean false인 경우
			// 			Swal.fire({
			// 				icon: 'error',
			// 				title: '에러 발생',
			// 				text: '처리도중 문제가 발생해 실행되지 않았습니다. 다시 시도해주세요. 재발생 시 본사에 문의 바랍니다.',
			// 				confirmButtonText: '확인'
			// 			});
			// 		}
			// 	},
			// 	error: function() {
			// 		console.error('AJAX 요청 실패:', error);
			// 		Swal.fire({
			// 			icon: 'error',
			// 			title: '오류',
			// 			text: '서버와의 통신에 실패했습니다.',
			// 			confirmButtonText: '확인'
			// 		});
			// 	}
			// });