
package admos;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import manipuladatos.MDCabana;
import modelo.Cabana;

@Named(value = "aDCabana")
@SessionScoped
public class ADCabana implements Serializable {

   @EJB
    private MDCabana mdCabana;

    private List<Cabana> cabanas;
    private Cabana selectedCabana = new Cabana();

    public List<Cabana> getCabanas() {
    if (cabanas == null) {
        // Solo obtener cabañas disponibles
        cabanas = mdCabana.obtenerCabanasDisponibles();
    }
    return cabanas;
}

    
    public String saveCabana() {
    try {
        // Guardar la cabaña utilizando MDCabana
        mdCabana.saveCabana(selectedCabana);

        // Recargar la lista de cabañas
        cabanas = null; // Forzar recarga al acceder a getCabanas()

        // Mensaje de éxito
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cabaña guardada correctamente"));

        // Redirigir a la lista de cabañas
        return "/cabanas/index.xhtml?faces-redirect=true";

    } catch (Exception e) {
        // Manejar errores y mostrar mensaje
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo guardar la cabaña"));
        return null; // Permanecer en la misma página
    }
}

    public String createNewCabana() {
    try {
        // Inicializar una nueva instancia de Cabana
        selectedCabana = new Cabana();
        // Redirigir a la vista de creación de cabaña
        return "/cabanas/crea.xhtml?faces-redirect=true";
    } catch (Exception e) {
        // Manejar errores y mostrar mensaje en caso de fallo
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo inicializar la creación de la cabaña"));
        return null; // Permanecer en la misma página
    }
}

public String editCabana(Cabana cabana) {
    try {
        // Validar que la cabaña no sea nula
        if (cabana == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Debe seleccionar una cabaña para editar"));
            return null; // Permanecer en la misma página
        }
        // Establecer la cabaña seleccionada
        selectedCabana = cabana;
        // Redirigir a la vista de edición de cabaña
        return "/cabanas/edit.xhtml?faces-redirect=true";
    } catch (Exception e) {
        // Manejar errores y mostrar mensaje en caso de fallo
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo redirigir a la edición de la cabaña"));
        return null; // Permanecer en la misma página
    }
}
    public String updateCabana() {
    try {
        // Actualizar la cabaña en la base de datos
        mdCabana.updateCabana(selectedCabana);

        // Recargar la lista de cabañas
        cabanas = null;

        // Mensaje de éxito
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cabaña actualizada correctamente"));

        // Redirigir a la vista de listado
        return "/cabanas/index.xhtml?faces-redirect=true";

    } catch (Exception e) {
        // Manejar errores y mostrar mensaje
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar la cabaña"));
        return null; // Permanecer en la misma página
    }
}

    public void deleteCabana(Cabana cabana) {
    if (cabana == null || cabana.getId() == null) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cabaña inválida para eliminar."));
        return;
    }

    try {
        mdCabana.removeCabana(cabana.getId());
        cabanas = null; // Recargar la lista después de eliminar
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cabaña eliminada correctamente."));
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la cabaña."));
        e.printStackTrace();
    }
}



    public Cabana getSelectedCabana() {
        return selectedCabana;
    }

    public void setSelectedCabana(Cabana selectedCabana) {
        this.selectedCabana = selectedCabana;
    }
    public List<SelectItem> getCapacidadOptions() {
        List<SelectItem> options = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            options.add(new SelectItem(i, i + " personas"));
        }
        return options;
    }
    
}
