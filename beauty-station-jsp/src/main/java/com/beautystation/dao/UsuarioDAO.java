package com.beautystation.dao;

import com.beautystation.modelo.Usuario;

public interface UsuarioDAO {
    Usuario buscarPorId(int id);
    Usuario buscarPorEmail(String email);
    void guardar(Usuario usuario);
    void actualizar(Usuario usuario);
}
