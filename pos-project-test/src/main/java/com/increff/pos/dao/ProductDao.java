package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
@Repository
public class ProductDao extends AbstractDao{
    private static String select_id = "select p from ProductPojo p where id=:id";
    private static String select_all = "select p from ProductPojo p";
    private static String duplicate_check = "select p from ProductPojo p where barcode=:barcode";
    private static String get_by_brand = "select p from ProductPojo p where brand_category=:brand_category";
    @PersistenceContext
    private EntityManager em;
    //CREATE
    @Transactional
    public void insert(ProductPojo pojo){em.persist(pojo);}
    //READ
    public ProductPojo select(int id){
        TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }
    public List<ProductPojo> selectAll(){
        TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
        return query.getResultList();
    }
    // Gets ProductPojo for a given barcode
    public ProductPojo checkBarcode(String barcode){
        TypedQuery<ProductPojo> query = getQuery(duplicate_check, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }
    // Gets all products corresponding to a given brand-category combination
    public List<ProductPojo> getByBrand(int brand_category){
        TypedQuery<ProductPojo> query = getQuery(get_by_brand, ProductPojo.class);
        query.setParameter("brand_category", brand_category);
        return query.getResultList();
    }
}
