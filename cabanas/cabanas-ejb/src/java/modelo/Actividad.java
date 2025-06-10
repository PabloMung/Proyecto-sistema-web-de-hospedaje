
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "actividad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Actividad.findAll", query = "SELECT a FROM Actividad a"),
    @NamedQuery(name = "Actividad.findById", query = "SELECT a FROM Actividad a WHERE a.id = :id"),
    @NamedQuery(name = "Actividad.findByNombre", query = "SELECT a FROM Actividad a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Actividad.findByPrecioAdulto", query = "SELECT a FROM Actividad a WHERE a.precioAdulto = :precioAdulto"),
    @NamedQuery(name = "Actividad.findByPrecioNino", query = "SELECT a FROM Actividad a WHERE a.precioNino = :precioNino"),
    @NamedQuery(name = "Actividad.findByCreatedAt", query = "SELECT a FROM Actividad a WHERE a.createdAt = :createdAt"),
    @NamedQuery(name = "Actividad.findByUpdatedAt", query = "SELECT a FROM Actividad a WHERE a.updatedAt = :updatedAt")})
public class Actividad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_adulto")
    private int precioAdulto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_nino")
    private int precioNino;
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Actividad() {
    }

    public Actividad(Integer id) {
        this.id = id;
    }

    public Actividad(Integer id, String nombre, int precioAdulto, int precioNino) {
        this.id = id;
        this.nombre = nombre;
        this.precioAdulto = precioAdulto;
        this.precioNino = precioNino;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecioAdulto() {
        return precioAdulto;
    }

    public void setPrecioAdulto(int precioAdulto) {
        this.precioAdulto = precioAdulto;
    }

    public int getPrecioNino() {
        return precioNino;
    }

    public void setPrecioNino(int precioNino) {
        this.precioNino = precioNino;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Actividad)) {
            return false;
        }
        Actividad other = (Actividad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Actividad[ id=" + id + " ]";
    }
    
}
