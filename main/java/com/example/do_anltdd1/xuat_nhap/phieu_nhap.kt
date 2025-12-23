package com.example.do_anltdd1.xuat_nhap;

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.Database.PhieuNhap
import com.example.do_anltdd1.Database.PhieuNhapDao
import com.example.do_anltdd1.Database.SanPham
import com.example.do_anltdd1.Database.SanPhamDao
import com.example.do_anltdd1.Database.Warehouse
import com.example.do_anltdd1.Database.WarehouseDao
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.do_anltdd1.R
class phieu_nhap : AppCompatActivity() {

    private lateinit var btnQuayLai: ImageView
    private lateinit var spNameWH: Spinner
    private lateinit var spNameProduct: Spinner
    private lateinit var edtQuantity: EditText
    private lateinit var edtPrice: EditText
    private lateinit var tvTotalPrice: TextView
    private lateinit var edtSupplier: EditText
    private lateinit var edtDate: EditText
    private lateinit var spLoai: Spinner
    private lateinit var btnTao: Button
    private lateinit var btnHuy: Button
    private lateinit var listSanPham: List<SanPham>
    private lateinit var listKho: List<Warehouse>

    lateinit var db: AppDB
    lateinit var phieuDao: PhieuNhapDao
    // Bien luu tru ngay nhap
    private var ngayDaChon: Calendar? = null
    private val dinhDangNgay = SimpleDateFormat("dd-MM-yyyy", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phieu_nhap)

        setControll()
        db = AppDB.Companion.get(this)
        phieuDao = db.daoPN()
        loadSpinnerSanPham()
        loadSpinnerKho()
        setEvent()
    }

    // Anh xa
    private fun setControll() {
        // Header
        btnQuayLai = findViewById(R.id.btn_back)

        // Fields
        spNameWH = findViewById(R.id.spKhoPN)
        spNameProduct = findViewById(R.id.spSanPham)
        edtPrice = findViewById(R.id.edtPrice)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        edtQuantity = findViewById(R.id.edtQuantity)
        spLoai = findViewById(R.id.spLoai)
        edtSupplier = findViewById(R.id.edtSupplier)
        edtDate = findViewById(R.id.edtDate)

        // Footer Buttons
        btnTao = findViewById(R.id.btn_tao)
        btnHuy = findViewById(R.id.btn_huy)
    }

    // Xu ly cac su kien
    private fun setEvent() {
        // Nut Quay Lai va Huy
        btnQuayLai.setOnClickListener { finish() }
        btnHuy.setOnClickListener { finish() }

        // Xử lý update tổng tiền sau khi nhập tiền
        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                capNhatTongTien()
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        }

        edtQuantity.addTextChangedListener(textWatcher)
        edtPrice.addTextChangedListener(textWatcher)

        // Xu ly Date Picker khi nhan vao EditText Ngay Nhap
        edtDate.setOnClickListener {
            hienThiDatePickerDialog()
        }

        // Ngan ban phim hien ra
        edtDate.keyListener = null

        // Nut Tao Phieu
        btnTao.setOnClickListener {
            if (kiemTraHopLe()) {
                luuPhieuNhap()
            }
        }
    }

    // Ham hien thi Date Picker Dialog
    private fun hienThiDatePickerDialog() {
        val c = ngayDaChon ?: Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            { _, namDaChon, thangDaChon, ngayDaChonInt ->
                // Luu ngay duoc chon
                ngayDaChon = Calendar.getInstance().apply {
                    set(namDaChon, thangDaChon, ngayDaChonInt)
                }
                // Dinh dang hien thi ngay va dua vao EditText
                edtDate.setText(dinhDangNgay.format(ngayDaChon!!.time))
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    // Ham kiem tra tinh hop le cua du lieu
    private fun kiemTraHopLe(): Boolean {
        edtQuantity.error = null
        edtPrice.error = null
        edtSupplier.error = null
        edtDate.error = null

        var hopLe = true

        val soLuong = edtQuantity.text.toString().toIntOrNull()
        if (soLuong == null || soLuong <= 0) {
            edtQuantity.error = "Số lượng phải > 0"
            hopLe = false
        }

        val gia = edtPrice.text.toString().toDoubleOrNull()
        if (gia == null || gia <= 0) {
            edtPrice.error = "Giá không hợp lệ"
            hopLe = false
        }

        if (edtSupplier.text.toString().trim().isEmpty()) {
            edtSupplier.error = "Nhà cung cấp không được trống"
            hopLe = false
        }

        if (edtDate.text.toString().trim().isEmpty()) {
            edtDate.error = "Vui lòng chọn ngày"
            hopLe = false
        }

        return hopLe
    }


    // Ham xu ly tao/luu phieu
    private fun luuPhieuNhap() {
        val spIndex = spNameProduct.selectedItemPosition
        val khoIndex = spNameWH.selectedItemPosition

        val idSanPham = listSanPham[spIndex].id
        val idKho = listKho[khoIndex].id
        val soLuong = edtQuantity.text.toString().toInt()
        val tongTien = soLuong * edtPrice.text.toString().toDouble()
        val loai = spLoai.selectedItem.toString()
        val nhaCC = edtSupplier.text.toString()
        val ngayNhap = edtDate.text.toString()

        val phieuMoi = PhieuNhap(
            idKho = idKho,
            idSanPham = idSanPham,
            quantity = soLuong,
            loai = loai,
            nhaCungCap = nhaCC,
            ngayNhap = ngayNhap,
            totalPrice = tongTien
        )

        db.daoPN().insert(phieuMoi)
        hienThiThongBao(phieuMoi)
    }



    // Ham hien thi Dialog
    private fun hienThiThongBao(phieuNhap: PhieuNhap) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_thong_bao)
        dialog.setCancelable(false)

        val btnOk: Button = dialog.findViewById(R.id.btn_dialog_ok)
        btnOk.setOnClickListener {
            dialog.dismiss()
            // Chuyen sang ActivityDanhSach
            val intent = Intent(this, danh_sach::class.java).apply {
                putExtra("NEW_PHIEU_NHAP", phieuNhap)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
        dialog.show()
    }
    private fun loadSpinnerSanPham() {
        listSanPham = db.daoSP().getAllForSpinner()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listSanPham.map { it.name }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spNameProduct.adapter = adapter
    }
    private fun loadSpinnerKho() {
        listKho = db.daoWH().getAllForSpinner()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listKho.map { it.name }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spNameWH.adapter = adapter
    }
    private fun capNhatTongTien() {
        val soLuong = edtQuantity.text.toString().toIntOrNull() ?: 0
        val gia = edtPrice.text.toString().toDoubleOrNull() ?: 0.0

        val tongTien = soLuong * gia
        tvTotalPrice.text = String.format("%,.0f", tongTien)
    }
}