package ro.pub.cs.aipi.lab04.graphicuserinterface;

import java.io.PrintWriter;
import java.util.List;

import ro.pub.cs.aipi.lab04.businesslogic.EntityManager;
import ro.pub.cs.aipi.lab04.general.Constants;

public class AdministratorGraphicUserInterface {

    private static EntityManager entityManager = new EntityManager();

    public AdministratorGraphicUserInterface() {
    }

    public static void displayAdministratorGraphicUserInterface(String username, List<String> databaseStructure,
            String currentTable, String identifierValueOfRecordToBeUpdated, PrintWriter printWriter) {
        entityManager.setTable(currentTable);
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
        content.append("        <form action=\"AdministratorServlet\" method=\"POST\" name=\"administrator_form\">\n");
        content.append("            <p style=\"text-align: right\">\n");
        content.append("                " + Constants.WELCOME_MESSAGE + username + "\n");
        content.append("                <br/>\n");
        content.append("                <input type=\"image\" name=\"" + Constants.SIGNOUT.toLowerCase() + "\" value=\""
                + Constants.SIGNOUT + "\" src=\"./images/user_interface/signout.png\"/>\n");
        content.append("                <br/>\n");
        content.append("            </p>\n");
        content.append("            <h2>" + Constants.ADMINISTRATOR_SERVLET_PAGE_NAME + "</h2>\n");
        content.append("            <p style=\"margin-left: auto; margin-right:auto\">\n");
        content.append("                <select name=\"" + Constants.CURRENT_TABLE
                + "\" onchange=\"document." + Constants.ADMINISTRATOR_FORM + ".submit()\">\n");
        for (String table : databaseStructure) {
            if (table.equals(currentTable)) {
                content.append("                    <option value=\"" + table + "\" SELECTED>" + table + "</option>\n");
            } else {
                content.append("                    <option value=\"" + table + "\">" + table + "</option>\n");
            }
        }
        content.append("                </select>\n");
        content.append("            </p>\n");
        content.append(
                "            <table style=\"background: #ffffff; margin: 0px auto;\" border=\"0\" cellpadding=\"4\" cellspacing=\"1\">\n");
        content.append("                <tbody>\n");
        List<String> structure = entityManager.getStructure();
        int primayKeyIndex = 0;
        content.append("                    <tr>\n");
        for (String attribute : structure) {
            content.append("                        <th>" + attribute + "</th>\n");
        }
        content.append("                        <th colspan=\"2\">" + Constants.OPERATION_TABLE_HEADER + "</th>\n");
        content.append("                    </tr>\n");
        content.append("                    <tr style=\"background: #ebebeb\">\n");
        String identifier = entityManager.getIdentifier();
        for (String attribute : structure) {
            if (attribute.equals(identifier)) {
                content.append("                        <td><input type=\"text\" name=\"" + attribute + "_"
                        + Constants.INSERT_BUTTON_NAME.toLowerCase() + "\" value=\""
                        + (entityManager.generateIdentifierNextValue() + 1) + "\" disabled /></td>\n");
            } else {
                content.append("                        <td><input type=\"text\" name=\"" + attribute + "_"
                        + Constants.INSERT_BUTTON_NAME.toLowerCase() + "\" /></td>\n");
            }
        }
        content.append(
                "                        <td style=\"text-align: center;\" colspan=\"2\"><input type=\"image\" name=\""
                + Constants.INSERT_BUTTON_NAME.toLowerCase() + "\" value=\"" + Constants.INSERT_BUTTON_NAME
                + "\" src=\"./images/user_interface/insert.png\" /></td>\n");
        content.append("                    </tr>\n");
        List<List<String>> tableContent = entityManager.getCollection(structure);
        for (List<String> tableRow : tableContent) {
            content.append("                    <tr style=\"background:#ebebeb\">\n");
            String currentPrimaryKey = tableRow.get(primayKeyIndex);
            boolean isRecordToBeUpdated = (identifierValueOfRecordToBeUpdated != null
                    && identifierValueOfRecordToBeUpdated.equals(currentPrimaryKey));
            int currentIndex = 0;
            for (String tableColumn : tableRow) {
                if (isRecordToBeUpdated) {
                    content.append("                        <td><input type=\"text\" name=\""
                            + structure.get(currentIndex) + "_" + currentPrimaryKey + "\" value=\"" + tableColumn
                            + "\" " + (structure.get(currentIndex).equals(identifier) ? "disabled" : "")
                            + " /></td>\n");
                } else {
                    content.append("                        <td>" + tableColumn + "</td>\n");
                }
                currentIndex++;
            }
            content.append("                        <td><input type=\"image\" name=\""
                    + Constants.UPDATE_BUTTON_NAME.toLowerCase() + (isRecordToBeUpdated ? "2" : "1") + "_"
                    + currentPrimaryKey + "\" value=\"" + Constants.UPDATE_BUTTON_NAME
                    + "\" src=\"./images/user_interface/update.png\" /></td>\n");
            content.append("                        <td><input type=\"image\" name=\""
                    + Constants.DELETE_BUTTON_NAME.toLowerCase() + "_" + currentPrimaryKey + "\" value=\""
                    + Constants.DELETE_BUTTON_NAME + "\" src=\"./images/user_interface/delete.png\" /></td>\n");
            content.append("                    </tr>\n");
        }
        content.append("                </tbody>\n");
        content.append("            </table>\n");
        content.append("        </form>\n");
        content.append("    </body>\n");
        content.append("</html>\n");
        printWriter.println(content.toString());
    }
}
