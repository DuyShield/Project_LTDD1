package com.example.do_anltdd1.UI_Product

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.R
import com.example.do_anltdd1.Database.SanPham

class ShowList_Adapter(val activity: AppCompatActivity,
                       val res: Int,
                       val ds: ArrayList<SanPham>):
    ArrayAdapter<SanPham>(activity,res,ds) {
    private val db = AppDB.Companion.get(activity)
    private val productDao = db.daoSP()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(res, parent, false)

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvSL = view.findViewById<TextView>(R.id.tvSL)
        val tvTT = view.findViewById<TextView>(R.id.tvTT)
        val tvK = view.findViewById<TextView>(R.id.tvK)
        val icon = view.findViewById<ImageView>(R.id.logoProduct)

        val btnUpdate = view.findViewById<Button>(R.id.btnUpForm)
        val btnDel = view.findViewById<Button>(R.id.btnDelForm)

        val p = ds[position]
        //Tìm tên kho
        val khoName = db.daoWH().getNameById(p.id_Kho) ?: -1

        tvName.text = "Tên sản phẩm: ${p.name}"
        tvSL.text = "Số lượng: ${p.quantity}"
        tvTT.text = "Trạng thái: ${p.status}"
        tvK.text = "Kho: ${khoName ?: "Không xác định"}"
        icon.setImageResource(R.drawable.iconapp)

        // Mở Update Form
        btnUpdate.setOnClickListener {
            // Truyền data vào form update
            val intent = Intent(activity, Update_Form::class.java)
            intent.putExtra("position", position)
            intent.putExtra("id", p.id)
            intent.putExtra("name", p.name)
            intent.putExtra("quantity", p.quantity)
            intent.putExtra("status", p.status)
            intent.putExtra("id_Kho", p.id_Kho)
            context.startActivity(intent)
        }
        // Mở dialog xóa nhận xóa sản phẩm
        btnDel.setOnClickListener {
            // Tạo dialog
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Xác nhận xóa")
            builder.setMessage("Bạn có chắc muốn xóa sản phẩm ${p.name}?")
            builder.setPositiveButton("Có") { dialog, _ ->
                // Xóa sản phẩm
                productDao.delete(p)
                ds.removeAt(position)
                notifyDataSetChanged()
                dialog.dismiss()
            }
            builder.setNegativeButton("Hủy") { dialog, _ ->
                // Thoát dialog
                dialog.dismiss()
            }
            builder.show()
        }
        return view
    }
}