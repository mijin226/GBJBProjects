// 가게 제보 링크를 처리하는 함수
function handleStoreReportLinks() {
    // 모든 .storeReportLink 요소를 선택합니다.
    const storeReportLinks = document.querySelectorAll('.storeReportLink');
    
    // 선택된 요소가 하나 이상 있을 경우
    if (storeReportLinks.length > 0) {
        // 각 요소에 대해 클릭 이벤트 리스너를 추가합니다.
        storeReportLinks.forEach(link => {
            link.addEventListener('click', function (event) {
                event.preventDefault();
                showLoginAlert(); 
            });
        });
    }
}

// 로그인 경고창을 표시하는 함수
function showLoginAlert() {
    Swal.fire({
        icon: 'warning', 
        title: '로그인이 필요합니다.', 
        text: '가게 제보를 이용하려면 로그인이 필요합니다.', 
        confirmButtonText: '로그인하기' 
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = 'login.do'; 
        }
    });
}

// DOMContentLoaded 이벤트가 발생했을 때 handleStoreReportLinks 함수 실행
document.addEventListener('DOMContentLoaded', handleStoreReportLinks);