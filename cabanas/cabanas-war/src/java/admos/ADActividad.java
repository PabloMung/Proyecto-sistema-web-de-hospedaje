package admos;

import manipuladatos.MDActividad;
import modelo.Actividad;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.ejb.EJB;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named(value = "aDActividad")
@SessionScoped
public class ADActividad implements Serializable {

    @EJB
    private MDActividad mdActividad;

    private List<Actividad> actividades;
    private Actividad selectedActividad;

    public List<Actividad> getActividades() {
        if (actividades == null) {
            actividades = mdActividad.findAllActividades();
        }
        return actividades;
    }
     public String navigateToCreate() {
        selectedActividad = new Actividad();
        return "/Actividad/crea.xhtml?faces-redirect=true";
    }
    public String createActividad() {
        try {
            mdActividad.createActividad(selectedActividad);
            actividades = null; // Refrescar lista
            return "/Actividad/index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo crear la actividad"));
            return null;
        }
    }

    public String editActividad(Actividad actividad) {
    selectedActividad = actividad; // Establece la actividad a editar
    return "/Actividad/edit.xhtml?faces-redirect=true"; // Redirige a la página de edición
}

    public String updateActividad() {
    try {
        mdActividad.editActividad(selectedActividad); // Actualiza la actividad en la base de datos
        actividades = null; // Refresca la lista de actividades
        return "/Actividad/index.xhtml?faces-redirect=true"; // Redirige a la lista de actividades
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar la actividad"));
        return null; // Se queda en la misma página si hay un error
    }
}


    public void deleteActividad(Actividad actividad) {
        try {
            mdActividad.removeActividad(actividad.getId());
            actividades = null; // Refrescar lista
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Actividad eliminada correctamente"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la actividad"));
        }
    }

    public Actividad getSelectedActividad() {
        return selectedActividad;
    }

    public void setSelectedActividad(Actividad selectedActividad) {
        this.selectedActividad = selectedActividad;
    }
}
