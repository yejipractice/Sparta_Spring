function addFolder() {
    const folderNames = $('.folderToAdd').toArray().map(input => input.value);
    folderNames.forEach(name => {
        if (name == '') {
            alert('올바른 폴더명을 입력해주세요');
            return;
        }
    })
    $.ajax({
        type: "POST",
        url: `/api/folders`,
        contentType: "application/json",
        data: JSON.stringify({
            folderNames
        }),
        success: function (response) {
            $('#container2').removeClass('active');
            alert('성공적으로 등록되었습니다.');
            window.location.reload();
        },
        error: function (response) {
            // 서버에서 받은 에러 메시지를 노출
            if (response.responseJSON && response.responseJSON.message) {
                alert(response.responseJSON.message);
            } else {
                alert("알 수 없는 에러가 발생했습니다.");
            }
        }
    })
}