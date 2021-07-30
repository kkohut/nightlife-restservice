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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistController.class)
public class GetCollectionArtistTest {

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
    public void get_collectionArtists_returns_ListOfArtists() throws Exception {
        final List<Artist> artistList = Arrays.asList(artist1, artist2, artist3);

        Mockito.when(artistRepository.findAll()).thenReturn(artistList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/artists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.artistList", hasSize(3)))
                .andExpect(jsonPath("$._embedded.artistList[2].name").value("Post Malone"));
    }
}
