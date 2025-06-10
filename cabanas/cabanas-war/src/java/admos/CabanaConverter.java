package admos;

   import javax.faces.component.UIComponent;
   import javax.faces.context.FacesContext;
   import javax.faces.convert.Converter;
   import javax.faces.convert.FacesConverter;
   import modelo.Cabana;
   import java.util.Map;
   import javax.faces.convert.FacesConverter;

   @FacesConverter(forClass = Cabana.class, value = "cabanaConverter")
   public class CabanaConverter implements Converter {

       @Override
       public Object getAsObject(FacesContext context, UIComponent component, String value) {
           if (value == null || value.isEmpty()) {
               return null;
           }

           try {
               Long id = Long.parseLong(value);
               // Assuming you have a way to retrieve Cabana by ID, e.g., via a service or EJB
               // This is a placeholder; replace with actual logic to fetch Cabana
               // For example, using MDReservacion or a similar service
               // Cabana cabana = mdReservacion.findCabanaById(id);
               // For now, this is a mock implementation; adjust as needed
               Cabana cabana = new Cabana();
               cabana.setId(id);
               // Add more initialization if needed
               return cabana;
           } catch (NumberFormatException e) {
               throw new IllegalArgumentException("Invalid Cabana ID: " + value, e);
           }
       }

       @Override
       public String getAsString(FacesContext context, UIComponent component, Object value) {
           if (value == null) {
               return "";
           }

           if (value instanceof Cabana) {
               Cabana cabana = (Cabana) value;
               return String.valueOf(cabana.getId());
           } else {
               throw new IllegalArgumentException("Object is not of type Cabana: " + value);
           }
       }
   }