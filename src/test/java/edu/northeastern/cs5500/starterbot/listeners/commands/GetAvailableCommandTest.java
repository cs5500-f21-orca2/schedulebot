package edu.northeastern.cs5500.starterbot.listeners.commands;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetAvailableCommandTest {
    private GetAvailableCommand getAvailableCommand;
    private DiscordIdController discordIdController;
    private InMemoryRepository<NEUUser> inMemoryRepository;

    /** Test statement setup before each test */
    @BeforeEach
    void initialize() {
        inMemoryRepository = new InMemoryRepository<>();
        discordIdController = new DiscordIdController(inMemoryRepository);
        getAvailableCommand = new GetAvailableCommand(discordIdController);
    }

    /** Testing getName function */
    @Test
    void testGetName() {
        assertThat(getAvailableCommand.getName()).isNotEmpty();
    }

    /** Test GetCommandData function */
    @Test
    void testGetCommandData() {
        assertThat(getAvailableCommand.getCommandData()).isNotNull();
    }

    @Test
    void testGetCommandDataIsConsistent() {
        assertThat(getAvailableCommand.getCommandData().getName())
                .isEqualTo(getAvailableCommand.getName());
    }

    /** Test getSingleDayReply function when the user is not registed. */
    @Test
    void getSingleDayReplyTestForNonRegisteredUser() {
        assertThat(getAvailableCommand.getReply(null, null)).isNotNull();
    }

    /** Test GetReply function when the user has no reserved office hour. */
    @Test
    public void testGetReplyForUserWithNoOfficeHour() {
        assertThat(getAvailableCommand.getReply(new NEUUser(), null)).isNotNull();
    }

    /** Test GetReply function when user passed in a invalid day of week. */
    @Test
    public void testGetReplyForInvaliedDayOfWeek() {
        NEUUser neuUser = new NEUUser();
        OfficeHour officeHour = new OfficeHour();
        List<OfficeHour> officeHours = new ArrayList<>();
        officeHours.add(officeHour);
        neuUser.setInvolvedOfficeHours(officeHours);
        assertThat(getAvailableCommand.getReply(neuUser, "THIS_IS_A_WRONG_DAY_OF_WEEK"))
                .isNotNull();
    }

    @Test
    public void testIsValidDayOfWeek() {
        assertTrue(getAvailableCommand.isValidDayOfWeek("Monday"));
        assertFalse(getAvailableCommand.isValidDayOfWeek("A_WRONG_DAY_OF_WEEK"));
    }

    @Test
    void getSingleDayReplyTestForNonStudentUser() {
        NEUUser user = new NEUUser();
        user.setStaff(true);
        assertThat(getAvailableCommand.getReply(user, null)).isNotNull();
    }

    @Test
    void getReplyTestForNonStudentUser() {
        NEUUser user = new NEUUser();
        user.setStaff(true);
        assertThat(getAvailableCommand.getReply(user, null)).isNotNull();
    }

    /** Test GetEntireWeekReply function when user has no reserved office hour. */
    @Test
    public void testGetEntireWeekReplyForEmptyListGiven() {
        List<OfficeHour> officeHours = new ArrayList<>();
        MessageEmbed messageEmbed = getAvailableCommand.getEntireWeekReply(officeHours);
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(messageEmbed.getFields()).isEmpty();
    }

    /** Test GetEntireWeekReply function. */
    @Test
    public void testGetEntireWeekReply() {
        List<OfficeHour> officeHours = new ArrayList<>();

        OfficeHour officeHour =
                new OfficeHour(DayOfWeek.MONDAY, new OfficeHourType("remote"), 12, 13, "1234");
        officeHours.add(officeHour);
        MessageEmbed messageEmbed = getAvailableCommand.getEntireWeekReply(officeHours);

        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
    }

    /** Test GetSingleDayReply function when user has no reserved office hour. */
    @Test
    public void testGetSingleDayReplyForEmptyListGiven() {
        List<OfficeHour> officeHours = new ArrayList<>();
        // Check for empty office hour list.
        MessageEmbed messageEmbed = getAvailableCommand.getSingleDayReply(officeHours, "firday");
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(messageEmbed.getFields()).isEmpty();
    }

    /** Test GetSingleDayReply function */
    @Test
    public void testGetSingleDayReply() {
        List<OfficeHour> officeHours = new ArrayList<>();

        OfficeHour officeHour =
                new OfficeHour(DayOfWeek.FRIDAY, new OfficeHourType("remote"), 2, 3, "1234");
        officeHours.add(officeHour);
        MessageEmbed messageEmbed = getAvailableCommand.getSingleDayReply(officeHours, "FRIDAY");
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(!messageEmbed.getFields().isEmpty());
    }
}
