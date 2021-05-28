$.validator.setDefaults({
    submitHandler: function(e) {
    }
});

$(document).ready(function () {
    validateForm();
});

var validatePass;
function validateForm() {
    validatePass = true;

    $.validator.addMethod('customphone', function (value, element) {
        return this.optional(element) || /([0]{1}[1-9]{1}[0-9]{8,9})/.test(value);
    }, "Bạn vui lòng nhập đúng định dạng số điện thoại");
    $.validator.addMethod('validateCMND', function (value, element) {
        return this.optional(element) || /(^[0-9]{9}(?:[0-9]{3})?$)/.test(value);
    }, "Bạn vui lòng nhập đúng định dạng số CMND/CCCD");
    $.validator.addMethod('validateUserName', function (value, element) {
        return this.optional(element) || /(^[\w\.]{6,50}$)/.test(value);
    }, "Tên đăng nhập phải bao gồm 6 kí tự");
    $.validator.addMethod('validatePassword', function (value, element) {
        return this.optional(element) || /(^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{12,}$)/.test(value);
    }, "Mật khẩu phải có tối thiểu 12 ký tự, mật khẩu phải bao gồm chữ cái, chữ số và ký tự đặc biệt");
    $("#login-form").validate({
        rules: {
            login_account: {
                required: true,
                minlength: 5
            },
            login_password: {
                required: true,
                minlength: 6
            },
        },
        messages: {
            login_account: {
                required: "Vui lòng điền tên đăng nhập",
                minlength: "Tên đăng nhập phải ít nhất 5 ký tự"
            },
            login_password: {
                required: "Vui lòng điền mật khẩu",
                minlength: "Mật khẩu ít nhất 6 ký tự"
            },
        },
        errorElement: "em",
        errorPlacement: function(error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");

            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                $(element).nextAll().remove();
                error.insertAfter(element);
            }
            validatePass = false;
        },
        highlight: function(element, errorClass, validClass) {
            $(element).parents(".form-group").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function(element, errorClass, validClass) {
            $(element).parents(".form-group").addClass("has-success").removeClass("has-error");
        },
        submitHandler: function(form) {
            $("#onlogin").click();
        }
    });
    $("#lostPass").validate({
        rules: {
            lostpass_email: {
                required: true,
                email: true,
            },
        },
        messages: {
            lostpass_email: {
                email: "Bạn vui lòng nhập đúng định dạng email",
                required: "Bạn vui lòng nhập email đã đăng ký",
            },
        },
        errorElement: "em",
        errorPlacement: function(error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");

            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                $(element).nextAll().filter('em').remove();
                error.insertAfter(element);
            }
            validatePass = false;
        },
        highlight: function(element, errorClass, validClass) {
            $(element).parents(".form-group").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function(element, errorClass, validClass) {
            $(element).parents(".form-group").addClass("has-success").removeClass("has-error");
        },
        submitHandler: function(form) {
            $("#getCode").click();
        }
    });
    $("#auction_form_register").validate({
        rules: {
            asset_description: {
                required: true,
            },
            asset_name: {
                required: true,
            },
        },
        messages: {
            asset_description: {
                required: "Bạn vui lòng nhập mô tả của tài sản",
            },
            asset_name: {
                required: "Bạn vui lòng nhập tên của tài sản",
            },
        },
        errorElement: "em",
        errorPlacement: function(error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");

            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                $(element).nextAll().remove();
                error.insertAfter(element);
            }
            validatePass = false;
        },
        highlight: function(element, errorClass, validClass) {
            $(element).parents(".col-md-10").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function(element, errorClass, validClass) {
            $(element).parent().nextAll().filter('em').remove();
            $(element).parents(".col-md-10").addClass("has-success").removeClass("has-error");
        },
        submitHandler: function(form) {
            $("#saveAuctionReq").click();
        }
    });
    $("#company-form").validate({
        rules: {
            tinhThanhPho: {
                required: true,
            },
            quanHuyen: {
                required: true,
            },
            phuongXa: {
                required: true,
            },
            c_name: {
                required: true,
            },
            c_user_name: {
                required: true,
                'validateUserName': true,
            },
            c_address: {
                required: true,
            },
            c_phone_number: {
                required: true,
                'customphone': true,
            },
            c_person: {
                required: true,
            },
            c_per_phone_number: {
                required: true,
                'customphone': true,
            },
            c_email: {
                required: true,
            },
            c_password: {
                required: true,
                'validatePassword': true,
            },
            c_re_password: {
                required: true,
                'validatePassword': true,
                equalTo: "#c_password"
            },
        },
        messages: {
            c_name: {
                required: "Bạn vui lòng nhập tên tổ chức",
            },
            c_user_name: {
                required: "Bạn vui lòng nhập tên đăng nhập",
            },
            c_address: {
                required: "Bạn vui lòng nhập địa chỉ tổ chức",
            },
            c_phone_number: {
                required: "Bạn vui lòng nhập số điện thoại tổ chức",
            },
            c_person: {
                required: "Bạn vui lòng nhập người đại diện tổ chức",
            },
            c_per_phone_number: {
                required: "Bạn vui lòng nhập số điện thoại người đại diện tổ chức",
            },
            c_email: {
                required: "Bạn vui lòng nhập email người đại diện tổ chức",
                email: "Bạn vui lòng điền đúng định dạng email"
            },
            c_password: {
                required: "Bạn vui lòng nhập mật khẩu",
            },
            c_re_password: {
                required: "Bạn vui lòng nhập lại mật khẩu ",
                equalTo: "Mật khẩu nhập lại không trùng khớp"
            },
            tinhThanhPho: {
                required: "Bạn vui lòng chọn tỉnh/thành phố",
            },
            quanHuyen: {
                required: "Bạn vui lòng chọn quận/huyện",
            },
            phuongXa: {
                required: "Bạn vui lòng chọn phường/xã",
            },

        },
        errorElement: "em",
        errorPlacement: function(error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");

            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                if($(element).attr('id') == 'tinhThanhPho' || $(element).attr('id') == 'quanHuyen' || $(element).attr('id') == 'phuongXa') {
                    $(element).nextAll().filter('em').remove();
                    error.insertAfter($(element).next());
                }
                    else
                    {
                        $(element).nextAll().filter('em').remove();
                        error.insertAfter(element);
                    }
            }
            validatePass = false;
        },
        highlight: function(element, errorClass, validClass) {
            $(element).parents(".col-md-8").addClass("has-error").removeClass("has-success");
            $(element).parents(".col-md-4").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function(element, errorClass, validClass) {
            $(element).parent().nextAll().filter('em').remove();
            $(element).nextAll().filter('em').remove();
            $(element).parents(".col-md-8").addClass("has-success").removeClass("has-error");
            $(element).parents(".col-md-4").addClass("has-success").removeClass("has-error");
        },
        submitHandler: function(form) {
            $("#registerCompany").click();
        }
    });
    $("#personal-form").validate({
        rules: {
            person_name: {
                required: true,
            },
            person_user_name: {
                required : true,
                'validateUserName': true,
            },
            person_phone_number: {
                required: true,
                'customphone': true,
            },
            person_email: {
                required: true,
                email: true
            },
            tinhThanhpho:{
                required: true,
            },
            quanhuyen: {
                required: true,
            },
            phuongxa: {
                required: true,
            },
            person_password: {
                required: true,
                'validatePassword': true,
            },
            person_re_password: {
                required: true,
                'validatePassword': true,
                equalTo: "#person_password"
            },
            address: {
                required: true,
            },
        },
        messages: {
            person_name: {
                required: "Bạn vui lòng nhập họ và tên",
            },
            person_user_name: {
                required: "Bạn vui lòng nhập tên đăng nhập",
            },
            person_phone_number: {
                required: "Bạn vui lòng nhập số điện thoại",
            },
            person_password: {
                required: "Bạn vui lòng nhập mật khẩu",
            },
            person_re_password: {
                required: "Bạn vui lòng nhập lại mật khẩu",
                equalTo: "Mật khẩu nhập lại không trùng khớp"
            },
            person_email: {
                required: "Bạn vui lòng nhập email",
                email: "Bạn vui lòng nhập đúng định dạng email",
            },
            tinhThanhpho:{
                required: "Bạn vui lòng chọn tỉnh/thành phố",
            },
            quanhuyen: {
                required: "Bạn vui lòng chọn quận/huyên",
            },
            phuongxa: {
                required: "Bạn vui lòng chọn phường/xã",
            },
            address: {
                required: "Bạn vui lòng nhập địa chỉ nhà",
            },
        },
        errorElement: "em",
        errorPlacement: function(error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");



            if (element.attr('id')== 'person_noicap') {
                error.insertAfter($(element).next());
            } else {
                if($(element).attr('id') == 'tinhThanhpho' || $(element).attr('id') == 'quanhuyen' || $(element).attr('id') == 'phuongxa'){
                    $(element).nextAll().filter('em').remove();
                    error.insertAfter($(element).next());
                }else{
                    $(element).nextAll().filter('em').remove();
                    error.insertAfter(element);
                }
            }
            validatePass = false;
        },
        highlight: function(element, errorClass, validClass) {

            $(element).parents(".col-md-8").addClass("has-error").removeClass("has-success");
            $(element).parents(".col-md-4").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function(element, errorClass, validClass) {

            $(element).parents().nextAll().filter('em').remove();
            $(element).nextAll().filter('em').remove();
            $(element).parents(".col-md-8").addClass("has-success").removeClass("has-error");
            $(element).parents(".col-md-4").addClass("has-success").removeClass("has-error");

        },
        submitHandler: function(form) {
            $("#registerPerson").click();
        }
    });
    $("#personal-account").validate({
        rules: {
            person_name: {
                required: true,
            },
            person_full_name:{
                required: true,
                'validateUserName': true,
            },
            person_address: {
                required: true,
            },
            tinhThanhPho: {
                required: true,
            },
            quanHuyen: {
                required: true,
            },
            phuongXa: {
                required: true,
            },
            sex:{
                required: true,
            },
            person_phone_number: {
                required: true,
                'customphone': true,
            },
            person_email: {
                required: true,
                email: true
            },
            person_cmnd: {
                required: true,
                'validateCMND': true,
            },
            person_noicap: {
                required: true,
            },
            person_ngaycap:{
                required : true,
            },
            person_address_now: {
                required: true,
            },
            address: {
                required: true,
            },
            person_ngaysinh: {
                required: true,
            },

            marriage_name: {
                required: {
                    depends: function(element) {
                        return $("#married_box:checked");
                    }
                },
            },
            marriage_id: {
                required: {
                    depends: function(element) {
                        return $("#married_box:checked");
                    }
                },
                'validateCMND': true,
            },

        },
        messages: {
            person_name: {
                required: "Bạn vui lòng nhập họ và tên",
            },
            person_address: {
                required: "Bạn vui lòng nhập địa chỉ",
            },
            person_phone_number: {
                required: "Bạn vui lòng nhập số điện thoại",
            },
            person_email: {
                required: "Bạn vui lòng nhập email",
                email: "Bạn vui lòng nhập đúng định dạng email",
            },
            person_cmnd: {
                required: "Bạn vui lòng nhập số CMND/CCCD",
            },
            person_noicap: {
                required: "Bạn vui lòng nhập nơi cấp",
            },
            marriage_name: {
                required: "Bạn vui lòng nhập họ tên vợ/chồng",
            },
            marriage_id: {
                required: "Bạn vui lòng nhập số CMND/CCCD vợ/chồng",
            },
            person_ngaycap:{
                required : "Bạn vui lòng nhập ngày cấp",
            },
            person_address_now: {
                required: "Bạn vui lòng nhập hộ khẩu thường trú",
            },
            tinhThanhPho: {
                required: "Bạn vui lòng chọn tỉnh/thành phố",
            },
            quanHuyen: {
                required: "Bạn vui lòng chọn quận/huyện",
            },
            phuongXa: {
                required: "Bạn vui lòng chọn phường/xã",
            },
            person_ngaysinh: {
                required: "Bạn vui lòng nhập ngày sinh"
            },
            sex:{
                required: "Bạn vui lòng chọn giới tính",
            },
            address:{
                required: "Bạn vui lòng nhập địa chỉ nhà",
            },
        },
        errorElement: "em",
        errorPlacement: function(error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");

            if($("#married").val() == "true"){
                $(element).nextAll().filter('em').remove();
                error.insertAfter(element);
            }
            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                if (element.hasClass('datetimepicker') || element.hasClass('datetimepicker1')) {
                    $(element).parent().nextAll().filter('em').remove();
                    error.insertAfter(element.parent());
                } else {
                    $(element).nextAll().filter('em').remove();
                    error.insertAfter(element);
                }
                if($(element).attr('id') == 'tinhThanhPho' || $(element).attr('id') == 'quanHuyen' || $(element).attr('id') == 'phuongXa' || $(element).attr('id') == 'person_noicap') {
                    $(element).nextAll().filter('em').remove();
                    error.insertAfter($(element).next());
                }
            }
            validatePass = false;
        },
        highlight: function(element, errorClass, validClass) {
            $(element).parents(".col-md-8").addClass("has-error").removeClass("has-success");
            $(element).parents(".col-md-4").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function(element, errorClass, validClass) {
            $(element).parent().nextAll().filter('em').remove();
            $(element).nextAll().filter('em').remove();
            $(element).parents(".col-md-8").addClass("has-success").removeClass("has-error");
            $(element).parents(".col-md-4").addClass("has-success").removeClass("has-error");
        },
        invalidHandler: function(form, validator) {
            if (validator.numberOfInvalids()) {
                $('#btnUpdate').button('reset');
            }
        },
        submitHandler: function(form) {
            $("#updatePerson").click();
            $('#btnUpdate').button('reset');
        }
    });
    $("#company-account").validate({
        rules: {
            company_name: {
                required: true,
            },
            company_DKKD:{
                required: true,
            },
            company_address: {
                required: true,
            },
            name_company: {
                required: true,
            },
            company_phone_number: {
                required: true,
                'customphone': true,
            },
            phone_number:{
                required: true,
                'customphone': true,
            },
            company_position:{
                required: true,
            },
            company_ngaysinh:{
                required: true,
            },
            company_ngaycap:{
                required: true,
            },
            company_email: {
                required: true,
                email: true
            },
            company_cmnd: {
                required: true,
                'validateCMND': true,
            },
            company_noicap: {
                required: true,
            },
            company_address_now: {
                required: true,
            },
            tinhThanhPho: {
                required: true,
            },
            quanHuyen: {
                required: true,
            },
            phuongXa: {
                required: true,
            },
            marriage_name: {
                required: {
                    depends: function(element) {
                        return $("#married_box:checked");
                    }
                }
            },
            marriage_id: {
                required: {
                    depends: function(element) {
                        return $("#married_box:checked");
                    }
                }
            },
            c_sex: {
                required: true,
            },
            address: {
                required: true,
            },
        },
        messages: {
            company_name: {
                required: "Bạn vui lòng nhập tên tổ chức",
            },
            company_DKKD:{
                required: "Bạn vui lòng nhập số đăng ký kinh doanh",
            },
            name_company: {
                required: "Bạn vui lòng nhập tên người đại diện",
            },
            company_position: {
                required: "Bạn vui lòng nhập chức vụ người đại diện",
            },
            company_address: {
                required: "Bạn vui lòng nhập địa chỉ",
            },
            company_phone_number: {
                required: "Bạn vui lòng nhập số điện thoại tổ chức",
            },
            phone_number: {
                required: "Bạn vui lòng nhập số điện thoại",
            },
            company_email: {
                required: "Bạn vui lòng nhập email",
                email: "Bạn vui lòng nhập đúng định dạng email",
            },
            company_ngaycap:{
                required: "Bạn vui lòng nhập ngày cấp",
            },
            company_cmnd: {
                required: "Bạn vui lòng nhập số CMND/CCCD",
            },
            company_noicap: {
                required: "Bạn vui lòng chọn nơi cấp",
            },
            marriage_name: {
                required: "Bạn vui lòng nhập họ tên vợ/chồng",
            },
            marriage_id: {
                required: "Bạn vui lòng nhập số CMND/CCCD vợ/chồng",
            },
            company_address_now: {
                required: "Bạn vui lòng nhập hộ khẩu thường trú",
            },
            tinhThanhPho: {
                required: "Bạn vui lòng chọn tỉnh/thành phố",
            },
            quanHuyen: {
                required: "Bạn vui lòng chọn quận/huyện",
            },
            phuongXa: {
                required: "Bạn vui lòng chọn phường/xã",
            },
            c_sex: {
                required: "Bạn vui lòng chọn giới tính",
            },
            address:{
                required: "Bạn vui lòng nhập địa chỉ tổ chức",
            },
        },
        errorElement: "em",
        errorPlacement: function(error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");


                if (element.hasClass('datetimepicker') || element.hasClass('datetimepicker1')) {
                    $(element).parent().nextAll().filter('em').remove();
                    error.insertAfter(element.parent());
                } else {
                    $(element).nextAll().filter('em').remove();
                    error.insertAfter(element);
                }
                if($(element).attr('id') == 'tinhThanhPho' || $(element).attr('id') == 'quanHuyen' || $(element).attr('id') == 'phuongXa') {
                    $(element).nextAll().filter('em').remove();
                    error.insertAfter($(element).next());
                }

            validatePass = false;
        },
        highlight: function(element, errorClass, validClass) {
            $(element).parents(".col-md-8").addClass("has-error").removeClass("has-success");
            $(element).parents(".col-md-4").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function(element, errorClass, validClass) {
            $(element).nextAll().filter('em').remove();
            $(element).parents(".col-md-8").addClass("has-success").removeClass("has-error");
            $(element).parents(".col-md-4").addClass("has-success").removeClass("has-error");
        },
        invalidHandler: function(form, validator) {
            if (validator.numberOfInvalids()) {
                $('#btnUpdate').button('reset');
            }
        },
        submitHandler: function(form) {
            $("#updateCompany").click();
            $('#btnUpdate').button('reset');
        }
    });
}

var timerLostPassword;

function countdownCheckMail() {
    $('#code').show();
    $('#lostpass_email').show();
    $('#inputCode').show();
    $('#countdownEx').show();
    $('#checkCode').show();
    $('#checkEmail').hide();
    $('#endTime').hide();
    $('#lostpass_email').attr('readonly','readonly');
    var count = 60;
    timerLostPassword = setInterval(function () {
        count--;
        $('#countdownEx').html('Mã xác nhận có hiệu lực trong ' + count + ' giây');
        if (count == 0) {
            clearInterval(timerLostPassword);
            $('#checkEmail').val('Gửi lại mã xác nhận');
            $('#checkEmail').show();
            $('#endTime').show();
            $('#countdownEx').hide();
            $('#checkCode').hide();
            $('#removeCode').click();
        }
    }, 1000);
}