
package manipuladatos;

import accesodatos.CabanaFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import modelo.Cabana;


@Stateless
@LocalBean
public class MDCabana {

     @EJB
    private CabanaFacade cabanaFacade;

    // Método para eliminar una cabaña por ID
 public void removeCabana(Long id) {
    Cabana cabana = cabanaFacade.find(id);
    if (cabana != null) {
        cabanaFacade.remove(cabana);
    } else {
        throw new IllegalArgumentException("La cabaña con ID " + id + " no existe.");
    }
}
    public void saveCabana(Cabana cabana) {
    if (cabana.getId() == null) {
        cabanaFacade.create(cabana); // Crear una nueva cabaña
    } else {
        cabanaFacade.edit(cabana); // Actualizar una cabaña existente
    }
    }
    public void updateCabana(Cabana cabana) {
    cabanaFacade.edit(cabana); // Actualizar la cabaña en la base de datos
}
public List<Cabana> obtenerCabanasDisponibles() {
    return cabanaFacade.findCabanasDisponibles();
}

}
