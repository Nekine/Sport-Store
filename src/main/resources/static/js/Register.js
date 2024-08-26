//main cách top 1 khaongr bằng đúng header  
// khi croll qua header thì nó sẽ suất hiện trượt xuống
// Function to handle scroll event
const handleScroll = () => {
    const header = document.querySelector('header');
    const main = document.querySelector('main');

    if (window.scrollY >= header.offsetHeight) {
        header.classList.add('fixed');
        main.style.marginTop = `${header.offsetHeight}px`; // Set margin top for main content
    } else {
        header.classList.remove('fixed');
        main.style.marginTop = '0';
    }
};

// Add scroll event listener
window.addEventListener('scroll', handleScroll);

// gọi khi thay đổi kích thước cửa sổ
window.addEventListener('resize', () => {
    handleScroll(); // Re-evaluate scroll position on resize

});

// body -> body-search-pc
// thao tác ẩn hiện search-pc & cart
document.querySelector('.ti-search').addEventListener('click', () => {
    var search_pc = document.querySelector('.body-search-pc')
    var body_content = document.querySelector('.body-content')

    search_pc.classList.add('active')
    body_content.classList.add('blur')
});

document.querySelector('.ti-shopping-cart').addEventListener('click', () => {
    var cart = document.querySelector('.body-cart')
    var body_content = document.querySelector('.body-content')

    cart.classList.add('active')
    body_content.classList.add('blur')
});

document.querySelector('.close-search-pc').addEventListener('click', () => {
    var search_pc = document.querySelector('.body-search-pc')
    var body_content = document.querySelector('.body-content')

    search_pc.classList.remove('active')
    body_content.classList.remove('blur')
});

document.querySelector('.close-cart').addEventListener('click', () => {
    var cart = document.querySelector('.body-cart')
    var body_content = document.querySelector('.body-content')

    cart.classList.remove('active')
    body_content.classList.remove('blur')
});

