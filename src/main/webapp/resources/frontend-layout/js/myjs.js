$(document).ready(function() {
    var path = window.location.pathname;
    if (path === '[del]/k2_tech_store_war_exploded/frontend/index.xhtml') {
        setTimeout(loginModal, 3000);
    }
});
function loginModal() {
    $('#login-modal').modal('show');
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
}