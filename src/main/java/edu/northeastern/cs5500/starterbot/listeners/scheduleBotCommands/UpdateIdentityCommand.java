package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class UpdateIdentityCommand extends ScheduleBotCommandsWithRepositoryAbstract {
    @Override
    public String getName() {
        return "changerole";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        if (user == null) {
            event.reply("You don't have an account. Please register first!").queue();
            return;
        }

        String oldRole = user.getRole();
        String newRole = event.getOption("role").getAsString().toLowerCase();
        boolean didRoleUpdate = discordIdController.updateRole(discordId, newRole);

        if (!didRoleUpdate) {
            // Role update failed; this is because an invalid role was provided.
            event.reply("Role must be one of Student, TA, or Professor.").queue();
            return;
        }

        event.reply(String.format("Your role was changed from %s to %s.", oldRole, newRole))
                .queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData(getName(), "Change your role")
                .addOptions(
                        new OptionData(
                                        OptionType.STRING,
                                        "role",
                                        "Your role; one of Student, TA, or Professor.")
                                .setRequired(true));
    }
}
