package com.chialung.jclprofile.Module;



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class IpartDictionary {
    public static HashMap<String, Integer> DB = null; //1
    public static HashMap<String, String> TextDB = null;
    public static HashMap<String, RequestData> URLDB = null; //2
    public static byte Save_Count = 0;
    public final static byte SAVE_LIMIT_COUNT = 5;

    public static void DropCache() {
        URLDB.clear();
    }

    public static void putURLDB(String key, RequestData data) {
        URLDB.put(key, data);
        if (++Save_Count > SAVE_LIMIT_COUNT && URLDB != null) {
            Save_Count = 0;
        }
        //Utils.debug("AiOut", "URLDB size:" + URLDB.size(), 3);
    }

    public static boolean checkTimeout() {
        boolean isDel = false;
        try {
            synchronized (URLDB) {
                Set<Entry<String, RequestData>> s = URLDB.entrySet();
                Iterator<Entry<String, RequestData>> it = s.iterator();
                while (it.hasNext()) {
                    Entry<String, RequestData> m = (Entry<String, RequestData>) it.next();
                    RequestData uh = (RequestData) m.getValue();
                    if (System.currentTimeMillis() - uh.requestTime > 43200000) {
                        it.remove();
                        isDel = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        ;
        return isDel;
    }

    @SuppressWarnings("unchecked")
    public static void init(int type) {
        switch (type) {
            case 1:
                IpartDictionary.setInit();
                break;
            case 2:
                if (IpartDictionary.URLDB == null) {
                    IpartDictionary.URLDB = new HashMap<String, RequestData>(20);
                }
                break;
            case 3:
                if (IpartDictionary.TextDB == null) {
                    IpartDictionary.TextDB = new HashMap<String, String>(70);
                    IpartDictionary.TextDB.put("height", "120cm/4'0\",121cm/4'0\",122cm/4'0\",123cm/4'1\",124cm/4'1\",125cm/4'2\",126cm/4'2\",127cm/4'2\",128cm/4'3\",129cm/4'3\",130cm/4'3\",131cm/4'4\",132cm/4'4\",133cm/4'5\",134cm/4'5\",135cm/4'5\",136cm/4'6\",137cm/4'6\",138cm/4'7\",139cm/4'7\",140cm/4'7\",141cm/4'8\",142cm/4'8\",143cm/4'9\",144cm/4'9\",145cm/4'9\",146cm/4'10\",147cm/4'10\",148cm/4'11\",149cm/4'11\",150cm/4'11\",151cm/5'0\",152cm/5'0\",153cm/5'1\",154cm/5'1\",155cm/5'1\",156cm/5'2\",157cm/5'2\",158cm/5'3\",159cm/5'3\",160cm/5'3\",161cm/5'4\",162cm/5'4\",163cm/5'4\",164cm/5'5\",165cm/5'5\",166cm/5'6\",167cm/5'6\",168cm/5'6\",169cm/5'7\",170cm/5'7\",171cm/5'8\",172cm/5'8\",173cm/5'8\",174cm/5'9\",175cm/5'9\",176cm/5'10\",177cm/5'10\",178cm/5'10\",179cm/5'11\",180cm/5'11\",181cm/6'0\",182cm/6'0\",183cm/6'0\",184cm/6'1\",185cm/6'1\",186cm/6'2\",187cm/6'2\",188cm/6'2\",189cm/6'3\",190cm/6'3\",191cm/6'4\",192cm/6'4\",193cm/6'4\",194cm/6'5\",195cm/6'5\",196cm/6'5\",197cm/6'6\",198cm/6'6\",199cm/6'7\",200cm/6'7\",201cm/6'7\",202cm/6'8\",203cm/6'8\",204cm/6'9\",205cm/6'9\",206cm/6'9\",207cm/6'10\",208cm/6'10\",209cm/6'11\",210cm/6'11\",211cm/6'11\",212cm/7'0\",213cm/7'0\",214cm/7'1\",215cm/7'1\",216cm/7'1\",217cm/7'2\",218cm/7'2\",219cm/7'3\",220cm/7'3\",");
                    IpartDictionary.TextDB.put("trans_height", "120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220");
                    IpartDictionary.TextDB.put("weight", "30kg/66lbs,31kg/68lbs,32kg/70lbs,33kg/72lbs,34kg/74lbs,35kg/77lbs,36kg/79lbs,37kg/81lbs,38kg/83lbs,39kg/85lbs,40kg/88lbs,41kg/90lbs,42kg/92lbs,43kg/94lbs,44kg/96lbs,45kg/99lbs,46kg/101lbs,47kg/103lbs,48kg/105lbs,49kg/107lbs,50kg/110lbs,51kg/112lbs,52kg/114lbs,53kg/116lbs,54kg/118lbs,55kg/121lbs,56kg/123lbs,57kg/125lbs,58kg/127lbs,59kg/129lbs,60kg/132lbs,61kg/134lbs,62kg/136lbs,63kg/138lbs,64kg/140lbs,65kg/143lbs,66kg/145lbs,67kg/147lbs,68kg/149lbs,69kg/151lbs,70kg/154lbs,71kg/156lbs,72kg/158lbs,73kg/160lbs,74kg/162lbs,75kg/165lbs,76kg/167lbs,77kg/169lbs,78kg/171lbs,79kg/174lbs,80kg/176lbs,81kg/178lbs,82kg/180lbs,83kg/182lbs,84kg/185lbs,85kg/187lbs,86kg/189lbs,87kg/191lbs,88kg/193lbs,89kg/196lbs,90kg/198lbs,91kg/200lbs,92kg/202lbs,93kg/204lbs,94kg/207lbs,95kg/209lbs,96kg/211lbs,97kg/213lbs,98kg/215lbs,99kg/218lbs,100kg/220lbs,101kg/222lbs,102kg/224lbs,103kg/226lbs,104kg/229lbs,105kg/231lbs,106kg/233lbs,107kg/235lbs,108kg/237lbs,109kg/240lbs,110kg/242lbs,111kg/244lbs,112kg/246lbs,113kg/248lbs,114kg/251lbs,115kg/253lbs,116kg/255lbs,117kg/257lbs,118kg/259lbs,119kg/262lbs,120kg/264lbs,121kg/266lbs,122kg/268lbs,123kg/270lbs,124kg/273lbs,125kg/275lbs,126kg/277lbs,127kg/279lbs,128kg/281lbs,129kg/284lbs,130kg/286lbs,131kg/288lbs,132kg/290lbs,133kg/292lbs,134kg/295lbs,135kg/297lbs,136kg/299lbs,137kg/301lbs,138kg/303lbs,139kg/306lbs,140kg/308lbs,141kg/310lbs,142kg/312lbs,143kg/314lbs,144kg/317lbs,145kg/319lbs,146kg/321lbs,147kg/323lbs,148kg/325lbs,149kg/328lbs,150kg/330lbs");
                    IpartDictionary.TextDB.put("trans_weight", "30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150");
                    IpartDictionary.TextDB.put("/001", "[:D]");
                    IpartDictionary.TextDB.put("/002", "[:)]");
                    IpartDictionary.TextDB.put("/003", "[:~]");
                    IpartDictionary.TextDB.put("/004", "[:6]");
                    IpartDictionary.TextDB.put("/005", "[:(]");
                    IpartDictionary.TextDB.put("/006", "[:|]");
                    IpartDictionary.TextDB.put("/007", "[:O]");
                    IpartDictionary.TextDB.put("/008", "[:E]");
                    IpartDictionary.TextDB.put("/009", "[:u]");
                    IpartDictionary.TextDB.put("/010", "[:@]");
                    IpartDictionary.TextDB.put("/011", "[:P]");
                    IpartDictionary.TextDB.put("/012", "[:-]");
                    IpartDictionary.TextDB.put("/013", "[X(]");
                    IpartDictionary.TextDB.put("/014", "[:((]");
                    IpartDictionary.TextDB.put("/015", "[:o#]");
                    IpartDictionary.TextDB.put("/016", "[=D]");
                    IpartDictionary.TextDB.put("/017", "[:o?]");
                    IpartDictionary.TextDB.put("/018", "[:$$]");
                    IpartDictionary.TextDB.put("/019", "[b-(]");
                    IpartDictionary.TextDB.put("/020", "[:*]");
                    IpartDictionary.TextDB.put("/021", "[:))]");
                    IpartDictionary.TextDB.put("/022", "[8-}]");
                    IpartDictionary.TextDB.put("/023", "[*)]");
                    IpartDictionary.TextDB.put("/024", "[XP~]");
                    IpartDictionary.TextDB.put("/025", "[:%%]");
                    IpartDictionary.TextDB.put("/026", "[:#]");
                    IpartDictionary.TextDB.put("/027", "[I-)]");
                    IpartDictionary.TextDB.put("/028", "[X-)]");
                    IpartDictionary.TextDB.put("/029", "[:-x]");
                    IpartDictionary.TextDB.put("/030", "[:-)]");
                    IpartDictionary.TextDB.put("/031", "[:-|]");
                    IpartDictionary.TextDB.put("/032", "[;-)]");
                    IpartDictionary.TextDB.put("/033", "[X-D]");
                    IpartDictionary.TextDB.put("/034", "[)-D]");
                    IpartDictionary.TextDB.put("/035", "[(-|]");
                    IpartDictionary.TextDB.put("/036", "[(-Q]");
                    IpartDictionary.TextDB.put("/037", "[X-(]");
                    IpartDictionary.TextDB.put("/038", "[(-*]");
                    IpartDictionary.TextDB.put("[:D]", "/001");
                    IpartDictionary.TextDB.put("[:)]", "/002");
                    IpartDictionary.TextDB.put("[:~]", "/003");
                    IpartDictionary.TextDB.put("[:6]", "/004");
                    IpartDictionary.TextDB.put("[:(]", "/005");
                    IpartDictionary.TextDB.put("[:|]", "/006");
                    IpartDictionary.TextDB.put("[:O]", "/007");
                    IpartDictionary.TextDB.put("[:E]", "/008");
                    IpartDictionary.TextDB.put("[:u]", "/009");
                    IpartDictionary.TextDB.put("[:@]", "/010");
                    IpartDictionary.TextDB.put("[:P]", "/011");
                    IpartDictionary.TextDB.put("[:-]", "/012");
                    IpartDictionary.TextDB.put("[X(]", "/013");
                    IpartDictionary.TextDB.put("[:((]", "/014");
                    IpartDictionary.TextDB.put("[:o#]", "/015");
                    IpartDictionary.TextDB.put("[=D]", "/016");
                    IpartDictionary.TextDB.put("[:o?]", "/017");
                    IpartDictionary.TextDB.put("[:$$]", "/018");
                    IpartDictionary.TextDB.put("[b-(]", "/019");
                    IpartDictionary.TextDB.put("[:*]", "/020");
                    IpartDictionary.TextDB.put("[:))]", "/021");
                    IpartDictionary.TextDB.put("[8-}]", "/022");
                    IpartDictionary.TextDB.put("[*)]", "/023");
                    IpartDictionary.TextDB.put("[XP~]", "/024");
                    IpartDictionary.TextDB.put("[:%%]", "/025");
                    IpartDictionary.TextDB.put("[:#]", "/026");
                    IpartDictionary.TextDB.put("[I-]", "/027");
                    IpartDictionary.TextDB.put("[X-)]", "/028");
                    IpartDictionary.TextDB.put("[:-x]", "/029");
                    IpartDictionary.TextDB.put("[:-)]", "/030");
                    IpartDictionary.TextDB.put("[:-|]", "/031");
                    IpartDictionary.TextDB.put("[;-)]", "/032");
                    IpartDictionary.TextDB.put("[X-D]", "/033");
                    IpartDictionary.TextDB.put("[)-D]", "/034");
                    IpartDictionary.TextDB.put("[(-|]", "/035");
                    IpartDictionary.TextDB.put("[(-Q]", "/036");
                    IpartDictionary.TextDB.put("[X-(]", "/037");
                    IpartDictionary.TextDB.put("[(-*]", "/038");
                }
                break;
            case 4://中文語系相關設定
                setInit();
                break;
            case 5://英文語系相關設定
                setInit();
                break;
        }
    }

    private static void setInit() {
        if (IpartDictionary.DB == null) {

            //興趣ICON
            //IpartDictionary.DB.put("/myInterest1", (R.drawable.interest_01));

        }
    }
}

