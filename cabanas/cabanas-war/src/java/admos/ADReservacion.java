package admos;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import manipuladatos.MDCabana;
import manipuladatos.MDReservacion;
import modelo.Cabana;
import modelo.Reservacion;

@Named(value = "aDReservacion")
@SessionScoped
public class ADReservacion implements Serializable {

    @EJB
    private MDReservacion mdReservacion;
    
    @EJB
    private MDCabana mdCabana;
    private Reservacion selectedReservacion;
    private List<Reservacion> reservaciones;
    private Reservacion reservacionSeleccionada;
    private List<Cabana> cabanasDisponibles;
    private Cabana cabanaSeleccionada;
    private Map<Long, Boolean> selectedCabanas = new HashMap<>();
    private Integer numeroPersonas;
    private String telefono;
    private Date fechaEntrada;
    private Date fechaSalida;
    private List<Integer> personasOptions;
    private Long selectedCabanaId;
    private List<Reservacion> listaTemp; // Temporary list for reservation requests

    @Inject
    private ADUsuario aDUsuario;

    @PostConstruct
    public void init() {
        selectedReservacion = new Reservacion();
        cabanasDisponibles = mdReservacion.obtenerCabanasDisponibles();
        cabanasDisponibles = mdReservacion.obtenerCabanasSinReservacion();
        selectedCabanas = new HashMap<>();
        listaTemp = new ArrayList<>(); // Initialize temporary list
        personasOptions = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    public List<Integer> getPersonasOptions() {
        return personasOptions;
    }

    public List<Reservacion> getReservaciones() {
        if (reservaciones == null) {
            reservaciones = mdReservacion.obtenerReservaciones();
        }
        return reservaciones;
    }
    
    public Cabana getCabanaSeleccionada() {
    return cabanaSeleccionada;
}

public void setCabanaSeleccionada(Cabana cabanaSeleccionada) {
    this.cabanaSeleccionada = cabanaSeleccionada;
}

    public List<Reservacion> getListaTemp() {
        return listaTemp;
    }

    public Reservacion getReservacionSeleccionada() {
        if (reservacionSeleccionada == null) {
            reservacionSeleccionada = new Reservacion();
        }
        return reservacionSeleccionada;
    }

    public void setReservacionSeleccionada(Reservacion reservacionSeleccionada) {
        this.reservacionSeleccionada = reservacionSeleccionada;
    }

    public String prepareCrearReservacion() {
        reservacionSeleccionada = new Reservacion();
        return "/reservaciones/crea.xhtml?faces-redirect=true";
    }

    public String prepareEditarReservacion(Reservacion reservacion) {
        if (reservacion == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se seleccionó ninguna reservación."));
            return null;
        }
        this.selectedReservacion = reservacion;
        actualizarCabanasDisponibles();
        return "/reservaciones/editar.xhtml?faces-redirect=true";
    }

    public String eliminarReservacion(Reservacion reservacion) {
        mdReservacion.eliminarReservacion(reservacion);
        reservaciones = null; // Forzar recarga de la lista
        return "/reservaciones/index.xhtml?faces-redirect=true";
    }

    public String submitReservationRequest() {
        if (reservacionSeleccionada == null) {
            reservacionSeleccionada = new Reservacion();
        }

        // Validaciones
        if (!validarReservacion()) {
            return null;
        }

        // Asignar usuario logueado si existe
        if (aDUsuario.getUsuarioLogueado() != null) {
            reservacionSeleccionada.setUsuarioId(aDUsuario.getUsuarioLogueado());
        }
        
        
        
        // Agregar a la lista temporal
        listaTemp.add(reservacionSeleccionada);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Solicitud de reservación enviada correctamente."));
            
        // Limpiar formulario
        reservacionSeleccionada = new Reservacion();
        return "/indexHome.xhtml?faces-redirect=true";
    }

    public String confirmReservationRequest(Reservacion reservacion) {
        try {
            if (reservacion == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay solicitud seleccionada para confirmar."));
                return null;
            }

            // Guardar en la base de datos
            mdReservacion.crearReservacion(reservacion);
            listaTemp.remove(reservacion); // Eliminar de la lista temporal
            reservaciones = null; // Forzar recarga de la lista
            actualizarCabanasDisponibles();
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Solicitud de reservación confirmada correctamente."));
            return "/reservaciones/indexRe.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo confirmar la solicitud."));
            e.printStackTrace();
            return null;
        }
    }

    public String deleteReservationRequest(Reservacion reservacion) {
        listaTemp.remove(reservacion);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Solicitud de reservación eliminada correctamente."));
        return "/reservaciones/indexRe.xhtml?faces-redirect=true";
    }

  public String saveReservacion() {
    if (reservacionSeleccionada == null) {
        reservacionSeleccionada = new Reservacion();
    }

    if (aDUsuario.getUsuarioLogueado() == null) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error usuario", "No has iniciado sesión."));
        return null;
    }

    if (selectedCabanaId == null) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error cabaña", "Debe seleccionar una cabaña."));
        return null;
    }

    Cabana cabanaSeleccionada = findCabanaById(selectedCabanaId);
    if (cabanaSeleccionada == null) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error cabaña", "La cabaña seleccionada no existe."));
        return null;
    }

    reservacionSeleccionada.setCabanaId(cabanaSeleccionada);
    reservacionSeleccionada.setUsuarioId(aDUsuario.getUsuarioLogueado());

    // Validaciones adicionales
    if (!validarReservacion()) {
        return null;
    }

    try {
        mdReservacion.crearReservacion(reservacionSeleccionada);
        reservaciones = null; // Forzar recarga
        actualizarCabanasDisponibles();
        reservacionSeleccionada = new Reservacion(); // Limpiar
        selectedCabanaId = null; // Limpiar selección
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Reservación realizada correctamente."));
        return "/reservaciones/index.xhtml?faces-redirect=true";
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la reservación."));
        e.printStackTrace();
        return null;
    }
}


    private boolean validarReservacion() {
        if (reservacionSeleccionada.getNombreReservador() == null || reservacionSeleccionada.getNombreReservador().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error nombre", "El nombre del reservador es obligatorio."));
            return false;
        }

        if (reservacionSeleccionada.getNumeroDePersonas() <= 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error personas", "El número de personas debe ser mayor a 0."));
            return false;
        }

        if (reservacionSeleccionada.getFechaEntrada() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error entrada", "La fecha de entrada es obligatoria."));
            return false;
        }

        if (reservacionSeleccionada.getFechaSalida() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error salida", "La fecha de salida es obligatoria."));
            return false;
        }

        if (reservacionSeleccionada.getTelefono() == null || reservacionSeleccionada.getTelefono().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error telefono", "El teléfono es obligatorio."));
            return false;
        }

        if (reservacionSeleccionada.getCabanaId() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error cabana", "Debe seleccionar una cabaña."));
            return false;
        }
        if (reservacionSeleccionada.getNumeroDePersonas() > reservacionSeleccionada.getCabanaId().getCapacidad()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error personas", "El número de personas no puede superar la capacidad de la cabaña."));
            return false;
        }
        
        return true;
    }
