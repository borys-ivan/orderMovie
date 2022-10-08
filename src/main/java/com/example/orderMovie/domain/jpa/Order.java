package com.example.orderMovie.domain.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderID;

    @Column(name = "price", columnDefinition="Decimal(6,2)")
    private BigDecimal price;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_age")
    private Integer customerAge;

    @Column(name = "additional")
    private String additional;

    @Column(name = "date_session")
    private LocalDateTime dateSession;

    @Column(name = "percent_discount")
    private Integer percentDiscount;

    @Column(name = "is_birthday")
    private Boolean isBirthday;

    @Column(name = "created")
    private Date created;

    ///////////////////////////////////////////
    // RELATIONS
    ///////////////////////////////////////////

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;

    @Column(name = "movie_id", insertable=false, updatable=false)
    private Long movieID;
}
