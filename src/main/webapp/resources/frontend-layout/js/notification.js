$('#bell').click(function () {
    if (!$('#count-record').hasClass('text-notification-0')) {
        seenAllNoti();
    }
});

$('#navbarNotis').click(function () {
    if (!$('#count-record').hasClass('text-notification-0')) {
        seenAllNoti();
    }
});

function loadMoreNoti() {
    $("#bell_menu").scroll(function(e) {
        if ($("#bell_menu").hasClass("ended-scroll")) {
            return;
        }
        var ul = $(this).get(0);
        if(ul.scrollTop + ul.clientHeight >= ul.scrollHeight) {
            // ajax call get data from server and append to the div
            cmdLoadmoreNotification();
        }
    });
}

$(document).ready(function () {
    loadMoreNoti();
});