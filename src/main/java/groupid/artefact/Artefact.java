package groupid.artefact;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.sun.javafx.image.BytePixelSetter;
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

    //сам бох знает что это за дерьмо
    static long joinDate;
    static long getJoinDate(){ return joinDate; }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new leaveDate(), this);
        // Подключение к базе
        MongoClient mongoClient = MongoClients.create("mongodb+srv://server:123456)@timetracker-n8llr.mongodb.net/test?retryWrites=true&w=majority");
        collection = mongoClient.getDatabase("spigot").getCollection("users");

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
            //ебучее обновление
            Bson updateJoinTime = new Document("joinDate", joinDate);
            Bson updateOper = new Document("$set", updateJoinTime);
            collection.updateOne(found, updateOper);
        } else {
            //ебучая вставка если не нашел
            collection.insertOne(timers);
        }
    }
}


// != null