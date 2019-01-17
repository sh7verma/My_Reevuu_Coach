package com.myreevuuCoach.network

import com.myreevuuCoach.firebase.MessageHistoryModel
import com.myreevuuCoach.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.DELETE




interface ApiInterface {
    @FormUrlEncoded
    @POST("/api/v1/coach/registers")
    fun userSignup(@Field("name") name: String,
                   @Field("email") email: String,
                   @Field("password") password: String,
                   @Field("device_token") device_token: String,
                   @Field("platform_type") platform_type: String): Call<SignUpModel>

    @FormUrlEncoded
    @POST("/api/v1/coach/authentications/authenticate")
    fun userLogin(@Field("email") email: String,
                  @Field("password") password: String,
                  @Field("device_token") device_token: String,
                  @Field("platform_type") platform_type: Int): Call<SignUpModel>

    @FormUrlEncoded
    @POST("/api/v1/users/forget_password")
    fun forgotPassword(@Field("email") email: String,
                       @Field("user_type") password: Int): Call<BaseSuccessModel>

    @FormUrlEncoded
    @POST("/api/v1/coach/coaches")
    fun setupCoach(@Field("access_token") access_token: String,
                   @Field("sport_id") sport_id: Int,
                   @Field("experience") experience: Int,
                   @Field("from_college") from_college: Int,
                   @Field("level") level: Int,
                   @Field("coach_level") coach_level: Int,
                   @Field("coach_experience") coach_experience: Int,
                   @Field("expertises") expertises: String,
                   @Field("about") about: String,
                   @Field("certificates") certificates: String): Call<SignUpModel>

    @Multipart
    @POST("/api/v1/coach/profile")
    fun createProfile(@Part("access_token") access_token: RequestBody,
                      @Part("username") username: RequestBody,
                      @Part("phone_code") phone_code: RequestBody,
                      @Part("phone_number") phone_number: RequestBody,
                      @Part("gender") gender: RequestBody,
                      @Part("athlete_email") athlete_email: RequestBody,
                      @Part("currency") currency: RequestBody,
                      @Part("routing_number") routing_number: RequestBody,
                      @Part("account_number") account_number: RequestBody,
                      @Part("city") city: RequestBody,
                      @Part("address") address: RequestBody,
                      @Part("postal_code") postal_code: RequestBody,
                      @Part("states") state: RequestBody,
                      @Part("dob") dob: RequestBody,
                      @Part("first_name") first_name: RequestBody,
                      @Part("last_name") last_name: RequestBody,
                      @Part("ssn") ssn: RequestBody,
                      @Part("country_code") country_code: RequestBody,
                      @Part user_image: MultipartBody.Part,
                      @Part contractor_forms: MultipartBody.Part,
                      @Part document: MultipartBody.Part): Call<SignUpModel>

    @FormUrlEncoded
    @POST("/api/v1/users/resend_email")
    fun resendEmail(@Field("access_token") access_token: String): Call<BaseSuccessModel>

    @FormUrlEncoded
    @POST("/api/v1/users/username")
    fun verifyUsername(@Field("username") username: String,
                       @Field("access_token") access_token: String): Call<BaseSuccessModel>


    @GET("/api/v1/feeds")
    fun getFeeds(@Query("access_token") access_token: String,
                 @Query("page") page: String): Call<FeedModel>

    @GET("/api/v1/feeds")
    fun getSearchFeeds(@Query("access_token") access_token: String,
                       @Query("search") search: String): Call<FeedModel>

    @GET("/api/v1/videos")
    fun getVideos(@Query("access_token") access_token: String,
                  @Query("id") id: Int,
                  @Query("page") page: Int): Call<FeedModel>

    @FormUrlEncoded
    @POST("/api/v1/reports")
    fun reportFeed(@Field("video_id") video_id: Int,
                   @Field("reason") reason: String,
                   @Field("access_token") access_token: String): Call<BaseSuccessModel>

