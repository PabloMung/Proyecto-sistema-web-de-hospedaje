
package admos.filters;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Permiso;

public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) { }

   @Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    // Obtener la URL solicitada
    String url = req.getRequestURI();
    String rutaSinContexto = url.substring(req.getContextPath().length());

    // Remover el prefijo "/faces" para la comparación
    String rutaNormalizada = rutaSinContexto.replaceFirst("^/faces", "");

    System.out.println("URL solicitada: " + url);
    System.out.println("Ruta sin contexto: " + rutaSinContexto);
    System.out.println("Ruta normalizada: " + rutaNormalizada);

    // Obtener los permisos del usuario desde la sesión
    List<Permiso> permisos = (List<Permiso>) req.getSession().getAttribute("permisos");
    if (permisos == null) {
        System.out.println("No hay permisos en la sesión. Redirigiendo a login.");
        res.sendRedirect(req.getContextPath() + "/login.xhtml");
        return;
    }

    // Imprimir los permisos cargados
    permisos.forEach(p -> System.out.println("Permiso: " + p.getRuta()));

    // Verificar si la ruta normalizada coincide con los permisos
    if (permisos.stream().noneMatch(p -> rutaNormalizada.equals(p.getRuta()))) {
        System.out.println("Permiso denegado para la URL: " + rutaNormalizada);
        res.sendRedirect(req.getContextPath() + "/acceso_denegado.xhtml");
        return;
    }

    System.out.println("Permiso concedido para la URL: " + rutaNormalizada);
    chain.doFilter(request, response);
}


    @Override
    public void destroy() { }
}
