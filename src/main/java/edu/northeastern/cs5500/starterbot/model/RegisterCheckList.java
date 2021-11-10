package edu.northeastern.cs5500.starterbot.model;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class RegisterCheckList implements Model {
    private ObjectId id;
    @Nonnull private HashMap<String, String> discordIdNuidHashMap;
    @Nonnull private List<String> taProfNuidList;
    @Nonnull private List<String> studentNuidList;
}
