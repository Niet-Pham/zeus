(function() {
    "use strict";
    // Scroll top
    $(window).on('load', function() {
        var wd = $(window).width();
        if ($('.scroll-to-top').length) {
            $(window).scroll(function() {
                if ($(this).scrollTop() > 1) {
                    $('.scroll-to-top').css({ bottom: "25px" });
                    if (wd > 991) {
                        $('.home .header').addClass("is-sticky");
                    }
                    if (wd < 991) {
                        $('.home .header').addClass("is-sticky");
                    }
                } else {
                    $('.scroll-to-top').css({ bottom: "-100px" });
                    $('.home .header').removeClass("is-sticky");
                }
            });

            $('.scroll-to-top').on('click', function() {
                $('html, body').animate({ scrollTop: '0px' }, 800);
                return false;
            });
        }
    });
    $(window).resize(function() {
        var wd = $(window).width();
        if ($('.scroll-to-top').length) {
            $(window).scroll(function() {
                if ($(this).scrollTop() > 1) {
                    $('.scroll-to-top').css({ bottom: "25px" });
                    if (wd > 991) {
                        $('.header').addClass("is-sticky");
                    }
                } else {
                    $('.scroll-to-top').css({ bottom: "-100px" });
                    $('.header').removeClass("is-sticky");
                }
            });

            $('.scroll-to-top').click(function() {
                $('html, body').animate({ scrollTop: '0px' }, 800);
                return false;
            });
        }
    });

    $('[data-toggle="tooltip"]').tooltip();
    $(document).ready(function() {
        /* Menu sidebar */
        $("<p></p>").insertAfter(".nav_categories > li > a");
        var $p = $(".nav_categories > li p");
        $(".nav_categories > li p:first").append('<span><i class="fa fa-plus-square-o"></i></span>');
        $(".nav_categories > li p:not(:first)").append('<span><i class="fa fa-plus-square-o"></i></span>');
        $(".nav_categories > li >ul:not(:first)").hide();
        $(".nav_categories > li").each(function() {
            if ($(this).find("ul > li").length == 0) {
                $(this).find('p').remove();
            }
        });

        $p.click(function() {
            $('.nav_categories > li').removeClass('active');
            $(this).parents("li").addClass('active');
            var $accordion = $(this).nextAll('ul');
            if ($accordion.is(':hidden') === true) {

                $(".nav_categories > li > ul").slideUp();
                $accordion.slideDown();

                $p.find('span').remove();
                $p.append('<span><i class="fa fa-plus-square-o"></i></span>');
                $(this).find('span').remove();
                $(this).append('<span><i class="fa fa-minus-square-o"></i></span>');
            } else {
                $accordion.slideUp();
                $(this).find('span').remove();
                $(this).append('<span><i class="fa fa-plus-square-o"></i></span>');
            }
        });

        $('.fancybox').fancybox();
    });
    // Menu Mobile
    var menuspeed = 400; // milliseconds for sliding menu animation time
    var $bdy = $('body');
    $('.open-menu').on('click', function(e) {
        if ($bdy.hasClass('show-menu')) {
            jsAnimateMenu('close');
        } else {
            jsAnimateMenu('open');
        }
    });
    $('.close-menu').on('click', function(e) {
        if ($bdy.hasClass('show-menu')) {
            jsAnimateMenu('close');
        } else {
            jsAnimateMenu('open');
        }
    });
    $('.overlay').on('click', function(e) {
        if ($bdy.hasClass('show-menu')) {
            jsAnimateMenu('close');
        }
    });

    function jsAnimateMenu(tog) {
        if (tog == 'open') {
            $bdy.addClass('show-menu');
        }
        if (tog == 'close') {
            $bdy.removeClass('show-menu');
        }
    }

    //Fancybox
    $(".fancybox").fancybox({
        openEffect: 'none',
        closeEffect: 'none'
    });
    /* Active Menu List */
    var pgurl = window.location.href.substr(window.location.href.lastIndexOf("/") + 1);
    jQuery(".main-navigation ul li a").each(function() {
        if (jQuery(this).attr("href") == pgurl || jQuery(this).attr("href") == '')
            jQuery(this).closest('.main-navigation > ul > li').addClass("active");
        if (jQuery(this).attr("href") == pgurl || jQuery(this).attr("href") == '')
            jQuery(this).parent().addClass("active");
    })

})();

function loadingScreen(x) {
    jQuery('.loading').show();
    setTimeout(function() { jQuery('.loading').hide(); }, x);
}

function loadingScreen() {
    jQuery('.loading').show();
}

function stopLoadingScreen() {
    jQuery('.loading').hide();
}

