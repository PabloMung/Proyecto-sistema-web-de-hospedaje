
package accesodatos;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Cabana;


@Stateless
public class CabanaFacade extends AbstractFacade<Cabana> {

    @PersistenceContext(unitName = "cabanas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CabanaFacade() {
        super(Cabana.class);
    }
    @Override
    public void edit(Cabana entity) {
    getEntityManager().merge(entity);
    }
    public List<Cabana> findCabanasDisponibles() {
    return em.createQuery(
        "SELECT c FROM Cabana c WHERE c.disponible = true AND c.id NOT IN " +
        "(SELECT r.cabanaId.id FROM Reservacion r WHERE r.fechaEntrada <= :currentDate AND r.fechaSalida >= :currentDate)", 
        Cabana.class)
        .setParameter("currentDate", new Date()) // Fecha actual
        .getResultList();
}
public List<Cabana> findCabanasSinReservacion() {
    return em.createQuery(
        "SELECT c FROM Cabana c WHERE c.id NOT IN " +
        "(SELECT r.cabanaId.id FROM Reservacion r WHERE r.cabanaId IS NOT NULL)", 
        Cabana.class)
        .getResultList();
}
public List<Cabana> findCabanasDisponiblesIncluyendoActual(Cabana cabanaActual) {
    return em.createQuery(
        "SELECT c FROM Cabana c WHERE c.id NOT IN " +
        "(SELECT r.cabanaId.id FROM Reservacion r WHERE r.cabanaId IS NOT NULL) " +
        "OR c.id = :cabanaActualId", Cabana.class)
        .setParameter("cabanaActualId", cabanaActual != null ? cabanaActual.getId() : -1L)
        .getResultList();
}

}
