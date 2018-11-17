package com.shosen.max.api;

import com.shosen.max.bean.AlipayReqBean;
import com.shosen.max.bean.CommentBean;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.FileUploadResponse;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.bean.FriendInvitationBean;
import com.shosen.max.bean.GetCountByUserIdBean;
import com.shosen.max.bean.MyRessMessBean;
import com.shosen.max.bean.OrderResponse;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.bean.RelationBean;
import com.shosen.max.bean.RemainNumResponse;
import com.shosen.max.bean.RewardListBean;
import com.shosen.max.bean.RewardTotalResponse;
import com.shosen.max.bean.User;
import com.shosen.max.bean.UserDetetail;
import com.shosen.max.bean.VerifyCodeResponse;
import com.shosen.max.bean.VersionUpdateResponse;
import com.shosen.max.bean.WxPayBean;

import java.io.File;
import java.util.List;
import java.util.Map;


import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface ApiService {

    public static final String BASE_URL = "http://192.168.1.160:8092/";

    //请求验证码
    @POST("login/code")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<VerifyCodeResponse>> loginCode(@Body RequestBody requestBody);

    //登出
    @POST("login/logoout")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> logoOut(@Body RequestBody requestBody);

    //登陆验证
    @POST("login/validate")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<LoginResponse>> loginValidate(@Body RequestBody requestBody);

    //用户预定
    @POST("user/book")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> userBook(@Body RequestBody requestBody);

    //预定详情
    @POST("user/bookDetail")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<OrderResponse>> userBookDetail(@Body RequestBody requestBody);

    //预定列表
    @POST("user/bookList")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<OrderResponse>>> userBookList(@Body RequestBody requestBody);

    //支付订单
    @POST("user/payBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> payBook(@Body RequestBody requestBody);

    //取消订单
    @POST("user/cancelBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> cancelBook(@Body RequestBody requestBody);

    //删除订单
    @POST("user/delBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> delBook(@Body RequestBody requestBody);

    //预定
    @POST("user/book")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<OrderResponse>> book(@Body RequestBody requestBody);

    //检查更新
    @POST("v/upgrade")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> checkUpdate(@Body RequestBody requestBody);

    //更新用户信息
    @POST("user/editUser")
    @Multipart
    Observable<BaseResponse<UserDetetail>> updateUserInfo(@PartMap() Map<String, RequestBody> partMap,
                                                          @Part @Nullable MultipartBody.Part file);

    //更新用户信息 不修改头像
    @POST("user/editUser")
    @Multipart
    Observable<BaseResponse<UserDetetail>> updateUserInfo(@PartMap() Map<String, RequestBody> partMap);

    //获取微信支付信息
    @POST("user/appWxPayBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<WxPayBean>> invokeWxPay(@Body RequestBody requestBody);

    //获取支付宝支付信息
    @POST("user/appAliPayBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<AlipayReqBean>> invokeAliPay(@Body RequestBody requestBody);

    //津贴领取列表
    @POST("user/rewardList")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<RewardListBean>>> rewardList(@Body RequestBody requestBody);

    //津贴汇总信息
    @POST("user/rewardTotal")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<RewardTotalResponse>> rewardTotal(@Body RequestBody requestBody);

    //根据Phone查询用户
    @POST("user/selectUserByPhone")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<User>> selectUserByPhone(@Body RequestBody requestBody);

    //查找字典数据 type=12抽奖数据
    @GET("base/dictionary")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<DictionaryBean>>> getDictionary(@Query("type") String type);

    //(未登录时查询)剩余车主数
    @GET("base/remainOwner")
    Observable<BaseResponse<RemainNumResponse>> getRemainNum();

    //检查版本
    @POST("v/upgrade")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<VersionUpdateResponse>> getLatestVerison(@Body RequestBody requestBody);

    //微信绑定(不需要验证码)
    @POST("weChat/loginRegister")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> weChatLoginRegister(@Body RequestBody requestBody);

    //微信注册
    @POST("weChat/login")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> weChatLogin(@Body RequestBody requestBody);

    //微信绑定
    @POST("weChat/register")
    Observable<BaseResponse<LoginResponse>> weChatRegister(@Body RequestBody requestBody);

    //微信解绑
    @POST("base/weChatUnLock")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> weChatUnLock(@Body RequestBody requestBody);

    //朋友圈数据
    @POST("circle/selectMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendCircleBean>>> selectFriendCircle(@Body RequestBody requestBody);

    //刷新朋友圈数据
    @POST("circle/refreshMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> refreshMess(@Body RequestBody requestBody);

    //发布朋友圈消息
    @POST("circle/addMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> addMess(@Body RequestBody requestBody);

    //删除朋友圈消息
    @POST("circle/delMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> delMess(@Body RequestBody requestBody);


    //获取评论列表
    @POST("circle/selectComm")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<CommentBean>>> getComments(@Body RequestBody requestBody);

    //添加评论
    @POST("circle/addComment")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> addComment(@Body RequestBody requestBody);

    //删除评论
    @POST("circle/delComment")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> delComment(@Body RequestBody requestBody);

    //添加关注
    @POST("circle/addFollow")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> addFollow(@Body RequestBody requestBody);

    //取消关注
    @POST("circle/delFollow")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> delFollow(@Body RequestBody requestBody);

    //关注列表
    @POST("circle/followList")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> getFollowList(@Body RequestBody requestBody);

    //上传一张图片
    @POST("base/upload")
    @Multipart
    Observable<BaseResponse<FileUploadResponse>> baseUpLoad(@Part @Nullable MultipartBody.Part file);

    //上传多张图片
    @POST("base/uploadFiles")
    @Multipart
    Observable<BaseResponse<FileUploadResponse>> baseUpLoadFiles(@Part @Nullable MultipartBody.Part... files);

    //获取BANNER数据
    @GET("base/bannerList")
    Observable<BaseResponse<List<String>>> getBannerList(@Query("type") int type);

    //好友推荐
    @POST("circle/friendInvi")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendInvitationBean>>> getFriendInvitation(@Body RequestBody requestBody);

    //点赞
    @POST("circle/updateMessMark")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> updateMessMark(@Body RequestBody requestBody);

    //评论点赞
    @POST("circle/updateCommentMark")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> updateCommentMark(@Body RequestBody requestBody);

    //热门话题
    @POST("circle/hotTop")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendCircleBean>>> gethotTop(@Body RequestBody requestBody);

    //个人关注
    @POST("circle/selfFollow")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendInvitationBean>>> selfFollow(@Body RequestBody requestBody);

    //个人消息
    @POST("circle/selfMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendCircleBean>>> selfMess(@Body RequestBody requestBody);

    //更多关注
    @POST("circle/moreFollow")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> moreFollower(@Body RequestBody requestBody);

    //获取个人中心 朋友圈 粉丝 关注数量接口
    @POST("circle/getCountByUserId")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<GetCountByUserIdBean>> getCountByUserId(@Body RequestBody requestBody);

    //我的消息列表
    @POST("circle/myReMesss")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<MyRessMessBean>>> getMyReMesss(@Body RequestBody requestBody);

    //举报拉黑  
    @POST("circle/defriendAndComplain")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> defriendAndComplain(@Body RequestBody requestBody);

    //取消拉黑
    @POST("circle/cancelDefriend")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> cancelDefriend(@Body RequestBody requestBody);

    //粉丝列表
    @POST("circle/selectFans")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendInvitationBean>>> getMyFans(@Body RequestBody requestBody);

    //判断用户关系
    @POST("circle/isRelation")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<RelationBean>> isRelation(@Body RequestBody requestBody);

    //消息提醒
    @POST("circle/noticeMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<MyRessMessBean>>> getNoticeMess(@Body RequestBody requestBody);

    //签到
    @POST("base/signLogin")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<String>> signLogin(@Body RequestBody requestBody);

    @GET("{url}")
    Observable<BaseResponse<Object>> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> maps
    );


    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            //  @Header("") String authorization,
            @QueryMap Map<String, String> maps);

    @POST("{url}")
    Observable<ResponseBody> json(
            @Path("url") String url,
            @Body RequestBody jsonStr);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Path("headers") Map<String, String> headers,
            @Part("filename") String description,
            @PartMap() Map<String, RequestBody> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);


}
