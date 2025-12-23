package com.example.do_anltdd1.Database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SanPham::class,  Warehouse::class, PhieuNhap::class, User::class, PhieuXuat::class] , version = 7)
abstract class AppDB: RoomDatabase() {
    abstract fun daoSP(): SanPhamDao
    abstract fun daoWH(): WarehouseDao
    abstract fun daoPN(): PhieuNhapDao
    abstract fun userDao(): UserDao
    abstract fun daoPX(): PhieuXuatDao
    companion object{
        fun get(context: Context) =
            Room.databaseBuilder(context, AppDB::class.java, "spdb.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}