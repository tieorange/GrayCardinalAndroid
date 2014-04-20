package models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ContactsInfo")
public class ContactInfo extends Model {
    @Column(name = "Contact", onDelete = Column.ForeignKeyAction.CASCADE)
    public Contact contact;
    @Column(name = "Name")
    private String name;
    @Column(name = "content")
    private String content;

    public ContactInfo() {
        super();
    }

    public ContactInfo(String name, String content) {
        super();
        this.name = name;
        this.content = content;
    }

    public ContactInfo(String name, String content, Contact contact) {
        this.contact = contact;
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
