package de.nightlife.restservice.repositories;

import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.models.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(final ArtistRepository artistRepository, final EventRepository eventRepository) {
        return args -> {
            log.info("Preloading " + artistRepository.save(new Artist("The Weeknd")));
            log.info("Preloading " + artistRepository.save(new Artist("Taylor Swift")));
            log.info("Preloading " + artistRepository.save(new Artist("Post Malone")));

            log.info("Preloading " + eventRepository.save(new Event("TestEvent1", null, null,
                    null, "TestVenue1", "City1")));
            log.info("Preloading " + eventRepository.save(new Event("TestEvent2", null, null,
                    null, "TestVenue2", "City2")));
            log.info("Preloading " + eventRepository.save(new Event("TestEvent3", null, null,
                    null, "TestVenue3", "City3")));
        };
    }
}
