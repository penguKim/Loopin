
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
	    didClose: () => {
	        if (element) {
	            setTimeout(() => {
	                element.focus();
	            }, 0);
	        }
	    }
	});
	
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
		didClose: () => {
	        if (element) {
	            setTimeout(() => {
	                element.focus();
	            }, 0);
	        }
	    }
	});
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
    const elements = document.querySelectorAll(el);
    elements.forEach(element => {
        element.style.height = `${window.innerHeight + height}px`;
    });
}


/**
 * 그리드 영역 높이 지정
 * @param {*} grid 그리드 객체
 * @param {number} height 화면 높이에서 뺄 높이
 */
function setGridHeight(grid, height) {
	const newHeight = window.innerHeight + height; // offset은 음수값
    grid.setBodyHeight(newHeight);
}

/**
 * 그리드 영역 너비 지정
 * @param {*} grid 그리드 객체
 * @param {number} width 부모 요소에서 뺄 너비
 */
function setGridWidth(grid, width) {
	const newWidth = $(grid.el).parent().width() + width;
    grid.setWidth(newWidth);
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
 * 라디오버튼 생성
 * @param {String} el 선택자
 * @param {*} list 리스트
 * @param {String} name name 값
 * @param {boolean} flag true -> 전체 버튼 추가
 */
function createRadio(el, list, name, flag) {
    const container = $(el);
    
    container.empty();
    
	if(flag) {
	    container.append(`
	        <div class="form-check">
	            <input class="form-check-input" type="radio" 
	                   name="${name}" id="${name}_ALL" value="ALL" checked>
	            <label class="form-check-label" for="${name}_ALL">전체</label>
	        </div>
	    `);
	}
    
    list.forEach(data => {
        container.append(`
			<div class="form-check">
			    <input type="radio" id="${name}_${data.common_cc}"name="${name}" value="${data.common_cc}" 
			        class="form-check-input">
			    <label class="form-check-label" for="${name}_${data.common_cc}">${data.common_nm}</label>
			</div>
        `);
    });
}

/**
 * select2 생성
 * @param {String} el 선택자
 * @param {*} list 리스트
 * @param {String} name name 값
 * @param {boolean} flag true -> 전체 버튼 추가
 */
function createSelect2(selectId, data, placeholder, parentModal) {
    const select = $(`${selectId}`);
    select.select2({
        dropdownParent: $(`#${parentModal}`),
        placeholder: placeholder,
        width: '100%',
        data: data.map(item => ({
            id: item['common_cc'],
            text: item['common_nm']
        }))
    }).next().after(`<button type="button" class="btn btn-sm btn-secondary mt-1" id="select-all-${selectId.substring(1)}">전체 선택</button>`);

    $(document).on('click', `#select-all-${selectId.substring(1)}`, function() {
        const button = $(this);
        
        if (select.val() && select.val().length == select.find('option').length) {
            select.val(null);
        } else {
            const allOptions = select.find('option').map(function() {
                return $(this).val();
            }).get();
            select.val(allOptions);
        }
        
        select.trigger('change');
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

/**
 * 검색 모듈에 엑셀버튼 추가
 * @param {*} grid 그리드 겍체
 * @param {String} title 엑셀 파일명
 */
function addExcelButton(grid, title) {
    const resetFilter = $('#resetFilter');
    if (!$('#btn_excel_download').length && resetFilter.length) {
        const excelBtn = $('<button>', {
            id: 'btn_excel_download',
            class: 'btn btn-primary me-2',
            text: '엑셀'
        }).on('click', () => {
            gridExcelDownload(grid, title);
        });
        
        resetFilter.before(excelBtn);
    }
}

/**
 * ajax post 요청을 Promise로 처리하는 함수
 * @param {string} url - 요청 url
 * @param {Object} jsonData - JSON 데이터
 * @returns {Promise} 응답 데이터
 */
function callAjaxPost(url, jsonData) {
	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'post',
            url: url,
            contentType: 'application/json',
            data: jsonData,
			headers: {[header]: token},
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                reject(xhr.responseJSON);
            }
        });
    });
}

