package edu.northeastern.cs5500.starterbot.listeners.commands;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.OfficeHourType;
import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/** This class is for command /reserve, reserve office hour for students */
public class ReserveCommand implements Command {

    private DiscordIdController discordIdController;

    public ReserveCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    /**
     * Get name of the command
     *
     * @return String, command name
     */
    @Override
    public String getName() {
        return "reserve";
    }

    /**
     * Convert string to title case
     *
     * @param str string needs to be converted
     * @return title case string
     */
    static String toTitleCase(@Nonnull String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, 1).toUpperCase());
        sb.append(str.substring(1, str.length()).toLowerCase());
        return sb.toString();
    }

    /**
     * Check if the input day of week is valid
     *
     * @param dayOfWeek String, inputed dayofweek
     * @return Boolean, true if vaid
     */
    Boolean isValidDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "Monday":
            case "Tuesday":
            case "Wednesday":
            case "Thursday":
            case "Friday":
            case "Saturday":
            case "Sunday":
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if the input meeting type is valid
     *
     * @param type String, nputed type
     * @return
     */
    Boolean isValidType(String type) {
        switch (type) {
            case "Inperson":
            case "Online":
                return true;
            default:
                return false;
        }
    }

    /**
     * Check edge cases: non-registers, non-students, request inperson appointment: non-vaccine,
     * having symptoms Make appointment if passes all edge cases, then reply messages to Discord
     *
     * @param user NEUUser, student user
     * @param dayOfWeek String, inputted day of week
     * @param type String, inputted appointment type
     * @param startTime Integer, inputted start hour
     * @param endTime Integer, inputted end hour
     * @param staffName String, inputted staff name
     * @return Message, messages which indicate if the reservation successfully made or need further
     *     information
     */
    Message getReply(
            @Nullable NEUUser user,
            @Nonnull String dayOfWeek,
            @Nonnull String type,
            @Nonnull Integer startTime,
            @Nonnull Integer endTime,
            @Nonnull String staffName) {
        MessageBuilder mb = new MessageBuilder();
        if (user == null) {
            return mb.append("You are not registered; please try /register first.").build();
        }
        if (user.isStaff() == true) {
            return mb.append("You are a Staff; this command is only useful for Students.").build();
        }
        if (type.equals("Inperson")) {
            if (user.isSymptomatic()) {
                return mb.append(
                                "In person appoinment not supported for symptomatic attendees. Please visit https://news.northeastern.edu/coronavirus/ for more information.")
                        .build();
            }
            if (!user.isVaccinated()) {
                return mb.append(
                                "InPerson request require you to be vaccinated or have a waiver."
                                        + "If you have been vaccinated, please update your vaccined status by using /vaccinated command.")
                        .build();
            }
        }
        return mb.setEmbed(getReserveReply(type, dayOfWeek, startTime, endTime, staffName, user))
                .build();
    }

    /**
     * Helper function for getReserveReply, check if inputted staff is valid
     *
     * @param taProfList Collection<NEUUser>, list of registered staff
     * @param staffName String, inputted staff name
     * @return NEUUser, matched staff
     */
    NEUUser checkNoStaff(Collection<NEUUser> taProfList, String staffName) {
        for (NEUUser neuUser : taProfList) {
            if (toTitleCase(neuUser.getUserName()).equals(staffName)) {
                return neuUser;
            }
        }
        return null;
    }

    /**
     * Helper function for getReserveReply, check if the student has already made an appointment at
     * same time slot
     *
     * @param dayOfWeek String, inputted day of week
     * @param startTime Integer, inputted start hour
     * @param endTime Integer, inputted end hour
     * @param user NEUUser, student user who made the request
     * @return Boolean, true if student has duplicate office hour
     */
    Boolean checkDuplicateOfficeHour(
            String dayOfWeek, Integer startTime, Integer endTime, NEUUser user) {
        List<OfficeHour> userOfficeHour = user.getInvolvedOfficeHours();
        for (OfficeHour officeHour : userOfficeHour) {
            if (officeHour.getDayOfWeek().toString().equals(dayOfWeek)
                    && officeHour.getStartHour() == startTime
                    && officeHour.getEndHour() == endTime) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function for getReserveReply, check if there is a matching office hour as requested,
     * if there is matching one, make reservation
     *
     * @param taProf NEUUser, matched staff as requested
     * @param dayOfWeek String, inputted day of week
     * @param startTime Integer, inputted start hour
     * @param endTime Integer, inputted end hour
     * @param type String, inputted appointment type
     * @param user NEUUser, student user who made the request
     * @return Boolean, true if there is a matching office hour and make reservation
     */
    Boolean checkMatchingOH(
            NEUUser taProf,
            String dayOfWeek,
            Integer startTime,
            Integer endTime,
            String type,
            NEUUser user) {
        for (OfficeHour officeHour : taProf.getInvolvedOfficeHours()) {
            if (officeHour.getDayOfWeek().toString().equals(dayOfWeek)
                    && officeHour.getStartHour() == startTime
                    && officeHour.getEndHour() == endTime) {
                officeHour.setOfficeHourType(new OfficeHourType(type));
                officeHour.setAttendeeNUID(user.getNuid());
                user.getInvolvedOfficeHours().add(officeHour);
                discordIdController.setInvolvedOfficeHours(
                        taProf.getDiscordId(), taProf.getInvolvedOfficeHours());
                taProf.setInvolvedOfficeHours(taProf.getInvolvedOfficeHours());
                Collections.sort(user.getInvolvedOfficeHours());
                discordIdController.setInvolvedOfficeHours(
                        user.getDiscordId(), user.getInvolvedOfficeHours());
                user.setInvolvedOfficeHours(user.getInvolvedOfficeHours());
                return true;
            }
        }
        return false;
    }

    /**
     * Make valid appointment, and reply message accordingly
     *
     * @param type String, inputted appointment type
     * @param dayOfWeek String, inputted day of week
     * @param startTime Integer, inputted start time
     * @param endTime Integer, inputted end time
     * @param staffName String, inputted staff name
     * @param user String, student user who made the request
     * @return MessageEmbed, reply messages accordingly
     */
    MessageEmbed getReserveReply(
            String type,
            String dayOfWeek,
            Integer startTime,
            Integer endTime,
            String staffName,
            NEUUser user) {
        NEUUser taProf = null;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.GREEN);
        Collection<NEUUser> taProfList = discordIdController.getAllTAProf();
        if (taProfList.isEmpty()) {
            return eb.setDescription(
                            "No office hours available right now, please check with the instructors")
                    .build();
        }
        taProf = checkNoStaff(taProfList, staffName);
        if (taProf == null) {
            return eb.setDescription(
                            "Unable to find the TA/Professor name as requested, try another staff by checking /getavailable")
                    .build();
        }
        if (checkDuplicateOfficeHour(dayOfWeek, startTime, endTime, user)) {
            return eb.setDescription(
                            "You already have a reservation on "
                                    + String.format(
                                            "%s, from %s:00 to %s:00, make sure to be here on time",
                                            dayOfWeek, startTime, endTime))
                    .build();
        }

        if (checkMatchingOH(taProf, dayOfWeek, startTime, endTime, type, user)) {
            return eb.setDescription(
                            "You have successfully scheduled a office hour appointment with: "
                                    + staffName
                                    + "\n"
                                    + String.format(
                                            "%s: %s:00 to %s:00", dayOfWeek, startTime, endTime))
                    .build();
        } else {
            return eb.setDescription(
                            "There is no matching office hour, try another time by checking the /getavailable")
                    .build();
        }
    }

    /**
     * Take inputs from command, check the validity of the inputs, and made reply accordingly
     *
     * @param event SlashCommandEvent
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        NEUUser user = discordIdController.getNEUUser(event.getUser().getId());
        String dayOfWeek = event.getOption("dayofweek").getAsString();
        String type = event.getOption("type").getAsString();
        Integer startTime = Integer.parseInt(event.getOption("starttime").getAsString());
        Integer endTime = Integer.parseInt(event.getOption("endtime").getAsString());
        String staffName = event.getOption("staffname").getAsString();
        dayOfWeek = toTitleCase(dayOfWeek);
        type = toTitleCase(type);
        staffName = toTitleCase(staffName);
        if (!isValidDayOfWeek(dayOfWeek)) {
            event.reply(
                            "Please try again, enter a valid day of week: Monday/Tuesday/Wednesday/Thusday/Friday/Saturday/Sunday")
                    .queue();
        }
        if (!isValidType(type)) {
            event.reply("Please try again, enter a valid meeting type: Online/Inperson").queue();
        }
        Message reply = getReply(user, dayOfWeek, type, startTime, endTime, staffName);
        event.reply(reply).queue();
    }

    /**
     * Get command data
     *
     * @return
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData("reserve", "Make a reservation")
                .addOptions(
                        new OptionData(OptionType.STRING, "dayofweek", "Enter day of week")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "starttime", "Enter start time")
                                .setRequired(true),
                        new OptionData(OptionType.INTEGER, "endtime", "Enter end time")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "staffname", "Enter Staff name")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "type", "Enter type").setRequired(true));
    }
}
