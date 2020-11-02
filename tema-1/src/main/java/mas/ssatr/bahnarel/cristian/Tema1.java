package mas.ssatr.bahnarel.cristian;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

public class Tema1 {
    private boolean ok = true;
    private int clc = 0;
    private String marcaj_initial = "";
    public Dictionary <String, Long> dict_loc_jet = new Hashtable();
    public Dictionary <String, Long> dict_tr_temp = new Hashtable();
    public Dictionary <String, String> dict_tr_ok = new Hashtable();
    public static Tema1 s = new Tema1();
    public static PrintStream o;
    public static PrintStream console = System.out;
    {
        try {
            o = new PrintStream(new File("G:/Meh -.-/MAS-IAISC An 1/SSTR/Tema1/src/main/java/mas/" +
                    "ssatr/bahnarel/cristian/Output.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> mark = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("G:/Meh -.-/MAS-IAISC An 1/SSTR/Tema1/src/main/java/mas/" +
                "ssatr/bahnarel/cristian/Reteaua_petri_finala.json"))
        {
            Object obj = jsonParser.parse(reader);
            JSONObject all = (JSONObject) obj;
            JSONArray loc_p = (JSONArray) all.get("locatii");
            JSONArray loc_t = (JSONArray) all.get("tranzitii");
            int nr_p = loc_p.size();
            int nr_t = loc_t.size();
            for(int x=1; x<=nr_t; x++){
                mark.add(0);
            }
            System.setOut(o);
            System.out.println("Locatii:" + nr_p + "  -  Tranzitii:" + nr_t);
            System.out.print("Timer");
            for(int i=1; i<=nr_p;i++){
                System.out.print("  P" + String.valueOf(i)+ "  ");
            }
            System.setOut(console);
            System.out.println("Locatii:" + nr_p + "  -  Tranzitii:" + nr_t);
            System.out.print("Timer");
            for(int i=1; i<=nr_p;i++){
                System.out.print("  P" + String.valueOf(i)+ "  ");
            }
            for(int i=0; i<nr_p;i++) {
                JSONObject obj_p = (JSONObject) loc_p.get(i);
                JSONObject obj_pp = (JSONObject) obj_p.get("locatie" + String.valueOf(i+1));
                s.LocatiiP(obj_pp, i);
            }
            for(int i=0; i<nr_t;i++) {
                JSONObject obj_t = (JSONObject) loc_t.get(i);
                JSONObject obj_tt = (JSONObject) obj_t.get("tranzitie" + String.valueOf(i+1));
                s.TranzitiiT(obj_tt, i);
            }


            s.simulate(nr_p, loc_t, nr_t, mark);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void simulate(int nr, JSONArray TTT, int nrtt, ArrayList<Integer> mark){
        while (ok) {
            clc++;
            if (clc == 1){
                marcaj_initial = get_marcaj();
            }
            if (verify_stop_cond(clc, marcaj_initial, mark)){
                System.exit(0);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.setOut(o);
            System.out.print("\n  " + String.valueOf(clc) + "  ");
            for(int k=1; k<=nr;k++){
                System.out.print("   " + dict_loc_jet.get("P" + String.valueOf(k)) + "  ");
            }
            System.setOut(console);
            System.out.print("\n  " + String.valueOf(clc) + "  ");
            for(int k=1; k<=nr;k++){
                System.out.print("   " + dict_loc_jet.get("P" + String.valueOf(k)) + "  ");
            }
            for(int i=0; i<nrtt;i++) {
                JSONObject obj_t = (JSONObject) TTT.get(i);
                JSONObject obj_tt = (JSONObject) obj_t.get("tranzitie" + String.valueOf(i+1));
                s.Tr_availability(obj_tt, i, clc, mark);
            }
            for(int i=0; i<nrtt;i++) {
                JSONObject obj_t = (JSONObject) TTT.get(i);
                JSONObject obj_tt = (JSONObject) obj_t.get("tranzitie" + String.valueOf(i+1));
                s.execute_Tr(obj_tt, i);
            }

        }
    }

    public boolean verify_stop_cond(int timer, String marcaj_init, ArrayList<Integer> mark){
        boolean ok = false;
        boolean ok_mark = true;
        for(int i=0; i< mark.size(); i++){
            if (mark.get(i) != 0){
                ok_mark = false;
            }
        }
        if (timer >=2){
            for(int k=1; k<=dict_tr_ok.size(); k++){
                if(dict_tr_ok.get("T" + String.valueOf(k)).equals("ok") || mark.get(k-1) !=0){
                    ok = true;
                }
            }
            if (ok == false){
                System.setOut(o);
                System.out.println("\nNu a mai ramas nimic de executat!");
                System.setOut(console);
                System.out.println("\nNu a mai ramas nimic de executat!");
                return true;
            }
            if (ok_mark==true && marcaj_init.equals(get_marcaj())){
                System.setOut(o);
                System.out.println("\nEste bucla infinita!");
                System.setOut(console);
                System.out.println("\nEste bucla infinita!");
                return true;
            }
        }
        if (timer == 30) {
            System.setOut(o);
            System.out.println("\nDeadlock!");
            System.setOut(console);
            System.out.println("\nDeadlock!");
            return true;
        }
        return false;
    }

    public void LocatiiP(JSONObject P, int nr){
        Long jeton = (Long) P.get("jeton");
        dict_loc_jet.put("P" + String.valueOf(nr+1), jeton);
    }

    public void TranzitiiT(JSONObject T, int nr){
        JSONArray temp_int = (JSONArray) T.get("temp");
        Random rand = new Random();
        int tmp = rand.nextInt(temp_int.size());
        Object temp = temp_int.get(tmp);
        dict_tr_temp.put("T" + String.valueOf(nr+1), (Long) temp);
    }

    public void Tr_availability(JSONObject TT, int nn, int clc, ArrayList<Integer> mark){
        JSONArray pre_list = (JSONArray) TT.get("pre");
        int ok = 0;
        ArrayList<String> ll = new ArrayList<>();
        for(int q = 0; q<pre_list.size(); q++){
            String pre = (String) pre_list.get(q);
            ll.add(pre);
        }
        for(int z = 0; z<ll.size(); z++){
            if (dict_loc_jet.get("P" + ll.get(z).substring(7).toString()) != 0){
                ok = ok +1;
            }
        }
        if (ok == ll.size()  && s.verify_temp(TT, clc,mark, nn) == true){
            dict_tr_ok.put("T" + String.valueOf(nn+1), "ok");
        }
        else {
            dict_tr_ok.put("T" + String.valueOf(nn+1), "not ok");
        }
    }

    public void execute_Tr(JSONObject TTT, int l){
        JSONArray pre_list = (JSONArray) TTT.get("pre");
        JSONArray post_list = (JSONArray) TTT.get("post");
        ArrayList <String> lll_pre = new ArrayList<>();
        ArrayList <String> lll_post = new ArrayList<>();
        for(int q = 0; q<pre_list.size(); q++){
            String pre = (String) pre_list.get(q);
            lll_pre.add(pre);
        }
        for(int w = 0; w<post_list.size(); w++){
            String post = (String) post_list.get(w);
            lll_post.add(post);
        }
        if (dict_tr_ok.get("T" + String.valueOf(l+1)).equals("ok")) {
            for(int e = 0; e<lll_pre.size(); e++){
                dict_loc_jet.put("P" + lll_pre.get(e).substring(7).toString(), dict_loc_jet.get("P" +
                        lll_pre.get(e).substring(7).toString()) - 1);
            }
            if(!lll_post.get(0).equals("null")){
                for(int y = 0; y<lll_post.size(); y++){
                    dict_loc_jet.put("P" + lll_post.get(y).substring(7).toString(), dict_loc_jet.get("P" +
                            lll_post.get(y).substring(7).toString()) + 1);
                }
            }
        }
    }

    public boolean verify_temp(JSONObject TTTT, int clc, ArrayList<Integer> mark, int nn){
        Long temporizare;
        if(mark.get(nn) == 0) {
            JSONArray temp_int = (JSONArray) TTTT.get("temp");
            Random rand = new Random();
            int tmp = rand.nextInt(temp_int.size());
            Object temp = temp_int.get(tmp);
            temporizare = (Long) temp;
        }
        else {
            temporizare = dict_tr_temp.get("T" + String.valueOf(nn+1));
        }
        if (mark.get(nn) == 0 && temporizare != 1){
            mark.set(nn, clc);
            return false;
        }
        else if(mark.get(nn) == 0 && temporizare == 1){
            return true;
        }
        else if(mark.get(nn) + temporizare-1 == clc){
            mark.set(nn,0);
            return true;
        }
        else {
            return false;
        }

    }

    public String get_marcaj(){
        String marcaj = "";
        for (int p = 1; p<=dict_loc_jet.size(); p++){
            marcaj = marcaj + dict_loc_jet.get("P" + String.valueOf(p));
        }
        return marcaj;
    }

}
