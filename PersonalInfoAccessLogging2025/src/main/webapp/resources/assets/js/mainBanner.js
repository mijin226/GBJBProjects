const slides = document.querySelectorAll('.banner-image');
let currentIndex = 0;

// 첫 번째 이미지만 표시
slides[currentIndex].style.display = 'block';

function showNextSlide() {
    // 현재 이미지 숨김
    slides[currentIndex].style.display = 'none';
    
    // 다음 이미지 인덱스 설정
    currentIndex = (currentIndex + 1) % slides.length;
    
    // 다음 이미지 표시
    slides[currentIndex].style.display = 'block';
}

// 3초마다 슬라이드 전환
setInterval(showNextSlide, 3000);
