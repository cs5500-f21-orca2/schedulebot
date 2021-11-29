package edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands;

import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class RulesCommand extends ScheduleBotCommandsWithRepositoryAbstract {
    @Override
    public String getName() {
        return "rules";
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

        embed.setTitle("Office Hour Schedule Bot");
        embed.setDescription("Let's make office hour more efficient");
        embed.addField(
                "Step 1",
                "/register: provide your first name, NUID, and role in order (only for first time user)",
                false);
        embed.addField(
                "Step 2",
                "/vaccinated: if schedule for a in-person appointment, please provide your vaccinated status",
                false);
        embed.addField(
                "Step 3", "/reserve: make your reservation! pick a time fits for you", false);
        embed.addField("Additional", "/listmyofficehour: check your scheduled appointments", true);
        embed.setAuthor("Fuen,Yuchi,Hao,Ziling,Fanxing");
        MessageEmbed eb = embed.build();
        event.replyEmbeds(eb).queue();
    }

    @Override
    public CommandData getCommandData() {
        return new CommandData("rules", "Read me first");
    }
}
