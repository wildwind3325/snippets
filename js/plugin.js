// Utils
$.util = {
    getRelativePath: function (url) {
        var str = url.split('//')[1];
        if (str.indexOf('?') != -1) {
            str = str.split('?')[0];
        }
        var start = str.indexOf('/');
        return str.substring(start).replace(/\//g, '%2f');
    },
    getUrlParam: function (name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        } else {
            return undefined;
        }
    },
    btoa: function (str) {
        return window.btoa(window.encodeURIComponent(str));
    },
    atob: function (str) {
        return window.decodeURIComponent(window.atob(str));
    },
    setCookie: function (name, value, path, expire) {
        var now = new Date();
        now.setTime(now.getTime() + expire * 24 * 3600 * 1000);
        document.cookie = name + '=' + value + '; path=' + path + '; expires=' + now.toUTCString();
    },
    getCookie: function (name) {
        var arr = document.cookie.split('; ');
        for (var i = 0; i < arr.length; i++) {
            var kv = arr[i].split('=');
            if (kv[0] == name) {
                return kv[1];
            }
        }
        return '';
    },
    getFilter: function (obj, unique) {
        var filter = {};
        if (!obj.Id) {
            obj.Id = '0';
        }
        if (obj.Id != '0') {
            filter.data = ['Id', '<>', obj.Id];
        }
        var conditions = new Array();
        for (var i = 0; i < unique.length; i++) {
            var ands = new Array();
            for (var j = 0; j < unique[i].length; j++) {
                ands.push({
                    data: [unique[i][j], '=', eval('obj.' + unique[i][j])]
                });
            }
            if (ands.length > 0) {
                conditions.push({
                    and: ands
                });
            }
        }
        if (conditions.length > 0) {
            filter.or = conditions;
        } else {
            filter.data = ['Id', '=', '0'];
        }
        return filter;
    }
};

// Extra method
Date.prototype.format = function (fmt) {
    var o = {
        'M+': this.getMonth() + 1,
        'd+': this.getDate(),
        'H+': this.getHours(),
        'h+': this.getHours(),
        'm+': this.getMinutes(),
        's+': this.getSeconds(),
        'q+': Math.floor((this.getMonth() + 3) / 3),
        'S': this.getMilliseconds()
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
        }
    }
    return fmt;
};

String.prototype.format = function () {
    var args = arguments;
    return this.replace(/\{(\d+)\}/g,
            function (m, i, o, n) {
                return args[i];
            });
};