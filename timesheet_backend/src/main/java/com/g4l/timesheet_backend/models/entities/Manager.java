package com.g4l.timesheet_backend.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "managers")
public class Manager extends User {
    @Id
    @Column(name = "manager_id")
    private String id;

    public Manager(String idNumber, String firstName, String lastName, String userName, String email,
            String phoneNumber, String id) {
        super(idNumber, firstName, lastName, userName, email, phoneNumber);
        this.id = id;
    }
}