package com.keycome.twinkleschedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.keycome.twinkleschedule.databinding.CustomToolbarLayoutBinding

class Base

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, javaClass.simpleName)
    }

    companion object {
        private const val TAG = "BaseActivity"
    }
}

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = supportBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arrayOf(0)
        val toolbar = supportToolbar(title)
        toolbar?.run {
            customToolbarLeading.setOnClickListener {
                requireActivity().onBackPressed()
            }
            customToolbarTitle.setText(title[0])
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun supportBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected abstract fun supportToolbar(title: Array<Int>): CustomToolbarLayoutBinding?
}