package manipuladatos;

import accesodatos.RolFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.Permiso;
import modelo.Rol;
import modelo.RolPermiso;

@Stateless
@LocalBean
public class MDRol {

    @EJB
    private RolFacade rolF;
    @PersistenceContext
    private EntityManager em;

    // Crear rol
    public void createRol(Rol rol) {
        rolF.create(rol);
    }

    // Editar rol
    public void editRol(Rol rol) {
        rolF.edit(rol);
    }

    // Eliminar rol
    public void removeRol(Integer id) {
        Rol rol = rolF.find(id);
        if (rol != null) {
            rolF.remove(rol);
        }
    }
    public List<Integer> findPermisosByRol(int rolId) {
    return em.createQuery(
            "SELECT rp.permiso.id FROM RolPermiso rp WHERE rp.rol.id = :rolId", Integer.class)
            .setParameter("rolId", rolId)
            .getResultList();
}


public void updatePermisos(int rolId, List<Integer> permisosIds) {
    // 1. Eliminar permisos existentes
    em.createQuery("DELETE FROM RolPermiso rp WHERE rp.rol.id = :rolId")
      .setParameter("rolId", rolId)
      .executeUpdate();

    // 2. Insertar nuevos permisos
    Rol rol = em.find(Rol.class, rolId);
    for (Integer permisoId : permisosIds) {
        Permiso permiso = em.find(Permiso.class, permisoId);
        RolPermiso rp = new RolPermiso();
        rp.setRol(rol);
        rp.setPermiso(permiso);
        em.persist(rp);
    }
}



    // Obtener rol por ID
    public Rol findRol(Integer id) {
        return rolF.find(id);
    }

    // Obtener todos los roles
    public List<Rol> findAllRoles() {
        return rolF.findAll();
    }

    // Buscar rol por nombre
    public Rol findRolByNombre(String nombre) {
        return rolF.findByNombre(nombre);
    }
}
