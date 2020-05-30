package groupid.artefact;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Date;

public class Artefact extends JavaPlugin implements Listener {

    public  static MongoCollection<Document> collection;

    //сам бох знает что это но без него не работает
    static long joinDate;
    static long getJoinDate(){ return joinDate; }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new leaveDate(), this);
        // Подключение к базе
        MongoClient mongoClient = MongoClients.create("mongodb+srv://MiXeR54:<password>@mern-s1bxs.mongodb.net/test?retryWrites=true&w=majority");
        collection = mongoClient.getDatabase("TimeTracker").getCollection("Users");

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        joinDate =  new Date().getTime();
        Bukkit.getConsoleSender().sendMessage("Время входа: " + joinDate);

        Player player = event.getPlayer();
        Document name = new Document("name", player.getName());
//                .append("joinDate", joinDate)
//                .append("leaveDate", 0)
//                .append("Duration", 0);

        Document timers = new Document("name", player.getName())
                .append("joinDate", joinDate)
                .append("leaveDate", 0)
                .append("Duration", 0);

        //поиск в базе
        Document found = (Document) collection.find(name).first();
        if (found != null){
            // обновление
            Bson updateJoinTime = new Document("joinDate", joinDate);
            Bson updateOper = new Document("$set", updateJoinTime);
            collection.updateOne(found, updateOper);
        } else {
            // вставка если не нашел
            collection.insertOne(timers);
        }
    }
}