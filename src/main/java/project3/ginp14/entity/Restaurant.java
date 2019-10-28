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

    @Column(name="telephone")
    private String telephone;

    @Column(name="address")
    private String address;

    @OneToOne
    @JoinColumn(name="restaurant_type_id")
    private RestaurantType restaurantType;

    public Restaurant() {
    }

    public Restaurant(@NotBlank(message = "This field cannot be blank") String name, @NotBlank(message = "This field cannot be blank") String description, @NotBlank(message = "This field cannot be blank") long averagePriceMin, @NotBlank(message = "This field cannot be blank") long averagePriceMax, String images, String telephone, String address, RestaurantType restaurantType) {
        this.name = name;
        this.description = description;
        this.averagePriceMin = averagePriceMin;
        this.averagePriceMax = averagePriceMax;
        this.images = images;
        this.telephone = telephone;
        this.address = address;
        this.restaurantType = restaurantType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAveragePriceMin() {
        return averagePriceMin;
    }

    public void setAveragePriceMin(long averagePriceMin) {
        this.averagePriceMin = averagePriceMin;
    }

    public long getAveragePriceMax() {
        return averagePriceMax;
    }

    public void setAveragePriceMax(long averagePriceMax) {
        this.averagePriceMax = averagePriceMax;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(RestaurantType restaurantType) {
        this.restaurantType = restaurantType;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
