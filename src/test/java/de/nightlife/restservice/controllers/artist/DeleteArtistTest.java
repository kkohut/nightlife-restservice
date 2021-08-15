package de.nightlife.restservice.controllers.artist;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.repositories.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtistController.class)
public class DeleteArtistTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ArtistRepository artistRepository;

    @Test
    public void delete_artist_existing_returns_nothing_204() throws Exception {
        final Long id = 3L;

        Mockito.doNothing().when(artistRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/artists/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.name").doesNotExist());
    }

    @Test
    public void delete_artist_nonexisting_returns_nothing_404() throws Exception {
        final Long id = Long.MAX_VALUE;

        Mockito.doThrow(new EmptyResultDataAccessException(1)).when(artistRepository).deleteById(id);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
