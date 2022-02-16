package com.vp.budgetmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vp.budgetmanager.model.Transaction
import com.vp.budgetmanager.viewmodel.TransactionViewModel
import com.vp.budgetmanager.viewmodel.TransactionViewModelFactory

import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var overview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==newTransactionActivityRequestCode  &&  resultCode== RESULT_OK){
            var a:Int?
            var t:String?
            data?.getIntExtra("Amount",-1).let {
                a = it
            }
            data?.getStringExtra("Type").let {
                t = it
            }

            if(a!=null  &&  t!=null){
                val date = System.currentTimeMillis()
                val transaction = Transaction(amount = a!!,type = t!!,time = date)
                transactionViewModel.insert(transaction)
            }else{
                Toast.makeText(this,"Transaction NULL",Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(this,"Transaction Failed",Toast.LENGTH_LONG).show()
        }
    }

}