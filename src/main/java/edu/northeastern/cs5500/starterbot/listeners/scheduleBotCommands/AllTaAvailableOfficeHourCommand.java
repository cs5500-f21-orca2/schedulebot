package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import java.util.Collection;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class AllTaAvailableOfficeHourCommand extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "alltaavailableofficehour";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        StringBuilder sb = new StringBuilder();
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        if (user == null) {
            event.reply("Please register before checking office hour").queue();
            return;
        }
        if (user.isStaff() == true) {
            event.reply("TA/Prof please use other command-line function to check").queue();
            return;
        }
        Collection<NEUUser> taProfList = discordIdController.getAllStaff();
        if (taProfList.isEmpty()) {
            event.reply("No office hours available").queue();
            return;
        }

        sb.append("Available Office Hours: \n");
        boolean someHoursAvailable = false;
        for (NEUUser taProf : taProfList) {
            List<OfficeHour> officeHourList = taProf.getInvolvedOfficeHours();
            if (officeHourList == null || officeHourList.isEmpty()) {
                continue;
            }
            for (OfficeHour officeHour : officeHourList) {
                if (officeHour.getAttendeeNUID() != null) continue;
                sb.append("TA/Professor: " + taProf.getUserName() + "\t");
                sb.append(officeHour.toString() + "\t");
                sb.append("Type: " + officeHour.getOfficeHourType().getTypeName() + "\n");
                someHoursAvailable = true;
            }
            sb.append("\n");
        }
        if (!someHoursAvailable) {
            event.reply("No Available Office Hours").queue();
        } else {
            event.reply(sb.toString()).queue();
        }
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("alltaavailableofficehour", "List all Available TA office Hour.");
    }
}
