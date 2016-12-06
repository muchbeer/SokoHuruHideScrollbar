package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

/**
 * Created by muchbeer on 11/26/2016.
 */

public class UkawaList  {
    private String cdnm;
    private String location;
    private String mbunge;
    private String diwan;
    private String testing1;
    private HashMap<String, Object> alist;
    private HashMap<String, Object> testing2;

    /**
     * Required public constructor
     */
    public UkawaList() {
    }

    /**
     * Use this constructor to create new ShoppingLists.
     * Takes shopping list listName and owner. Set's the last
     * changed time to what is stored in ServerValue.TIMESTAMP
     *
     * @param cdnm
     * @param location
     *
     */
    public UkawaList(String cdnm, String location, String mbunge, String diwan) {
        this.cdnm = cdnm;
        this.location = location;
        this.mbunge = mbunge;
        this.diwan=diwan;
        this.testing1= testing1;


        HashMap<String, Object> alistObject = new HashMap<String, Object>();
     //alistObject.put("") timestampLastChangedObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        alistObject.put("description", "namad");
        alistObject.put("Gadiel", "My son");
       // this.testing2 = alistObject;
        this.alist = alistObject;

    }


    public UkawaList(String testing1) {

        this.testing1= testing1;


        HashMap<String, Object> alistObject = new HashMap<String, Object>();
        //alistObject.put("") timestampLastChangedObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
      //  alistObject.put("description", "namad");
        alistObject.put("Gadiel", "My son");
        // this.testing2 = alistObject;
        this.testing2 = alistObject;

    }

    public String getCdnm() {
        return cdnm;
    }

    public String getLocation() {
        return location;
    }

    public String getMbunge() {
        return mbunge;
    }

    public String getDiwan() {
        return diwan;
    }

    public String getTesting1() {
        return testing1;
    }

    public String setTesting1(String testing1) {
        return testing1;
    }
    public HashMap<String, Object> getAlist() {
        return alist;
    }

    public HashMap<String, Object> getAllTest() {
        return testing2;
    }

    @JsonIgnore
    public String getAlistString() {

        return (String) alist.get("description");
    }


}
