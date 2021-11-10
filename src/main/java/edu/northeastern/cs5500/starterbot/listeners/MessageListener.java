package edu.northeastern.cs5500.starterbot.listeners;

import edu.northeastern.cs5500.starterbot.model.*;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class MessageListener extends ListenerAdapter {
    private GenericRepository<NEUUser> userRepository;
    private GenericRepository<OfficeHour> OHRepository;
    private GenericRepository<RegisterCheckList> registerCheckListRepository;
    private NEUUser user;
    OfficeHour oh;
    RegisterCheckList registerCheckList;

    public void setNEUUserRepository(GenericRepository<NEUUser> user) {
        this.userRepository = user;
    }

    public void setOHRepository(GenericRepository<OfficeHour> ohRepository) {
        this.OHRepository = ohRepository;
    }

    public void setRegisterCheckListRepository(
            GenericRepository<RegisterCheckList> registerCheckListRepository) {
        this.registerCheckListRepository = registerCheckListRepository;
    }

    public boolean isStudent(String nuid) {
        registerCheckList = getRegisterCheckList();
        if (registerCheckList != null) {
            return registerCheckList.getStudentNuidList().contains(nuid);
        } else {
            return false;
        }
    }

    public boolean isTaProf(String nuid) {
        registerCheckList = getRegisterCheckList();
        if (registerCheckList != null) {
            return registerCheckList.getTaProfNuidList().contains(nuid);
        } else {
            return false;
        }
    }

    public String isDiscordIdRegistered(String discordId) {
        registerCheckList = getRegisterCheckList();
        if (registerCheckList != null
                && registerCheckList.getDiscordIdNuidHashMap().containsKey(discordId)) {
            return registerCheckList.getDiscordIdNuidHashMap().get(discordId);
        } else {
            return null;
        }
    }

    public RegisterCheckList getRegisterCheckList() {
        if (!registerCheckListRepository.getAll().isEmpty()) {
            for (RegisterCheckList r : registerCheckListRepository.getAll()) registerCheckList = r;
            return registerCheckList;
        } else return null;
    }

    public NEUUser getNeuUser(String nuid) {
        NEUUser neuUser = null;
        for (NEUUser n : userRepository.getAll()) {
            if (n.getNuid() == nuid) {
                neuUser = n;
                return neuUser;
            }
        }
        return null;
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        switch (event.getName()) {
            case "register":
                {
                    String[] infoArr = event.getOption("content").getAsString().split("\\s+");
                    String role = infoArr[2].toLowerCase();
                    String nuid = infoArr[0];
                    if (role.equals("student")) {
                        if (!isStudent(nuid)) {
                            event.reply("Invalid input or You are not a student \n Try Again")
                                    .queue();
                            break;
                        } else {
                            user = new NEUUser(infoArr[0], infoArr[1]);
                            RegisterCheckList updateRegisterCheckList = getRegisterCheckList();
                            updateRegisterCheckList
                                    .getDiscordIdNuidHashMap()
                                    .put(event.getUser().getId(), nuid);
                            this.registerCheckListRepository.update(updateRegisterCheckList);
                        }
                    } else if (role.equals("ta") || role.equals("professor")) {
                        if (!isTaProf(nuid)) {
                            event.reply(
                                            "Invalid input or You are not a TA or Professor \n Try Again")
                                    .queue();
                            break;
                        } else {
                            user = new NEUUser(infoArr[0], infoArr[1]);
                            user.setStuff(true);
                            RegisterCheckList updateRegisterCheckList = getRegisterCheckList();
                            updateRegisterCheckList
                                    .getDiscordIdNuidHashMap()
                                    .put(event.getUser().getId(), nuid);
                            this.registerCheckListRepository.update(updateRegisterCheckList);
                        }
                    } else {
                        event.reply("Invalid input, Try Agian").queue();
                        break;
                    }
                    userRepository.add(user);
                    event.reply("You have been registered!").queue();
                    break;
                }

            case "reserve":
                {
                    String nuid = isDiscordIdRegistered(event.getUser().getId());
                    if (nuid != null) {
                        NEUUser neuUser = getNeuUser(nuid);
                        event.reply("Welcome back " + neuUser.getUserName()).queue();
                    } else {
                        event.reply("You haven't registered yet, Please register").queue();
                        break;
                    }
                    String[] infoArr = event.getOption("content").getAsString().split("\\s+");
                    String dayOfWeek = infoArr[1].toLowerCase();
                    String type = infoArr[2].toLowerCase();
                    String startTime = infoArr[3];
                    String endTime = infoArr[4];
                    if (dayOfWeek.equals("sunday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.SUNDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("monday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.MONDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("tuesday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.TUESDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("wednesday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.WEDNESDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("thursday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.THURSDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("friday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.FRIDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else if (dayOfWeek.equals("saturday")) {
                        oh =
                                new OfficeHour(
                                        DayOfWeek.SATURDAY,
                                        new OfficeHourType(type),
                                        Integer.parseInt(startTime),
                                        Integer.parseInt(endTime));
                    } else {
                        event.reply("You have error in your input, please try agian.").queue();
                        break;
                    }
                    this.OHRepository.add(oh);

                    event.reply("You made a reservation!").queue();
                    break;
                }
            case "time":
                {
                    Date timestamp = new Date();
                    DateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
                    df.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                    String temp = df.format(timestamp);
                    event.reply(temp).queue();
                    break;
                }
                // case "say":
                //     {
                //         event.reply(event.getOption("content").getAsString()).queue();
                //         // event.reply(this.user.getId()).queue();
                //         break;
                //     }
            case "vaccinated":
                {
                    OptionMapping vaccinated = event.getOption("vaccinated");

                    StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder.append("Your status is: ");

                    if (vaccinated != null) {
                        responseBuilder.append(vaccinated.getAsBoolean());
                    } else {
                        responseBuilder.append("UNKNOWN");
                    }
                    event.reply(responseBuilder.toString()).queue();
                    break;
                }
        }
    }
}
