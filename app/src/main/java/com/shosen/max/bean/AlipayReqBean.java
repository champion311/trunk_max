package com.shosen.max.bean;

public class AlipayReqBean {

    private String alipay_sdk; //alipay-sdk-java-dynamicVersionNo,
    private String charset; //UTF-8,
    private String biz_content; //%7B%22body%22%3A%22MaxMaker%E6%96%B0%E8%83%BD%E6%BA%90%E6%B1%BD%E8%BD%A6%22%2C%22out_trade_no%22%3A%22201810091539064293589818%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%96%B0%E8%83%BD%E6%BA%90%E6%B1%BD%E8%BD%A6%22%2C%22timeout_express%22%3A%223m%22%2C%22total_amount%22%3A%2225000%22%7D,
    private String method; //alipay.trade.app.pay,
    private String payStr; //alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018092761539635&biz_content=%7B%22body%22%3A%22MaxMaker%E6%96%B0%E8%83%BD%E6%BA%90%E6%B1%BD%E8%BD%A6%22%2C%22out_trade_no%22%3A%22201810091539064293589818%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%96%B0%E8%83%BD%E6%BA%90%E6%B1%BD%E8%BD%A6%22%2C%22timeout_express%22%3A%223m%22%2C%22total_amount%22%3A%2225000%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2F1975ab4627.iask.in%2Fpay%2FaliPayNotify&return_url=http%3A%2F%2F1975ab4627.iask.in%2Fpay%2FaliPayNotify&sign=pFuzLDtfICvg%2FTmO0Flwjk7CnOyywnSK%2F2nkob2HR8WdvffU6ihb45rDG9THQQiQQDb1AbCuIu0%2BVPDOTBUNfh05y%2FQQKUoKfkWSeWjc%2BgLEJA4ZyufMp4ODA5MpRnjdhAUyThO%2BSkesncTEHRQLUu2XqZZhaZRJ16sBPjNUQShUhWlniw%2F6O2CBRrptqsB8xFRgeMtmGvxrCQH4HLJlxthUxly6y%2FUNrY7xiSyTqlCuPm2Nk9oiDW9IYNxXahAPAK8zFn7wn1PAWMmfTM%2BMJJ4Awm8jC2DnJSju8o62aJULezRV8sNJp8iI6bPsPBapNL%2BDqxJsv1GdGKkH7VlN8g%3D%3D&sign_type=RSA2×tamp=2018-10-09+13%3A52%3A25&version=1.0,
    private String format; //json,
    private String sign; //pFuzLDtfICvg%2FTmO0Flwjk7CnOyywnSK%2F2nkob2HR8WdvffU6ihb45rDG9THQQiQQDb1AbCuIu0%2BVPDOTBUNfh05y%2FQQKUoKfkWSeWjc%2BgLEJA4ZyufMp4ODA5MpRnjdhAUyThO%2BSkesncTEHRQLUu2XqZZhaZRJ16sBPjNUQShUhWlniw%2F6O2CBRrptqsB8xFRgeMtmGvxrCQH4HLJlxthUxly6y%2FUNrY7xiSyTqlCuPm2Nk9oiDW9IYNxXahAPAK8zFn7wn1PAWMmfTM%2BMJJ4Awm8jC2DnJSju8o62aJULezRV8sNJp8iI6bPsPBapNL%2BDqxJsv1GdGKkH7VlN8g%3D%3D,
    private String notify_url; //http%3A%2F%2F1975ab4627.iask.in%2Fpay%2FaliPayNotify,
    private String version; //1.0,
    private String return_url; //http%3A%2F%2F1975ab4627.iask.in%2Fpay%2FaliPayNotify,
    private String app_id; //2018092761539635,
    private String sign_type; //RSA2,
    private String timestamp; //2018-10-09+13%3A52%3A25

    public String getAlipay_sdk() {
        return alipay_sdk;
    }

    public void setAlipay_sdk(String alipay_sdk) {
        this.alipay_sdk = alipay_sdk;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPayStr() {
        return payStr;
    }

    public void setPayStr(String payStr) {
        this.payStr = payStr;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
