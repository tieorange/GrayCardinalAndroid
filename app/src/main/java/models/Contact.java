package models;

import android.graphics.Bitmap;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Contacts")
public class Contact extends Model {
    @Column(name = "Name", onDelete = Column.ForeignKeyAction.CASCADE)
    private String name;
    @Column(name = "Photo")
    private Bitmap photo;


    public Contact() {
        super();
    }

    public Contact(String name) {
        super();
        this.name = name;
        //this.infoList();
    }

    public Contact(String name, Bitmap photo) {
        super();
        this.name = name;
        this.photo = photo;
        //this.infoList = new ArrayList<ContactInfo>();
    }


    public Contact(String name, Bitmap photo, List<ContactInfo> infoList) {
        super();
        this.name = name;
        this.photo = photo;
        //this.infoList = infoList;
    }

    public Contact(String name, List<ContactInfo> infoList) {
        super();
        this.name = name;
        //this.infoList = infoList;
    }

    public static Contact getFirst() {
        return new Select()
                .from(Contact.class)
                .executeSingle();
    }

    public List<ContactInfo> infoList() {
        return getMany(ContactInfo.class, "Contact");
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
