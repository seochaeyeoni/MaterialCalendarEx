package com.alice.materialcalender.src

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

open class BaseFrag : Fragment() {
    fun showCustomToast(message: String?) {
        if (message != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

}