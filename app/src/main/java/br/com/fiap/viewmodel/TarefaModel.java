package br.com.fiap.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import dao.BaseDados;
import model.Tarefa;

/**
 * Created by logonrm on 13/11/2017.
 */

public class TarefaModel extends AndroidViewModel {
    private LiveData<List<Tarefa>> tarefas;
    private BaseDados bd;

    public TarefaModel(@NonNull Application application) {
        super(application);
        bd = BaseDados.getDatabase(application.getApplicationContext());
        carregarDados();

    }

    private void carregarDados() {
        tarefas = bd.tarefaDao().listarTodas();
    }

    public LiveData<List<Tarefa>> getTarefas(){
        return this.tarefas;
    }
}