// xử lý sự kiện tìm kiếm
function search(idInput, idContent) {
    const searchInput = document.querySelector(idInput);
    const resultsContainer = document.querySelector(idContent);

    var count_product = 0; 
    var name_product = '';

    searchInput.addEventListener('input', function() {
        const keyword = searchInput.value.trim();
        if (keyword.length > 0) {
            count_product = 0; 
            name_product = '';

            fetch(`/api/products/search?keyword=${encodeURIComponent(keyword)}`)
                .then(response => response.json())
                .then(products => {
                    displayResults(products);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        } else {
            resultsContainer.innerHTML = ''; // Clear results if input is empty
        }
    });

    function displayResults(products) {
        resultsContainer.innerHTML = '';
        for(let i=0; i<products.length; i++){
            if(products[i].ten !== name_product && count_product < 5){
                var product_item = `
                    <a th:href="@{/neekine/products/${products[i].namePathProduct}}" class="search-item row">
                        <div class="content-product col l-10 m-11 c-10">
                            <p>${products[i].ten}</p>`

                if(products[i].phan_tram !== 0){
                    product_item += ` <div class="cost">
                                <span class="new-price">${formatCurrency(products[i].gia_ban * (1-products[i].phan_tram/100))}₫</span>
                                <span class="old-price">${formatCurrency(products[i].gia_ban)}₫</span>
                            </div>`
                }
                else{
                    product_item += ` <div class="cost">
                                <span class="price">${formatCurrency(products[i].gia_ban)}₫</span>
                            </div>`
                }

                product_item += `</div>
                        <div class="image-product col l-2 m-1 c-2">
                            <img src="/images/${products[i].photoNames[0]}" alt="">
                        </div>
                    </a>
                `

                resultsContainer.innerHTML += product_item;
                name_product = products[i].ten;
                count_product++;
            }
            else if(products[i].ten !== name_product){
                name_product = products[i].ten;
                count_product++;
            }
        }

        if(count_product > 5){
            resultsContainer.innerHTML += `
                <a th:href="@{/neekine}" class="resultsMore">
                    Xem thêm ${count_product - 5} sản phẩm
                </a>
            `
        }

        // click vào thẻ a thì sẽ chuyển đến url mong muốn
        redirect_to_path();
    }
}

// xử lý hiện sản phẩm trong giỏ hàng và các thao tác
function cart(){
    const cartElement = document.querySelector('.content-cart');
    const sumCost = document.querySelector('.total-cost');

    fetch(`/api/products/cart`)
                .then(response => response.json())
                .then(products => {
                    displayResults(products);
                })
                .catch(error => {
                    console.log(error);
                });

    function displayResults(products) {
        cartElement.innerHTML = '';
        var sum = 0;

        for(let i=0; i<products.length; i++){
            sum += (products[i].gia_ban * (1-products[i].phan_tram/100)) * products[i].so_luong;

            var product_item = `
                <tr class="row">
                    <td class="img col l-3 m-3 c-3">
                        <a th:href="@{/neekine/products/${products[i].namePathProduct}}" title="/neekine/products/${products[i].namePathProduct}">
                            <img src="/images/${products[i].photoNames[0]}">
                        </a>
                    </td>
                    <td class="col l-8 m-8 c-8">
                        <a class="pro-title-view" th:href="@{/neekine/products/${products[i].namePathProduct}}" title="/neekine/products/${products[i].namePathProduct}">${products[i].ten}</a><br>
                        `
            if(products[i].kich_thuoc !== '0'){
                product_item += `<span class="variant">${products[i].kich_thuoc}</span>`
            }
            else {
                product_item += `<span class="variant"></span>`
            }
            product_item +=
                        `
                        <span class="pro-quantity-view">${products[i].so_luong}</span>
                        <span class="pro-price-view">${formatCurrency(products[i].gia_ban * (1-products[i].phan_tram/100))}₫</span>
                    </td>
                    <td class="col l-1 m-1 c-1">
                        <span class="ti-close drop-product"></span>
                    </td>
                </tr>
            `

            cartElement.innerHTML += product_item;
        }

        // tổng giá trị các sản phẩm
        sumCost.innerHTML = `<span>Tổng tiền: ${formatCurrency(sum)}₫</span>`

        // click vào thẻ a thì sẽ chuyển đến url mong muốn
        redirect_to_path();

        // xóa sản phẩm trong giỏ hàng
        deleteProductsFromCart();
    }
}

function formatCurrency(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

document.addEventListener('DOMContentLoaded', function(){
    search('#searchInput-1', '#search-1');
    search('#searchInput-2', '#search-2');

    formatPrice();
    selectSize();
    displayProductImages()
    cart();
});

// click vào thẻ a thì sẽ chuyển đến url mong muốn
function redirect_to_path(){
    const List_A_Elements = document.querySelectorAll('a');
    List_A_Elements.forEach(function(item) {
        item.addEventListener('click', function() {
            // Lấy giá trị của thuộc tính th:href
            const thHref = item.getAttribute('th:href');

            // Kiểm tra nếu thHref không null và có định dạng @{...}
            if (thHref && thHref.startsWith('@{') && thHref.endsWith('}')) {
                // Trích xuất URL bên trong @{}
                var url = thHref.slice(2, -1);

                // chuyển về định dạng phân trang nếu có
                url = url.replace("(", "?").replace(")", "");

                // Điều hướng đến URL
                window.location.href = url;
            } else {
                console.error("Invalid th:href format");
            }
        });
    });
}

// #########################################################################

// chuyển price them đúng form tiền VND
function formatPrice(){
    var oldPriceElements = document.querySelectorAll('.old-price');
    var newPriceElements = document.querySelectorAll('.new-price');
    var priceElements = document.querySelectorAll('.price');

    oldPriceElements.forEach(function(element) {
        var number = parseFloat(element.textContent);
        element.textContent = formatCurrency(number) + '₫';
    });

    newPriceElements.forEach(function(element) {
        var number = parseFloat(element.textContent);
        element.textContent = formatCurrency(number) + '₫';
    });
    
    priceElements.forEach(function(element) {
        var number = parseFloat(element.textContent);
        element.textContent = formatCurrency(number) + '₫';
    });
}

// chọn size product
function selectSize(){
    let button = document.querySelectorAll('.size')
    button.forEach(function(item){
        item.addEventListener('click', () => {
            const activeSize = document.querySelector('.size.active');
            if(activeSize){
                activeSize.classList.remove('active');
            }

            item.classList.add('active');
        })
    });
}

// tăng/giảm số lượng sản phẩm
function minusQuantity() {
    const quantityInput = document.getElementById('quantity');
    let currentQuantity = parseInt(quantityInput.value);

    if (currentQuantity > 1) {
        quantityInput.value = currentQuantity - 1;
    }
}

function plusQuantity() {
    const quantityInput = document.getElementById('quantity');
    let currentQuantity = parseInt(quantityInput.value);

    quantityInput.value = currentQuantity + 1;
}

// gửi dữ liệu input product
document.getElementById('add-to-cart-form').addEventListener('submit', function(event) {
    event.preventDefault(); // Ngăn chặn hành vi mặc định của form

    // Lấy dữ liệu product
    var size = document.querySelector('.size.active');
    var size_2 = document.querySelector('#size');
    const quantity = document.getElementById('quantity').value;
    const name = document.getElementById('nameProduct').textContent;

    if(size){
        size = size.value;
    }
    else if(size_2){
        size = "0";
    }
    else {
        alert('Vui lòng chọn size sản phẩm !!');
        return;
    }

    // Tạo dữ liệu để gửi đến server
    const data = {
        name: name,
        size: size,
        quantity: quantity
    };
    // // Gửi dữ liệu qua AJAX
    fetch('/api/products/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(response => {
        const cartElement = document.querySelector(".body-cart");
        const body = document.querySelector(".body-content");
        cartElement.classList.add("active");
        body.classList.add("blur");

        cart();
    })
    .catch((error) => {
        alert('Vui lòng đăng nhập tài khoản để mua sản phẩm !!')
    });
});

// gửi yêu cầu xóa products trong giỏ hàng
function deleteProductsFromCart(){
    document.querySelectorAll('.drop-product').forEach(function (item, index){
        item.addEventListener('click', () => {
            const name = document.querySelectorAll('.pro-title-view')[index].textContent;
            let size = document.querySelectorAll('.variant')[index].textContent;
            const quantity = document.querySelectorAll('.pro-quantity-view')[index].textContent;
            if(size === ""){
                size = "0";
            }
    
            const data = {
                name: name,
                size: size,
                quantity: quantity
            };
    
            fetch('/api/products/cart/delete', {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(response => {
                cart();
            })
            .catch((error) => {
                console.log("Error: " + error);
            });
        });
        
    });
}

// #########################################################################

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