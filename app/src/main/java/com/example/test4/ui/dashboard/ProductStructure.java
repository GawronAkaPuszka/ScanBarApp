package com.example.test4.ui.dashboard;

import java.sql.Date;

public class ProductStructure {
    private int id;
    private String barcode;
    private String name;
    private Date dateOfScan;

    public ProductStructure(int id, String barcode, String name, Date dateOfScan) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.dateOfScan = dateOfScan;
    }

    public ProductStructure(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfScan() {
        return dateOfScan;
    }

    public void setDateOfScan(Date dateOfScan) {
        this.dateOfScan = dateOfScan;
    }

    @Override
    public String toString() {
        return "ProductStructure{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", dateOfScan=" + dateOfScan +
                '}';
    }
}
