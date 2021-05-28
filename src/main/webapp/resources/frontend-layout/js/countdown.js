var $ = jQuery;

function onloadCountdown() {
    $('[data-countdown="countdown"]').each(function(index, el) {
        var $this = $(this);
        $this.lofCountDown = undefined;
    });
    $('[data-countdown="countdown"]').each(function(index, el) {
        var $this = $(this);
        if ($this.data('date') != null) {

            var $date = dateFormatPattern($this.data('date'));
            var $num = $this.data('num');
            $this.lofCountDown({
                TargetDate: $date,
                DisplayFormat: "<div class=\"timeSale\"><p class=\"date\"><span>%%D%% </span>Ngày</p><p class=\"hour\"><span>%%H%% </span>Giờ</p><p class=\"min\"><span>%%M%% </span>Phút</p><p class=\"sec\"><span>%%S%%</span>Giây</p></div>",
                FinishMessage: "<div class=\"timeSale\"><p class=\"date\"><span><b>00</b></span>Ngày</p><p class=\"hour\"><span><b>00</b></span>Giờ</p><p class=\"min\"><span><b>00</b></span>Phút</p><p class=\"sec\"><span><b>00</b></span>Giây</p></div>",
                CountUp: $this.data("countup"),
                CallbackFinish: !!$this.data("callback")
            });
        }
    });
}

(function($) {
    $.fn.lofCountDown = function(options) {
        return this.each(function() {
            // get instance of the lofCountDown.
            new $.lofCountDown(this, options);
        });
    };
    $.lofCountDown = function(obj, options) {
        this.options = $.extend({
            autoStart: true,
            LeadingZero: true,
            DisplayFormat: "<p class=\"date\"><span>%%D%% </span><i>Ngày</i></p><p class=\"hour\"><span>%%H%% </span><i>Giờ</i></p><p class=\"min\"><span>%%M%% </span><i>Phút</i></p><p class=\"sec\"><span>%%S%%</span><i>Giây</i></p>",
            FinishMessage: "Expired",
            CountActive: true,
            TargetDate: null,
            TargetNum: null,
            CountUp: false,
            CallbackFinish: false
        }, options || {});
        if (this.options.TargetDate == null || this.options.TargetDate == '') {
            return;
        }
        this.timer = null;
        this.element = obj;
        this.CountStepper = options.CountUp ? 1 : -1;
        this.CountStepper = Math.ceil(this.CountStepper);
        this.SetTimeOutPeriod = (Math.abs(this.CountStepper) - 1) * 1000 + 990;
        this.CountBack(this.options.TargetDate, this);

    };
    $.lofCountDown.fn = $.lofCountDown.prototype;
    $.lofCountDown.fn.extend = $.lofCountDown.extend = $.extend;
    $.lofCountDown.fn.extend({
        calculateDate: function(secs, num1, num2) {
            var s = ((Math.floor(secs / num1)) % num2).toString();
            if (this.options.LeadingZero && s.length < 2) {
                s = "0" + s;
            }
            return "<b>" + s + "</b>";
        },
        CountBack: function(targetDate, self) {
            var dthen = new Date(targetDate);
            var dnow = new Date();
            if (this.CountStepper > 0) {
                ddiff = new Date(dnow - dthen);
            } else {
                ddiff = new Date(dthen - dnow);
            }
            var secs = Math.floor(ddiff.valueOf() / 1000);

            if (secs < 0) {
                self.element.innerHTML = '<div class="lof-labelexpired"> ' + self.options.FinishMessage + "</div>";
                return;
            }
            clearInterval(self.timer);
            DisplayStr = self.options.DisplayFormat.replace(/%%D%%/g, self.calculateDate(secs, 86400, 100000));
            DisplayStr = DisplayStr.replace(/%%H%%/g, self.calculateDate(secs, 3600, 24));
            DisplayStr = DisplayStr.replace(/%%M%%/g, self.calculateDate(secs, 60, 60));
            DisplayStr = DisplayStr.replace(/%%S%%/g, self.calculateDate(secs, 1, 60));
            self.element.innerHTML = DisplayStr;
            if (self.options.CountActive) {
                self.timer = null;
                self.timer = setTimeout(function() {
                    self.CountBack(targetDate, self);
                }, (self.SetTimeOutPeriod));
            }
        }

    });

    $(document).ready(function() {
        onloadCountdown();
    });

})(jQuery);

var countDownWinner;
function countDownTheWin(elementId) {
    if (countDownWinner) {
        clearInterval(countDownWinner);
        countDownWinner = null;
    }
    var e = jQuery('.' + elementId);
    var timeCountDown = e.data("countdown");
    var $date = new Date(dateFormatPattern(timeCountDown));
    countDownWinner = setInterval(function() {
        var distance = ($date.getTime() - new Date().getTime()) / 1000;
        var minutes = Math.floor(distance / 60) % 60;
        var seconds = Math.floor(distance) % 60;
        if (!e.length) {
            clearInterval(countDownWinner);
        }
        if (distance < 0) {
            e.html(
                (Math.abs(distance) / 1000 % 2 === 0 ? '<span style="color:white;">':'') +
                "00 : 00" +
                (Math.abs(distance) / 1000 % 2 === 0 ? '</span>':'')
            );
        } else {
            e.html(
                (minutes < 10 ? '0':'') + minutes + " : " + (seconds < 10 ? '0':'') + seconds
            );
        }
    }, 990);
}

function dateFormatPattern(e) {
    try {
        var $date = e.split("-");
        return $date[0] + "/" + $date[1] + "/" + $date[2] + " " + $date[3] + ":" + $date[4] + ":" + $date[5];
    } catch (e) {
        return '';
    }
}
