package com.example.do_anltdd1.xuat_nhap

import android.content.Context
import android.content.Intent
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.Database.PhieuNhap
import com.example.do_anltdd1.Database.PhieuNhapDao
import com.example.do_anltdd1.R
import java.text.NumberFormat
import java.util.*

class danh_sach : AppCompatActivity() {

    private lateinit var btnQuayLaiDanhSach: ImageView
    private lateinit var edtTimKiem: EditText
    private lateinit var btnTimKiem: Button
    private lateinit var lvDanhSachPhieu: ListView

    private lateinit var db: AppDB
    private lateinit var phieuNhapDao: PhieuNhapDao

    private val danhSachChoXuat = mutableListOf<PhieuNhap>()
    private val danhSachDaXuat = mutableListOf<PhieuNhap>()
    private lateinit var lvDanhSachDaXuat: ListView
    private lateinit var adapterChoXuat: PhieuNhapAdapter
    private lateinit var adapterDaXuat: PhieuNhapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_danh_sach)
        setControll()
        db = AppDB.get(this)
        phieuNhapDao = db.daoPN()

        datDuLieuBanDau()
        setEvent()
    }

    // Ánh xạ
    private fun setControll() {
        btnQuayLaiDanhSach = findViewById(R.id.btn_back_list)
        edtTimKiem = findViewById(R.id.edt_search_list)
        btnTimKiem = findViewById(R.id.btn_tim_kiem)

        lvDanhSachPhieu = findViewById(R.id.lv_DanhSach)
        lvDanhSachDaXuat = findViewById(R.id.lv_DanhSachXuat)

        adapterChoXuat = PhieuNhapAdapter(this, danhSachChoXuat)
        adapterDaXuat = PhieuNhapAdapter(this, danhSachDaXuat)

        lvDanhSachPhieu.adapter = adapterChoXuat
        lvDanhSachDaXuat.adapter = adapterDaXuat
    }

    // Lấy dữ liệu từ room
    private fun datDuLieuBanDau() {
        Thread {
            val tatCaPhieu = phieuNhapDao.getAll()

            runOnUiThread {
                danhSachChoXuat.clear()
                danhSachDaXuat.clear()

                for (phieu in tatCaPhieu) {
                    if (phieu.loai == "DA_XUAT") {
                        danhSachDaXuat.add(phieu)
                    } else {
                        danhSachChoXuat.add(phieu)
                    }
                }

                adapterChoXuat.notifyDataSetChanged()
                adapterDaXuat.notifyDataSetChanged()
            }
        }.start()
    }

    // Sự kiện
    private fun setEvent() {
        btnQuayLaiDanhSach.setOnClickListener {
            finish()
        }

        btnTimKiem.setOnClickListener {
            thucHienTimKiem()
        }

        lvDanhSachPhieu.setOnItemClickListener { _, _, position, _ ->
            val phieuChon = danhSachChoXuat[position]

            val intent = Intent(this, xuat_kho::class.java)
            intent.putExtra("PHIEU_DUOC_CHON", phieuChon)
            startActivity(intent)
        }
    }

    // Tìm kiếm bằng room
    private fun thucHienTimKiem() {
        val tuKhoa = edtTimKiem.text.toString().trim()

        Thread {
            val ketQua = if (tuKhoa.isBlank()) {
                phieuNhapDao.getAll()
            } else {
                phieuNhapDao.search(tuKhoa)
            }

            runOnUiThread {
                danhSachChoXuat.clear()
                danhSachDaXuat.clear()

                for (phieu in ketQua) {
                    if (phieu.loai == "XUAT") {
                        danhSachDaXuat.add(phieu)
                    } else {
                        danhSachChoXuat.add(phieu)
                    }
                }

                adapterChoXuat.notifyDataSetChanged()
                adapterDaXuat.notifyDataSetChanged()

                if (ketQua.isEmpty()) {
                    hienThiDialogKhongTimThay()
                }
            }
        }.start()
    }

    private fun hienThiDialogKhongTimThay() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_thong_bao_tim_kiem)
        dialog.setCancelable(true)

        val btnOk: Button = dialog.findViewById(R.id.btn_dialog_tim_kiem_ok)
        btnOk.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    // Adapter
    private class PhieuNhapAdapter(context: Context, phieuList: List<PhieuNhap>) :
        ArrayAdapter<PhieuNhap>(context, 0, phieuList) {

        private val dinhDangTien =
            NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            if (itemView == null) {
                itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_phieu_nhap, parent, false)
            }

            val phieu = getItem(position) ?: return itemView!!

            val tvTenSP: TextView = itemView!!.findViewById(R.id.tv_item_ten_sp)
            val tvThongTin: TextView = itemView.findViewById(R.id.tv_item_thong_tin)

            // Hiển thị id sản phẩm
            tvTenSP.text = "SP: ${phieu.idSanPham}"

            // Format lại tiền
            val tien = dinhDangTien.format(phieu.totalPrice)

            tvThongTin.text =
                "SL: ${phieu.quantity} | Tiền: $tien | Loại: ${phieu.loai} | NCC: ${phieu.nhaCungCap}"

            return itemView
        }

    }
    override fun onResume() {
        super.onResume()
        datDuLieuBanDau() // reload lại 2 danh sách
    }
}
