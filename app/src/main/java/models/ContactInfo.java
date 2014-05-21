package models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ContactsInfo")
public class ContactInfo extends Model {

    @Column(name = "Contact", notNull = true,
            onNullConflict = Column.NullConflictAction.ABORT)
    public Contact contact;
    @Column(name = "Name")
    private String name;
    @Column(name = "Value")
    private String value;

    public ContactInfo() {
        super();
    }

    public ContactInfo(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public ContactInfo(String name, String value, Contact contact) {
        this.contact = contact;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
