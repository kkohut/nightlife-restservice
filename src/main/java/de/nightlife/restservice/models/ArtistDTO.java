package de.nightlife.restservice.models;

public class ArtistDTO {

    private long id;

    private String name;


    public ArtistDTO() {
    }

    public ArtistDTO(final Artist artist) {
        this.id = artist.getId();
        this.name = artist.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
