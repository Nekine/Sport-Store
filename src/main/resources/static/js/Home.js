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

// Add resize event listener to handle changes in screen size
window.addEventListener('resize', () => {
    handleScroll(); // Re-evaluate scroll position on resize
});

// main -> section

// // Hàm kiểm tra xem phần tử đã cuộn đến chưa
// function isElementInViewport(el, index) {
//     var rect = el.getBoundingClientRect();
//     console.log(rect.top)
//     return (
//         rect.top >= 0 
//         // rect.bottom <= (window.innerHeight || document.documentElement.clientHeight)
//     );
// }

// // Hàm xử lý khi cuộn trang
// function handleScroll() {
//     var section = document.querySelectorAll('section');
//     section.forEach(function(section_item, index) {
//         if (isElementInViewport(section_item, index)) {
//             section_item.classList.add('visible'); // Thêm lớp visible khi cuộn đến phần tử
//             //window.removeEventListener('scroll', handleScroll); // Ngừng lắng nghe sự kiện cuộn khi đã hiển thị
//         }
//     })
//     console.log("ok")
// }

// // Lắng nghe sự kiện cuộn trang
// window.addEventListener('scroll', handleScroll);

// main -> section -> nhom 1
document.addEventListener('DOMContentLoaded', function() {
    const shoesElement = document.querySelector('.shoes');
    const boxElement = document.querySelector('.box-product');

    var count_shoes = 10;

    for(let i = 0; i < count_shoes; i++) {
        const product_item = `
            <div class="col l-1-2 m-1-2 c-1-2 product">
                    <div class="product-item">
                        <a href="/neekine">
                            <div class="product-img">
                                <span class="percent">20%</span>
                                <img src="/images/shoes-${i+1}.1.jpg" alt="" class="first-img">
                                <img src="/images/shoes-${i+1}.2.jpg" alt="" class="second-img">
                            </div>
                            <p>New Balance Fresh Foam Arishi v3 Slip Resistant - Grey</p>
                        </a>

                        <div class="cost">
                            <span class="price">1,000,000 đ</span>
                            <span class="old-price">1,250,000 đ</span>
                        </div>
                    </div>
                </div>
        `;
        
        boxElement.innerHTML += product_item;
        console.log(boxElement)
    }

    shoesElement.innerHTML += `
        <div class="prev">
            <i class="ti-angle-left"></i>
        </div>
    `

    shoesElement.innerHTML += `
        <div class="next">
            <i class="ti-angle-right"></i>
        </div>
    `
});

// main -> section -> nhom 1 (hieu ung chuyen san pham)
document.addEventListener('DOMContentLoaded', function() {
    const slider = document.querySelector('.box-product');
    const slides = document.querySelectorAll('.product');
    const nextButton = document.querySelector('.next');
    const prevButton = document.querySelector('.prev');
    let index = 0;

    nextButton.addEventListener('click', () => {
        if (index < slides.length - 5) {
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
});


// main -> section -> nhom 2,3
document.addEventListener('DOMContentLoaded', function() {
    const bagElement = document.querySelector('.bag');
    const clothesElement = document.querySelector('.clothes');

    // Lấy chiều rộng của toàn bộ trang (viewport)
    const viewportWidth = window.innerWidth;
    var count_bag = 8;
    var count_clothes = 7;

    if(viewportWidth <= 1112){
        count_clothes = 6;
    }
    else {
        count_clothes = 7;
    }

    for(let i=2; i<count_bag; i++){
        const product_item = `
            <div class="col l-2-4 m-6 c-6 product">
                <div class="product-item">
                    <a href="/neekine">
                        <div class="product-img">
                            <span class="percent">20%</span>
                            <img src="/images/balo-${i+1}.1.jpg" alt="" class="first-img">
                            <img src="/images/balo-${i+1}.2.jpg" alt="" class="second-img">
                        </div>
                        <p>New Balance Fresh Foam Arishi v3 Slip Resistant - Grey</p>
                    </a>

                    <div class="cost">
                        <span class="price">1,000,000 đ</span>
                        <span class="old-price">1,250,000 đ</span>
                    </div>
                </div>
            </div>
        `;

        bagElement.innerHTML += product_item;
    }

    for(let i=0; i<count_clothes; i++){
        const product_item = `
        <div class="col l-2-4 m-6 c-6 product">
            <div class="product-item">
                <a href="/neekine">
                    <div class="product-img">
                        <span class="percent">20%</span>
                        <img src="/images/coat-${i+1}.1.jpg" alt="" class="first-img">
                        <img src="/images/coat-${i+1}.2.jpg" alt="" class="second-img">
                    </div>
                    <p>New Balance Fresh Foam Arishi v3 Slip Resistant - Grey</p>
                </a>

                <div class="cost">
                    <span class="price">1,000,000 đ</span>
                    <span class="old-price">1,250,000 đ</span>
                </div>
            </div>
        </div>
        `;

        clothesElement.innerHTML += product_item;
    }
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