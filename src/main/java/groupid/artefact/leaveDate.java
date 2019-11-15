package groupid.artefact;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class leaveDate implements Listener {


    @EventHandler
    void onPlayerDisconnect(PlayerQuitEvent event) {

        //чекаем когда игрок вышел
        long leaveDate = new Date().getTime();
        Bukkit.getConsoleSender().sendMessage("Время выхода: " + leaveDate);

        //ищем юзверя
        Player player = event.getPlayer();
        Document name = new Document("name", player.getName());

        Document found = (Document) Artefact.collection.find(name).first();

        long DurationX = leaveDate - found.getLong("joinDate");
        long sesDur = DurationX / 1000;

        //форматирую милисекунды в реальную дату
//        Date date = new Date(DurationX);
//        SimpleDateFormat formatter = new SimpleDateFormat("HH часов mm минут ss секунд");
//        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
//        String formatted = formatter.format(date);

//        System.out.println(found.getLong("joinDate"));

        if (found != null) {
            //ебучее обновление
            Bson updateLeaveTime = new Document("leaveDate", leaveDate);
            Bson updateLeaveOper = new Document("$set", updateLeaveTime);
            Artefact.collection.updateOne(found, updateLeaveOper);


            //ебучий костыль от Ильи
             name = new Document("name", player.getName());
             found = (Document) Artefact.collection.find(name).first();

            Bson updateDurationTime = new Document("Duration", sesDur);
            Bson updateOperDuration = new Document("$inc", updateDurationTime);
            Artefact.collection.updateOne(found,updateOperDuration);

        }
    }
}

