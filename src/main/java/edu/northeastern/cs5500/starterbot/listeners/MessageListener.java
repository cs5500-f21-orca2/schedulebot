package edu.northeastern.cs5500.starterbot.listeners;

import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.AllTaAvailableOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.CheckInPersonOHCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.CreateOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.DeleteOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ListAllOfficeHourCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.RegisterCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ReserveCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.RulesCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ScheduleBotCommandsInterface;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.ScheduleBotCommandsWithRepositoryAbstract;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.Staffdailyofficehour;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.SymptomCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.TimeCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.UpdateIdentityCommand;
import edu.northeastern.cs5500.starterbot.listeners.scheduleBotCommands.VaccinateCommand;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Data
@EqualsAndHashCode(callSuper = false)
public class MessageListener extends ListenerAdapter {
    private Map<String, ScheduleBotCommandsInterface> commandsHashMap;
    @Nonnull ScheduleBotCommandsInterface time;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract register;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract reserve;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract vaccinate;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract covidsymptom;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract createOfficeHour;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract listAllOfficeHour;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract deleteOfficeHour;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract rules;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract alltaavailableofficehour;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract staffdailyofficehour;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract checkinpersonoh;
    @Nonnull ScheduleBotCommandsWithRepositoryAbstract updateidentity;

    public MessageListener() {
        commandsHashMap = new HashMap<>();
        time = new TimeCommand();
        register = new RegisterCommand();
        reserve = new ReserveCommand();
        vaccinate = new VaccinateCommand();
        covidsymptom = new SymptomCommand();
        createOfficeHour = new CreateOfficeHourCommand();
        listAllOfficeHour = new ListAllOfficeHourCommand();
        deleteOfficeHour = new DeleteOfficeHourCommand();
        rules = new RulesCommand();
        alltaavailableofficehour = new AllTaAvailableOfficeHourCommand();
        staffdailyofficehour = new Staffdailyofficehour();
        checkinpersonoh = new CheckInPersonOHCommand();
        updateidentity = new UpdateIdentityCommand();

        commandsHashMap.put(time.getName(), time);
        commandsHashMap.put(register.getName(), register);
        commandsHashMap.put(reserve.getName(), reserve);
        commandsHashMap.put(vaccinate.getName(), vaccinate);
        commandsHashMap.put(covidsymptom.getName(), covidsymptom);
        commandsHashMap.put(createOfficeHour.getName(), createOfficeHour);
        commandsHashMap.put(listAllOfficeHour.getName(), listAllOfficeHour);
        commandsHashMap.put(deleteOfficeHour.getName(), deleteOfficeHour);
        commandsHashMap.put(rules.getName(), rules);
        commandsHashMap.put(alltaavailableofficehour.getName(), alltaavailableofficehour);
        commandsHashMap.put(staffdailyofficehour.getName(), staffdailyofficehour);
        commandsHashMap.put(checkinpersonoh.getName(), checkinpersonoh);
        commandsHashMap.put(updateidentity.getName(), updateidentity);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (commandsHashMap.containsKey(event.getName())) {
            ScheduleBotCommandsInterface scheduleBotCommands = commandsHashMap.get(event.getName());
            scheduleBotCommands.onSlashCommand(event);
            return;
        }
    }
}
