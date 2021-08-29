package de.nightlife.restservice.controllers;

import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.models.ArtistDTO;
import de.nightlife.restservice.models.Event;
import de.nightlife.restservice.repositories.ArtistRepository;
import de.nightlife.restservice.repositories.EventRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EventController {
    final EventRepository eventRepository;
    final ArtistRepository artistRepository;

    public EventController(final EventRepository eventRepository, ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
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
                                linkTo(methodOn(EventController.class).deleteEvent(event.getId())).withRel("delete-event"),
                                linkTo(methodOn(EventController.class).getArtistCollectionOfEvent(event.getId())).withRel("get-artist-collection-of-event")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/events", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createEvent(@RequestBody @Valid final Event newEvent) {
        final Event createdEvent = eventRepository.save(newEvent);
        final EntityModel<Event> createdEventResource = EntityModel.of(createdEvent,
                linkTo(methodOn(EventController.class).getSingleEvent(createdEvent.getId())).withSelfRel());

        return ResponseEntity.created(
                createdEventResource.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(createdEvent);
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

    @GetMapping(value = "/events/{eventId}/artists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Artist>>> getArtistCollectionOfEvent(@PathVariable final long eventId) {
        final Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        final List<EntityModel<Artist>> artistsOfEvent = event.get()
                .getArtists()
                .stream()
                .map(artist -> EntityModel.of(artist)
                        .add(linkTo(methodOn(EventController.class).getSingleArtistOfEvent(eventId, artist.getId())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(artistsOfEvent)
                .add(linkTo(methodOn(EventController.class).getEventCollection()).withRel("get-event-collection")));
    }

    @GetMapping(value = "/events/{eventId}/artists/{artistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Artist>> getSingleArtistOfEvent(@PathVariable final long eventId, @PathVariable final long artistId) {
        final Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        final Optional<Artist> artistOfEvent = event.get()
                .getArtists()
                .stream()
                .filter(artist -> artist.getId() == artistId)
                .findFirst();

        if (artistOfEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityModel.of(artistOfEvent.get())
                .add(linkTo(methodOn(EventController.class).unlinkArtistFromEvent(event.get().getId(), artistOfEvent.get().getId())).withRel("unlink-artist-from-event"),
                        linkTo(methodOn(EventController.class).getArtistCollectionOfEvent(event.get().getId())).withRel("get-artist-collection-of-event")
                ));
    }

    @PutMapping(value = "/events/{eventId}/artists/{artistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ArtistDTO>>> linkArtistToEvent(@PathVariable Long eventId, @PathVariable Long artistId) throws URISyntaxException {
        final Optional<Artist> newArtist = artistRepository.findById(artistId);
        final Optional<Event> updatedEvent = eventRepository.findById(eventId)
                .map(event -> {
                    if (newArtist.isPresent()) {
                        event.addArtist(newArtist.get());
                    }
                    return eventRepository.save(event);
                });

        if (updatedEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<EntityModel<ArtistDTO>> artistDTOs = StreamSupport.stream(updatedEvent.get().getArtistDTOs().spliterator(), false)
                .map(artist -> EntityModel.of(artist,
                        linkTo(methodOn(ArtistController.class).getSingleArtist(artist.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.created(
                        linkTo(methodOn(EventController.class).getSingleArtistOfEvent(updatedEvent.get().getId(), artistId)).toUri())
                .body(CollectionModel.of(artistDTOs));
    }

    @DeleteMapping(value = "/events/{eventId}/artists/{artistId}")
    public ResponseEntity<Event> unlinkArtistFromEvent(@PathVariable final Long eventId, @PathVariable final Long artistId) {
        final Optional<Event> updatedEvent = eventRepository.findById(eventId)
                .map(event -> {
                    for (final Artist artist : event.getArtists()) {
                        if (artist.getId() == artistId) {
                            event.removeArtist(artist);
                            break;
                        }
                    }
                    return eventRepository.save(event);
                });

        if (updatedEvent.isEmpty()) {
            return ResponseEntity.notFound()
                    .header("Link", linkTo(methodOn(EventController.class).getEventCollection()).withRel("get-event-collection").toString())
                    .build();
        }
        return ResponseEntity.noContent()
                .header("Link", linkTo(methodOn(EventController.class).getEventCollection()).withRel("get-event-collection").toString())
                .build();
    }
}
