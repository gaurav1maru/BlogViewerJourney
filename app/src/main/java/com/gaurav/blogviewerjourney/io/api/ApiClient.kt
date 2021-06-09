package com.gaurav.blogviewerjourney.io.api

import com.gaurav.blogviewerjourney.io.model.BlogModel
import com.gaurav.blogviewerjourney.io.model.CommentModel
import com.gaurav.blogviewerjourney.io.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiClient {
    @GET("/posts")
    suspend fun getBlogList(): Response<List<BlogModel>>

    @GET("/users")
    suspend fun getUserList(): Response<List<UserModel>>

    @GET("/posts/{postId}/comments")
    suspend fun getCommentList(@Path("postId") postId: Int): Response<List<CommentModel>>
}