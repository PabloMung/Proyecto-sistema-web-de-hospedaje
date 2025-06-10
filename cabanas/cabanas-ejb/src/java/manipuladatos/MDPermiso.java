package manipuladatos;

import accesodatos.PermisoFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import modelo.Permiso;

@Stateless
public class MDPermiso {

    @EJB
    private PermisoFacade permisoFacade;

    // Método para obtener todos los permisos
    public List<Permiso> findAll() {
        return permisoFacade.findAll();
    }

    // Método para encontrar un permiso por su ID
    public Permiso findById(int id) {
        return permisoFacade.find(id);
    }

    // Método para crear un permiso
    public void createPermiso(Permiso permiso) {
        permisoFacade.create(permiso);
    }

    // Método para actualizar un permiso
    public void updatePermiso(Permiso permiso) {
        permisoFacade.edit(permiso);
    }

    // Método para eliminar un permiso por ID
    public void deletePermiso(int id) {
        Permiso permiso = permisoFacade.find(id);
        if (permiso != null) {
            permisoFacade.remove(permiso);
        }
    }
}
