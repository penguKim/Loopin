
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
