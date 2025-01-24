import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class User<T> {
    private Information information;
    private AccountType accountType;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<T> favorites;

    public User(String name, String country, int age, char gender, int experience, String username) {
        this.information = new Information(name, country, age, gender);
        this.accountType = AccountType.REGULAR;
        this.username = username;
        this.experience = experience;
        this.notifications = List.of();
        this.favorites = createFavoritesSet();
    }

    protected abstract SortedSet<T> createFavoritesSet();

    public enum AccountType {
        REGULAR,
        CONTRIBUTOR,
        ADMIN
    }

    public class Information {
        private String name;
        private String country;
        private int age;
        private char gender;

        public Information(String name, String country, int age, char gender) {
            this.name = name;
            this.country = country;
            this.age = age;
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public char getGender() {
            return gender;
        }
    }
}


class ContributorUser extends User<Movie> {

    public ContributorUser(String name, String country, int age, char gender, int experience, String username) {
        super(name, country, age, gender, experience, username);
    }

    @Override
    protected SortedSet<Movie> createFavoritesSet() {
        return new TreeSet<>();
    }

    public void addRequest(Request request) {
    }

    public void deleteRequest(Request request) {
    }
}

class AdminUser extends User<Actor> {

    public AdminUser(String name, String country, int age, char gender, int experience, String username) {
        super(name, country, age, gender, experience, username);
    }

    @Override
    protected SortedSet<Actor> createFavoritesSet() {
        return new TreeSet<>();
    }

    public void addActor(Actor actor) {
    }

    public void deleteActor(Actor actor) {
    }

    public void resolveRequest(Request request) {
    }
}


