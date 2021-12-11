package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.DayOfWeek;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/** This class represents the /deleteofficehour function for instructors' use. */
public class DeleteOfficeHourCommand implements Command {

    private DiscordIdController discordIdController;

    /**
     * Constructs a DeleteOfficeHourCommand.
     *
     * @param discordIdController
     */
    public DeleteOfficeHourCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    /** Returns the command name as a string. */
    @Override
    public String getName() {
        return "deleteofficehour";
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
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        if (!user.isStaff()) {
            event.reply("Only instructors can delete (their own) office hours.").queue();
            return;
        }

        final OptionMapping dayOfWeekOption = event.getOption("dayofweek");
        final DayOfWeek dayOfWeek;
        String dayOfWeekString;
        if (dayOfWeekOption == null) {
            dayOfWeekString = null;
        } else {
            dayOfWeekString = toTitleCase(dayOfWeekOption.getAsString());
        }
        switch (dayOfWeekString) {
            case "Monday":
                dayOfWeek = DayOfWeek.MONDAY;
                break;
            case "Tuesday":
                dayOfWeek = DayOfWeek.TUESDAY;
                break;
            case "Wednesday":
                dayOfWeek = DayOfWeek.WEDNESDAY;
                break;
            case "Thursday":
                dayOfWeek = DayOfWeek.THURSDAY;
                break;
            case "Friday":
                dayOfWeek = DayOfWeek.FRIDAY;
                break;
            case "Saturday":
                dayOfWeek = DayOfWeek.SATURDAY;
                break;
            case "Sunday":
                dayOfWeek = DayOfWeek.SUNDAY;
                break;
            default:
                event.reply("Please enter a valid day").queue();
                return;
        }

        int startHour = Integer.parseInt(event.getOption("start").getAsString());
        int endHour = Integer.parseInt(event.getOption("end").getAsString());

        List<OfficeHour> involvedOfficeHours = user.getInvolvedOfficeHours();
        for (int i = 0; i < involvedOfficeHours.size(); i++) {
            if (involvedOfficeHours.get(i).getDayOfWeek().equals(dayOfWeek)
                    && involvedOfficeHours.get(i).getStartHour() == startHour
                    && involvedOfficeHours.get(i).getEndHour() == endHour
                    && involvedOfficeHours.get(i).getAttendeeNUID() == null) {
                involvedOfficeHours.remove(i);
                discordIdController.setInvolvedOfficeHours(discordId, involvedOfficeHours);
                event.reply("You have successfully deleted this office hour!").queue();
                return;
            } else if (involvedOfficeHours.get(i).getDayOfWeek().equals(dayOfWeek)
                    && involvedOfficeHours.get(i).getStartHour() == startHour
                    && involvedOfficeHours.get(i).getEndHour() == endHour
                    && involvedOfficeHours.get(i).getAttendeeNUID() != null) {
                event.reply("Reserved office hours cannot be deleted.").queue();
                return;
            }
        }
        event.reply("This office hour has not been created.").queue();
        return;
    }

    /**
     * Constructs a command from guided user input.
     *
     * @return CommandData
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Delete your office hour if it is not reserved")
                .addOptions(
                        new OptionData(OptionType.STRING, "dayofweek", "Enter day of the week")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "start", "Enter start time")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "end", "Enter end time")
                                .setRequired(true));
    }
}
