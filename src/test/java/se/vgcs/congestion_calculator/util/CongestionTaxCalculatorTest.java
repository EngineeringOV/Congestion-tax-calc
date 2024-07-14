package se.vgcs.congestion_calculator.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static se.vgcs.congestion_calculator.util.CongestionTaxCalculator.IsTollFreeDate;
import static se.vgcs.congestion_calculator.util.CongestionTaxCalculator.getTax;

public class CongestionTaxCalculatorTest {

    @Test
    public void noDatesFixTest1() {
        int tax = getTax("Car", null);

        assertEquals(0, tax);
    }

    @Test
    public void noDatesFixTest2() {
        Date[] noDates = new Date[5];
        int tax = getTax("Car", noDates);

        assertEquals(0, tax);
    }

    @Test
    public void IsTollFreeDateFixTest() {
        Date date = new Date(2013-1900, Calendar.JANUARY, 1, 15, 35);
        Boolean taxFreeDate = IsTollFreeDate(date);
        assertEquals(true, taxFreeDate);
    }

    @Test
    public void IsTollFreeCarTypeTest() {
        Date date = new Date(2024, 9, 9, 15, 35);
        Date[] dates = {date};
        int taxVehicle1 = getTax("Emergency", dates);
        int taxVehicle2 = getTax("Motorcycle", dates);
        int taxVehicle3 = getTax("Tractor", dates);
        int taxVehicle4 = getTax("Diplomat", dates);
        int taxVehicle5 = getTax("Foreign", dates);
        int taxVehicle6 = getTax("Military", dates);

        assertEquals(0, taxVehicle1);
        assertEquals(0, taxVehicle2);
        assertEquals(0, taxVehicle3);
        assertEquals(0, taxVehicle4);
        assertEquals(0, taxVehicle5);
        assertEquals(0, taxVehicle6);
    }

    @Test
    public void IsNotTollFreeCarTypeTest() {
        Date date = new Date(2024, 9, 9, 15, 35);
        Date[] dates = {date};
        int taxVehicle = getTax("Car", dates);

        assertEquals(18, taxVehicle);
    }

    @Test
    public void maxTaxTest() {
        //todo should've regex'd find and replace into an array of dates directly
        String[] dateStrings = {
                "2013-01-14 21:00:00",
                "2013-01-15 21:00:00",
                "2013-02-07 06:23:27",
                "2013-02-07 15:27:00",
                "2013-02-08 06:27:00",
                "2013-02-08 06:20:27",
                "2013-02-08 14:35:00",
                "2013-02-08 15:29:00",
                "2013-02-08 15:47:00",
                "2013-02-08 16:01:00",
                "2013-02-08 16:48:00",
                "2013-02-08 17:49:00",
                "2013-02-08 18:29:00",
                "2013-02-08 18:35:00",
                "2013-03-26 14:25:00",
                "2013-03-28 14:07:27"
        };

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date[] dates = new Date[dateStrings.length];

        for (int i = 0; i < dateStrings.length; i++) {
            try {
                dates[i] = dateFormat.parse(dateStrings[i]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int taxVehicle = getTax("Car", dates);

        assertEquals(89, taxVehicle);
    }

}
