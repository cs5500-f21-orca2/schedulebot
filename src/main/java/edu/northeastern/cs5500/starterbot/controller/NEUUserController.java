package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.NEUUser;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NEUUserController {
    @Nonnull private GenericRepository<NEUUser> neuUserRepository;

    public NEUUser getNEUUser(String discordId) {
        for (NEUUser user : this.neuUserRepository.getAll()) {
            if (user.getDiscordId().equals(discordId)) {
                return user;
            }
        }
        return null;
    }

    public Collection<NEUUser> getAllStaff() {
        Deque<NEUUser> staffList = new ArrayDeque<>();
        for (NEUUser user : this.neuUserRepository.getAll()) {
            if (user.isStaff() == true) {
                staffList.add(user);
            }
        }
        return staffList;
    }

    public boolean updateRole(String discordId, String role) {
        NEUUser user = getNEUUser(discordId);
        if (user == null) {
            return false;
        }

        // Only allow the role to be set to a valid value
        switch (role) {
            case "student":
            case "ta":
            case "professor":
                user.setRole(role);
                break;
            default:
                return false;
        }

        neuUserRepository.update(user);
        return true;
    }
}
