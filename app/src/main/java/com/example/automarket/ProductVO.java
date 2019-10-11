package com.example.automarket;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public class ProductVO implements Parcelable {

    private String orderid; // 주문자id
    private String prodid; // 상품id
    private String prodnm; //상품이름
    private int ordercnt; // 상품 개수
    private int prodprice; // 상품 가격
    private String imgpath; // 이미지

    private Drawable drawable;

    public static final Creator<ProductVO> CREATOR = new Creator<ProductVO>() {
        @Override
        public ProductVO createFromParcel(Parcel parcel) {
            return new ProductVO(parcel);
        }

        @Override
        public ProductVO[] newArray(int size) { return new ProductVO[size]; }
    };

    public ProductVO() { }

    public ProductVO(String orderid, String prodid, String prodnm, int ordercnt, int prodprice, String imgpath) {
        this.orderid = orderid;
        this.prodid = prodid;
        this.prodnm = prodnm;
        this.ordercnt = ordercnt;
        this.prodprice = prodprice;
        this.imgpath = imgpath;
    }

    protected ProductVO(Parcel parcel) {
        orderid = parcel.readString();
        prodid = parcel.readString();
        prodnm = parcel.readString();
        ordercnt = parcel.readInt();
        prodprice = parcel.readInt();
        imgpath = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try {
            parcel.writeString(orderid);
            parcel.writeString(prodid);
            parcel.writeString(prodnm);
            parcel.writeInt(ordercnt);
            parcel.writeInt(prodprice);
            parcel.writeString(imgpath);

        } catch (Exception e) {
            Log.i("ProcdLog", e.toString());
        }
    }

    public String getOrderid() {return  orderid;}
    public String getProdid() {return  prodid;}
    public String getProdnm() {return prodnm;}
    public int getOrdercnt() {return ordercnt;}
    public int getProdprice() {return prodprice;}
    public String getImgpath() {return imgpath;}

    public void setOrderid(String orderid) {this.orderid = orderid;}
    public void setProdid(String prodid) {this.prodid = prodid;}
    public void setProdnm(String prodnm) {this.prodnm = prodnm;}
    public void setOrdercnt(int ordercnt) {this.ordercnt = ordercnt;}
    public void setProdprice(int prodprice) {this.prodprice = prodprice;}
    public void setImgpath(String imgpath) {this.imgpath = imgpath;}


    public Drawable getDrawable() {return drawable;}

    public void drawableFromURL() {
        Drawable d = null;

        String test = "http://70.12.115.50:7777/automarket"+imgpath;

        try {
            Log.i("IMIM1",test);
            InputStream is = (InputStream)new URL(test).getContent();
            d = Drawable.createFromStream(is, "NAME");
            this.drawable = d;
            Log.i("IMIM",is.toString());
        } catch (Exception e) {
            Log.i("Error", e.toString());
        }
    }
}
