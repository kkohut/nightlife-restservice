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

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetSingleArtistTest {

    final Artist artist1 = new Artist("The Weeknd");
    final Artist artist2 = new Artist("Taylor Swift");
    final Artist artist3 = new Artist("Post Malone");
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ArtistRepository artistRepository;

    @Test
    public void get_singleArtist_id_1_returns_singleArtist_200() throws Exception {
        final long id = 1L;
        Mockito.when(artistRepository.findById(id)).thenReturn(Optional.of(artist1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("The Weeknd"));
    }

    @Test
    public void get_singleArtist_id_2_returns_singleArtist_200() throws Exception {
        final long id = 2L;
        Mockito.when(artistRepository.findById(id)).thenReturn(Optional.of(artist2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Taylor Swift"));
    }

    @Test
    public void get_singleArtist_id_nonExistent_returns_nothing_404() throws Exception {
        final long id = Long.MAX_VALUE;
        Mockito.when(artistRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artists/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.name").doesNotExist());
    }
}
