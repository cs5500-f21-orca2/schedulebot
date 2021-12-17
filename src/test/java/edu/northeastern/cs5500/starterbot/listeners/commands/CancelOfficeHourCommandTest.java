package edu.northeastern.cs5500.starterbot.listeners.commands;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CancelOfficeHourCommandTest {
    private CancelOfficeHourCommand cancelOfficeHourCommand;
    private DiscordIdController discordIdController;

    @BeforeEach
    void initialize() {
        discordIdController = new DiscordIdController(new InMemoryRepository<>());
        cancelOfficeHourCommand = new CancelOfficeHourCommand(discordIdController);
    }

    @Test
    void testGetName() {
        assertThat(cancelOfficeHourCommand.getName()).isNotEmpty();
    }

    @Test
    void testGetCommandData() {
        assertThat(cancelOfficeHourCommand.getCommandData()).isNotNull();
    }

    @Test
    void testGetCommandDataIsConsistent() {
        assertThat(cancelOfficeHourCommand.getCommandData().getName())
                .isEqualTo(cancelOfficeHourCommand.getName());
    }

    @Test
    void testToTitleCase() {
        assertThat(CancelOfficeHourCommand.toTitleCase("testString")).isEqualTo("Teststring");
        assertThat(CancelOfficeHourCommand.toTitleCase("")).isEmpty();
    }

    @Test
    void testStaffUsersGetAnError() {
        NEUUser neuUser = new NEUUser();
        neuUser.setStaff(true);
        assertThat(cancelOfficeHourCommand.validateInputs(DayOfWeek.MONDAY, neuUser))
                .startsWith("ERROR: ");
    }

    @Test
    void testInvalidDayGetsAnError() {
        NEUUser neuUser = new NEUUser();
        neuUser.setStaff(false);
        assertThat(cancelOfficeHourCommand.validateInputs(null, neuUser)).startsWith("ERROR: ");
    }

    @Test
    void testUnregistredUserGetsAnError() {
        assertThat(cancelOfficeHourCommand.validateInputs(DayOfWeek.MONDAY, null))
                .startsWith("ERROR: ");
    }

    @Test
    void testValidInputs() {
        NEUUser neuUser = new NEUUser();
        neuUser.setStaff(false);
        assertThat(cancelOfficeHourCommand.validateInputs(DayOfWeek.MONDAY, neuUser)).isNull();
    }

    @Test
    void testCancelOfficeHourEmbed() {
        MessageEmbed messageEmbed =
                cancelOfficeHourCommand.cancelOfficeHour(DayOfWeek.MONDAY, 0, 1, "STAFF_USER_NAME");
        assertThat(messageEmbed).isNotNull();
        assertThat(messageEmbed.getTitle()).isNotEmpty();
        assertThat(messageEmbed.getFields()).isNotEmpty();
    }
}
