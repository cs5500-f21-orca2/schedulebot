package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class NEUUser implements Model {
    private ObjectId id;
    @Nonnull private String discordId;
    @Nonnull private String userName;
    @Nonnull private String nuid;
    @Nonnull private String role;
    private boolean isVaccinated = false;
    private boolean symptom = false;
    List<OfficeHour> involvedOfficeHours = new ArrayList<OfficeHour>();

    public boolean isStaff() {
        switch (role) {
            case "ta":
            case "professor":
                return true;
            default:
                return false;
        }
    }
}
