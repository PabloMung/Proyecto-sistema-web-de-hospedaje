
package accesodatos;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Permiso;

@Stateless
public class PermisoFacade extends AbstractFacade<Permiso> {

    @PersistenceContext(unitName = "cabanas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PermisoFacade() {
        super(Permiso.class);
    }
    
}
