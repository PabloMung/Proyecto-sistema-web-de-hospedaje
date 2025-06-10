package admos;

import manipuladatos.MDRol;
import modelo.Rol;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.ejb.EJB;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import manipuladatos.MDPermiso;
import modelo.Permiso;


@Named(value = "aDRol")
@SessionScoped
public class ADRol implements Serializable {

    @EJB
    private MDRol mdRol;
    @EJB
    private MDPermiso mdPermiso;
    private List<Permiso> permisos; // Todos los permisos disponibles
    private List<Integer> permisosSeleccionados; // IDs de permisos seleccionados para el rol
    private Rol rol = new Rol();
    private List<Rol> roles;
    private Rol selectRol;
    public ADRol() {
    }
    @PostConstruct
    public void init() {
    roles = mdRol.findAllRoles();
    if (roles == null || roles.isEmpty()) {
        System.out.println("La lista de roles está vacía.");
    }
    }
    public String crearNuevoRol() {
    selectRol = new Rol(); // Inicializa un nuevo usuario
    return "/roles/crea.xhtml?faces-redirect=true";
}
    // Crear nuevo rol
    public void createRol() {
        mdRol.createRol(rol);
        rol = new Rol();
        loadRoles();
    }
     // Método para editar un rol
    public String editRol(Rol rolSeleccionado) {
    this.rol = rolSeleccionado; // Cargar rol seleccionado
    loadPermisos(); // Cargar todos los permisos disponibles
    loadPermisosForRol(rolSeleccionado.getId()); // Cargar permisos asignados al rol
    return "/roles/editar.xhtml?faces-redirect=true"; // Redirigir a la vista de edición
}
    // Cargar todos los roles
    public void loadRoles() {
        roles = mdRol.findAllRoles();
    }
      // Método para guardar los cambios del rol
    public String updateRol() {
        try {
            mdRol.editRol(rol); // Llama al método en el EJB para actualizar el rol
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Rol actualizado correctamente."));
            rol = new Rol(); // Reinicia el objeto rol
            loadRoles();     // Recargar la lista de roles
            return "/roles/index.xhtml?faces-redirect=true"; // Redirigir al índice de roles
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el rol."));
            return null;
        }
    }
    // Método para eliminar un rol
    public void deleteRol(Rol rl) {
    mdRol.removeRol(rl.getId());
    loadRoles();
    }
     // Método para cargar todos los permisos
    public void loadPermisos() {
        permisos = mdPermiso.findAll();
    } 
    // Método para cargar los permisos asignados a un rol
    public void loadPermisosForRol(int rolId) {
        permisosSeleccionados = mdRol.findPermisosByRol(rolId); // Este método debe implementarse en MDRol
    }
    
    public void savePermisosForRol() {
    try {
        mdRol.updatePermisos(rol.getId(), permisosSeleccionados); // Guardar la relación rol-permiso
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Permisos guardados correctamente."));
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudieron guardar los permisos."));
    }
}


    // Getters y setters para permisos y permisosSeleccionados
    public List<Permiso> getPermisos() {
        if (permisos == null) {
            loadPermisos();
        }
        return permisos;
    }

    public List<Integer> getPermisosSeleccionados() {
        return permisosSeleccionados;
    }

    public void setPermisosSeleccionados(List<Integer> permisosSeleccionados) {
        this.permisosSeleccionados = permisosSeleccionados;
    }

    // Getters y Setters
    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

     public List<Rol> getRoles() {
        if (roles == null) {
            loadRoles(); // Cargar los roles si aún no están disponibles
        }
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}
