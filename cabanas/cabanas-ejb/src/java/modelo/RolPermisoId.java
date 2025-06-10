package modelo;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
public class RolPermisoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "permiso_id")
    private Integer permisoId;

    // Getters y setters
    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public Integer getPermisoId() {
        return permisoId;
    }

    public void setPermisoId(Integer permisoId) {
        this.permisoId = permisoId;
    }

    // hashCode y equals para la clave compuesta
    @Override
public int hashCode() {
    int hash = 7; // Un número arbitrario para iniciar el cálculo del hash
    hash = 31 * hash + (rolId != null ? rolId.hashCode() : 0);
    hash = 31 * hash + (permisoId != null ? permisoId.hashCode() : 0);
    return hash;
}


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RolPermisoId other = (RolPermisoId) obj;
        return this.rolId.equals(other.rolId) && this.permisoId.equals(other.permisoId);
    }
}
