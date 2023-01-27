package com.example.test4;

public class DataFromDB {
    //There should be API to get basic info about scanned product. Every looked API by me is not free
    //or data is not good. Gonna use google instead. :)
    private String barcode;

    public DataFromDB(String barcode) {
        this.barcode = barcode;
    }


    public String getBarcode() {
        return barcode.trim();
    }

    public void downloadData() {

    }

}
