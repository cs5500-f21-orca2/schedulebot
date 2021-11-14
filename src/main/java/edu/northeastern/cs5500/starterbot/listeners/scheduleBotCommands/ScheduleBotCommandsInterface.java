package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface ScheduleBotCommandsInterface {
    public String getName();

    public void onSlashCommand(SlashCommandEvent event);

    public CommandData getCommandData();
}
