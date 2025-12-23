package com.example.do_anltdd1.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class SanPham(
    @PrimaryKey var id: String,
    var name : String,
    var quantity: Int,
    var status: String,
    var icon: String?,
    var id_Kho: Int?
)
@Entity("warehouse")
data class Warehouse(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name : String,
    var quantity: Int,
    var status: String,
    var note: String?
)
@Entity(tableName = "phieunhap")
data class PhieuNhap(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val idKho: Int,
    val idSanPham: String,
    val quantity: Int,
    val loai: String,
    val nhaCungCap: String,
    val ngayNhap: String,
    val totalPrice: Double
) : Serializable
@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val username: String,
    val password: String,
    val role: String
)
@Entity
data class PhieuXuat(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sanPhamId: Int,
    val soLuong: Int,
    val tongTien: Double,
    val khachHang: String,
    val ngayXuat: String
)