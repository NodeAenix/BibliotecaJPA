package model;

import entity.Prestamo;

import java.util.List;

public class PrestamoDAO extends GenericDAO<Prestamo, Integer> {

    public PrestamoDAO() {
        super(Prestamo.class);
    }

    public List<Prestamo> getUserPrestamos(int userId) {
        String query = "FROM Prestamo p WHERE p.usuario.id=:userId";
        return etManager.createQuery(query, Prestamo.class)
                        .setParameter("userId", userId)
                        .getResultList();
    }
}
