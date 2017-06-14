package michael.com.kotlindemo.kotlin.network

import michael.com.kotlindemo.kotlin.util.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Mikhail on 5/31/17.
 */

object TaskService {

    fun networkCall(): TaskApi {

        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(TaskApi::class.java)
    }

}
