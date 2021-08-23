package de.nightlife.restservice.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_sequence")
    @Column(name = "event_id", nullable = false)
    private long id;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ElementCollection
    @Column(name = "image_urls")
    private Set<String> imageUrls;

    @Column(name = "venue_name")
    private String venueName;

    @Column(name = "city")
    private String city;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(name = "artists")
    @JoinTable(                             // TODO überarbeiten!
            name = "events_participating",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> artists;

    public Event() {
    }

    public Event(final String name, final LocalDate startDate, final LocalDate endDate, final Set<String> imageUrls,
                 final String venueName, final String city) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUrls = imageUrls;
        this.venueName = venueName;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(final Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(final String venueName) {
        this.venueName = venueName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(final Set<Artist> artists) {
        this.artists = artists;
    }

    public void addArtist(final Artist artist) {
        this.artists.add(artist);
        artist.getEvents().add(this);
    }

    public void removeArtist (final Artist artist) {
        this.artists.remove(artist);
        artist.getEvents().remove(this);
    }
}
