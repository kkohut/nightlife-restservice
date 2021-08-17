package de.nightlife.restservice.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class Artist {

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    private String name;

    public Artist() {
    }

    public Artist(final String name) {
        this.name = name;
    }

    public Artist(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
