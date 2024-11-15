package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoArbitros extends DaoBase {

    public ArrayList<Arbitro> listarArbitros() {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT idArbitro, nombre, pais FROM arbitro";

        try (Connection connection = this.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arbitros;
    }

    public ArrayList<Arbitro> busquedaPais(String pais) {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT idArbitro, nombre, pais FROM arbitro WHERE pais LIKE ?";

        try (Connection connection = this.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, "%" + pais + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arbitros;
    }

    public ArrayList<Arbitro> busquedaNombre(String nombre) {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT idArbitro, nombre, pais FROM arbitro WHERE nombre LIKE ?";

        try (Connection connection = this.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombre"));
                arbitro.setPais(rs.getString("pais"));
                arbitros.add(arbitro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arbitros;
    }

    public void crearArbitro(Arbitro arbitro) throws SQLException {
        String sqlVerificar = "SELECT COUNT(*) FROM arbitro WHERE nombre = ?";
        String sqlInsertar = "INSERT INTO arbitro (nombre, pais) VALUES (?, ?)";

        try (Connection connection = this.getConnection();
             PreparedStatement verificarStmt = connection.prepareStatement(sqlVerificar);
             PreparedStatement insertarStmt = connection.prepareStatement(sqlInsertar)) {

            verificarStmt.setString(1, arbitro.getNombre());
            ResultSet rs = verificarStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("El nombre del árbitro ya existe en la base de datos.");
            }

            insertarStmt.setString(1, arbitro.getNombre());
            insertarStmt.setString(2, arbitro.getPais());
            insertarStmt.executeUpdate();
        }
    }

    //Nota: Para implementar este código, decidí que borrar a un arbitro borraba también los
    //partidos que estuvieran asociados a este. Si no lo hacía de esta forma alguno
    //árbitros serían borrados y otros no.
    //¿Se podría decir que es una especie de reprogramación del partido?
    public void borrarArbitro(int id) throws SQLException {
        String sqlEliminarPartidos = "DELETE FROM partido WHERE arbitro = ?";
        String sqlBorrarArbitro = "DELETE FROM arbitro WHERE idArbitro = ?";

        try (Connection connection = this.getConnection();
             PreparedStatement eliminarPartidosStmt = connection.prepareStatement(sqlEliminarPartidos);
             PreparedStatement borrarArbitroStmt = connection.prepareStatement(sqlBorrarArbitro)) {

            eliminarPartidosStmt.setInt(1, id);
            eliminarPartidosStmt.executeUpdate();

            borrarArbitroStmt.setInt(1, id);
            borrarArbitroStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al intentar borrar el árbitro con id " + id);
        }
    }


}
