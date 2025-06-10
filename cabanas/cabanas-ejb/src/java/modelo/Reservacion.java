
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "reservacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reservacion.findAll", query = "SELECT r FROM Reservacion r"),
    @NamedQuery(name = "Reservacion.findById", query = "SELECT r FROM Reservacion r WHERE r.id = :id"),
    @NamedQuery(name = "Reservacion.findByNombreReservador", query = "SELECT r FROM Reservacion r WHERE r.nombreReservador = :nombreReservador"),
    @NamedQuery(name = "Reservacion.findByNumeroDePersonas", query = "SELECT r FROM Reservacion r WHERE r.numeroDePersonas = :numeroDePersonas"),
    @NamedQuery(name = "Reservacion.findByFechaEntrada", query = "SELECT r FROM Reservacion r WHERE r.fechaEntrada = :fechaEntrada"),
    @NamedQuery(name = "Reservacion.findByFechaSalida", query = "SELECT r FROM Reservacion r WHERE r.fechaSalida = :fechaSalida"),
    @NamedQuery(name = "Reservacion.findByTelefono", query = "SELECT r FROM Reservacion r WHERE r.telefono = :telefono"),
    @NamedQuery(name = "Reservacion.findByCreatedAt", query = "SELECT r FROM Reservacion r WHERE r.createdAt = :createdAt"),
    @NamedQuery(name = "Reservacion.findByUpdatedAt", query = "SELECT r FROM Reservacion r WHERE r.updatedAt = :updatedAt")})
public class Reservacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre_reservador")
    private String nombreReservador;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_de_personas")
    private int numeroDePersonas;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_entrada")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrada;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "cabana_id", referencedColumnName = "id")
    private Cabana cabanaId;
    
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioId;
    
    
    
    public Reservacion() {
    }

    public Reservacion(Integer id) {
        this.id = id;
    }

    public Reservacion(Integer id, String nombreReservador, int numeroDePersonas, Date fechaEntrada, Date fechaSalida, String telefono) {
        this.id = id;
        this.nombreReservador = nombreReservador;
        this.numeroDePersonas = numeroDePersonas;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.telefono = telefono;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreReservador() {
        return nombreReservador;
    }

    public void setNombreReservador(String nombreReservador) {
        this.nombreReservador = nombreReservador;
    }

    public int getNumeroDePersonas() {
        return numeroDePersonas;
    }

    public void setNumeroDePersonas(int numeroDePersonas) {
        this.numeroDePersonas = numeroDePersonas;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public Cabana getCabanaId() {
        return cabanaId;
    }

    public void setCabanaId(Cabana cabanaId) {
        this.cabanaId = cabanaId;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
       
        if (!(object instanceof Reservacion)) {
            return false;
        }
        Reservacion other = (Reservacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Reservacion[ id=" + id + " ]";
    } 
}
