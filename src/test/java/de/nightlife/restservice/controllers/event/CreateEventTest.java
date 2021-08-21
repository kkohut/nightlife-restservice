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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateEventTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    EventRepository eventRepository;

    @Test
    public void create_event_valid_returns_newEvent_201() throws Exception {
        final Event newEvent = new Event("TestEvent1", null, null, null,
                "TestVenue1", "City1");
        final String newEventJson = mapper.writeValueAsString(newEvent);

        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(newEvent);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEventJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newEvent.getName()));
    }

    @Test
    public void create_event_invalid_emptyName_returns_nothing_400() throws Exception {
        final Event newEvent = new Event("", null, null, null,
                "TestVenue1", "City1");
        final String newEventJson = mapper.writeValueAsString(newEvent);

        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(newEvent);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEventJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").doesNotHaveJsonPath());
    }
}
