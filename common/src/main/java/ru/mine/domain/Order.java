package ru.mine.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "order_info")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_id")
    @JoinColumn(table = "m_users", name = "id",
    foreignKey = @ForeignKey(name = "m_customer_orders_m_users_id_fk"))
    private Integer customerId;

    @Column(name = "order_id")
    private Integer orderLine;

    @Column(name = "order_time")
    private Timestamp orderTime;

    @Column(name = "courier_id")
    @JoinColumn(name = "id", table = "m_drivers")
    private Integer courierId;

    @Column(name = "delivered")
    private boolean delivered;

}
