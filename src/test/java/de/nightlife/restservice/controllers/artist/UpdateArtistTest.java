package de.nightlife.restservice.controllers.artist;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.repositories.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtistController.class)
public class UpdateArtistTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ArtistRepository artistRepository;

    @Test
    public void update_artist_valid_returns_updatedArtist_201() throws Exception {
        final long id = 3L;
        final Artist updatedArtist = new Artist("UpdatedArtist");
        updatedArtist.setId(id);
        final String updatedArtistJson = mapper.writeValueAsString(updatedArtist);

        Mockito.when(artistRepository.save(any(Artist.class))).thenReturn(updatedArtist);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedArtistJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("UpdatedArtist"));
    }

    @Test
    public void update_artist_valid_nonexistent_returns_updatedArtist_201() throws Exception {
        final long id = Long.MAX_VALUE;
        final Artist updatedArtist = new Artist("UpdatedArtist");
        updatedArtist.setId(id);
        final String updatedArtistJson = mapper.writeValueAsString(updatedArtist);

        Mockito.when(artistRepository.save(any(Artist.class))).thenReturn(updatedArtist);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedArtistJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("UpdatedArtist"));
    }

    @Test
    public void update_artist_invalid_emptyName_returns_nothing_400() throws Exception {
        final long id = 3L;
        final Artist updatedArtist = new Artist("");
        updatedArtist.setId(id);
        final String updatedArtistJson = mapper.writeValueAsString(updatedArtist);

        Mockito.when(artistRepository.save(any(Artist.class))).thenReturn(updatedArtist);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedArtistJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").doesNotHaveJsonPath());
    }

    @Test
    public void update_artist_invalid_differentIds_returns_nothing_400() throws Exception {
        final long id = 3L;
        final Artist updatedArtist = new Artist("UpdatedArtist");
        updatedArtist.setId(4L);
        final String updatedArtistJson = mapper.writeValueAsString(updatedArtist);

        Mockito.when(artistRepository.save(any(Artist.class))).thenReturn(updatedArtist);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedArtistJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").doesNotHaveJsonPath());
    }
}
