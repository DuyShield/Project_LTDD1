package com.example.do_anltdd1.UI_WareHouse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.do_anltdd1.R

class Updatewarehouse : Activity() {

    private lateinit var edtTenKho: EditText
    private lateinit var edtSoLuong: EditText
    private lateinit var edtGhiChu: EditText
    private lateinit var spTrangThai: Spinner
    private lateinit var btnSaveChange: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.updatewarehouse)
        setControl()
        setEvent()
    }
    private fun setControl(){
        // Bind view
        edtTenKho = findViewById(R.id.edtTenKho)
        edtSoLuong = findViewById(R.id.edtSoLuong)
        spTrangThai = findViewById(R.id.spItem_Update)
        edtGhiChu = findViewById(R.id.edtGhiChu)

        btnSaveChange = findViewById(R.id.btnSaveChange)
        btnCancel = findViewById(R.id.btnCancel)
    }
    private fun setEvent(){
        val intent = intent

        // Nhận dữ liệu từ Intent
        val tenKho = intent.getStringExtra("name") ?: ""
        val soLuong = intent.getIntExtra("capacity", 0)
        val trangThai = intent.getStringExtra("status")
        val ghiChu = intent.getStringExtra("note") ?: ""

        edtTenKho.setText(tenKho)
        edtSoLuong.setText(soLuong.toString())
        edtGhiChu.setText(ghiChu)

        // Set spinner theo trạng thái
        trangThai?.let {
            for (i in 0 until spTrangThai.count) {
                if (spTrangThai.getItemAtPosition(i).toString() == it) {
                    spTrangThai.setSelection(i)
                    break
                }
            }
        }

        // Lưu thay đổi
        btnSaveChange.setOnClickListener {

            val newTenKho = edtTenKho.text.toString().trim()
            val soLuongStr = edtSoLuong.text.toString().trim()
            val newTrangThai = spTrangThai.selectedItem.toString()
            val newGhiChu = edtGhiChu.text.toString().trim()

            if (newTenKho.isEmpty() || soLuongStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newSoLuong = soLuongStr.toInt()

            // Trả dữ liệu về Formwarehouse
            val result = Intent().apply {
                putExtra("id", intent.getIntExtra("id", -1))
                putExtra("name", newTenKho)
                putExtra("capacity", newSoLuong)
                putExtra("status", newTrangThai)
                putExtra("note", newGhiChu)
            }

            setResult(RESULT_OK, result)
            Toast.makeText(this, "Cập nhật kho thành công!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Hủy
        btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
