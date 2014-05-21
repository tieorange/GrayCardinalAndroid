package models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andriy on 21.05.2014.
 */
public class SerializableContact implements Serializable {

    private String contactName;
    private Bitmap contactPhoto;
    private List<SerializableContactInfo> contactInfoList;

    public SerializableContact(String contactName, Bitmap contactPhoto,
            List<SerializableContactInfo> contactInfoList) {
        this.contactName = contactName;
        this.contactPhoto = contactPhoto;
        this.contactInfoList = contactInfoList;
    }

    public SerializableContact(String contactName, Bitmap contactPhoto) {
        this.contactPhoto = contactPhoto;
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Bitmap getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(Bitmap contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    public List<SerializableContactInfo> getContactInfoList() {
        return contactInfoList;
    }

    public void setContactInfoList(List<SerializableContactInfo> contactInfoList) {
        this.contactInfoList = contactInfoList;
    }
}
