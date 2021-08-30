package de.nightlife.restservice.controllers;

import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.models.Event;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(maxAge = 3600)
@RestController
public class DispatcherController {

    @GetMapping(value = "/")
    public ResponseEntity getDispatcher() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Link", linkTo(methodOn(ArtistController.class).getArtistCollection()).withRel("get-all-artists").toString());
        responseHeaders.set("Link", linkTo(methodOn(ArtistController.class).createArtist(new Artist())).withRel("create-artist").toString());
        responseHeaders.set("Link", linkTo(methodOn(EventController.class).getEventCollection()).withRel("get-all-events").toString());
        responseHeaders.set("Link", linkTo(methodOn(EventController.class).createEvent(new Event())).withRel("create-event").toString());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("");
    }
}
