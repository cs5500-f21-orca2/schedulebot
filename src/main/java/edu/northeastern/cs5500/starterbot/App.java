package edu.northeastern.cs5500.starterbot;

import static spark.Spark.*;

import edu.northeastern.cs5500.starterbot.controller.DiscordIdController;
import edu.northeastern.cs5500.starterbot.listeners.MessageListener;
import edu.northeastern.cs5500.starterbot.model.DiscordIdLog;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.repository.MongoDBRepository;
import edu.northeastern.cs5500.starterbot.service.MongoDBService;
import java.util.EnumSet;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class App {

    static String getBotToken() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String token = processBuilder.environment().get("BOT_TOKEN");
        return token;
    }

    public static void main(String[] arg) throws LoginException {
        String token = getBotToken();
        if (token == null) {
            throw new IllegalArgumentException(
                    "The BOT_TOKEN environment variable is not defined.");
        }

        MessageListener messageListener = new MessageListener();
        MongoDBService mongoDBService = new MongoDBService();
        GenericRepository<NEUUser> userRepository =
                new MongoDBRepository<NEUUser>(NEUUser.class, mongoDBService);

        GenericRepository<OfficeHour> officeHourRepository =
                new MongoDBRepository<OfficeHour>(OfficeHour.class, mongoDBService);

        GenericRepository<DiscordIdLog> discordIdLogRepository =
                new MongoDBRepository<DiscordIdLog>(DiscordIdLog.class, mongoDBService);

        DiscordIdController discordIdController =
                new DiscordIdController(discordIdLogRepository, userRepository);

        messageListener.getRegister().setUserRepository(userRepository);
        messageListener.getRegister().setDiscordIdLogRepository(discordIdLogRepository);
        messageListener.getRegister().setDiscordIdController(discordIdController);

        messageListener.getVaccinate().setUserRepository(userRepository);
        messageListener.getVaccinate().setDiscordIdLogRepository(discordIdLogRepository);
        messageListener.getVaccinate().setDiscordIdController(discordIdController);

        messageListener.getCovidsymptom().setUserRepository(userRepository);
        messageListener.getCovidsymptom().setDiscordIdLogRepository(discordIdLogRepository);
        messageListener.getCovidsymptom().setDiscordIdController(discordIdController);

        messageListener.getReserve().setOfficeHourRepository(officeHourRepository);

        messageListener.getCreateOfficeHour().setUserRepository(userRepository);
        messageListener.getCreateOfficeHour().setDiscordIdLogRepository(discordIdLogRepository);
        messageListener.getCreateOfficeHour().setDiscordIdController(discordIdController);

        messageListener.getListAllOfficeHour().setUserRepository(userRepository);
        messageListener.getListAllOfficeHour().setDiscordIdLogRepository(discordIdLogRepository);
        messageListener.getListAllOfficeHour().setDiscordIdController(discordIdController);

        messageListener.getDeleteOfficeHour().setUserRepository(userRepository);
        messageListener.getDeleteOfficeHour().setDiscordIdLogRepository(discordIdLogRepository);
        messageListener.getDeleteOfficeHour().setDiscordIdController(discordIdController);

        messageListener.getAlltaavailableofficehour().setUserRepository(userRepository);
        messageListener
                .getAlltaavailableofficehour()
                .setDiscordIdLogRepository(discordIdLogRepository);
        messageListener.getAlltaavailableofficehour().setDiscordIdController(discordIdController);

        JDA jda =
                JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class))
                        .addEventListeners(messageListener)
                        .build();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(messageListener.getTime().getCommandData());
        commands.addCommands(messageListener.getRegister().getCommandData());
        commands.addCommands(messageListener.getReserve().getCommandData());
        commands.addCommands(messageListener.getVaccinate().getCommandData());
        commands.addCommands(messageListener.getCovidsymptom().getCommandData());
        commands.addCommands(messageListener.getCreateOfficeHour().getCommandData());
        commands.addCommands(messageListener.getListAllOfficeHour().getCommandData());
        commands.addCommands(messageListener.getDeleteOfficeHour().getCommandData());
        commands.addCommands(messageListener.getRules().getCommandData());
        commands.addCommands(messageListener.getAlltaavailableofficehour().getCommandData());

        commands.queue();

        port(8080);

        get(
                "/",
                (request, response) -> {
                    return "{\"status\": \"OK\"}";
                });
    }
}
