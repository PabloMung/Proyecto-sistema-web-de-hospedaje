
package accesodatos;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import modelo.Rol;


@Stateless
public class RolFacade extends AbstractFacade<Rol> {

    @PersistenceContext(unitName = "cabanas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }
    
    // Buscar un rol por nombre
    public Rol findByNombre(String nombre) {
        TypedQuery<Rol> query = em.createNamedQuery("Rol.findByNombre", Rol.class);
        query.setParameter("nombre", nombre);
        return query.getResultStream().findFirst().orElse(null);
    }
}
