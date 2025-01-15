// Toast UI DateRangePicker 생성 함수
function createDateRangePicker(elementId, format, start, end) {
    const startContainer = document.querySelector(`#${elementId}StartContainer`);
    const endContainer = document.querySelector(`#${elementId}EndContainer`);
	const hasTime = format != 'YYYY-MM-dd' ? true : false;
	
    if (!startContainer || !endContainer) {
        console.error(`DateRangePicker container not found: #${elementId}StartContainer or #${elementId}EndContainer`);
        return null;
    }

	const startDate = start ? new Date(start) : new Date();
	const endDate = end ? new Date(end) : new Date();

    // Startpicker 초기화
    const startPicker = new tui.DatePicker(`#${elementId}StartContainer`, {
        date: startDate,
        input: {
            element: `#${elementId}StartInput`,
            format: format
        },
        timePicker: hasTime
    });

    // Endpicker 초기화
    const endPicker = new tui.DatePicker(`#${elementId}EndContainer`, {
        date: endDate,
        input: {
            element: `#${elementId}EndInput`,
            format: format
        },
        timePicker: hasTime,
    });

    return startPicker;
}


// 필터 모듈 초기화 함수
function initializeFilterModule(filterModuleId, filterConfig, onFilterApplyCallback) {
    const filterModule = document.getElementById(filterModuleId);

    if (!filterModule) {
        console.error(`Element with ID "${filterModuleId}" not found.`);
        return;
    }
	
	const startDate = filterConfig.find(config => config.key == 'startDate');
	const endDate = filterConfig.find(config => config.key == 'endDate');

    // 기본 필터 UI 생성
    const defaultFilterHTML = `
        <div class="row align-items-center g-0">
            <div class="col-auto me-5">
                <label for="mainDateRangeStartInput" class="form-label">${startDate.label}</label>
                <input type="text" id="mainDateRangeStartInput" class="form-control" style="width: 153.5px;">
                <div id="mainDateRangeStartContainer"></div>
            </div>
            <div class="col-auto me-5">
                <label for="mainDateRangeEndInput" class="form-label">${endDate.label}</label>
                <input type="text" id="mainDateRangeEndInput" class="form-control" style="width: 153.5px;">
                <div id="mainDateRangeEndContainer"></div>
            </div>
            <div class="col d-flex justify-content-end align-items-center mt-3">
                <button id="resetFilter" class="btn btn-secondary me-2">초기화</button>
                <button id="applyFilter" class="btn btn-primary me-2">검색</button>
                <button id="toggleFilters" class="btn btn-outline-secondary">
                    <i class="bi bi-chevron-down"></i>
                </button>
            </div>
        </div>
        <div id="additionalFilters" class="row d-none"></div>
    `;

    filterModule.innerHTML = defaultFilterHTML;
	
    // Main DateRangePicker 초기화
    setTimeout(() => {
        const mainPicker = createDateRangePicker('mainDateRange', startDate.format,startDate.value, endDate.value);
        if (mainPicker) {
            console.log('Main DateRangePicker 초기화 성공');
        }
    }, 0); // DOM 안정화 후 실행

    // 숨겨진 필터 UI 생성
    const additionalFilterHTML = filterConfig
        .filter(config => config.key !== 'mainDateRange') // 숨겨진 필터에서 열람 시간 제거
        .map(config => {
            if (config.type === 'text') {
                return `
                    <div class="${config.col}">
                        <label for="${config.key}" class="form-label">${config.label}</label>
                        <input type="text" id="${config.key}" class="form-control" placeholder="${config.placeholder || ''}">
                    </div>
                `;
            }
        }).join('');

    document.getElementById('additionalFilters').innerHTML = additionalFilterHTML;

	setTimeout(() => {
	    const applyButton = document.getElementById('applyFilter');
	    const resetButton = document.getElementById('resetFilter');

	    if (!applyButton || !resetButton) {
	        console.error('Apply 또는 Reset 버튼을 찾을 수 없습니다.');
	        return;
	    }

	    // 검색 버튼 이벤트 등록
	    applyButton.addEventListener('click', () => {
	        const filterValues = {};

	        // Main DateRangePicker 값 가져오기
	        const startInput = document.getElementById('mainDateRangeStartInput');
	        const endInput = document.getElementById('mainDateRangeEndInput');

	        if (startInput && endInput) {
	            filterValues['startDate'] = startInput.value;
	            filterValues['endDate'] = endInput.value;
	        } else {
	            console.error('Main DateRangePicker 필드가 누락되었습니다.');
	        }

	        // 숨겨진 필터 값 가져오기
	        filterConfig.forEach(config => {
	            const element = document.getElementById(config.key);
				if (config.key !== 'startDate' && config.key !== 'endDate') {
		            if (element) {
		                filterValues[config.key] = element.value;
		            } else {
		                console.warn(`요소를 찾을 수 없습니다: ${config.key}`);
		            }
				}
	        });

	        onFilterApplyCallback(filterValues);
	    });

	    // 초기화 버튼 이벤트 등록
	    resetButton.addEventListener('click', () => {
	        const startInput = document.getElementById('mainDateRangeStartInput');
	        const endInput = document.getElementById('mainDateRangeEndInput');

	        if (startInput) {
	            startInput.value = startDate.value;
	        }
	        if (endInput) {
	            endInput.value = endDate.value;
	        }

	        filterConfig.forEach(config => {
	            const element = document.getElementById(config.key);
	            if (element) {
	                element.value = '';
	            }
	        });
	    });
	}, 0);


    // 숨겨진 필터 토글 버튼 이벤트 등록
    document.getElementById('toggleFilters').addEventListener('click', () => {
        const additionalFilters = document.getElementById('additionalFilters');
        const toggleIcon = document.querySelector('#toggleFilters i');

        if (additionalFilters.classList.contains('d-none')) {
            additionalFilters.classList.remove('d-none'); // 숨김 제거
            toggleIcon.classList.remove('bi-chevron-down');
            toggleIcon.classList.add('bi-chevron-up');
            console.log('숨겨진 필터가 표시되었습니다.');
        } else {
            additionalFilters.classList.add('d-none'); // 숨김 추가
            toggleIcon.classList.remove('bi-chevron-up');
            toggleIcon.classList.add('bi-chevron-down');
            console.log('숨겨진 필터가 숨겨졌습니다.');
        }
    });
}