/**
 * ajax get 요청을 Promise로 처리하는 함수
 * @param {string} url - 요청 url
 * @param {Object} jsonData - JSON 데이터
 * @returns {Promise} 응답 데이터
 */
function callAjaxGet(url, jsonData) {
	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'get',
            url: url,
            data: jsonData,
			headers: {[header]: token},
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                reject(xhr.responseJSON);
            }
        });
    });
}

/**
 * ajax post 요청을 Promise로 처리하는 함수
 * @param {string} url - 요청 url
 * @param {Object} jsonData - form 데이터
 * @returns {Promise} 응답 데이터
 */
function callAjaxFileUpload(url, formData) {
	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");
    return new Promise((resolve, reject) => {
        $.ajax({
            type: 'POST',
            url: url,
			processData: false,
			contentType: false,
            data: formData,
			headers: {
				[header]: token,
		},
            success: function(response) {
                resolve(response);
            },
            error: function(xhr) {
                reject(xhr.responseJSON);
            }
        });
    });
}

/**
 * 공통코드 조회
 * @param {string} codes - 공통코드 조회할 가변 문자열
 * @returns {*} 응답 데이터
 */
async function getCommonList(...codes) {
	let data = {
		list: codes
	};
	let jsonData = JSON.stringify(data);
	try {
	    let ajaxData = await callAjaxPost('/select_COMMON_list', jsonData);
		return ajaxData['commonList'];
	} catch (error) {
		console.log(error.msg);
		return null;
	}
}

/**
 * 공통코드 -> 필터 리스트 변환
 * @param {string} commonCode - 공통코드 리스트
 * @returns {*} 필터 리스트
 */
function setFilterList(commonCode) {
    if (!commonCode || !Array.isArray(commonCode)) return [];
    
    return commonCode.map((item, index) => ({
        value: item.common_cc,
        text: item.common_nm,
        checked: index == 0 ? 'checked' : ''
    }));
}

/**
 * 인풋 길이 체크
 * @param {string} selector 체크할 요소명
 * @param {int} maxBytes 최대 바이트 수
 * @returns {boolean} 
 */
function byteCheck(selector, maxBytes) {
    let element = $(selector);
    let text = element.val();
    let encoder = new TextEncoder();
    let byteLength = encoder.encode(text).length;
    if(byteLength > maxBytes) {
        let cutText = '';
        for(let i = 0; i < text.length; i++) {
            let char = text.slice(0, i + 1);
            let charByteLength = encoder.encode(char).length;
            
            if (charByteLength > maxBytes) break;
            
            cutText = char;
        }
        
        element.val(cutText);
        return false;
    }
    return true;
}

/**
 * 그리드 검증 체크
 * @param {*} grid - 그리드 객체
 * @returns {boolean} 검증 통과 시 true
 */
function gridValidationCheck(grid) {
	const ERROR_MESSAGES = {
	    'REGEXP': '올바른 형식이 아닙니다.',
	    'REQUIRED': '입력해주세요.',
	    'NUMBER': '숫자만 입력 가능합니다.',
	    'MIN': '최소값보다 작습니다.',
	    'MAX': '최대값보다 큽니다.'
	};
	const getErrorMessage = (errorType, rowKey, header) => {
	    return `${rowKey + 1}행의 ${header}은(는) ${ERROR_MESSAGES[errorType]}`;
	};
    const validationResult = grid.validate();
    
    for (const row of validationResult) {
        const rowKey = row['rowKey'];
        
        for (const cell of row.errors) {
            const column = grid.getColumns().find(col => col['name'] == cell.columnName);
            const header = column['header'];
            const errorType = cell.errorCode[0];
            
            const msg = getErrorMessage(errorType, rowKey, header);
            showAlert('', 'error', '입력 체크', msg);
            grid.focus(rowKey, cell.columnName);
            return false;
        }
    }
    return true;
}
