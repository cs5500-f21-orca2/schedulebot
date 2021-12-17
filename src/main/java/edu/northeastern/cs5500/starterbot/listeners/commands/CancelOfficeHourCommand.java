package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import java.awt.Color;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/** This class represents the /cancelofficehour function for students' use. */
public class CancelOfficeHourCommand implements Command {
    public static String ERROR_DID_NOT_REMOVE =
            "Cannot cancel this office hour.\nPlease use /schedule to check your reserved office hours.";

    private DiscordIdController discordIdController;

    /**
     * Constructs a CancelOfficeHourCommand.
     *
     * @param discordIdController
     */
    public CancelOfficeHourCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    /** Returns the command name as a string. */
    @Override
    public String getName() {
        return "cancelofficehour";
    }

    /**
     * Returns a String with its first character in upper case, followed by rest of characters in
     * lower case.
     *
     * @param str a String
     * @return a formatted String
     */
    static String toTitleCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 1).toUpperCase());
        sb.append(str.substring(1, str.length()).toLowerCase());
        return sb.toString();
    }

    /**
     * Performs a slash command method.
     *
     * @param event an event from Discord's slash command
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        final String dayOfWeekString = event.getOption("dayofweek").getAsString();
        DayOfWeek dayOfWeek = DayOfWeek.fromString(toTitleCase(dayOfWeekString));
        int startHour = (int) event.getOption("start").getAsLong();
        int endHour = (int) event.getOption("end").getAsLong();
        final String staffName = toTitleCase(event.getOption("staffname").getAsString());
        String discordId = event.getUser().getId();

        NEUUser user = discordIdController.getNEUUser(discordId);
        final String errors = validateInputs(dayOfWeek, user);
        if (errors != null) {
            event.reply(errors).queue();
        } else if (!discordIdController.cancelOfficeHour(
                user, dayOfWeek, startHour, endHour, staffName)) {
            event.reply(ERROR_DID_NOT_REMOVE).queue();
        } else {
            MessageEmbed cancelMessageEmbed =
                    cancelOfficeHour(dayOfWeek, startHour, endHour, staffName);
            event.replyEmbeds(cancelMessageEmbed).queue();
        }
    }

    /**
     * Builds a Message for onSlashCommand method to reply depending on the arguments, then decides
     * what MessageEmbed method to call
     *
     * @param dayOfWeek day of week
     * @param user The NEUUser making the request
     * @return null if there are no errors; an error message if there are some errors.
     */
    public String validateInputs(@Nullable DayOfWeek dayOfWeek, @Nullable NEUUser user) {

        if (user == null) {
            return "ERROR: You have to /register before using other commands.";
        }

        if (user.isStaff()) {
            return "ERROR: Only students can cancel office hours they reserved.";
        }

        if (dayOfWeek == null) {
            return "ERROR: Please enter a valid day.";
        }

        return null;
    }

    /**
     * Builds an Embed for Message to cancel a single office hour from user's list and sets up an
     * advanced display format
     *
     * @param dayOfWeek enum of DayOfWeek
     * @param startHour int of office hour start time
     * @param endHour int of office hour end time
     * @param staffName String of staff that hosts this office hour
     * @param discordId String of user discordId
     * @return A MessageEmbed for Message method to build
     */
    MessageEmbed cancelOfficeHour(
            @Nonnull DayOfWeek dayOfWeek, int startHour, int endHour, @Nonnull String staffName) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Cancel an office hour");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb.addField(
                "",
                ":partying_face:"
                        + "Success! You have canceled this office hour on "
                        + dayOfWeek.toString()
                        + " from "
                        + startHour
                        + " to "
                        + endHour
                        + " with "
                        + staffName
                        + "\n"
                        + "It is now available for reservation to all students",
                true);

        return eb.build();
    }

    /**
     * Constructs a command from guided user input.
     *
     * @return CommandData
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Cancel the office hour you reserved")
                .addOptions(
                        new OptionData(OptionType.STRING, "dayofweek", "Enter day of the week")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "start", "Enter start time")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "end", "Enter end time")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "staffname", "Enter staff name")
                                .setRequired(true));
    }
}
