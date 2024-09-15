function formatCurrency(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

document.addEventListener('DOMContentLoaded', function(){

    fetchProducts();
    formatPrice();
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