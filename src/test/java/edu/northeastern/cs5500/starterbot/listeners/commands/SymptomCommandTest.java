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

class SymptomCommandTest {

    private SymptomCommand symptomCommand;
    private DiscordIdController discordIdController;
    private GenericRepository<NEUUser> userRepository = new InMemoryRepository<NEUUser>();

    private NEUUser student1;

    @BeforeEach
    void initialize() {

        discordIdController = new DiscordIdController(userRepository);
        symptomCommand = new SymptomCommand(discordIdController);

        student1 = new NEUUser("Student1", "nuid1", "discordId1");

        userRepository.add(student1);
    }

    @Test
    void testCreateNonRegisteredUserReportSymptomatic() {
        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("-----------Covid Symptom Self-report----------");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb.setDescription(
            "Unable to determine whether you have covid symptom or not."
                + "\n"
                + "Have you registered?");
        mb.setEmbed(eb.build());
        assertEquals(
            symptomCommand.getReply(null, null, true),
            mb.build());
    }

    @Test
    void testNoInputData() {
        MessageBuilder mb1 = new MessageBuilder();
        EmbedBuilder eb1 = new EmbedBuilder();
        eb1.setTitle("---------------Covid Symptom Self-report---------------");
        eb1.setColor(Color.CYAN);
        eb1.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb1.setDescription(
            "You are NOT experiencing covid symptom."
                + "\n"
                + "So you can make inperson office hour appointment");
        mb1.setEmbed(eb1.build());
        assertEquals(
            symptomCommand.getReply("discordId1", student1, student1.isSymptomatic()),
            mb1.build());
    }

    @Test
    void testCreateSymptomaticReport() {
        MessageBuilder mb1 = new MessageBuilder();
        EmbedBuilder eb1 = new EmbedBuilder();
        eb1.setTitle("---------------Covid Symptom Self-report---------------");
        eb1.setColor(Color.CYAN);
        eb1.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb1.setDescription(
            "You are experiencing covid symptom, So you will not"
                + "\n"
                + "be able to make an inperson office hour appointment"
                + "\n"
                + "in order to avoid potential risk"
                + "\n"
                + "Thanks for your understanding!");
        mb1.setEmbed(eb1.build());
        Boolean optionData = true;
        assertEquals(symptomCommand.getReply("discordId1", student1, optionData), mb1.build());
    }

    @Test
    void createNonSymptomaticReport() {
        MessageBuilder mb2 = new MessageBuilder();
        EmbedBuilder eb2 = new EmbedBuilder();
        eb2.setTitle("---------------Covid Symptom Self-report---------------");
        eb2.setColor(Color.CYAN);
        eb2.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb2.setDescription(
            "You are NOT experiencing covid symptom."
                + "\n"
                + "So you can make inperson office hour appointment");
        mb2.setEmbed(eb2.build());
        Boolean optionData = false;
        assertEquals(symptomCommand.getReply("discordId1", student1, optionData), mb2.build());
    }

    @Test
    void getName() {
        assertEquals(symptomCommand.getName(), "covidsymptom");
    }

    @Test
    void getReply() {}

    @Test
    void getCommandDataArgs1() {
        assertEquals(symptomCommand.getCommandData().getOptions().get(0).getName(), "covidsymptom");
    }

    @Test
    void getCommandDataArgs2() {
        assertEquals(
            symptomCommand.getCommandData().getDescription(),
            "Please indicate if you are experiencing covid symptom");
    }

    @Test
    void getCommandDataArgs3() {
        assertEquals(
            symptomCommand.getCommandData().getOptions().get(0).getDescription(),
            "true if you are experiencing covid symptom; false if you are not");
    }
}
