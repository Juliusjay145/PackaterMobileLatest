package com.example.packaters;

public class CateringServiceList {

    String image,id,name,price,caterid;

    public CateringServiceList(String image, String id, String name, String price,
                       String caterid) {
        super();
        this.image = image;
        this.id = id;
        this.name = name;
        this.price = price;
        this.caterid = caterid;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCaterid() {
        return caterid;
    }

    public void setCaterid(String caterid) {
        this.caterid = caterid;
    }

}
