let editorInstance;  // 전역 변수로 선언하여 에디터 인스턴스를 관리

// 이미지 업로드 어댑터 클래스 정의
class MyUploadAdapter {
    constructor(loader) {
        this.loader = loader;  // 로더 객체를 생성자에 저장하여 파일 로드 시 사용
    }

    // 이미지 업로드 메서드
    upload() {
        return this.loader.file
            .then(file => new Promise((resolve, reject) => {
                const formData = new FormData();
                formData.append("file", file); // FormData에 `file` 필드를 추가 (DTO의 `file` 필드와 매핑)
                formData.append("boardFolder", boardFolder); // FormData에 `boardFolder` 필드를 추가 (DTO의 `boardFolder` 필드와 매핑)

                // 이미지 업로드 요청
                fetch('addImage.do', {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.text()) // 응답을 텍스트 형식으로 변환
                .then(url => {
                    if (url) {
                        // url 앞뒤의 따옴표 제거 후 resolve
                        const cleanUrl = url.replace(/^"|"$/g, '');
                        const result = { default: cleanUrl };
                        console.log('resolve할 객체:', result);
                        resolve(result);
                    } else {
                        reject("이미지 업로드 실패"); // URL이 없으면 업로드 실패 처리
                    }
                })
                .catch(error => reject(error)); // 오류 시 Promise 거부
            }));
    }
}

// CKEditor 초기화
document.addEventListener('DOMContentLoaded', () => {
    ClassicEditor.create(document.querySelector('#boardContent'), {
		language: 'ko', // 에디터 언어를 한국어로 설정
		ckfinder: {
		    uploadUrl: '/addImage.do' // 이미지 업로드 URL을 설정
		},

    }).then(editor => {
        editorInstance = editor; // 에디터 인스턴스를 전역 변수에 저장하여 나중에 접근 가능

		// 에디터 높이 설정
		editor.ui.view.editable.element.style.height = '500px';

        // 에디터의 기본 라벨을 숨김 처리
        const labelElement = document.querySelector('.ck-label');
        if (labelElement) {
            labelElement.style.display = 'none'; // 라벨이 있을 경우 숨김 처리
        }

        // 파일 업로드 어댑터를 MyUploadAdapter로 설정
        editor.plugins.get('FileRepository').createUploadAdapter = loader => new MyUploadAdapter(loader);
    }).catch(error => {
        console.error("[ERROR] CKEditor 초기화 실패:", error); // 초기화 실패 시 오류 출력
    });
});

// 폼 제출 시 CKEditor 데이터를 숨겨진 입력 필드에 동기화
document.querySelector('#postForm').addEventListener('submit', function(e) {
    if (editorInstance) {
        const editorData = editorInstance.getData(); // 에디터 내용을 가져옴
        document.querySelector('#boardContent').value = editorData; // 가져온 내용을 숨겨진 input에 동기화
    }
});
