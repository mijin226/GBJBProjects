// 페이지가 로드 되었을 때 실행
$(document).ready(function() {

	// 복사하기 버튼 눌렀을 때 수행 되는 함수
	copyStorePhoneNumber();

});

// 복사하기 버튼 눌렀을 때 수행 되는 함수
function copyStorePhoneNumber() {
	// class가 copy인 btn이 클릭되었을 때
	$('.copy').on('click', function() {
		// 버튼에서 val 값을 가져옴
		const copyWord = $(this).val();
		console.log('copyWord : ['+copyWord+']');

		// 복사 기능
		navigator.clipboard.writeText(copyWord).then(function() {
			Swal.fire({
				icon: 'success',
				title: '복사 성공',
				text: '복사에 성공했습니다.',
				confirmButtonText: '확인'
			});
		}, function(err) {
			console.error('복사 실패 : ['+err+']');
			Swal.fire({
				icon: 'error',
				title: '복사 실패',
				text: '복사에 실패했습니다. 페이지를 다시 로드해주세요.',
				confirmButtonText: '확인'
			});
		});
	});
}