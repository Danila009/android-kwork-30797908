package com.example.messenger.data.network.messagingService.api

import com.example.messenger.data.network.messagingService.model.PushMessagingBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MessagingServiceApi  {

    companion object {
        const val BASE_URL = "https://fcm.googleapis.com"
        const val PUSH_FIREBASE_URL = "/fcm/send"
        const val SERVER_KEY = " AAAAibefKEk:APA91bHgZEoRbwi35cEE9lstjsdmINtT03J-k68fWCenq9TcTOP19zYO048E7TN3-nmsqc-8uJu2xcJgMcDOKEvdY4vFhXAPRjP1rkI1R7C5z1MkMs3p-4VpQL8scI1_k-K146E8vKRQ "
    }

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:application/json")
    @POST(PUSH_FIREBASE_URL)
    suspend fun pushMessaging(
        @Body body: PushMessagingBody
    ):Response<ResponseBody>
}