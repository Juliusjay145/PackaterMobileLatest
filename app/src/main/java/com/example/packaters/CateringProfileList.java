package com.example.packaters;

public class CateringProfileList {

    private String image,id, name,address,contact,details;


    public CateringProfileList(String image, String id, String name,
                               String address, String contact, String details) {
        super();
        this.image = image;
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


}
