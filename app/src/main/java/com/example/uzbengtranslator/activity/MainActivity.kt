package com.example.uzbengtranslator.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.uzbengtranslator.R
import com.example.uzbengtranslator.adapter.SavedWordAdapter
import com.example.uzbengtranslator.database.SavedWordDatabase
import com.example.uzbengtranslator.databinding.ActivityMainBinding
import com.example.uzbengtranslator.helper.SimpleItemTouchHelperCallBack
import com.example.uzbengtranslator.model.SavedWord
import com.example.uzbengtranslator.networking.ApiClient
import com.example.uzbengtranslator.utils.UiStateObject
import com.example.uzbengtranslator.viewmodel.MainViewModel
import com.example.uzbengtranslator.viewmodel.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    private var job: Job? = null
    private var isUzWordEntered = false
    private lateinit var savedWordDatabase: SavedWordDatabase
    lateinit var adapter: SavedWordAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //changing status bar text to light mode text color(black)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setUpViewModel()
        initDatabase()
        initViews()
        setUpLanguageDetectionObserver()
        setUpTranslationObserver()
    }

    private fun initDatabase() {
        savedWordDatabase = SavedWordDatabase.getInstance(this)
    }

    private fun setUpTranslationObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.translation.collect {
                when (it) {
                    UiStateObject.LOADING -> {
                        //show progress
                    }

                    is UiStateObject.SUCCESS -> {
                        binding.tvTranslation.text = it.data.data.translations[0].translatedText
                    }
                    is UiStateObject.ERROR -> {
                        Log.d("TAG", "setupUI: ${it.message}")
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun setUpLanguageDetectionObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.language.collect {
                when (it) {
                    UiStateObject.LOADING -> {
                        //show progress
                    }

                    is UiStateObject.SUCCESS -> {
                        isUzWordEntered = if (
                            it.data.data.detections[0][0].language == getString(
                                R.string.uz
                            )
                        ) {
                            viewModel.translate(
                                binding.edtText.text.toString(),
                                getString(R.string.uz),
                                getString(R.string.en)
                            )
                            true
                        } else {
                            false
                        }
                    }
                    is UiStateObject.ERROR -> {
                        Log.d("TAG", "setupUI: ${it.message}")
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiClient.translateService)
        )[MainViewModel::class.java]
    }

    private fun initViews() {
        binding.edtText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(textInput: Editable?) {
                job?.cancel()
                binding.btnSave.isEnabled = true
                job = lifecycleScope.launchWhenStarted {
                    delay(400)
                    if (textInput!!.isNotEmpty()) {
                        viewModel.detectLanguage(
                            textInput.toString()
                        )
                        binding.btnSave.isEnabled = true
                    } else {
                        binding.btnSave.isEnabled = false
                        binding.tvTranslation.text = ""
                    }
                }
            }
        })

        binding.btnSave.setOnClickListener {
            val uz = binding.edtText.text.toString()
            val en = binding.tvTranslation.text.toString()
            if (uz.isNotEmpty() && en.isNotEmpty()) {
                if (isUzWordEntered) {
                    val wordToSave = SavedWord(uzWord = uz, enWord = en)
                    savedWordDatabase.savedWordDao().saveWord(wordToSave)
                    adapter.submitData(getSavedWords())
                } else {
                    Toast.makeText(this, getString(R.string.str_loading_word), Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.btnSave.isEnabled = false
        }

        binding.apply {
            ivChangeLanguage.setOnClickListener {
                val lang = tvTranslateFrom.text
                tvTranslateFrom.text = tvTranslateTo.text
                tvTranslateTo.text = lang
            }
        }

        refreshAdapter()
    }

    private fun refreshAdapter() {
        adapter = SavedWordAdapter { wordID ->
            savedWordDatabase.savedWordDao().removeFromDatabase(wordID)
        }
        adapter.submitData(getSavedWords())
        binding.rvSavedWords.adapter = adapter

        setSwipeAndDragDrop(adapter)
    }

    private fun setSwipeAndDragDrop(adapter: SavedWordAdapter) {
        //drag and drop,as well as swipe to delete actions
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallBack(adapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvSavedWords)
    }

    private fun getSavedWords(): ArrayList<SavedWord> =
        savedWordDatabase.savedWordDao().getSavedWords() as ArrayList<SavedWord>


    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}