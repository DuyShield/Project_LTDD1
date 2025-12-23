package com.example.do_anltdd1.UI_Product

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Dao
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.R
import com.example.do_anltdd1.Database.SanPham
import com.example.do_anltdd1.Database.SanPhamDao
import com.example.do_anltdd1.Database.Warehouse
import com.example.do_anltdd1.Database.WarehouseDao

class Add_New_Form : AppCompatActivity() {
    lateinit var btnAdd: Button
    lateinit var btnCancel: Button
    lateinit var edtId : EditText
    lateinit var edtName: EditText
    lateinit var edtSL : EditText
    lateinit var ivImg: ImageView
    lateinit var spStatus : Spinner
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
        setContentView(R.layout.addnew_form)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addnew)) { v, insets ->
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
    private fun setControl(){
        edtId = findViewById(R.id.edtID)
        edtName = findViewById(R.id.edtName)
        edtSL = findViewById(R.id.edtQuan)
        spStatus = findViewById(R.id.spStatus)
        ivImg = findViewById(R.id.ivImg_Add)
        btnAdd = findViewById(R.id.btnAdd)
        btnCancel = findViewById(R.id.btnCancelAdd)
        spKho = findViewById(R.id.spKho)
    }

    private fun setEvent() {
        // Event
        // Mở thư viện ảnh
//        ivImg.setOnClickListener {
//            pickImage.launch("image/*")
//        }
        // Thêm mới
        btnAdd.setOnClickListener {
            val id = edtId.text.toString().trim()
            val name = edtName.text.toString()
            val quantity = edtSL.text.toString().trim()
            val status = spStatus.selectedItem.toString().trim()
            val selectedIndexWH = spKho.selectedItemPosition
            val selectedIdWH = warehouseList[selectedIndexWH].id
            if(id.isEmpty()){
                Toast.makeText(this,"Id không được để trống", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
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
            // Kiểm tra id trùng
            val isDuplicate = productDao.isIdExist(id)
            if (isDuplicate) {
                Toast.makeText(this, "Id đã tồn tại, vui lòng nhập id khác!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Thêm sản phẩm
            val sp = SanPham(id, name, num, status, null, selectedIdWH)
            db.daoSP().insert(sp)
            finish()
        }
        // Thoát form
        btnCancel.setOnClickListener {
            finish()
        }
    }
    private fun loadWarehouseSpinner() {
            warehouseList = warehouseDao.getAll()

            runOnUiThread {
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
            }
    }
}