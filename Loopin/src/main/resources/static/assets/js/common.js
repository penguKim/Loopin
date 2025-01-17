
/**
 * alert 띄움
* @param {element} element 포커스 줄 요소
* @param {String} icon 아이콘
* @param {String} title 제목
* @param {String} msg 내용
 */
function showAlert(element, icon, title, msg) {
	Swal.fire({
	    icon: icon,
	    title: title,
	    html: msg,
	});
	
	if(element != '') {
		element.focus();
	}
	
}

/**
 * toast 띄움
 * @param {element} element 포커스 줄 요소
 * @param {String} icon 아이콘
 * @param {String} title 제목
 * @param {String} msg 내용
 */
function showToast(element, icon, title, msg) {
    Swal.fire({
        toast: true,
        position: 'center',
        icon: icon,
        title: title,
        html: msg,
        showConfirmButton: false,
        timer: 1500,
    });
	
	if(element != '') {
		element.focus();
	}
}

/**
 * 컨펌 띄움(await / async)
 * @param {String} title 제목
 * @param {String} msg 내용
 */
async function showConfirm(title, msg) {
    const result = await Swal.fire({
        title: title,
        html: msg,
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#0d6efd',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소',
		reverseButtons: true, 
    });
    
    return result.isConfirmed;
}


/**
 * 년월일 -> 문자열 리턴
 * @param {date} date 객체
 * @returns {String} 년월일
 */
function getDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

/**
 * 파라미터 만큼의 이전 날짜 문자열 리턴
 * @param {number} num n일 전
 * @returns {String} 년월일
 */
function getPrevDate(num) {
    const date = new Date();
    date.setDate(date.getDate() - num);
    return getDate(date);
}

/**
 * 파라미터 만큼의 이후 날짜 문자열 리턴
 * @param {number} num n일 후
 * @returns {String} 년월일
 */
function getNextDate(num) {
    const date = new Date();
    date.setDate(date.getDate() + num);
    return getDate(date);
}

