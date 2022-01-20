package com.roman.entity;

import com.roman.component.DepartmentRevisionListener;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "REVINFO")
@RevisionEntity(DepartmentRevisionListener.class)
@Data
public class DepartmentRevision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "REV")
    private int id;

    @RevisionTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "action_completed_on")
    private Date actionCompletedAt;

    @Column(name = "user_name")
    private String userName;

}
