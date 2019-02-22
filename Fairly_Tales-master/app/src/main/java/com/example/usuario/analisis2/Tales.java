package com.example.usuario.analisis2;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tales implements Serializable {
    ;
    @SerializedName("logo")
    private String logo;
    @SerializedName("title")
    private String title;
    @SerializedName("Synopsis")
    private String synopsis;
    @SerializedName("resume")
    private String resume;

    public Tales(String logo, String title, String synopsis, String resume) {
        this.logo = logo;
        this.title = title;
        this.synopsis = synopsis;
        this.resume = resume;
    }


    String getLogo() {
        return logo;
    }


    String getTitle() {
        return title;
    }


    String getSynopsis() {
        return synopsis;
    }

    String getResume() {
        return resume;
    }
}
