var menuBtn = document.querySelector('.menu-btn');
var nav = document.querySelector('.side-menu');
var lineOne = document.querySelector('.side-menu .menu-btn .line--1');
var lineTwo = document.querySelector('.side-menu .menu-btn .line--2');
var lineThree = document.querySelector('.side-menu .menu-btn .line--3');
var link = document.querySelector('.side-menu .nav-links');
menuBtn.addEventListener('click', () => {
    nav.classList.toggle('nav-open');
    lineOne.classList.toggle('line-cross');
    lineTwo.classList.toggle('line-fade-out');
    lineThree.classList.toggle('line-cross');
    link.classList.toggle('fade-in');
})
console.clear();

ScrollOut({
  cssProps: {
    visibleY: true,
    viewportY: true
  }
});

Splitting({ target: '.heading' });
  



