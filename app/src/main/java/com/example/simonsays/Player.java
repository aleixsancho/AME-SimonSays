package com.example.simonsays;

import java.io.Serializable;

class Player implements Serializable, Comparable<Player> {
    private String username;
    private int score;

    /**
     * Constructor.
     *
     * @param username String that indicates the name of the player.
     * @param score    Integer that corresponds to player's score.
     */
    public Player(String username, int score) {
        this.username = username;
        this.score = score;
    }

    /**
     * Getter of username.
     *
     * @return String.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter of username.
     *
     * @param username String to set username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter of score
     *
     * @return Integer.
     */
    public int getScore() {
        return score;
    }

    /**
     * Setter of score.
     *
     * @param score Integer to set score.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * To string.
     *
     * @return String.
     */
    @Override
    public String toString() {
        return username + "," + score;
    }

    /**
     * Compare method.
     *
     * @param o Player.
     * @return Integer, -1 if player score is smaller, 1 if is bigger, 0 if are equals.
     */
    @Override
    public int compareTo(Player o) {
        if (this.getScore() < o.getScore()) {
            return 1;
        } else if (this.getScore() > o.getScore()) {
            return -1;
        } else {
            return 0;
        }
    }
}
