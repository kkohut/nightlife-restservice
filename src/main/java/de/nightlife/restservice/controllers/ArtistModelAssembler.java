package de.nightlife.restservice.controllers;

import de.nightlife.restservice.models.Artist;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ArtistModelAssembler implements SimpleRepresentationModelAssembler<Artist> {

	@Override
	public EntityModel<Artist> toModel(final Artist artist) {
	    final EntityModel<Artist> artistModel = EntityModel.of(
	    		artist,
				linkTo(methodOn(ArtistController.class).getSingle(artist.getId())).withSelfRel(),
				linkTo(methodOn(ArtistController.class).getAll()).withRel("artists")
		);

	    return artistModel;
	}

	@Override
	public void addLinks(final EntityModel<Artist> resource) {
		final Long artistId = resource.getContent().getId();
		resource.add(linkTo(methodOn(ArtistController.class).getSingle(artistId)).withSelfRel());
	}

	@Override
	public void addLinks(final CollectionModel<EntityModel<Artist>> resources) {
		resources.add(linkTo(methodOn(ArtistController.class).getAll()).withSelfRel());
	}
}