    @Multipart
    @POST("/api/v1/videos")
    fun uploadVideo(@Part("access_token") access_token: RequestBody,
                    @Part video: MultipartBody.Part,
                    @Part thumbnail: MultipartBody.Part,
                    @Part("title") title: RequestBody,
                    @Part("description") description: RequestBody,
                    @Part("privacy") privacy: RequestBody,
                    @Part("sport_id") sport_id: RequestBody,
                    @Part("coach_id") coach_id: RequestBody,
                    @Part("video_width") video_width: RequestBody,
                    @Part("video_height") video_height: RequestBody,
                    @Part("expertises") expertises: RequestBody): Call<VideoModel>

    @Multipart
    @PATCH("/api/v1/profiles")
    fun editProfile(@Part("access_token") access_token: RequestBody,
                    @Part profile_pic: MultipartBody.Part,
                    @Part("name") name: RequestBody,
                    @Part("gender") gender: RequestBody): Call<SignUpModel>

    @GET("/api/v1/payments")
    fun payments(@Query("access_token") access_token: String): Call<PaymentModel>

    @GET("/api/v1/transactions")
    fun transactions(@Query("access_token") access_token: String): Call<TransactionModel>

    @FormUrlEncoded
    @POST("/api/v1/users/skip_tip")
    fun skip_tip(@Field("access_token") access_token: String): Call<SkipModel>

    @FormUrlEncoded
    @POST("/api/v1/review_requests/response_a_request")
    fun response_a_request(@Field("access_token") access_token: String,
                           @Field("id") id: String,
                           @Field("review_status") review_status: String): Call<RequestsModel>

    @FormUrlEncoded
    @POST("/api/v1/settings/change_password")
    fun change_password(@Field("access_token") access_token: String,
                        @Field("password") id: String,
                        @Field("new_password") review_status: String): Call<BaseSuccessModel>

    @GET("/api/v1/coach-requests")
    fun coach_requests(@Query("access_token") access_token: String,
                       @Query("search") search: String,
                       @Query("review_status") review_status: String): Call<RequestListModel>

    @GET("/api/v1/athlete_profile")
    fun athlete_profile(@Query("access_token") access_token: String,
                        @Query("id") review_status: String): Call<AthleteModel>

    @FormUrlEncoded
    @POST("/api/v1/settings/change_email")
    fun change_email(@Field("access_token") access_token: String,
                     @Field("email") email: String): Call<BaseSuccessModel>

    @FormUrlEncoded
    @POST("/api/v1/settings/notification_preferences")
    fun notification_preferences(@Field("access_token") access_token: String,
                                 @Field("email_notification") email_notification: String): Call<BaseSuccessModel>

    @FormUrlEncoded
    @POST("/api/v1/chats/chat_history")
    fun getChatHistory(@Field("access_token") access_token: String): Call<MessageHistoryModel>

    @FormUrlEncoded
    @POST("/api/v1/chats/report")
    fun chatReport(@Field("access_token") access_token: String,
                   @Field("other_user_id") other_user_id: String): Call<BaseSuccessModel>

    @FormUrlEncoded
    @POST("/api/v1/chats/delete_messages")
    fun chat_delete_messages(@Field("access_token") access_token: String,
                             @Field("message_id") other_user_id: String): Call<BaseSuccessModel>

    @DELETE("/api/v1/videos")
    fun deleteVideo(@Query("id") id: Int,
                    @Query("access_token") access_token: String): Call<BaseSuccessModel>

    @DELETE("/api/v1/chats")
    fun clear_converation(@Query("access_token") access_token: String,
                          @Query("chat_dialog_id") chat_dialog_id: String,
                          @Query("firebase_time") firebase_time: String,
                          @Query("delete_type") delete_type: String): Call<BaseSuccessModel>


    @GET("/api/v1/notifications")
    fun getNotification(@Query("access_token") access_token: String): Call<NotificationCenterModel>

    @FormUrlEncoded
    @PATCH("/api/v1/notifications")
    fun setNotification(@Field("access_token") access_token: String,
                        @Field("read_type") read_type: String,
                        @Field("notification_id") notification_id: String): Call<SkipModel>  //0 = Off //1 == on

    @GET("/api/v1/video_by_id")
    fun getSingleVideo(@Query("access_token") access_token: String,
                       @Query("id") videoId: String): Call<VideoModelSingle>

    @GET("/api/v1/request_by_id")
    fun getRequestDetailByID(@Query("access_token") access_token: String,
                             @Query("id") videoId: String): Call<RequestsModel>


}