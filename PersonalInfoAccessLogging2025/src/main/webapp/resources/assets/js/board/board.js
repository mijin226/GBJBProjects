//게시물 전체 조회 스크립트	///////////////////////////

// 특정 행(게시물)을 클릭하면 상세조회 이동
$(document).ready(function() {

	//상세조회에서 수정 버튼을 누르면
	$(document).on('click', '#updateButton', function () {
		$('#updateForm').submit(); // 수정 폼 제출

	});


	//상세조회에서 삭제 버튼을 누르면
	$(document).on('click', '#deleteButton', function () {

		Swal.fire({
			icon: 'warning',
			title: '게시물 삭제 확인',
			text: '해당 게시물을 삭제합니다. 정말 삭제하시겠습니까?',
			showCancelButton: true, // 취소 버튼 생성
			confirmButtonText: '확인', // 확인
			cancelButtonText: '취소' // 취소
		}).then((result) => {
			if (result.isConfirmed) {
				$('#deleteForm').submit(); // 삭제 폼 제출
			}
		})
	});

	//////////////////////////////////////////////////////////////////////////////////////

	// 좋아요 버튼을 눌렀다면
	$('#likeBtn').click(async function () {
		const $this = $(this);
		const boardNum = $this.data('board-id');
		const userPK = $this.data('user-pk');
		console.log('boardNum : ' + boardNum);
		console.log('userPK : ' + userPK);

		// 로그인을 했다면
		if (userPK) {
			try {
				console.log("log: login");
				const likeNum = await getLikeNum(boardNum, userPK);
				console.log("log: likeNum: " + likeNum);

				// 좋아요가 아닌 상태라면
				if (likeNum == null || likeNum === undefined) {
					// 좋아요 상태로 변경
					$this.removeClass('like-false').addClass('like-true');

					const response = await $.ajax({
						url: 'addLike.do',
						type: 'POST',
						contentType: 'application/json',
						data: JSON.stringify({
							boardNum: boardNum,
							memberNum: userPK
						}),
						dataType: 'json'
					});

					console.log('addLike data : ' + response);

					// 추가 성공
					if (response === true) {
						console.log('좋아요 추가 성공');
						// 좋아요 수 업데이트
						const likeCnt = await getLikeCnt(boardNum);
						console.log("log: add like cnt " + likeCnt);
						// 새 값 넣어주기
						document.getElementById('likeCount').innerText = likeCnt;
					} else {
						// 실패
						console.log('좋아요 추가 실패');
					}
				} else {
					// 좋아요 취소 상태로 변경
					$this.removeClass('like-true').addClass('like-false');

					const response = await $.ajax({
						url: 'deleteLike.do',
						type: 'POST',
						data: {likeNum: likeNum}
					});

					console.log('deleteLike data : ' + response);

					// 삭제 성공
					if (response === true) {
						console.log('좋아요 삭제 성공');
						// 좋아요 수 업데이트
						const likeCnt = await getLikeCnt(boardNum);
						console.log("log: delete like cnt " + likeCnt);
						// 새 값 넣어주기
						document.getElementById('likeCount').innerText = likeCnt;
					} else {
						// 삭제 실패
						console.log('좋아요 삭제 실패');
					}
				}
			} catch (error) {
				console.error('Error:', error);
			}
		}
		// 로그인이 안 됐을 때
		else {
			Swal.fire({
				title: '로그아웃 상태',
				text: '로그인 후에 이용해주세요.',
				icon: 'error',
				confirmButtonText: '확인',
			}).then(() => {
				window.location.href = "login.do";
			});
		}
	});

// likeNum을 얻기 위한 비동기 처리 함수
	function getLikeNum(boardNum, userPK) {
		return new Promise((resolve, reject) => {
			console.log("log: getLikeNum start");
			$.ajax({
				url: 'infoLike.do',
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify({
					boardNum: boardNum,
					memberNum: userPK
				}),
				dataType: 'json',
				success: function (likeNumData) {
					console.log('likeNumData : ' + likeNumData);
					if (likeNumData < 0) {
						resolve(null);
					}
					resolve(likeNumData);
				},
				error: function (error) {
					console.error('Error:', error);
					reject(error);
				}
			});
		});
	}

// 좋아요 수를 얻기 위한 비동기 처리 함수
	function getLikeCnt(boardNum) {
		return new Promise((resolve, reject) => {
			console.log("log: getLikeCnt start");
			$.ajax({
				url: 'infoLikeCnt.do',
				type: 'POST',
				data: {boardNum: boardNum},
				success: function (likeCntData) {
					console.log('likeCntData : ' + likeCntData);
					if (likeCntData < 0) {
						resolve(0);
					}
					resolve(likeCntData);
				},
				error: function (error) {
					console.error('Error:', error);
					reject(error);
				}
			});
		});
	}
});