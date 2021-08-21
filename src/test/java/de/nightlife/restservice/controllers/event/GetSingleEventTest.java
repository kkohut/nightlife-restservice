package de.nightlife.restservice.controllers.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.nightlife.restservice.models.Event;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetSingleEventTest {
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
    public void get_singleArtist_id_1_returns_singleEvent_200() throws Exception {
        final long id = 1L;
        Mockito.when(eventRepository.findById(id)).thenReturn(Optional.of(event1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(event1.getName()));
    }

    @Test
    public void get_singleArtist_id_2_returns_singleArtist_200() throws Exception {
        final long id = 2L;
        Mockito.when(eventRepository.findById(id)).thenReturn(Optional.of(event2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(event2.getName()));
    }

    @Test
    public void get_singleArtist_id_nonExistent_returns_nothing_404() throws Exception {
        final long id = Long.MAX_VALUE;
        Mockito.when(eventRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/events/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.name").doesNotExist());

    }
}
