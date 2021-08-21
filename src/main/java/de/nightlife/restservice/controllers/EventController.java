package de.nightlife.restservice.controllers;

import de.nightlife.restservice.models.Event;
import de.nightlife.restservice.repositories.EventRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
                                linkTo(methodOn(EventController.class).getEventCollection()).withRel("get-all-events"),
                                linkTo(methodOn(EventController.class).updateEvent(event, event.getId())).withRel("update-event"),
                                linkTo(methodOn(EventController.class).deleteEvent(event.getId())).withRel("delete-event")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createEvent(@RequestBody @Valid final Event newEvent) {
        final Event createdEvent = eventRepository.save(newEvent);
        final EntityModel<Event> createdEventResource = EntityModel.of(createdEvent,
                linkTo(methodOn(EventController.class).getSingleEvent(createdEvent.getId())).withSelfRel());

        return ResponseEntity.created(
                        createdEventResource.getRequiredLink(IanaLinkRelations.SELF).toUri()) .body(createdEvent);
    }

    @PutMapping(value = "/events/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Event>> updateEvent(@RequestBody @Valid final Event newEvent, @PathVariable Long id) {
        if (newEvent.getId() != id) {
            return ResponseEntity.badRequest().build();
        }
        final Event updatedEvent = eventRepository.findById(id)
                .map(event -> {
                    event.setName(newEvent.getName());
                    return eventRepository.save(event);
                })
                .orElseGet(() -> {
                    newEvent.setId(id);
                    return eventRepository.save(newEvent);
                });
        final EntityModel<Event> updatedEventResource = EntityModel.of(updatedEvent,
                linkTo(methodOn(EventController.class).getSingleEvent(updatedEvent.getId())).withSelfRel());

        return ResponseEntity.created(
                        linkTo(methodOn(EventController.class).getSingleEvent(updatedEvent.getId())).toUri())
                .body(updatedEventResource);
    }


    @DeleteMapping(value = "/events/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable final Long id) {
        try {
            eventRepository.deleteById(id);
            return ResponseEntity.noContent()
                    .header("get-all-events", linkTo(methodOn(EventController.class).getEventCollection()).toString())
                    .build();

        } catch (final EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound()
                    .header("get-all-events", linkTo(methodOn(EventController.class).getEventCollection()).toString())
                    .build();
        }
    }
}

