
package accesodatos;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import modelo.Actividad;


@Stateless
public class ActividadFacade extends AbstractFacade<Actividad> {

    @PersistenceContext(unitName = "cabanas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActividadFacade() {
        super(Actividad.class);
    }
    
    // Obtener actividades con precio menor o igual al especificado para adultos
    public List<Actividad> findByMaxAdultPrice(int maxPrice) {
        TypedQuery<Actividad> query = em.createQuery("SELECT a FROM Actividad a WHERE a.precioAdulto <= :maxPrice", Actividad.class);
        query.setParameter("maxPrice", maxPrice);
        return query.getResultList();
    }

    // Obtener actividades con precio menor o igual al especificado para niños
    public List<Actividad> findByMaxChildPrice(int maxPrice) {
        TypedQuery<Actividad> query = em.createQuery("SELECT a FROM Actividad a WHERE a.precioNino <= :maxPrice", Actividad.class);
        query.setParameter("maxPrice", maxPrice);
        return query.getResultList();
    }
    @Override
    public void edit(Actividad entity) {
    getEntityManager().merge(entity);
}

}
