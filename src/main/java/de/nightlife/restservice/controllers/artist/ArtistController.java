package de.nightlife.restservice.controllers.artist;

import de.nightlife.restservice.controllers.artist.Exceptions.ArtistInvalidException;
import de.nightlife.restservice.controllers.artist.Exceptions.ArtistNotFoundException;
import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/artists")
public class ArtistController {

    @Autowired final ArtistRepository artistRepository;
    @Autowired final ArtistModelAssembler assembler;

    public ArtistController(ArtistRepository artistRepository, ArtistModelAssembler assembler) {
        this.artistRepository = artistRepository;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    CollectionModel<EntityModel<Artist>> getArtistCollection() {
        final List<EntityModel<Artist>> artists = artistRepository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(artists,
                linkTo(methodOn(ArtistController.class).getArtistCollection()).withSelfRel());
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE) //TODO add Hyperlinks
    EntityModel<Artist> getSingleArtist(@PathVariable final Long id) {
        final Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException(id));

        return assembler.toModel(artist);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)   //TODO add Hyperlinks
    ResponseEntity<Artist> createArtist(@RequestBody @Valid final Artist newArtist) {
        final Artist createdArtist = artistRepository.save(newArtist);
        if (createdArtist == null) {
            throw new ArtistInvalidException();
        } else {
            return new ResponseEntity<>(createdArtist, HttpStatus.CREATED);
        }
    }

    @DeleteMapping(value = "{id}")
    ResponseEntity<Artist> deleteArtist(@PathVariable final Long id) { //TODO add Hyperlinks
        try {
            artistRepository.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (final EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
