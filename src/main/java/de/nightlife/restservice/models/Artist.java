package de.nightlife.restservice.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private long id;

    @NotEmpty
    @Column(name = "name", nullable= false)
    private String name;

    public Artist() {
    }

    public Artist(final String name) {
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
