package model;

import entity.Usuario;

import java.util.List;

public class UsuarioDAO extends GenericDAO<Usuario, Integer> {

    public UsuarioDAO() {
        super(Usuario.class);
    }

    public Usuario getUserByEmailAndPassword(String email, String password) {
        String query = "FROM Usuario u WHERE u.email=:email AND u.password=:password";
        List<Usuario> users = etManager.createQuery(query, Usuario.class)
                                       .setParameter("email", email)
                                       .setParameter("password", password)
                                       .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }
}
