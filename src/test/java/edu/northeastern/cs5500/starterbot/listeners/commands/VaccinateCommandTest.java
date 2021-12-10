package edu.northeastern.cs5500.starterbot.listeners.commands;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccinateCommandTest {

    private VaccinateCommand vaccinateCommand;
    private DiscordIdController discordIdController;
    private InMemoryRepository<NEUUser> inMemoryRepository;

    private NEUUser student1;
    private NEUUser student2;

    @BeforeEach
    void initialize() {
        inMemoryRepository = new InMemoryRepository<>();
        discordIdController = new DiscordIdController(inMemoryRepository);
        vaccinateCommand = new VaccinateCommand(discordIdController);

        student1 = new NEUUser("Student1", "nuid1", "discordId1");
        student2 = new NEUUser("Student2", "nuid2", "discordId2");

        student1.setVaccinated(true);
        student2.setVaccinated(false);

        inMemoryRepository.add(student1);
        inMemoryRepository.add(student2);
    }

    @Test
    void testIsVaccinated() {
        assertTrue(student1.isVaccinated());
        assertFalse(student2.isVaccinated());
    }

    @Test
    void testGetName() {
        assertEquals(vaccinateCommand.getName(), "vaccinated");
    }

    @Test
    void testGetCommandData() {
        assertEquals(vaccinateCommand.getCommandData().getOptions().get(0).getName(), "vaccinated");
        assertEquals(
                vaccinateCommand.getCommandData().getDescription(),
                "Get or set your own vaccination status.");
        assertEquals(
                vaccinateCommand.getCommandData().getOptions().get(0).getDescription(),
                "true if you are vaccinated or have a waiver; false if you are not");
    }

    @Test
    void testEmptyInputData() {
        assertFalse(vaccinateCommand.getCommandData().getOptions().isEmpty());
    }
}
