package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public interface EntityTools {

    EntityManagerFactory etManagerFactory = Persistence.createEntityManagerFactory("default");

    EntityManager etManager = etManagerFactory.createEntityManager();

    EntityTransaction etTransaction = etManager.getTransaction();

}
