package com.example.appcar.ui.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appcar.ui.base.BaseActivity

abstract class BaseFragment : Fragment() {


    protected val isDoubleClick: Boolean
        get() {
            if (activity == null) {
                return false
            }
            return if (activity is BaseActivity) {
                (activity as BaseActivity?)!!.isDoubleClick
            } else false
        }

    fun showLoading() {
        if (activity != null && activity is BaseActivity) {
            (activity as BaseActivity?)!!.showLoading()
        }
    }

    fun hideLoading() {
        if (activity != null && activity is BaseActivity) {
            (activity as BaseActivity?)!!.hiddenLoading()
        }
    }

    fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}