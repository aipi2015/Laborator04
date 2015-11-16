package ro.pub.cs.aipi.lab04.graphicuserinterface;

import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ro.pub.cs.aipi.lab04.businesslogic.BookPresentationManager;
import ro.pub.cs.aipi.lab04.businesslogic.CategoryManager;
import ro.pub.cs.aipi.lab04.businesslogic.FormatManager;
import ro.pub.cs.aipi.lab04.businesslogic.LanguageManager;
import ro.pub.cs.aipi.lab04.general.Constants;
import ro.pub.cs.aipi.lab04.general.Utilities;
import ro.pub.cs.aipi.lab04.helper.Record;

public class ClientGraphicUserInterface {

    private static BookPresentationManager bookPresentationManager = new BookPresentationManager();
    private static FormatManager formatManager = new FormatManager();
    private static LanguageManager languageManager = new LanguageManager();
    private static CategoryManager categoryManager = new CategoryManager();

    public ClientGraphicUserInterface() {
    }

    @SuppressWarnings("unchecked")
    public static void displayClientGraphicUserInterface(String username, List<List<Record>> books, String errorMessage,
            List<Record> shoppingCart, List<String> formatsFilter, List<String> languagesFilter,
            List<String> categoriesFilter, int currentRecordsPerPage, int currentPage, boolean filterChange, PrintWriter printWriter) {
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
        content.append("    <body>\n");
        content.append(
                "        <h2 style=\"text-align: center\">" + Constants.APPLICATION_NAME.toUpperCase() + "</h2>\n");
        content.append("        <form action=\"" + Constants.CLIENT_SERVLET_PAGE_CONTEXT + "\" method=\"post\" name=\""
                + Constants.CLIENT_FORM + "\">\n");
        content.append("            <p style=\"text-align:right\">\n");
        content.append("                " + Constants.WELCOME_MESSAGE + username + "\n");
        content.append("                <br/>\n");
        content.append("                <input type=\"image\" name=\"" + Constants.SIGNOUT.toLowerCase() + "\" value=\""
                + Constants.SIGNOUT + "\" src=\"./images/user_interface/signout.png\" />\n");
        content.append("                <br/>\n");
        content.append("            </p>\n");
        content.append(
                "            <h2 style=\"text-align: center\">" + Constants.CLIENT_SERVLET_PAGE_NAME + "</h2>\n");
        content.append("            <p style=\"text-align:right\">\n");
        content.append("                " + Constants.RECORDS_PER_PAGE + "<select name=\""
                + Utilities.removeSpaces(Constants.RECORDS_PER_PAGE.toLowerCase().trim()) + "\" onchange=\"document."
                + Constants.CLIENT_FORM + ".submit()\">\n");
        for (int recordsPerPageValue : Constants.RECORDS_PER_PAGE_VALUES) {
            content.append("                    <option value=\"" + recordsPerPageValue + "\""
                    + ((recordsPerPageValue == currentRecordsPerPage) ? " SELECTED" : "") + ">" + recordsPerPageValue
                    + "</option>\n");
        }
        content.append("                </select>\n");
        content.append("                " + Constants.PAGE + "<select name=\""
                + Utilities.removeSpaces(Constants.PAGE.toLowerCase().trim()) + "\" onchange=\"document."
                + Constants.CLIENT_FORM + ".submit()\">\n");
        for (int pageValue = 1; pageValue <= books.size() / currentRecordsPerPage + ((books.size() % currentRecordsPerPage) != 0 ? 1 : 0); pageValue++) {
            content.append("                    <option value=\"" + pageValue + "\""
                    + ((pageValue == currentPage) ? " SELECTED" : "") + ">" + pageValue + "</option>\n");
        }
        content.append("                </select>\n");
        content.append("            </p>\n");
        content.append(
                "            <table border=\"0\" cellpadding=\"4\" cellspacing=\"1\" style=\"width: 100%; background-image: url(./images/user_interface/background.jpg); margin: 0px auto;\">\n");
        content.append("                <tbody>\n");
        content.append("                    <tr>\n");
        content.append("                        <td style=\"width: 20%; text-align: left; vertical-align: top;\">\n");
        content.append("                            <div id=\"wrapperrelative\">\n");
        content.append("                                <div id=\"wrappertop\"></div>\n");
        content.append("                                <div id=\"wrappermiddle\">\n");
        content.append(
                "                                    <table border=\"0\" cellpadding=\"4\" cellspacing=\"1\">\n");
        content.append("                                        <tbody>\n");
        content.append("                                            <tr>\n");
        content.append("                                                <td>" + Constants.FORMAT + "</td>\n");
        content.append("                                                <td>\n");
        content.append("                                                    <select name=\"" + Constants.CURRENT_FORMAT
                + "\" style=\"width: 100%\">\n");
        List<String> formatAttributes = new ArrayList<>();
        formatAttributes.add(Constants.FORMAT_ATTRIBUTE);
        List<List<String>> formats = formatManager.getCollection(formatAttributes);
        List<String> formatsList = new ArrayList<>();
        for (List<String> format : formats) {
            formatsList.add(format.get(0));
        }
        for (String formatAttribute : formatsList) {
            content.append("                                                        <option value=\"" + formatAttribute
                    + "\">" + formatAttribute + "</option>\n");
        }
        content.append("                                                    </select>\n");
        content.append("                                                </td>\n");
        content.append("                                                <td><input type=\"image\" name=\""
                + Constants.INSERT_BUTTON_NAME.toLowerCase() + "_" + Constants.FORMAT
                + "\" src=\"./images/user_interface/insert.png\"/></td>\n");
        content.append("                                            </tr>\n");
        content.append("                                            <tr><td colspan=\"3\">\n");
        content.append("                                                <table>\n");
        for (String formatFilter : formatsFilter) {
            content.append(
                    "                                                    <tr><td style=\"background: #ebebeb; text-align: left;\">"
                    + formatFilter + "</td><td><input type=\"image\" name=\""
                    + Constants.DELETE_BUTTON_NAME.toLowerCase() + "_" + Constants.FORMAT + "_" + formatFilter
                    + "\" src=\"./images/user_interface/delete.png\" width=\"16\" height=\"16\" /></td></tr>\n");
        }
        content.append("                                                </table>\n");
        content.append("                                            </td></tr>");
        content.append("                                            <tr>\n");
        content.append("                                                <td>" + Constants.LANGUAGE + "</td>\n");
        content.append("                                                <td>\n");
        content.append("                                                    <select name=\""
                + Constants.CURRENT_LANGUAGE + "\" style=\"width: 100%\">\n");
        List<String> languageAttributes = new ArrayList<>();
        languageAttributes.add(Constants.LANGUAGE_ATTRIBUTE);
        List<List<String>> languages = languageManager.getCollection(languageAttributes);
        List<String> languagesList = new ArrayList<>();
        for (List<String> language : languages) {
            languagesList.add(language.get(0));
        }
        for (String languageAttribute : languagesList) {
            content.append("                                                        <option value=\""
                    + languageAttribute + "\">" + languageAttribute + "</option>\n");
        }
        content.append("                                                    </select>\n");
        content.append("                                                </td>\n");
        content.append("                                                <td><input type=\"image\" name=\""
                + Constants.INSERT_BUTTON_NAME.toLowerCase() + "_" + Constants.LANGUAGE
                + "\" src=\"./images/user_interface/insert.png\"/></td>\n");
        content.append("                                            </tr>\n");
        content.append("                                            <tr><td colspan=\"3\">\n");
        content.append("                                                <table>\n");
        for (String languageFilter : languagesFilter) {
            content.append(
                    "                                                    <tr><td style=\"background: #ebebeb; text-align: left;\">"
                    + languageFilter + "</td><td><input type=\"image\" name=\""
                    + Constants.DELETE_BUTTON_NAME.toLowerCase() + "_" + Constants.LANGUAGE + "_"
                    + languageFilter
                    + "\" src=\"./images/user_interface/delete.png\" width=\"16\" height=\"16\" /></td></tr>\n");
        }
        content.append("                                                </table>\n");
        content.append("                                            </td></tr>");
        content.append("                                            <tr>\n");
        content.append("                                                <td>" + Constants.CATEGORY + "</td>\n");
        content.append("                                                <td>\n");
        content.append("                                                    <select name=\""
                + Constants.CURRENT_CATEGORY + "\" style=\"width: 100%\">\n");
        List<String> categoryAttributes = new ArrayList<>();
        categoryAttributes.add(Constants.LANGUAGE_ATTRIBUTE);
        List<List<String>> categories = categoryManager.getCollection(categoryAttributes);
        List<String> categoriesList = new ArrayList<>();
        for (List<String> category : categories) {
            categoriesList.add(category.get(0));
        }
        for (String categoryAttribute : categoriesList) {
            content.append("                                                        <option value=\""
                    + categoryAttribute + "\">" + categoryAttribute + "</option>\n");
        }
        content.append("                                                    </select>\n");
        content.append("                                                </td>\n");
        content.append("                                                <td><input type=\"image\" name=\""
                + Constants.INSERT_BUTTON_NAME.toLowerCase() + "_" + Constants.CATEGORY
                + "\" src=\"./images/user_interface/insert.png\"/></td>\n");
        content.append("                                            </tr>\n");
        content.append("                                            <tr><td colspan=\"3\">\n");
        content.append("                                                <table>\n");
        for (String categoryFilter : categoriesFilter) {
            content.append(
                    "                                                    <tr><td style=\"background: #ebebeb; text-align: left;\">"
                    + categoryFilter + "</td><td><input type=\"image\" name=\""
                    + Constants.DELETE_BUTTON_NAME.toLowerCase() + "_" + Constants.CATEGORY + "_"
                    + categoryFilter
                    + "\" src=\"./images/user_interface/delete.png\" width=\"16\" height=\"16\" /></td></tr>\n");
        }
        content.append("                                                </table>\n");
        content.append("                                            </td></tr>");
        content.append("                                        </tbody>\n");
        content.append("                                    </table>\n");
        content.append("                                </div>\n");
        content.append("                                <div id=\"wrapperbottom\"></div>\n");
        content.append("                            </div>\n");
        content.append("                        </td>\n");
        content.append("                        <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n");
        content.append("                        <td style=\"width: 60%; text-align: center;\">\n");
        if (errorMessage != null && !errorMessage.isEmpty()) {
            content.append("                            " + errorMessage + "\n");
            content.append("                            <br/>\n");
            content.append("                            <br/>\n");
        }
        content.append(
                "                            <table border=\"0\" cellpadding=\"4\" cellspacing=\"1\" style=\"margin: 0px auto;\">\n");
        content.append("                                <tbody>\n");
        int index = 0;
        for (List<Record> book : books) {
            index++;
            if (index < ((currentPage - 1) * currentRecordsPerPage + 1)
                    || index > (currentPage * currentRecordsPerPage)) {
                continue;
            }
            content.append("                                    <tr>\n");
            content.append("                                        <td>\n");
            content.append("                                            <div id=\"wrappertop\"></div>\n");
            content.append("                                            <div id=\"wrappermiddle\">\n");
            content.append(
                    "                                                <table style=\"width: 100%;\" border=\"0\" cellpadding=\"4\" cellspacing=\"4\">\n");
            content.append("                                                    <tbody>\n");
            content.append("                                                        <tr>\n");
            content.append("                                                            <td>&nbsp;</td>\n");
            content.append(
                    "                                                            <td style=\"text-align: left;\">\n");
            for (int field = Constants.TITLE_INDEX; field <= Constants.CATEGORIES_INDEX; field++) {
                content.append("                                                                <b>"
                        + book.get(field).getAttribute() + "</b>: " + book.get(field).getValue() + "\n");
                content.append("                                                                <br/>\n");
            }
            content.append("                                                                <br/>\n");
            content.append(
                    "                                                                <table style=\"width: 100%;\">\n");
            List<List<Record>> bookPresentations = (List<List<Record>>) book.get(Constants.BOOK_PRESENTATIONS_INDEX)
                    .getValue();
            for (List<Record> bookPresentation : bookPresentations) {
                content.append("                                                                  <tr>\n");
                content.append(
                        "                                                                    <td style=\"width: 100%; background: #ebebeb; text-align: left;\">\n");
                for (int field = Constants.ISBN_INDEX; field <= Constants.STOCKPILE_INDEX; field++) {
                    content.append("                                                                <b>"
                            + bookPresentation.get(field).getAttribute() + "</b>: "
                            + bookPresentation.get(field).getValue() + "\n");
                    content.append("                                                                <br/>\n");
                }
                content.append("                                                                    </td>\n");
                content.append("                                                                    <td>&nbsp;</td>\n");
                String currentIdentifier = bookPresentation.get(Constants.ID_INDEX).getValue().toString();
                content.append(
                        "                                                                    <td style=\"vertical-align: middle;\">\n");
                content.append("                                                                        <table>\n");
                content.append("                                                                            <tr>\n");
                content.append(
                        "                                                                                <td>\n");
                content.append(
                        "                                                                                    <input type=\"text\" name=\""
                        + Constants.COPIES.toLowerCase() + "_"
                        + Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()) + "_"
                        + currentIdentifier + "\" size=\"3\"/>\n");
                content.append(
                        "                                                                                    <br/>\n");
                content.append(
                        "                                                                                    <input type=\"image\" name=\""
                        + Constants.INSERT_BUTTON_NAME.toLowerCase() + "_"
                        + Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()) + "_"
                        + currentIdentifier + "\" value=\"" + Constants.INSERT_BUTTON_NAME
                        + "\" src=\"./images/user_interface/add_to_shopping_cart.png\"/>\n");
                content.append(
                        "                                                                                </td>\n");
                content.append("                                                                            </tr>\n");
                content.append("                                                                        </table>\n");
                content.append("                                                                    </td>\n");
                content.append("                                                                  </tr>\n");
                content.append(
                        "                                                                  <tr><td colspan=\"2\">&nbsp;</td></tr>\n");
            }
            content.append("                                                                </table>\n");
            content.append("                                                            </td>\n");
            content.append("                                                        </tr>\n");
            content.append("                                                    </tbody>\n");
            content.append("                                                </table>\n");
            content.append("                                            </div>\n");
            content.append("                                            <div id=\"wrapperbottom\"></div>\n");
            content.append("                                        </td>\n");
            content.append("                                    </tr>\n");
            content.append("                                    <tr>\n");
            content.append("                                        <td>&nbsp;</td>\n");
            content.append("                                    </tr>\n");
        }
        content.append("                                </tbody>\n");
        content.append("                            </table>\n");
        content.append("                        </td>\n");
        content.append("                        <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n");
        content.append("                        <td style=\"width: 20%; text-align: left; vertical-align: top\">\n");
        content.append("                            <div id=\"wrappertop\"></div>\n");
        content.append("                            <div id=\"wrappermiddle\">\n");
        content.append("                                <table style=\"width: 100%\">\n");
        content.append("                                    <tr>\n");
        content.append("                                        <td style=\"text-align: center\">"
                + Constants.SHOPPING_CART + " (" + shoppingCart.size() + ") </td>\n");
        content.append("                                    </tr>\n");
        if (!shoppingCart.isEmpty()) {
            // TODO: exercise 8
            double shoppingCartValue = 0.0;
            content.append("                                    <tr>\n");
            content.append("                                        <td>\n");
            content.append(
                    "                                            <table border=\"0\" cellpadding=\"4\" cellspacing=\"1\" style=\"width: 100%; background: #ffffff;\">\n");
            content.append("                                                <tbody>\n");
            for (Record shoppingCartContent : shoppingCart) {
                long identifier = Long.parseLong(shoppingCartContent.getAttribute());
                double currentBookValue = bookPresentationManager.getPrice(identifier);
                currentBookValue *= Integer.parseInt(shoppingCartContent.getValue().toString());
                content.append(
                        "                                                    <tr style=\"background: #ebebeb;\">\n");
                List<String> information = bookPresentationManager.getInformation(identifier);
                String display = information.get(0) + "<br/>&nbsp;&nbsp;&nbsp;&nbsp;(" + information.get(1) + ", "
                        + information.get(2) + ")<br/>&nbsp;&nbsp;&nbsp;&nbsp";
                content.append("                                                        <td>"
                        + shoppingCartContent.getValue() + " x " + display + "= " + new DecimalFormat("#.##").format(currentBookValue) + "</td>\n");
                content.append("                                                    </tr>\n");
                shoppingCartValue += currentBookValue;
            }
            content.append(
                    "                                                    <tr style=\"background: #ebebeb;\"><td></td></tr>\n");
            content.append("                                                    <tr style=\"background: #ebebeb;\"><td>"
                    + Constants.ORDER_TOTAL + "<b>" + new DecimalFormat("#.##").format(shoppingCartValue) + "</b></td></tr>\n");
            content.append("                                                </tbody>\n");
            content.append("                                            </table>\n");
            content.append("                                        </td>\n");
            content.append("                                    </tr>\n");
            // TODO: exercise 9
            content.append("                                    <tr>\n");
            content.append("                                        <td style=\"text-align: center\">\n");
            content.append("                                            <input type=\"image\" name=\""
                    + Utilities.removeSpaces(Constants.CANCEL_COMMAND.toLowerCase()) + "\" value=\""
                    + Constants.CANCEL_COMMAND
                    + "\" src=\"./images/user_interface/remove_from_shopping_cart.png\" />\n");
            content.append("                                            &nbsp;&nbsp;\n");
            content.append("                                            <input type=\"image\" name=\""
                    + Utilities.removeSpaces(Constants.COMPLETE_COMMAND.toLowerCase()) + "\" value=\""
                    + Constants.COMPLETE_COMMAND + "\" src=\"./images/user_interface/shopping_cart_accept.png\" />\n");
            content.append("                                        </td>\n");
            content.append("                                    </tr>\n");
        } else {
            content.append("                                    <tr>\n");
            content.append("                                        <td style=\"text-align: center;\">"
                    + Constants.EMPTY_CART + "</td>\n");
            content.append("                                    </tr>\n");
        }
        content.append("                                </table>\n");
        content.append("                            </div>\n");
        content.append("                            <div id=\"wrapperbottom\"></div>\n");
        content.append("                        </td>\n");
        content.append("                    </tr>\n");
        content.append("                </tbody>\n");
        content.append("            </table>\n");
        content.append("        </form>\n");
        content.append("    </body>\n");
        content.append("</html>");
        printWriter.println(content.toString());
    }
}
