package uz.abbosbek.chatappcodial.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import uz.abbosbek.chatappcodial.models.notification.MyResponse
import uz.abbosbek.chatappcodial.models.notification.Sender

interface ApiService {
    @Headers(
        "Content-type:application/json",
        "Authorization:key=AAAAagIc0YE:APA91bHXWWgQu_W5lPjx3QPleZGnM29OfVVwvbdzw8BbF1ZpBxQBKBD7okElkahFmHuqliS0jLhB5lim-JZanANN21QTRsNcG0hhkFU2xbHZHEAyAXV0QGmycK_JOSkcCXYAGUlm0oCk"
    )
    @POST("fcm/send")
    fun sendNotification(@Body sender: Sender): Call<MyResponse>
}