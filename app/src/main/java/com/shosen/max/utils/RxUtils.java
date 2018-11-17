package com.shosen.max.utils;

import com.shosen.max.bean.BaseResponse;
import com.shosen.max.constant.Contstants;
import com.shosen.max.ui.activity.LoginActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/9/14.
 */
public class RxUtils {

    //Rxjava2的一些工具类

    /**
     * 统一线程切换
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> ToMain() {
        //推荐使用lamada表达式
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        //  new ObservableTransformer<T, T>() {
//
//            @Override
//            public ObservableSource apply(Observable upstream) {
//                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
//            }
//        };
    }

    ObservableTransformer transformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return null;
        }
    };

    public static <T> ObservableTransformer<BaseResponse<T>, T> handleResult() {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(new ResponseFunction<>());
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<BaseResponse<T>, ObservableSource<T>> {


        @Override
        public ObservableSource<T> apply(BaseResponse<T> tBaseResponse) throws Exception {
            int code = tBaseResponse.getCode();
            String message = tBaseResponse.getMsg();
            if (code == 100) {
                return Observable.just(tBaseResponse.getData());
            } else if (code == 998) {
                //
                //ActivityUtils.startActivity(LoginActivity.class);
                //TODO 待修改 错误登陆的情况
                return Observable.error(new RuntimeException(tBaseResponse.getMsg()));
            } else {
                //TODO 待修改 因该在ErrorResumeFunction中处理
                return Observable.error(new RuntimeException(tBaseResponse.getMsg()));
            }
        }
    }

    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends BaseResponse<T>>> {

        @Override
        public ObservableSource<? extends BaseResponse<T>> apply(Throwable throwable) throws Exception {
            return Observable.error(new RuntimeException(throwable.toString()));
        }
    }

    /**
     * 倒计时
     *
     * @param time 倒计时时间
     * @return
     */
    public static Observable<Integer> countdown(int time) {
        if (time < 0) {
            time = 0;
        }
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).
                        map(new Function<Long, Integer>() {
                            @Override
                            public Integer apply(Long aLong) throws Exception {
                                return countTime - aLong.intValue();
                            }
                        }).take(countTime + 1);

    }

}
