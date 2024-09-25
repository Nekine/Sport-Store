// xử lý hiện sản phẩm trong giỏ hàng và các thao tác
var list_quantity = [];

function cart(){
    const cartElement = document.querySelector('.content-cart');
    const sumCost = document.querySelector('.total-cost');
    const count_product = document.querySelector('.count-products-cart');
    const body_page_cart = document.querySelector('.body-page-cart');

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
        body_page_cart.innerHTML = '';
        var sum = 0;
        var count = 0;

        for(let i=0; i<products.length; i++){
            sum += (products[i].gia_ban * (1-products[i].phan_tram/100)) * products[i].so_luong;
            count += products[i].so_luong;
            if(list_quantity.length <= products.length){
                list_quantity.push(products[i].so_luong);
            }

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

            var product_item_2 = `
            <div class="row">
                <div class="img col l-2 m-2 c-3">
                    <a th:href="@{/neekine/products/${products[i].namePathProduct}}" title="/neekine/products/${products[i].namePathProduct}">
                        <img src="/images/${products[i].photoNames[0]}">
                    </a>
                </div>
                <div class="infor col l-10 m-10 c-9">
                    <div class="pro-title-view-page">
                        <a class="title-product" th:href="@{/neekine/products/${products[i].namePathProduct}}" title="/neekine/products/${products[i].namePathProduct}">${products[i].ten}</a>
                    </div>
                    <div class="pro-price-view">${formatCurrency(products[i].gia_ban * (1-products[i].phan_tram/100))}₫</div>
                    `
        if(products[i].kich_thuoc !== '0'){
            product_item_2 += `<div class="variant-page">${products[i].kich_thuoc}</div>`
        }
        else {
            product_item_2 += `<div class="variant-page"></div>`
        }
        product_item_2 +=
                    `
                    <div class="infor-sum-profuct row">
                        <div class="quantity-area col l-10 m-10 c-12">
                            <input type="button" value="-" class="minus">
                            <input type="text" class="value_quantity" name="quantity" value="${list_quantity[i]}" min="${products[i].so_luong}" class="quantity-selector">
                            <input type="button" value="+" class="plus">
                        </div>
                        <div class="pro-sum-price-view col l-2 m-2 c-0">${formatCurrency((products[i].gia_ban * (1-products[i].phan_tram/100)) * products[i].so_luong)}₫</div>
                        <div class="pro-sum-price-view col l-0 m-0 c-12">Thành tiền: ${formatCurrency((products[i].gia_ban * (1-products[i].phan_tram/100)) * products[i].so_luong)}₫</div>
                    </div>
                    <span class="ti-close drop-product-page"></span>
                </div> 
        `

            cartElement.innerHTML += product_item;
            body_page_cart.innerHTML += product_item_2;

        }

        body_page_cart.innerHTML += `
            <div class="total-cost-page">
                Tổng tiền: <span>${formatCurrency(sum)}₫</span>
            </div>
            <div class="button-box-page row">
                <button class="col l-2"><a th:href="@{/neekine}" class="back-buy"><i class="ti-back-left"></i> TIẾP TỤC MUA HÀNG</a></button>
                <button class="col l-2 update-cart">CẬP NHẬT</button>
                <button class="col l-2"><a th:href="@{/neekine/checkout}" class="checkout">THANH TOÁN</a></button>
            </div>
            </div> 
        `

        count_product.innerHTML = `<span>Có ${count} sản phẩm trong giỏ hàng</spam>`

        // tổng giá trị các sản phẩm
        sumCost.innerHTML = `<span>Tổng tiền: ${formatCurrency(sum)}₫</span>`

        // click vào thẻ a thì sẽ chuyển đến url mong muốn
        redirect_to_path();

        // tăng & giảm số lượng sản phẩm
        plus_minus_quantity()

        // xóa sản phẩm trong giỏ hàng
        deleteProductsFromCart();
        deleteProductsFromCartPage();

        // update sản phẩm trong giỏ hàng
        updateProducts();
    }
}

function formatCurrency(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

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

// tăng/giảm số lượng sản phẩm
function plus_minus_quantity(){
    document.querySelectorAll(".minus").forEach(function(item, index){
        item.addEventListener('click', () => {
            minusQuantity(index);
        })
    });
    
    document.querySelectorAll(".plus").forEach(function(item, index){
        item.addEventListener('click', () => {
            plusQuantity(index);
        })
    });
}

function minusQuantity(index) {
    if (list_quantity[index] > 1) {
        list_quantity[index] -= 1;
        cart();
    }
}

function plusQuantity(index) {
    list_quantity[index] += 1;
    cart();
}

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

function deleteProductsFromCartPage(){
    document.querySelectorAll('.drop-product-page').forEach(function (item, index){
        item.addEventListener('click', () => {
            const name = document.querySelectorAll('.title-product')[index].textContent;
            let size = document.querySelectorAll('.variant-page')[index].textContent;
            const quantity = document.querySelectorAll('.value_quantity')[index].value;
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

// update sản phẩm
function updateProducts() {
    document.querySelector('.update-cart').addEventListener('click', () => {
        const names = document.querySelectorAll('.title-product');
        let sizes = document.querySelectorAll('.variant-page');
        const quantities = document.querySelectorAll('.value_quantity');

        var data = []

        names.forEach((item, index) => {
            const name = item.textContent;
            const size = sizes[index].textContent;
            const quantity = quantities[index].value;

            data.push({
                name: name,
                size: size,
                quantity: quantity
            })
        });

        // Gửi yêu cầu PUT đến API
        fetch('/api/products/cart/update', {
            method: 'PUT',
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


document.addEventListener('DOMContentLoaded', function(){
    cart();
});