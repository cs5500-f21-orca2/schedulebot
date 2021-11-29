package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class CheckInPersonOH extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "checkinpersonoh";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        StringBuilder sb = new StringBuilder();
        String discordId = event.getUser().getId();
        NEUUser botUser = discordIdController.getNEUUser(discordId);
        if (botUser == null) {
            event.reply("You have to register first!").queue();
            return;
        }
        if (botUser.isStaff() == false) {
            event.reply("This command only allows TA/Prof to check their in-perosn appointments.")
                    .queue();
            return;
        }
        List<OfficeHour> botUserOfficeHourList = botUser.getInvolvedOfficeHours();
        for (OfficeHour oh : botUserOfficeHourList) {
            if (oh.getOfficeHourType().getTypeName().toString().toUpperCase() == "INPERSON") {
                sb.append("In-person OH: " + oh.toString());
                sb.append("\n");
                event.reply(sb.toString()).queue();
            }

            event.reply("You don't have inperson office hour appointments.").queue();
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("checkinpersonoh", "List all of my in-person meeting.");
    }
}
