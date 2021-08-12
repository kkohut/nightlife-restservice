package de.nightlife.restservice.controllers.artist.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ArtistNotFoundException extends RuntimeException {

    public ArtistNotFoundException(final Long id) {
        super("Could not find artist with id " + id);
    }
}
