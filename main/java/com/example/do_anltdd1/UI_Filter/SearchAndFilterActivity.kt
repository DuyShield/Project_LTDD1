package com.example.do_anltdd1.Filter;

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.Database.SanPhamDao
import com.example.do_anltdd1.R

class SearchAndFilterActivity : AppCompatActivity() {

    private lateinit var productDao: SanPhamDao

    // Khai báo RecyclerView và Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    private lateinit var edtKeyword: EditText
    private lateinit var rgStatus: RadioGroup
    private lateinit var rgQuantity: RadioGroup
    private lateinit var btnApply: Button
    private lateinit var imgExit: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_filter)

        productDao = AppDB.get(this).daoSP()

        setControl()
        setEvent()
        // Cấu hình RecyclerView
        productAdapter = ProductAdapter(emptyList())
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadAndDisplayResults()

        btnApply.setOnClickListener {
            loadAndDisplayResults()
        }
    }

    private fun setControl() {
        edtKeyword = findViewById(R.id.edt_keyword)
        rgStatus = findViewById(R.id.rg_status)
        rgQuantity = findViewById(R.id.rg_quantity_sort)
        btnApply = findViewById(R.id.btn_search_apply)
        imgExit = findViewById(R.id.imgExitSF)

        // Ánh xạ RecyclerView
        recyclerView = findViewById(R.id.recycler_view_products)
    }
    private fun setEvent() {
        imgExit.setOnClickListener {
            finish()
        }
    }

    private fun loadAndDisplayResults() {
        val keyword = edtKeyword.text.toString().trim()
        val searchKeyword = "%$keyword%"

        val status: String? = when (rgStatus.checkedRadioButtonId) {
            R.id.rbCH -> "Còn hàng"
            R.id.rbHH -> "Hết hàng"
            R.id.rbTH -> "Tạm hết"
            else -> null
        }

        val sortOrder: String = when (rgQuantity.checkedRadioButtonId) {
            R.id.rb_asc -> "ASC"
            R.id.rb_desc -> "DESC"
            else -> ""
        }

        val results = productDao.searchFilterSort(searchKeyword, status, sortOrder)

        // Cập nhật Adapter thay vì cập nhật TextView
        productAdapter.updateList(results)
    }
}