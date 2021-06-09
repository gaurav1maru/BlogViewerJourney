package com.gaurav.blogviewerjourney.io.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gaurav.blogviewerjourney.io.api.ApiAdapter
import com.gaurav.blogviewerjourney.io.model.BlogModel
import com.gaurav.blogviewerjourney.io.model.CommentModel
import com.gaurav.blogviewerjourney.io.model.UserModel
import kotlinx.coroutines.*

class BlogViewModel : ViewModel(), CoroutineScope by MainScope() {
    val blogListLiveData: MutableLiveData<List<BlogModel>> = MutableLiveData<List<BlogModel>>()
    val userListLiveData: MutableLiveData<List<UserModel>> = MutableLiveData<List<UserModel>>()
    val commentListLiveData: MutableLiveData<List<CommentModel>> =
        MutableLiveData<List<CommentModel>>()

    fun fetchBlogList() {
        launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiAdapter.apiClient.getBlogList()
                }
                if (response.isSuccessful && response.body() != null) {
                    blogListLiveData.postValue(response.body())
                } else {
                    blogListLiveData.postValue(arrayListOf())
                }
            } catch (e: Exception) {
                Log.e("fetchBlogList", "fail - " + e.localizedMessage)
                blogListLiveData.postValue(arrayListOf())
            }
        }
    }

    fun getBlogList(): LiveData<List<BlogModel>> {
        return blogListLiveData
    }

    fun fetchUserList() {
        launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiAdapter.apiClient.getUserList()
                }
                if (response.isSuccessful && response.body() != null) {
                    userListLiveData.postValue(response.body())
                } else {
                    userListLiveData.postValue(arrayListOf())
                }
            } catch (e: Exception) {
                Log.e("fetchUserList", "fail - " + e.localizedMessage)
                userListLiveData.postValue(arrayListOf())
            }
        }
    }

    fun getUserList(): LiveData<List<UserModel>> {
        return userListLiveData
    }

    fun fetchCommentListByPostId(id: Int) {
        launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiAdapter.apiClient.getCommentList(id)
                }
                if (response.isSuccessful && response.body() != null) {
                    commentListLiveData.postValue(response.body())
                } else {
                    commentListLiveData.postValue(arrayListOf())
                }
            } catch (e: Exception) {
                Log.e("fetchCommentListById", "fail - " + e.localizedMessage)
                commentListLiveData.postValue(arrayListOf())
            }
        }
    }
}