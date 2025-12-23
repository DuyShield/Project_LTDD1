package com.example.do_anltdd1.UI_WareHouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.do_anltdd1.R

class Addwarehouse : Activity() {

    private lateinit var edtName: EditText
    private lateinit var edtCapacity: EditText
    private lateinit var edtNote: EditText
    private lateinit var spStatus: Spinner
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addwarehouse)
        setControl()
        setEvent()
    }
    private fun setControl(){
        edtName = findViewById(R.id.edtTenKho)
        spStatus = findViewById(R.id.spItem_Add)
        edtCapacity = findViewById(R.id.edtSoLuong_Add)
        edtNote = findViewById(R.id.edtGhiChu_Add)

        btnSubmit = findViewById(R.id.btnSubmitKho)
        btnCancel = findViewById(R.id.btnCancel)
    }
    private fun setEvent(){
        btnSubmit.setOnClickListener {

            val name = edtName.text.toString().trim()
            val status = spStatus.selectedItem.toString().trim()
            val capacityStr = edtCapacity.text.toString().trim()
            val note = edtNote.text.toString().trim()

            if (name.isEmpty() || status.isEmpty() || capacityStr.isEmpty() || note.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = Intent().apply {
                putExtra("name", name)
                putExtra("status", status)
                putExtra("capacity", capacityStr.toInt())
                putExtra("note", note)
            }

            setResult(Activity.RESULT_OK, result)
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}
