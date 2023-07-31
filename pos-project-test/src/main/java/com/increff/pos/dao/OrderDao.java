package com.increff.pos.dao;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao extends  AbstractDao{
    private static String select_id = "select p from OrderPojo p where id=:id";
    private static String select_all = "select p from OrderPojo p";
    private static String select_date= "select p from OrderPojo p where date_time>=:start_date and date_time<=:end_date";

    @PersistenceContext
    private EntityManager em;
    //CREATE
    @Transactional
    public OrderPojo insert(OrderPojo pojo){
        em.persist(pojo);
        return pojo;
    }
    //READ
    public OrderPojo select(int id){
        TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }
    // Gets list of orders created in given timeframe
    public List<OrderPojo> getByDate(LocalDateTime start_date, LocalDateTime end_date){
        TypedQuery<OrderPojo> query = getQuery(select_date, OrderPojo.class);
        query.setParameter("start_date",start_date);
        query.setParameter("end_date",end_date);
        return query.getResultList();
    }

    public List<OrderPojo> selectAll(){
        TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
        return query.getResultList();
    }



}
