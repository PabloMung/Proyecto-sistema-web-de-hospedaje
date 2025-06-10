package modelo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "Rol_Permiso")
public class RolPermiso implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private RolPermisoId id;

    @ManyToOne
    @MapsId("rolId")
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @ManyToOne
    @MapsId("permisoId")
    @JoinColumn(name = "permiso_id", nullable = false)
    private Permiso permiso;

    // Getters y setters
    public RolPermisoId getId() {
        return id;
    }

    public void setId(RolPermisoId id) {
        this.id = id;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Permiso getPermiso() {
        return permiso;
    }

    public void setPermiso(Permiso permiso) {
        this.permiso = permiso;
    }
}
