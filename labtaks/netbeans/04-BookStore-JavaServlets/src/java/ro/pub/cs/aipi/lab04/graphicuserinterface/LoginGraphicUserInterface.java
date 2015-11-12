package ro.pub.cs.aipi.lab04.graphicuserinterface;

import java.io.PrintWriter;

import ro.pub.cs.aipi.lab04.general.Constants;

public class LoginGraphicUserInterface {

    public LoginGraphicUserInterface() {
    }

    public static void displayLoginGraphicUserInterface(boolean isLoginError, PrintWriter printWriter) {
        StringBuilder content = new StringBuilder();
        content.append(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
        content.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        content.append("    <head>\n");
        content.append("        <meta http-equiv=\"content-type\" content=\"text/html; charset=ISO-8859-1\" />\n");
        content.append("        <title>" + Constants.APPLICATION_NAME + "</title>\n");
        content.append("        <link rel=\"stylesheet\" type=\"text/css\" href=\"css/bookstore.css\" />\n");
        content.append("        <link rel=\"icon\" type=\"image/x-icon\" href=\"./images/favicon.ico\" />\n");
        content.append("    </head>\n");
        content.append("    <body style=\"text-align: center\">\n");
        content.append("        <h2>" + Constants.APPLICATION_NAME.toUpperCase() + "</h2>\n");
        content.append("        <form action=\"LoginServlet\" method=\"post\">\n");
        content.append("            <div id=\"wrapperabsolute\">\n");
        content.append("                <div id=\"wrappertop\"></div>\n");
        content.append("                <div id=\"wrappermiddle\">\n");
        content.append("                    <h2>" + Constants.SIGNIN + "</h2>\n");
        content.append("                    <div id=\"username_input\">\n");
        content.append("                        <div id=\"field_inputleft\"></div>\n");
        content.append("                        <div id=\"field_inputmiddle\">\n");
        content.append("                            <input type=\"text\" name=\"" + Constants.USERNAME
                + "\" id=\"url\" value=\"" + Constants.USERNAME + "\" onclick=\"this.value = ''\">\n");
        content.append(
                "                            <img id=\"url_field\" src=\"./images/user_interface/username.png\" alt=\"\">\n");
        content.append("                        </div>\n");
        content.append("                        <div id=\"field_inputright\"></div>\n");
        content.append("                    </div>\n");
        content.append("                    <div id=\"password_input\">\n");
        content.append("                        <div id=\"field_inputleft\"></div>\n");
        content.append("                        <div id=\"field_inputmiddle\">\n");
        content.append("                            <input type=\"password\" name=\"" + Constants.PASSWORD
                + "\" id=\"url\" value=\"" + Constants.PASSWORD + "\" onclick=\"this.value = ''\">\n");
        content.append(
                "                            <img id=\"url_field\" src=\"./images/user_interface/password.png\" alt=\"\">\n");
        content.append("                        </div>\n");
        content.append("                        <div id=\"field_inputright\"></div>\n");
        content.append("                    </div>\n");
        content.append("                    <div id=\"submit\">\n");
        content.append(
                "                        <input type=\"image\" src=\"./images/user_interface/signin.png\" id=\"submit2\" name=\""
                + Constants.SIGNIN.toLowerCase() + "\" value=\"" + Constants.SIGNIN + "\">\n");
        content.append("                    </div>\n");
        if (isLoginError) {
            content.append(
                    "                    <h3 style=\"color: red\">" + Constants.ERROR_USERNAME_PASSWORD + "</h3>\n");
        }
        content.append("                </div>\n");
        content.append("                <div id=\"wrapperbottom\"></div>\n");
        content.append("            </div>\n");
        content.append("        </form>\n");
        content.append("    </body>\n");
        content.append("</html>");
        printWriter.println(content.toString());
    }
}
