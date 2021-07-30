package de.nightlife.restservice.controllers.artist;

public class ArtistNotFoundException extends RuntimeException {

    public ArtistNotFoundException(final Long id) {
        super("Could not find artist with id " + id);
    }
}