function addCommas(x) {
    var parts = x.toString().split(",");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(",");
}
//Qty
function increaseQty(priceStep) {
    var qtya = jQuery('.qty-input').val();
    qtya = qtya.split(',').join("");
    var qtyb = qtya * 1 + priceStep * 1;
    jQuery('.qty-input').val(addCommas(qtyb));
    price_set = addCommas(qtyb);
}

function decreaseQty(priceStep, minPrice) {
    var qtya = jQuery('.qty-input').val();
    qtya = qtya.split(',').join("");
    var qtyb = qtya * 1 - priceStep * 1;
    if (qtyb < minPrice) {
        qtyb = minPrice;
    }

    //jQuery('.qty-input').val(qtyb);
    // $(selector).autoNumeric('set', value)
    jQuery('.qty-input').val(addCommas(qtyb));
    price_set = addCommas(qtyb);
}
// Active Cart, Search
function toggleFilter(obj) {
    if (jQuery(obj).parent().find('.content-filter').hasClass('active')) {
        jQuery(obj).parent().find('.content-filter').removeClass('active');
    } else {
        jQuery('.content-filter').removeClass('active');
        jQuery(obj).parent().find('.content-filter').addClass('active');
    }
}
// Active Cart, Search
function closeModal(obj) {
    jQuery(obj).modal("toggle");
}

// Active Cart, Search
function openModal(obj) {
    jQuery(obj).modal("show");
}

function resetModal(e) {
    let id = jQuery(e).data("target");
    jQuery(id + " input, " + id + " textarea, " + id + " select").not(":input[type=submit], :input[type=button], :input[type=hidden], :input[type=radio], :input[id=recaptcha-token]").each(function(){
        this.value = "";
    });
}


function focusInput(id) {
    focusInputSecond(id, 1000);
}

function focusInputSecond(id, seconds) {
    setTimeout(function () {
        jQuery("#" + id).focus();
    }, seconds);
}

var event;
if(typeof(Event) === 'function') {
    event = new Event('change');
}else{
    event = document.createEvent('Event');
    event.initEvent('change', true, true);
}

function resetRegister() {
    jQuery('#tinhThanhpho').val('');
    document.getElementById('tinhThanhpho').dispatchEvent(event);
    jQuery('#tinhThanhPho').val('');
    document.getElementById('tinhThanhPho').dispatchEvent(event);
    jQuery(".personal-content-form").show();
    jQuery(".radio #radioPersonal").attr("checked", "checked");
    jQuery(".company-content-form").hide();
    jQuery('#personal-form').validate().resetForm();
    jQuery('#company-form').validate().resetForm();
}

function processRandom(th) {
    var $ = jQuery;

    var maxRandCount = 10;
    var maxFlashCount = 3000;

    for (var i = 0; i < th; i++) {
        $('#randomTH').append('<div class="eTH eTH-' + i + '"><div></div></div>')
    }

    setTimeout(function() {
        var randCount = 0;
        var randPrev = 0;
        var clrFlash;
        var flashCount = 0;
        var ir = setInterval(function() {
            // random
            var rand = Math.floor(Math.random() * th);
            if (randCount++ < maxRandCount) {
                $('.eTH-' + randPrev).css('background-color', '#eee');
                $('.eTH-' + rand).css('background-color', '#999');
                randPrev = rand;
                return;
            }
            // flash
            var ofs = 0;
            var el = $('.eTH-' + randPrev);
            clearInterval(ir);
            clrFlash = setInterval(function(){
                if (flashCount++ > maxFlashCount) {
                    clearInterval(clrFlash);
                    return;
                }
                $('.eTH-' + randPrev).css('background-color','rgba(200, 100, 100, ' + Math.abs(Math.sin(ofs)) + ')');
                ofs += 0.1;
            }, 10);
        },400);
    }, 1000);
}

// -------------------------
function resetGoogleCaptcha(e) {
    try {
        var formId = "#" + e + " ";

        jQuery(formId + ".g-recaptcha").each(function (i, e) {
            jQuery(e).html('');
        });

        jQuery(formId + ".g-recaptcha").each(function (i, e) {
            grecaptcha.render(
                jQuery(e).attr('id'),
                {
                    sitekey: jQuery(e).data('sitekey')
                });
        });
    } catch (e) {
        console.log(e);
    }
}

// -------------------------
// function for open Modal
// -------------------------
function openModalLogin(e) {
    cmdOpenModalLogin();
    resetModal(e);
    focusInputSecond('login_account',800)
}

function openModalRegister(e) {
    cmdOpenModalRegister();
    resetModal(e);
    resetRegister();
    focusInputSecond('person_name',600);
    setTimeout(function () {
        resetGoogleCaptcha("personal-form");
        resetGoogleCaptcha("company-form");
    }, 500);
}

// remove cookie
function logout() {
    document.cookie = '_JCA=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    document.cookie = '_JCP=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

// get param url
function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
}