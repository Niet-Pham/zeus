$(document).ready(function () {
    let pathCurrent = location.pathname;
    let classActive = 'active-menuitem active';
    let closeable = false;
    let isFE = location.pathname.indexOf('frontend') > -1;

    let menuClass;
    if (isFE) {
        menuClass = '.primary-menu li';
    } else menuClass = '.layout-menu li';

    $(menuClass).each(function (i, e) {
        let $this = $(e);
        let pathElement = $this.find('a').attr('href');
        let parentElement = $this.parent('ul').parent('li');
        let isSub = parentElement.length > 0;

        if (pathCurrent.indexOf(pathElement) > -1) {
            // set active current
            if (!isSub || !isFE) {
                $this.addClass(classActive);
            }

            // set active menu parent
            if (!isFE) {
                $('#' + $this.attr('id').slice(0, -2)).addClass(classActive);
            } else {
                parentElement.addClass(classActive);
            }

            // set close in BE
            if (i === 0) closeable = true;
        } else {
            $this.removeClass(classActive);
            if (closeable && !isFE) {
                $this.find('ul').css('display', 'none');
            }
        }
    });
});

// ik0 - set value | ik1 - secret
function _inf_c_th(e, ik0, ik1) {
    let $ik0 = jQuery('#' + ik0);
    let $ik1 = jQuery('#' + ik1);
    $ik0.val(CryptoJS.AES.encrypt(e.value, $ik1.val()));
}

function showMessageCommon(type_warn, text_warn, title, message) {
    PrimeFaces.cw("Growl","growl", {
        id: "growl",
        sticky: false,
        life: 5000,
        escape: true,
        keepAlive: false,
        msgs: [{
            summary: title,
            detail: message,
            severity: type_warn,
            severityText: text_warn
        }]
    });
}

function showErrorMessage(title, message) {
    showMessageCommon('error', 'Error', title, message);
}

function showSuccessMessage(title, message) {
    showMessageCommon('info', 'Information', title, message);
}