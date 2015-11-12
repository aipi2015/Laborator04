package ro.pub.cs.aipi.lab04.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ro.pub.cs.aipi.lab04.businesslogic.BookManager;
import ro.pub.cs.aipi.lab04.general.Constants;
import ro.pub.cs.aipi.lab04.general.Utilities;
import ro.pub.cs.aipi.lab04.graphicuserinterface.ClientGraphicUserInterface;
import ro.pub.cs.aipi.lab04.helper.Record;

public class ClientServlet extends HttpServlet {

    final public static long serialVersionUID = 10021002L;

    private BookManager bookManager;

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

				// TODO: exercise 7
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

				// TODO: exercise 8
				// TODO: exercise 11
                session.setAttribute(Constants.FORMATS_FILTER, formatsFilter);
                session.setAttribute(Constants.LANGUAGES_FILTER, languagesFilter);
                session.setAttribute(Constants.CATEGORIES_FILTER, categoriesFilter);

				// TODO: exercise 12
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
