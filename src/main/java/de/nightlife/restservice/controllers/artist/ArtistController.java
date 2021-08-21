package de.nightlife.restservice.controllers.artist;

import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.repositories.ArtistRepository;
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
public class ArtistController {

    final ArtistRepository artistRepository;

    public ArtistController(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
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
                                linkTo(methodOn(ArtistController.class).deleteArtist(artist.getId())).withRel("delete-artist")))
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
                    .header("get-all-artists", linkTo(methodOn(ArtistController.class).getArtistCollection()).toString())
                    .build();

        } catch (final EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound()
                    .header("get-all-artists", linkTo(methodOn(ArtistController.class).getArtistCollection()).toString())
                    .build();
        }
    }
}
