/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo.seguridad;

import Controlador.seguridad.Bitacora;
import Modelo.Conexion;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author visitante
 */
public class BitacoraDAO {

    private static final String SQL_SELECT = "SELECT id_bitacora, id_usuario, id_aplicacion, fecha, ip , accion FROM bitacora";
    private static final String SQL_INSERT = "INSERT INTO bitacora(id_usuario, id_aplicacion, fecha, ip, accion) VALUES(?, ? , NOW(), ?, ?)";
    private static final String SQL_QUERY = "SELECT id_bitacora, id_usuario, id_aplicacion, fecha, ip, accion FROM bitacora WHERE id_bitacora = ?";
    
    // Método para registrar una acción en la bitácora
    public void registrarAccionEnBitacora(int idUsuario, int idAplicacion, String accion) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setInt(1, idUsuario); // ID del usuario que realiza la acción
            stmt.setInt(2, idAplicacion);
            stmt.setString(3, obtenerIP()); // IP de la computadora desde donde se realiza la acción
            stmt.setString(4, accion); // Descripción de la acción (e.g., "ins alum", "upd alum")

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
    }

    // Método para obtener la IP de la máquina
    public String obtenerIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "0.0.0.0"; // Valor por defecto si no se puede obtener la IP
        }
    }
    
    public List<Bitacora> select() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Bitacora bitacora = null;
        List<Bitacora> bitacoras = new ArrayList<Bitacora>();

        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int idBitacora = rs.getInt("id_bitacora");
                int idUsuario = rs.getInt("id_usuario");
                int idAplicacion = rs.getInt("id_aplicacion");
                String Fecha = rs.getString("fecha");
                String Ip = rs.getString("ip");
                String Accion = rs.getString("accion");
                
                bitacora = new Bitacora();
                bitacora.setIdBitacora(idBitacora);
                bitacora.setIdUsuario(idUsuario);
                bitacora.setIdAplicacion(idAplicacion);
                bitacora.setFecha(Fecha);
                bitacora.setIp(Ip);
                bitacora.setAccion(Accion);
                
                bitacoras.add(bitacora);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return bitacoras;
    }

    public int insert(Bitacora bitacoras) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setInt(1, bitacoras.getIdUsuario());
            stmt.setInt(2, bitacoras.getIdAplicacion());
            stmt.setString(3, bitacoras.getIp()); // Recibe la IP directamente desde la capa vista
            stmt.setString(4, bitacoras.getAccion());

            System.out.println("ejecutando query:" + SQL_INSERT);
            rows = stmt.executeUpdate();
            System.out.println("Registros afectados:" + rows);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        return rows;
    }


    public Bitacora query(Bitacora bitacora) {    
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int rows = 0;

        try {
            conn = Conexion.getConnection();
            System.out.println("Ejecutando query:" + SQL_QUERY);
            stmt = conn.prepareStatement(SQL_QUERY);
            stmt.setInt(1, bitacora.getIdBitacora());
            rs = stmt.executeQuery();
            while (rs.next()) {
                int idBitacora = rs.getInt("id_bitacora");
                int idUsuario = rs.getInt("id_usuario");
                int idAplicacion = rs.getInt("id_aplicacion");
                String Fecha = rs.getString("fecha");
                String Ip = rs.getString("ip");
                String Accion = rs.getString("accion");
                
                bitacora = new Bitacora();
                bitacora.setIdBitacora(idBitacora);
                bitacora.setIdUsuario(idUsuario);
                bitacora.setIdAplicacion(idAplicacion);
                bitacora.setFecha(Fecha);
                bitacora.setIp(Ip);
                bitacora.setAccion(Accion);
                
            }
            //System.out.println("Registros buscado:" + vendedor);
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }

        //return vendedores;  // Si se utiliza un ArrayList
        return bitacora;
    }
    
}