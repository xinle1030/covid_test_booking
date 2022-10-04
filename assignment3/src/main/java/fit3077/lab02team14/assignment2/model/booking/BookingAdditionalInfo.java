package fit3077.lab02team14.assignment2.model.booking;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import fit3077.lab02team14.assignment2.model.modalInfo.BookingState;
import fit3077.lab02team14.assignment2.services.QRCode;

@Generated("net.hexar.json2pojo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingAdditionalInfo {

    private static String meetingUrlTemplate = "https://monash.zoom.us/j/%s?pwd=%s";
    private HashMap<String, Object> postBookingAdditionalInfo;
    private String qRCode = "";
    private String url = "";
    private String imgPath = "";
    private ArrayList<BookingState> bookingVersions = new ArrayList<>();
    
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
        ArrayList<Object> arrayList = new ArrayList<>();
        //Gson converts all attributes (testingSiteId and startTime) from bookingState into Json format
        Gson gson = new Gson();
        this.postBookingAdditionalInfo = new HashMap<String, Object>(){{
            put("qRCode", qRCode);
            put("url", url);
            for(BookingState bookingState : bookingVersions){
                //gson.ToJson(bookingState) will produce sample like {"testingSiteId":"ccad0b5b-0786-42d2-802d-3497c5eda14e","startTime":"2022-05-04T06:33"}
                arrayList.add(gson.toJson(bookingState));
                //Putting them together results in [{"testingSiteId":"ccad0b5b-0786-42d2-802d-3497c5eda14e","startTime":"2022-05-04T06:33"}, [{"testingSiteId":"ccad0b5b-0786-42d2-802d-3497c5eda14e","startTime":"2022-05-04T06:33"}]
            }
            put("bookingVersions", arrayList);
        }};
    }

    public String getImgPath() {
        return imgPath;
    }

    // public void setImgPath(String imgPath) {
    //     this.imgPath = imgPath;
    // }

    public void setImgPath(String bookingId) {
        String imgPath = QRCode.byteArrToImgPath(this.getqRCode(), bookingId);
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

    public void addMemento(BookingState bookingState){
        bookingVersions.add(bookingState);
    }

    public BookingState getMemento(Integer stateVersion){
        return bookingVersions.get(stateVersion);
    }
    //BookingAdditionalInfo acts as caretaker
    public ArrayList<BookingState> getBookingVersions() {
        return bookingVersions;
    }

    public void setBookingVersions(ArrayList<BookingState> bookingVersions) {
        this.bookingVersions = bookingVersions;
    }
}
