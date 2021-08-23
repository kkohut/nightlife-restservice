package de.nightlife.restservice.controllers;

import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.models.Event;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class DispatcherController {

    @GetMapping(value = "/")
    public ResponseEntity getDispatcher() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("get-all-artists", linkTo(methodOn(ArtistController.class).getArtistCollection()).toString());
        responseHeaders.set("create-artist", linkTo(methodOn(ArtistController.class).createArtist(new Artist())).toString());
        responseHeaders.set("get-all-events", linkTo(methodOn(EventController.class).getEventCollection()).toString());
        responseHeaders.set("create-event", linkTo(methodOn(EventController.class).createEvent(new Event())).toString());


        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("");
    }
}
