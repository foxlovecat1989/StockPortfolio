package com.moresby.ed.stockportfolio.watch;

import com.moresby.ed.stockportfolio.watchlist.Watchlist;
import com.moresby.ed.stockportfolio.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "Watch")
@Table(name = "watch")
@NoArgsConstructor
@Getter
@Setter
public class Watch {
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "watch_user_fk")
    )
    private User user;

    @OneToMany(
            mappedBy = "watch",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<Watchlist> watchlists;
}
