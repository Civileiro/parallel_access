import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CSC {
    private final Lock lockAcesso = new ReentrantLock(true);
    private final HashMap<Empresa, Condition> empresasCadastradas = new HashMap<>();
    private final Condition esperandoUsar = lockAcesso.newCondition();
    private Empresa empresaUsando = null;
    private final int maxAcessosSimultaneos = 3;
    private int pessoasUsando = 0;
    private final Queue<Empresa> empresasEsperando = new LinkedList<>();

    private Lock getLockAcesso() {
        return lockAcesso;
    }
    public void cadastrarEmpresa(Empresa e) {
        synchronized (empresasCadastradas) {
            var aviso = this.lockAcesso.newCondition();
            empresasCadastradas.put(e, aviso);
        }

    }

    private void esperarVaga() throws InterruptedException {
        while(pessoasUsando == maxAcessosSimultaneos) {
            esperandoUsar.await();
        }
        pessoasUsando++;
    }
    public void requisitarAcesso(Empresa p) throws InterruptedException {
        this.getLockAcesso().lock();
        try {
            while(true) {
                if (pessoasUsando == 0) {
                    empresaUsando = null;
                }
                if (empresaUsando == null || empresaUsando == p) {
                    empresaUsando = p;
                    esperarVaga();
                    return;
                }

                if(!empresasEsperando.contains(p)) {
                    empresasEsperando.add(p);
                }

                empresasCadastradas.get(p).await();
            }
        } finally {
            this.getLockAcesso().unlock();
        }
    }
    public void concluirAcesso(Empresa p) throws InterruptedException {
        this.getLockAcesso().lock();
        try {
            if(pessoasUsando == maxAcessosSimultaneos) {
                esperandoUsar.signal();
            }
            pessoasUsando--;
            if(pessoasUsando == 0) {
                var proximaEmpresa = empresasEsperando.poll();
                if(proximaEmpresa != null) {
                    empresasCadastradas.get(proximaEmpresa).signalAll();
                }
            }
        } finally {
            this.getLockAcesso().unlock();
        }
    }

    public void executar(Runnable trabalho) {
        trabalho.run();
    }
}
