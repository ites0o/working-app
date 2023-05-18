package com.example.languagemaster

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ShareCompat
import com.example.languagemaster.R
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TranslateActivity : AppCompatActivity() {
    private lateinit var mInputText: EditText
    private lateinit var mOutputText: TextView
    private lateinit var mTranslateButton: Button
    private lateinit var dbHelper: TranslateDBHelper
    private lateinit var db: SQLiteDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var mShareSelectedButton: Button
    private lateinit var historyAdapter: TranslationHistoryAdapter
    private lateinit var historyListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        mInputText = findViewById(R.id.input_text)
        mOutputText = findViewById(R.id.output_text)
        mTranslateButton = findViewById(R.id.translate_button)

        dbHelper = TranslateDBHelper(this)
        db = dbHelper.writableDatabase
        auth = FirebaseAuth.getInstance()

        mTranslateButton.setOnClickListener {
            val input = mInputText.text.toString()
            if (input.isEmpty()) {
                mOutputText.text = ""
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid
            if (userId != null) {
                GlobalScope.launch(Dispatchers.Main) {
                    val options = TranslateOptions.newBuilder()
                        .setApiKey("AIzaSyA5NnkS50R3mxJOD501OThFbA4BAqw_Zh8")
                        .build()
                    val translate = options.service
                    val result = withContext(Dispatchers.IO) {
                        val translation = translate.translate(input, Translate.TranslateOption.targetLanguage("zh-CN"))
                        translation.translatedText
                    }

                    val values = ContentValues().apply {
                        put(TranslateDBHelper.COLUMN_USER_ID, userId)
                        put(TranslateDBHelper.COLUMN_INPUT, input)
                        put(TranslateDBHelper.COLUMN_OUTPUT, result)
                    }

                    db.insert(TranslateDBHelper.TABLE_NAME, null, values)

                    mOutputText.text = result

                    loadHistory(userId)
                }
            }
        }



        mShareSelectedButton = findViewById(R.id.share_selected_button)
        mShareSelectedButton.setOnClickListener {
            val selectedTranslations = historyAdapter.getSelectedTranslations()
            val selectedOutput = selectedTranslations.joinToString("\n\n") { it.output }
            shareText(selectedOutput)
        }

        historyListView = findViewById(R.id.history_list)
        val userId = auth.currentUser?.uid
        if (userId != null) {
            loadHistory(userId)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadHistory(userId: String) {
        val cursor = db.query(
            TranslateDBHelper.TABLE_NAME,
            null,
            "${TranslateDBHelper.COLUMN_USER_ID} = ?",
            arrayOf(userId),
            null,
            null,
            "${TranslateDBHelper.COLUMN_TIMESTAMP} DESC",
            null
        )

        val historyList = mutableListOf<TranslationHistory>()

        cursor?.use {
            while (it.moveToNext()) {
                val input = it.getString(it.getColumnIndexOrThrow(TranslateDBHelper.COLUMN_INPUT))
                val output = it.getString(it.getColumnIndexOrThrow(TranslateDBHelper.COLUMN_OUTPUT))
                val timestamp = it.getString(it.getColumnIndexOrThrow(TranslateDBHelper.COLUMN_TIMESTAMP))

                historyList.add(TranslationHistory(input, output, timestamp))
            }
        }

        historyAdapter = TranslationHistoryAdapter(this, R.layout.history_list_item, historyList)
        historyListView.adapter = historyAdapter
        historyListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        historyListView.setOnItemClickListener { _, _, position, _ ->
            historyListView.setItemChecked(position, !historyListView.isItemChecked(position))
            updateShareSelectedButtonVisibility(historyListView.checkedItemCount > 0)
        }
        historyListView.setOnItemClickListener { _, _, position, _ ->
            val checked = !historyListView.isItemChecked(position)
            historyListView.setItemChecked(position, checked)
            updateShareSelectedButtonVisibility(historyListView.checkedItemCount > 0)
        }

    }


    private fun updateShareSelectedButtonVisibility(visible: Boolean) {
        mShareSelectedButton.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun shareText(text: String) {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setText(text)
            .setChooserTitle("Share via")
            .createChooserIntent()

        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(shareIntent)
        }
    }
}

data class TranslationHistory(val input: String, val output: String, val timestamp: String)
