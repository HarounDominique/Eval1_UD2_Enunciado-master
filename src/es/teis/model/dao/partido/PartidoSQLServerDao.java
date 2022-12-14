package es.teis.model.dao.partido;

import es.teis.db.DBCPDataSourceFactory;
import es.teis.model.Partido;
import es.teis.model.dao.AbstractGenericDao;

import javax.sql.DataSource;
import java.sql.*;

public class PartidoSQLServerDao extends AbstractGenericDao<Partido> implements IPartidoDao {
    private DataSource dataSource;
    public PartidoSQLServerDao() {
        this.dataSource = DBCPDataSourceFactory.getDataSource();
    }

    @Override
    public Partido create(Partido entity) {
        try (
                Connection conexion = this.dataSource.getConnection();
                PreparedStatement pstmt = conexion.prepareStatement(
                        "INSERT INTO [dbo].[PARTIDO]\n"
                                + "           ([nombre]\n"
                                + "           ,[porcentaje])\n"
                                + "           ,[numero_votos])\n"
                                + "     VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS
                );) {

            pstmt.setString(1, entity.getNombre());
            pstmt.setFloat(2, entity.getPorcentaje());
            pstmt.setInt(3, entity.getVotos());

            // Devolverá 0 para las sentencias SQL que no devuelven nada o el número de filas afectadas
            int result = pstmt.executeUpdate();

            ResultSet clavesResultado = pstmt.getGeneratedKeys();

            if (clavesResultado.next()) {
                int partidoId = clavesResultado.getInt(1);
                entity.setId(partidoId);
            } else {
                entity = null;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
            entity = null;
        }
        return entity;
    }

    @Override
    public boolean comprobarExistencia(String nombre) {
        boolean existe = false;
        try (
                Connection conexion = this.dataSource.getConnection();
                PreparedStatement pstmt = conexion.prepareStatement(
                        "SELECT * FROM [dbo].[PARTIDO]\n"
                                + "   WHERE nombre = (?)\n"
                );) {

            pstmt.setString(1, nombre);

            // Devolverá 0 para las sentencias SQL que no devuelven nada o el número de filas afectadas
            int result = pstmt.executeUpdate();

            if (result !=0) {
                existe = true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
        }
        return existe;
    }

    @Override
    public boolean transferirVotos(String nombreOrigen, String nombreDestino, int cantidadVotos) {
        return false;
    }

    @Override
    public Partido read(int id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean update(Partido entity) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(int id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
