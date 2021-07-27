package de.nightlife.restservice.controllers;

import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.repositories.ArtistRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ArtistController {

    private final ArtistRepository artistRepository;
    private final ArtistModelAssembler assembler;

    public ArtistController(ArtistRepository artistRepository, ArtistModelAssembler assembler) {
        this.artistRepository = artistRepository;
        this.assembler = assembler;
    }

    @GetMapping("/artists")
    CollectionModel<EntityModel<Artist>> getAll() {
        final List<EntityModel<Artist>> artists = artistRepository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(artists,
                linkTo(methodOn(ArtistController.class).getAll()).withSelfRel());
    }

    @GetMapping("/artists/{id}")
    EntityModel<Artist> getSingle(@PathVariable final Long id) {
        final Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException(id));

        return assembler.toModel(artist);
    }
}
