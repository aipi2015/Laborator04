package ro.pub.cs.aipi.lab04.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ro.pub.cs.aipi.lab04.businesslogic.BookManager;
import ro.pub.cs.aipi.lab04.businesslogic.BookPresentationManager;
import ro.pub.cs.aipi.lab04.businesslogic.InvoiceHeaderManager;
import ro.pub.cs.aipi.lab04.businesslogic.InvoiceLineManager;
import ro.pub.cs.aipi.lab04.businesslogic.UserManager;
import ro.pub.cs.aipi.lab04.general.Constants;
import ro.pub.cs.aipi.lab04.general.Utilities;
import ro.pub.cs.aipi.lab04.graphicuserinterface.ClientGraphicUserInterface;
import ro.pub.cs.aipi.lab04.helper.Record;

public class ClientServlet extends HttpServlet {

    final public static long serialVersionUID = 10021002L;

    private BookManager bookManager;
    private BookPresentationManager bookPresentationManager;
    private UserManager userManager;
    private InvoiceHeaderManager invoiceHeaderManager;
    private InvoiceLineManager invoiceLineManager;

    private List<Record> shoppingCart;
    private List<String> formatsFilter;
    private List<String> languagesFilter;
    private List<String> categoriesFilter;
    private String previousRecordsPerPage;
    private String currentRecordsPerPage;
    private String currentPage;

