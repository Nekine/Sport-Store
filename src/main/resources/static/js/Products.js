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
    toggleVisibility();

});

// body -> body-search-pc
// thao tác ẩn hiện search-pc
document.querySelector('.ti-search').addEventListener('click', () => {
    var search_pc = document.querySelector('.body-search-pc')
    var body_content = document.querySelector('.body-content')

    search_pc.classList.add('active')
    body_content.classList.add('blur')
});

document.querySelector('.ti-close').addEventListener('click', () => {
    var search_pc = document.querySelector('.body-search-pc')
    var body_content = document.querySelector('.body-content')

    search_pc.classList.remove('active')
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
                    <a th:href="@{/neekine}" class="search-item row">
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
    }
}

function formatCurrency(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

document.addEventListener('DOMContentLoaded', function(){
    search('#searchInput-1', '#search-1');
    search('#searchInput-2', '#search-2');
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

// click thay đổi icon thẻ i và danh sách sản phẩm của thẻ span
document.addEventListener('DOMContentLoaded', function(){
    const itemFilter = document.querySelectorAll('.item-filter');
    var bodyItemFilter = document.querySelectorAll('.body-item-filter');
    var iconDownElement = document.querySelectorAll('.ti-angle-double-down');
    var iconUpElement = document.querySelectorAll('.ti-angle-double-up');

    var checkElement = -1;

    itemFilter.forEach(function(item, index){
        item.addEventListener('click', function(){
            if(iconDownElement[index].classList.contains('none-block')){
                iconDownElement[index].classList.remove('none-block');
                iconUpElement[index].classList.add('none-block');
                bodyItemFilter[index].classList.add('none-block');
            }
            else{
                iconDownElement[index].classList.add('none-block');
                iconUpElement[index].classList.remove('none-block');
                bodyItemFilter[index].classList.remove('none-block');

                if(index !== checkElement){
                    if(checkElement >= 0){
                        bodyItemFilter[checkElement].classList.add('none-block');
                        iconDownElement[checkElement].classList.remove('none-block');
                        iconUpElement[checkElement].classList.add('none-block');
                    }

                    checkElement = index;
                }
            }
        });
    })
});

// tắt/bật thanh sidebar menu khi ở tablet & mobile 
function toggleVisibility(){
    const titleSidebar = document.querySelectorAll('.title-sidebar');
    const bodySidebar = document.querySelectorAll('.body-sidebar');
    const iconDown = document.querySelectorAll('.title-sidebar .ti-angle-down');
    const iconUp = document.querySelectorAll('.title-sidebar .ti-angle-up');
    
    // ẩn sidebar khi chuyển sang tablet & mobile
    const width = getWindowWidth();
    if(width < 1000){
        bodySidebar.forEach(function(item){
            item.classList.add('none-block');
        });
    }
    else {
        bodySidebar.forEach(function(item){
            item.classList.remove('none-block');
        });
    }

    // lick để bật tắt sidebar
    titleSidebar.forEach(function(item, index){
        item.addEventListener('click', function(){
            if(bodySidebar[index].classList.contains('none-block')){
                bodySidebar[index].classList.remove('none-block');
                iconDown[index].classList.add('none-block');
                iconUp[index].classList.remove('none-block');
            }
            else{
                bodySidebar[index].classList.add('none-block');
                iconDown[index].classList.remove('none-block');
                iconUp[index].classList.add('none-block');
            }
        });
    });
};
document.addEventListener('DOMContentLoaded', toggleVisibility());

// lấy kích thước width web
function getWindowWidth() {
    const width = document.documentElement.clientWidth || document.body.clientWidth;
    return width;
}


// lấy dữ liệu từ API và hiển thị Products
function displayProducts(url){
    const boxProductElement = document.querySelector('.box-product');
    const paginationElement = document.querySelector('.pagination');

    fetch(url)
        .then(response => response.json())
        .then(products => {
            displayResults(products);
        })
        .catch(error => {
            console.error('Error:', error);
        });

    function displayResults(products) {
        boxProductElement.innerHTML = ''; // Clear products if any
        paginationElement.innerHTML = ''; // Clear pagination if any
        // hiển thị sản phẩm
        for(const product of products.content){
            var product_item = `
            <div class="col l-3 m-6 c-6 product">
                <div class="product-item">
                    <a th:href="@{/neekine}">
                        <div class="product-img">`
            
            if(product.phan_tram !== 0){
                product_item += `<span class="percent">${product.phan_tram}%</span>`;
            }

            product_item += `<img src="/images/${product.photoNames[0]}" alt="" class="first-img">
                            <img src="/images/${product.photoNames[1]}" alt="" class="second-img">
                        </div>
                        <p>${product.ten}</p>
                    </a>`

            if(product.phan_tram !== 0){
                product_item += ` <div class="cost">
                            <span class="new-price">${formatCurrency(product.gia_ban * (1-product.phan_tram/100))}₫</span>
                            <span class="old-price">${formatCurrency(product.gia_ban)}₫</span>
                        </div>`
            }
            else{
                product_item += ` <div class="cost">
                            <span class="price">${formatCurrency(product.gia_ban)}₫</span>
                        </div>`
            }
                `</div>
            </div>
            `;
            boxProductElement.innerHTML += product_item;
        }

        if(products.content.length === 0){
            boxProductElement.innerHTML = '<p class="no-product">Không tìm thấy sản phẩm nào</p>';
        }

        // hiển thị phân trang
        var pagination = ``;

        if(!products.first){
            pagination += `
                <li>
                    <a th:href="@{/neekine/collections/all(page=${products.number})}" class="ti-arrow-left"></a>
                </li>
            `
        }

        for(let i=1; i<=products.totalPages; i++){      
            if((i <= products.number+2) && (i >= products.number)){
                if(i == (products.number+1)){
                    pagination += `<li class="active">`
                }
                else{
                    pagination += `<li>`
                }
                
                pagination += `
                        <a th:href="@{/neekine/collections/all(page=${i})}">${i}</a>
                    </li>
                `
            }
        }

        if(!products.last){
            pagination += `
                <li>
                    <a th:href="@{/neekine/collections/all(page=${products.number+2})}" class="ti-arrow-right"></a>
                </li>
            `
        }

        paginationElement.innerHTML += pagination;

        // Thêm lớp 'show' để kích hoạt hiệu ứng cho tất cả các sản phẩm
        const productDivs = document.querySelectorAll('.product');
        setTimeout(() => {
            productDivs.forEach(div => {
                div.classList.add('show');
            });
        }, 100);

        // click vào thẻ a thì sẽ chuyển đến url mong muốn
        redirect_to_path();
    }
};

// gửi yêu cầu filter với các điều kiện được chọn
function request(){
    // tách path để biết xem đang ở trang nào
    let pathname = window.location.href;
    let segments = pathname.split('/');
    let typeCollections = "";

    typeCollections = segments[segments.length - 1];
    let url = '/api/products/collections/' + typeCollections;
    
    return url;
}

// duyệt qua các input[type="checkbox"] xem được thêm điều kiện nào để add vào request
document.querySelectorAll('.body-item-filter input[type="checkbox"]').forEach(input => {
    input.addEventListener('change', function() {
        let brands = [];
        let prices = [];
        let sizes = [];

        // Duyệt qua tất cả các checkbox đang được chọn
        document.querySelectorAll('.body-item-filter input[type="checkbox"]:checked').forEach(checkedInput => {
            // Phân loại checkbox theo loại (brand, price, size)
            if (checkedInput.name === 'brand') {
                brands.push(checkedInput.value);
            } else if (checkedInput.name === 'price') {
                prices.push(checkedInput.value);
            } else if (checkedInput.name === 'size') {
                sizes.push(checkedInput.value);
            }
        });

        // Cuộn trang về đầu trang khi thêm sản phẩm
        var width = getWindowWidth();
        const boxProductElement = document.querySelector('.box-product');
        if(width <= 1000){
            boxProductElement.scrollIntoView({
                top: 0,
                behavior: 'smooth' // Hiệu ứng cuộn mượt mà
            });
        }
        else {
            window.scrollTo({
                top: 0,
                behavior: 'auto' // Hiệu ứng cuộn tự đ��ng
            });
        }

        // Tạo URL với các tham số filter được nối lại bằng ký tự '&'
        let url = request() + '?filter=' + brands.concat(prices, sizes).join('&');
        displayProducts(url);
    });
});

// chạy các hàm mong muốn
displayProducts(request());