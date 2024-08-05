//main cách top 1 khaongr bằng₫úng header  
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

// Add resize event listener to handle changes in screen size
window.addEventListener('resize', () => {
    handleScroll(); // Re-evaluate scroll position on resize
});


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
                    <a th:href="@{/neekine}">
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
                    <a th:href="@{/neekine}">
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
                    <a th:href="@{/neekine}">
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

function formatCurrency(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

document.addEventListener('DOMContentLoaded', fetchProducts);

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
document.addEventListener('DOMContentLoaded', function(){
    search('#searchInput-1', '#search-1');
    search('#searchInput-2', '#search-2');
});
