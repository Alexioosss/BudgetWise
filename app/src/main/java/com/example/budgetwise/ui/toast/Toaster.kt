package com.example.budgetwise.ui.toast

import android.content.Context
import android.widget.Toast

fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}