package com.example.do_anltdd1.UI_Product

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.R
import com.example.do_anltdd1.Database.SanPham
import com.example.do_anltdd1.Database.SanPhamDao

class Main_product : AppCompatActivity() {
    lateinit var name: TextView
    lateinit var listView: ListView
    lateinit var btnAdd: Button
    lateinit var edtSearch: EditText
    lateinit var adapterProduct: ShowList_Adapter
    lateinit var imgExit: ImageView
    val list: ArrayList<SanPham> = ArrayList<SanPham>()

    lateinit var db: AppDB
    lateinit var productDao: SanPhamDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setControl();
        db = AppDB.Companion.get(this)
        productDao = db.daoSP()
        setEvent();
    }
    private fun setControl(){
        //Ánh xạ
        listView = findViewById(R.id.listView)
        btnAdd = findViewById(R.id.btnAdd_MS)
        edtSearch = findViewById(R.id.edtSearch)
        imgExit = findViewById(R.id.imgExit)
    }
    private fun setEvent(){
        // Hiển thị ra màn hình
        adapterProduct = ShowList_Adapter(this@Main_product, R.layout.item_layout_product, list)
        listView.adapter = adapterProduct
        val dsKQ = ArrayList<String>()

        // Sự kiện mở form
        btnAdd.setOnClickListener {
            //Mở form thêm mới
            val intent = Intent(this, Add_New_Form::class.java)
            startActivity(intent)
        }
        // Sư kiên tìm kiếm và update list real time
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchData(s.toString().trim().lowercase())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        // Thoát quản lý danh sách sản phẩm
        imgExit.setOnClickListener {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        // Reload danh sách khi quay lại
        list.clear()
        list.addAll(productDao.getAll())
        adapterProduct.notifyDataSetChanged()
    }
    private fun searchData(key: String) {
        list.clear()
        if (key.isEmpty()) {
            list.addAll(productDao.getAll())
        } else {
            list.addAll(productDao.search(key))
        }
        adapterProduct.notifyDataSetChanged()
    }
}