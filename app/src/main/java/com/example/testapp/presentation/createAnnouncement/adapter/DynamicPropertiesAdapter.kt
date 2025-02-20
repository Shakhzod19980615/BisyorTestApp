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
import android.util.Log
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
import com.example.testapp.domain.model.createAnnouncement.AnnouncementDynamicPropertyModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import uz.bisyor.corelib.common.extension.showSelectorDialog

class DynamicPropertiesAdapter(private val context: Context) : RecyclerView.Adapter<DynamicPropertiesAdapter.MyHolderItem>() {

    private val items: MutableList<AnnouncementDynamicPropertyModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderItem {
        val inflater = LayoutInflater.from(context)
        val layoutId = when (viewType) {
            1 -> R.layout.item_dynamic_edittext_single
            2 -> R.layout.item_dynamic_edittext_multiple
            4, 5 -> R.layout.item_dynamic_checkbox
            6, 8, 9, 11 -> R.layout.item_dynamic_selector
            else -> R.layout.item_dynamic_edittext_single
        }
        return MyHolderItem(inflater.inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(viewHolder: MyHolderItem, position: Int) {
        val item = items[position]
        Log.d("AdapterDebug", "Binding item at position $position: ${item.title}")
        viewHolder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].intType
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateDynamicProperties(properties: List<AnnouncementDynamicPropertyModel>) {
        Log.d("DynamicPropertiesAdapter", "Updating with ${properties.size} items")
        items.clear()
        items.addAll(properties)
        notifyDataSetChanged()
    }

    fun getValues(): JsonObject? {
        val jsonObject = JsonObject()
        for (item in items) {
            if (item.isRequired && item.lastSelectedValue.isEmpty()) return null

            when (item.intType) {
                1, 2, 10, 4, 5 -> if (item.lastSelectedValue.isNotEmpty()) jsonObject.addProperty(item.name, item.lastSelectedValue)
                6, 8, 11 -> item.lastSelectedValue.toIntOrNull()?.let { jsonObject.addProperty(item.name, item.variants?.get(it)?.first) }
                9 -> {
                    item.lastSelectedValueArray?.takeIf { it.isNotEmpty() }?.let { selectedValues ->
                        val sum = selectedValues.sumOf { key -> MyUtil.pairListKeys(item.variants!!)[key].toInt() }
                        jsonObject.addProperty(item.name, sum)
                    }
                }
            }
        }
        return jsonObject
    }

    override fun onViewRecycled(holder: MyHolderItem) {
        super.onViewRecycled(holder)
        holder.itemView.findViewById<TextInputEditText>(R.id.ed_text)?.setText("")
    }

    inner class MyHolderItem(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(model: AnnouncementDynamicPropertyModel) {
            Log.d("ConfigureViewDebug", "Configuring view for: ${model.title}")
            configureView(model)
        }

        private fun configureView(model: AnnouncementDynamicPropertyModel) {
            val spannable = SpannableString(model.title + if (model.isRequired) "*" else "").apply {
                if (model.isRequired) setSpan(ForegroundColorSpan(Color.RED), model.title.length, model.title.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            when (model.intType) {
                1, 2, 10 -> setupEditText(model, spannable)
                4, 5 -> setupCheckbox(model, spannable)
                6, 8, 11 -> setupSingleChoiceSelector(model, spannable)
                9 -> setupMultiChoiceSelector(model, spannable)
            }
        }

        private fun setupEditText(model: AnnouncementDynamicPropertyModel, spannable: Spannable) {
            val edLayout: TextInputLayout = itemView.findViewById(R.id.ed_lay)
            val edText: TextInputEditText = itemView.findViewById(R.id.ed_text)
            edLayout.hint = spannable
            edText.apply {
                addTextChangedListener(MyTextWatcher(model))
                setTextColor(Color.BLACK)
                setText(model.lastSelectedValue)
            }
        }

        private fun setupCheckbox(model: AnnouncementDynamicPropertyModel, spannable: Spannable) {
            val text: TextView = itemView.findViewById(R.id.text)
            val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
            text.text = spannable
            checkBox.apply {
                setOnCheckedChangeListener { _, isChecked -> model.lastSelectedValue = if (isChecked) "2" else "1" }
                isChecked = model.lastSelectedValue == "2"
            }
        }

        private fun setupSingleChoiceSelector(model: AnnouncementDynamicPropertyModel, spannable: Spannable) {
            val title: TextInputLayout = itemView.findViewById(R.id.title)
            val textView: TextInputEditText = itemView.findViewById(R.id.text)
            val layout: ViewGroup = itemView.findViewById(R.id.clicker)
            title.hint = spannable
            layout.setOnClickListener { showSingleSelectableDialog(model, textView) }
            textView.setText(model.variants?.get(model.lastSelectedValue.toIntOrNull() ?: -1)?.second ?: "")
        }

        private fun setupMultiChoiceSelector(model: AnnouncementDynamicPropertyModel, spannable: Spannable) {
            val title: TextInputLayout = itemView.findViewById(R.id.title)
            val textView: TextView = itemView.findViewById(R.id.text)
            val layout: ViewGroup = itemView.findViewById(R.id.clicker)
            title.hint = spannable
            layout.setOnClickListener { callMultipleSelectableDialog(model, textView) }
            textView.text = model.lastSelectedValueArray?.joinToString(", ") { model.variants?.get(it)?.second ?: "" } ?: ""
        }
    }

    private fun showSingleSelectableDialog(model: AnnouncementDynamicPropertyModel, textView: TextView) {
        val items = model.variants?.map { it.second }?.toTypedArray() ?: return
        AlertDialog.Builder(context)
            .setTitle(model.title)
            .setItems(items) { _, which ->
                textView.text = items[which]
                model.lastSelectedValue = which.toString()
            }
            .show()
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
    }
    private inner class MyTextWatcher(val model: AnnouncementDynamicPropertyModel) : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            model.lastSelectedValue = s.toString().trim()
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }
}
