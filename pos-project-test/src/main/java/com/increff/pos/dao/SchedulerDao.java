package com.increff.pos.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.pos.pojo.SchedulerPojo;
import org.springframework.stereotype.Repository;

@Repository
public class SchedulerDao extends AbstractDao{
    private static String select_all = "select p from SchedulerPojo p";
    private static String select_by_date = "select p from SchedulerPojo p where date>=:start_date and date<=:end_date";

    @PersistenceContext
    private EntityManager em;
    //CREATE
    @Transactional
    public void insert(SchedulerPojo pojo){em.persist(pojo);}
    //READ
    public List<SchedulerPojo> selectAll(){
        TypedQuery<SchedulerPojo> query = getQuery(select_all, SchedulerPojo.class);
        return query.getResultList();
    }
    // Gets Day-on-day sales report for a given timeframe
    public List<SchedulerPojo> selectByDate(LocalDate start_date, LocalDate end_date){
        TypedQuery<SchedulerPojo> query = getQuery(select_by_date, SchedulerPojo.class);
        query.setParameter("start_date", start_date);
        query.setParameter("end_date", end_date);
        return query.getResultList();
    }
}
