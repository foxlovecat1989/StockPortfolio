package com.moresby.ed.stockportfolio.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moresby.ed.stockportfolio.model.entities.TStock;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "Classify")
@Table(name = "classify")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonIgnoreProperties({"tStocks"})
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

    @Column(name = "name")
    private String name;

    @OneToMany(
            mappedBy = "classify",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<TStock> tStocks = new ArrayList<>();

    public Integer getClassifyId() {
        return classifyId;
    }

    public String getName() {
        return name;
    }

    public List<TStock> gettStocks() {
        return tStocks;
    }
}
