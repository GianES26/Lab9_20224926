package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Estadio;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoPartidos extends DaoBase {

    public ArrayList<Partido> listaDePartidos() {
        ArrayList<Partido> partidos = new ArrayList<>();

        String sql = "SELECT p.idPartido, p.fecha, p.numeroJornada, " +
                "selLocal.idSeleccion AS idLocal, selLocal.nombre AS nombreLocal, " +
                "selVisitante.idSeleccion AS idVisitante, selVisitante.nombre AS nombreVisitante, " +
                "a.idArbitro, a.nombre AS nombreArbitro, " +
                "e.idEstadio, e.nombre AS nombreEstadio " +
                "FROM partido p " +
                "JOIN seleccion selLocal ON p.seleccionLocal = selLocal.idSeleccion " +
                "JOIN seleccion selVisitante ON p.seleccionVisitante = selVisitante.idSeleccion " +
                "JOIN arbitro a ON p.arbitro = a.idArbitro " +
                "JOIN estadio e ON selLocal.estadio_idEstadio = e.idEstadio";

        try (Connection connection = this.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Partido partido = new Partido();

                partido.setIdPartido(rs.getInt("idPartido"));
                partido.setFecha(rs.getString("fecha"));
                partido.setNumeroJornada(rs.getInt("numeroJornada"));

                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(rs.getInt("idLocal"));
                seleccionLocal.setNombre(rs.getString("nombreLocal"));
                Estadio estadio = new Estadio();
                estadio.setIdEstadio(rs.getInt("idEstadio"));
                estadio.setNombre(rs.getString("nombreEstadio"));
                seleccionLocal.setEstadio(estadio);
                partido.setSeleccionLocal(seleccionLocal);

                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(rs.getInt("idVisitante"));
                seleccionVisitante.setNombre(rs.getString("nombreVisitante"));
                partido.setSeleccionVisitante(seleccionVisitante);

                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombreArbitro"));
                partido.setArbitro(arbitro);

                partidos.add(partido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return partidos;
    }

    public void crearPartido(Partido partido) {
        String verificarSQL = "SELECT COUNT(*) FROM partido WHERE " +
                "(seleccionLocal = ? AND seleccionVisitante = ?) OR " +
                "(seleccionLocal = ? AND seleccionVisitante = ?)";
        String insertarSQL = "INSERT INTO partido (seleccionLocal, seleccionVisitante, arbitro, fecha, numeroJornada) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = this.getConnection();
             PreparedStatement verificarStmt = connection.prepareStatement(verificarSQL);
             PreparedStatement insertarStmt = connection.prepareStatement(insertarSQL)) {

            verificarStmt.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            verificarStmt.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            verificarStmt.setInt(3, partido.getSeleccionVisitante().getIdSeleccion());
            verificarStmt.setInt(4, partido.getSeleccionLocal().getIdSeleccion());

            try (ResultSet rs = verificarStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Un partido entre estas selecciones ya existe en la base de datos.");
                }
            }

            if (partido.getSeleccionLocal().getIdSeleccion() == partido.getSeleccionVisitante().getIdSeleccion()) {
                throw new SQLException("La selección local y la visitante no pueden ser la misma.");
            }

            insertarStmt.setInt(1, partido.getSeleccionLocal().getIdSeleccion());
            insertarStmt.setInt(2, partido.getSeleccionVisitante().getIdSeleccion());
            insertarStmt.setInt(3, partido.getArbitro().getIdArbitro());
            insertarStmt.setString(4, partido.getFecha());
            insertarStmt.setInt(5, partido.getNumeroJornada());

            insertarStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
