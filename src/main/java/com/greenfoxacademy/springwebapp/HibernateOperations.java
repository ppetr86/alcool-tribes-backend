/*
package com.greenfoxacademy.springwebapp;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

*/
/**
 *
 *Class to illustrate the usage of EntityManager API.
 *//*

public class HibernateOperations {

    private static final EntityManagerFactory emf;

    */
/**
     * Static block for creating EntityManagerFactory. The Persistence class looks for META-INF/persistence.xml in the classpath.
     *//*

    static {
        emf = Persistence.createEntityManagerFactory("com.greenfoxacademy.springwebapp");
    }

    */
/**
     * Static method returning EntityManager.
     * @return EntityManager
     *//*

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    */
/**
     * Saves the LocationEntity entity into the database. Here we are using Application Managed EntityManager, hence should handle transactions by ourselves.
     *//*

    public void saveLocationEntity() {
        EntityManager em = HibernateOperations.getEntityManager();
        em.getTransaction()
                .begin();
        LocationEntity loc = new LocationEntity();
        loc.setKingdom(null);
        loc.setType(LocationType.EMPTY);
        loc.setId(111L);
        loc.setX(99);
        loc.setY(99);
        em.persist(loc);
        em.getTransaction()
                .commit();
    }

    */
/**
     * Method to illustrate the querying support in EntityManager when the result is a single object.
     * @return LocationEntity
     *//*

    public LocationEntity queryForLocationEntityById() {
        EntityManager em = HibernateOperations.getEntityManager();
        LocationEntity loc = (LocationEntity) em.createQuery("SELECT loc from LocationEntity loc where loc.id = ?1")
                .setParameter(1, 1L)
                .getSingleResult();
        return loc;
    }

    */
/**
     * Method to illustrate the querying support in EntityManager when the result is a list.
     * @return
     *//*

    public List<?> queryForLocationEntities() {
        EntityManager em = HibernateOperations.getEntityManager();
        List<?> locs = em.createQuery("SELECT loc from LocationEntity loc where loc.id = ?1")
                .setParameter(1, "English")
                .getResultList();
        return locs;
    }

    */
/**
     * Method to illustrate the usage of find() method.
     * @param locId
     * @return LocationEntity
     *//*

    public LocationEntity getLocationEntity(Long locId) {
        EntityManager em = HibernateOperations.getEntityManager();
        LocationEntity loc = em.find(LocationEntity.class, locId);
        return loc;
    }

    */
/**
     * Method to illustrate the usage of merge() function.
     *//*

    public void mergeLocationEntity() {
        EntityManager em = HibernateOperations.getEntityManager();
        LocationEntity loc = getLocationEntity(1L);
        em.detach(loc);
        loc.setType(LocationType.DESERT);
        em.getTransaction()
                .begin();
        em.merge(loc);
        em.getTransaction()
                .commit();
    }

    */
/**
     * Method to illustrate the usage of remove() function.
     *//*

    public void removeLocationEntity() {
        EntityManager em = HibernateOperations.getEntityManager();
        em.getTransaction()
                .begin();
        LocationEntity loc = em.find(LocationEntity.class, 1L);
        em.remove(loc);
        em.getTransaction()
                .commit();
    }

}
*/
