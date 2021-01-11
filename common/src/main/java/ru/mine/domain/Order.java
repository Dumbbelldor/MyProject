package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "order_info")
public class Order implements Serializable {

    private static final long serialVersionUID = 123L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "customer_id")
    @JoinColumn(table = "m_users", name = "id",
    foreignKey = @ForeignKey(name = "m_customer_orders_m_users_id_fk"))
    private Integer customerId;

    @Column(name = "order_line")
    private String orderLine;

    @Column(name = "creation_time")
    private Timestamp creationTime;

    @Column(name = "courier_id")
    @JoinColumn(name = "id", table = "m_drivers")
    private Integer courierId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "delivery_cost")
    private int deliveryCost;
}
