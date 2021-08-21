package de.nightlife.restservice.controllers.event;

import de.nightlife.restservice.models.Event;
import de.nightlife.restservice.repositories.EventRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EventController {
    final EventRepository eventRepository;

    public EventController(final EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Event>>> getEventCollection() {
        final List<EntityModel<Event>> events = StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .map(event -> EntityModel.of(event,
                        linkTo(methodOn(EventController.class).getSingleEvent(event.getId())).withSelfRel(),
                        linkTo(methodOn(EventController.class).getEventCollection()).withRel("get-all-events")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(events,
                linkTo(methodOn(EventController.class).getEventCollection()).withSelfRel()));
    }

    @GetMapping(value = "/events/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Event>> getSingleEvent(@PathVariable long id) {
        return eventRepository.findById(id)
                .map(event -> EntityModel.of(event)
                        .add(
                                linkTo(methodOn(EventController.class).getSingleEvent(event.getId())).withSelfRel(),
                                linkTo(methodOn(EventController.class).getEventCollection()).withRel("get-all-events")))
                                //linkTo(methodOn(EventController.class).updateEvent(artist, artist.getId())).withRel("update-event"),
                                //linkTo(methodOn(EventController.class).deleteEvent(artist.getId())).withRel("delete-event")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
