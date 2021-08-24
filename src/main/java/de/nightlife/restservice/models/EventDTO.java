package de.nightlife.restservice.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class EventDTO {

    private long id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Set<String> imageUrls;

    private String venueName;

    private String city;

    public EventDTO() {
    }

    public EventDTO(final Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.imageUrls = event.getImageUrls();
        this.venueName = event.getVenueName();
        this.city = event.getCity();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
