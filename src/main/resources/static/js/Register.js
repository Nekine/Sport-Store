// Đối tượng `Validator`
function Validator(options) {
    function getParent(element, selector) {
        while (element.parentElement) {
            if (element.parentElement.matches(selector)) {
                return element.parentElement;
            }
            element = element.parentElement;
        }
    }

    var selectorRules = {};

    // Hàm thực hiện validate
    function validate(inputElement, rule) {
        var errorElement = getParent(inputElement, options.formGroupSelector).querySelector(options.errorSelector);
        var errorMessage;

        // Lấy ra các rules của selector
        var rules = selectorRules[rule.selector];
        
        // Lặp qua từng rule & kiểm tra
        // Nếu có lỗi thì dừng việc kiểm
        for (var i = 0; i < rules.length; ++i) {
            errorMessage = rules[i](inputElement.value);
            if (errorMessage) break;
        }
        
        if (errorMessage) {
            errorElement.innerText = errorMessage;
            getParent(inputElement, options.formGroupSelector).classList.add('invalid');
        } else {
            errorElement.innerText = '';
            getParent(inputElement, options.formGroupSelector).classList.remove('invalid');
        }

        // thay đổi height khi có lỗi
        FormHeight()

        return !errorMessage;
    }

    // Lấy element của form cần validate
    var formElement = document.querySelector(options.form);
    if (formElement) {
        // Khi submit form
        formElement.onsubmit = function (e) {
            e.preventDefault();

            var isFormValid = true;

            // Lặp qua từng rules và validate
            options.rules.forEach(function (rule) {
                var inputElement = formElement.querySelector(rule.selector);
                var isValid = validate(inputElement, rule);
                if (!isValid) {
                    isFormValid = false;
                }
            });

            if (isFormValid) {
                // Trường hợp submit với javascript
                if (typeof options.onSubmit === 'function') {
                    var enableInputs = formElement.querySelectorAll('[name]');
                    var formValues = Array.from(enableInputs).reduce(function (values, input) {
                        
                        switch(input.type) {
                            case 'radio':
                                values[input.name] = formElement.querySelector('input[name="' + input.name + '"]:checked').value;
                                break;
                            case 'checkbox':
                                if (!input.matches(':checked')) {
                                    values[input.name] = '';
                                    return values;
                                }
                                if (!Array.isArray(values[input.name])) {
                                    values[input.name] = [];
                                }
                                values[input.name].push(input.value);
                                break;
                            case 'file':
                                values[input.name] = input.files;
                                break;
                            default:
                                values[input.name] = input.value;
                        }

                        return values;
                    }, {});
                    options.onSubmit(formValues);
                }
                // Trường hợp submit với hành vi mặc định
                else {
                    formElement.submit();
                }
            }
        }

        // Lặp qua mỗi rule và xử lý (lắng nghe sự kiện blur, input, ...)
        options.rules.forEach(function (rule) {

            // Lưu lại các rules cho mỗi input
            if (Array.isArray(selectorRules[rule.selector])) {
                selectorRules[rule.selector].push(rule.test);
            } else {
                selectorRules[rule.selector] = [rule.test];
            }

            var inputElements = formElement.querySelectorAll(rule.selector);

            Array.from(inputElements).forEach(function (inputElement) {
               // Xử lý trường hợp blur khỏi input
                inputElement.onblur = function () {
                    validate(inputElement, rule);
                }

                // Xử lý mỗi khi người dùng nhập vào input
                inputElement.oninput = function () {
                    var errorElement = getParent(inputElement, options.formGroupSelector).querySelector(options.errorSelector);
                    errorElement.innerText = '';
                    getParent(inputElement, options.formGroupSelector).classList.remove('invalid');
                } 
            });
        });
    }
}


// Định nghĩa rules
// Nguyên tắc của các rules:
// 1. Khi có lỗi => Trả ra message lỗi
// 2. Khi hợp lệ => Không trả ra cái gì cả (undefined)
Validator.isRequired = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            return value ? undefined :  message || 'Vui lòng nhập trường này'
        }
    };
}

Validator.isEmail = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
            if (!value){
                return 'Vui lòng nhập email của bạn'
            }
            return regex.test(value) ? undefined :  message || 'Trường này phải là email';
        }
    };
}

Validator.isSex = function (selector, message) {
    return {
        selector: selector,
        test: function () {
            // Lấy tất cả các radio button trong nhóm với selector
            const radios = document.querySelectorAll(`.form-radio`);
            // Kiểm tra nếu có radio button nào được chọn
            const value = Array.from(radios).some(radio => radio.checked);

            // Kiểm tra giá trị của radio button được chọn
            return value ? undefined : message 
        }
    }
}


Validator.minLength = function (selector, min, message) {
    return {
        selector: selector,
        test: function (value) {
            if(!value){
                return 'Vui lòng nhập mật khẩu'
            }
            return value.length >= min ? undefined :  message || `Vui lòng nhập tối thiểu ${min} kí tự`;
        }
    };
}

Validator.isConfirmed = function (selector, getConfirmValue, message) {
    return {
        selector: selector,
        test: function (value) {
            return value === getConfirmValue() ? undefined : message || 'Giá trị nhập vào không chính xác';
        }
    }
}

// thay đổi height form
function FormHeight() {
    var Form = document.querySelector('#form-1')
    var Form_group = document.querySelectorAll('.form-group')
    var errorCount = 0;

    Form_group.forEach(function(item){
        if(item.classList.contains('invalid')){
            errorCount++;
        }
    }) 
    
    Form.style.paddingTop = `${110 - errorCount*20}px`;
}

// click thay đổi icon thẻ i và danh sách item trong menu
document.addEventListener('DOMContentLoaded', function(){
    const boxIcon = document.querySelectorAll('.icon-box');
    var bodyItemFilter = document.querySelectorAll('.body-item-filter');
    var iconDownElement = document.querySelectorAll('.ti-angle-down');
    var iconUpElement = document.querySelectorAll('.ti-angle-up');

    var checkElement = -1;

    boxIcon.forEach(function(item, index){
        item.addEventListener('click', function(){
            if(iconDownElement[index+5].classList.contains('none-block')){
                iconDownElement[index+5].classList.remove('none-block');
                iconUpElement[index].classList.add('none-block');
                bodyItemFilter[index].classList.add('none-block');
            }
            else{
                iconDownElement[index+5].classList.add('none-block');
                iconUpElement[index].classList.remove('none-block');
                bodyItemFilter[index].classList.remove('none-block');

                if(index !== checkElement){
                    if(checkElement >= 0){
                        bodyItemFilter[checkElement].classList.add('none-block');
                        iconDownElement[checkElement+5].classList.remove('none-block');
                        iconUpElement[checkElement].classList.add('none-block');
                    }

                    checkElement = index;
                }
            }
        });
    })
});
