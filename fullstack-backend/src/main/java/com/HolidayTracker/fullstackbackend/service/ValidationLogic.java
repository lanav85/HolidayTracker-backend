package com.HolidayTracker.fullstackbackend.service;

import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class ValidationLogic {

    public long countWeekdays(Date requestFrom, Date requestTo) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(requestFrom);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(requestTo);

        long weekdays = 0;
        // Iterate through each day between the start and end dates
        while (!startCalendar.after(endCalendar)) {
            int dayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                weekdays++;
            }
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return weekdays;
    }

}
