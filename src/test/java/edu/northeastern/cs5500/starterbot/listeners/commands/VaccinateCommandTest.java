package edu.northeastern.cs5500.starterbot.listeners.commands;

import static org.junit.jupiter.api.Assertions.*;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccinateCommandTest {

    private VaccinateCommand vaccinateCommand;
    private DiscordIdController discordIdController;
    private GenericRepository<NEUUser> userRepository = new InMemoryRepository<NEUUser>();

    private NEUUser student1;
    private NEUUser student2;

    @BeforeEach
    void initialize() {

        discordIdController = new DiscordIdController(userRepository);
        vaccinateCommand = new VaccinateCommand(discordIdController);

        student1 = new NEUUser("Student1", "nuid1", "discordId1");

        userRepository.add(student1);
    }

    @Test
    void testCreateNonRegisteredUserReport() {
        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("--------Vaccination Record--------");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb.setDescription(
            "Unable to determine your vaccination status." + "\n" + "Have you registered?");
        mb.setEmbed(eb.build());
        assertEquals(
            vaccinateCommand.getReply(null, null, true),
            mb.build());
    }


    @Test
    void testNoInputData() {
        MessageBuilder mb1 = new MessageBuilder();
        EmbedBuilder eb1 = new EmbedBuilder();
        eb1.setTitle("--------Vaccination Record--------");
        eb1.setColor(Color.CYAN);
        eb1.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb1.setDescription("Your status is: NOT vaccinated");
        mb1.setEmbed(eb1.build());
        assertEquals(
                vaccinateCommand.getReply("discordId1", student1, student1.isVaccinated()),
                mb1.build());
    }

    @Test
    void testCreateVaccineReport() {
        MessageBuilder mb2 = new MessageBuilder();
        EmbedBuilder eb2 = new EmbedBuilder();
        eb2.setTitle("--------Vaccination Record--------");
        eb2.setColor(Color.CYAN);
        eb2.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb2.setDescription("Your status is: vaccinated");
        mb2.setEmbed(eb2.build());
        Boolean optionData = true;
        assertEquals(vaccinateCommand.getReply("discordId1", student1, optionData), mb2.build());
    }

    @Test
    void testCreateNonVaccineReport() {
        MessageBuilder mb3 = new MessageBuilder();
        EmbedBuilder eb3 = new EmbedBuilder();
        eb3.setTitle("--------Vaccination Record--------");
        eb3.setColor(Color.CYAN);
        eb3.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb3.setDescription("Your status is: NOT vaccinated");
        mb3.setEmbed(eb3.build());
        Boolean optionData = false;
        assertEquals(vaccinateCommand.getReply("discordId1", student1, optionData), mb3.build());
    }

    @Test
    void testIsVaccinated() {
        student1.setVaccinated(true);
        student2 = new NEUUser("Student2", "nuid2", "discordId2");
        student2.setVaccinated(false);
        assertTrue(student1.isVaccinated());
        assertFalse(student2.isVaccinated());
    }

    @Test
    void testGetName() {
        assertEquals(vaccinateCommand.getName(), "vaccinated");
    }

    @Test
    void testGetCommandDataArgs1() {
        assertEquals(vaccinateCommand.getCommandData().getOptions().get(0).getName(), "vaccinated");
    }

    @Test
    void testGetCommandDataArgs2() {
        assertEquals(
                vaccinateCommand.getCommandData().getDescription(),
                "Get or set your own vaccination status.");
    }

    @Test
    void testGetCommandDataArgs3() {
        assertEquals(
                vaccinateCommand.getCommandData().getOptions().get(0).getDescription(),
                "true if you are vaccinated or have a waiver; false if you are not");
    }

    @Test
    void testEmptyInputData() {
        assertFalse(vaccinateCommand.getCommandData().getOptions().isEmpty());
    }
}
