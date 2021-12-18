package edu.northeastern.cs5500.starterbot.listeners.commands;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class GetAvailableCommand extends GetScheduleCommand {

    /**
     * Returns the command name as a string.
     *
     * @return The command name "getavailable"
     */
    @Override
    public String getName() {
        return "getavailable";
    }

    private DiscordIdController discordIdController;

    public GetAvailableCommand(DiscordIdController discordIdController) {
        super(discordIdController);
        this.discordIdController = discordIdController;
    }

    @Override
    Message getReply(@Nullable NEUUser user, @Nullable String dayOfWeek) {
        MessageBuilder mb = new MessageBuilder();
        if (user == null) {
            return mb.append("You are not registered; please try /register first.").build();
        }

        if (user.isStaff() == true) {
            return mb.append("This command is only useful for student.").build();
        }

        Collection<NEUUser> taProfList = discordIdController.getAllTAProf();

        if (taProfList == null || taProfList.isEmpty()) {
            return mb.append("Can't find instructor in this class.").build();
        }

        List<OfficeHour> allOfficeHours = new ArrayList<>();
        for (NEUUser neuUser : taProfList) {
            List<OfficeHour> neuUserOfficeHours = neuUser.getInvolvedOfficeHours();
            for (OfficeHour officeHour : neuUserOfficeHours) {
                allOfficeHours.add(officeHour);
            }
        }
        if (dayOfWeek == null) {
            // reply with the entire week
            return mb.setEmbed(getEntireWeekReply(allOfficeHours)).build();
        } else {
            // reply with just the requested day
            if (!isValidDayOfWeek(dayOfWeek)) {
                return mb.append(
                                "Please enter a valid day of the week (case-insensitive); e.g. 'Monday'")
                        .build();
            }
            return mb.setEmbed(getSingleDayReply(allOfficeHours, dayOfWeek)).build();
        }
    }

    /**
     * A function will take a list of OfficeHour and a day of week in string. Will return all
     * unreserved office hours in passed in list on passed in day of week.
     *
     * @param userOfficeHourList contains all current user's office hours.
     * @param dayOfWeek The target day of week user want to check.
     * @return a MessageEmbed contians all valid office hours.
     */
    /**
     * A function will take a list of OfficeHour and return all inperson office hours in passed in
     * list in MessageEmbed.
     *
     * @param userOfficeHourList an office hour list.
     * @return A MessageEmbed for getReply method to build
     */
    @Override
    MessageEmbed getEntireWeekReply(List<OfficeHour> userOfficeHourList) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Available office hours for the week:");
        if (userOfficeHourList == null || userOfficeHourList.isEmpty()) {
            eb.setDescription("(no office hours for the week)");
        } else {
            Collections.sort(userOfficeHourList);
            for (OfficeHour hour : userOfficeHourList) {
                if (hour.getAttendeeNUID() == null) {
                    eb.addField(
                            hour.getDayOfWeek().toString(),
                            String.format(
                                    "%d:00 to %d:00; %s",
                                    hour.getStartHour(),
                                    hour.getEndHour(),
                                    hour.getOfficeHourType().getTypeName()),
                            false);
                }
            }
        }
        return eb.build();
    }

    /**
     * A function will take a list of OfficeHour and a day of week in string. Will return all office
     * hours in passed in list on passed in day of week.
     *
     * @param userOfficeHourList a list contains all current user's office hour.
     * @param dayOfWeek the target day of week
     * @return A MessageEmbed for getReply method to build
     */
    @Override
    MessageEmbed getSingleDayReply(List<OfficeHour> userOfficeHourList, String dayOfWeek) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(String.format("Available office hours for %s:", dayOfWeek));
        OfficeHour officeHour = null;
        for (OfficeHour oh : userOfficeHourList) {
            if (dayOfWeek.equalsIgnoreCase(oh.getDayOfWeek().toString())) {
                officeHour = oh;
                break;
            }
        }
        if (officeHour == null) {
            eb.setDescription("(no appointments for this day)");
        } else {
            Collections.sort(userOfficeHourList);
            for (OfficeHour hour : userOfficeHourList) {
                if (hour.getAttendeeNUID() == null) {
                    if (hour.getDayOfWeek()
                            .toString()
                            .toLowerCase()
                            .equals(dayOfWeek.toLowerCase()))
                        eb.addField(
                                hour.getDayOfWeek().toString(),
                                String.format(
                                        "%d:00 to %d:00; %s %s",
                                        hour.getStartHour(),
                                        hour.getEndHour(),
                                        hour.getOfficeHourType().getTypeName(),
                                        hour.getAttendeeNUID() == null
                                                ? ""
                                                : "\nStudent:  "
                                                        + discordIdController
                                                                .getNEUUserByNuid(
                                                                        hour.getAttendeeNUID())
                                                                .getUserName()),
                                false);
                }
            }
        }
        return eb.build();
    }

    /** For Java Discord API in App.java to add commands */
    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        getName(), "Get available hours for the week or a given day (For Student).")
                .addOptions(
                        new OptionData(
                                OptionType.STRING,
                                "dayofweek",
                                "Monday/Tuesday/Wednesday/Thursday/Friday/Saturday/Sunday; if empty, the entire week is displayed"));
    }
}
