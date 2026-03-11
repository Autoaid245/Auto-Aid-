package com.project.auto_aid.data.network

import com.project.auto_aid.data.network.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /* ================= AUTH ================= */

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @POST("api/auth/signup")
    suspend fun signup(@Body body: SignupRequest): Response<SignupInitResponse>

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body body: VerifyOtpRequest): Response<AuthResponse>

    @POST("api/auth/resend-otp")
    suspend fun resendOtp(@Body body: ResendOtpRequest): Response<MessageResponse>

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): Response<MessageResponse>

    /* ================= PROFILE ================= */

    @GET("api/auth/me")
    suspend fun getMe(): Response<MeResponse>

    @GET("api/providers/me")
    suspend fun getProviderMe(): Response<ProviderDto>

    @PATCH("api/providers/me")
    suspend fun updateProviderProfile(@Body body: UpdateProfileRequest): Response<ProviderDto>

    /* ================= REQUESTS ================= */

    @POST("api/requests")
    suspend fun createRequest(@Body body: CreateRequestBody): Response<RequestDto>

    @GET("api/requests/provider")
    suspend fun getProviderBuckets(
        @Query("providerType") providerType: String
    ): Response<ProviderBucketsResponse>

    @POST("api/requests/{id}/assign")
    suspend fun assignRequest(@Path("id") requestId: String): Response<RequestDto>

    @POST("api/requests/{id}/decline")
    suspend fun declineRequest(@Path("id") requestId: String): Response<MessageResponse>

    @PATCH("api/requests/{id}/status")
    suspend fun updateRequestStatus(
        @Path("id") requestId: String,
        @Body body: UpdateStatusBody
    ): Response<RequestDto>

    @GET("api/requests/{id}")
    suspend fun getRequestById(@Path("id") id: String): Response<RequestDto>

    @GET("api/requests/{id}/location")
    suspend fun getUserLocation(@Path("id") requestId: String): Response<LocationResponse>

    /* ================= PROVIDERS ================= */

    @GET("api/providers/available")
    suspend fun getAvailableProviders(
        @Query("providerType") providerType: String,
        @Query("isOnline") isOnline: Boolean = true
    ): Response<List<ProviderLiteDto>>

    /* ================= HEALTH ================= */

    @GET("api/ping")
    suspend fun ping(): Response<Map<String, Any>>

    /* ================= UPLOADS ================= */

    @Multipart
    @POST("api/uploads/profile-image")
    suspend fun uploadProfileImage(
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>
}