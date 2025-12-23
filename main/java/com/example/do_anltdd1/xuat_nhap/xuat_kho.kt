package com.example.do_anltdd1.xuat_nhap;

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.do_anltdd1.Database.PhieuNhap
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.Database.PhieuNhapDao
import com.example.do_anltdd1.Database.PhieuXuat
import com.example.do_anltdd1.Database.PhieuXuatDao
import com.example.do_anltdd1.Database.SanPhamDao
import com.example.do_anltdd1.R
class xuat_kho : AppCompatActivity() {

    // Khai bao UI
    private lateinit var btnQuayLai: ImageView
    private lateinit var edtTenSanPham: EditText
    private lateinit var edtSoLuong: EditText
    private lateinit var edtTongTien: EditText
    private lateinit var spnTrangThai: Spinner
    private lateinit var edtKhachHang: EditText
    private lateinit var edtNgayXuat: EditText
    private lateinit var btnXuat: Button
    private lateinit var btnHuy: Button
    private lateinit var db: AppDB
    private lateinit var phieuNhapDao: PhieuNhapDao
    private lateinit var phieuHienTai: PhieuNhap
    lateinit var sanPhamDao: SanPhamDao
    lateinit var phieuXuatDao: PhieuXuatDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xuat_kho)

        taiDuLieuPhieu()
        setControll()
        db = AppDB.get(this)
        phieuNhapDao = db.daoPN()
        sanPhamDao = db.daoSP()
        phieuXuatDao = db.daoPX()
        datDuLieuVaoTruong()
        setEvent()
    }

    private fun taiDuLieuPhieu() {
        val phieu = intent.getSerializableExtra("PHIEU_DUOC_CHON") as? PhieuNhap

        if (phieu == null) {
            Toast.makeText(this, "Không tìm thấy phiếu để xuất.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        phieuHienTai = phieu
    }

    private fun setControll() {
        btnQuayLai = findViewById(R.id.btn_back)
        edtTenSanPham = findViewById(R.id.edt_ten_san_pham_xuat)
        edtSoLuong = findViewById(R.id.edt_so_luong_xuat)
        edtTongTien = findViewById(R.id.edt_tong_tien_xuat)
        spnTrangThai = findViewById(R.id.spn_trang_thai_xuat)
        edtKhachHang = findViewById(R.id.edt_khach_hang)
        edtNgayXuat = findViewById(R.id.edt_ngay_xuat)
        btnXuat = findViewById(R.id.btn_xuat)
        btnHuy = findViewById(R.id.btn_huy_xuat)

        val mangTrangThai = arrayOf("Đã xuất", "Đang chờ xuất", "Hủy xuất")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mangTrangThai)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTrangThai.adapter = adapter
    }

    private fun datDuLieuVaoTruong() {
        edtTenSanPham.setText(phieuHienTai.idSanPham)
        edtSoLuong.setText(phieuHienTai.quantity.toString())
        edtTongTien.setText(phieuHienTai.totalPrice.toString())
        edtNgayXuat.setText(phieuHienTai.ngayNhap)
        edtKhachHang.setText(phieuHienTai.nhaCungCap)


        // Khóa các trường
        edtTenSanPham.isEnabled = false
        edtSoLuong.isEnabled = false
        edtTongTien.isEnabled = false
        spnTrangThai.setSelection(1)
        spnTrangThai.isEnabled = false
        edtNgayXuat.isEnabled = false
        edtKhachHang.isEnabled = false
    }

    private fun setEvent() {
        btnQuayLai.setOnClickListener { finish() }
        btnHuy.setOnClickListener { finish() }

        // Nut Xuat
        btnXuat.setOnClickListener {
                hienThiDialogIn()
        }
    }

    // Ham hien thi Dialog In Phieu
    private fun hienThiDialogIn() {
        val dialogIn = Dialog(this)
        dialogIn.setContentView(R.layout.dialog_in_phieu)
        dialogIn.setCancelable(true)

        // Ánh xạ
        val spnInPhieu: Spinner = dialogIn.findViewById(R.id.spn_in_phieu)
        val btnIn: Button = dialogIn.findViewById(R.id.btn_dialog_in_phieu)
        val btnHuy: Button = dialogIn.findViewById(R.id.btn_dialog_huy_in)

        val mangTrangThai = arrayOf("*.pdf", "*.doc", "*.xlsx")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mangTrangThai)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnInPhieu.adapter = adapter

        // Xử lý khi nhấn nút "In phiếu"
        btnIn.setOnClickListener {
            // Giả lập quá trình in/xuất file
            Toast.makeText(this, "Đang xử lý in phiếu", Toast.LENGTH_SHORT).show()
            dialogIn.dismiss()

            //Hiển thị dialog thông báo xuất thành công
            hienThiThongBaoXuat()
        }

        // Xử lý khi nhấn nút "Hủy"
        btnHuy.setOnClickListener {
            dialogIn.dismiss()
        }

        dialogIn.show()
    }

    // Ham hien thi Dialog Thong bao Xuat thanh cong
    private fun hienThiThongBaoXuat() {
        val dialogThongBao = Dialog(this)
        dialogThongBao.setContentView(R.layout.dialog_thong_bao_xuat)
        dialogThongBao.setCancelable(false)

        val btnOk: Button = dialogThongBao.findViewById(R.id.btn_dialog_xuat_ok)
        btnOk.setOnClickListener {
            dialogThongBao.dismiss()

            capNhatTrangThaiPhieu()

            // Quay về
            finish()
        }
        dialogThongBao.show()
    }

    // Cap nhat trang thai phieu trong danh sach kho chung
    private fun capNhatTrangThaiPhieu() {
        val sanPhamId = phieuHienTai.idSanPham.toInt()
        val soLuongXuat = phieuHienTai.quantity

        val tonKho = sanPhamDao.getSoLuong(sanPhamId)

        if (tonKho < soLuongXuat) {
            Toast.makeText(this, "Không đủ hàng trong kho", Toast.LENGTH_SHORT).show()
            return
        }

        // Trừ kho
        sanPhamDao.truSoLuong(sanPhamId, soLuongXuat)

        // Lưu phiếu xuất
        val px = PhieuXuat(
            sanPhamId = sanPhamId,
            soLuong = soLuongXuat,
            tongTien = phieuHienTai.totalPrice.toDouble(),
            khachHang = edtKhachHang.text.toString(),
            ngayXuat = edtNgayXuat.text.toString()
        )
        phieuXuatDao.insert(px)
        val phieuCapNhat = phieuHienTai.copy(
            loai = "DA_XUAT"
        )
        phieuNhapDao.update(phieuCapNhat)

        Toast.makeText(this, "Xuất kho thành công", Toast.LENGTH_SHORT).show()
        finish()
    }
}