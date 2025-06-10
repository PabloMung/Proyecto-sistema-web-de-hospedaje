
package accesodatos;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import modelo.Usuario;

@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "cabanas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    // Buscar usuario por correo
    public Usuario findByEmail(String email) {
        TypedQuery<Usuario> query = em.createNamedQuery("Usuario.findByEmail", Usuario.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst().orElse(null);
    }

    // Obtener todos los usuarios de un rol específico
    public List<Usuario> findByRol(Integer rolId) {
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.rolId.id = :rolId", Usuario.class);
        query.setParameter("rolId", rolId);
        return query.getResultList();
    }
    public List<Usuario> findAllUsuarios() {
    List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u JOIN FETCH u.rolId", Usuario.class).getResultList();
    for (Usuario u : usuarios) {
        System.out.println("Usuario: " + u.getNombre() + ", Rol: " + (u.getRolId() != null ? u.getRolId().getNombre() : "Sin Rol"));
    }
    return usuarios;
}

}
