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


// click thay đổi icon thẻ i của thẻ span
document.addEventListener('DOMContentLoaded', function(){
    const itemFilter = document.querySelectorAll('.item-filter');
    var bodyItemFilter = document.querySelectorAll('.body-item-filter');
    var iconDownElement = document.querySelectorAll('.ti-angle-double-down');
    var iconUpElement = document.querySelectorAll('.ti-angle-double-up');

    itemFilter.forEach(function(item, index){
        item.addEventListener('click', function(){
            if(iconDownElement[index].classList.contains('none-block')){
                iconDownElement[index].classList.remove('none-block');
                iconUpElement[index].classList.add('none-block');
                bodyItemFilter[index].classList.add('none-block');
                console.log("ok1")
            }
            else{
                console.log("ok")
                iconDownElement[index].classList.add('none-block');
                iconUpElement[index].classList.remove('none-block');
                bodyItemFilter[index].classList.remove('none-block');
            }
        });
    })
});