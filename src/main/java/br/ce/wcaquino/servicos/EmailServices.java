package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;

public interface EmailServices {

    public void notificarAtraso(Usuario usuario);
}
