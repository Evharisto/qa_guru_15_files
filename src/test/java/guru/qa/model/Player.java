package guru.qa.model;

import java.util.List;

public class Player {
    public String firstName;
    public String lastName;
    public int age;
    public boolean isPlaying;
    public List<String> teams;
    public Player.PlayingCareer playingCareer;

    public static class PlayingCareer {
        public String team;
        public int games;
        public int goals;
        public String contractUntil;
    }
}
