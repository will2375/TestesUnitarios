package br.ce.wcaquino.daos;

import br.ce.wcaquino.entidades.Locacao;

import java.util.List;

public interface LocacaoDAO {
    public void salvar(Locacao locacao);

    public List<Locacao> obterLocacoesPendentes();
}
