package admos;

import manipuladatos.MDUsuario;
import modelo.Usuario;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.ejb.EJB;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import manipuladatos.MDRol;
import modelo.Permiso;
import modelo.Rol;

@Named(value = "aDUsuario")
@SessionScoped

public class ADUsuario implements Serializable {

    @EJB
    private MDUsuario mdUsuario;
    private Usuario usuarioLogueado;
    private String email;
    private String password;
    private String confirmPassword;
    private Usuario newUser = new Usuario();
     private Usuario selectedUser;
    private List<Usuario> usuarios;
   private Long rolId;
   
    @EJB
    private MDRol mdRol; 
       @PostConstruct
    public void init() {
        this.selectedUser = new Usuario();
        this.selectedUser.setRolId(new Rol());
    }
    public ADUsuario() {
        this.selectedUser = new Usuario();
         this.selectedUser.setRolId(new Rol());
    }
    public List<Usuario> getUsuarios() {
    if (usuarios == null) {
        usuarios = mdUsuario.findAllUsuarios();
        for (Usuario u : usuarios) {
            System.out.println("Usuario: " + u.getNombre() + ", Rol: " + (u.getRolId() != null ? u.getRolId().getNombre() : "Sin Rol"));
        }
    }
    return usuarios;
}

    public void reloadUsuarios() {
    usuarios = mdUsuario.findAllUsuarios(); // Recargar desde la base de datos
    for (Usuario u : usuarios) {
        System.out.println("Usuario: " + u.getNombre() + ", Rol: " + (u.getRolId() != null ? u.getRolId().getNombre() : "Sin Rol"));
    }
    }

    public String editUser(Usuario user) {
        selectedUser = user;
        return "/usuarios/edit.xhtml?faces-redirect=true";
    }
    public String createNewUser() {
        this.selectedUser = new Usuario();
        this.selectedUser.setRolId(new Rol());
        return "/usuarios/crea.xhtml?faces-redirect=true";
    }
     public String loadUserForEdit(Usuario usuario) {
    this.selectedUser = usuario;
    this.selectedUser.setRolId(new Rol());
    return "/usuarios/editar.xhtml?faces-redirect=true";
    }
    public void deleteUser(int userId) {
        mdUsuario.removeUsuario(userId);
        usuarios = null; // Recargar lista
    }
    // Variables para manejar la visibilidad de los formularios
    private boolean loginVisible = true;
    private boolean registerVisible = false;

    public void switchToLogin() {
        loginVisible = true;
        registerVisible = false;
    }

    public void switchToRegister() {
        loginVisible = false;
        registerVisible = true;
    }

    public String login() {
    Usuario user = mdUsuario.findUsuarioByEmail(email); // Busca el usuario por email
    if (user != null && user.getContrasena().equals(password)) { // Verifica contraseña
        usuarioLogueado = user; // Asigna el usuario logueado
         // Cargar permisos del usuario
        List<Permiso> permisos = user.getRolId().getPermisoList();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("permisos", permisos);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLogueado", usuarioLogueado); // Almacena en la sesión
        return "/cabanas/bienvenido.xhtml?faces-redirect=true";
    }
    FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales incorrectas", "Credenciales incorrectas"));
    return null;
}


    public String register() {
        try {
            mdUsuario.createUsuario(newUser);
            newUser = new Usuario(); // Reinicia el objeto
            return "/cabanas/bienvenido.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", 
                            "No se pudo registrar el usuario"));
            return null;
        }
    }
  public String saveUser() {
    // Validaciones
    if (selectedUser.getNombre() == null || selectedUser.getNombre().isEmpty()) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nombre necesario", 
                    "El nombre es obligatorio."));
        return null;
    }

    if (selectedUser.getEmail() == null || selectedUser.getEmail().isEmpty()) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email necesario", 
                    "El correo electrónico es obligatorio."));
        return null;
    }
 
    System.out.println("Contraseña ingresada: " + selectedUser.getContrasena());

    if (selectedUser.getContrasena() == null || selectedUser.getContrasena().isEmpty()) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "contraseña necesaria", 
                    "La contraseña es obligatoria."));
        return null;
    }
    // Validar formato de contraseña
    if (!isPasswordValid(selectedUser.getContrasena())) {
    FacesContext.getCurrentInstance().addMessage(null,
        new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña debe tener entre 8 y 15 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial.",
            "La contraseña debe tener entre 8 y 15 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial."));
    return null;
    }
    
    if (!selectedUser.getContrasena().equals(confirmPassword)) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de confirmacion de contraseña", "Las contraseñas no coinciden."));
        return null;
    }

    if (selectedUser.getRolId() == null || selectedUser.getRolId().getId() == null) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener el rol", "Debe seleccionar un rol."));
        return null;
    }

    // Guardar el usuario
    try {
        mdUsuario.createUsuario(selectedUser);

        // Recargar la lista de usuarios
        reloadUsuarios();

        // Mensaje de éxito
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario Guardado", "Usuario guardado correctamente."));

        // Redirige a la página index.xhtml
        return "/usuarios/index.xhtml?faces-redirect=true";
    } catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al guardar usuario", "No se pudo guardar el usuario."));
        return null;
    }
}

