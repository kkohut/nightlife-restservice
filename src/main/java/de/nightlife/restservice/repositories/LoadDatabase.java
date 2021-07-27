package de.nightlife.restservice.repositories;

import de.nightlife.restservice.models.Artist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ArtistRepository artistRepository) {
        return args -> {
            log.info("Preloading " + artistRepository.save(new Artist("The Weeknd")));
            log.info("Preloading " + artistRepository.save(new Artist("Taylor Swift")));
            log.info("Preloading " + artistRepository.save(new Artist("Post Malone")));
        };
    }
}
