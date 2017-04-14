// params
// defaultData: ["ActionScript","AppleScript","Asp","BASIC","C","C++","Clojure","COBOL","ColdFusion","Erlang","Fortran","Groovy","Haskell","Java","JavaScript","Lisp","Perl","PHP","Python","Ruby","Scala","Scheme"];
// reqUrl : '', 远程请求数据接口 https://info.tongbanjie.com/search.json?q=
// reqMethod : '', 请求方式， 默认为 GET
// curTxt : '', 输入框ID
// tipPannel : '' 数据浮层 ID

function AutoComplete($, params) {

    if (params) {
        for (key in params) {
            this[key] = params[key] || null;
        }
        this.curTxtDOM = $('#' + this.curTxt);
        this.tipPannelDOM = $('#' + this.tipPannel);
    }

    this.switchIndex = -1; // 上下键盘切换索引

};

AutoComplete.prototype = {

    init: function () {
        this.bindEvent();
    },

    bindEvent: function () {
        // 绑定数据浮层 单个数据项点击事件
        var that = this;
        this.tipPannelDOM.delegate('li', 'click', function (e) {
            that.curTxtDOM.val($(this).text());
            that.tipPannelDOM.hide();
        });

        this.curTxtDOM.on('focus', function (e) {
            that.fillData(that.curTxtDOM.val());
            return false;
        }).on('blur', function (e) {
            e.stopPropagation();
            return false;
        });

        this.curTxtDOM.on('keydown', function (e) {
            var keyCode = e.keyCode;

            setTimeout(function () {
                if ((keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 96 && keyCode <= 105) || keyCode === 8) {
                    that.filterData(that.curTxtDOM.val(), function (err, datas) {
                        if (!err && (datas && datas.length > 0)) {
                            var tpls = [];
                            datas.forEach(function (item, index) {
                                tpls.push('<li>' + item + '</li>');
                            });
                            var txtWidth = that.curTxtDOM.outerWidth();
                            that.tipPannelDOM.html(tpls.join('')).css({
                                'width': txtWidth + 'px',
                                'top': that.curTxtDOM.offset().top + that.curTxtDOM.height(),
                                'left': that.curTxtDOM.offset().left
                            }).show();
                            that.switchIndex = -1;
                        }
                    });
                }
            }, 150);

            // 向下键盘操作
            if (keyCode === 40) {
                that.switchIndex++;
                var curLi = that.tipPannelDOM.find('li')[that.switchIndex];
                if (curLi) {
                    if ($(curLi).prev()) {
                        $(curLi).prev().css({
                            'background': '#FFF',
                            'color': '#33384A'
                        });
                    }
                    $(curLi).css({
                        'background': '#0097CF',
                        'color': '#FFF'
                    })
                }
            }

            // 向上键盘操作
            if (keyCode === 38) {
                that.switchIndex--;
                var curLi = that.tipPannelDOM.find('li')[that.switchIndex];
                if (curLi) {
                    if ($(curLi).next()) {
                        $(curLi).next().css({
                            'background': '#FFF',
                            'color': '#33384A'
                        });
                    }
                    $(curLi).css({
                        'background': '#0097CF',
                        'color': '#FFF'
                    })
                }
            }

            if (keyCode === 13) {
                var txt = $(that.tipPannelDOM.find('li')[that.switchIndex]).html();
                that.curTxtDOM.val(txt)
                that.tipPannelDOM.hide();
            }

            if (keyCode === 27) {
                that.tipPannelDOM.hide();
            }
        });
        $(window).on('keydown', function (e) {
            if (e.keyCode === 27) {
                that.tipPannelDOM.hide();
            }
        }).on('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            that.fillData(that.curTxtDOM.val());
            if (!$(e.target).hasClass('search-txt')) {
                that.tipPannelDOM.hide();
            }
        });
    },

    // 为Tip框填充数据
    fillData: function (txtV) {
        var that = this;
        this.filterData(txtV, function (err, datas) {
            if (!err && (datas && datas.length > 0)) {
                var tpls = [];
                datas.forEach(function (item, index) {
                    tpls.push('<li>' + item + '</li>');
                });
                var txtWidth = that.curTxtDOM.outerWidth();
                that.tipPannelDOM.html(tpls.join('')).css({
                    'width': txtWidth + 'px',
                    'top': that.curTxtDOM.offset().top + that.curTxtDOM.height(),
                    'left': that.curTxtDOM.offset().left
                }).show();
                that.switchIndex = -1;
            }
        });
    },

    // 数据过滤
    filterData: function (txt, cb) {
        var results = [];
        this.loadData(txt, function (err, datas) {
            if (!err && (datas && datas.length > 0)) {
                datas.forEach(function (item, index) {
                    if (item.indexOf(txt) > -1) {
                        results.push(item);
                    }
                });
                // 这里考虑触发input框 focus 事件默认加载所有数据, 因此当results为空时返回所有数据集合
                if (results.length === 0) {
                    results = datas;
                }
            }

            cb(err, results);
        });
    },
    // 加载远程数据
    loadData: function (txt, cb) {
        var that = this;
        if (this.reqUrl) {
            $.ajax({
                url: that.remoteUrl + txt, // https://info.tongbanjie.com/search.json?q=
                method: that.reqMethod || 'GET',
                dataType: 'json',
                success: function (data) {
                    cb(null, data);
                },
                error: function (err) {
                    cb(err, null);
                }
            });
        } else {
            cb(null, this.defaultData);
        }
    }
};