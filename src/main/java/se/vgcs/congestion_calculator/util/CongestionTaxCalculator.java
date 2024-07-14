package se.vgcs.congestion_calculator.util;

import lombok.SneakyThrows;
import se.vgcs.congestion_calculator.model.TollFreeVehicle;

import java.util.*;

public class CongestionTaxCalculator {

    //todo given more time I'd probably rewrite this into something more readable
    @SneakyThrows
    public static int getTax(String vehicleType, Date[] dates) {
        // Bug with empty dates list fixed by pre-check
        if(dates == null || Arrays.stream(dates).allMatch(Objects::isNull)) {
            return 0;
        }
        if(vehicleType == null){
            throw new TaxCalculationException("Not enough information for calculation");
        }
        Arrays.sort(dates);
        Date intervalStart = dates[0];
        Date currentDay = dates[0];
        int currentDailyTax = 0;
        int totalFee = 0;

//was missing daily limit
        for (Date date : dates) {
            if(!isSameDay(currentDay, date)){
                totalFee += currentDailyTax;
                currentDailyTax = 0;
                currentDay = date;
            }
            int nextFee = GetTollFee(date, vehicleType);
            int tempFee = GetTollFee(intervalStart, vehicleType);
            long diffInMillies = date.getTime() - intervalStart.getTime();
            long minutes = diffInMillies / 1000 / 60;

            if (minutes <= 60) {
                if (currentDailyTax > 0) currentDailyTax -= tempFee;
                if (nextFee >= tempFee) tempFee = nextFee;
                currentDailyTax += tempFee;

            } else {
                currentDailyTax += nextFee;
                //bug fix, interval not updating
                intervalStart = date;
            }
            currentDailyTax = Math.min(currentDailyTax, 60);

        }
        totalFee += currentDailyTax;
        return totalFee;
    }

    private static int GetTollFee(Date date, String vehicle) {
        if (IsTollFreeDate(date) || TollFreeVehicle.isTollFreeVehicle(vehicle)) {
            return 0;
        }
        //todo replace deprecated methods
        int hour = date.getHours();
        int minute = date.getMinutes();

        // todo given more time I would create a serialized object(s?) for this
        if (hour == 6 && minute >= 0 && minute <= 29) return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59) return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59) return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29) return 13;
        //bug with 30min loophole
        else if (hour >= 8 && hour <= 14 && minute <= 59) return 8;
        else if (hour == 15 && minute >= 0 && minute <= 29) return 13;
        //Minute was 0 when should be 30 but doesn't really matter as it would be caught by previous statement and && has precedence over || but specifying the OOO reads better IMO
        else if ((hour == 15 && minute >= 30) || (hour == 16 && minute <= 59)) return 18;
        else if (hour == 17 && minute >= 0 && minute <= 59) return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29) return 8;
        else return 0;
    }

     static Boolean IsTollFreeDate(Date date) {
        //todo replace deprecated methods
        int year = date.getYear();
        int month = date.getMonth() + 1;
        int day = date.getDay() + 1;
        int dayOfMonth = date.getDate();

        if (day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
            return true;
        }

         // Technically a bug not representing year as subtracted by 1900
        if (year == 2013-1900) {
            // todo given more time I would create a serialized object(s?) for this
            if (    (month == 1 && dayOfMonth == 1) ||
                    (month == 3 && (dayOfMonth == 28 || dayOfMonth == 29)) ||
                    (month == 4 && (dayOfMonth == 1 || dayOfMonth == 30)) ||
                    (month == 5 && (dayOfMonth == 1 || dayOfMonth == 8 || dayOfMonth == 9)) ||
                    (month == 6 && (dayOfMonth == 5 || dayOfMonth == 6 || dayOfMonth == 21)) ||
                    (month == 7) ||
                    (month == 11 && dayOfMonth == 1) ||
                    (month == 12 && (dayOfMonth == 24 || dayOfMonth == 25 || dayOfMonth == 26 || dayOfMonth == 31))) {
                return true;
            }
        }
        return false;
    }
    private static boolean isSameDay(Date date1, Date date2) {
        return (date1.getYear() == date2.getYear()) &&
                (date1.getMonth() == date2.getMonth()) &&
        (date1.getDay() == date2.getDay());
    }
}