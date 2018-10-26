package com.theevilroot.philosophy.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.theevilroot.philosophy.R
import com.theevilroot.philosophy.model.Term
import kotlinx.android.synthetic.main.term_item_layout.view.*


class TermsAdapter(val onClick: (Term) -> Unit): RecyclerView.Adapter<TermsAdapter.TermsHolder>() {
    private val items = ArrayList<Term>()
    fun setItems(list: List<Term>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermsHolder =
            TermsHolder(LayoutInflater.from(parent.context).inflate(R.layout.term_item_layout, parent, false))

    override fun getItemCount(): Int =
            items.size
    override fun onBindViewHolder(holder: TermsHolder, position: Int) =
            holder.bind(items[position], onClick)


    class TermsHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(term: Term, onClick: (Term) -> Unit) {
            with(itemView) {
                termName.text = "${term.term} -"
                termReference.text = term.reference
                setOnLongClickListener {
                    onClick(term)
                    true
                }
            }
        }
    }
}