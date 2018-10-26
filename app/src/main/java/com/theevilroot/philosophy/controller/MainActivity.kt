package com.theevilroot.philosophy.controller

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.theevilroot.philosophy.*
import com.theevilroot.philosophy.model.TheHolder
import com.theevilroot.philosophy.model.loadTerms
import com.theevilroot.philosophy.view.TermsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    val adapter by lazy {
        TermsAdapter {
            currentTermTitle.text = it.term
            currentTermText.text = it.reference
            slidingView.expand()
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout()
        initLoading()
    }

    @SuppressLint("CheckResult")
    private fun initLoading() {
        loadTerms(this, URL("http://52.48.142.75/ph.txt"), "ph.json").subscribe({ term ->
            TheHolder.terms.add(term)
        }, {
            it.printStackTrace()
            AlertDialog.Builder(this).setMessage("File loading failed").setTitle("Sorry").setPositiveButton("Retry") { _, _ -> initLoading()}.create().show()
        }) {
            val items = TheHolder.terms.asSequence().sortedWith(Comparator { term1, term2 ->
                term1.term.compareTo(term2.term)
            }).toList()
            TheHolder.terms.clear()
            TheHolder.terms.addAll(items)
            adapter.setItems(TheHolder.terms)
        }

    }

    private fun setupLayout() {
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchField.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null || s.isBlank()) {
                    adapter.setItems(TheHolder.terms)
                    return
                }
                adapter.setItems(TheHolder.terms.filter { it.term.toLowerCase().startsWith(s.toString().toLowerCase()) })
            }
        })
    }

}
