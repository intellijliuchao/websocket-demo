function initDateComponent(options) {

    var defaultLocale = {
        "format": "YYYY-MM-DD",
        "separator": " - ",
        "applyLabel": "确定",
        "cancelLabel": "取消",
        "fromLabel": "开始",
        "toLabel": "结束",
        "customRangeLabel": "Custom",
        "weekLabel": "W",
        "daysOfWeek": [
            "周日",
            "周一",
            "周二",
            "周三",
            "周四",
            "周五",
            "周六"
        ],
        "monthNames": [
            "一月",
            "二月",
            "三月",
            "四月",
            "五月",
            "六月",
            "七月",
            "八月",
            "九月",
            "十月",
            "十一月",
            "十二月"
        ],
        "firstDay": 1
    };

    locale = options && options.locale ? options.locale : defaultLocale;
    var parentEl = options && options.parentEl ? options.parentEl : null;
    var format = options && options.format ? options.format : null;
    var timePicker = options && options.timePicker ? options.timePicker : null;
    var timePicker12Hour = options && options.timePicker12Hour ? options.timePicker12Hour : null;
    var timePickerIncrement = options && options.timePickerIncrement ? options.timePickerIncrement : null;


    $(".daterangepicker").remove();

    // Date Range Picker
    if($.isFunction($.fn.daterangepicker))
    {
        $(".daterange").each(function(i, el)
        {
            // Change the range as you desire
            var ranges = {
                '今天': [moment(), moment()],
                '昨天': [moment().subtract('days', 1), moment().subtract('days', 1)],
                'Last 30 Days': [moment().subtract('days', 29), moment()],
                'Last 7 Days': [moment().subtract('days', 6), moment()],
                'This Month': [moment().startOf('month'), moment().endOf('month')],
                'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
            };
            
            var $this = $(el),
                opts = {
                    format: format ? format : attrDefault($this, 'format', 'MM/DD/YYYY'),
                    timePicker: timePicker ? timePicker : attrDefault($this, 'timePicker', false),
                    timePicker12Hour: timePicker12Hour ? timePicker12Hour : attrDefault($this, 'timePicker12Hour', false),
                    timePickerIncrement: timePickerIncrement ? timePickerIncrement : attrDefault($this, 'timePickerIncrement', false),
                    // format: format,
                    // timePicker: timePicker,
                    // timePicker12Hour: timePicker12Hour,
                    // timePickerIncrement: timePickerIncrement,
                    separator: attrDefault($this, 'separator', ' - '),
                    parentEl: parentEl,
                    locale: locale,
                },

                min_date = attrDefault($this, 'minDate', ''),
                max_date = attrDefault($this, 'maxDate', ''),
                start_date = attrDefault($this, 'startDate', ''),
                end_date = attrDefault($this, 'endDate', '');
            
            if($this.hasClass('add-ranges'))
            {
                opts['ranges'] = ranges;
            }   
                
            if(min_date.length)
            {
                opts['minDate'] = min_date;
            }
                
            if(max_date.length)
            {
                opts['maxDate'] = max_date;
            }
                
            if(start_date.length)
            {
                opts['startDate'] = start_date;
            }
                
            if(end_date.length)
            {
                opts['endDate'] = end_date;
            }
            
            
            $this.daterangepicker(opts, function(start, end)
            {
                var drp = $this.data('daterangepicker');
                
                if($this.is('[data-callback]'))
                {
                    //daterange_callback(start, end);
                    callback_test(start, end);
                }
                
                if($this.hasClass('daterange-inline'))
                {
                    $this.find('span').html(start.format(drp.format) + drp.separator + end.format(drp.format));
                }
            });
            
            if(typeof opts['ranges'] == 'object')
            {
                $this.data('daterangepicker').container.removeClass('show-calendar');
            }
        });
    }
}