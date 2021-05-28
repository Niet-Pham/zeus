function SignFileCallBack(rv) { //ky o view
    if (rv != null) {
        var received_msg = JSON.parse(rv);
        if (received_msg != null && received_msg.Status == 0) {
            var fileName = received_msg.FileName;
            var fileId = fileName.toLowerCase().substring(0, fileName.toLowerCase().lastIndexOf("."));
            console.log("fileId: " + fileId + "received_msg " + received_msg.Status);

            var param = {
                'attachId': fileId
            };
            var wgt = zk.Widget.$("$docView");
            zAu.send(new zk.Event(wgt, 'onUpdateStatusVGACA', {
                '': param
            }));
            alert("Ký thành công!");
        } else {
            if (rv == null) {
                alert("Ký thất bại! ");
            } else {
                alert("Lỗi trong quá trình ký: [" + received_msg.Status + "] - " + received_msg.Message);
            }
        }
    }

}
function SignFileCallBackPublic(rv) {  
    if (rv != null) {
        var received_msg = JSON.parse(rv);
        if (received_msg != null && received_msg.Status == 0) {
            var fileName = received_msg.FileName;
            var fileId = fileName.toLowerCase().substring(0, fileName.toLowerCase().lastIndexOf("."));
            console.log("fileId: " + fileId + "received_msg " + received_msg.Status);

            var param = {
                'attachId': fileId
            };
            var wgt = zk.Widget.$("$docPublish");
            zAu.send(new zk.Event(wgt, 'onUpdateStatusVGACA', {
                '': param
            }));
            alert("Ký thành công!");
        } else {
             var wgt = zk.Widget.$("$docPublish");
            zAu.send(new zk.Event(wgt, 'onFailVGACA', {
            }));
            if (rv == null) {
                alert("Ký thất bại! ");
            } else {
                alert("Lỗi trong quá trình ký: [" + received_msg.Status + "] - " + received_msg.Message);
            }
        }
    }

}
function SignFileCallBackDialog(rv) {  //ky o cap so ban hanh
    if (rv != null) {
        var received_msg = JSON.parse(rv);
        if (received_msg != null && received_msg.Status == 0) {
            var fileName = received_msg.FileName;
            var fileId = fileName.toLowerCase().substring(0, fileName.toLowerCase().lastIndexOf("."));
            console.log("fileId: " + fileId + "received_msg " + received_msg.Status);

            var param = {
                'attachId': fileId
            };
            var wgt = zk.Widget.$("$docPublish");
            zAu.send(new zk.Event(wgt, 'onUpdateStatusVGACA', {
                '': param
            }));
            alert("Ký thành công!");
        } else {
            
            var wgt = zk.Widget.$("$docPublish");
            zAu.send(new zk.Event(wgt, 'onFailVGACA', {
            }));
            
            
            if (rv == null) {
                alert("Ký thất bại! ");
            } else {
                alert("Lỗi trong quá trình ký: [" + received_msg.Status + "] - " + received_msg.Message);
            }
        }
    }

}

function exc_sign_approved(sessionid, filename, metadata) {
    var prms = {};
    var urlFull = window.location.href;
    var str = urlFull.split('index.zul');
    console.log("" + sessionid + "filename" + filename + " str " + str[0]);

    prms["FileUploadHandler"] = str[0] + '/vgaUploadFile.do';
    prms["SessionId"] = sessionid;
    prms["FileName"] = str[0] + filename;
    prms["DocNumber"] = "123";
    prms["IssuedDate"] = "2019-03-12T12:00:00+07:00";

    var json_prms = JSON.stringify(prms);
    vgca_sign_approved(json_prms, SignFileCallBack);

}

function exc_sign_issued(sessionid, filename, bookNumber, dateStr) {
    var prms = {};
    var urlFull = window.location.href;
    var str = urlFull.split('index.zul');
    console.log("" + sessionid + "filename" + filename + " str " + str[0]);

    prms["FileUploadHandler"] = str[0] + '/vgaUploadFile.do'; //document.location.origin
    prms["SessionId"] = sessionid;
    prms["FileName"] = str[0] + filename;
    prms["DocNumber"] = bookNumber;
    prms["IssuedDate"] = dateStr;

    var json_prms = JSON.stringify(prms);
    vgca_sign_issued(json_prms, SignFileCallBackDialog);

}

//metadata có kiểu List<KeyValue> 
//KeyValue là class { string Key; string Value; }
function exc_sign_file(sessionid, filename, metadata) {
    var prms = {};

    var urlFull = window.location.href;
    var str = urlFull.split('index.zul');
    console.log("" + sessionid + "filename" + filename + " str " + str[0]);
    //prms["FileUploadHandler"] = 'http://localhost:8081/vgcaUploadFile.do';
    prms["FileUploadHandler"] = str[0] + '/vgaUploadFile.do';
    prms["SessionId"] = sessionid;
    prms["FileName"] = str[0] + filename;
    prms["MetaData"] = metadata;

    var json_prms = JSON.stringify(prms);
    vgca_sign_file(json_prms, SignFileCallBack);
}
    