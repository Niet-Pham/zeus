var price_set;
function onInputMoney(e) {
    //only input number
    e.value = e.value.replace(/[^0-9.]/g, '').replace(/(,.*)\./g, '$1');
    //split by dot
    e.value = e.value.replace(/,/g, '').replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
    price_set = e.value;
}

function onBargainConfirm(e, currentPrice, priceStep, isInput) {
    // let form = jQuery('#confirmPopup');
    var price;

    var bargain_text = "trả";
    if (isInput) {
        price = jQuery('#' + e).val();
    } else {
        bargain_text = "chấp nhận mức";
        price = jQuery('#' + e).text();
    }

    var realPriceBargain = price.replace(/[^0-9]+/g, '') * 1;
    priceStep *= 1;
    currentPrice *= 1;

    if (price != '') {

        if (isInput) {

            // reset if priceStep wrong
            if ((realPriceBargain - currentPrice) % priceStep !== 0) {
                jQuery("#" + e).val(currentPrice);
                onInputMoney(document.getElementById(e));

                bootbox.alert({
                    message: "Số tiền đấu giá sai bước giá",
                    size: 'small',
                    className: 'error-status modal-login'
                });
                return;
            }

            // reset if priceStep wrong
            if (realPriceBargain < currentPrice) {
                jQuery("#" + e).val(currentPrice);
                onInputMoney(document.getElementById(e));

                bootbox.alert({
                    message: "Số tiền đấu giá phải lớn hơn giá hiện tại",
                    size: 'small',
                    className: 'error-status modal-login'
                });
                return;
            }
        }

        bootbox.confirm({
            title: "Xác nhận trả giá",
            //<![CDATA[
            message: "Bạn có chắc chắn muốn " + bargain_text + " giá <span>" + (isInput ? price + " VNĐ" : price) + " </span> cho tài sản này?",
            //]]>
            className: "confirmStore modal-login",
            swapButtonOrder: true,
            buttons: {
                confirm: {
                    label: isInput ? 'Trả giá' : 'Chấp nhận',
                    className: 'btn btn-custom main-color'
                },
                cancel: {
                    label: 'Huỷ bỏ',
                    className: 'btn btn-default btn-custom reset '
                },
            },
            callback: function (result) {
                if (result) {
                    if (isInput) {
                        jQuery("#bargainMoney").val(realPriceBargain);
                        jQuery("#bargainSubmit").click();
                        // reset if priceStep wrong
                        if ((realPriceBargain - currentPrice) % priceStep !== 0) {
                            jQuery("#" + e).val(currentPrice);
                            onInputMoney(document.getElementById(e));
                        }
                    } else {
                        jQuery("#bargainSubmitAccept").click();
                    }
                }
            }
        });
    } else {
        bootbox.alert({
            message: "Bạn vui lòng nhập số tiền mà bạn muốn trả cho tài sản",
            size: 'small',
            className: 'error-status modal-login'
        });
    }
}

function onAcceptWinner() {
    bootbox.confirm({
        title: "Xác nhận chấp nhận kết quả",
        //<![CDATA[
        message: "Bạn có chắc chắn muốn chấp nhận kết quả thắng cuộc không?",
        //]]>
        className: "confirmStore modal-login",
        swapButtonOrder: true,
        buttons: {
            confirm: {
                label: 'CHẤP NHẬN THẮNG CUỘC',
                className: 'btn btn-custom main-color'
            },
            cancel: {
                label: 'Đóng',
                className: 'btn btn-default btn-custom reset hide'
            }
        },
        callback: function (result) {
            if (result) {
                cmdAcceptWinner();
            }
        }
    });
}

function onRefuseWinner(directDown) {
    if (typeof directDown === "undefined") {
        directDown = false;
    }

    warning_text = "";
    if (directDown) {
        warning_text = "Sau khi từ chối bạn sẽ bị truất quyền tham gia cuộc đấu giá này. ";
    }
    bootbox.confirm({
        title: "Xác nhận từ chối kết quả",
        //<![CDATA[
        message: warning_text + "Bạn có chắc chắn muốn từ chối kết quả không?",
        //]]>
        className: "confirmStore modal-login",
        swapButtonOrder: true,
        buttons: {
            confirm: {
                label: 'TỪ CHỐI KẾT QUẢ',
                className: 'btn btn-danger'
            },
            cancel: {
                label: 'Đóng',
                className: 'btn btn-default btn-custom reset hide'
            }
        },
        callback: function (result) {
            if (result) {
                cmdRefuseWinner();
                if (directDown) {
                    setTimeout(function() {
                        cmdRefreshDeposit();
                    }, 2000);
                }
            }
        }
    });
}

function onRetractPrice() {
    bootbox.confirm({
        title: "Xác nhận rút lại giá",
        //<![CDATA[
        message: "Nếu rút lại giá đã trả bạn sẽ bị truất quyền tham gia đấu giá và không được trả lại tiền đặt trước. Bạn có chắc chắn muốn rút lại giá đã trả không?",
        //]]>
        className: "confirmStore modal-login",
        swapButtonOrder: true,
        buttons: {
            confirm: {
                label: 'RÚT LẠI GIÁ',
                className: 'btn btn-danger'
            },
            cancel: {
                label: 'HỦY BỎ',
                className: 'btn btn-default btn-custom reset'
            }
        },
        callback: function (result) {
            if (result) {
                jQuery("#retractPriceSubmit").click();
            }
        }
    });
}

function storeRefuseWinner1th() {
    bootbox.confirm({
        title: "Thông báo",
        //<![CDATA[
        message: "Bạn đang thực hiện từ chối trúng đấu giá, bạn có chắc chắn từ chối hay không?<br/><br/><b style='color:red'>Chú ý:</b> nếu nhấn vào Đồng ý, bạn sẽ không được hoàn lại tiền đặt trước.",
        //]]>
        className: "confirmStore modal-login",
        swapButtonOrder: true,
        buttons: {
            confirm: {
                label: 'Đồng ý',
                className: 'btn btn-custom main-color'
            },
            cancel: {
                label: 'Thoát',
                className: 'btn btn-default btn-custom reset'
            }
        },
        callback: function (result) {
            if (result) {
                cmdRefuseWinner();
                jQuery("#modalStoreWinner").modal("hide");
                jQuery("#modalStoreRefuseWinner").modal("show");
            }
        }
    });
}