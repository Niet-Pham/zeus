// A $( document ).ready() block.
$( document ).ready(function() {
    updateUiSelectonemenuLayout();
});

function updateUiSelectonemenuLayout() {
    $("div.ui-selectonemenu.full-width").each(
        function () {
            $(this).css("width", $(this).parent().width());
        }
    );
};