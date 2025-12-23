package com.example.do_anltdd1.xuat_nhap;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.Database.PhieuNhap
import com.example.do_anltdd1.Database.PhieuNhapDao
import com.example.do_anltdd1.Database.PhieuXuatDao
import com.example.do_anltdd1.Database.SanPhamDao
import com.example.do_anltdd1.Database.WarehouseDao
import com.example.do_anltdd1.R
class main : AppCompatActivity() {

    // Khai bao cac thanh phan UI
    private lateinit var btnNhapKho: Button
    private lateinit var btnDanhSach: Button
    private lateinit var btnThoat: Button
    private lateinit var tvTonKho: TextView
    private lateinit var tvTamHet: TextView
    private lateinit var tvHetHang: TextView
    private lateinit var tvTongNhap: TextView
    private lateinit var tvTongXuat: TextView
    private lateinit var tvTongTien: TextView
    private lateinit var tvTongKho: TextView
    private lateinit var tvTongSanPham: TextView
    lateinit var db: AppDB
    lateinit var productDao: SanPhamDao
    lateinit var phieuNhapDao: PhieuNhapDao
    lateinit var warehouseDao: WarehouseDao
    lateinit var phieuXuatDao: PhieuXuatDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_xuat)

        setControll()
        db = AppDB.Companion.get(this)
        productDao = db.daoSP()
        phieuNhapDao = db.daoPN()
        warehouseDao = db.daoWH()
        phieuXuatDao = db.daoPX()
        setEvent()
        loadThongKe()
    }

    // Anh xa
    private fun setControll() {
        tvTonKho = findViewById(R.id.tvTonKho)
        tvTamHet= findViewById(R.id.tvTamHet)
        tvHetHang= findViewById(R.id.tvHetHang)
        tvTongNhap = findViewById(R.id.tvTongNhap)
        tvTongXuat = findViewById(R.id.tvTongXuat)
        tvTongTien = findViewById(R.id.tvTongTien)
        tvTongKho = findViewById(R.id.tvTongKho)
        tvTongSanPham = findViewById(R.id.tvTongSanPham)
        btnNhapKho = findViewById(R.id.btn_nhap_kho)
        btnDanhSach = findViewById(R.id.btn_danh_sach)
        btnThoat = findViewById(R.id.btn_thoat)
    }

    private fun setEvent() {
        btnNhapKho.setOnClickListener {
            val intent = Intent(this, phieu_nhap::class.java)
            startActivity(intent)
        }

        btnDanhSach.setOnClickListener {
            val intent = Intent(this, danh_sach::class.java)
            startActivity(intent)
        }


        // Nút Thoát
        btnThoat.setOnClickListener {
            finish()
        }
    }
    private fun loadThongKe() {

        // Trạng thái kho
        val tonKho = productDao.countConHang() ?: 0
        val tamHet = productDao.countTamHet() ?: 0
        val hetHang = productDao.countHetHang() ?: 0

        tvTonKho.text = "Tồn kho: $tonKho sản phẩm"
        tvTamHet.text = "Tạm hết: $tamHet sản phẩm"
        tvHetHang.text = "Hết hàng: $hetHang sản phẩm"

        // Thống kê nhập
        val tongNhap = phieuNhapDao.tongNhap() ?: 0
        val tongTien = phieuNhapDao.tongTienNhap() ?: 0
        val tongKho = warehouseDao.countTongKho() ?: 0
        val tongSanPham = productDao.countTongSanPham() ?: 0
        val tongXuat = phieuXuatDao.countTongPhieuXuat() ?: 0

        tvTongSanPham.text = "Tổng tất cả sản phẩm: $tongSanPham"
        tvTongKho.text = "Tồng số kho: $tongKho"
        tvTongNhap.text = "Tổng nhập: $tongNhap"
        tvTongXuat.text = "Tổng xuất: $tongXuat"
        tvTongTien.text = String.format("Tổng tiền: %,d ₫", tongTien)

        // Nếu có xuất
        // val tongXuat = phieuXuatDao.tongXuat()
        // tvTongXuat.text = "Tổng xuất: $tongXuat"
    }
}