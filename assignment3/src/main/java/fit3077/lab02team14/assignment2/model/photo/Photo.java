
package fit3077.lab02team14.assignment2.model.photo;

import javax.annotation.Generated;
import java.util.Date;

@Generated("net.hexar.json2pojo")
public class Photo {

    private PhotoAdditionalInfo additionalInfo;
    private Date dateCreated;
    private String fileName;
    private String id;
    private String mimeType;

    public PhotoAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(PhotoAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "additionalInfo=" + additionalInfo +
                ", dateCreated=" + dateCreated +
                ", fileName='" + fileName + '\'' +
                ", id='" + id + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }
}
