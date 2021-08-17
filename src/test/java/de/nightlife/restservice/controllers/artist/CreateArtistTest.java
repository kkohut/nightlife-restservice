package de.nightlife.restservice.controllers.artist;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.nightlife.restservice.models.Artist;
import de.nightlife.restservice.repositories.ArtistRepository;
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
public class CreateArtistTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ArtistRepository artistRepository;

    @Test
    public void create_artist_valid_returns_newArtist_201() throws Exception {
        final Artist newArtist = new Artist("TestArtist");
        final String newArtistJson = mapper.writeValueAsString(newArtist);

        Mockito.when(artistRepository.save(any(Artist.class))).thenReturn(newArtist);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newArtistJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestArtist"));
    }

    @Test
    public void create_artist_invalid_emptyName_returns_nothing_400() throws Exception {
        final Artist newArtist = new Artist("");
        final String newArtistJson = mapper.writeValueAsString(newArtist);

        Mockito.when(artistRepository.save(any(Artist.class))).thenReturn(newArtist);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newArtistJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").doesNotHaveJsonPath());
    }
}
