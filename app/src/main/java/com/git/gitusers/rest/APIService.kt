package com.git.gitusers.rest
import com.git.gitusers.model.Users
import retrofit2.Call
import retrofit2.http.GET

interface APIService {



    @GET("users")
    fun getUserobj():Call<List<Users>>


    @GET("users")    //End Url
    fun fetchQuestions(): Call<List<Users>>

}
