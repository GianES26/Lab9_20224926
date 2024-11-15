package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Estadio;
import com.example.lab9_base.Bean.Seleccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoSelecciones extends DaoBase {

    public ArrayList<Seleccion> listarSelecciones() {
        ArrayList<Seleccion> selecciones = new ArrayList<>();

        String sql = "SELECT s.idSeleccion, s.nombre, s.tecnico, " +
                "e.idEstadio, e.nombre AS nombreEstadio, e.provincia, e.club " +
                "FROM seleccion s " +
                "JOIN estadio e ON s.estadio_idEstadio = e.idEstadio";

        try (Connection connection = this.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Seleccion seleccion = new Seleccion();
                seleccion.setIdSeleccion(rs.getInt("idSeleccion"));
                seleccion.setNombre(rs.getString("nombre"));
                seleccion.setTecnico(rs.getString("tecnico"));

                Estadio estadio = new Estadio();
                estadio.setIdEstadio(rs.getInt("idEstadio"));
                estadio.setNombre(rs.getString("nombreEstadio"));
                estadio.setProvincia(rs.getString("provincia"));
                estadio.setClub(rs.getString("club"));

                seleccion.setEstadio(estadio);
                selecciones.add(seleccion);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return selecciones;
    }
}
