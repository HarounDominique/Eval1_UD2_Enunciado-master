package es.teis.model.dao.partido;

import es.teis.model.Partido;
import es.teis.model.dao.IGenericDao;

public interface IPartidoDao extends IGenericDao<Partido> {
    Partido create(Partido partido);

    boolean comprobarExistencia(String nombre);

    boolean transferirVotos(String nombreOrigen, String nombreDestino, int cantidadVotos);
}
