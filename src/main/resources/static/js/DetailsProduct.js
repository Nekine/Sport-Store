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

// hiển thị hình ảnh sản phẩm
function displayProductImages() {
    const listImages = document.querySelectorAll('.list-img .item-img');
    const mainImage = document.getElementById('mainImage');


    listImages.forEach(function(item) {
        item.addEventListener('click', function() {
            // cho biết là đang hiển thị hình ảnh nào trong list images
            const imgActive = document.querySelector('img.active');
            if(imgActive){
                imgActive.classList.remove('active');
            }
            const img = item.querySelector('img')
            img.classList.add('active');

            // hiển thị hình ảnh chọn trong list lên
            mainImage.src = img.src
        });
    });
}

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
    console.log("ok")
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

document.addEventListener('click', () => {
    selectSize();
});