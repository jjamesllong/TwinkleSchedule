package com.keycome.twinkleschedule.custom

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.CustomDialogLayoutBinding
import com.keycome.twinkleschedule.databinding.DatePickerLayoutBinding
import com.keycome.twinkleschedule.databinding.TimePickerLayoutBinding
import com.keycome.twinkleschedule.databinding.WheelListLayoutBinding

abstract class CustomDialog(context: Context) : Dialog(context, R.style.CustomDialog), BodyView {

    private lateinit var binding: CustomDialogLayoutBinding
    private var positiveAction: PositiveAction? = null
    private var negativeAction: NegativeAction? = null
    private var singleAction: SingleAction? = null
    var autoDismiss: Boolean = true
    var hasTitle = true
    var hasActionButton = true
    var singleActionButton = false
    var negativeText = "取消"
    var positiveText = "确定"
    var singleText = "好"
    var titleText = "标题"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        initStyle()
        initEvent()
    }

    override fun show() {
        super.show()
        val lp: WindowManager.LayoutParams? = window?.attributes?.apply {
            width = (Resources.getSystem().displayMetrics.widthPixels * 0.85).toInt()
        }
        window?.attributes = lp
    }

    private fun initStyle() {
        if (!hasTitle) binding.dialogHead.visibility = View.GONE
        if (!hasActionButton) binding.dialogFoot.visibility = View.GONE
        if (singleActionButton) {
            if (hasActionButton) binding.dialogNegativeFrame.visibility = View.GONE
            else throw IllegalArgumentException("this dialog has been defined to no action buttons")
        }
        binding.dialogBody.addView(body)

        binding.run {
            dialogHead.text = titleText
            if (singleActionButton) dialogPositiveText.text = singleText
            else {
                dialogPositiveText.text = positiveText
                dialogNegativeText.text = negativeText
            }
        }
    }

    private fun initEvent() {

        if (singleActionButton) {
            binding.dialogPositiveText.setOnClickListener {
                singleAction?.action()
                if (autoDismiss && isShowing) dismiss()
            }
        } else {
            binding.dialogPositiveText.setOnClickListener {
                positiveAction?.action()
                if (autoDismiss && isShowing) dismiss()
            }

            binding.dialogNegativeText.setOnClickListener {
                negativeAction?.action()
                if (autoDismiss && isShowing) dismiss()
            }
        }
    }

    fun onPositiveButtonPressed(action: PositiveAction) {
        this.positiveAction = action
    }

    fun onNegativeButtonPressed(action: NegativeAction) {
        this.negativeAction = action
    }

    fun onSingleButtonPressed(action: SingleAction) {
        this.singleAction = action
    }

    fun interface PositiveAction {
        fun action()
    }

    fun interface NegativeAction {
        fun action()
    }

    fun interface SingleAction {
        fun action()
    }
}

interface BodyView {
    val body: View
}

class TextDialog(context: Context, block: (TextDialog.() -> Unit)? = null) : CustomDialog(context) {

    var content: String = ""

    override val body: View
        get() = TextView(context).apply {
            text = content
            setTextColor(resources.getColor(R.color.gray, null))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            gravity = Gravity.CENTER
        }

    init {
        block?.invoke(this)
    }
}

class EditTextDialog(context: Context, block: (EditTextDialog.() -> Unit)? = null) :
    CustomDialog(context) {

    private var textChangedAction: TextChangedAction? = null

    override val body: View
        get() = EditText(context).apply {
            setBackgroundResource(R.drawable.bg_edit_text_background)
            setEms(14)
            setSingleLine()
            inputType = InputType.TYPE_CLASS_TEXT
            gravity = Gravity.CENTER
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    textChangedAction?.action(s.toString())
                }
            })
        }

    init {
        block?.invoke(this)
    }

    fun onTextChanged(action: TextChangedAction) {
        this.textChangedAction = action
    }

    fun interface TextChangedAction {
        fun action(content: String)
    }
}

class DatePickerDialog(context: Context, block: (DatePickerDialog.() -> Unit)? = null) :
    CustomDialog(context) {

    var datePickerPosition = "2021-06-05"

    override val body: View
        get() {
            val b = DatePickerLayoutBinding.inflate(layoutInflater)
            b.datePickerView.setDate("1970-01-01", "2100-01-01", datePickerPosition)
            return b.root
        }

    init {
        hasTitle = false
        singleActionButton = true
        singleText = "确定"
        block?.invoke(this)
    }

}

class TimePickerDialog(context: Context, block: (TimePickerDialog.() -> Unit)? = null) :
    CustomDialog(context) {

    var timePickerPosition = arrayOf(15, 24)

    override val body: View
        get() {
            val b = TimePickerLayoutBinding.inflate(layoutInflater)
            val list = mutableListOf<String>()
            (0..59).forEach {
                list.add(if (it < 10) "0$it" else it.toString())
            }
            b.timePickerOfHour.setData(list.subList(0, 24), timePickerPosition[0])
            b.timePickerOfMinute.setData(list, timePickerPosition[1])
            return b.root
        }

    init {
        hasTitle = false
        singleActionButton = true
        singleText = "确定"
        block?.invoke(this)
    }

}

class WheelDialog(context: Context, block: (WheelDialog.() -> Unit)? = null) :
    CustomDialog(context) {
    override val body: View
        get() {
            val b = WheelListLayoutBinding.inflate(layoutInflater)
            val list = mutableListOf<String>()
            (0..59).forEach {
                list.add(if (it < 10) "0$it" else it.toString())
            }
            b.wheelDialogPicker.setData(list, 20)
            return b.root
        }

    init {
        hasTitle = false
        block?.invoke(this)
    }
}