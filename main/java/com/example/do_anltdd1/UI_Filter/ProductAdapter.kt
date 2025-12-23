package com.example.do_anltdd1.Filter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.do_anltdd1.Database.SanPham
import com.example.do_anltdd1.R

class ProductAdapter(private var productList: List<SanPham>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tv_product_name)
        val quantityTextView: TextView = itemView.findViewById(R.id.tv_quantity)
        val statusTextView: TextView = itemView.findViewById(R.id.tv_available_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_warehouse, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        // Hiển thị thông tin sản phẩm
        holder.nameTextView.text = "Tên Sản Phẩm: ${product.name}"
        holder.quantityTextView.text = "Số lượng: ${product.quantity}"

        val statusText = when (product.status) {
            "Còn hàng" -> "Còn hàng"
            "Hết hàng" -> "Hết hàng"
            "Tạm hết"  -> "Tạm hết"
            else       -> "Không xác định"
        }
        holder.statusTextView.text = statusText
    }

    override fun getItemCount(): Int = productList.size

    // Hàm cập nhật danh sách khi tìm kiếm/lọc
    fun updateList(newList: List<SanPham>) {
        productList = newList
        notifyDataSetChanged()
    }
}