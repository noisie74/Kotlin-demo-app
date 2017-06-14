package michael.com.kotlindemo.kotlin.network

import michael.com.kotlindemo.kotlin.model.ResponseObject
import michael.com.kotlindemo.kotlin.model.Task
import retrofit2.Response
import retrofit2.http.*
import rx.Observable
import rx.Single

/**
 * Created by Mikhail on 5/31/17.
 */

interface TaskApi {

    @GET("/tasks")
    fun getTasks(): Observable<Response<ResponseObject>>

    @POST("/tasks")
    fun saveTask(@Body task: Task): Single<Response<ResponseObject>>

    @PATCH("/tasks/{id}")
    fun updateTask(@Path("id") id: String, @Body task: Task): Single<Response<ResponseObject>>

}
