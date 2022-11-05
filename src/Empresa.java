public class Empresa {
    private final String nome;
    private final CSC superComputador;

    Empresa(String nome, CSC superComputador) {
        this.nome = nome;
        this.superComputador = superComputador;
    }

    public String getNome() {
        return nome;
    }

    void acessarCSC(Runnable trabalho) throws InterruptedException {
        superComputador.requisitarAcesso(this);
        superComputador.executar(trabalho);
        superComputador.concluirAcesso();
    }
}
