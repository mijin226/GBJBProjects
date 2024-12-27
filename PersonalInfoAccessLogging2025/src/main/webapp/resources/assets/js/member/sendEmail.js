//최프기준 사용하지 않음.
 // 회원 정보 확인하기
  function emailNameCheck(e) {
    e.preventDefault(); // 폼의 기본 동작(페이지 리로드)을 막음

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;

    // AJAX로 회원정보 확인하기
    $.ajax({
      url: 'checkEmailName.do',  // 서버에 회원 정보 확인 요청
      type: 'POST',
      data: {
        name: name,
        email: email
      },
      //dataType: "json",
      success: function(response) {
        // 성공: 이메일 인증번호 발송
        if (response.result) {//백단으로부터 받은 값이 true
           console.log("로그 : response.result : "+ response.result);
           console.log("로그 : response.memberPK : "+ response.memberPK);
           console.log("로그 : 이메일 인증 시작 data : "+response);
           document.getElementById('memberPK').value = response.memberPK;
           console.log("로그 : memberPK : "+document.getElementById('memberPK').value);
          sendEmail();  // 인증 이메일 전송
        } else {
          alert('입력하신 정보를 찾을 수 없습니다.');
        }
      },
      error: function() {
        alert('서버와 통신에 문제가 발생했습니다.');
      }
    });
  }
 
  //이메일 발송 및 수신에 따른 결과 안내
  var sendEmailResult = false;
  var checkNumResult = false;

  // 이메일 전송 함수
  function sendEmail() {
	//현재 문서(login.jsp)에서 아이디 값이 email인 값(인증번호)을 email 변수로 참조함.
    const email = document.getElementById('email').value;
    console.log("로그 : 이메일에 인증번호 전송");

    $.ajax({
      url: 'sendMail.do',  // 이메일로 인증번호 전송 요청
      method: 'POST',
      data: {
        email: email
      },
      success: function(data) {
        sendEmailResult = data === 'true';
        console.log("이메일 인증 성공 : "+data);
        if (sendEmailResult) {
          $("#sendEmailMsg").text("인증번호 메일전송 완료").css('color', 'green');
        } else {
          $("#sendEmailMsg").text("인증번호 메일전송 실패").css('color', 'red');
        }
      },
      error: function() {
        alert('인증번호 전송 중 오류가 발생했습니다.');
        console.log("로그 : 인증번호 전송 실패");
      }
    });
  }

  // 인증번호 확인 함수
  function checkNum() {
     //인증번호 값
    const checkNum = document.getElementById('checkNum').value;
    console.log("로그 : 인증번호 확인");

    $.ajax({
      url: 'checkEmailNum',  // 서버에 인증번호 확인 요청
      method: 'POST',
      data: {
        checkNum: checkNum
      },
      success: function(data) {
        checkNumResult = data === 'true';
        console.log("인증번호 확인 data : "+data);
        if (checkNumResult) {
          $("#checkNumMsg").text("인증번호 일치").css('color', 'green');
        } else {
          $("#checkNumMsg").text("인증번호가 일치하지 않습니다.").css('color', 'red');
          document.getElementById('checkNum').value = ''; // 입력란 비우기
          document.getElementById('checkNum').focus();  // 입력창으로 이동
        }
      },
      error: function() {
        alert('인증번호 확인 중 오류가 발생했습니다.');
        console.log("로그 : 인증번호 확인 실패");
      }
    });
  }

  // 이메일 전송 버튼 클릭 이벤트
  document.getElementById('sendNum').addEventListener('click', function(e) {
    emailNameCheck(e);
    alert('이메일에서 인증번호를 확인해주세요.');
  });