function getFirstDayOfMonth(date) {
    const firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    const year = firstDay.getFullYear();
    const month = String(firstDay.getMonth() + 1).padStart(2, '0');
    const day = String(firstDay.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}


/**
 * 시분초 -> 문자열 리턴
 * @param {date} date 객체
 * @returns {String} 년월일
 */
function getTime(date) {
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${hours}:${minutes}:${seconds}`;
}

// 
/**
 * 년월일 시분초 -> 문자열 리턴
 * @param {date} date 객체
 * @returns {String} 년월일
 */
function getDateTime(date) {
    // 날짜 포맷팅
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    // 시간 포맷팅
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    
    return `${year}년 ${month}월 ${day}일 ${hours}:${minutes}:${seconds}`;
}

/**
 * card 영역 높이 지정
 * @param {String} el 선택자
 * @param {number} height 화면 높이에서 뺄 높이
 */
function setElementHeight(el, height) {
	let element = document.querySelector(el);
    element.style.height = `${window.innerHeight + height}px`;
}

/**
 * 그리드 영역 높이 지정
 * @param {*} grid 그리드 객체
 * @param {number} height 화면 높이에서 뺄 높이
 */
function setGridHeight(grid, height) {
	const newHeight = window.innerHeight + height; // offset은 음수값
	console.log(newHeight);
    grid.setBodyHeight(newHeight);
}

/**
 * 인풋을 hh:mm:ss 형식으로 입력
 * @param {*} input 선택자 객체
 */
function inputTimeFormat(input) {
    // 숫자와 콜론만 입력 가능하도록 필터링
    input.value = input.value.replace(/[^0-9:]/g, '');
    input.addEventListener('blur', function() {
        let value = this.value;
        
        // 입력값 검증
        if (value === '') return;
        
        // 숫자만 입력된 경우 (시간만 입력)
        if(/^\d{1,2}$/.test(value)) {
            const hours = parseInt(value);
            if(hours >= 0 && hours <= 23) {
                this.value = value.padStart(2, '0') + ':00:00';
            } else {
                showToast(input, 'error', '올바른 시간 형식이 아닙니다', '00:00:00 ~ 23:59:59 사이의 값을<br>입력해주세요');
                this.value = '';
            }
            return;
        }
        
        // HH:mm 형식으로 입력된 경우
        if(/^([0-9]{1,2}):([0-9]{2})$/.test(value)) {
            const [hours, minutes] = value.split(':').map(Number);
            if(hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59) {
                this.value = hours.toString().padStart(2, '0') + ':' + 
                            minutes.toString().padStart(2, '0') + ':00';
                return;
            }
        }
        
        // HH:mm:ss 형식 검증
        if(!validateTime(value)) {
            showToast(input, 'error', '올바른 시간 형식이 아닙니다', '00:00:00 ~ 23:59:59 사이의 값을<br>입력해주세요');
            this.value = '';
            return;
        }
        
        // 올바른 형식인 경우 포맷팅
        const [hours, minutes, seconds = "00"] = value.split(':');
        this.value = hours.padStart(2, '0') + ':' + 
                    minutes.padStart(2, '0') + ':' + 
                    seconds.padStart(2, '0');
    });

    // 입력 중 콜론 자동 추가
    if(input.value.length === 2 && !input.value.includes(':')) {
        const hours = parseInt(input.value);
        if(hours >= 0 && hours <= 23) {
            input.value = input.value + ':';
        }
    } else if(input.value.length === 5 && input.value.split(':').length === 2) {
        const [hours, minutes] = input.value.split(':').map(Number);
        if(hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59) {
            input.value = input.value + ':';
        }
    }
}

// 시간 형식 검증 함수
function validateTime(timeStr) {
    const timePattern = /^([0-9]{1,2}):([0-9]{2})(?::([0-9]{2}))?$/;
    if (!timePattern.test(timeStr)) return false;
    
    const [hours, minutes, seconds = "00"] = timeStr.split(':').map(Number);
    return hours >= 0 && hours <= 23 && 
           minutes >= 0 && minutes <= 59 && 
           seconds >= 0 && seconds <= 59;
}

/**
 * 라디오버튼 체크
 * @param {String} radioName 라디오 요소 name
 * @param {String} value 값
 */
function setRadioValue(radioName, value) {
    $(`input:radio[name=${radioName}][value=${value}]`).prop('checked', true);
}

function getRadioValue(radioName) {
	return radioName.filter(':checked').val();
}

/**
 * 셀렉트박스 생성
 * @param {String} el 셀렉트박스 선택자
 * @param {*} list 리스트
 * @param {String} title 기본값
 */
function createSelectBox(el, list, title) {
    const selectBox = $(el);

    selectBox.empty();
	
	if(title) {
	    selectBox.append(`<option value="">${title}</option>`);		
	}

    list.forEach(data => {
        selectBox.append(`<option value="${data.common_cc}">${data.common_nm}</option>`);
    });
}

/**
 * 그리드 -> 엑셀 다운로드
 * @param {*} grid 그리드 겍체
 * @param {String} title 엑셀 파일명
 */
function gridExcelDownload(grid, title) {
	const token = $("meta[name='_csrf']").attr("content")
	const header = $("meta[name='_csrf_header']").attr("content");
    const headers = grid.getColumns();
    const rows = grid.getData();
    
    const data = {
        headers: headers,
        rows: rows,
        title: title
    };
    
    $.ajax({
        type: 'post',
        url: '/excelDownload',
        contentType: 'application/json',
        data: JSON.stringify(data),
        xhrFields: {
            responseType: 'blob'
        },
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(blob) {
            const file = new Blob([blob], {
                type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            });
            
            const url = window.URL.createObjectURL(file);
            const a = document.createElement('a');
            a.href = url;
            a.download = title + '.xlsx';
            
            document.body.appendChild(a);
            a.click();
            
            setTimeout(function() {
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);
            }, 100);
        },
        error: function(xhr, textStatus, errorThrown) {
            console.error('엑셀 다운로드 실패:', errorThrown);
        }
    });
}


