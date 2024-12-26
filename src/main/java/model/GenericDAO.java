package model;

import util.EntityTools;

import java.util.List;

public class GenericDAO<T, ID> implements EntityTools {

    private final Class<T> objectClass;

    public GenericDAO(Class<T> classType) {
        this.objectClass = classType;
    }

    public T getById(ID id) {
        return etManager.find(objectClass, id);
    }

    public List<T> getAll() {
        return etManager.createQuery("FROM " + objectClass.getName(), objectClass).getResultList();
    }

    public void add(T t) {
        etTransaction.begin();
        etManager.persist(t);
        etTransaction.commit();
    }

    public void update(T t) {
        etTransaction.begin();
        etManager.merge(t);
        etTransaction.commit();
    }

    public void delete(T t) {
        etTransaction.begin();
        etManager.remove(t);
        etTransaction.commit();
    }
}
