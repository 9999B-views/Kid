package com.example.aferyannie.learningapp;

import java.io.Serializable;

/**
 * Created by aferyannie on 04/10/18.
 */

public class Name implements Serializable { // Serializable is used for passing object between running processes.
    private String name;
    private Double score;
    private Long created_at;

    public Name(){

    }

    public Name(String name, Double score, Long created_at) {
        this.name = name;
        this.score = score;
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public Double getScore() {
        return score;
    }

    public Long getCreated_at() {
        return created_at;
    }

}
