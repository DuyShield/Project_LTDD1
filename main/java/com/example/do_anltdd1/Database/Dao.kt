package com.example.do_anltdd1.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.do_anltdd1.Database.WarehouseDao

@Dao
interface SanPhamDao {
    @Insert
    fun insert(sp: SanPham)
    @Update
    fun update(sp: SanPham)
    @Delete
    fun delete(sp: SanPham)
    // Hiển thị danh sách sản phẩm
    @Query("SELECT * FROM sanpham")
    fun getAll(): List<SanPham>
    // Kiểm tra id
    @Query("SELECT EXISTS(SELECT 1 FROM sanpham WHERE id = :id)")
    fun isIdExist(id: String): Boolean
    // Tìm kiếm sản phẩm
    @Query("""
    SELECT * FROM sanpham 
    WHERE LOWER(name) LIKE '%' || :key || '%' 
       OR LOWER(id) LIKE '%' || :key || '%'
""")
    fun search(key: String): List<SanPham>
    // Lọc sản phẩm
    @Query("""
    SELECT * FROM sanpham
    WHERE (:searchKeyword IS NULL OR name LIKE :searchKeyword)
    AND (:status IS NULL OR status = :status)
    ORDER BY 
        CASE WHEN :sortOrder = 'ASC' THEN quantity END ASC,
        CASE WHEN :sortOrder = 'DESC' THEN quantity END DESC,
        id ASC
""")
    fun searchFilterSort(
        searchKeyword: String?,
        status: String?,
        sortOrder: String
    ): List<SanPham>
    // Lấy số lượng sản phẩm
    @Query("SELECT quantity FROM SanPham WHERE id = :id")
    fun getSoLuong(id: Int): Int
    // Update số lượng sản phẩm
    @Query("UPDATE SanPham SET quantity = quantity - :sl WHERE id = :id")
    fun truSoLuong(id: Int, sl: Int)
    @Query("SELECT * FROM sanpham")
    fun getAllForSpinner(): List<SanPham>

    @Query("SELECT COUNT(*) FROM sanpham WHERE quantity > 0")
    fun countTonKho(): Int

    @Query("SELECT COUNT(*) FROM sanpham WHERE quantity <= 0")
    fun countTrong(): Int

    @Query("SELECT COUNT(*) FROM sanpham WHERE status = 'Còn hàng'")
    fun countConHang(): Int

    @Query("SELECT COUNT(*) FROM sanpham WHERE status = 'Hết hàng'")
    fun countHetHang(): Int

    @Query("SELECT COUNT(*) FROM sanpham WHERE status = 'Tạm hết'")
    fun countTamHet(): Int
    @Query("SELECT COUNT(*) FROM sanpham")
    fun countTongSanPham(): Int
}
@Dao
interface WarehouseDao {
    @Insert
    fun insert(sp: Warehouse)
    @Update
    fun update(sp: Warehouse)
    @Delete
    fun delete(sp: Warehouse)
    // Hiển thị danh sách kho
    @Query("SELECT * FROM warehouse")
    fun getAll(): List<Warehouse>
    // Kiểm tra id
    @Query("SELECT EXISTS(SELECT 1 FROM warehouse WHERE id = :id)")
    fun isIdExist(id: String): Boolean
    // Tìm kiếm kho
    @Query("""
    SELECT * FROM warehouse 
    WHERE LOWER(name) LIKE '%' || :key || '%' 
       OR LOWER(id) LIKE '%' || :key || '%'
""")
    fun search(key: String): List<Warehouse>
    //Hiển thị tên kho
    @Query("SELECT name FROM warehouse WHERE id = :idKho LIMIT 1")
    fun getNameById(idKho: Int?): String?
    @Query("SELECT * FROM warehouse")
    fun getAllForSpinner(): List<Warehouse>
    @Query("SELECT COUNT(*) FROM warehouse")
    fun countTongKho(): Int
    @Query("SELECT COUNT(*) FROM warehouse WHERE quantity = 0")
    fun countKhoDay(): Int
}
@Dao
interface PhieuNhapDao {
    @Insert
    fun insert(sp: PhieuNhap)
    @Update
    fun update(sp: PhieuNhap)
    @Delete
    fun delete(sp: PhieuNhap)
    @Query("SELECT * FROM phieunhap")
    fun getAll(): List<PhieuNhap>

    @Query("SELECT EXISTS(SELECT 1 FROM phieunhap WHERE id = :id)")
    fun isIdExist(id: Int): Boolean

    @Query("""
    SELECT pn.*, sp.name as productName
    FROM phieunhap pn
    INNER JOIN sanpham AS sp ON pn.idSanPham = sp.id
    INNER JOIN warehouse AS wh ON pn.idKho = wh.id
    WHERE sp.name LIKE :key or wh.name LIKE :key
    """)
    fun search(key: String): List<PhieuNhap>
    @Query("SELECT COUNT(*) FROM phieunhap")
    fun tongNhap(): Int

    @Query("SELECT IFNULL(SUM(totalPrice),0) FROM phieunhap")
    fun tongTienNhap(): Long

}
@Dao
interface UserDao {

    @Query("""
        SELECT * FROM user 
        WHERE username = :u AND password = :p
    """)
    fun login(u: String, p: String): User?

    @Insert
    fun insert(user: User)

    @Query("SELECT COUNT(*) FROM user")
    fun countUser(): Int
}
@Dao
interface PhieuXuatDao {
    @Insert
    fun insert(px: PhieuXuat)
    @Query("SELECT COUNT(*) FROM phieuxuat")
    fun countTongPhieuXuat(): Int
}
