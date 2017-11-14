package br.com.fiap.persistencia;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.view.TarefaDialog;
import br.com.fiap.view.adapter.TarefaAdapter;
import br.com.fiap.view.listener.OnItemClickListener;
import br.com.fiap.viewmodel.TarefaModel;
import dao.BaseDados;
import model.Tarefa;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView rvTarefas;
    private TarefaAdapter adapter;
    private List<Tarefa> tarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        rvTarefas = (RecyclerView) findViewById(R.id.rvTarefas);

        tarefas = new ArrayList<>();

        ViewModelProviders.of(this)
                .get(TarefaModel.class)
                .getTarefas()
                .observe(this, new Observer<List<Tarefa>>() {
                    @Override
                    public void onChanged(@Nullable List<Tarefa> tarefas) {
                        adapter.setList(tarefas);
                        rvTarefas.getAdapter().notifyDataSetChanged();
                    }
                });

        rvTarefas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TarefaAdapter(tarefas, deleteClick);
        rvTarefas.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TarefaDialog dialog = new TarefaDialog();
                dialog.show(getFragmentManager(), "CriarTarefa");
            }
        });
    }

    private OnItemClickListener deleteClick = new OnItemClickListener() {
        @Override
        public void onClick(int position) {
            BaseDados db = BaseDados.getDatabase(MainActivity.this.getApplicationContext());
            new ApagarAsyncTask(db).execute(adapter.getTarefa(position));
        }
    };

    private class ApagarAsyncTask extends AsyncTask<Tarefa, Void, Void> {

        private BaseDados db;

        ApagarAsyncTask(BaseDados appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Tarefa... params) {
            db.tarefaDao().apagar(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Registro excluído com sucesso", Toast.LENGTH_SHORT).show();
        }
    }
}
