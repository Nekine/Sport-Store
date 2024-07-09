//main cách top 1 khaongr bằng đúng header 

window.addEventListener('DOMContentLoaded', (event) => {
    const header = document.querySelector('header')
    const main = document.querySelector('main')

    if(header.classList.contains('fixed')) {
        const headerHeight = header.offsetHeight;
        main.style.marginTop = `${headerHeight}px`;
    }
});

// khi croll qua header thì nó sẽ suất hiện trượt xuống
window.addEventListener('scroll', () => {
    const header = document.querySelector('header')
    const main = document.querySelector('main')

    if(window.scrollY >= header.offsetHeight) {
        header.classList.add('fixed');
    }
    else {
        header.classList.remove('fixed');
        main.style.marginTop = 0;
    }
});

// main -> nhom 1
// var group_1 = document.getElementsByClassName('shoes');

// for(let i = 0; i < 6; i++) {
//     const product_item = `
//         <div class="col l-2-4 m-6 c-12">
//             <div class="product-item">
//                 <a href="/neekine">
//                     <div class="product-img">
//                         <img src="/images/shoes-${i+1}.1.jpg" alt="" class="first-img">
//                         <img src="/images/shoes-${i+1}.2.jpg" alt="" class="second-img">
//                     </div>
//                     <p>New Balance Fresh Foam Arishi v3 Slip Resistant - Grey</p>
//                 </a>
//             </div>
//         </div>
//     `;

//     group_1.innerHTML += product_item;
// }
