package ro.pub.cs.aipi.lab04.servlets;

import java.io.IOException;
import java.io.PrintWriter;
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

import ro.pub.cs.aipi.lab04.businesslogic.EntityManager;
import ro.pub.cs.aipi.lab04.general.Constants;
import ro.pub.cs.aipi.lab04.graphicuserinterface.AdministratorGraphicUserInterface;
import ro.pub.cs.aipi.lab04.helper.Record;

public class AdministratorServlet extends HttpServlet {

    public final static long serialVersionUID = 10011001L;

    private EntityManager entityManager;

    private List<String> databaseStructure;
    private String currentTable;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        entityManager = new EntityManager();
        databaseStructure = entityManager.getDatabaseStructure();
        currentTable = databaseStructure.get(0);
        entityManager.setTable(currentTable);
    }

    @Override
    public void destroy() {
    }

    public List<String> getAttributes(String table, List<Record> records) {
        List<String> structure = entityManager.getStructure();
        if (structure == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (String attribute : structure) {
            for (Record record : records) {
                if (attribute.equals(record.getAttribute())) {
                    result.add(record.getAttribute());
                }
            }
        }
        return result;
    }

    public List<String> getValues(String table, List<Record> records) {
        List<String> result = new ArrayList<>();
        List<String> attributes = getAttributes(table, records);
        for (String attribute : attributes) {
            for (Record record : records) {
                if (attribute.equals(record.getAttribute())) {
                    String value = record.getValue().toString();
                    if (value == null || value.isEmpty()) {
                        value = Constants.INVALID_VALUE;
                    }
                    result.add(value);
                }
            }
        }
        return result;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Record> insertRecords = new ArrayList<>();
        ArrayList<Record> updateRecords = new ArrayList<>();
        ArrayList<Record> deleteRecords = new ArrayList<>();
        ArrayList<Record> genericRecords = new ArrayList<>();
        Enumeration<String> parameters = request.getParameterNames();
        int operation = Constants.OPERATION_NONE;
        String identifier = entityManager.getIdentifier();
        String identifierValue = new String();

        while (parameters.hasMoreElements()) {
            String parameter = (String) parameters.nextElement();
            if (parameter.equals(Constants.SIGNOUT.toLowerCase() + ".x")) {
                operation = Constants.OPERATION_LOGOUT;
            }
            if (parameter.equals(Constants.INSERT_BUTTON_NAME.toLowerCase() + ".x")) {
                operation = Constants.OPERATION_INSERT;
            } else if (parameter.contains(Constants.UPDATE_BUTTON_NAME.toLowerCase() + "1_")
                    && parameter.contains(".x")) {
                operation = Constants.OPERATION_UPDATE_PHASE1;
                identifierValue = parameter.substring(parameter.indexOf("_") + 1, parameter.indexOf(".x"));
            } else if (parameter.contains(Constants.UPDATE_BUTTON_NAME.toLowerCase() + "2_")
                    && parameter.contains(".x")) {
                operation = Constants.OPERATION_UPDATE_PHASE2;
                identifierValue = parameter.substring(parameter.indexOf("_") + 1, parameter.indexOf(".x"));
            } else if (parameter.contains(Constants.DELETE_BUTTON_NAME.toLowerCase()) && parameter.contains(".x")) {
                operation = Constants.OPERATION_DELETE;
                identifierValue = parameter.substring(parameter.indexOf("_") + 1, parameter.indexOf(".x"));
                deleteRecords.add(new Record(identifier, identifierValue));
            } else {
                genericRecords.add(new Record(parameter, request.getParameter(parameter)));
            }
            if (parameter.equals(Constants.CURRENT_TABLE)) {
                currentTable = request.getParameter(parameter);
                entityManager.setTable(currentTable);
            }
        }
        response.setContentType("text/html");
        try (PrintWriter printWriter = new PrintWriter(response.getWriter())) {
            switch (operation) {
                case Constants.OPERATION_INSERT:
                    for (Record record : genericRecords) {
                        String attribute = record.getAttribute();
                        String value = record.getValue().toString();
                        if (attribute.endsWith("_" + Constants.INSERT_BUTTON_NAME.toLowerCase())) {
                            insertRecords.add(new Record(attribute.substring(0, attribute.lastIndexOf("_")), value));
                        }
                    }
                    entityManager.create(getValues(currentTable, insertRecords));
                    break;
                case Constants.OPERATION_UPDATE_PHASE2:
                    for (Record record : genericRecords) {
                        String attribute = record.getAttribute();
                        String value = record.getValue().toString();
                        if (attribute.endsWith("_" + identifierValue) && !attribute.startsWith(identifier)) {
                            updateRecords.add(new Record(attribute.substring(0, attribute.lastIndexOf("_")), value));
                        }
                    }
                    updateRecords.add(new Record(identifier, identifierValue));
                    entityManager.update(getValues(currentTable, updateRecords), Long.parseLong(identifierValue));
                    break;
                case Constants.OPERATION_DELETE:
                    entityManager.delete(Long.parseLong(identifierValue));
                    break;
                case Constants.OPERATION_LOGOUT:
                    Enumeration<String> requestParameters = request.getParameterNames();
                    while (requestParameters.hasMoreElements()) {
                        request.removeAttribute(requestParameters.nextElement());
                    }
                    RequestDispatcher dispatcher = getServletContext()
                            .getRequestDispatcher("/" + Constants.LOGIN_SERVLET_PAGE_CONTEXT);
                    if (dispatcher != null) {
                        dispatcher.forward(request, response);
                    }
                    break;
            }

            HttpSession session = request.getSession(true);
            if (session == null || session.getAttribute(Constants.DISPLAY) == null) {
                return;
            }
            String username = session.getAttribute(Constants.DISPLAY).toString();
            String identifierValueOfRecordToBeUpdated = null;
            if (operation == Constants.OPERATION_UPDATE_PHASE1) {
                identifierValueOfRecordToBeUpdated = identifierValue;
            }
            AdministratorGraphicUserInterface.displayAdministratorGraphicUserInterface(username, databaseStructure,
                    currentTable, identifierValueOfRecordToBeUpdated, printWriter);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