    private List<List<Record>> books;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bookManager = new BookManager();
        bookPresentationManager = new BookPresentationManager();
        userManager = new UserManager();
        invoiceHeaderManager = new InvoiceHeaderManager();
        invoiceLineManager = new InvoiceLineManager();
        previousRecordsPerPage = String.valueOf(Constants.RECORDS_PER_PAGE_VALUES[0]);
        currentRecordsPerPage = String.valueOf(Constants.RECORDS_PER_PAGE_VALUES[0]);
        currentPage = String.valueOf(1);
        books = null;
    }

    @Override
    public void destroy() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session == null || session.getAttribute(Constants.DISPLAY) == null) {
            return;
        }
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            String display = session.getAttribute(Constants.DISPLAY).toString();
            shoppingCart = (List<Record>) session
                    .getAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()));
            if (shoppingCart == null) {
                shoppingCart = new ArrayList<>();
            }
            formatsFilter = (List<String>) session.getAttribute(Constants.FORMATS_FILTER);
            if (formatsFilter == null) {
                formatsFilter = new ArrayList<>();
            }
            languagesFilter = (List<String>) session.getAttribute(Constants.LANGUAGES_FILTER);
            if (languagesFilter == null) {
                languagesFilter = new ArrayList<>();
            }
            categoriesFilter = (List<String>) session.getAttribute(Constants.CATEGORIES_FILTER);
            if (categoriesFilter == null) {
                categoriesFilter = new ArrayList<>();
            }
            String errorMessage = "";
            boolean filterChange = false;
            Enumeration<String> parameters = request.getParameterNames();
            while (parameters.hasMoreElements()) {
                String parameter = (String) parameters.nextElement();

                // TODO: exercise 7
                if (parameter.equals(Utilities.removeSpaces(Constants.RECORDS_PER_PAGE.toLowerCase().trim()))) {
                    currentRecordsPerPage = request.getParameter(parameter);
                }
                if (parameter.equals(Utilities.removeSpaces(Constants.PAGE.toLowerCase().trim()))) {
                    currentPage = request.getParameter(parameter);
                }
                if (parameter.equals(Constants.INSERT_BUTTON_NAME.toLowerCase() + "_" + Constants.FORMAT + ".x")) {
                    String format = request.getParameter(Constants.CURRENT_FORMAT);
                    if (!formatsFilter.contains(format)) {
                        formatsFilter.add(format);
                        filterChange = true;
                    }
                }
                if (parameter.startsWith(Constants.DELETE_BUTTON_NAME.toLowerCase() + "_" + Constants.FORMAT + "_")
                        && parameter.endsWith(".x")) {
                    String format = parameter.substring(parameter.lastIndexOf("_") + 1, parameter.indexOf(".x"));
                    if (formatsFilter.contains(format)) {
                        formatsFilter.remove(format);
                        filterChange = true;
                    }
                }
                if (parameter.equals(Constants.INSERT_BUTTON_NAME.toLowerCase() + "_" + Constants.LANGUAGE + ".x")) {
                    String language = request.getParameter(Constants.CURRENT_LANGUAGE);
                    if (!languagesFilter.contains(language)) {
                        languagesFilter.add(language);
                        filterChange = true;
                    }
                }
                if (parameter.startsWith(Constants.DELETE_BUTTON_NAME.toLowerCase() + "_" + Constants.LANGUAGE + "_")
                        && parameter.endsWith(".x")) {
                    String language = parameter.substring(parameter.lastIndexOf("_") + 1, parameter.indexOf(".x"));
                    if (languagesFilter.contains(language)) {
                        languagesFilter.remove(language);
                        filterChange = true;
                    }
                }
                if (parameter.equals(Constants.INSERT_BUTTON_NAME.toLowerCase() + "_" + Constants.CATEGORY + ".x")) {
                    String category = request.getParameter(Constants.CURRENT_CATEGORY);
                    if (!categoriesFilter.contains(category)) {
                        categoriesFilter.add(category);
                        filterChange = true;
                    }
                }
                if (parameter.startsWith(Constants.DELETE_BUTTON_NAME.toLowerCase() + "_" + Constants.CATEGORY + "_")
                        && parameter.endsWith(".x")) {
                    String category = parameter.substring(parameter.lastIndexOf("_") + 1, parameter.indexOf(".x"));
                    if (categoriesFilter.contains(category)) {
                        categoriesFilter.remove(category);
                        filterChange = true;
                    }
                }

                if (parameter
                        .startsWith(Constants.INSERT_BUTTON_NAME.toLowerCase() + "_"
                                + Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()) + "_")
                        && parameter.endsWith(".x")) {
                    String bookPresentationId = parameter.substring(parameter.lastIndexOf("_") + 1,
                            parameter.indexOf(".x"));
                    String quantity = request.getParameter(Constants.COPIES.toLowerCase() + "_"
                            + Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()) + "_" + bookPresentationId);
                    boolean found = false;
                    for (Record shoppingCartRecord : shoppingCart) {
                        if (shoppingCartRecord.getAttribute().equals(bookPresentationId)) {
                            found = true;
                            if (Integer.parseInt(quantity) == 0) {
                                shoppingCart.remove(shoppingCartRecord);
                            } else {
                                shoppingCartRecord.setValue(quantity);
                            }
                            break;
                        }
                    }
                    if (!found && Integer.parseInt(quantity) != 0) {
                        shoppingCart.add(new Record(bookPresentationId, quantity));
                    }
                }

                // TODO: exercise 10
                if (parameter.equals(Utilities.removeSpaces(Constants.COMPLETE_COMMAND.toLowerCase()) + ".x")) {
                    List<String> invoiceHeaderValues = new ArrayList<>();
                    invoiceHeaderValues.add(Utilities.generateIdentificationNumber(3, 6));
                    invoiceHeaderValues.add(LocalDate.now().toString());
                    invoiceHeaderValues.add(Constants.STATE_ISSUED);
                    invoiceHeaderValues.add(String.valueOf(userManager.getIdentifier(display)));
                    long invoiceHeaderId = invoiceHeaderManager.create(invoiceHeaderValues);
                    for (Record shoppingCartRecord : shoppingCart) {
                        long bookPresentationId = Long.parseLong(shoppingCartRecord.getAttribute());
                        int quantity = Integer.parseInt(shoppingCartRecord.getValue().toString());
                        int stockpile = bookPresentationManager.getStockpile(bookPresentationId);
                        if (quantity <= stockpile) {
                            List<String> bookPresentationAttributes = new ArrayList<>();
                            bookPresentationAttributes.add(Constants.STOCKPILE_ATTRIBUTE);
                            List<String> bookPresentationValues = new ArrayList<>();
                            bookPresentationValues.add(String.valueOf(stockpile - quantity));
                            bookPresentationManager.update(bookPresentationAttributes, bookPresentationValues,
                                    bookPresentationId);
                            List<String> invoiceLineValues = new ArrayList<>();
                            invoiceLineValues.add(String.valueOf(invoiceHeaderId));
                            invoiceLineValues.add(String.valueOf(bookPresentationId));
                            invoiceLineValues.add(String.valueOf(quantity));
                            invoiceLineManager.create(invoiceLineValues);
                        } else {
                            errorMessage += "<span style=\"color: #ff0000;\">" + Constants.INVALID_COMMAND_ERROR1
                                    + bookPresentationId + Constants.INVALID_COMMAND_ERROR2 + "</span><br/>";
                        }
                    }
                    shoppingCart = new ArrayList<>();
                }

                // TODO: exercise 11
                if (parameter.equals(Utilities.removeSpaces(Constants.CANCEL_COMMAND.toLowerCase()) + ".x")) {
                    shoppingCart = new ArrayList<>();
                }

                session.setAttribute(Utilities.removeSpaces(Constants.SHOPPING_CART.toLowerCase()), shoppingCart);
                session.setAttribute(Constants.FORMATS_FILTER, formatsFilter);
                session.setAttribute(Constants.LANGUAGES_FILTER, languagesFilter);
                session.setAttribute(Constants.CATEGORIES_FILTER, categoriesFilter);
                if (parameter.equals(Constants.SIGNOUT.toLowerCase() + ".x")) {
                    Enumeration<String> requestParameters = request.getParameterNames();
                    while (requestParameters.hasMoreElements()) {
                        request.removeAttribute(requestParameters.nextElement());
                    }
                    shoppingCart.clear();
                    formatsFilter.clear();
                    languagesFilter.clear();
                    categoriesFilter.clear();
                    previousRecordsPerPage = String.valueOf(Constants.RECORDS_PER_PAGE_VALUES[0]);
                    currentRecordsPerPage = String.valueOf(Constants.RECORDS_PER_PAGE_VALUES[0]);
                    currentPage = String.valueOf(1);
                    books = null;
                    RequestDispatcher dispatcher = getServletContext()
                            .getRequestDispatcher("/" + Constants.LOGIN_SERVLET_PAGE_CONTEXT);
                    if (dispatcher != null) {
                        dispatcher.forward(request, response);
                    }
                    session.invalidate();
                    break;
                }
            }
            if (books == null || filterChange) {
                books = bookManager.getCollection(formatsFilter, languagesFilter, categoriesFilter);
            }
            response.setContentType("text/html");
            ClientGraphicUserInterface.displayClientGraphicUserInterface(display, books, errorMessage, shoppingCart,
                    formatsFilter, languagesFilter, categoriesFilter,
                    (currentRecordsPerPage != null && !filterChange) ? Integer.parseInt(currentRecordsPerPage)
                            : Constants.RECORDS_PER_PAGE_VALUES[0],
                    (currentPage != null && !filterChange && currentRecordsPerPage != null
                    && currentRecordsPerPage.equals(previousRecordsPerPage)) ? Integer.parseInt(currentPage)
                    : 1,
                    filterChange, printWriter);
            previousRecordsPerPage = currentRecordsPerPage;
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
