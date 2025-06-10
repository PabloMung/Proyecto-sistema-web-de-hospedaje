
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "cabana")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cabana.findAll", query = "SELECT c FROM Cabana c"),
    @NamedQuery(name = "Cabana.findById", query = "SELECT c FROM Cabana c WHERE c.id = :id"),
    @NamedQuery(name = "Cabana.findByNombre", query = "SELECT c FROM Cabana c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cabana.findByCapacidad", query = "SELECT c FROM Cabana c WHERE c.capacidad = :capacidad"),
    @NamedQuery(name = "Cabana.findByDisponible", query = "SELECT c FROM Cabana c WHERE c.disponible = :disponible"),
    @NamedQuery(name = "Cabana.findByCreatedAt", query = "SELECT c FROM Cabana c WHERE c.createdAt = :createdAt"),
    @NamedQuery(name = "Cabana.findByUpdatedAt", query = "SELECT c FROM Cabana c WHERE c.updatedAt = :updatedAt"),
    @NamedQuery(name = "Cabana.findByCosto", query = "SELECT c FROM Cabana c WHERE c.costo = :costo")})
public class Cabana implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "capacidad")
    private int capacidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "disponible")
    private long disponible;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo")
    private int costo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cabanaId")
    private List<Reservacion> reservacionList;

    public Cabana() {
    }

    public Cabana(Long id) {
        this.id = id;
    }

    public Cabana(Long id, String nombre, int capacidad, long disponible, int costo) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.disponible = disponible;
        this.costo = costo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public long getDisponible() {
        return disponible;
    }

    public void setDisponible(long disponible) {
        this.disponible = disponible;
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

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    @XmlTransient
    public List<Reservacion> getReservacionList() {
        return reservacionList;
    }

    public void setReservacionList(List<Reservacion> reservacionList) {
        this.reservacionList = reservacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cabana)) {
            return false;
        }
        Cabana other = (Cabana) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Cabana[ id=" + id + " ]";
    }
    
}
