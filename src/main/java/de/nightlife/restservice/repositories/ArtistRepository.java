package de.nightlife.restservice.repositories;

import de.nightlife.restservice.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long>
{
}
