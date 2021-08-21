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
public class UpdateEventTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    EventRepository eventRepository;

    @Test
    public void update_event_valid_returns_updatedEvent_201() throws Exception {
        final long id = 3L;
        final Event updatedEvent = new Event("UpdatedEventName", null, null, null,
                "TestVenue1", "City1");
        updatedEvent.setId(id);
        final String updatedEventJson = mapper.writeValueAsString(updatedEvent);

        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/events/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEventJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(updatedEvent.getName()));
    }

    @Test
    public void update_event_valid_nonexistent_returns_updatedEvent_201() throws Exception {
        final long id = Long.MAX_VALUE;
        final Event updatedEvent = new Event("UpdatedEventName", null, null, null,
                "TestVenue1", "City1");
        updatedEvent.setId(id);
        final String updatedEventJson = mapper.writeValueAsString(updatedEvent);

        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/events/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEventJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(updatedEvent.getName()));
    }

    @Test
    public void update_event_invalid_emptyName_returns_nothing_400() throws Exception {
        final long id = 3L;
        final Event updatedEvent = new Event("", null, null, null,
                "TestVenue1", "City1");
        updatedEvent.setId(id);
        final String updatedEventJson = mapper.writeValueAsString(updatedEvent);

        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/events/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEventJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").doesNotHaveJsonPath());
    }

    @Test
    public void update_event_invalid_differentIds_returns_nothing_400() throws Exception {
        final long id = 3L;
        final Event updatedEvent = new Event("UpdatedEventName", null, null, null,
                "TestVenue1", "City1");
        updatedEvent.setId(4L);
        final String updatedEventJson = mapper.writeValueAsString(updatedEvent);

        Mockito.when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/events/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEventJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").doesNotHaveJsonPath());
    }
}
