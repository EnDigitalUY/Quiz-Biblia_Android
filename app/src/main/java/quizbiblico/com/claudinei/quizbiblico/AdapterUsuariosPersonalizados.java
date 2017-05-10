package quizbiblico.com.claudinei.quizbiblico;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Claudinei on 10/05/2017.
 */

public class AdapterUsuariosPersonalizados extends BaseAdapter {

    private final ArrayList<Usuario> usuarios;
    private final Activity activity;

    public AdapterUsuariosPersonalizados(ArrayList<Usuario> usuarios, Activity activity) {
        this.usuarios = usuarios;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.lista_usuarios_personalizada, parent, false);

        Usuario usuario = usuarios.get(position);

        //Instanciando as Views
        TextView nome = (TextView) view.findViewById(R.id.lista_usuarios_personalizada_nome);
        TextView pontuacao = (TextView) view.findViewById(R.id.lista_usuarios_personalizada_pontuacao);
        TextView posicao = (TextView) view.findViewById(R.id.lista_usuarios_personalizada_posicao);
        TextView dataCadastro = (TextView) view.findViewById(R.id.lista_usuarios_personalizada_datacadastro);

        //Definindo os valores para as Views
        nome.setText( usuario.getNome() == null ? usuario.getEmail() : usuario.getNome());
        pontuacao.setText(String.valueOf(usuario.getPontuacao()));
        posicao.setText(String.valueOf(position + 1) + "º");
        dataCadastro.setText(new SimpleDateFormat("dd/MM/yyyy").format(usuario.getPrimeiroAcesso()));

        return view;
    }
}
