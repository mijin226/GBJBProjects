document.addEventListener("DOMContentLoaded", function() {
    const boardCateNameSelect = document.getElementById("boardCateName");
    const postSecretGroup = document.getElementById("postSecretGroup");
    const postSecretCheckbox = document.getElementById("postSecret");
    const hiddenBoardOpen = document.getElementById("hiddenBoardOpen");

    function updateBoardOpenState(selectedCategory) {
        if (selectedCategory === "boardList") {
            postSecretGroup.style.display = "none"; // 비공개 선택 그룹 숨기기
            postSecretCheckbox.checked = false; // 체크 해제
            hiddenBoardOpen.value = "Y"; // 공개로 고정
            console.log("boardList selected - boardOpen:", hiddenBoardOpen.value); 
        } else {
            postSecretGroup.style.display = "block"; // 비공개 선택 그룹 표시
            hiddenBoardOpen.value = postSecretCheckbox.checked ? "N" : "Y"; // 체크 여부에 따라 값 설정
            console.log("noticeBoard selected - boardOpen:", hiddenBoardOpen.value); 
        }
    }

    // 초기 설정
    updateBoardOpenState(boardCateNameSelect.value);

    // 게시판 선택이 변경될 때마다 상태 업데이트
    boardCateNameSelect.addEventListener("change", function() {
        updateBoardOpenState(this.value);
    });

    // 체크박스 체크 여부에 따라 hiddenBoardOpen 값 업데이트
    postSecretCheckbox.addEventListener("change", function() {
        hiddenBoardOpen.value = this.checked ? "N" : "Y";
        console.log("Checkbox changed - boardOpen:", hiddenBoardOpen.value); 
    });
});
