package de.admadic.units.core;

import junit.framework.TestCase;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UnitsTsTest extends TestCase {
    public void testTsParsing() {
        try {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date now = new Date();
            String ds = df.format(now);
            System.out.println(ds);
            Date d = df.parse("Wednesday, January 11, 2006 at 8:25:25 PM GMT");
            assertNotNull(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
