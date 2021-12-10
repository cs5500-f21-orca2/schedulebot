package edu.northeastern.cs5500.starterbot.listeners.commands;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SymptomCommandTest {

    private SymptomCommand symptomCommand;
    private DiscordIdController discordIdController;
    private InMemoryRepository<NEUUser> inMemoryRepository;

    private NEUUser student1;
    private NEUUser student2;

    @BeforeEach
    void initialize() {
        inMemoryRepository = new InMemoryRepository<>();
        discordIdController = new DiscordIdController(inMemoryRepository);
        symptomCommand = new SymptomCommand(discordIdController);
    }

    @Test
    void getName() {
        assertEquals(symptomCommand.getName(), "covidsymptom");
    }

    @Test
    void getReply() {}

    @Test
    void getCommandData() {
        assertEquals(symptomCommand.getCommandData().getOptions().get(0).getName(), "covidsymptom");
        assertEquals(
                symptomCommand.getCommandData().getDescription(),
                "Please indicate if you are experiencing covid symptom");
        assertEquals(
                symptomCommand.getCommandData().getOptions().get(0).getDescription(),
                "true if you are experiencing covid symptom; false if you are not");
    }
}
