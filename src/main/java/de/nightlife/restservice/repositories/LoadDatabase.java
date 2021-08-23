package de.nightlife.restservice.repositories;

import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.models.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(final ArtistRepository artistRepository, final EventRepository eventRepository) {
        return args -> {
            Artist artist1 = new Artist("The Weeknd");
            artist1.setEvents(new HashSet<>());
            Artist artist2 = new Artist("Taylor Swift");
            artist2.setEvents(new HashSet<>());
            Artist artist3 = new Artist("Post Malone");
            artist3.setEvents(new HashSet<>());

            log.info("Preloading " + artistRepository.save(artist1));
            log.info("Preloading " + artistRepository.save(artist2));
            log.info("Preloading " + artistRepository.save(artist3));


            Event event1 = new Event("TestEvent1", null, null,
                    null, "TestVenue1", "City1");
            event1.setArtists(new HashSet<>());
            Event event2 = new Event("TestEvent2", null, null,
                    null, "TestVenue2", "City2");
            event2.setArtists(new HashSet<>());
            Event event3 = new Event("TestEvent3", null, null,
                    null, "TestVenue3", "City3");
            event3.setArtists(new HashSet<>());

            log.info("Preloading " + eventRepository.save(event1));
            log.info("Preloading " + eventRepository.save(event2));
            log.info("Preloading " + eventRepository.save(event3));
        };
    }
}
