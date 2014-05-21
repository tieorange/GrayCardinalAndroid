package models;

/**
 * Created by Andriy on 21.05.2014.
 */
public class SerializableContactInfo {

    private String infoName;
    private String infoValue;

    public SerializableContactInfo(String infoName,
            String infoValue) {
        this.infoName = infoName;
        this.infoValue = infoValue;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public String getInfoValue() {
        return infoValue;
    }

    public void setInfoValue(String infoValue) {
        this.infoValue = infoValue;
    }
}
