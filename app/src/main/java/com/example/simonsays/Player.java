package com.example.simonsays;

import java.io.Serializable;

class Player implements Serializable, Comparable<Player> {
    private String username;
    private int score;

    public Player(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return username + "," + score;
    }

    @Override
    public int compareTo(Player o) {
        if (this.getScore() < o.getScore()){
            return 1;
        }else if (this.getScore() > o.getScore()){
            return -1;
        }else{
            return 0;
        }
    }
}
