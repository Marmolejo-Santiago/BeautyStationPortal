package com.beautystation.dao;

import com.beautystation.modelo.Usuario;
import java.util.List;

public interface UsuarioDAO {
    Usuario buscarPorId(int id);
    Usuario buscarPorEmail(String email);
    void guardar(Usuario usuario);
    void actualizar(Usuario usuario);

    /** Clientas registradas (sin cuentas de administración), por nombre. */
    List<Usuario> listarClientes();
}