public Long getSelectedCabanaId() {
        return selectedCabanaId;
    }

    public void setSelectedCabanaId(Long selectedCabanaId) {
        this.selectedCabanaId = selectedCabanaId;
    }
public String updateReservacion() {
        try {
            if (selectedReservacion == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay reservación seleccionada para actualizar."));
                return null;
            }

            // Obtener la cabaña seleccionada desde el campo oculto
            if (selectedCabanaId == null || selectedCabanaId <= 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe seleccionar una cabaña."));
                return null;
            }
            if (selectedReservacion.getFechaSalida() != null && selectedReservacion.getFechaEntrada() != null) {
                if (selectedReservacion.getFechaSalida().before(selectedReservacion.getFechaEntrada())) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de fechas, no puede ser menor la fecha de salida a la fecha de entrada", 
                                         "La fecha no puede ser menor."));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de fechas", 
                                     "Las fechas de entrada y salida son obligatorias."));
                return null;
            }

            // Asignar la nueva cabaña a la reservación
            Cabana nuevaCabaña = findCabanaById(selectedCabanaId);
            if (nuevaCabaña == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La cabaña seleccionada no existe."));
                return null;
            }
            
           if (selectedReservacion.getNumeroDePersonas() <= nuevaCabaña.getCapacidad()) {
    selectedReservacion.setCabanaId(nuevaCabaña);
    mdReservacion.updateReservacion(selectedReservacion);
    
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Reservación actualizada correctamente."));
    
    return "/reservaciones/index.xhtml?faces-redirect=true";
} else {
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de personas/capacidad", 
                         "El número de personas no puede superar la capacidad de la cabaña."));
    return null;
}


        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar la reservación."));
            e.printStackTrace();
            return null;
        }
    }

    public Reservacion getSelectedReservacion() {
        return selectedReservacion;
    }

    public void setSelectedReservacion(Reservacion selectedReservacion) {
        this.selectedReservacion = selectedReservacion;
    }

    public String cancel() {
        return "/indexHome.xhtml?faces-redirect=true";
    }

    public List<Cabana> getCabanasDisponibles() {
        if (cabanasDisponibles == null){
            cabanasDisponibles = mdCabana.obtenerCabanasDisponibles();
        }
        return cabanasDisponibles;
    }

    public Map<Long, Boolean> getSelectedCabanas() {
        return selectedCabanas;
    }

    public Integer getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(Integer numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public void setSelectedCabanas(Map<Long, Boolean> selectedCabanas) {
        this.selectedCabanas = selectedCabanas;
    }
private Cabana findCabanaById(Long cabanaId) {
        if (cabanasDisponibles == null || cabanasDisponibles.isEmpty()) {
            System.out.println("La lista cabanasDisponibles está vacía o no inicializada.");
            return null;
        }

        for (Cabana cabana : cabanasDisponibles) {
            if (cabanaId.equals(cabana.getId())) {
                return cabana;
            }
        }

        System.out.println("Error: No se encontró una cabaña con ID: " + cabanaId);
        return null;
    }

    public void actualizarCabanasDisponibles() {
        if (selectedReservacion != null) {
            cabanasDisponibles = mdReservacion.obtenerCabanasDisponiblesIncluyendoActual(selectedReservacion.getCabanaId());
        } else {
            cabanasDisponibles = mdReservacion.obtenerCabanasSinReservacion();
        }
    }

    public void cargarCabanasDisponiblesParaEdicion() {
        if (selectedReservacion != null) {
            cabanasDisponibles = mdReservacion.obtenerCabanasDisponiblesIncluyendoActual(selectedReservacion.getCabanaId());
        } else {
            cabanasDisponibles = mdReservacion.obtenerCabanasSinReservacion();
        }
    }
}