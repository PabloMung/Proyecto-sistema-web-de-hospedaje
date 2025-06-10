
package manipuladatos;

import accesodatos.CabanaFacade;
import accesodatos.ReservacionFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import modelo.Cabana;
import modelo.Reservacion;

@Stateless
@LocalBean
public class MDReservacion {
   @EJB
    private ReservacionFacade reservacionFacade;
   @EJB
    private CabanaFacade cabanaFacade;

    public List<Reservacion> obtenerReservaciones() {
        return reservacionFacade.findAll();
    }
    public void crearReservacion(Reservacion reservacion) {
        reservacionFacade.create(reservacion);
    }
    public void eliminarReservacion(Reservacion reservacion) {
        reservacionFacade.remove(reservacion);
    }
    public List<Cabana> obtenerCabanasDisponibles() {
        return cabanaFacade.findCabanasDisponibles(); // Implementa esta consulta en CabanaFacade
    }
   public List<Cabana> obtenerCabanasSinReservacion() {
    return cabanaFacade.findCabanasSinReservacion();
}


    public void updateReservacion(Reservacion reservacion) {
        if (reservacion == null || reservacion.getId() == null) {
            throw new IllegalArgumentException("La reservación es nula o no tiene un ID válido.");
        }
        reservacionFacade.edit(reservacion); // Llamada al método edit del facade
    }
    public List<Cabana> obtenerCabanasDisponiblesIncluyendoActual(Cabana cabanaActual) {
    return cabanaFacade.findCabanasDisponiblesIncluyendoActual(cabanaActual);
}

}