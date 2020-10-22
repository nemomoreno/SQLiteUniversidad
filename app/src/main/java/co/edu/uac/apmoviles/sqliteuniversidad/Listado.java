package co.edu.uac.apmoviles.sqliteuniversidad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.CookieHandler;
import java.util.ArrayList;

public class Listado extends AppCompatActivity {

    ListView lista;
    public ArrayList<String> myList;
    public ArrayList<Estudiante> myList2;
    EstudianteController ec;
    int posicion;
    Estudiante est;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
        lista = findViewById(R.id.lstestudiantes);
        myList = (ArrayList<String>) getIntent().getSerializableExtra("mylist");
        myList2 = (ArrayList<Estudiante>) getIntent().getSerializableExtra("mylist2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myList);
        lista.setAdapter(adapter);

        /*lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), myList.get(position),Toast.LENGTH_LONG).show();

            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerForContextMenu(lista);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        switch (v.getId()){

            case R.id.lstestudiantes:

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                String estudiante = ((TextView) info.targetView).getText().toString();
                menu.setHeaderTitle(estudiante);

                String [] actions = getResources().getStringArray(R.array.context_menu);
                for (int i=0;i<actions.length;i++){
                    menu.add(Menu.NONE, i, i, actions[i]);
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Key code
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String [] menuItems = getResources().getStringArray(R.array.context_menu);
        String menuItemName = menuItems[menuItemIndex];
        switch (menuItemName) {
            case "Editar":
                Estudiante estudent=myList2.get(menuInfo.position);
                Intent i = new Intent(this, Editar.class);
                i.putExtra("estudiante", estudent);
                startActivityForResult(i, 1);
                posicion=menuInfo.position;
                break;

            case "Borrar":
                String cod = myList2.get(menuInfo.position).getCodigo();
                ec = new EstudianteController(this);
                boolean isDelete=ec.deleteEstudiante(cod);
                if (isDelete){
                    myList.remove(menuInfo.position);
                    myList2.remove(menuInfo.position);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myList);
                lista.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), "Estudiante eliminado",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                est = (Estudiante) data.getSerializableExtra("value");
                myList2.set(posicion, est);
                myList.set(posicion, "Código: "+est.getCodigo()+ ", Nombre: "+est.getNombre()+", Programa: "+est.getPrograma());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myList);
                lista.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), "Estudiante editado",Toast.LENGTH_LONG).show();
            }
        }
    }
}