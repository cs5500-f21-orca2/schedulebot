package edu.northeastern.cs5500.starterbot.listeners.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import net.dv8tion.jda.api.entities.MessageEmbed;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReserveCommandTest {
    private ReserveCommand reserve;
    private DiscordIdController discordIdController;
    private InMemoryRepository<NEUUser> inMemoryRepository;
    private NEUUser newUser;
    private List<OfficeHour> involvedOfficeHours;
    private OfficeHour newOH;
    private GenericRepository<NEUUser> userRepository;

    @BeforeEach
    void initialize() {
        inMemoryRepository = new InMemoryRepository<>();
        discordIdController = new DiscordIdController(inMemoryRepository);
        reserve = new ReserveCommand(discordIdController);
        newUser = new NEUUser();
        involvedOfficeHours = new ArrayList<OfficeHour>();
        newOH = new OfficeHour();
    }

    @Test
    void testGetCommandData() {
        assertEquals(reserve.getCommandData().getDescription(), "Make a reservation");
        assertFalse(reserve.getCommandData().getOptions().isEmpty());
        assertEquals(reserve.getCommandData().getOptions().get(0).getName(), "dayofweek");
        assertEquals(
                reserve.getCommandData().getOptions().get(0).getDescription(), "Enter day of week");
        assertEquals(reserve.getCommandData().getOptions().get(1).getName(), "starttime");
        assertEquals(
                reserve.getCommandData().getOptions().get(1).getDescription(), "Enter start time");
        assertEquals(reserve.getCommandData().getOptions().get(2).getName(), "endtime");
        assertEquals(
                reserve.getCommandData().getOptions().get(2).getDescription(), "Enter end time");
        assertEquals(reserve.getCommandData().getOptions().get(3).getName(), "staffname");
        assertEquals(
                reserve.getCommandData().getOptions().get(3).getDescription(), "Enter Staff name");
        assertEquals(reserve.getCommandData().getOptions().get(4).getName(), "type");
        assertEquals(reserve.getCommandData().getOptions().get(4).getDescription(), "Enter type");
    }

    @Test
    void testGetName() {
        assertEquals(reserve.getName(), "reserve");
    }

    @Test
    void testGetReply() {
        assertThat(reserve.getReply(newUser, "Friday", "Online", 2, 3, "yu")).isNotNull();
        ;

    }

    @Test
    void testGetReplyValid() {
        newUser.setStaff(true);
        assertThat(reserve.getReply(null, "Friday", "Online", 2, 3, "yu")).isNotNull();
        assertThat(reserve.getReply(newUser, "Friday", "Online", 2, 3, "yu")).isNotNull();
    }

    @Test
    void testGetReplyVaccine() {
        newUser.setSymptomatic(true);
        ;
        assertThat(reserve.getReply(newUser, "Friday", "Inperson", 2, 3, "yu")).isNotNull();
        newUser.setVaccinated(false);
        assertThat(reserve.getReply(newUser, "Friday", "Inperson", 2, 3, "yu")).isNotNull();
    }

    @Test
    void testGetReserveReplyNoTA() {
        assertThat(reserve.getReserveReply("Online", "Friday", 1, 2, "yu", newUser)).isNotNull();
    }

    @Test
    void testGetReserveReplyDuplicateOH() {
        assertThat(reserve.getReserveReply("Online", "Friday", 1, 2, "yu", newUser)).isNotNull();
    }

    @Test
    void testIsValidDayOfWeek() {
        assertTrue(reserve.isValidDayOfWeek("Monday"));
        assertFalse(reserve.isValidDayOfWeek("monday"));
    }

    @Test
    void testIsValidType() {
        assertTrue(reserve.isValidType("Inperson"));
        assertFalse(reserve.isValidType("inperson"));
    }

    @Test
    void testGetReserveReplyNoStaff(){
        NEUUser student = new NEUUser();
        newUser.setUserName("shen");
        newUser.setStaff(true);
        newOH.setDayOfWeek(DayOfWeek.FRIDAY);
        newOH.setStartHour(1);
        newOH.setEndHour(2);
        involvedOfficeHours.add(newOH);
        newUser.setInvolvedOfficeHours(involvedOfficeHours);
        inMemoryRepository.add(newUser);
        assertThat(reserve.getReserveReply("Online", "Friday", 1, 2, "shen", student)).isNotNull();
        student.setInvolvedOfficeHours(involvedOfficeHours);
        assertThat(reserve.getReserveReply("Online", "Friday", 1, 2, "Shen", student)).isNotNull();
    }

    @Test
    void testOnSlashCommand() {

    }

    @Test
    void testToTitleCase() {
        assertEquals(ReserveCommand.toTitleCase("testString"), "Teststring");
        assertEquals(ReserveCommand.toTitleCase(""), "");
    }

    @Test
    void testCheckDuplicateOfficeHour() {
        NEUUser newUser = new NEUUser();
        List<OfficeHour> involvedOfficeHours = new ArrayList<OfficeHour>();
        OfficeHour newOH = new OfficeHour();
        newOH.setDayOfWeek(DayOfWeek.FRIDAY);
        newOH.setStartHour(1);
        newOH.setEndHour(2);
        involvedOfficeHours.add(newOH);
        newUser.setInvolvedOfficeHours(involvedOfficeHours);
        assertTrue(reserve.checkDuplicateOfficeHour("Friday", 1, 2, newUser));
        assertFalse(reserve.checkDuplicateOfficeHour("Monday", 1, 2, newUser));
        assertFalse(reserve.checkDuplicateOfficeHour("Friday", 2, 3, newUser));
        assertThat(reserve.getReserveReply("Online", "Friday", 1, 2, "yu", newUser)).isNotNull();
    }

    @Test
    void testCheckMatchingOH() {
        newUser.setUserName("shen");
        newOH.setDayOfWeek(DayOfWeek.FRIDAY);
        newOH.setStartHour(1);
        newOH.setEndHour(2);
        involvedOfficeHours.add(newOH);
        newUser.setInvolvedOfficeHours(involvedOfficeHours);
        Collection<NEUUser> taProfListNonEmpty = new ArrayList<NEUUser>();
        taProfListNonEmpty.add(newUser);
        NEUUser student = new NEUUser();
        assertTrue(reserve.checkMatchingOH(newUser, "Friday", 1, 2, "Online", student));
        assertFalse(reserve.checkMatchingOH(newUser, "Monday", 1, 2, "Online", student));
        assertFalse(reserve.checkMatchingOH(newUser, "Friday", 2, 3, "Online", student));
    }

    @Test
    void testCheckNoStaff() {
        Collection<NEUUser> taProfList = discordIdController.getAllTAProf();
        assertNull(reserve.checkNoStaff(taProfList, "yu"));
        NEUUser newUser = new NEUUser();
        newUser.setUserName("shen");
        Collection<NEUUser> taProfListNonEmpty = new ArrayList<NEUUser>();
        taProfListNonEmpty.add(newUser);
        assertEquals(reserve.checkNoStaff(taProfListNonEmpty, "Shen"), newUser);
        assertThat(reserve.getReserveReply("Online", "Monday", 1, 2, "yu", newUser)).isNotNull();
    }
}
