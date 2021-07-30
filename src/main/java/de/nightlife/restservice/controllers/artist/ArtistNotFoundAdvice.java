package de.nightlife.restservice.controllers.artist;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ArtistNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ArtistNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String artistNotFoundHandler(final ArtistNotFoundException ex) {
        return ex.getMessage();
    }
}
