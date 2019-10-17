package project3.ginp14.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @NotBlank(message = "This field cannot be blank")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "This field cannot be blank")
    private String description;

    @Column(name = "average_price_min")
    @NotBlank(message = "This field cannot be blank")
    private long averagePriceMin;

    @Column(name = "average_price_max")
    @NotBlank(message = "This field cannot be blank")
    private long averagePriceMax;

    @Column(name = "images")
    private String images;

    @OneToOne
    @JoinColumn(name="restaurant_type_id")
    private RestaurantType restaurantType;
}
