package edu.northeastern.cs5500.starterbot.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OfficeHourTest {
    @Test
    void testOffcieHour() {
        OfficeHour monday1213 =
                new OfficeHour(DayOfWeek.MONDAY, new OfficeHourType("remote"), 12, 13, "1234");
        OfficeHour monday1314 =
                new OfficeHour(DayOfWeek.MONDAY, new OfficeHourType("remote"), 13, 14, "1234");
        OfficeHour tuesday1213 =
                new OfficeHour(DayOfWeek.TUESDAY, new OfficeHourType("remote"), 12, 13, "1234");
        OfficeHour tuesday1314 =
                new OfficeHour(DayOfWeek.TUESDAY, new OfficeHourType("remote"), 13, 14, "1234");
        OfficeHour friday1011 =
                new OfficeHour(DayOfWeek.FRIDAY, new OfficeHourType("remote"), 10, 11, "1234");

        assertTrue(monday1213.compareTo(monday1314) < 0);
        assertTrue(monday1213.compareTo(tuesday1213) < 0);
        assertTrue(monday1213.compareTo(tuesday1314) < 0);
        assertTrue(monday1213.compareTo(friday1011) < 0);
        assertTrue(tuesday1213.compareTo(friday1011) < 0);
        assertTrue(tuesday1314.compareTo(friday1011) < 0);
    }
}
