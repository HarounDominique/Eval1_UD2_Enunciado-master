/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.teis.model.servicio;

import es.teis.model.Partido;
import es.teis.model.dao.partido.IPartidoDao;

import java.util.ArrayList;

/**
 *
 * @author maria
 */
public class PartidoServicio implements IPartidoServicio {

    private IPartidoDao partidoDao;

    public PartidoServicio(IPartidoDao partidoDao) {
        this.partidoDao = partidoDao;
    }

    public void crearPartidos(ArrayList<Partido> partidos) {
        Partido creado = null;
        for (Partido p : partidos) {
            creado = partidoDao.create(p);
            System.out.println("Se ha creado un partido con id: " + creado.getId());
        }
    }

    @Override
    public boolean transferirVotos(String nombreOrigen, String nombreDestino, int cantidadVotos) {

        boolean exito = false;
        Connection con = null;
        PreparedStatement updateOrigen = null;
        PreparedStatement updateDestino = null;
        try {
            con = this.dataSource.getConnection();

            updateOrigen = con.prepareStatement("UPDATE [dbo].[PARTIDO]\n"
                    + "   SET [numero_votos] = (numero_votos - ?) \n"
                    + " WHERE nombre = ?");
            updateDestino = con.prepareStatement("UPDATE [dbo].[PARTIDO]\n"
                    + "   SET [numero_votos] = (numero_votos + ?) \n"
                    + " WHERE nombre = ?");

            updateOrigen.setInt(1, cantidadVotos);
            updateOrigen.setString(2, nombreOrigen);
            updateOrigen.executeUpdate();

            updateDestino.setInt(1, cantidadVotos);
            updateDestino.setString(2, nombreDestino);
            updateDestino.executeUpdate();

            exito = true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha habido una excepción. " + ex.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.err.println("Ha habido una excepción cerrando la conexión: " + ex.getMessage());
                }
            }
        }
        return exito;
    }

}
