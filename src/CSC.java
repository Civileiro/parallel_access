import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;


public class CSC {
    private final Semaphore semaforoUso = new Semaphore(3, true);
    private Empresa empresaUsando = null;
    private int pessoasUsando = 0;
    private final Queue<Empresa> empresasEsperando = new LinkedList<>();

    public void requisitarAcesso(Empresa p) throws InterruptedException {
        synchronized (this) {
            while(true) {
                if (empresaUsando == null || empresaUsando == p) {
                    empresaUsando = p;
                    pessoasUsando++;
                    break;
                }

                if(!empresasEsperando.contains(p)) {
                    empresasEsperando.add(p);
                }
                wait();
            }

        }
        semaforoUso.acquire();
    }
    public void concluirAcesso() {
        semaforoUso.release();
        synchronized (this) {
            pessoasUsando--;
            if(pessoasUsando == 0) {
                empresaUsando = empresasEsperando.poll();
                notifyAll();
            }
        }
    }

    public void executar(Runnable trabalho) {
        trabalho.run();
    }
}
