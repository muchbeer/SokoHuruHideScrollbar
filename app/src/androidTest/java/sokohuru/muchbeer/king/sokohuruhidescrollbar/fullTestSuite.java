package sokohuru.muchbeer.king.sokohuruhidescrollbar;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by muchbeer on 11/18/2016.
 */

public class fullTestSuite extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(fullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public fullTestSuite() {
        super();
    }
}
