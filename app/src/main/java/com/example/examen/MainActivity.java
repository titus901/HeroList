package com.example.examen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private String NumberPage, name = "", idHero = "1";
    private AlertDialog.Builder AlertDialog;
    private Dialog dialog;
    private SearchView search;

    private String apiLink = "https://www.superheroapi.com/api.php/10156112965520834";
    private static JSONArray item;
    private static ArrayAdapter<Object> adapterList;
    private static List<Object> ItemsList = new ArrayList<>();
    private ListView itemsScroll;
    private Boolean flag_loading = false;
    private int DataPage = 1, page = 10, listInit = 1, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress();
        GoList();
        Button all = (Button) findViewById(R.id.all);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_loading = false;
                page = 10;
                listInit = 1;
                dialog.show();
                setTitle("Hero List");
                GoList();
            }
        });
    }

    private void GoList() {
        ListView items = (ListView) (findViewById(R.id.list));
        items.setAdapter(null);
        ItemsList = new ArrayList<>();

        for (int i = listInit; i < (page+1); i++) {
            idHero = String.valueOf(i);
            LoadData(page, name, 0, idHero);
            if (page == i){
                ClickDetail();
                ScrollPaginate();
                search();
            }
        }
    }

    private void progress() {
        AlertDialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        AlertDialog.setView(R.layout.content_progress);
        AlertDialog.setCancelable(false);
        dialog = AlertDialog.create();
        dialog.show();
    }

    public void LoadData(int page, String name, final int scroll, String idHero){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                apiLink + "/"+idHero,
                null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("JavascriptInterface")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String valido = response.getString("response");
                            if("error".equals(valido)){
                                //Toast.makeText(MainActivity.this, "No se encontraron resultados", Toast.LENGTH_LONG).show();
                            }else {
                                NumberPage = "644";
                                String IdHero = response.getString("id");
                                String NameHero = response.getString("name");
                                JSONObject FrsImg = response.getJSONObject("image");
                                String img = FrsImg.getString("url");
                                JSONObject powerstats = response.getJSONObject("powerstats");
                                JSONObject biography = response.getJSONObject("biography");
                                JSONObject appearance = response.getJSONObject("appearance");
                                JSONObject work = response.getJSONObject("work");
                                JSONObject connections = response.getJSONObject("connections");
                                String Url = apiLink+IdHero;
                                ItemsList.add(new personaje(IdHero, NameHero, Url, img, powerstats, biography, appearance, work, connections));
                                if(scroll == 1){
                                    adapterList.notifyDataSetChanged();
                                }else{
                                    adapterList = new listAdapter();
                                    ListView items = findViewById(R.id.list);
                                    items.setAdapter(adapterList);
                                }
                                if(scroll == 1){flag_loading = false;}
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Upps, algo ha salido mal.", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(myReq);

    }

    public void LoadDataSearch(String name, final int scroll){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                apiLink + "/search/"+name,
                null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("JavascriptInterface")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String valido = response.getString("response");
                            if("error".equals(valido)){
                                Toast.makeText(MainActivity.this, "No se encontraron resultados", Toast.LENGTH_LONG).show();
                            }else{
                                JSONArray array = response.getJSONArray("results");
                                int countArray = array.length();
                                NumberPage = String.valueOf(countArray);
                                for (int i = 0; i < countArray; i++) {
                                    JSONObject data = array.getJSONObject(i);
                                    NumberPage = "644";
                                    String IdHero = data.getString("id");
                                    String NameHero = data.getString("name");
                                    JSONObject FrsImg = data.getJSONObject("image");
                                    String img = FrsImg.getString("url");
                                    JSONObject powerstats = data.getJSONObject("powerstats");
                                    JSONObject biography = data.getJSONObject("biography");
                                    JSONObject appearance = data.getJSONObject("appearance");
                                    JSONObject work = data.getJSONObject("work");
                                    JSONObject connections = data.getJSONObject("connections");
                                    String Url = apiLink+IdHero;
                                    ItemsList.add(new personaje(IdHero, NameHero, Url, img, powerstats, biography, appearance, work, connections));
                                    if(scroll == 1){
                                        adapterList.notifyDataSetChanged();
                                    }else{
                                        adapterList = new listAdapter();
                                        ListView items = findViewById(R.id.list);
                                        items.setAdapter(adapterList);
                                    }
                                    if(scroll == 1){flag_loading = false;}
                                }
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Upps, algo ha salido mal.", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(myReq);

    }

    private void ScrollPaginate() {
        itemsScroll = (ListView) (findViewById(R.id.list));
        itemsScroll.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Log.e("scroll", String.valueOf(scrollState));
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0){
                    if(Integer.parseInt(NumberPage) < page){
                        flag_loading = true;
                    }
                    if(flag_loading == false)
                    {
                        flag_loading = true;
                        for (int i = 0; i < 1; i++) {
                            dialog.show();
                            page = page + limit;
                            listInit = listInit + limit;
                            for (int e = listInit; e < (page+1); e++) {
                                idHero = String.valueOf(e);
                                LoadData(page, name, 1, idHero);
                            }
                        }
                    }
                }

            }
        });
    }

    private void ClickDetail(){
        itemsScroll = (ListView) (findViewById(R.id.list));
        itemsScroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaje hero = (personaje) ItemsList.get(position);
                Intent i = new Intent(MainActivity.this, HeroDetail.class);
                i.putExtra("url", hero.getUrl());
                i.putExtra("name", hero.getName());
                i.putExtra("image", hero.getImage());
                i.putExtra("powerstats", hero.getPowerstats().toString());
                i.putExtra("biography", String.valueOf(hero.getBiography()));
                i.putExtra("appearance", String.valueOf(hero.getAppearance()));
                i.putExtra("work", String.valueOf(hero.getWork()));
                i.putExtra("connections", String.valueOf(hero.getConnections()));
                startActivity(i);
            }
        });

    }

    public void search(){
        search = (SearchView) findViewById(R.id.search);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setQueryHint("Buscar...");
        search.setSearchableInfo(searchManager.getSearchableInfo(MainActivity. this.getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search.onActionViewExpanded();
                dialog.show();
                ListView items = (ListView) (findViewById(R.id.list));
                items.setAdapter(null);
                ItemsList = new ArrayList<>();
                flag_loading = true;
                if("".equals(query.toString())){
                    flag_loading = false;
                    GoList();
                    setTitle("Hero List");
                }else{
                    setTitle("Busqueda "+"\""+query.toString()+"\"");
                    String tituloPr = query.toLowerCase();
                    name = tituloPr.replace(" ", "%20");
                    LoadDataSearch(name, 0);
                }
                search.onActionViewCollapsed();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

    }

    private class listAdapter extends ArrayAdapter<Object> {
        public listAdapter() {
            super(MainActivity.this, R.layout.content_list_hero, ItemsList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.content_list_hero, parent, false);
            }
            personaje hero = (personaje) ItemsList.get(position);

            TextView name = (TextView) convertView.findViewById(R.id.name);
            //ImageView image = (ImageView) convertView.findViewById(R.id.imgHero);
            CircleImageView image = (CircleImageView) convertView.findViewById(R.id.imgHero);
            name.setText(hero.getName());
            Picasso.with(MainActivity.this).load(hero.getImage()).into(image);
            return convertView;
        }
    }

    class personaje{
        private String id;
        private String name;
        private String Url;
        private String image;
        private JSONObject powerstats;
        private JSONObject biography;
        private JSONObject appearance;
        private JSONObject work;
        private JSONObject connections;

        public personaje(String id, String name, String url, String image, JSONObject powerstats, JSONObject biography, JSONObject appearance, JSONObject work, JSONObject connections) {
            this.id = id;
            this.name = name;
            Url = url;
            this.image = image;
            this.powerstats = powerstats;
            this.biography = biography;
            this.appearance = appearance;
            this.work = work;
            this.connections = connections;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return Url;
        }

        public String getImage() {
            return image;
        }

        public JSONObject getPowerstats() {
            return powerstats;
        }

        public JSONObject getBiography() {
            return biography;
        }

        public JSONObject getAppearance() {
            return appearance;
        }

        public JSONObject getWork() {
            return work;
        }

        public JSONObject getConnections() {
            return connections;
        }
    }


}