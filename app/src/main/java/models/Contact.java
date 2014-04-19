package models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Contact {
    private String name;
    private Bitmap photo;
    private List<ContactInfo> infoList;

    public Contact(String name) {
        this.name = name;
        this.infoList = new ArrayList<ContactInfo>();
    }

    public Contact(String name, Bitmap photo) {
        this.name = name;
        this.photo = photo;
        this.infoList = new ArrayList<ContactInfo>();
    }

    public Contact(String name, Bitmap photo, List<ContactInfo> infoList) {
        this.name = name;
        this.photo = photo;
        this.infoList = infoList;
    }

    public Contact(String name, List<ContactInfo> infoList) {
        this.name = name;
        this.infoList = infoList;
    }

    public List<ContactInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<ContactInfo> infoList) {
        this.infoList = infoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
