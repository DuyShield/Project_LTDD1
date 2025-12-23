package com.example.do_anltdd1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.Database.SanPhamDao
import com.example.do_anltdd1.Database.User
import com.example.do_anltdd1.Database.UserDao
import com.example.do_anltdd1.xuat_nhap.main
import kotlinx.coroutines.launch

class Form_login : AppCompatActivity() {

    private lateinit var edtUser: EditText
    private lateinit var edtPass: EditText
    private lateinit var btnLogin: Button
    lateinit var db: AppDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)
        db = AppDB.Companion.get(this)

        // Tạo tài khoản mặc định khi mở app
        if (db.userDao().countUser() == 0) {
            db.userDao().insert(User(username = "admin", password = "123", role = "ADMIN"))
            db.userDao().insert(User(username = "user", password = "123", role = "USER"))
        }
        setControl()
        setEvent()
    }

    private fun setControl() {
        edtUser = findViewById(R.id.edtUsername)
        edtPass = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
    }

    private fun setEvent() {
        btnLogin.setOnClickListener {
            val user = edtUser.text.toString().trim()
            val pass = edtPass.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


                val userLogin = db.userDao().login(user, pass)

                if (userLogin != null) {
                    val prefs = getSharedPreferences("LOGIN", MODE_PRIVATE)
                    prefs.edit()
                        .putBoolean("IS_LOGIN", true)
                        .putString("USERNAME", userLogin.username)
                        .putString("ROLE", userLogin.role)
                        .apply()

                    startActivity(Intent(this@Form_login, index::class.java))
                    finish()
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@Form_login,
                            "Sai tài khoản hoặc mật khẩu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }
    }
}
