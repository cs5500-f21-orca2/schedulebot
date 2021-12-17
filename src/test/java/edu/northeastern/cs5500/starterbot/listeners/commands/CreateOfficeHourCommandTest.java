package edu.northeastern.cs5500.starterbot.listeners.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test createOfficeHourCommand */
class CreateOfficeHourCommandTest {
    private CreateOfficeHourCommand createOfficeHourCommand;
    private DiscordIdController discordIdController;

    private NEUUser student1;
    private NEUUser student2;
    private NEUUser student3;
    private NEUUser ta1;
    private NEUUser ta2;
    private NEUUser ta3;
    private NEUUser prof1;
    private NEUUser prof2;
    private NEUUser prof3;

    private GenericRepository<NEUUser> userRepository = new InMemoryRepository<NEUUser>();

    private MessageBuilder mb1;
    private MessageBuilder mb2;
    private MessageBuilder mb3;
    private MessageBuilder mb4;

    /** Test statement setup before each test */
    @BeforeEach
    void initialize() {
        student1 = new NEUUser("Student1", "nuid0001", "discordId0001");
        student2 = new NEUUser("Student2", "nuid0002", "discordId0002");
        student3 = new NEUUser("Student3", "nuid0003", "discordId0003");
        ta1 = new NEUUser("TA1", "nuidTA1", "discordIdTA1");
        ta2 = new NEUUser("TA2", "nuidTA2", "discordIdTA2");
        ta3 = new NEUUser("TA3", "nuidTA3", "discordIdTA3");
        prof1 = new NEUUser("Prof1", "nuidProf1", "discordIdProf1");
        prof2 = new NEUUser("Prof2", "nuidProf2", "discordIdProf2");
        prof3 = new NEUUser("Prof3", "nuidProf3", "discordIdProf3");

        student1.setVaccinated(true);
        student1.setSymptomatic(false);
        student2.setVaccinated(false);
        student2.setSymptomatic(true);
        student3.setVaccinated(true);
        student3.setSymptomatic(true);

        ta1.setVaccinated(true);
        ta1.setSymptomatic(false);
        ta1.setStaff(true);
        ta2.setVaccinated(false);
        ta2.setSymptomatic(true);
        ta2.setStaff(true);
        ta3.setVaccinated(true);
        ta3.setSymptomatic(true);
        ta3.setStaff(true);

        prof1.setVaccinated(true);
        prof1.setSymptomatic(false);
        prof1.setStaff(true);
        prof2.setVaccinated(false);
        prof2.setSymptomatic(true);
        prof2.setStaff(true);
        prof3.setVaccinated(true);
        prof3.setSymptomatic(true);
        prof3.setStaff(true);

        userRepository.add(student1);
        userRepository.add(student2);
        userRepository.add(student3);
        userRepository.add(ta1);
        userRepository.add(ta2);
        userRepository.add(ta3);
        userRepository.add(prof1);
        userRepository.add(prof2);
        userRepository.add(prof3);

        discordIdController = new DiscordIdController(userRepository);
        createOfficeHourCommand = new CreateOfficeHourCommand(discordIdController);

        mb1 = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Create an office hour");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb.addField(
                "OfficeHour",
                ":partying_face:"
                        + "Success! You created an office hour on "
                        + "monday"
                        + " from "
                        + 1
                        + " to "
                        + 2,
                true);
        mb1.setEmbed(eb.build());

        mb2 = new MessageBuilder();
        EmbedBuilder eb2 = new EmbedBuilder();
        eb2.setTitle("Create an office hour");
        eb2.setColor(Color.CYAN);
        eb2.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb2.addField(
                "OfficeHour",
                ":partying_face:"
                        + "Success! You created an office hour on "
                        + "tuesday"
                        + " from "
                        + 1
                        + " to "
                        + 2,
                true);
        mb2.setEmbed(eb2.build());

        mb3 = new MessageBuilder();
        EmbedBuilder eb3 = new EmbedBuilder();
        eb3.setTitle("Create Multiple office hours");
        eb3.setColor(Color.CYAN);
        eb3.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb3.addField(
                "OfficeHour",
                ":partying_face:"
                        + "You created an office hour on "
                        + "sunday"
                        + " from "
                        + 1
                        + " to "
                        + 2,
                true);
        eb3.addField(
                "OfficeHour",
                ":partying_face:"
                        + "You created an office hour on "
                        + "sunday"
                        + " from "
                        + 2
                        + " to "
                        + 3,
                true);
        mb3.setEmbed(eb3.build());

