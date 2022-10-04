
package fit3077.lab02team14.assignment2.model.booking;

import java.util.HashMap;

import javax.annotation.Generated;

import fit3077.lab02team14.assignment2.services.QRCode;

@Generated("net.hexar.json2pojo")
public class BookingAdditionalInfo {

    private static String meetingUrlTemplate = "https://monash.zoom.us/j/%s?pwd=%s";
    private HashMap<String, Object> postBookingAdditionalInfo;
    private String qRCode = "";
    private String url = "";
    private String imgPath = "";

    public BookingAdditionalInfo(String bookingId){
        makeQRCodeWithId(bookingId);
        makeUrlWithId(bookingId);
        this.setPostBookingAdditionalInfo();
    }

    public BookingAdditionalInfo(){
        this.setPostBookingAdditionalInfo();
    }

    public void makeQRCodeWithId(String bookingId) {
        this.qRCode = QRCode.generateByteQRCode(bookingId);
    }

    public void makeUrlWithId(String bookingId) {
        this.url = String.format(meetingUrlTemplate, bookingId, bookingId);
    }

    public HashMap<String, Object> getPostBookingAdditionalInfo() {
        return postBookingAdditionalInfo;
    }

    public String getqRCode() {
        return qRCode;
    }

    public void setqRCode(String qRCode) {
        this.qRCode = qRCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPostBookingAdditionalInfo() {
        this.postBookingAdditionalInfo = new HashMap<String, Object>(){{
            put("qRCode", qRCode);
            put("url", url);
        }};
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return "BookingAdditionalInfo{" +
                "meetingUrlTemplate='" + meetingUrlTemplate + '\'' +
                ", postBookingAdditionalInfo=" + postBookingAdditionalInfo +
                ", qRCode='" + qRCode + '\'' +
                ", url='" + url + '\'' +
                ", imgPath='" + imgPath + '\'' +
                '}';
    }
}
