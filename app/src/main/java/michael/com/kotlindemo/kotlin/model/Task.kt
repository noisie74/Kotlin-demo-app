package michael.com.kotlindemo.kotlin.model

import com.google.gson.annotations.SerializedName


/**
 * Created by Mikhail on 5/31/17.
 */

data class Task(
        @SerializedName("_id") val id: String? = null,
        @SerializedName("text") var taskTitle: String? = null,
        @SerializedName("completed") var isCompleted: Boolean? = null){

    fun isEmpty(): Boolean {
        return taskTitle == null || "" == taskTitle
    }

}
