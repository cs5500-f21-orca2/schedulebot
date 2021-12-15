package edu.northeastern.cs5500.starterbot.listeners.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeleteOfficeHourCommandTest {
    private CreateOfficeHourCommand createOfficeHourCommand;
    private DeleteOfficeHourCommand deleteOfficeHourCommand;
    private DiscordIdController discordIdController;

    private NEUUser student1;
    private NEUUser ta1;

    private GenericRepository<NEUUser> userRepository = new InMemoryRepository<NEUUser>();

    private MessageBuilder mb1;
    private MessageBuilder mb2;
    private MessageBuilder mb3;
    private MessageBuilder mb4;

    @BeforeEach
    void initialize() {
        student1 = new NEUUser("Student1", "nuid0001", "discordId0001");
        ta1 = new NEUUser("TA1", "nuidTA1", "discordIdTA1");

        student1.setVaccinated(true);
        student1.setSymptomatic(false);

        ta1.setVaccinated(true);
        ta1.setSymptomatic(false);
        ta1.setStaff(true);

        userRepository.add(student1);
        userRepository.add(ta1);

        discordIdController = new DiscordIdController(userRepository);
        createOfficeHourCommand = new CreateOfficeHourCommand(discordIdController);
        deleteOfficeHourCommand = new DeleteOfficeHourCommand(discordIdController);

        createOfficeHourCommand.getReply("Monday", 9, 10, "discordIdTA1");
        createOfficeHourCommand.getReply("Tuesday", 10, 11, "discordIdTA1");
        createOfficeHourCommand.getReply("Sunday", 14, 15, "discordIdTA1");

        mb1 = new MessageBuilder();
        mb1.append("Only instructors can delete (their own) office hours.").build();

        mb2 = new MessageBuilder();
        EmbedBuilder eb2 = new EmbedBuilder();
        eb2.setTitle("Delete an office hour");
        eb2.setColor(Color.CYAN);
        eb2.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb2.addField(
                "",
                ":partying_face:"
                        + "Success! You have deleted this office hour on "
                        + "monday"
                        + " from "
                        + 9
                        + " to "
                        + 10,
                true);
        mb2.setEmbed(eb2.build());

        mb3 = new MessageBuilder();
        EmbedBuilder eb3 = new EmbedBuilder();
        eb3.setTitle("Delete an office hour");
        eb3.setColor(Color.CYAN);
        eb3.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb3.addField("", "Reserved office hours cannot be deleted.", true);
        mb3.setEmbed(eb3.build());

        mb4 = new MessageBuilder();
        EmbedBuilder eb4 = new EmbedBuilder();
        eb4.setTitle("Delete an office hour");
        eb4.setColor(Color.CYAN);
        eb4.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb4.addField("", "This office hour has not been created.", true);
        mb4.setEmbed(eb4.build());
    }

    @Test
    void testGetName() {
        assertEquals(deleteOfficeHourCommand.getName(), "deleteofficehour");
    }

    @Test
    void testToTitleCase() {
        assertEquals(deleteOfficeHourCommand.toTitleCase("testString"), "Teststring");
        assertEquals(deleteOfficeHourCommand.toTitleCase(""), "");
    }

    @Test
    void testGetReplyWhenIsNotStaff() {
        assertEquals(
                deleteOfficeHourCommand.getReply("Monday", 9, 10, "discordId0001"), mb1.build());
    }

    @Test
    void testGetReplyWithInvalidDay() {
        MessageBuilder mb5 = new MessageBuilder();
        assertEquals(
                deleteOfficeHourCommand.getReply("thursday", 1, 2, "discordIdTA1"),
                mb5.append("Please enter a valid day").build());
    }

    @Test
    void testGetReplyByDeleteAnUnreservedOfficeHour() {
        assertEquals(
                deleteOfficeHourCommand.getReply("Monday", 9, 10, "discordIdTA1"), mb2.build());
    }

    @Test
    void testGetReplyByDeleteAReservedOfficeHour() {
        discordIdController
                .getNEUUser("discordIdTA1")
                .getInvolvedOfficeHours()
                .get(2)
                .setAttendeeNUID("discordId0001");
        assertEquals(
                deleteOfficeHourCommand.getReply("Tuesday", 10, 11, "discordIdTA1"), mb3.build());
    }

    @Test
    void testGetReplyByDeleteAnNonExistingOfficeHour() {
        assertEquals(
                deleteOfficeHourCommand.getReply("Friday", 17, 18, "discordIdTA1"), mb4.build());
        assertEquals(
                deleteOfficeHourCommand.getReply("Sunday", 17, 18, "discordIdTA1"), mb4.build());
        assertEquals(
                deleteOfficeHourCommand.getReply("Wednesday", 17, 18, "discordIdTA1"), mb4.build());
        assertEquals(
                deleteOfficeHourCommand.getReply("Thursday", 17, 18, "discordIdTA1"), mb4.build());
        assertEquals(
                deleteOfficeHourCommand.getReply("Saturday", 17, 18, "discordIdTA1"), mb4.build());
    }

    @Test
    void testGetCommandData() {
        assertEquals(
                deleteOfficeHourCommand.getCommandData().getDescription(),
                "Delete your office hour if it is not reserved");
        assertEquals(
                deleteOfficeHourCommand.getCommandData().getOptions().get(0).getName(),
                "dayofweek");
        assertEquals(
                deleteOfficeHourCommand.getCommandData().getOptions().get(0).getDescription(),
                "Enter day of the week");
        assertEquals(
                deleteOfficeHourCommand.getCommandData().getOptions().get(1).getName(), "start");
        assertEquals(
                deleteOfficeHourCommand.getCommandData().getOptions().get(1).getDescription(),
                "Enter start time");
        assertEquals(deleteOfficeHourCommand.getCommandData().getOptions().get(2).getName(), "end");
        assertEquals(
                deleteOfficeHourCommand.getCommandData().getOptions().get(2).getDescription(),
                "Enter end time");
    }
}
