package com.aviatoranalyzer.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    LinearLayout historyBox; TextView signal, stats; ArrayList<Double> history=new ArrayList<>();
    int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+0.5f);}
    TextView tv(String s,float size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setPadding(dp(12),dp(8),dp(12),dp(8));if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return t;}
    public void onCreate(Bundle b){super.onCreate(b);dashboard();}
    void dashboard(){
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(Color.rgb(245,247,250));
        TextView bar=tv("AVIATOR ANALYZER",20,Color.WHITE,true);bar.setGravity(Gravity.CENTER_VERTICAL);bar.setBackgroundColor(Color.rgb(11,18,32));root.addView(bar,new LinearLayout.LayoutParams(-1,dp(64)));
        ScrollView sc=new ScrollView(this);LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(16),dp(16),dp(16),dp(24));
        c.addView(tv("Analyse des coefficients",22,Color.rgb(11,18,32),true));c.addView(tv("Ajoute les coefficients observés pour construire ton historique.",14,Color.DKGRAY,false));
        EditText input=new EditText(this);input.setHint("Ex. 2.35");input.setInputType(2|8192);c.addView(input,new LinearLayout.LayoutParams(-1,dp(56)));
        Button add=new Button(this);add.setText("AJOUTER LE COEFFICIENT");c.addView(add);
        signal=tv("Signal expérimental : —",17,Color.rgb(11,18,32),true);signal.setBackgroundColor(Color.WHITE);c.addView(signal);
        stats=tv("Historique : 0 tour",15,Color.DKGRAY,false);c.addView(stats);
        TextView note=tv("⚠️ Analyse statistique uniquement : l'application ne connaît pas à l'avance le prochain coefficient et ne garantit aucun gain.",13,Color.rgb(120,70,0),false);note.setBackgroundColor(Color.rgb(255,245,220));c.addView(note);
        c.addView(tv("Derniers coefficients",19,Color.rgb(11,18,32),true));historyBox=new LinearLayout(this);historyBox.setOrientation(LinearLayout.VERTICAL);historyBox.setBackgroundColor(Color.WHITE);c.addView(historyBox);
        Button clear=new Button(this);clear.setText("EFFACER L'HISTORIQUE");c.addView(clear);
        add.setOnClickListener(v->{try{double x=Double.parseDouble(input.getText().toString().replace(",","."));if(x<1)throw new Exception();history.add(0,x);input.setText("");update();}catch(Exception e){Toast.makeText(this,"Entre un coefficient valide, par exemple 2.35",Toast.LENGTH_SHORT).show();}});
        clear.setOnClickListener(v->{history.clear();update();});sc.addView(c);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
    }
    void update(){historyBox.removeAllViews();for(int i=0;i<Math.min(history.size(),20);i++){historyBox.addView(tv((i+1)+".   "+String.format(Locale.US,"%.2fx",history.get(i)),16,Color.rgb(30,30,30),false));}stats.setText("Historique : "+history.size()+" tour"+(history.size()>1?"s":""));if(history.size()<5){signal.setText("Signal expérimental : collecte encore 5+ coefficients");return;}int low=0,high=0;for(double x:history){if(x<2)low++;if(x>=5)high++;}double r=(double)low/history.size();signal.setText(r>.70?"Signal expérimental : série basse observée":high>=2?"Signal expérimental : forte volatilité observée":"Signal expérimental : aucune tendance forte détectée");}
}
