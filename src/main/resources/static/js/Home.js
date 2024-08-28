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
// thao tác ẩn hiện search-pc & cart & menu (for tablet & mobile)
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

document.querySelector('.ti-menu').addEventListener('click', () => {
    var menu = document.querySelector('.body-menu')
    var body_content = document.querySelector('.body-content')

    menu.classList.add('active')
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

document.querySelector('.close-menu').addEventListener('click', () => {
    var menu = document.querySelector('.body-menu')
    var body_content = document.querySelector('.body-content')

    menu.classList.remove('active')
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

    fetchProducts();
    formatPrice();
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


// #################################################################
// main -> section -> nhom 1, 2, 3
async function fetchProducts() {
    const shoesElement = document.querySelector('.box-product');
    const bagElement = document.querySelector('.bag');
    const clothesElement = document.querySelector('.clothes');

    var count_shoes = 10;
    var count_bags = 10;
    var count_clothes = 10;
    var product_name = '';

    const response = await fetch('/api/products');
    const products = await response.json();

    for(let i=0; i<products.length; i++) {
        if(products[i].ten !== product_name && products[i].loai === "giày" && count_shoes > 0) {
            var product_item = `
            <div class="col l-1-2 m-1-2 c-1-2 product">
                <div class="product-item">
                    <a th:href="@{/neekine/products/${products[i].namePathProduct}}">
                        <div class="product-img">`
            
            if(products[i].phan_tram !== 0){
                product_item += `<span class="percent">${products[i].phan_tram}%</span>`;
            }

            product_item += `<img src="/images/${products[i].photoNames[0]}" alt="" class="first-img">
                            <img src="/images/${products[i].photoNames[1]}" alt="" class="second-img">
                        </div>
                        <p>${products[i].ten}</p>
                    </a>`

            if(products[i].phan_tram !== 0){
                product_item += ` <div class="cost">
                            <span class="new-price">${formatCurrency(products[i].gia_ban * (1-products[i].phan_tram/100))}₫</span>
                            <span class="old-price">${formatCurrency(products[i].gia_ban)}₫</span>
                        </div>`
            }
            else{
                product_item += ` <div class="cost">
                            <span class="price">${products[i].gia_ban.toLocaleString('vi-VN')}₫</span>
                        </div>`
            }
                `</div>
            </div>
            `;
            shoesElement.innerHTML += product_item;

            product_name = products[i].ten;
            count_shoes--;
        }
        else if(products[i].ten !== product_name && products[i].loai === "bag" && count_bags > 0) {
            var product_item = `
            <div class="col l-2-4 m-6 c-6 product">
                <div class="product-item">
                    <a th:href="@{/neekine/products/${products[i].namePathProduct}}">
                        <div class="product-img">`
            
            if(products[i].phan_tram !== 0){
                product_item += `<span class="percent">${products[i].phan_tram}%</span>`;
            }

            product_item += `<img src="/images/${products[i].photoNames[0]}" alt="" class="first-img">
                            <img src="/images/${products[i].photoNames[1]}" alt="" class="second-img">
                        </div>
                        <p>${products[i].ten}</p>
                    </a>`

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
                `</div>
            </div>
            `;
            bagElement.innerHTML += product_item;

            product_name = products[i].ten;
            count_bags--;
        }
        else if(products[i].ten !== product_name && products[i].loai === "clothes" && count_clothes > 0) {
            var product_item = `
            <div class="col l-2-4 m-6 c-6 product">
                <div class="product-item">
                    <a th:href="@{/neekine/products/${products[i].namePathProduct}}">
                        <div class="product-img">`
            
            if(products[i].phan_tram !== 0){
                product_item += `<span class="percent">${products[i].phan_tram}%</span>`;
            }

            product_item += `<img src="/images/${products[i].photoNames[0]}" alt="" class="first-img">
                            <img src="/images/${products[i].photoNames[1]}" alt="" class="second-img">
                        </div>
                        <p>${products[i].ten}</p>
                    </a>`
            
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
                    
                `</div>
            </div>
            `;
            clothesElement.innerHTML += product_item;

            product_name = products[i].ten;
            count_clothes--;
        }
        
        if(count_shoes === 0 && count_bags === 0 && count_clothes === 0){
            break;
        }
    }

    // click vào thẻ a thì sẽ chuyển đến url mong muốn
    redirect_to_path();

    // xử lý sự kiện chuyển shoes products
    const slider = document.querySelector('.box-product');
    const nextButton = document.querySelector('.next');
    const prevButton = document.querySelector('.prev');
    let index = 0;

    nextButton.addEventListener('click', () => {
        if (index < 5) {
            index++;
            updateSlider();
        }
    });

    prevButton.addEventListener('click', () => {
        if (index > 0) {
            index--;
            updateSlider();
        }
    });

    function updateSlider() {
        slider.style.transform = `translateX(-${index * 10}%)`;
    }
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
