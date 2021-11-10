package edu.northeastern.cs5500.starterbot;

import static spark.Spark.*;

import edu.northeastern.cs5500.starterbot.listeners.MessageListener;
import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.model.OfficeHour;
import edu.northeastern.cs5500.starterbot.model.RegisterCheckList;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.repository.MongoDBRepository;
import edu.northeastern.cs5500.starterbot.service.MongoDBService;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
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

        GenericRepository<OfficeHour> ohRepository =
                new MongoDBRepository<OfficeHour>(OfficeHour.class, mongoDBService);

        GenericRepository<RegisterCheckList> registerCheckListRepository =
                new MongoDBRepository<RegisterCheckList>(RegisterCheckList.class, mongoDBService);

        messageListener.setNEUUserRepository(userRepository);
        messageListener.setOHRepository(ohRepository);

        /** ------------------------------------------------------------------ */
        // Initialize Nuid database
        // Added sample NUID for ta and student
        // User can be register only if their nuid in the registerCheckListRepository
        // Prevent non-NEU students from enrolling
        if (registerCheckListRepository.getAll().isEmpty()) {
            List<String> studentNuidList = new ArrayList<>();
            studentNuidList.add("1111");
            studentNuidList.add("2222");
            List<String> taProfNuidList = new ArrayList<>();
            taProfNuidList.add("0000");
            taProfNuidList.add("9999");
            HashMap<String, String> map = new HashMap<>();
            map.put("123123123", "value");
            RegisterCheckList registerCheckListInitialize =
                    new RegisterCheckList(map, taProfNuidList, studentNuidList);
            registerCheckListRepository.add(registerCheckListInitialize);
        }
        /** ------------------------------------------------------------------ */
        messageListener.setRegisterCheckListRepository(registerCheckListRepository);

        JDA jda =
                JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class))
                        .addEventListeners(messageListener)
                        .build();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                // new CommandData("say", "Makes the bot say what you told it to say")
                //         .addOptions(
                //                 new OptionData(
                //                                 OptionType.STRING,
                //                                 "content",
                //                                 "What the bot should say")
                //                         .setRequired(true)),

                new CommandData("reserve", "Make a reservation")
                        .addOptions(
                                new OptionData(
                                                OptionType.STRING,
                                                "content",
                                                "format: {TAsName} {WhitchDAY} {inPerson/Online} {StartTime} {EndTime}")
                                        .setRequired(true)),
                new CommandData("register", "register a student by name,NUID, and role")
                        .addOptions(
                                new OptionData(
                                                OptionType.STRING,
                                                "content",
                                                "format: {firstname} {NUID} {role(Student/TA/Professor)}")
                                        .setRequired(true)),
                new CommandData("time", "Display current time"),
                new CommandData("vaccinated", "Get or set your own vaccination status.")
                        .addOptions(
                                new OptionData(
                                                OptionType.BOOLEAN,
                                                "vaccinated",
                                                "true if you are vaccinated; false if you are not")
                                        .setRequired(false)));
        commands.queue();

        port(8080);

        get(
                "/",
                (request, response) -> {
                    return "{\"status\": \"OK\"}";
                });
    }
}
