package com.example.do_anltdd1.UI_WareHouse;

public class Warehouse {
    private int id;
    private String name;
    private String status;
    private int capacity;
    private String note;

    // Constructor đầy đủ
    public Warehouse(int id, String name, int capacity, String status, String note) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.status = status;
        this.note = note;
    }


    // Getter
    public int getId() { return id; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public int getCapacity() { return capacity; }
    public String getNote() { return note; }

    // Setter
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setStatus(String status) { this.status = status; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setNote(String note) { this.note = note; }

    // Hiển thị đẹp khi dùng ListView, Spinner, ArrayAdapter...
    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }
}
