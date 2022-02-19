package com.vp.budgetmanager.views.utils
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vp.budgetmanager.R
import com.vp.budgetmanager.TransactionApplication
import com.vp.budgetmanager.model.Transaction
import com.vp.budgetmanager.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewAdapter : ListAdapter<Transaction, RecyclerViewAdapter.TransactionViewHolder>(
    WORDS_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder.create(parent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    @SuppressLint("SimpleDateFormat")
    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amount: TextView = itemView.findViewById(R.id.amount_type)
        private val time: TextView = itemView.findViewById(R.id.time)
        private val name: TextView = itemView.findViewById(R.id.transaction_name)
        private val delete: ImageView = itemView.findViewById(R.id.delete)
        private val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)

        fun bind(transaction: Transaction?) {
            val amountAndType = transaction!!.amount.toString() + " zł, " + transaction.type
            name.text = transaction.name.toString()
            amount.text = amountAndType
            val format = SimpleDateFormat("dd/MM/yyyy hh:mm")
            val cal = Calendar.getInstance()
            cal.timeInMillis = transaction.time
            this.time.text = format.format(cal.time)

            val color = itemView.resources.getColor(R.color.primary_light_green)
            linearLayout.setBackgroundColor(color)

            delete.setOnClickListener {
                val dialog = AlertDialog.Builder(itemView.context)
                    .setTitle("Usunąć koszt?")
                    .setMessage("Czy chcesz usunąć wybrany koszt?")
                    .setPositiveButton("Tak"){ _, _ ->
                        val transactionViewModel = TransactionViewModel(TransactionApplication().repository)
                        transactionViewModel.delete(transaction = transaction)
                    }

                    .setNegativeButton("Nie"){ dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                dialog.show()
            }

        }

        companion object {
            fun create(parent: ViewGroup): TransactionViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.transaction_item, parent, false)
                return TransactionViewHolder(view)
            }
        }
    }

    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}