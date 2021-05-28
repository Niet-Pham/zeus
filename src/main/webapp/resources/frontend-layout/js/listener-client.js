function showDialog(data) {
    jQuery('#' + data.name + ' #dialog-content').html(data.data);
    jQuery('#' + data.name).modal('show');
}

function showMessage(data) {
    simpleNotify.notify({message: data.data, level: 'good'});
}

/*
 *
 *
 */
function showConfirm(data) {
    if (typeof assetDetail !== "undefined") {
        if (assetDetail != data.data.assetId) {
            hideAllPopup();
        }
        cmdRefreshDataStart();
    }
    updateScope(data);
    updateScopeModal(data);
    if (data.type === 'asset_started') {
        data.message = l_modal[data.type].message.replace('{0}', data.data.name);
        if ((typeof assetDetail === "undefined" && (typeof myAuction === "undefined" || !myAuction)) || (typeof assetDetail !== "undefined" && assetDetail != data.data.assetId)) {
            hideAllPopup();
            setTimeout(function() {
                assetStarted(data);
            }, 500);
        }
    }
}

function assetStarted(data) {
    bootbox.confirm({
        title: l_modal[data.type].title,
        message: data.message,
        className: "confirmAuction modal-login",
        swapButtonOrder: true,
        buttons: {
            confirm: {
                label: l_modal[data.type].button,
                className: 'btn btn-custom main-color'
            },
            cancel: {
                label: 'Đóng',
                className: 'btn btn-default btn-custom reset hide'
            },
        },
        callback: function (result) {
            if (result) {
                location.href = urlContext + "/frontend/asset/asset_detail.xhtml?id=" + data.data.assetId;
            }
        }
    });
}

function auctionCancelConfirm() {
    bootbox.confirm({
        title: 'Xác nhận từ chối tham gia đấu giá',
        message: 'Bạn có chắc chắn muốn từ chối tham gia đấu giá không?<br/><br/><b style="color:red">Chú ý:</b> nếu nhấn vào Xác nhận, bạn sẽ không được hoàn lại tiền đặt trước.',
        className: "confirmAuction modal-login",
        swapButtonOrder: true,
        buttons: {
            confirm: {
                label: 'Xác nhận',
                className: 'btn btn-custom main-color'
            },
            cancel: {
                label: 'Huỷ bỏ',
                className: 'btn btn-default btn-custom reset'
            },
        },
        callback: function (result) {
            if (result) {
                jQuery('#cancelSubmit').click();
            }
        }
    });
}

function updateScopeModal(data) {
    if (typeof cmdRefreshIncludeModal === "function") {
        cmdRefreshIncludeModal();
    }
}

function updateScope(data) {

    jQuery('.modal.hide-at-update').modal('hide');

    if (typeof myAuction !== "undefined" || typeof assetDetail !== "undefined") {
        cmdRefreshData([{name: 'id', value: getUrlParameter('id')}]);
    }
}

function updateEnded(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        showPopupCancelAsset(data);
    } else {
        updateScope(data);
    }
}

function showPopupCancelAsset(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();
        updateScope(data);
        cmdRefreshCancelAsset();
    } else {
        updateScope(data);
    }
}

function showPopupChangeRound(data) {
    hideAllPopup();
    updateScope(data);
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        cmdRefreshChangeRound();
    }
}

function showPopupAuctionLost(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();
        jQuery('#modalAuctionLost').modal('show');
    }
}

function showModalAuctionWinnerFinish(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();
        cmdRefreshWinnerFinish();
    }
}

function showModalAuctionRandom(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();
        cmdRefreshRandom();
    }
}

function showModalAuctionWinner(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();
        cmdRefreshWinner();
    }
}

function showModalAuctionOpportunityD0(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();
        cmdRefreshOpportunityD0();
    }
}

function showModalAuctionOpportunityD1(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();
        jQuery('#modalAuctionOpportunityD1').modal('show');
    }
}

function showModalAuctionOpportunity1(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();
        cmdRefreshOpportunity1();
    }
}

function showModalAuctionOpportunity2(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();
        cmdRefreshOpportunity2();
    }
}

function showModalDeposit(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        hideAllPopup();

        updateScope(data);
        if (typeof myAuction !== "undefined") {
            return;
        }
        cmdRefreshDeposit();
    }
}

function showModalChangeRound(data) {
    if (typeof assetDetail !== "undefined" && assetDetail == data.data.assetId) {
        cmdChangeRound();
    }
}

function isJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}

function getParam(param) {
    var url = new URL(location.href);
    return url.searchParams.get(param);
}

function hideAllPopup() {
    bootbox.hideAll();
    jQuery('.modal').modal('hide');
}
