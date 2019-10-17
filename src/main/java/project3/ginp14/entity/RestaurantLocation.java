package project3.ginp14.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="restaurant_location")
public class RestaurantLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "address")
    @NotBlank(message = "This field cannot be blank")
    private String address;

    @Column(name = "telephone")
    @NotBlank(message = "This field cannot be blank")
    private String telephone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;
}
