package com.example.packaters;

public class BookingDetailsList {

    String image,name,address,date,time,price,rstatus,pname,id,restid;


    public BookingDetailsList(String image, String name, String address,
                              String date, String time, String price, String rstatus, String pname,String id,
                              String restid){
        super();
        this.image = image;
        this.name = name;
        this.address = address;
        this.date = date;
        this.time = time;
        this.price = price;
        this.rstatus = rstatus;
        this.pname = pname;
        this.id = id;
        this.restid = restid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRstatus() {
        return rstatus;
    }

    public void setRstatus(String rstatus) {
        this.rstatus = rstatus;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestid() {
        return restid;
    }

    public void setRestid(String restid) {
        this.restid = restid;
    }
}
