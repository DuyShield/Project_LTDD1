package com.example.do_anltdd1.UI_WareHouse

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.do_anltdd1.Database.AppDB
import com.example.do_anltdd1.Database.Warehouse
import com.example.do_anltdd1.Database.WarehouseDao
import com.example.do_anltdd1.R
import java.util.ArrayList

class Formwarehouse : AppCompatActivity() {

    private lateinit var btnAddWarehouse: Button
    private lateinit var imgExit: ImageView
    private lateinit var listViewKho: ListView

    private lateinit var dsKho: ArrayList<Warehouse>
    private lateinit var adapter: WarehouseAdapter

    private lateinit var launcherAdd: ActivityResultLauncher<Intent>
    private lateinit var launcherUpdate: ActivityResultLauncher<Intent>

    private lateinit var db: AppDB
    private lateinit var warehouseDao: WarehouseDao
    lateinit var edtSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.formwarehome)
        setControl()
        db = AppDB.get(this)
        warehouseDao = db.daoWH()
        setEvent()

    }
    private fun setControl(){
        btnAddWarehouse = findViewById(R.id.btnAdd)
        imgExit = findViewById(R.id.imgExitWH)
        listViewKho = findViewById(R.id.lvShow)
        edtSearch = findViewById(R.id.edtSearchKho)
    }
    private fun setEvent(){
        // Load dữ liệu từ Room
        dsKho = ArrayList(warehouseDao.getAll())

        adapter = WarehouseAdapter(this, dsKho)
        listViewKho.adapter = adapter
        // Sư kiên tìm kiếm và update list real time
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchData(s.toString().trim().lowercase())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        // ===== Launcher Add =====
        launcherAdd = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult

                val name = data.getStringExtra("name") ?: ""
                val status = data.getStringExtra("status") ?: ""
                val quantity = data.getIntExtra("quantity", 0)
                val note = data.getStringExtra("note") ?: ""

                val w = Warehouse(0, name, quantity, status, note)

                warehouseDao.insert(w)

                reloadData()
                Toast.makeText(this, "Đã thêm kho: $name", Toast.LENGTH_SHORT).show()
            }
        }

        // ===== Launcher Update =====
        launcherUpdate = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult

                val id = data.getIntExtra("id", -1)
                val name = data.getStringExtra("name") ?: ""
                val status = data.getStringExtra("status") ?: ""
                val quantity = data.getIntExtra("quantity", 0)
                val note = data.getStringExtra("note") ?: ""

                for (w in dsKho) {
                    if (w.id == id) {
                        w.name = name
                        w.quantity = quantity
                        w.status = status
                        w.note = note
                        warehouseDao.update(w)
                        break
                    }
                }

                reloadData()
                Toast.makeText(this, "Đã cập nhật kho: $name", Toast.LENGTH_SHORT).show()
            }
        }

        btnAddWarehouse.setOnClickListener {
            val intent = Intent(this, Addwarehouse::class.java)
            launcherAdd.launch(intent)
        }

        imgExit.setOnClickListener {
            finish()
        }
    }

    private fun reloadData() {
        dsKho.clear()
        dsKho.addAll(warehouseDao.getAll())
        adapter.notifyDataSetChanged()
    }

    fun openUpdateForm(w: Warehouse) {
        val intent = Intent(this, Updatewarehouse::class.java).apply {
            putExtra("id", w.id)
            putExtra("name", w.name)
            putExtra("quantity", w.quantity)
            putExtra("status", w.status)
            putExtra("note", w.note)
        }
        launcherUpdate.launch(intent)
    }

    fun deleteWarehouse(w: Warehouse) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa kho: ${w.name} ?")
            .setPositiveButton("Xóa") { _, _ ->
                warehouseDao.delete(w)
                reloadData()
                Toast.makeText(this, "Đã xóa kho: ${w.name}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showDetail(w: Warehouse) {
        Toast.makeText(
            this,
            "Tên: ${w.name}\nSố lượng: ${w.quantity}\nTrạng thái: ${w.status}",
            Toast.LENGTH_LONG
        ).show()
    }
    private fun searchData(key: String) {
        dsKho.clear()
        if (key.isEmpty()) {
            dsKho.addAll(warehouseDao.getAll())
        } else {
            dsKho.addAll(warehouseDao.search(key))
        }
        adapter.notifyDataSetChanged()
    }
}
