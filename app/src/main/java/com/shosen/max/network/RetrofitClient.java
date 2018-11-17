package com.shosen.max.network;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.shosen.max.MaxApplication;
import com.shosen.max.R;
import com.shosen.max.api.ApiService;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.constant.Contstants;
import com.shosen.max.utils.RxUtils;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.shosen.max.MaxApplication.getAppContext;


public class RetrofitClient {

    public static final int DEFAULT_TIME_OUT = 30;//默认超时时间

    public static final int MAX_CACHE_SIZE = 10 * 1024 * 1024;//最大缓存大小

    //https://39.104.62.133:8092
    public static final String BASE_URL = "https://192.168.1.160:8092/";

    //public static final String BASE_URL = "https://39.104.62.133:8092/";

    private File cacheFile;//缓存文件的位置

    private Cache cache = null;

    private ApiService apiService;

    private static Retrofit retrofit;

    private static OkHttpClient okHttpClient;

    private static RetrofitClient mInstance;

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .addNetworkInterceptor(
                            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);

    private static final class SingleHolder {
        private static RetrofitClient instance = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingleHolder.instance;
    }

    public static RetrofitClient getInstance(String url) {
        return new RetrofitClient(url);
    }

    public static RetrofitClient getInstance(String url, Map<String, String> headers) {
        return new RetrofitClient(url, headers);
    }

    //暂时不添加上下文
    public RetrofitClient() {
        this(null, null);
    }

    public RetrofitClient(String url) {
        this(url, null);
    }

    public RetrofitClient(String url, Map<String, String> headers) {
        if (TextUtils.isEmpty(url)) {
            url = BASE_URL;
        }
        if (cacheFile == null) {
            cacheFile = new File(getAppContext().getCacheDir(), Contstants.CACHE_FILE_NAME);
        }
        if (cache == null) {
            cache = new Cache(cacheFile, MAX_CACHE_SIZE);
        }
        okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor
                (new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).
                cookieJar(new CookieJarIml()).
                addNetworkInterceptor(new CacheInterceptor()).
                addInterceptor(new BaseInterceptor(headers)).
                connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS).
                writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS).
                readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS).
                //设置链接池保持时间 8个连接数,15秒连接时间
                        connectionPool(new ConnectionPool(8,
                        15, TimeUnit.SECONDS)).
                        hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                //only test environment
                                return true;
                            }
                        }).build();
        retrofit = new Retrofit.Builder().client(okHttpClient).addConverterFactory
                (GsonConverterFactory.create()).addCallAdapterFactory
                (RxJava2CallAdapterFactory.create()).baseUrl(BASE_URL).build();

    }

    public ApiService getApiService() {
        return apiService;
    }


    public RetrofitClient createApiService() {
        apiService = create(ApiService.class);
        return this;
    }

    /**
     * 创建API service
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void get(String url, Map parameters, BaseObserver baseObserver) {
        apiService.executeGet(url, parameters).compose(ToMain()).subscribe(baseObserver);
    }

    public void post(String url, Map parameters, BaseObserver baseObserver) {
        apiService.executePost(url, parameters).compose(ToMain()).subscribe(baseObserver);

    }

    public void json(String url, String jsonStr, BaseObserver baseObserver) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        apiService.json(url, requestBody).compose(ToMain()).subscribe(baseObserver);

    }

    public void json(String url, Map parameters, BaseObserver baseObserver) {
        RequestBody requestBody =
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(parameters));
        apiService.json(url, requestBody).compose(ToMain()).subscribe(baseObserver);

    }

    /**
     * TODO 等待修改
     *
     * @param url
     * @param uriString
     */
    public void uploadFile(String url, String uriString, BaseObserver baseObserver) {
        ///RequestBody requestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/"), uriString);
        apiService.upLoadFile(url, requestBody).compose(ToMain()).subscribe(baseObserver);
    }

    /**
     * 切换到主线程
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> ToMain() {
        //推荐使用lamada表达式
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

//    protected SSLSocketFactory getSSL() {
//        try {
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            InputStream cert = MaxApplication.getAppContext().getResources().openRawResource(R.raw.client);
//            Certificate ca = cf.generateCertificate(cert);
//            cert.close();
//
//            // creating a KeyStore containing our trusted CAs
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//            return new AdditionalKeyStore(keyStore);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
