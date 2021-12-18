package edu.northeastern.cs5500.starterbot.listeners.commands;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeleteOfficeHourCommandTest {
    private DeleteOfficeHourCommand deleteOfficeHourCommand;
    private DiscordIdController discordIdController;

    @BeforeEach
    void initialize() {
        discordIdController = new DiscordIdController(new InMemoryRepository<NEUUser>());
        deleteOfficeHourCommand = new DeleteOfficeHourCommand(discordIdController);
    }

    @Test
    void testGetName() {
        assertThat(deleteOfficeHourCommand.getName()).isNotEmpty();
    }

    @Test
    void testGetCommandData() {
        assertThat(deleteOfficeHourCommand.getCommandData()).isNotNull();
    }

    @Test
    void testGetCommandDataIsConsistent() {
        assertThat(deleteOfficeHourCommand.getCommandData().getName())
                .isEqualTo(deleteOfficeHourCommand.getName());
    }

    @Test
    void testToTitleCase() {
        assertThat(DeleteOfficeHourCommand.toTitleCase("testString")).isEqualTo("Teststring");
        assertThat(DeleteOfficeHourCommand.toTitleCase("")).isEmpty();
    }
}
