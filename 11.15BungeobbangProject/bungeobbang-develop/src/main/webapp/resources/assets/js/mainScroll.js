// 스크롤 이벤트 리스너 추가
document.addEventListener("scroll", function () {
    const section1 = document.querySelector("#section1"); // section1 요소 선택
    const scrollPosition = window.scrollY; // 현재 스크롤 위치

    // 스크롤이 100px 이상 내려갔을 때 section1 요소 숨기기
    if (scrollPosition > 100) {
        section1.style.opacity = "0"; // opacity를 0으로 설정하여 숨김
        section1.style.transition = "opacity 3s ease"; // 부드럽게 전환 효과 적용
    } else {
        section1.style.opacity = "1"; // 스크롤이 100px 이하일 경우 다시 나타남
    }
});


function handleScrollEffect(selector, scrollThreshold, fadeInClass, fadeOutClass) {
    const element = document.querySelector(selector); // 선택자로 요소를 가져옴

    if (!element) return; // 요소가 없을 경우 함수 종료

    // 스크롤 이벤트 리스너 추가
    document.addEventListener("scroll", function () {
        const scrollPosition = window.scrollY; // 현재 스크롤 위치
		console.log(scrollPosition); // 현재 스크롤 위치를 콘솔에 출력

        // 스크롤 위치가 임계값보다 크면 fadeOutClass를 추가하고 fadeInClass 제거
        if (scrollPosition > scrollThreshold) {
            element.classList.add(fadeOutClass); // 사라지는 효과 클래스 추가
            element.classList.remove(fadeInClass); // 나타나는 효과 클래스 제거
        } else {
            // 스크롤 위치가 임계값 이하이면 fadeInClass를 추가하고 fadeOutClass 제거
            element.classList.remove(fadeOutClass); // 사라지는 효과 클래스 제거
            element.classList.add(fadeInClass); // 나타나는 효과 클래스 추가
        }
    });
}

// 스크롤 효과 설정
handleScrollEffect(".text-box", 100, "fade-in-right", "fade-out-left"); // 첫 번째 텍스트 상자에 대한 스크롤 효과 적용
handleScrollEffect(".second-text-box", 100, "fade-in-left", "fade-out-right"); // 두 번째 텍스트 상자에 대한 스크롤 효과 적용
