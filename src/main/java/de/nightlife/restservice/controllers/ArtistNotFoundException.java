package de.nightlife.restservice.controllers;

public class ArtistNotFoundException extends RuntimeException {

    public ArtistNotFoundException(final Long id) {
        super("Could not find artist with id " + id);
    }
}
