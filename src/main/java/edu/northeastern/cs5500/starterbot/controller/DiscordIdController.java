package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The DiscordIdController directly operate UserRepository(MongoDB) It has a Composition
 * relationship with each command.java
 */
@Slf4j
@RequiredArgsConstructor
public class DiscordIdController {
    @Nonnull private GenericRepository<NEUUser> neuUserRepository;

    /**
     * To get NUID by discordId
     *
     * @param discordId String of user discordId
     * @return user's nuid or null if user is not exist
     */
    public String getNuidByDiscordId(String discordId) {
        for (NEUUser user : neuUserRepository.getAll()) {
            if (user.getDiscordId() != null && user.getDiscordId().equals(discordId)) {
                return user.getNuid();
            }
        }
        return null;
    }

    /**
     * Confirm a discordId is already been registered
     *
     * @param discordId String of user discordId
     * @return true if discordId is registered false if discordId is not register
     */
    public boolean isDiscordIdRegistered(String discordId) {
        for (NEUUser user : neuUserRepository.getAll()) {
            if (user.getDiscordId() != null && user.getDiscordId().equals(discordId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * To get a NEU user by discordId
     *
     * @param discordId String of user discordId
     * @return NEUUser if input a correct discordId or null discordId not exist
     */
    public NEUUser getNEUUser(String discordId) {
        for (NEUUser user : neuUserRepository.getAll()) {
            if (user.getDiscordId().equals(discordId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * To get a NEU user by userName
     *
     * @param userName String of user userName
     * @return NEUUser if it can be found; otherwise null
     */
    public NEUUser getNEUUserByUserName(String userName) {
        for (NEUUser user : neuUserRepository.getAll()) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Create a NEU user into the neuUserRepository(MongoDB)
     *
     * @param name username of user
     * @param nuid user's nuid
     * @param role user's role student or ta or professor
     * @param discordId discordId of user
     * @return NEUUser if successfully created, null is Invalid role requested
     */
    public NEUUser createNEUUser(String name, String nuid, String role, String discordId) {
        boolean isStaff = false;
        switch (role) {
            case "ta":
            case "professor":
                isStaff = true;
                break;
            case "student":
                isStaff = false;
                break;
            default:
                log.warn("Invalid role requested: {}", role);
                return null;
        }

        NEUUser user = new NEUUser(name, nuid, discordId);
        user.setStaff(isStaff);
        return neuUserRepository.add(user);
    }

    /**
     * To get Collection of All TA and Professor
     *
     * @return an ArrayDeque of all TA and Professor
     */
    public Collection<NEUUser> getAllTAProf() {
        Deque<NEUUser> taProfList = new ArrayDeque<>();
        for (NEUUser user : neuUserRepository.getAll()) {
            if (user.isStaff() == true) {
                taProfList.add(user);
            }
        }
        return taProfList;
    }

    /**
     * Confirm and update a user's vaccination
     *
     * @param discordId user's discordId
     * @param vaccinated user's vaccination state
     * @return return true if successfully updated, false user not exist
     */
    public boolean updateVaccination(String discordId, boolean vaccinated) {
        NEUUser user = getNEUUser(discordId);
        if (user == null) {
            return false;
        }

        user.setVaccinated(vaccinated);
        neuUserRepository.update(user);
        return true;
    }

    /**
     * Confirm and update user's Symptomatic
     *
     * @param discordId user's discordId
     * @param symptomatic user's Symptomatic state
     * @return true if successfully updated, false user not exist
     */
    public boolean updateSymptomatic(String discordId, boolean symptomatic) {
        NEUUser user = getNEUUser(discordId);
        if (user == null) {
            return false;
        }

        user.setSymptomatic(symptomatic);
        neuUserRepository.update(user);
        return true;
    }

    /**
     * To set InvolvedOfficeHour List<OfficeHour> of a NEUUser
     *
     * @param discordId user's discordId
     * @param involvedOfficeHours List<OfficeHour> of a NEUUser
     * @return true if successfully updated, false user not exist
     */
    public boolean setInvolvedOfficeHours(String discordId, List<OfficeHour> involvedOfficeHours) {
        NEUUser user = getNEUUser(discordId);
        if (user == null) {
            return false;
        }

        user.setInvolvedOfficeHours(involvedOfficeHours);
        neuUserRepository.update(user);
        return true;
    }

    public boolean cancelOfficeHour(
            @Nonnull String discordId,
            @Nonnull DayOfWeek dayOfWeek,
            int startHour,
            int endHour,
            @Nonnull String staffUserName) {
        return cancelOfficeHour(
                getNEUUser(discordId), dayOfWeek, startHour, endHour, staffUserName);
    }

    public boolean cancelOfficeHour(
            @Nonnull NEUUser user,
            @Nonnull DayOfWeek dayOfWeek,
            int startHour,
            int endHour,
            @Nonnull String staffUserName) {
        NEUUser staffUser = getNEUUserByUserName(staffUserName);
        if (staffUser == null) {
            throw new IllegalArgumentException("staffUsername cannot be null.");
        }
        String staffNUID = staffUser.getNuid();
        String studentNUID = user.getNuid();

        List<OfficeHour> studentOfficeHours = user.getInvolvedOfficeHours();
        List<OfficeHour> staffOfficeHours = staffUser.getInvolvedOfficeHours();

        boolean foundOfficeHours = false;

        for (int i = 0; i < studentOfficeHours.size(); i++) {
            OfficeHour current = studentOfficeHours.get(i);

            if (!current.matches(dayOfWeek, startHour, endHour, staffNUID)) {
                continue;
            }

            studentOfficeHours.remove(i);
            foundOfficeHours = true;
        }

        if (!foundOfficeHours) {
            return false;
        }

        user.setInvolvedOfficeHours(studentOfficeHours);
        neuUserRepository.update(user);

        foundOfficeHours = false;

        for (int i = 0; i < staffOfficeHours.size(); i++) {
            OfficeHour current = staffOfficeHours.get(i);

            if (!current.matches(dayOfWeek, startHour, endHour, studentNUID)) {
                continue;
            }

            staffOfficeHours.get(i).setAttendeeNUID(null);
            staffOfficeHours.get(i).setOfficeHourType(new OfficeHourType("Online"));
            foundOfficeHours = true;
        }

        if (!foundOfficeHours) {
            try {
                assert false;
            } catch (AssertionError e) {
            }
        }

        staffUser.setInvolvedOfficeHours(staffOfficeHours);
        neuUserRepository.update(staffUser);

        return true;
    }

    /**
     * To get a NEUUser by user's NUID
     *
     * @param nuid user's NUID
     * @return NEUUser if nuid is valid, false if nuid is invaild
     */
    @Nullable
    public NEUUser getNEUUserByNuid(String nuid) {
        for (NEUUser user : this.neuUserRepository.getAll()) {
            if (user.getNuid().equals(nuid)) {
                return user;
            }
        }
        return null;
    }
}
