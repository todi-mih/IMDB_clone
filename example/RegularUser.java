
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;
import java.util.SortedSet;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RegularUser extends User<Rating> {

    public RegularUser(String name, String country, int age, char gender, int experience, String username) {
        super(name, country, age, gender, experience, username);
    }

    @Override
    protected SortedSet<Rating> createFavoritesSet() {
        return new TreeSet<>();
    }

    public static RegularUser createUser(String name, String country, int age, char gender, int experience, String username) {
        return new RegularUser(name, country, age, gender, experience, username);
    }

    public static void addUserToFile(String filePath, String name, String country, int age, char gender, int experience, String username) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            List<User<?>> existingUsers = objectMapper.readValue(new File(filePath), new TypeReference<List<User<?>>>() {});

            User<?> newUser = createUser(name, country, age, gender, experience, username);

            existingUsers.add(newUser);

            objectMapper.writeValue(new FileWriter(filePath), existingUsers);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
