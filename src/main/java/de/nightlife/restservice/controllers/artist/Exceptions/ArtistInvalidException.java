package de.nightlife.restservice.controllers.artist.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArtistInvalidException extends RuntimeException {
    public ArtistInvalidException() {
        super("Artist malformed");
    }
}
