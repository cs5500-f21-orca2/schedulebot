package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class OfficeHour implements Comparable<OfficeHour> {
    @Nonnull private DayOfWeek dayOfWeek;
    @Nonnull private OfficeHourType officeHourType;
    @Nonnull private int startHour; // 0 = midnight, 23 = 11pm
    @Nonnull private int endHour; // 0 = midnight, 23 = 11pm
    @Nonnull private String hostNUID;
    private String attendeeNUID;

    @Override
    public int compareTo(OfficeHour other) {
        int a = this.getDayOfWeek().toInt();
        int b = other.getDayOfWeek().toInt();
        if (a != b) {
            return a - b;
        } else {
            return this.getStartHour() - other.getStartHour();
        }
    }

    public boolean matches(DayOfWeek dayOfWeek, int startHour, int endHour, String userNUID) {
        if (this.dayOfWeek != dayOfWeek || startHour != this.startHour || endHour != this.endHour) {
            return false;
        }
        // A user can either be a student and attending office hours or staff and hosting office
        // hours
        if (hostNUID.equalsIgnoreCase(userNUID)) {
            return true;
        }
        if (attendeeNUID != null && attendeeNUID.equalsIgnoreCase(userNUID)) {
            return true;
        }
        return false;
    }
}
