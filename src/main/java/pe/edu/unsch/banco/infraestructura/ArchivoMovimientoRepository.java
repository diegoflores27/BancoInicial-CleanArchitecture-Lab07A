package pe.edu.unsch.banco.infraestructura;

import pe.edu.unsch.banco.aplicacion.MovimientoRepository;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/** Adaptador que almacena un historial de movimientos, no cuentas completas. */
public final class ArchivoMovimientoRepository implements MovimientoRepository {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Path archivo;

    public ArchivoMovimientoRepository(Path archivo) {
        this.archivo = Objects.requireNonNull(archivo, "Ruta de archivo obligatoria");
    }

    @Override
    public void guardar(String movimiento) {
        String linea = LocalDateTime.now().format(FORMATO) + " | " + movimiento
                + System.lineSeparator();
        try {
            Files.writeString(archivo, linea, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new UncheckedIOException("No se pudo guardar el movimiento", ex);
        }
    }
}
