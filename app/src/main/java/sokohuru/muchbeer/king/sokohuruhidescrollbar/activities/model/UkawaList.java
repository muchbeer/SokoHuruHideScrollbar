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
    private HashMap<String, Object> author;
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
    public UkawaList(String cdnm, String location, String mbunge, String diwan, HashMap<String, Object> author) {
        this.cdnm = cdnm;
        this.location = location;
        this.mbunge = mbunge;
        this.diwan=diwan;
        this.testing1= testing1;

        this.author = author;

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


    public HashMap<String, Object> getAuthor() {
        return author;
    }



    @JsonIgnore
    public String getAuthorLong() {

        return (String) author.get("gd001");
    }


}
