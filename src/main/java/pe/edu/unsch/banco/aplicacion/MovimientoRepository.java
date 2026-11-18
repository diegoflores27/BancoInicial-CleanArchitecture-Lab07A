package pe.edu.unsch.banco.aplicacion;

/** Puerto de salida: el caso de uso no depende de archivos ni de una BD concreta. */
public interface MovimientoRepository {
    void guardar(String movimiento);
}
