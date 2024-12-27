//댓글 목록 조회, 추가, 삭제 스크립트

// 페이지 로드 시 댓글 목록 불러오기	////////////////////////////////////////////////////
$(document).ready(() => {
	loadReply();

	// 댓글 목록 불러오기	////////////////////////////////////////////////////
	// 보내는 값 : 게시글 고유번호 boardNum
	// 받는 값 : 댓글 리스트	List<ReplyDTO> replyList
	function loadReply() {
		let boardNum = $('#boardNum').val();
		let memberNum = $('#replyMemberNum').val();//로그인된 사용자
		console.log("replyList.js loadReply start");

		if (boardNum) {
			$.ajax({
				url: 'loadListReply.do',
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify({ boardNum: boardNum }),
				dataType: 'json',
				success: function(result) {
					console.log("log: ajax loadListReply success");
					$('#replyList').empty(); // 댓글 초기화
					$.each(result, function(index, reply) {
						var replyListResult = `
							<!--댓글 고유번호 출력-->
				        	<input type="hidden" id="replyNum" name="replyNum" value="${reply.replyNum}">
				            <div class="row align-items-center replyList">
				            	<div class="col-12 col-md-9">
				                	<div class="replySection"><!-- 댓글 닉네임 및 내용 출력 -->
				                    	<span class="nickName">${reply.memberNickname}</span>
				                    	<span class="replyContent">${reply.replyContent}</span>
				                    </div>
				                </div>
								<!-- 댓글 작성일자 -->
				                <div class="col-12 col-md-2 text-center" id="replyData">
				                	<div class="date">${reply.replyWriteDay}</div>
				               </div>
									<!-- 댓글 삭제버튼 -->
									<!-- 만약 사용자라면 댓글 삭제 보여주기 / 아니라면 숨김-->
									${reply.memberNum == memberNum ?
								`<div class="col-12 col-md-1 buttonBox">
				                         <button class="btn btn-danger deleteReply" data-member-num="${reply.memberNum}" data-reply-num="${reply.replyNum}">삭제</button>
				                     </div>`
								: ''}
				               <hr>
				           </div>
				        `;
						$('#replyList').append(replyListResult); // 댓글 목록에 추가
					});
				},
				error: function() {
					console.log("log: reply error");
					Swal.fire({
						icon: 'error',
						title: '댓글 목록 불러오기 오류',
						text: '댓글 목록 불러오는 도중 오류가 발생했습니다. 새로고침 해주세요.',
						confirmButtonText: '확인'
					});
				}
			});
		}
	}


	// 댓글 작성 버튼 클릭 시	////////////////////////////////////////////////////
	// 보내는 값 : 회원, 게시물, 댓글 고유번호
	// 받는 값 : boolean(true/false)
	$('#insertReply').on('click', function() {
		//댓글작성 클릭했을 때 댓글 작성한 내용이 있다면
		let replyContent = $('#myReplyContent').val();
		console.log("myReplyContent : [",replyContent,"]");
		if (replyContent) {
			console.log("replyList.js insertReply start");
			let memberNum = $('#replyMemberNum').val();
			let boardNum = $('#boardNum').val();

			console.log("memberNum : [",memberNum,"]");
			console.log("replyContent : [",replyContent,"]");
			console.log("boardNum : [",boardNum,"]");

			$.ajax({
				url: 'addReply.do',
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify({
					memberNum: memberNum,
					replyContent: replyContent,
					boardNum: boardNum
				}),
				dataType: 'json',
				success: function(result) {
					if (result === 'true') {
						$('#myReplyContent').val(''); // 댓글 입력란 초기화
						//window.location.href = 'board.do'; // board.do로 이동
						loadReply(); //댓글목록 다시 불러오기
					}
					else {
						Swal.fire({
							icon: 'error',
							title: '댓글 추가 오류',
							text: '댓글 추가하는 도중 오류가 발생했습니다. 다시 입력해주세요.',
							confirmButtonText: '확인'
						});
					}
				}
			});
		}
		else {
			console.log("log: reply click");
			Swal.fire({
				icon: 'error',
				title: '댓글 작성 오류',
				text: '작성할 댓글 내용이 없습니다.',
				confirmButtonText: '확인'
			});
		}
	});

	// 댓글 삭제 버튼 클릭 시	////////////////////////////////////////////////////
	// 보내는 값 : 댓글 고유번호
	// 받는 값 : boolean(true/false)
	$(document).on('click', '.deleteReply', function() {
		var replyNum = $(this).data('reply-num');
		var memberNum = $(this).data('member-num');
		console.log("replyNum : [", replyNum, "]");
		console.log("memberNum : [", memberNum, "]");

		Swal.fire({
			icon: 'warning',
			title: '댓글 삭제 확인',
			text: '해당 댓글을 삭제합니다. 정말 삭제하시겠습니까?',
			showCancelButton: true, // 취소 버튼 생성
			confirmButtonText: '확인', // 확인
			cancelButtonText: '취소' // 취소
		}).then((infoResult) => {
			if (infoResult.isConfirmed) { // 확인 버튼 클릭 시 댓글삭제 진행
				$.ajax({
					url: 'deleteReply.do',
					type: 'POST',
					contentType: 'application/json',
					data: JSON.stringify({ replyNum: replyNum, memberNum : memberNum }),
					success: function(result) {
						if (result === 'true') { //댓글이 삭제되었다면
							//window.location.href = 'board.do'; // board.do로 이동
							loadReply(); //댓글목록 다시 불러오기
						}
						else {
							Swal.fire({
								icon: 'error',
								title: '오류',
								text: '댓글 삭제 중 오류가 발생했습니다.'
							});
						}
					},
					error: function() {
						Swal.fire({
							icon: 'error',
							title: '오류',
							text: '댓글 삭제에 실패했습니다.'
						});
					}
				});
			}
		});
	});

});