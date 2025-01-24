import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.util.forEach
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.R
import com.example.testapp.common.util.MyUtil
import com.example.testapp.domain.model.FilterDynamicProperty
import com.example.testapp.domain.model.createAnnouncement.AnnouncementDynamicPropertyModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import uz.bisyor.corelib.common.extension.showSelectorDialog

class DynamicPropertiesAdapter(
    private val context: Context,
): RecyclerView.Adapter<DynamicPropertiesAdapter.MyHolderItem>() {
    private val items: MutableList<AnnouncementDynamicPropertyModel> = mutableListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DynamicPropertiesAdapter.MyHolderItem {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        when(viewType){
            1-> return MyHolderItem(
                inflater.inflate(
                    R.layout.item_dynamic_edittext_single,
                    parent,
                    false
                )
            )
            2-> return MyHolderItem(
                inflater.inflate(
                    R.layout.item_dynamic_edittext_multiple,
                    parent,
                    false
                )
            )
            4 -> return MyHolderItem(
                inflater.inflate(
                    R.layout.item_dynamic_checkbox,
                    parent,
                    false
                )
            )
            5 -> return MyHolderItem(
                inflater.inflate(
                    R.layout.item_dynamic_checkbox,
                    parent,
                    false
                )
            )
            6,8,9,11-> return MyHolderItem(
                inflater.inflate(
                    R.layout.item_dynamic_selector,
                    parent,
                    false
                )
            )
        }
        return MyHolderItem(
            inflater.inflate(
                R.layout.item_dynamic_edittext_single,
                parent,
                false
            )
        )
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateDynamicProperties(
        properties: List<AnnouncementDynamicPropertyModel>,
        //models:List<FilterDynamicProperty>
    ) {
        items.clear()
        items.addAll(properties)
        /*for (model in models) {
            for (item in items) {
                if (model.name == item.name) {
                    when (model.type) {
                        1, 2, 4, 5, 6, 9 -> item.lastSelectedValue = model.value ?: ""
                        8, 10, 11 -> item.lastSelectedValue = model.from + "," + model.to
                    }
                    break
                }
            }
        }*/
        notifyDataSetChanged()
    }
    fun getValues(): JsonObject? {
        for (item in items) {
            if (item.isRequired && item.lastSelectedValue.isEmpty()) {
                return null
            }
        }
        val p = JsonObject()
        for (item in items) {
            when (item.intType) {
                1, 2, 10 -> if (item.lastSelectedValue.isNotEmpty())
                    p.addProperty(item.name, item.lastSelectedValue)
                4, 5 -> if (item.lastSelectedValue.isNotEmpty())
                    p.addProperty(item.name, item.lastSelectedValue)
                6, 8, 11 -> if (item.lastSelectedValue.isNotEmpty())
                    p.addProperty(
                        item.name,
                        item.variants?.get(item.lastSelectedValue.toInt())?.first
                    )
                9 -> if (item.lastSelectedValueArray.isNullOrEmpty().not()) {
                    val keyList: Array<String> =
                        MyUtil.pairListKeys(item.variants!!)

                    var sum = 0
                    item.lastSelectedValueArray?.forEach {
                        sum += keyList[it].toInt()
                    }

                    p.addProperty(item.name, sum)
                }
            }
        }
        return p
    }

    override fun onBindViewHolder(viewHolder: DynamicPropertiesAdapter.MyHolderItem, position: Int) {
        if (viewHolder is MyHolderItem) {
            viewHolder.configureView(items[position])
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class MyHolderItem(var view: View) : RecyclerView.ViewHolder(view) {
        var text: String? = null

        fun configureView(model: AnnouncementDynamicPropertyModel) {
            val title: String = model.title
            val spannable: Spannable
            if (model.isRequired) {
                spannable = SpannableString("$title*")
                spannable.setSpan(
                    ForegroundColorSpan(Color.RED),
                    title.length,
                    title.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else spannable = SpannableString(title)

            when (model.intType) {
                1, 2, 10 -> {
                    val edLay: TextInputLayout = view.findViewById(R.id.ed_lay)
                    edLay.hint = spannable
                    val edText: TextInputEditText = view.findViewById(R.id.ed_text)
                    edText.addTextChangedListener(MyTextWatcher(items[bindingAdapterPosition]))
                    edText.setTextColor(Color.BLACK)
                    edText.setText(model.lastSelectedValue)
                }
                4, 5 -> {
                    val text: TextView = view.findViewById(R.id.text)
                    text.text = spannable
                    val checkBox: CheckBox = view.findViewById(R.id.checkbox)
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        model.lastSelectedValue = if (isChecked) "2" else "1"
                    }
                    //initial values
                    checkBox.isChecked = model.lastSelectedValue == "2"
                }
                6, 8, 11 -> {
                    val titleText: TextInputLayout = view.findViewById(R.id.title)
                    titleText.hint = spannable
                    val tv: TextInputEditText = view.findViewById(R.id.text)
                    val layout: ViewGroup = view.findViewById(R.id.clicker)
                    layout.setOnClickListener {
                        val item: AnnouncementDynamicPropertyModel = items[bindingAdapterPosition]
                        callSingleSelectableDialog(item, tv)
                    }

                    //initial values
                    if (model.lastSelectedValue.isNotBlank()) {
                        val charSequenceList: Array<String> =
                            MyUtil.pairListValues(model.variants!!)
                        tv.setText(charSequenceList[model.lastSelectedValue.toInt()])
                    } else {
                        tv.setText("")
                    }
                }
                9 -> {
                    val title9: TextInputLayout = view.findViewById(R.id.title)
                    title9.hint = spannable
                    val tv9: TextView = view.findViewById(R.id.text)
                    tv9.text = ""
                    val layout9: ViewGroup = view.findViewById(R.id.clicker)
                    layout9.setOnClickListener {
                        val item: AnnouncementDynamicPropertyModel = items[bindingAdapterPosition]
                        callMultipleSelectableDialog(item, tv9)
                    }
                    //initial values
                    if (model.lastSelectedValueArray.isNullOrEmpty().not()) {
                        val charSequenceList: Array<String> =
                            MyUtil.pairListValues(model.variants!!)

                        val textBuilder = StringBuilder()
                        model.lastSelectedValueArray?.forEach {
                            textBuilder.append((if (textBuilder.isEmpty()) "" else ", ") + charSequenceList[it])
                        }
                        tv9.text = textBuilder.toString()
                    }
                }
            }
        }
    }
    fun callSingleSelectableDialog(model: AnnouncementDynamicPropertyModel, tv: TextView) {
        val charSequenceList: Array<String> =
            MyUtil.pairListValues(model.variants!!)
        AlertDialog.Builder(context)
            .setTitle(model.title)
            .setItems(charSequenceList) { _, which ->
                tv.text = charSequenceList[which]
                model.lastSelectedValue = which.toString()
            }
            .show()
        /*context.showSelectorDialog(charSequenceList, model.title) { dialog, which ->
            dialog.dismiss()  // Dismiss the dialog
            tv.text = charSequenceList[which]  // Set the text of tv to the selected item
            model.lastSelectedValue = which.toString()  // Update the last selected value
        }*/





    }
    fun callMultipleSelectableDialog(model: AnnouncementDynamicPropertyModel, tv: TextView) {
        val charSequenceList: Array<String> =
            MyUtil.pairListValues(model.variants!!)
        // Utility function to show multiple item picker dialog with checkboxes
        fun Context.showMultiItemPickerDialog(
            items: Array<String>,
            title: String,
            selectedItems: BooleanArray?,
            onPositiveClick: (DialogInterface, Int) -> Unit
        ) {
            // Create a checked state array if not provided
            val selected = selectedItems ?: BooleanArray(items.size)

            AlertDialog.Builder(this)
                .setTitle(title) // Set dialog title
                .setMultiChoiceItems(items, selected) { _, which, isChecked ->
                    // Handle item selection
                    selected[which] = isChecked
                }
                .setPositiveButton("OK", onPositiveClick)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        context.showMultiItemPickerDialog(charSequenceList, model.title, null,) { d, _ ->
            d.dismiss()
            tv.text = ""

            val positions: SparseBooleanArray =
                (d as AlertDialog).listView.checkedItemPositions

            val list = mutableListOf<Int>()
            val textBuilder = StringBuilder()
            positions.forEach { key, value ->
                if (value) {
                    list.add(key)
                    textBuilder.append((if (textBuilder.isEmpty()) "" else ", ") + charSequenceList[key])
                }
            }
            tv.text = textBuilder.toString()
            model.lastSelectedValueArray = list
        }
    }
    internal inner class MyTextWatcher(val model: AnnouncementDynamicPropertyModel) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            model.lastSelectedValue = s.toString().trim { it <= ' ' }
        }
    }


}