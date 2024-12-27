let editorInstance;

class MyUploadAdapter {
    constructor(loader) {
        this.loader = loader;
    }

    upload() {
        return this.loader.file
            .then(file => new Promise((resolve, reject) => {
                const formData = new FormData();
                formData.append("file", file); // DTO의 `file` 필드와 매핑
                formData.append("boardFolder", boardFolder); // DTO의 `boardFolder` 필드와 매핑

                fetch('addImage.do', {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.text())
                .then(url => {
                    if (url) {
                        // url 앞뒤의 따옴표 제거 후 resolve
                        const cleanUrl = url.replace(/^"|"$/g, '');
                        const result = { default: cleanUrl };
                        console.log('resolve할 객체:', result);
                        resolve(result);
                    } else {
                        reject("이미지 업로드 실패");
                    }
                })
                .catch(error => reject(error));
            }));
    }
}

// CKEditor 초기화
document.addEventListener('DOMContentLoaded', () => {
    ClassicEditor.create(document.querySelector('#boardContent'), {
        language: 'ko',
        ckfinder: {
            uploadUrl: '/addImage.do'
        },
    }).then(editor => {
        editorInstance = editor;
        editor.setData(initialContent);  // 데이터를 에디터에 세팅
        console.log("CKEditor 초기 데이터 설정 완료:", initialContent);

        editor.ui.view.editable.element.style.height = '500px';

        editor.plugins.get('FileRepository').createUploadAdapter = loader => new MyUploadAdapter(loader);
    }).catch(error => {
        console.error("[ERROR] CKEditor 초기화 실패:", error);
    });
});

// 폼 제출 시 CKEditor 데이터를 hidden input에 동기화
document.querySelector('#updateForm').addEventListener('submit', function(e) {
    if (editorInstance) {
        const editorData = editorInstance.getData();
        document.querySelector('#boardContent').value = editorData;
    }
});