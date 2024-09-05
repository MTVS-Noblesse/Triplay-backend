package com.noblesse.backend.preference.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="preference")
@Getter @Setter
@Embeddable
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="preference_id")
    private Long preferenceId;

    @Column(name="preference_name")
    private String preferenceName;

    public Preference() {}

    public Preference(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    @Override
    public String toString() {
        return "Preference{" +
                "preferenceId=" + preferenceId +
                ", preferenceName='" + preferenceName + '\'' +
                '}';
    }
}
