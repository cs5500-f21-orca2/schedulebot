package edu.northeastern.cs5500.starterbot.listeners.commands;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/**
 * The SymptomCommand class is used to get or set if the NEUUser is experiencing covid symptom. If
 * it is true, they will not be able to attend inperson office hour
 */
public class SymptomCommand implements Command {

    private DiscordIdController discordIdController;

    /**
     * Constructor of the class SymptomCommand
     *
     * @param discordIdController DiscordIdController, it is a basic requirement of the constructor
     */
    public SymptomCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    /**
     * The getName method return a String type, it return the command name that dispalys on the
     * scheduleBot
     */
    @Override
    public String getName() {
        return "covidsymptom";
    }

    /**
     * The getReply method generates reply message for different cases. If the user is not valid,
     * reply with message unable to determine. Otherwise, update the symptomatic status. If the
     * symptomatic status is true, reply with experiencing covid symptom. False reply with NOT
     * experiencing
     *
     * @param discordId String, the discord id
     * @param user NEUUser, the neu user
     * @param vaccinationStatus boolean, true or false input argument
     * @return
     */
    Message getReply(
            @Nullable String discordId,
            @Nullable NEUUser user,
            @Nullable boolean covidSymptomStatus) {

        MessageBuilder mb = new MessageBuilder();

        if (!discordIdController.updateSymptomatic(discordId, covidSymptomStatus)) {
            return mb.append(
                            "Unable to determine whether you have covid symptom or not; have you registered?")
                    .build();
        }

        discordIdController.updateSymptomatic(discordId, covidSymptomStatus);
        if (covidSymptomStatus) {
            return mb.append(
                            "You are experiencing covid symptom, So you will not be able to"
                                    + "\n"
                                    + "make an inperson office hour appointment in order to avoid potential risk"
                                    + "\n"
                                    + "Thanks for your understanding!")
                    .build();
        } else {
            return mb.append(
                            "You are NOT experiencing covid symptom. So you can make inperson office hour appointment")
                    .build();
        }
    }

    /**
     * The method onSlashCommand implement the check logic of this class. If the covidsymptom data
     * is a null input, it will returns the default value as false. Otherwise, it will get the data
     * from event option
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        OptionMapping covidsymptom = event.getOption("covidsymptom");
        final boolean covidSymptomStatus;

        if (covidsymptom != null) {
            covidSymptomStatus = covidsymptom.getAsBoolean();
        } else {
            covidSymptomStatus = user.isSymptomatic();
        }

        final Message reply = getReply(discordId, user, covidSymptomStatus);
        event.reply(reply).queue();
    }

    /**
     * The method getCommandData requires user to input BOOLEAN True or False The option is not
     * required
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData(
                        "covidsymptom", "Please indicate if you are experiencing covid symptom")
                .addOptions(
                        new OptionData(
                                        OptionType.BOOLEAN,
                                        "covidsymptom",
                                        "true if you are experiencing covid symptom; false if you are not")
                                .setRequired(false));
    }
}
