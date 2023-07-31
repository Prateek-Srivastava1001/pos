package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryDao extends AbstractDao{
    private String select_id = "select p from InventoryPojo p where id=:id";
    private String select_all = "select i from InventoryPojo i";


    @PersistenceContext
    private EntityManager em;
    //CREATE
    @Transactional
    public void insert(InventoryPojo pojo){em.persist(pojo);}
    //READ
    public InventoryPojo select(int id){
        TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }
    public List<InventoryPojo> selectAll(){
        TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
        return query.getResultList();
    }
}
