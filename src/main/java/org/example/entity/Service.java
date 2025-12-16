package org.example.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private int id;

    @JsonProperty("description")
    @Column(unique = true)
    private String name;

    @JsonProperty("price")
    @Column(name = "monthly_fee")
    private double monthlyFee;

    @ManyToMany(
            mappedBy = "services",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private Set<Subscriber> subscribers = new HashSet<>();

    // --- Конструкторы ---

    public Service() {
    }

    public Service(String name, double monthlyFee) {
        this.name = name;
        this.monthlyFee = monthlyFee;
    }

    // --- Геттеры и Сеттеры ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public Set<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    // --- equals, hashCode ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return id == service.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}