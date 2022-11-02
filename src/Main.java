
public class Main {


    public static void main(String[] args) {
        var superComputador = new CSC();
        criarEmpresa("A", superComputador);
        criarEmpresa("B", superComputador);
    }

    public static void criarEmpresa(String nome, CSC csc) {
        var empresa = new Empresa(nome, csc);
        for(int i = 1; i <= 10; i++) {
            var t = new Thread(new Funcionario(empresa, i));
            t.start();
        }
    }
}