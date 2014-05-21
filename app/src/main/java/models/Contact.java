package models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Table(name = "Contacts")
public class Contact extends Model implements Serializable {

    @Column(name = "Name", notNull = true)
    private String name;
    @Column(name = "PhotoPath")
    private String photoName;

    public Contact() {
        super();
    }

    public Contact(String name) {
        super();
        this.name = name;
    }

    public Contact(String name, String photo) {
        super();
        this.name = name;
        this.photoName = photo;
    }


    public Contact(String name, String photo, List<ContactInfo> infoList) {
        super();
        this.name = name;
        this.photoName = photo;
    }

    public Contact(String name, List<ContactInfo> infoList) {
        super();
        this.name = name;
    }

    public static Contact getFirst() {
        return new Select()
                .from(Contact.class)
                .executeSingle();
    }

    @Override
    public boolean equals(Object obj) {
        //compare contacts by name (for avoiding duplication)
        return new String(((Contact) obj).getName()).equals(this.getName());
    }

    public List<ContactInfo> infoList() {
        List<ContactInfo> contacts = getMany(ContactInfo.class, "Contact");
        Collections.reverse(contacts);
        return contacts;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
