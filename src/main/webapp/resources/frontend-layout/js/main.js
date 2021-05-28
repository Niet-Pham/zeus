//<![CDATA[
// do when access web
(function() {

})();

// do st after ready
$(document).ready(function() {
    searchClick(false);
    $('.carousel').carousel({
        interval: 200000
    });
    $('.content-introduce-detail__content').hover(
        function() {
            $(this).addClass('active');
        },
        function() {
            $(this).removeClass('active');
        }
    );
    scrollPage();
    autoPlayCarousel('.my-projects-slide', 300000, true, 3);
    autoPlayCarousel('.content-brands-slides', 300000, false, 4);
    autoPlayCarousel('.my-feedback-slide', 300000, false, 3);
});

// change bg navrbar after scroll
$(function() {
    $(document).scroll(function() {
        var $nav = $(".navbar-fixed-top");
        $nav.toggleClass('scrolled', $(this).scrollTop() > $nav.height());
    });
});

// scroll down to hide / up to show
function scrollPage() {
    var prevScrollpos = window.pageYOffset;
    window.onscroll = function() {
        var currentScrollPos = window.pageYOffset;
        if (prevScrollpos > currentScrollPos) {
            $('#navbar').css('top', '0');
        } else {
            $('#navbar').css('top', '-5rem');
        }
        prevScrollpos = currentScrollPos;
    }
}

// search
function searchClick(e) {
    if (e === true) {
        $('.search-function').hide();
        $('.nav').addClass('display-none');
        $('.form-inline').css('width', '100%').css('left', '20rem');
        $('.search-function__input').css('width', '100%').css('padding', '10px 0');
        $('.nav-search').css('width', $(window).width() >= 1900 ? '97%' : '90%');
        $('.search-function__input').show();
        $('.nav-search').focus();
    } else {
        $('.search-function').show();
        $('.nav').removeClass('display-none');
        $('.navbar-brand').removeClass('display-none');
        $('.collapse').removeClass('padding-1rem');
        $('.form-inline').css('width', '').css('left', '');
        $('.search-function__input').css('width', '');
        $('.nav-search').css('width', '');
        $('.search-function__input').hide();
        $('.result-search').addClass('display-none');
    }
}

// override function owl carousel
function autoPlayCarousel(className, time, check, item) {
    var owl = $(className);

    owl.owlCarousel({
        margin: 10,
        loop: true,
        center: true,
        autoplay: true,
        autoplayTimeout: time,
        autoplayHoverPause: true,
        navText: check ? ["<div class='nav-btn prev-slide'></div>", "<div class='nav-btn next-slide'></div>"] : [],
        responsiveClass: true,
        responsive: {
            0: {
                items: 1,
                nav: false
            },
            480: {
                items: 1,
                nav: false
            },
            768: {
                items: 1,
                nav: false
            },
            992: {
                items: 1,
                nav: false
            },
            1024: {
                items: item,
                nav: false
            },
            1170: {
                items: item,
                nav: true
            },
            1366: {
                items: item,
                nav: true
            },
            1920: {
                items: item,
                nav: true
            }
        }
    });
    owl.on('changed.owl.carousel', function() {
        hoverToDiaplayContent(className);
    });
}

// hover to display content owl causel
function hoverToDiaplayContent(className) {
    if (className === '.my-feedback-slide') {
        return;
    }
    var item = $('.my-projects-slide .center').children();
    item.on('mouseover', function() {
        $(item).children('.item__img').addClass('display-none');
        $(item).children('.item__detail').removeClass('display-none');
    });
    item.on('mouseout', function() {
        $(item).children('.item__img').removeClass('display-none');
        $(item).children('.item__detail').addClass('display-none');
    });
}

//close modal
function useModal(e) {
    if(e === true){
        $('#login-modal').modal('hide');
        $('#register-modal').modal('show');
        $('#verify').css('display','none');
        $('#fullName').val(null);
        $('#register_account').val(null);
        $('#register_password').val(null);
        $('#rePassword').val(null);
        $('#phone').val(null);
        $('#email').val(null);
        $('#verify').val(null);
    }
    else {
        $('#register-modal').modal('hide');
        $('#login-modal').modal('show');
        $('#login_account').val(null);
        $('#login_password').val(null);
    }
}

//show verify register
function showVerifyRegister() {
    $('#verify').attr('style','display: block');
}
//]]>