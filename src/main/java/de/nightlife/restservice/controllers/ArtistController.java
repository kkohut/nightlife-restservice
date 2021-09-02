package de.nightlife.restservice.controllers;

import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.models.Event;
import de.nightlife.restservice.models.EventDTO;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(maxAge = 3600)
@RestController
public class ArtistController {

    final ArtistRepository artistRepository;

    final EventRepository eventRepository;

    public ArtistController(final ArtistRepository artistRepository, EventRepository eventRepository) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping(value = "/artists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Artist>>> getArtistCollection() {
        final List<EntityModel<Artist>> artists = StreamSupport.stream(artistRepository.findAll().spliterator(), false)
                .map(artist -> EntityModel.of(artist,
                        linkTo(methodOn(ArtistController.class).getSingleArtist(artist.getId())).withSelfRel(),
                        linkTo(methodOn(ArtistController.class).getArtistCollection()).withRel("get-all-artists")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(artists,
                linkTo(methodOn(ArtistController.class).getArtistCollection()).withSelfRel()));
    }

    @GetMapping(value = "/artists/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Artist>> getSingleArtist(@PathVariable long id) {
        return artistRepository.findById(id)
                .map(artist -> EntityModel.of(artist)
                        .add(
                                linkTo(methodOn(ArtistController.class).getSingleArtist(artist.getId())).withSelfRel(),
                                linkTo(methodOn(ArtistController.class).getArtistCollection()).withRel("get-all-artists"),
                                linkTo(methodOn(ArtistController.class).updateArtist(artist, artist.getId())).withRel("update-artist"),
                                linkTo(methodOn(ArtistController.class).deleteArtist(artist.getId())).withRel("delete-artist"),
                                linkTo(methodOn(ArtistController.class).getEventCollectionOfArtist(artist.getId())).withRel("get-event-collection-of-artist")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/artists", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createArtist(@RequestBody @Valid final Artist newArtist) {
        final Artist createdArtist = artistRepository.save(newArtist);
        final EntityModel<Artist> createdArtistResource = EntityModel.of(createdArtist,
                linkTo(methodOn(ArtistController.class).getSingleArtist(createdArtist.getId())).withSelfRel());

        return ResponseEntity.created(
                        createdArtistResource.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(createdArtist);
    }

    @PutMapping(value = "/artists/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Artist>> updateArtist(@RequestBody @Valid final Artist newArtist, @PathVariable Long id) {
        if (newArtist.getId() != id) {
            return ResponseEntity.badRequest().build();
        }
        final Artist updatedArtist = artistRepository.findById(id)
                .map(artist -> {
                    artist.setName(newArtist.getName());
                    return artistRepository.save(artist);
                })
                .orElseGet(() -> {
                    newArtist.setId(id);
                    return artistRepository.save(newArtist);
                });
        final EntityModel<Artist> updatedArtistResource = EntityModel.of(updatedArtist,
                linkTo(methodOn(ArtistController.class).getSingleArtist(updatedArtist.getId())).withSelfRel());

        return ResponseEntity.created(
                        linkTo(methodOn(ArtistController.class).getSingleArtist(updatedArtist.getId())).toUri())
                .body(updatedArtistResource);
    }

    @DeleteMapping(value = "/artists/{id}")
    public ResponseEntity<Artist> deleteArtist(@PathVariable final Long id) {
        try {
            artistRepository.deleteById(id);
            return ResponseEntity.noContent()
                    .header("Access-Control-Expose-Headers", "Link")
                    .header("Link", linkTo(methodOn(ArtistController.class).getArtistCollection()).withRel("get-artist-collection").toString())
                    .build();

        } catch (final EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound()
                    .header("Access-Control-Expose-Headers", "Link")
                    .header("Link", linkTo(methodOn(ArtistController.class).getArtistCollection()).withRel("get-artist-collection").toString())
                    .build();
        }
    }

    @GetMapping(value = "/artists/{artistId}/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Event>>> getEventCollectionOfArtist(@PathVariable final long artistId) {
        final Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        final List<EntityModel<Event>> eventsOfArtist = artist.get()
                .getEvents()
                .stream()
                .map(event -> EntityModel.of(event)
                        .add(linkTo(methodOn(ArtistController.class).getSingleEventOfArtist(artist.get().getId(), event.getId())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(eventsOfArtist)
                .add(linkTo(methodOn(ArtistController.class).getArtistCollection()).withRel("get-artist-collection")));
    }

    @GetMapping(value = "/artists/{artistId}/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Event>> getSingleEventOfArtist(@PathVariable final long artistId, @PathVariable final long eventId) {
        final Optional<Artist> artist = artistRepository.findById(artistId);
        if (artist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        final Optional<Event> eventOfArtist = artist.get()
                .getEvents()
                .stream()
                .filter(event -> event.getId() == eventId)
                .findFirst();

        if (eventOfArtist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityModel.of(eventOfArtist.get())
                .add(linkTo(methodOn(ArtistController.class).unlinkEventFromArtist(artist.get().getId(), eventOfArtist.get().getId())).withRel("unlink-event-from-artist"),
                        linkTo(methodOn(ArtistController.class).getEventCollectionOfArtist(artist.get().getId())).withRel("get-event-collection-of-artist")));
    }

    @PutMapping(value = "/artists/{artistId}/events/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<EventDTO>>> linkEventToArtist(@PathVariable Long artistId, @PathVariable Long eventId) {
        final Optional<Event> newEvent = eventRepository.findById(eventId);
        final Optional<Artist> updatedArtist = artistRepository.findById(artistId)
                .map(artist -> {
                    if (newEvent.isPresent()) {
                        artist.addEvent(newEvent.get());
                    }
                    return artistRepository.save(artist);
                });

        if (updatedArtist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final List<EntityModel<EventDTO>> eventDTOs = updatedArtist.get().getEventDTOs().stream()
                .map(event -> EntityModel.of(event,
                        linkTo(methodOn(EventController.class).getSingleEvent(event.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.created(
                        linkTo(methodOn(ArtistController.class).getSingleEventOfArtist(updatedArtist.get().getId(), eventId)).toUri())
                .body(CollectionModel.of(eventDTOs));
    }

    @DeleteMapping(value = "/artists/{artistId}/events/{eventId}")
    public ResponseEntity<Event> unlinkEventFromArtist(@PathVariable final Long artistId, @PathVariable final Long eventId) {
        final Optional<Artist> updatedArtist = artistRepository.findById(artistId)
                .map(artist -> {
                    for (final Event event : artist.getEvents()) {
                        if (event.getId() == eventId) {
                            artist.removeEvent(event);
                            break;
                        }
                    }
                    return artistRepository.save(artist);
                });

        if (updatedArtist.isEmpty()) {
            return ResponseEntity.notFound()
                    .header("Access-Control-Expose-Headers", "Link")
                    .header("Link", linkTo(methodOn(ArtistController.class).getArtistCollection()).withRel("get-event-collection-of-artist").toString())
                    .build();
        }
        return ResponseEntity.noContent()
                    .header("Access-Control-Expose-Headers", "Link")
                    .header("Link", linkTo(methodOn(ArtistController.class).getArtistCollection()).withRel("get-event-collection-of-artist").toString())
                    .build();
    }

}
