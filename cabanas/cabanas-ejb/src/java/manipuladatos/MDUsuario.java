package manipuladatos;

import accesodatos.UsuarioFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import modelo.Usuario;

@Stateless
@LocalBean
public class MDUsuario {

    @EJB
    private UsuarioFacade usuarioF;

    // Crear usuario
    public void createUsuario(Usuario usuario) {
        usuarioF.create(usuario);
    }
    // Eliminar usuario por ID
    public void removeUsuario(Integer id) {
        Usuario usuario = usuarioF.find(id);
        if (usuario != null) {
            usuarioF.remove(usuario);
        }
    }
    public void updateUsuario(Usuario usuario) {
    try {
        Usuario existingUser = usuarioF.find(usuario.getId());
        if (existingUser != null) {
            existingUser.setNombre(usuario.getNombre());
            existingUser.setEmail(usuario.getEmail());
            existingUser.setContrasena(usuario.getContrasena());
            existingUser.setRolId(usuario.getRolId()); // Asegúrate de que esta línea actualice el Rol
            usuarioF.edit(existingUser);
        }
    } catch (Exception e) {
        throw new RuntimeException("Error al actualizar el usuario", e);
    }
}

    // Obtener usuario por ID
    public Usuario findUsuarioById(Integer id) {
        return usuarioF.find(id);
    }

    // Obtener todos los usuarios
    public List<Usuario> findAllUsuarios() {
        return usuarioF.findAll();
    }

    // Buscar usuario por correo electrónico
    public Usuario findUsuarioByEmail(String email) {
        return usuarioF.findByEmail(email);
    }

    // Obtener usuarios por rol
    public List<Usuario> findUsuariosByRol(Integer rolId) {
        return usuarioF.findByRol(rolId);
    }
}
