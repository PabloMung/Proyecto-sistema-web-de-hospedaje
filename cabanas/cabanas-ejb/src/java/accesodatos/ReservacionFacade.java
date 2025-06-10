
package accesodatos;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import modelo.Cabana;

import modelo.Reservacion;


@Stateless
public class ReservacionFacade extends AbstractFacade<Reservacion> {

    @PersistenceContext(unitName = "cabanas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReservacionFacade() {
        super(Reservacion.class);
    }
     public List<Cabana> findCabanasDisponibles(Date fechaEntrada, Date fechaSalida) {
    TypedQuery<Cabana> query = em.createQuery(
        "SELECT c FROM Cabana c WHERE c.id NOT IN (" +
        "SELECT r.cabanaId.id FROM Reservacion r " +
        "WHERE (:fechaEntrada BETWEEN r.fechaEntrada AND r.fechaSalida) " +
        "OR (:fechaSalida BETWEEN r.fechaEntrada AND r.fechaSalida))", Cabana.class);
    query.setParameter("fechaEntrada", fechaEntrada);
    query.setParameter("fechaSalida", fechaSalida);
    return query.getResultList();
}
}
