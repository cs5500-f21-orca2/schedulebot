package edu.northeastern.cs5500.starterbot.listeners.commands;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

/** The VaccinateCommand class is used to get or set the NEUUser's vaccinated status */
public class VaccinateCommand implements Command {

    private DiscordIdController discordIdController;

    /**
     * Constructor of the class VaccinatedCommand
     *
     * @param discordIdController DiscordIdController, it is a basic requirement of the constructor
     */
    public VaccinateCommand(DiscordIdController discordIdController) {
        this.discordIdController = discordIdController;
    }

    /**
     * The getName method return a String type, it return the command name that dispalys on the
     * scheduleBot
     */
    @Override
    public String getName() {
        return "vaccinated";
    }

    /**
     * The getReply method generates reply message for different cases. If the user is not valid,
     * reply with message unable to determine. Otherwise, update the vaccination status If the
     * vaccination status is true, reply with vaccinated. False reply with NOT vaccinated
     *
     * @param discordId String, the discord id
     * @param user NEUUser, the neu user
     * @param vaccinationStatus boolean, true or false input argument
     * @return
     */
    Message getReply(
            @Nullable String discordId,
            @Nullable NEUUser user,
            @Nullable boolean vaccinationStatus) {

        MessageBuilder mb = new MessageBuilder();

        if (!discordIdController.updateVaccination(discordId, vaccinationStatus)) {
            return mb.setEmbed(createNonRegisteredUserReport(discordId, user, vaccinationStatus))
                    .build();
        }

        discordIdController.updateVaccination(discordId, vaccinationStatus);
        if (vaccinationStatus) {
            return mb.setEmbed(createVaccineReport(discordId, user, vaccinationStatus)).build();
        } else {
            return mb.setEmbed(createNonVaccineReport(discordId, user, vaccinationStatus)).build();
        }
    }

    MessageEmbed createNonRegisteredUserReport(
            String discordId, NEUUser user, boolean vaccinationStatus) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("--------Vaccination Record--------");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb.setDescription(
                "Unable to determine your vaccination status." + "\n" + "Have you registered?");
        return eb.build();
    }

    MessageEmbed createVaccineReport(String discordId, NEUUser user, boolean vaccinationStatus) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("--------Vaccination Record--------");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb.setDescription("Your status is: vaccinated");
        return eb.build();
    }

    MessageEmbed createNonVaccineReport(String discordId, NEUUser user, boolean vaccinationStatus) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("--------Vaccination Record--------");
        eb.setColor(Color.CYAN);
        eb.setImage("https://brand.northeastern.edu/wp-content/uploads/4_BlackOnColor.png");
        eb.setDescription("Your status is: NOT vaccinated");
        return eb.build();
    }

    /**
     * The method onSlashCommand implement the check logic of this class. If the vaccineOption data
     * is a null input, it will returns the default value as false. Otherwise, it will get the data
     * from event option
     */
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        String discordId = event.getUser().getId();
        NEUUser user = discordIdController.getNEUUser(discordId);
        OptionMapping vaccinatedOption = event.getOption("vaccinated");
        final boolean vaccinationStatus;

        if (vaccinatedOption != null) {
            vaccinationStatus = vaccinatedOption.getAsBoolean();
        } else {
            vaccinationStatus = user.isVaccinated();
        }

        final Message reply = getReply(discordId, user, vaccinationStatus);
        event.reply(reply).queue();
    }

    /**
     * The method getCommandData requires user to input BOOLEAN True or False The option is not
     * required
     */
    @Override
    public CommandData getCommandData() {
        return new CommandData("vaccinated", "Get or set your own vaccination status.")
                .addOptions(
                        new OptionData(
                                        OptionType.BOOLEAN,
                                        "vaccinated",
                                        "true if you are vaccinated or have a waiver;"
                                                + " false if you are not")
                                .setRequired(false));
    }
}
