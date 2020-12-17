package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "m_customer_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "order_line")
    private String orderLine;

    @Column(name = "order_time")
    private Timestamp orderTime;

    @Column(name = "courier_id")
    private Integer courierId;

    @Column(name = "is_delivered")
    private boolean isDelivered;

    @Column(name = "total_price")
    private Integer totalPrice;

}
