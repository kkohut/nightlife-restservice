package de.nightlife.restservice.controllers.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.nightlife.restservice.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteEventTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    EventRepository eventRepository;

    @Test
    public void delete_event_existing_returns_nothing_204() throws Exception {
        final Long id = 3L;

        Mockito.doNothing().when(eventRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/events/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/events/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.name").doesNotExist());
    }

    @Test
    public void delete_event_nonexisting_returns_nothing_404() throws Exception {
        final Long id = Long.MAX_VALUE;

        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(eventRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/events/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
