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
import androidx.core.view.children
import com.keycome.twinkleschedule.R
import com.keycome.twinkleschedule.databinding.*
import com.keycome.twinkleschedule.record.span.Date
import com.keycome.twinkleschedule.record.span.Day
import com.keycome.twinkleschedule.record.span.Time

abstract class TemplateDialog(context: Context) : Dialog(context, R.style.TemplateDialog) {

    abstract val body: View
    private lateinit var binding: ViewDialogTemplateBinding
    private var positiveAction: PositiveAction? = null
    private var negativeAction: NegativeAction? = null
    private var neutralAction: NeutralAction? = null
    var autoDismiss: Boolean = true
    var hasTitle = true
    var hasActionButton = true
    var neutralActionButton = false
    var negativeText = "取消"
    var positiveText = "确定"
    var neutralText = "好"
    var titleText = "标题"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewDialogTemplateBinding.inflate(layoutInflater)
        setCanceledOnTouchOutside(false)
        initTemplate()
        setContentView(binding.root)
    }

    override fun show() {
        super.show()
        val lp: WindowManager.LayoutParams? = window?.attributes?.apply {
            width = (Resources.getSystem().displayMetrics.widthPixels * 0.85).toInt()
        }
        window?.attributes = lp
    }

    private fun initTemplate() {
        if (!hasTitle)
            binding.dialogHead.visibility = View.GONE
        else
            binding.dialogHead.text = titleText
        if (!hasActionButton)
            binding.dialogFoot.visibility = View.GONE
        else {
            if (neutralActionButton) {
                binding.dialogNegativeFrame.visibility = View.GONE
                binding.dialogPositiveText.text = neutralText
                binding.dialogPositiveText.setOnClickListener {
                    neutralAction?.action()
                    if (autoDismiss && isShowing) dismiss()
                }
            } else {
                binding.dialogPositiveText.text = positiveText
                binding.dialogNegativeText.text = negativeText
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
        binding.dialogBody.addView(body)
    }

    protected fun requireWidth(): Int {
        val metrics = Resources.getSystem().displayMetrics
        val w = (metrics.widthPixels * 0.85).toInt()
        val l = binding.root.paddingLeft
        val r = binding.root.paddingRight
        return w - l - r
    }

    fun onPositiveButtonPressed(action: PositiveAction) {
        this.positiveAction = action
    }

    fun onNegativeButtonPressed(action: NegativeAction) {
        this.negativeAction = action
    }

    fun onNeutralButtonPressed(action: NeutralAction) {
        this.neutralAction = action
    }

    fun interface PositiveAction {
        fun action()
    }

    fun interface NegativeAction {
        fun action()
    }

    fun interface NeutralAction {
        fun action()
    }
}

class TextDialog(context: Context, block: (TextDialog.() -> Unit)? = null) :
    TemplateDialog(context) {

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
    TemplateDialog(context) {

    private var textChangedAction: TextChangedAction? = null
    var textContent: String? = null

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
                    textContent = s.toString()
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
    TemplateDialog(context) {

    private val b: CustomDatePickerLayoutBinding =
        CustomDatePickerLayoutBinding.inflate(layoutInflater)
    var datePickerPosition = "2021-06-05"
    val currentDate: Date
        get() {
            val stringList = b.datePickerView.dateString.split("-")
            return Date(
                stringList[0].toInt(),
                stringList[1].toInt(),
                stringList[2].toInt()
            )
        }

    override val body: View
        get() {
            b.datePickerView.setDate("1970-01-01", "2100-01-01", datePickerPosition)
            return b.root
        }

    init {
        hasTitle = false
        block?.invoke(this)
    }

}

class TimePickerDialog(context: Context, block: (TimePickerDialog.() -> Unit)? = null) :
    TemplateDialog(context) {

    private val b = CustomTimePickerLayoutBinding.inflate(layoutInflater)
    var timePickerPosition = arrayOf(15, 24)

    override val body: View
        get() {
            val list = mutableListOf<String>()
            (0..59).forEach {
                list.add(if (it < 10) "0$it" else it.toString())
            }
            b.timePickerOfHour.setData(list.subList(0, 24), timePickerPosition[0])
            b.timePickerOfMinute.setData(list, timePickerPosition[1])
            return b.root
        }

    val currentTime: Time
        get() = Time(
            b.timePickerOfHour.currentValue.toInt(),
            b.timePickerOfMinute.currentValue.toInt(),
            0
        )

    init {
        hasTitle = false
        block?.invoke(this)
    }

}

class WheelDialog(
    context: Context,
    val list: List<String>,
    block: (WheelDialog.() -> Unit)? = null
) : TemplateDialog(context) {

    private val b = CustomWheelListLayoutBinding.inflate(layoutInflater)
    var position = 0
    val currentValue: String get() = b.wheelDialogPicker.currentValue

    override val body: View
        get() {
            b.wheelDialogPicker.setData(list, position)
            return b.root
        }

    init {
        hasTitle = false
        block?.invoke(this)
    }
}

class EndDayDialog(
    context: Context,
    block: (EndDayDialog.() -> Unit)? = null
) : TemplateDialog(context) {

    private val b = ViewEndDayPickerBinding.inflate(layoutInflater)
    var day: Day = Day.Friday
        private set
    private var action: OnItemSelected? = null

    override val body: View
        get() {
            return b.root
        }

    init {
        for (v in b.root.children) {
            v.setOnClickListener {
                when (v) {
                    b.endDayFriday -> day = Day.Friday
                    b.endDaySaturday -> day = Day.Saturday
                    b.endDaySunday -> day = Day.Sunday
                }
                dismiss()
                action?.onItemSelected(day)
            }
        }
        hasActionButton = false
        block?.invoke(this)
    }

    fun onItemSelected(action: OnItemSelected) {
        this.action = action
    }

    fun interface OnItemSelected {
        fun onItemSelected(day: Day)
    }
}