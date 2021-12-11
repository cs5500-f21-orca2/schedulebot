package edu.northeastern.cs5500.starterbot.listeners.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * This interface is used to store all the commands ScheduleBot needs to receive in order to
 * function.
 */
public interface Command {

    /** Returns the command name as a string. */
    public String getName();

    /**
     * Performs a slash command method.
     *
     * @param event an event from Discord's slash command
     */
    public void onSlashCommand(SlashCommandEvent event);

    /**
     * Constructs a command from guided user input.
     *
     * @return CommandData
     */
    public CommandData getCommandData();
}
