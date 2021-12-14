package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "Classify")
@Table(name = "classify")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(value = {"tstocks"})
public class Classify {
    @Id
    @SequenceGenerator(
            name = "classify_sequence",
            sequenceName = "classify_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "classify_sequence"
    )
    @Column(name = "classify_id")
    private Integer classifyId;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @OneToMany(mappedBy = "classify")
    private List<TStock> tStocks = new ArrayList<>();
}
