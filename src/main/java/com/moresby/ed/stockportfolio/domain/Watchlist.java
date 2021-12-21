package com.moresby.ed.stockportfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.moresby.ed.stockportfolio.domain.TStock;
import com.moresby.ed.stockportfolio.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "Watchlist")
@Table(name = "watch_list")
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(value = {"user"})
public class Watchlist {
    @Id
    @SequenceGenerator(
            name = "watchlist_sequence",
            sequenceName = "watchlist_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "watchlist_sequence"
    )
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "last_update_at",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private LocalDateTime lastUpdateAt;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "watchlist_user_fk")
    )
    private User user;

    @ManyToMany
    @JoinTable(
            name = "watchlist_tstock",
            joinColumns = @JoinColumn(
                    name = "watchlist_id",
                    foreignKey = @ForeignKey(name = "watchlist_tstock_fk")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tstock_id",
                    foreignKey = @ForeignKey(name = "tstock_watchlist_fk")
            )
    )
    private List<TStock> tStocks;

    @Builder
    public Watchlist(String name, LocalDateTime lastUpdateAt, User user, List<TStock> tStocks) {
        this.name = name;
        this.lastUpdateAt = lastUpdateAt;
        this.user = user;
        this.tStocks = tStocks;
    }
}
