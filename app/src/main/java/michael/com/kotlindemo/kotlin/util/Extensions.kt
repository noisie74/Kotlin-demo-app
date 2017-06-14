@file:JvmName("ExtensionsUtils")

package michael.com.kotlindemo.kotlin.util

import android.view.View

/**
 * Created by mborisovskiy on 6/5/17.
 */


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}