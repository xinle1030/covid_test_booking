package fit3077.lab02team14.assignment2.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Util{

    // ISO 8601 BASIC is used by the API signature
    public static String ISO_8601BASIC_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static String LOCAL_DATETIME_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    public static String JAVA_DATETIME_FORMAT = "E MMM dd HH:mm:ss z yyyy";
    public static String TIME_FORMAT = "hh:mm a";
    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static boolean isIsoTimestamp(String s) {

        return s.matches("\\d{8}T\\d{6}Z");
    }

    public static String parseLocalDateTime(Date date)
    {
        SimpleDateFormat newDateFormat = new SimpleDateFormat(LOCAL_DATETIME_FORMAT);
        String retDate = newDateFormat.format(date);
        return retDate;
    }
    
    public static String parseIsoDateTime(String s)
    {
        SimpleDateFormat prevFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat javaFormat = new SimpleDateFormat(JAVA_DATETIME_FORMAT);
        SimpleDateFormat newDateFormat = new SimpleDateFormat(ISO_8601BASIC_DATE_PATTERN);
        Date date = new Date();
        try {
            date = prevFormat.parse(s);
        } catch (ParseException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
        try {
            date = javaFormat.parse(s);
        } catch (ParseException e) {
            // Auto-generated catch block
            e.printStackTrace();
        }
        String retDate = newDateFormat.format(date);
        System.out.println("retDate: " + retDate);
        return retDate;
    }

    public static boolean nowInZone(Date compareDate1, Date compareDate2){
        LocalTime now = LocalDateTime.ofInstant(new Date().toInstant(),ZoneId.systemDefault()).toLocalTime();
        LocalTime time1 = LocalDateTime.ofInstant(compareDate1.toInstant(),ZoneId.systemDefault()).toLocalTime();
        LocalTime time2 = LocalDateTime.ofInstant(compareDate2.toInstant(),ZoneId.systemDefault()).toLocalTime();
        Boolean targetInZone = ( 
            now.isAfter(time1) && 
            now.isBefore(time2)); 
        return targetInZone;
    }

    public static boolean beforeZone(Date compareDate1, Date compareDate2){
        LocalTime now = LocalDateTime.ofInstant(new Date().toInstant(),ZoneId.systemDefault()).toLocalTime();
        LocalTime time1 = LocalDateTime.ofInstant(compareDate1.toInstant(),ZoneId.systemDefault()).toLocalTime();
        LocalTime time2 = LocalDateTime.ofInstant(compareDate2.toInstant(),ZoneId.systemDefault()).toLocalTime();
        Boolean targetInZone = ( 
            now.isBefore(time1) && 
            now.isBefore(time2)); 
        return targetInZone;
    }

    public static String timeFormatting(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        String timeFormat = sdf.format(date);
        return timeFormat;
    }
    
    public static boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    public static boolean compareDateTime(Date date1, Date cutOffDate){
        Date nowDate = new Date();
        return isSameDay(nowDate, date1) && beforeZone(date1, cutOffDate);
    }

    public static ArrayList<String> convertStringToArray(String s){
        if(s != null) {
            String replace = s.replace("[", "");
            String replace1 = replace.replace("]", "");
            String replace2 = replace1.replaceAll(", ", ",");
            return new ArrayList<String>(Arrays.asList(replace2.split(",")));
        }
        return null;
    }

    public static String convertToJavaStringDateTime(String s){
        SimpleDateFormat isoDateFormat = new SimpleDateFormat(ISO_8601BASIC_DATE_PATTERN);
        String retDate = null;
        try{
            retDate = isoDateFormat.parse(s).toString();
        } catch (ParseException e){
            e.printStackTrace();
        }
        return retDate;
    }

    public static Date convertToJavaDateTime(String s){
        SimpleDateFormat isoDateFormat = new SimpleDateFormat(ISO_8601BASIC_DATE_PATTERN);
        SimpleDateFormat prevFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date retDate = null;
        try{
            retDate = isoDateFormat.parse(s);
        } catch (ParseException e){
            e.printStackTrace();
        }
        try{
            retDate = prevFormat.parse(s);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return retDate;
    }

    public static boolean checkIfLapsed(Date targetDate){
        Date date = new Date();
        LocalDateTime now = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        //Assume time is in Java format
//        Date date1 = Util.convertToJavaDateTime(time);
        if (targetDate == null){
            return false;
        }
        LocalDateTime targetTime = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return now.isAfter(targetTime);
    }



}
