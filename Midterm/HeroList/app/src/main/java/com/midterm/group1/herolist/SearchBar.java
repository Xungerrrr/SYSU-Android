package com.midterm.group1.herolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchBar extends Activity
        implements SearchView.OnQueryTextListener {
    private SearchView searchbar;
    private ListView listView;
    private MyListViewAdapter adapter;
    //private Spinner chooseList;
    private Spinner chooseCategory;
    private MyData database;
    private List<Hero> herolist = new ArrayList<Hero>();
    private String[] heroarray;

    private String username;
    //private ArrayAdapter<String> adapter;
    private String table;
    private String category;
    private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        searchbar = (SearchView)findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.slist);
        //chooseList = (Spinner) findViewById(R.id.spinner_list);
        chooseCategory = (Spinner)findViewById(R.id.spinner_category);
        database = new MyData(this);
        table = "";
        category = "";
        query = "";
        initlist();

        adapter = new MyListViewAdapter(this,herolist);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchBar.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                Hero hero = (Hero)parent.getItemAtPosition(position);
                String name = hero.getName();
                bundle.putSerializable("click",database.findHero(name));
                bundle.putString("name", username);
                bundle.putInt("position", -1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        searchbar.setIconifiedByDefault(false);
        searchbar.setOnQueryTextListener(this);
        spinnerHandler();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(TextUtils.isEmpty(newText)) {
            adapter.clearTextFilter();
            query = "";
        }
        else {
            adapter.setFilterText(newText);
            query = newText;
            //adapter.getFilter().filter(newText);
        }
        //adapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void spinnerHandler() {
        /*
        chooseList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] tlist = getResources().getStringArray(R.array.choose_list);
                table = tlist[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
        chooseCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] tcategory = getResources().getStringArray(R.array.choose_category);
                category = tcategory[position];
                String temp = category + "," + query;
                //Toast.makeText(getApplicationContext(), temp,Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter(temp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initlist() {

        if(username == null || username.isEmpty()) return;
        herolist = database.userAdd(username);

        //herolist.add(new Hero("bqicon", "bq","白起", "上路", "坦克", 0,80,30,40,40,"“我不是人。而是兵器。”\n“即使也有过身为普通人的岁月。那段记忆已变得混乱模糊。”\n“作为人的时候，我是如此弱小，残缺不健全。一个普通孩童的力量都可以把我掀翻。”\n“从有意识起，我就待在黑暗之中，待在狭小的，没有光芒的房间。”\n“直到有一天，唯一的门被推开，阳光照射了进来。是那个孩子。他是那样尊贵，那样聪颖而博识，那样炫目如同光明。”\n“当我被芈月太后找来的奇怪太医徐福实施的手术折磨时，只有他会来看我。”\n“他为什么知道我渴求着什么？他竟然承诺要让我走出黑暗。”\n“是的，我如此渴望拥抱光明。为了光，为了照亮我暗无天日的生活中的这道光明。”\n“身在黑暗，心向光明。”\n“所以我想变强。我想有一天，走出这狭小的房间，堂堂正正站在阿政的身边，站在阳光之下，为他开疆拓土。”\n“徐福发现了这件事。他哈哈大笑，让我无地自容。”\n“干嘛做这些多余的事。他说。很快，你将变得强大。比任何人都强大。”\n“接下来发生什么，我就不记得了。徐福找来他过去的弟子，一起为我施行了手术。”\n“我……不再是我，甚至连人都不是。”\n“坚硬的躯壳，令人窒息的皮肤……”\n“但是我的确变得强。我走出狭小房间，在荒原上，轻而易举埋葬了整整一支军队。”\n“天与地都变成暗色，就像囚禁我好多年的房间的颜色。”\n“徐福说，你不是人，而是一把武器。最强的剑，只会被最强的手所挥动。”\n“从那时候起，已经过了多少年了呢？我一直记得这句话。”\n“能挥动我这把剑的，永远只有一个人——阿政。那个过去的小孩子，现在是君临秦国的君主，而且还将统一六国，争霸天下。”\n“我并不怨恨徐福将我变成这副奇怪的模样。我实现了自己的梦想。曾经我仅能仰望阿政，现在我可以为他冲锋在前。”\n“徐福的离开一度让我认为自己会因渴血而死。还好我找到了当年徐福的弟子，医师扁鹊。他为我再次施行了手术。”\n“‘你应该知道，做完手术后，你也可以如常人般生存。但你的身体衰败的速度会异于常人。这样也没关系吗？’扁鹊问。”\n“当然没关系。”\n“有件小事，我从来没有告诉过阿政。在实施过手术刚苏醒时，听到芈月太后说，逝去武王的孩子，也被送给了徐福作为手术素材之一。那个孩子是我吗？我不知道。王座本应是我的吗？我也不在乎。”\n能够作为阿政的臣子，为他冲锋在前，这便足矣。\n\n“身在黑暗，心向光明”"));
        //herolist.add(new Hero("cjshicon", "cjsh","成吉思汗", "下路", "射手", 0,30,80,30,50,"千年之前，太古的迁徙者经过凛冬之海，遭遇了来自深渊的苍狼。惨烈的战斗后，一些人得以成功的继续前进，另一些人则因为伤病被迫留下来，他们臣服于苍狼，成为它的子民。苍狼的后裔，成为北夷人的先祖。\n因为这样的传说，这样的血统，北夷人被视为蛮荒之民和血脉不纯正者。他们只能龟缩在极寒之地，依靠放牧和掠夺艰难求生，甚至在荒年分裂为好几个部族，为了争夺粮食自相残杀或是向其他国家乞求施舍。唯独一致的是苦苦渴盼神明的解救，渴盼着先祖重新降临大地。\n冬夜，部落的帐篷中诞生了一个婴儿。花剌子模的商人带来大巫的预言：占卜这个孩童的命运，只到深沉的黑暗。他是带来祸乱的灾厄之子吗？族长终于命人将婴儿遗弃到冰结的湖上。可狼群长啸着聚集起来，它们带走了婴儿。\n花剌子模，西域诸国之一。他们恐惧强盛的大唐，却鄙夷野蛮又弱小的北夷部族，用粮食挑唆他们争斗，为自己卑鄙的夺取利益。他们的国王尤其疯狂热衷于魔道，花费了数十年以太古的奥秘制造出强力的魔种，放任它们进入草原掠食落单的北夷人成长。他幻想着复兴魔种的军队，去与邻近的楼兰，乃至大唐相抗衡。\n但放出的魔种再没有归来。它们在草原追猎人类之时，陷入了成千上百苍狼的包围。面对力量，身躯十倍于己的魔种，狼群却毫无畏惧。它们灵活的躲避伤害，前仆后继的朝着魔种的弱点攻击——听从那骑在庞大头狼上的青年的指挥，展开了巧妙的猎杀行动。\n传说中的先祖自深渊归来了吗？名叫铁木真的苍狼之子摒除了魔种的威胁，如同闪电般赢得了北夷青年们的拥戴和崇敬。在他的率领下，很快群狼和铁骑开始南下。曾经不可一世的花剌子模陷入了灭顶的恐惧。灾厄，深渊的灾厄……信仰魔道的国王颤抖着，绝望自尽。铁木真望着欢呼解放的城市，再次证实了自己的力量。\n花剌子模被吞并，使许多部落臣服于铁木真，奉他为王——成吉思汗。金帐汗国建立的盛大典礼上，甚至收到来自西方的异乡人的敬献。他拜见了汗王，试图说服他联合起来征服东方的唐国。为什么不呢？他们有着共同的渊源，而且利益更为一致。\n成吉思汗的目光却越过异乡人，投向凛冬之海的夜空。他是苍狼之子，他曾在草原之灵的指引下，自深渊中跋涉，他比世上大多数人都了解深渊和黑暗的秘密。异乡人不知道的是，他并非为凡人的欲望而生，命中注定将要击碎的，是曾凝望这个世界的星辰。\n\n “当你凝视深渊的时候 深渊也在凝视你。”"));

        //heroarray = new String[herolist.size()];
        //for(int i = 0; i < herolist.size(); i++) {
          //  heroarray[i] = herolist.get(i).getName();
        //}
    }
}
