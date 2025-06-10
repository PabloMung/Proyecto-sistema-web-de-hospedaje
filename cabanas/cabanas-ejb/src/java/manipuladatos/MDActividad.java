package manipuladatos;

import accesodatos.ActividadFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import modelo.Actividad;

@Stateless
@LocalBean
public class MDActividad {

    @EJB
    private ActividadFacade actividadF;

    // Crear actividad
    public void createActividad(Actividad actividad) {
        actividadF.create(actividad);
    }

    // Editar actividad
    public void editActividad(Actividad actividad) {
    actividadF.edit(actividad); // Actualiza la actividad en la base de datos
}

    // Eliminar actividad
    public void removeActividad(Integer id) {
        Actividad actividad = actividadF.find(id);
        if (actividad != null) {
            actividadF.remove(actividad);
        }
    }

    // Obtener actividad por ID
    public Actividad findActividad(Integer id) {
        return actividadF.find(id);
    }

    // Obtener todas las actividades
    public List<Actividad> findAllActividades() {
        return actividadF.findAll();
    }

    // Obtener actividades por precio máximo
    public List<Actividad> findActividadesByMaxPrice(int maxPrice) {
        return actividadF.findByMaxAdultPrice(maxPrice);
    }
}