        mb4 = new MessageBuilder();
        EmbedBuilder eb4 = new EmbedBuilder();
        eb4.setTitle("Create Multiple office hours");
        eb4.setColor(Color.CYAN);
        eb4.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb4.addField(
                "OfficeHour",
                ":partying_face:"
                        + "You created an office hour on "
                        + "saturday"
                        + " from "
                        + 1
                        + " to "
                        + 2,
                true);
        eb4.addField(
                "OfficeHour",
                ":partying_face:"
                        + "You created an office hour on "
                        + "saturday"
                        + " from "
                        + 2
                        + " to "
                        + 3,
                true);
        mb4.setEmbed(eb4.build());
    }

    /** Test getName */
    @Test
    void testGetName() {
        assertEquals(createOfficeHourCommand.getName(), "createofficehour");
    }

    /** Test toTitleCase */
    @Test
    void testToTitleCase() {
        assertEquals(createOfficeHourCommand.toTitleCase("testString"), "Teststring");
    }

    /** Test getReplyWhenIsNotStaff */
    @Test
    void testGetReplyWhenIsNotStaff() {
        MessageBuilder mb1 = new MessageBuilder();
        assertEquals(
                createOfficeHourCommand.getReply("Thursday", 1, 2, "discordId0001"),
                mb1.append("Only instructor can create office hour.").build());
        MessageBuilder mb2 = new MessageBuilder();
        assertEquals(
                createOfficeHourCommand.getReply("Wednesday", 1, 2, "discordId0002"),
                mb2.append("Only instructor can create office hour.").build());
    }

    /** Test getReplyWithInvalidDay */
    @Test
    void testGetReplyWithInvalidDay() {
        MessageBuilder mb1 = new MessageBuilder();
        assertEquals(
                createOfficeHourCommand.getReply("thursday", 1, 2, "discordIdTA1"),
                mb1.append("Please enter a valid day").build());
        MessageBuilder mb2 = new MessageBuilder();
        assertEquals(
                createOfficeHourCommand.getReply("Wednesda", 1, 2, "discordIdTA1"),
                mb2.append("Please enter a valid day").build());
    }

    /** Test getReplyByCreateSingleOfficeHour */
    @Test
    void testGetReplyByCreateSingleOfficeHour() {
        assertEquals(createOfficeHourCommand.getReply("Monday", 1, 2, "discordIdTA1"), mb1.build());
        assertEquals(
                createOfficeHourCommand.getReply("Tuesday", 2, 1, "discordIdProf1"), mb2.build());
    }

    /** Test listAfterCreateSingleOfficeHour */
    @Test
    void testListAfterCreateSingleOfficeHour() {
        createOfficeHourCommand.getReply("Wednesday", 1, 2, "discordIdTA1");
        createOfficeHourCommand.getReply("Thursday", 2, 1, "discordIdProf1");
        OfficeHour officeHour =
                new OfficeHour(DayOfWeek.WEDNESDAY, new OfficeHourType("Online"), 1, 2, "nuidTA1");
        assertEquals(
                discordIdController.getNEUUser("discordIdTA1").getInvolvedOfficeHours().get(0),
                officeHour);

        OfficeHour officeHour2 =
                new OfficeHour(DayOfWeek.THURSDAY, new OfficeHourType("Online"), 1, 2, "nuidProf1");
        assertEquals(
                discordIdController.getNEUUser("discordIdProf1").getInvolvedOfficeHours().get(0),
                officeHour2);
    }

    /** Test getReplyByCreateMutipleOfficeHour */
    @Test
    void testGetReplyByCreateMultipleOfficeHour() {
        assertEquals(createOfficeHourCommand.getReply("Sunday", 1, 3, "discordIdTA2"), mb3.build());
        assertEquals(
                createOfficeHourCommand.getReply("Saturday", 3, 1, "discordIdProf2"), mb4.build());
    }

    /** Test Office Hour store in the NEUUser list */
    @Test
    void testOfficeHour() {
        createOfficeHourCommand.getReply("Sunday", 1, 3, "discordIdTA2");
        createOfficeHourCommand.getReply("Saturday", 3, 1, "discordIdProf2");
        OfficeHour officeHour =
                new OfficeHour(DayOfWeek.SUNDAY, new OfficeHourType("Online"), 1, 2, "nuidTA2");
        OfficeHour officeHour2 =
                new OfficeHour(DayOfWeek.SUNDAY, new OfficeHourType("Online"), 2, 3, "nuidTA2");
        OfficeHour officeHour3 =
                new OfficeHour(DayOfWeek.SATURDAY, new OfficeHourType("Online"), 1, 2, "nuidProf2");
        OfficeHour officeHour4 =
                new OfficeHour(DayOfWeek.SATURDAY, new OfficeHourType("Online"), 2, 3, "nuidProf2");
        assertEquals(
                discordIdController.getNEUUser("discordIdTA2").getInvolvedOfficeHours().get(0),
                officeHour);
        assertEquals(
                discordIdController.getNEUUser("discordIdTA2").getInvolvedOfficeHours().get(1),
                officeHour2);
        assertEquals(
                discordIdController.getNEUUser("discordIdProf2").getInvolvedOfficeHours().get(0),
                officeHour3);
        assertEquals(
                discordIdController.getNEUUser("discordIdProf2").getInvolvedOfficeHours().get(1),
                officeHour4);
    }

    /** Test getCommandDate */
    @Test
    void testGetCommandData() {
        assertEquals(
                createOfficeHourCommand.getCommandData().getDescription(),
                "Create a new office hour session");
        assertEquals(
                createOfficeHourCommand.getCommandData().getOptions().get(0).getName(),
                "dayofweek");
        assertEquals(
                createOfficeHourCommand.getCommandData().getOptions().get(0).getDescription(),
                "Enter day of the week");
        assertEquals(
                createOfficeHourCommand.getCommandData().getOptions().get(1).getName(), "start");
        assertEquals(
                createOfficeHourCommand.getCommandData().getOptions().get(1).getDescription(),
                "Enter start time");
        assertEquals(createOfficeHourCommand.getCommandData().getOptions().get(2).getName(), "end");
        assertEquals(
                createOfficeHourCommand.getCommandData().getOptions().get(2).getDescription(),
                "Enter end time");
    }
}
