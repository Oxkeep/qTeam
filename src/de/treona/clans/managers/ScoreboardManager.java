package de.treona.clans.managers;

import de.treona.clans.Clans;
import de.treona.clans.common.Clan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.UUID;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ScoreboardManager {

    private Scoreboard scoreboard;
    private final JavaPlugin plugin;

    public ScoreboardManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        Bukkit.getScheduler().runTask(this.plugin, () -> this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard());
        List<Clan> clans = Clans.getClans();
        clans.forEach(clan -> {
            Team team = scoreboard.registerNewTeam(clan.getClanTag());
            team.setPrefix(ChatColor.GRAY + "[" + ChatColor.AQUA + clan.getClanTag() + ChatColor.GRAY + "] ");
            clan.getMembers().forEach(member -> team.addEntry(Bukkit.getOfflinePlayer(member).getName()));
        });
    }

    public void removeClan(Clan clan) {
        Team team = this.scoreboard.getTeam(clan.getClanTag());
        if (team != null) {
            team.unregister();
        }
    }

    public void registerClan(Clan clan) {
        Team team = this.scoreboard.getTeam(clan.getClanTag());
        if (team == null) {
            team = this.scoreboard.registerNewTeam(clan.getClanTag());
        }
        for (UUID uuid : clan.getMembers()) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if(!team.hasEntry(offlinePlayer.getName())){
                team.addEntry(offlinePlayer.getName());
            }
        }
    }

    public void removePlayer(Clan clan, OfflinePlayer offlinePlayer) {
        this.removePlayer(clan, offlinePlayer.getName());
    }

    public void removePlayer(Clan clan, Player player) {
        this.removePlayer(clan, player.getName());
    }

    public void removePlayer(Clan clan, String entry) {
        Team team = this.scoreboard.getTeam(clan.getClanTag());
        if (team == null) {
            return;
        }
        if (team.hasEntry(entry)) {
            team.removeEntry(entry);
        }
    }

    public void addPlayer(Clan clan, OfflinePlayer player) {
        this.addPlayer(clan, player.getName());
    }

    public void addPlayer(Clan clan, Player player) {
        this.addPlayer(clan, player.getName());
    }

    public void addPlayer(Clan clan, String entry) {
        Team team = this.scoreboard.getTeam(clan.getClanTag());
        if (team == null) {
            return;
        }
        if (!team.hasEntry(entry)) {
            team.addEntry(entry);
        }
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
