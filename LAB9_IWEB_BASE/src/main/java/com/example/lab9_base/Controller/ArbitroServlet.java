package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoArbitros;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        DaoArbitros daoArbitros = new DaoArbitros();
        RequestDispatcher view;

        switch (action) {
            case "lista":
                ArrayList<Arbitro> arbitros = daoArbitros.listarArbitros();
                request.setAttribute("arbitros", arbitros);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            case "borrar":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    daoArbitros.borrarArbitro(id);
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                } catch (SQLException | NumberFormatException e) {
                    e.printStackTrace();
                    request.setAttribute("error", "No se pudo borrar el árbitro. Inténtalo de nuevo.");
                    view = request.getRequestDispatcher("/arbitros/list.jsp");
                    view.forward(request, response);
                }
                break;

            case "crear":
                // Cargar lista de países en el formulario vacío
                ArrayList<String> paises = new ArrayList<>();
                paises.add("Peru");
                paises.add("Chile");
                paises.add("Argentina");
                paises.add("Paraguay");
                paises.add("Uruguay");
                paises.add("Colombia");
                request.setAttribute("paises", paises);
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        DaoArbitros daoArbitros = new DaoArbitros();

        switch (action) {
            case "guardar":
                String nombre = request.getParameter("nombre");
                String pais = request.getParameter("pais");

                // Validación: No permitir campos vacíos
                if (nombre == null || nombre.trim().isEmpty() || pais == null || pais.trim().isEmpty()) {
                    request.setAttribute("error", "Todos los campos son obligatorios.");

                    // Cargar lista de países en caso de error
                    ArrayList<String> paises = new ArrayList<>();
                    paises.add("Peru");
                    paises.add("Chile");
                    paises.add("Argentina");
                    paises.add("Paraguay");
                    paises.add("Uruguay");
                    paises.add("Colombia");

                    request.setAttribute("paises", paises);

                    // Volver a mostrar el formulario vacío
                    RequestDispatcher view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                    return;
                }

                Arbitro nuevoArbitro = new Arbitro();
                nuevoArbitro.setNombre(nombre);
                nuevoArbitro.setPais(pais);

                try {
                    // Intentar guardar el árbitro
                    daoArbitros.crearArbitro(nuevoArbitro);
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                } catch (SQLException e) {
                    // Si el árbitro ya existe, no se guardan los valores en el formulario
                    request.setAttribute("error", "El nombre del árbitro ya existe.");

                    // Cargar lista de países en caso de error
                    ArrayList<String> paises = new ArrayList<>();
                    paises.add("Peru");
                    paises.add("Chile");
                    paises.add("Argentina");
                    paises.add("Paraguay");
                    paises.add("Uruguay");
                    paises.add("Colombia");

                    request.setAttribute("paises", paises);

                    // Volver a mostrar el formulario vacío
                    RequestDispatcher view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                }
                break;

            case "buscar":
                String tipo = request.getParameter("tipo");
                String criterio = request.getParameter("buscar");
                ArrayList<Arbitro> arbitros;

                if ("nombre".equals(tipo)) {
                    arbitros = daoArbitros.busquedaNombre(criterio);
                } else {
                    arbitros = daoArbitros.busquedaPais(criterio);
                }

                request.setAttribute("arbitros", arbitros);
                request.setAttribute("opciones", new String[]{"nombre", "pais"});
                RequestDispatcher view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;
        }
    }
}
