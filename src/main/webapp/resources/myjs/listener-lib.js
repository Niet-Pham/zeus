function showDialog(data) {
    $('#' + data.name + ' #dialog-content').html(data.data);
    PF(data.name).show();
}

function showMessage(data) {
    PF(data.name).renderMessage({
        summary: data.title,
        detail: data.data,
        severity: 'info'
    });
}

function updateScope(data) {
    cmdRefreshData();
}

function showConfirm(data) {
    updateScope(data);
}

function showPopupChangeRound(data) {
    updateScope(data);
}

function showPopupCancelAsset(data) {
    updateScope(data);
}

// Trang điều hành -> Màn hình điều hành
function updateStoreRegDataTable(data) {
    jQuery('#updateStoreRegDataTable').click();
}

// Trang tỉnh
function updateScreen(data) {
    jQuery('#updateScreen').click();
}

// Trang huyện
function updateScreenDistrict(data) {
    jQuery('#updateScreenDistrict').click();
}

// Trang xã
function updateScreenCommune(data) {
    jQuery('#updateScreenCommune').click();
}