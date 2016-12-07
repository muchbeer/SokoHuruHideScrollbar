package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.testing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.HashMap;

/**
 * Created by muchbeer on 11/30/2016.
 */

public class TestingList {

    //name and address string
    private String name;
    private String address;
    private HashMap<String, Object> districtz;
    private HashMap<String, Object> timestampLastChanged;

    public TestingList() {
      /*Blank default constructor essential for Firebase*/
    }

    public TestingList(String name, String address, HashMap<String, Object> districtz) {
        this.name = name;
        this.address = address;

        this.districtz = districtz;


        HashMap<String, Object> timestampLastChangedObj = new HashMap<String, Object>();
        timestampLastChangedObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampLastChangedObj;

    }
    //Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public HashMap<String, Object> getDistrictz() {
        return districtz;
    }

    @JsonIgnore
    public String getDistrictzLong() {

        return (String) districtz.get("nestedFirebase");

        // return (long) timestampLastChanged.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);

    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }


    @JsonIgnore
    public long getTimestampLastChangedLong() {

        return (long) timestampLastChanged.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
    }
}
