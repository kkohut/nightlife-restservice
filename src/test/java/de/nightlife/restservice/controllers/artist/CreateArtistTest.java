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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistController.class)
public class CreateArtistTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ArtistRepository artistRepository;

    @Test
    public void create_artist_existing_returns_newArtist_201() throws Exception {
        final Artist newArtist = new Artist("TestArtist");
        final String newArtistJson = mapper.writeValueAsString(newArtist);
        
        Mockito.when(artistRepository.save(any(Artist.class))).thenReturn(newArtist);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newArtistJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TestArtist"));
    }
}
