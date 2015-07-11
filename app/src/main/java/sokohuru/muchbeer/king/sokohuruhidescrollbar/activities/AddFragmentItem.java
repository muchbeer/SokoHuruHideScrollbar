package sokohuru.muchbeer.king.sokohuruhidescrollbar.activities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by muchbeer on 4/6/2015.
 */

public class AddFragmentItem {

   // public static final String[] PLANETS = {"sun","mercury","venus","earth","mars","jupiter","saturn","uranus","neptune"};
    public static final String[] SOKONI = {"home","page","product","services"};

    public static final Map<String, String> SOKONI_DETAIL;
    static {
        Map<String, String> soko = new HashMap<String, String>();
        soko.put("home", "Name of the item");
        soko.put("page", "Location of the item");
        soko.put("product", "Contact of the item");
        soko.put("services", "Number of seller");
        SOKONI_DETAIL = Collections.unmodifiableMap(soko);
    }

    public static Map<String, String> SOKONI_DETAIL_BELOW;
    static {
        Map<String, String> soko_below = new HashMap<String, String>();
        soko_below.put("home", "Price of the item");
        soko_below.put("page", "Picture of the item");
        soko_below.put("product", "Brand of the item");
        soko_below.put("services", "Contact of seller");
        SOKONI_DETAIL_BELOW = Collections.unmodifiableMap(soko_below);
    }

    public static Map<String, String> SOKONI_DETAIL_TEXT;
    static {
        Map<String, String> soko_text = new HashMap<String, String>();
        soko_text.put("home", "1");
        soko_text.put("page", "2");
        soko_text.put("product", "3");
        soko_text.put("services", "4");
        SOKONI_DETAIL_TEXT = Collections.unmodifiableMap(soko_text);
    }

    public static Map<String, String> SOKONI_DETAIL_TEXT_BELOW;
    static {
        Map<String, String> soko_text_below = new HashMap<String, String>();
        soko_text_below.put("home", "1");
        soko_text_below.put("page", "2");
        soko_text_below.put("product", "3");
        soko_text_below.put("services", "4");
        SOKONI_DETAIL_TEXT_BELOW = Collections.unmodifiableMap(soko_text_below);
    }
}
