package com.g4l.timesheet_backend.models.entities;

import com.g4l.timesheet_backend.models.enums.LogbookStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "logbooks")
public class Logbook {
    @Id
    @Column(name = "logbook_id")
    private String logbookId;
    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private Consultant consultant;
    // @Column(name = "consultant_id")
    // private String consultantId;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;
    // @Column(name = "manager_id")
    // private String managerId;
    @Column(name = "week_number")
    private int weekNumber;
    @Column
    private double monday;
    @Column
    private double tuesday;
    @Column
    private double wednesday;
    @Column
    private double thursday;
    @Column
    private double friday;
    @Column
    private double saturday;
    @Column
    private double sunday;
    @Column(name = "total_hours")
    private double totalHours;
    @Enumerated(EnumType.STRING)
    @Column
    private LogbookStatus status;
    @Column(name = "status_description")
    private String statusDescription;
}
