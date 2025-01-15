import org.apache.commons.lang3.StringUtils;

public class TestEMail {

    private static boolean validateEmail(String email) {
        if(StringUtils.isEmpty(email) || email.length() < 6) {
            return false;
        }

        return email.matches("^(?=(?:.*[a-zA-Z]){1,})(?=(?:.*[0-9]){1,})*$");
    }


    private static String checkUser(String user) {
        if (StringUtils.containsAnyIgnoreCase(user, "kierownik", "sprzedawca", "bileter", "serwisant")) {
            return "user";
        }

        return "nieznany";
    }

    public static void main(String[] args){
    //System.out.println("is valid: " + validateEmail("123456lL"));

      String test = "12a";

    System.out.println( "matches: " + test.matches("(?=(?:.*[a-zA-Z]){1,})(?=(?:.*[0-9]){1,})^[a-zA-Z0-9@-]*$"));

    System.out.println("user: " + checkUser("sssdprzedawca"));
  }
}
