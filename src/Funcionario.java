public class Funcionario implements Runnable {
    private final int id;
    private final Empresa empresa;

    Funcionario(Empresa empresa, int id) {
        this.empresa = empresa;
        this.id = id;
    }

    @Override
    public String toString() {
        return "[" + empresa.getNome() + "] F" + this.id;
    }

    @Override
    public void run() {

        var tempo = Math.random() * 30;
        sleep(tempo);
        System.out.println(this + " tentando acesso");
        try {
            empresa.acessarCSC(() -> {
                System.out.println("+ " + this + " acessou");
                var tempo1 = Math.random() * 3 + 3;
                sleep(tempo1);
                System.out.println("- " + this + " terminou");
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    private void sleep(double secs) {
        try {
            var tempo = (long)(secs * 1000);
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
