package com.booksaw.betterTeams;

import com.booksaw.betterTeams.message.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

class PlayerTeleportTest {

	@Test
	void sendsSuccessMessageAfterScheduledTeleportSucceeds() throws Exception {
		verifyTeleportMessage(true);
	}

	@Test
	void doesNotSendSuccessMessageWhenTeleportIsCancelled() throws Exception {
		verifyTeleportMessage(false);
	}

	private void verifyTeleportMessage(boolean succeeds) throws Exception {
		Main previousPlugin = Main.plugin;
		Main plugin = mock(Main.class);
		Player player = mock(Player.class);
		BukkitScheduler scheduler = mock(BukkitScheduler.class);
		Location destination = mock(Location.class);
		when(destination.getWorld()).thenReturn(mock(World.class));
		when(plugin.getConfig()).thenReturn(new YamlConfiguration());
		when(player.getLocation()).thenReturn(destination);
		when(player.teleport(destination)).thenReturn(succeeds);
		Main.plugin = plugin;
		try (MockedStatic<Bukkit> bukkit = mockStatic(Bukkit.class);
			 MockedStatic<MessageManager> messages = mockStatic(MessageManager.class)) {
			bukkit.when(Bukkit::getScheduler).thenReturn(scheduler);
			new PlayerTeleport(player, destination, "home.success");
			verify(player, never()).teleport(destination);
			ArgumentCaptor<Runnable> task = ArgumentCaptor.forClass(Runnable.class);
			verify(scheduler).runTask(eq(plugin), task.capture());
			task.getValue().run();
			verify(player).teleport(destination);
			messages.verify(() -> MessageManager.sendMessage(player, "home.success"),
					times(succeeds ? 1 : 0));
		} finally {
			Main.plugin = previousPlugin;
		}
	}
}
