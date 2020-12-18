package com.example.examen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class HeroDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);
        Intent i = getIntent();
        setTitle(i.getStringExtra("name"));
        CircleImageView profile_image = (CircleImageView) findViewById(R.id.profile_image);
        TextView name = (TextView) findViewById(R.id.name);
        //biography
        TextView fullname =  (TextView) findViewById(R.id.full_name);
        TextView alteregos =  (TextView) findViewById(R.id.alter_egos);
        TextView aliases =  (TextView) findViewById(R.id.aliases);
        TextView placeofbirth =  (TextView) findViewById(R.id.place_of_birth);
        TextView first_appearance =  (TextView) findViewById(R.id.first_appearance);
        TextView publisher =  (TextView) findViewById(R.id.publisher);
        TextView alignment =  (TextView) findViewById(R.id.alignment);
        //appearance
        TextView gender =  (TextView) findViewById(R.id.gender);
        TextView race =  (TextView) findViewById(R.id.race);
        TextView height =  (TextView) findViewById(R.id.height);
        TextView weight =  (TextView) findViewById(R.id.weight);
        TextView eyecolor =  (TextView) findViewById(R.id.eye_color);
        TextView haircolor =  (TextView) findViewById(R.id.hair_color);
        //work
        TextView occupation =  (TextView) findViewById(R.id.occupation);
        TextView base =  (TextView) findViewById(R.id.base);
        //connections
        TextView groupaffiliation =  (TextView) findViewById(R.id.group_affiliation);
        TextView relatives =  (TextView) findViewById(R.id.relatives);
        //powerstats
        TextView intelligence =  (TextView) findViewById(R.id.intelligence);
        TextView strength =  (TextView) findViewById(R.id.strength);
        TextView speed =  (TextView) findViewById(R.id.speed);
        TextView durability =  (TextView) findViewById(R.id.durability);
        TextView power =  (TextView) findViewById(R.id.power);
        TextView combat =  (TextView) findViewById(R.id.combat);
        ProgressBar intelligencep = (ProgressBar) findViewById(R.id.intelligenceP);
        ProgressBar strengthp = (ProgressBar) findViewById(R.id.strengthP);
        ProgressBar speedp = (ProgressBar) findViewById(R.id.speedP);
        ProgressBar durabilityp = (ProgressBar) findViewById(R.id.durabilityP);
        ProgressBar powerp = (ProgressBar) findViewById(R.id.powerP);
        ProgressBar combatp = (ProgressBar) findViewById(R.id.combatP);

        try {
            //biography
            JSONObject biography = new JSONObject(i.getStringExtra("biography"));
            fullname.setText(biography.getString("full-name"));
            alteregos.setText(biography.getString("alter-egos"));
            JSONArray Alias = biography.getJSONArray("aliases");
            String AllAlias = "";
            for (int e = 0; e < Alias.length(); e++) {
                AllAlias = AllAlias + Alias.getString(e) + ", ";
            }
            aliases.setText(AllAlias);
            placeofbirth.setText(biography.getString("place-of-birth"));
            first_appearance.setText(biography.getString("first-appearance"));
            publisher.setText(biography.getString("publisher"));
            alignment.setText(biography.getString("alignment"));
            //appearance
            JSONObject appearance = new JSONObject(i.getStringExtra("appearance"));
            gender.setText(appearance.getString("gender"));
            race.setText(appearance.getString("race"));
            JSONArray heights = appearance.getJSONArray("height");
            Log.e("heights", String.valueOf(heights));
            String Allheight = "";
            for (int e = 0; e < heights.length(); e++) {
                Allheight = Allheight + heights.getString(e) + ", ";
            }
            height.setText(Allheight);
            JSONArray weights = appearance.getJSONArray("weight");
            String Allweight = "";
            for (int e = 0; e < weights.length(); e++) {
                Allweight = Allweight + weights.getString(e) + ", ";
            }
            weight.setText(Allweight);
            eyecolor.setText(appearance.getString("eye-color"));
            haircolor.setText(appearance.getString("hair-color"));
            //work
            JSONObject work = new JSONObject(i.getStringExtra("work"));
            occupation.setText(work.getString("occupation"));
            base.setText(work.getString("base"));
            //connections
            JSONObject connections = new JSONObject(i.getStringExtra("connections"));
            groupaffiliation.setText(connections.getString("group-affiliation"));
            relatives.setText(connections.getString("relatives"));
            //powerstats
            JSONObject powerstats = new JSONObject(i.getStringExtra("powerstats"));
            if("null".equals(powerstats.getString("intelligence"))){
                intelligence.setText("0%");
                intelligencep.setProgress(0);
            }else{
                intelligence.setText(powerstats.getString("intelligence") + "%");
                intelligencep.setProgress(Integer.parseInt(powerstats.getString("intelligence")));
            }
            if("null".equals(powerstats.getString("strength"))){
                strength.setText("0%");
                strengthp.setProgress(0);
            }else{
                strength.setText(powerstats.getString("strength")  + "%");
                strengthp.setProgress(Integer.parseInt(powerstats.getString("strength")));
            }

            if("null".equals(powerstats.getString("speed"))){
                speed.setText("0%");
                speedp.setProgress(0);
            }else{
                speed.setText(powerstats.getString("speed")  + "%");
                speedp.setProgress(Integer.parseInt(powerstats.getString("speed")));
            }

            if("null".equals(powerstats.getString("durability"))){
                durability.setText("0%");
                durabilityp.setProgress(0);
            }else{
                durability.setText(powerstats.getString("durability")  + "%");
                durabilityp.setProgress(Integer.parseInt(powerstats.getString("durability")));
            }

            if("null".equals(powerstats.getString("power"))){
                power.setText("0%");
                powerp.setProgress(0);
            }else{
                power.setText(powerstats.getString("power")  + "%");
                powerp.setProgress(Integer.parseInt(powerstats.getString("power")));
            }

            if("null".equals(powerstats.getString("combat"))){
                combat.setText("0%");
                combatp.setProgress(0);
            }else{
                combat.setText(powerstats.getString("combat")  + "%");
                combatp.setProgress(Integer.parseInt(powerstats.getString("combat")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        name.setText(i.getStringExtra("name"));
        Picasso.with(HeroDetail.this).load(i.getStringExtra("image")).into(profile_image);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}