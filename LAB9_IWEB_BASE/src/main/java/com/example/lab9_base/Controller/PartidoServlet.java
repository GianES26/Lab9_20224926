package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoPartidos;
import com.example.lab9_base.Dao.DaoSelecciones;
import com.example.lab9_base.Dao.DaoArbitros;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet", "/"})
public class PartidoServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");

        DaoPartidos daoPartidos = new DaoPartidos();

        if ("guardar".equals(action)) {
            try {

                int jornada = Integer.parseInt(request.getParameter("jornada"));
                String fecha = request.getParameter("fecha");
                int idLocal = Integer.parseInt(request.getParameter("local"));
                int idVisitante = Integer.parseInt(request.getParameter("visitante"));
                int idArbitro = Integer.parseInt(request.getParameter("arbitro"));

                if (idLocal == idVisitante) {
                    request.setAttribute("error", "La selección local y visitante no pueden ser la misma.");
                    response.sendRedirect("PartidoServlet?action=crear");
                    return;
                }

                Partido partido = new Partido();
                partido.setNumeroJornada(jornada);
                partido.setFecha(fecha);

                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(idLocal);
                partido.setSeleccionLocal(seleccionLocal);

                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(idVisitante);
                partido.setSeleccionVisitante(seleccionVisitante);

                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(idArbitro);
                partido.setArbitro(arbitro);

                daoPartidos.crearPartido(partido);
                response.sendRedirect("PartidoServlet?action=lista");

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("PartidoServlet?action=crear");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        DaoPartidos daoPartidos = new DaoPartidos();
        DaoSelecciones daoSelecciones = new DaoSelecciones();
        DaoArbitros daoArbitros = new DaoArbitros();
        RequestDispatcher view;

        switch (action) {
            case "lista":
                ArrayList<Partido> partidos = daoPartidos.listaDePartidos();
                request.setAttribute("partidos", partidos);
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                break;

            case "crear":
                ArrayList<Seleccion> selecciones = daoSelecciones.listarSelecciones();
                ArrayList<Arbitro> arbitros = daoArbitros.listarArbitros();
                request.setAttribute("selecciones", selecciones);
                request.setAttribute("arbitros", arbitros);
                view = request.getRequestDispatcher("/partidos/form.jsp");
                view.forward(request, response);
                break;

            default:
                response.sendRedirect("PartidoServlet?action=lista");
                break;
        }
    }
}
