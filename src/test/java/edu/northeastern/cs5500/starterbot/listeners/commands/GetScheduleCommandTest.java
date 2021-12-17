package edu.northeastern.cs5500.starterbot.listeners.commands;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetScheduleCommandTest {
    private GetScheduleCommand getSchedule;
    private DiscordIdController discordIdController;
    private InMemoryRepository<NEUUser> inMemoryRepository;

    @BeforeEach
    void initialize() {
        inMemoryRepository = new InMemoryRepository<>();
        discordIdController = new DiscordIdController(inMemoryRepository);
        getSchedule = new GetScheduleCommand(discordIdController);
    }

    @Test
    public void testGetName() {
        assertEquals(getSchedule.getName(), "schedule");
    }

    @Test
    public void testToTitleCase() {
        assertEquals(GetScheduleCommand.toTitleCase("testString"), "Teststring");
    }

    @Test
    public void testGetCommandData() {
        assertEquals(
                getSchedule.getCommandData().getDescription(),
                "Get your scheduled office hours for the week or a given day.");
        assertFalse(getSchedule.getCommandData().getOptions().isEmpty());
        assertEquals(getSchedule.getCommandData().getOptions().get(0).getName(), "dayofweek");
        assertEquals(
                getSchedule.getCommandData().getOptions().get(0).getDescription(),
                "Monday/Tuesday/Wednesday/Thursday/Friday/Saturday/Sunday; if empty, the entire week is displayed");
    }

    @Test
    public void testGetReplyForNonRegisteredUser() {
        assertThat(getSchedule.getReply(null, null)).isNotNull();
    }

    @Test
    public void testGetReplyForUserWithNoOfficeHour() {
        assertThat(getSchedule.getReply(new NEUUser(), null)).isNotNull();
    }

    @Test
    public void testGetReplyForInvaliedDayOfWeek() {
        MessageBuilder mb = new MessageBuilder();
        mb.append("Please enter a valid day of the week (case-insensitive); e.g. 'Monday'");
        NEUUser neuUser = new NEUUser();
        OfficeHour officeHour = new OfficeHour();
        List<OfficeHour> officeHours = new ArrayList<>();
        officeHours.add(officeHour);
        neuUser.setInvolvedOfficeHours(officeHours);
        assertEquals(mb.build(), getSchedule.getReply(neuUser, "THIS_IS_A_WRONG_DAY_OF_WEEK"));
    }

    @Test
    public void testGetEntireWeekReplyForEmptyListGiven() {
        List<OfficeHour> officeHours = new ArrayList<>();
        // Check for empty office hour list.
        assertEquals(
                "(no office hours for the week)",
                getSchedule.getEntireWeekReply(officeHours).getDescription());
    }

    @Test
    public void testGetEntireWeekReply() {
        List<OfficeHour> officeHours = new ArrayList<>();

        OfficeHour officeHour =
                new OfficeHour(DayOfWeek.MONDAY, new OfficeHourType("remote"), 12, 13, "1234");
        officeHours.add(officeHour);
        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(
                officeHour.getDayOfWeek().toString(),
                String.format(
                        "%d:00 to %d:00; %s",
                        officeHour.getStartHour(),
                        officeHour.getEndHour(),
                        officeHour.getOfficeHourType().getTypeName()),
                false);
        assertEquals(
                "Monday", getSchedule.getEntireWeekReply(officeHours).getFields().get(0).getName());
        assertEquals(
                "12:00 to 13:00; remote",
                getSchedule.getEntireWeekReply(officeHours).getFields().get(0).getValue());
    }

    @Test
    public void testGetSingleDayReply() {
        List<OfficeHour> officeHours = new ArrayList<>();

        OfficeHour officeHour =
                new OfficeHour(DayOfWeek.FRIDAY, new OfficeHourType("remote"), 2, 3, "1234");
        officeHours.add(officeHour);
        assertEquals(
                "Friday",
                getSchedule.getSingleDayReply(officeHours, "FRIDAY").getFields().get(0).getName());
        assertEquals(
                "2:00 to 3:00; remote",
                getSchedule.getSingleDayReply(officeHours, "FRIDAY").getFields().get(0).getValue());
    }

    @Test
    public void testGetSingleDayReplyForEmptyListGiven() {
        List<OfficeHour> officeHours = new ArrayList<>();
        // Check for empty office hour list.
        assertEquals(
                "(no appointments for this day)",
                getSchedule.getSingleDayReply(officeHours, "firday").getDescription());
    }
}
