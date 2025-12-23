package com.example.do_anltdd1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.do_anltdd1.Filter.SearchAndFilterActivity
import com.example.do_anltdd1.UI_Product.Main_product
import com.example.do_anltdd1.UI_WareHouse.Formwarehouse
import com.example.do_anltdd1.xuat_nhap.main

class index : AppCompatActivity() {
    lateinit var btnWH: Button
    lateinit var btnSP: Button
    lateinit var btnFilter: Button
    lateinit var btnXN: Button
    lateinit var btnDX: Button
    private var role: String = "USER"
    lateinit var tvWelcome: TextView
    lateinit var tvRole: TextView
    lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_index)
        //Kiểm tra login
        val prefs = getSharedPreferences("LOGIN", MODE_PRIVATE)
        val isLogin = prefs.getBoolean("IS_LOGIN", false)

        if (!isLogin) {
            startActivity(Intent(this, Form_login::class.java))
            finish()
            return
        }
        //Lấy role và name người dùng
        role = prefs.getString("ROLE", "USER") ?: "USER"
        username = prefs.getString("USERNAME", "USER") ?: "User"

        setControl()
        phanQuyen()
        setEvent()
    }
    private fun setControl(){
        btnWH = findViewById(R.id.btnFormKho)
        btnSP = findViewById(R.id.btnFormSanPham)
        btnFilter = findViewById(R.id.btnFormFiler)
        btnXN = findViewById(R.id.btnFormXN)
        btnDX = findViewById(R.id.btnDX)
        tvWelcome = findViewById(R.id.tvWelcome)
        tvRole = findViewById(R.id.tvRole)
    }
    private fun phanQuyen() {
        if (role == "USER") {
            btnWH.visibility = View.GONE
            btnSP.visibility = View.GONE
            btnFilter.visibility = View.GONE
        }
    }
    private fun setEvent(){

        tvWelcome.text = "Xin chào: $username"
        tvRole.text = "Quyền: $role"
        btnSP.setOnClickListener {
            if (role != "ADMIN") {
                Toast.makeText(this, "Bạn không có quyền", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, Main_product::class.java)
            startActivity(intent)
        }
        btnWH.setOnClickListener {
            if (role != "ADMIN") {
                Toast.makeText(this, "Bạn không có quyền", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, Formwarehouse::class.java)
            startActivity(intent)
        }
        btnFilter.setOnClickListener {
            if (role != "ADMIN") {
                Toast.makeText(this, "Bạn không có quyền", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, SearchAndFilterActivity::class.java)
            startActivity(intent)
        }
        btnXN.setOnClickListener {
            val intent = Intent(this, main::class.java)
            startActivity(intent)
        }
        btnDX.setOnClickListener {
            val prefs = getSharedPreferences("LOGIN", MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(this, Form_login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}