private boolean isPasswordValid(String password) {
    if (password == null) {
        return false;
    }
    // Expresión regular ajustada y probada
    String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$";
    boolean isValid = password.matches(passwordRegex);
    System.out.println("Contraseña validada: " + password + ", válida: " + isValid); // Debug
    return isValid;
}

   public String updateUser() {
 
    if (selectedUser.getNombre() == null || selectedUser.getNombre().isEmpty()) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nombre obligatorio", "El nombre es obligatorio."));
        return null;
    }

    if (selectedUser.getEmail() == null || selectedUser.getEmail().isEmpty()) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "El email es obligatorio", "El correo electrónico es obligatorio."));
        return null;
    }

    if (selectedUser.getContrasena() == null || selectedUser.getContrasena().isEmpty()) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña es necesaria", "La contraseña es obligatoria."));
        return null;
    }
    if (!isPasswordValid(selectedUser.getContrasena())) {
    FacesContext.getCurrentInstance().addMessage(null, 
        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener la contraseña", 
            "La contraseña debe tener entre 8 y 15 caracteres, incluir al menos una letra mayúscula, una letra minúscula, un número y un carácter especial."));
    return null;
}

    if (!selectedUser.getContrasena().equals(confirmPassword)) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al confirmar la contraseña", "Las contraseñas no coinciden."));
        return null;
    }

    if (selectedUser.getRolId() == null || selectedUser.getRolId().getId() == null) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener el  rol", "Debe seleccionar un rol."));
        return null;
    }
    System.out.println("Rol ID antes de actualizar: " + selectedUser.getRolId().getId());
    System.out.println("Nombre del rol antes de actualizar: " + selectedUser.getRolId().getNombre());
    try {
        // Actualizar el usuario en la base de datos
        mdUsuario.updateUsuario(selectedUser);

        // Recargar la lista de usuarios
        reloadUsuarios();

        // Mensaje de éxito
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario Actualizado", "Usuario actualizado correctamente."));
        // Redirige a la página index.xhtml
    return "/usuarios/index.xhtml?faces-redirect=true";
    }
    catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar el usuario."));
         return null;
    }
   }

    public void deleteUser(Usuario usuario) {
    if (usuario != null) {
        try {
            mdUsuario.removeUsuario(usuario.getId());
            usuarios = mdUsuario.findAllUsuarios(); // Recargar la lista desde la base de datos
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado", "Usuario eliminado correctamente."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el usuario."));
        }
    }
}

   public String logout() {
    usuarioLogueado = null; // Limpia el usuario logueado
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Invalida la sesión actual
    return "/login.xhtml?faces-redirect=true"; // Redirige a la página de login
}
   // Método para convertir String a Rol
    public Rol getRolFromId(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return mdRol.findRol(Integer.parseInt(id)); // Busca el rol por ID
    }

    // Método para convertir Rol a String
    public String getIdFromRol(Rol rol) {
        if (rol == null) {
            return "";
        }
        return String.valueOf(rol.getId());
    }
   public Usuario getSelectedUser() {
    if (selectedUser == null) {
        System.out.println("Rol ID es: " + selectedUser.getRolId().getId());
        selectedUser = new Usuario();
        selectedUser.setRolId(new Rol());
        System.out.println("Rol ID des: " + selectedUser.getRolId().getId());
    } else if (selectedUser.getRolId() == null) {
        System.out.println("selectedUser.rolId es null, inicializando...");
        selectedUser.setRolId(new Rol());
    }
    return selectedUser;
    }
   public String cancelEdit() {
    return "/index.xhtml?faces-redirect=true";
    }
   
    public boolean tienePermiso(String ruta) {
    System.out.println("Verificando permiso para la ruta: " + ruta);
    List<Permiso> permisos = (List<Permiso>) FacesContext.getCurrentInstance()
            .getExternalContext().getSessionMap().get("permisos");

    if (permisos == null) {
        System.out.println("No hay permisos en la sesión.");
        return false;
    }

    boolean tiene = permisos.stream().anyMatch(p -> p.getRuta().equals(ruta));
    System.out.println("Tiene permiso (" + ruta + "): " + tiene);
    return tiene;
}



    public void setSelectedUser(Usuario selectedUser) {
        this.selectedUser = selectedUser;
    }
    // Getters y Setters
    public boolean isLoginVisible() {
        return loginVisible;
    }

    public boolean isRegisterVisible() {
        return registerVisible;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getNewUser() {
        return newUser;
    }

    public void setNewUser(Usuario newUser) {
        this.newUser = newUser;
    }
    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Usuario getUsuarioLogueado() {
    System.out.println("Usuario logueado: " + usuarioLogueado);
    return usuarioLogueado;
}


    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }
}
