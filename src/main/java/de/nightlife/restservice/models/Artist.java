package de.nightlife.restservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_sequence")
    @Column(name = "artist_id", nullable = false)
    private long id;

    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Event> events;

    @JsonProperty("events")
    @Transient
    private Set<EventDTO> eventDTOs;

    public Artist() {
    }

    public Artist(final String name) {
        this.name = name;
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

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<EventDTO> getEventDTOs() {
        this.eventDTOs= new HashSet<>();
        for (final Event event: this.events) {
            this.eventDTOs.add(new EventDTO(event));
        }
        return eventDTOs;
    }

    public void setEventDTOs(final Set<EventDTO> eventDTOs) {
        this.eventDTOs = eventDTOs;
    }

    public void addEvent(final Event event) {
        this.events.add(event);
        event.getArtists().add(this);
    }

    public void removeEvent(final Event event) {
        this.events.remove(event);
        event.getArtists().remove(this);

    }
}
