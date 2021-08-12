package de.nightlife.restservice.controllers.artist.ExceptionHandlers;

import de.nightlife.restservice.controllers.artist.Exceptions.ArtistInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ArtistInvalidAdvice {
    @ResponseBody
    @ExceptionHandler(ArtistInvalidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String artistNotFoundHandler(final ArtistInvalidException ex) {
        return ex.getMessage();
    }
}
