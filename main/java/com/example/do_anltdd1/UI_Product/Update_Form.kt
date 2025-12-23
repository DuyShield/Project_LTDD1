package com.example.do_anltdd1.UI_Product

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.R
import com.example.do_anltdd1.Database.SanPham
import com.example.do_anltdd1.Database.SanPhamDao
import com.example.do_anltdd1.Database.Warehouse
import com.example.do_anltdd1.Database.WarehouseDao

class Update_Form : AppCompatActivity() {
    private lateinit var btnUpdate : Button
    private lateinit var btnCancel : Button
    lateinit var tvId : TextView
    lateinit var edtName : EditText
    lateinit var edtSL : EditText
    lateinit var ivImg : ImageView
    lateinit var spStatus : Spinner
    private var position: Int = -1
    //    private var selectedImageUri: Uri ?= null
//
//    // Bộ chọn ảnh
//    private val pickImage = registerForActivityResult(
//        ActivityResultContracts.GetContent()
//    ) { uri ->
//        if (uri != null) {
//            selectedImageUri = uri
//            // Hiển thị ảnh ngay vào ImageView
//            ivImg.setImageURI(uri)
//        }
//    }
    lateinit var db: AppDB
    lateinit var productDao: SanPhamDao
    lateinit var spKho: Spinner
    lateinit var warehouseDao: WarehouseDao
    lateinit var warehouseList: List<Warehouse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.update)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.update)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setControl()
        db = AppDB.Companion.get(this)
        productDao = db.daoSP()
        warehouseDao = db.daoWH()
        loadWarehouseSpinner()
        setEvent()
    }
    private fun setControl() {
        tvId = findViewById(R.id.tvID)
        edtName = findViewById(R.id.edtName)
        edtSL = findViewById(R.id.edtQuan)
        spStatus = findViewById(R.id.spStatusUp)
        ivImg = findViewById(R.id.ivImg_Update)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnCancel = findViewById(R.id.btnCancelUp)
        spKho = findViewById(R.id.spKhoUp)
        // Nhận dữ liệu từ Intent
        position = intent.getIntExtra("position", -1)
        tvId.setText(intent.getStringExtra("id"))
        edtName.setText(intent.getStringExtra("name"))
        edtSL.setText(intent.getIntExtra("quantity", 0).toString())
        val status = intent.getStringExtra("status")
        val adapter = spStatus.adapter
        if (status != null) {
            for (i in 0 until adapter.count) {
                if (adapter.getItem(i).toString() == status) {
                    spStatus.setSelection(i)
                    break
                }
            }
        }
    }
    private fun setEvent() {
        // Event
        // Mở thư viện ảnh
//        ivImg.setOnClickListener {
//            pickImage.launch("image/*")
//        }
        // Update
        btnUpdate.setOnClickListener {
            val id = tvId.text.toString().trim()
            val name = edtName.text.toString()
            val quantity = edtSL.text.toString().trim()
            val status = spStatus.selectedItem.toString().trim()
            val selectedIndexWH = spKho.selectedItemPosition
            val selectedIdWH = warehouseList[selectedIndexWH].id
            if(name.isEmpty()){
                Toast.makeText(this,"Tên không được để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(quantity.isEmpty()){
                Toast.makeText(this,"Số lượng không được để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Kiểm tra số lượng là số
            val num = quantity.toIntOrNull()
            if (num == null) {
                Toast.makeText(this, "Số lượng phải là số!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Cập nhật sản phẩm
            if (position != -1) {
                val update = SanPham(id, name, num, status, null, selectedIdWH)
                db.daoSP().update(update)
            }
            finish()
        }
        // Thoát form
        btnCancel.setOnClickListener {
            // Thoát form
            finish()
        }
    }
    private fun loadWarehouseSpinner() {
        warehouseList = warehouseDao.getAll()

        val nameList = warehouseList.map { it.name }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            nameList
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spKho.adapter = adapter

        // Set kho đang chọn
        val currentKhoId = intent.getIntExtra("id_Kho", -1)
        if (currentKhoId != null) {
            val index = warehouseList.indexOfFirst { it.id == currentKhoId }
            if (index != -1) {
                spKho.setSelection(index)
            }
        }
    }
}