package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class RegisterCommand extends ScheduleBotCommandsWithRepositoryAbstract {

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String discordId = event.getUser().getId();

        NEUUser user = discordIdController.getNEUUser(discordId);

        if (user != null) {
            event.reply("Welcome back:  " + user.getUserName()).queue();
            return;
        }

        String nickname = event.getOption("nickname").getAsString();
        String nuid = event.getOption("nuid").getAsString();
        String role = event.getOption("role").getAsString().toLowerCase();

        switch (role) {
            case "student":
            case "ta":
            case "professor":
                break;
            default:
                event.reply("Role must be one of Student, TA, or Professor.").queue();
                return;
        }
        user = new NEUUser(discordId, nickname, nuid, role);

        userRepository.add(user);
        event.reply("You have been registered!").queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("register", "register a student by name, NUID, and role")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "nickname",
                                        "How you want the bot to call you")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "nuid", "Your NUID").setRequired(true),
                        new OptionData(
                                        OptionType.STRING,
                                        "role",
                                        "Your role; one of Student, TA, or Professor.")
                                .setRequired(true));
    }
}
