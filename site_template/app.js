console.clear();

var menuBtn = document.querySelector('.menu-btn');
var nav = document.querySelector('.side-menu');
var lineOne = document.querySelector('.side-menu .menu-btn .line--1');
var lineTwo = document.querySelector('.side-menu .menu-btn .line--2');
var lineThree = document.querySelector('.side-menu .menu-btn .line--3');
var link = document.querySelector('.side-menu .nav-links');
if ( menuBtn != null ) {
	menuBtn.addEventListener('click', () => {
		nav.classList.toggle('nav-open');
		lineOne.classList.toggle('line-cross');
		lineTwo.classList.toggle('line-fade-out');
		lineThree.classList.toggle('line-cross');
		link.classList.toggle('fade-in');
	})
}

ScrollOut({
	cssProps: {
		visibleY: true,
		viewportY: true
	}
})

// Splitting({ target: '.heading' });
$(function(){
  var duration = 300;
  var $side = $('.scroll-move-box');
  var $side_btn = $side.find('button').on('click', function(){
    $side.toggleClass('open');

    if($side.hasClass('open')){
      $side.stop(true).animate({right:'220px'}, duration);
      $side_btn.find('span').text('▶');
    }
    else {
      $side.stop(true).animate({right:'0px'}, duration);
      $side_btn.find('span').text('◀')
    };
  });
});

$(document).ready(function($) {

        $(".scroll_move").click(function(event){            

                event.preventDefault();

                $('html,body').animate({scrollTop:$(this.hash).offset().top}, 500);

        });

});