package org.openstreetmap.josm.plugins.ohmdatefilter;

import java.util.Date;

/**
 *
 * @author rub21
 */
public class OHMDateObject {

    private String strDate;
    private Date date;

    // Constructor to initialize the date string
    public OHMDateObject(String date) {
        this.strDate = date;
    }

    public Date getDate() {
        String currentDate_str = UtilDates.formatDate(strDate);
        Date currentDate = UtilDates.stringToDate(currentDate_str);
        
        return currentDate;
    }

    // Setter method to set a new date string
    public void setDate(String date) {
        this.strDate = date;
    }

    
    
    // Setter method to set a new date string
    public void setDate(Date date) {
        this.date = date;
    }
    // Override the toString method to provide a string representation of the object
    @Override
    public String toString() {
        return "OHMDateObject{" + "date=" + strDate + '}';
    }
}
