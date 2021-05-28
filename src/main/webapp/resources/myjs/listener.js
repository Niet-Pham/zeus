var first_load = true;
// function socketConnect() {
//     if (window.WebSocket) {
//         var ws, data_, pingMsg;
//         var ws_prefix = "ws://";
//         if (location.protocol === 'https:') {
//             ws_prefix = "wss://";
//         }
//         var be = typeof beRequest === "undefined" ? "" : "?be";
//         var urlSocket = [ws_prefix, location.host, urlContext, "/connection", be];
//         ws = new WebSocket(urlSocket.join(""));
//         ws.onopen = function() {
//             console.log('Connected!');
//             pingMsg = setInterval(function() {
//                 ws.send("ping " + ((typeof assetDetail === 'undefined') ? '0' : assetDetail));
//             }, 1500);
//             if (first_load) {
//                 first_load = false
//             } else if (typeof cmdRefreshData === "function") {
//                 cmdRefreshData();
//             }
//         };
//         ws.onclose = function() {
//             console.log('Disconnected!');
//             clearInterval(pingMsg);
//             console.log("Trying to reconnect...");
//             setTimeout(function() {
//                 socketConnect();
//             }, 1000);
//         };
//         ws.onmessage =
//             function onMessage(event) {
//                 data_ = JSON.parse(event.data);
//                 if (data_.data != null) {
//                     try {
//                         data_.data = JSON.parse(data_.data);
//                     } catch (e) {
//                         console.log(e);
//                     }
//                 }
//                 console.log(window[data_.action]);
//                 if (window[data_.action]) {
//                     window[data_.action](data_);
//                 }
//             };
//     } else {
//
//     }
// }
// socketConnect();

function countDown(value, stringExpired) {
    stringExpired = "00:00:00";
    value *= 1000;
    var days = Math.floor(value / (1000 * 60 * 60 * 24));
    var hours = Math.floor((value % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    var minutes = Math.floor((value % (1000 * 60 * 60)) / (1000 * 60));
    var seconds = Math.floor((value % (1000 * 60)) / 1000);
    if (value < 0) {
        return stringExpired;
    } else {
        return (days <= 0 ? "" : (days < 10 ? '0':'') + days + " ngày, ") +
            (hours < 10 ? '0':'') + hours + ":" +
            (minutes < 10 ? '0':'') + minutes + ":" +
            (seconds < 10 ? '0':'') + seconds;
    }
}