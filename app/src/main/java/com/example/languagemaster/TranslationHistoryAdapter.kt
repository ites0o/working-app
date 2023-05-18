package com.example.languagemaster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat

class TranslationHistoryAdapter(
    context: Context,
    private val resource: Int,
    objects: MutableList<TranslationHistory>
) : ArrayAdapter<TranslationHistory>(context, resource, objects) {

    private val selectedItems = HashSet<Int>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, parent, false)
        }
        val inputTextView = view?.findViewById<TextView>(R.id.input_text_view)
        val outputTextView = view?.findViewById<TextView>(R.id.output_text_view)
        val timestampTextView = view?.findViewById<TextView>(R.id.timestamp_text_view)
        val shareButton = view?.findViewById<Button>(R.id.share_selected_button)
        val checkBox = view?.findViewById<CheckBox>(R.id.checkbox)

        val item = getItem(position)
        inputTextView?.text = item?.input ?: ""
        outputTextView?.text = item?.output ?: ""
        timestampTextView?.text = item?.timestamp ?: ""

        checkBox?.setOnCheckedChangeListener { _, isChecked ->
            toggleSelection(position, isChecked)
        }
        checkBox?.isChecked = selectedItems.contains(position)

        shareButton?.setOnClickListener {
            val output = item?.output ?: ""
            shareText(output)
        }

        return view!!
    }

    fun toggleSelection(position: Int, isChecked: Boolean) {
        if (isChecked) {
            selectedItems.add(position)
        } else {
            selectedItems.remove(position)
        }
    }

    fun getSelectedTranslations(): List<TranslationHistory> {
        val selectedTranslations = mutableListOf<TranslationHistory>()
        for (position in selectedItems) {
            getItem(position)?.let { selectedTranslations.add(it) }
        }
        return selectedTranslations
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    private fun shareText(text: String) {
        val shareIntent = ShareCompat.IntentBuilder.from(context as AppCompatActivity)
            .setType("text/plain")
            .setText(text)
            .setChooserTitle("Share via")
            .createChooserIntent()

        if (shareIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(shareIntent)
        }
    }
}
