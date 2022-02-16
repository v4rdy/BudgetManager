package com.vp.budgetmanager

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.vp.budgetmanager.databinding.ActivityNewTransactionBinding
import com.vp.budgetmanager.databinding.FragmentNewTransactionBinding

class NewTransactionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var amount: TextInputEditText
    private lateinit var spinner: Spinner
    private lateinit var add: Button
    var type: String = ""
    var money: Int = 0

    private lateinit var binding: FragmentNewTransactionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.add.setOnClickListener {
            val replyIntent = Intent()
            if(TextUtils.isEmpty(amount.text)  ||  amount.text.toString().toInt()>Int.MAX_VALUE){
                setResult(RESULT_CANCELED,replyIntent)
            }else{
                replyIntent.putExtra("Amount",amount.text.toString().toInt())
                replyIntent.putExtra("Type",type)
                setResult(RESULT_OK,replyIntent)
            }
            finish()
        }
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b

        initialize()



    }

    private fun initialize () {
        amount = findViewById(R.id.amount_input)
        add = findViewById(R.id.add_button)
        spinner = findViewById(R.id.dropdown_menu)
        ArrayAdapter
            .createFromResource(this,R.array.spinner_options, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
            .also { adapter ->
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        type = parent?.getItemAtPosition(position) as String
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}