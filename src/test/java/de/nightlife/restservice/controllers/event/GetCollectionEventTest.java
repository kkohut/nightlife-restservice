package de.nightlife.restservice.controllers.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.models.Event;
import de.nightlife.restservice.repositories.ArtistRepository;
import de.nightlife.restservice.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetCollectionEventTest {

    final Event event1 = new Event("TestEvent1", null, null, null, "TestVenue1", "City1");
    final Event event2 = new Event("TestEvent2", null, null, null, "TestVenue2", "City2");
    final Event event3 = new Event("TestEvent3", null, null, null, "TestVenue3", "City3");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    EventRepository eventRepository;

    @Test
    public void get_eventsCollection_returns_ListOfEvents() throws Exception {
        final List<Event> eventList = Arrays.asList(event1, event2, event3);

        Mockito.when(eventRepository.findAll()).thenReturn(eventList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.eventList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.eventList[2].name").value(event3.getName()));
    }
}
