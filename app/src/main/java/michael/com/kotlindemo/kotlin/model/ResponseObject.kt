package michael.com.kotlindemo.kotlin.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Mikhail on 6/1/17.
 */

open class ResponseObject {

    open var tasks: MutableList<Task>? = null

    @SerializedName("_id") open var id: String? = ""
    @SerializedName("text") open var taskTitle: String? = null
    @SerializedName("completed") open var isCompleted: Boolean? = false
}