package com.itemshare;

import com.google.inject.Provides;
import static com.itemshare.constant.ItemShareConstants.ICON;
import com.itemshare.model.ItemShareContainer;
import com.itemshare.model.ItemShareData;
import com.itemshare.model.ItemSharePlayer;
import com.itemshare.service.ItemShareCloudService;
import com.itemshare.service.ItemShareConfigService;
import com.itemshare.service.ItemShareDataService;
import com.itemshare.ui.ItemSharePanel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.banktags.BankTagsPlugin;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@PluginDescriptor(
	name = "Item Share"
)
@PluginDependency(BankTagsPlugin.class)
public class ItemSharePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ItemShareConfig config;

	@Inject
	private ItemShareDataService dataService;

	@Inject
	private ItemShareConfigService configService;

	@Inject
	private ItemShareCloudService cloudService;

	@Inject
	private ClientToolbar toolbar;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ItemManager itemManager;

	private String playerName;
	private ItemShareData data;
	private ItemSharePlayer player;
	private ItemSharePanel panel;

	@Provides
	ItemShareConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ItemShareConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		loadData();
		loadUI();
		updateUI();
	}

	@Override
	protected void shutDown() throws Exception
	{
		saveData();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		loadPlayer();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged e)
	{
		if (isValidState())
		{
			if (e.getGameState().equals(GameState.LOGGED_IN))
			{
				loadPlayer();
				updateUI();
			}
			else
			{
				saveData();
			}
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (isValidState())
		{
			loadItems(event);
		}
	}

	private boolean isSupportedWorld()
	{
		return client.getWorldType().stream().noneMatch(EnumSet.of(
			WorldType.NOSAVE_MODE,
			WorldType.TOURNAMENT_WORLD,
			WorldType.DEADMAN,
			WorldType.SEASONAL
		)::contains);
	}

	private void loadData()
	{
		ArrayList<ItemSharePlayer> players = cloudService.getPlayers();
		List<String> names = players.stream().map(ItemSharePlayer::getUserName).collect(Collectors.toList());

		data = configService.getLocalData();
		data.getPlayers().removeIf(p -> names.contains(p.getUserName()));
		data.getPlayers().addAll(players);
	}

	private void loadItems(ItemContainerChanged event)
	{
		ItemContainer container = event.getItemContainer();

		if (container == client.getItemContainer(InventoryID.BANK))
		{
			loadBank(container);
		}
		else if (container == client.getItemContainer(InventoryID.INVENTORY))
		{
			loadInventory(container);
		}
		else if (container == client.getItemContainer(InventoryID.EQUIPMENT))
		{
			loadEquipment(container);
		}
	}

	private void loadEquipment(ItemContainer container)
	{
		player.setEquipment(dataService.getItemContainer(container));
		player.setUpdatedDate(new Date());

		updateUI();
	}

	private void loadInventory(ItemContainer container)
	{
		player.setInventory(dataService.getItemContainer(container));
		player.setUpdatedDate(new Date());

		updateUI();
	}

	private void loadBank(ItemContainer container)
	{
		player.setBank(dataService.getItemContainer(container));
		player.setUpdatedDate(new Date());

		updateUI();
	}

	private void loadPlayer()
	{
		if (playerName == null)
		{
			playerName = getPlayerName();
		}
		else if (player == null)
		{
			player = getPlayer();
		}
//		else if (player.getEquipment() == null)
//		{
//			ItemContainer container = client.getItemContainer(InventoryID.EQUIPMENT);
//			loadEquipment(container);
//		}
//		else if (player.getInventory() == null)
//		{
//			ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);
//			loadInventory(container);
//		}
//		else if (player.getBank() == null)
//		{
//			ItemContainer container = client.getItemContainer(InventoryID.BANK);
//			loadBank(container);
//		}
	}

	private String getPlayerName()
	{
		Player player = client.getLocalPlayer();
		return player == null ? null : player.getName();
	}

	private ItemSharePlayer getPlayer()
	{
		return data.getPlayers().stream()
			.filter(player -> player.getUserName().equals(playerName))
			.findFirst()
			.orElseGet(() -> addPlayer(data, playerName));
	}

	private ItemSharePlayer addPlayer(ItemShareData data, String name)
	{
		ItemSharePlayer player = ItemSharePlayer.builder()
			.userName(playerName)
			.build();

		data.getPlayers().add(player);
		return player;
	}

	private boolean isValidState()
	{
		return isSupportedWorld()
			&& player != null
			&& !StringUtils.isEmpty(player.getUserName())
			&& data != null;
	}

	private void loadUI()
	{
		assert SwingUtilities.isEventDispatchThread();
		panel = injector.getInstance(ItemSharePanel.class);

		BufferedImage icon = ImageUtil.loadImageResource(ItemSharePlugin.class, ICON);
		NavigationButton button = NavigationButton.builder()
			.tooltip("View shared items")
			.icon(icon)
			.priority(7)
			.panel(panel)
			.build();

		toolbar.addNavigation(button);
	}

	private void updateUI()
	{
		SwingUtilities.invokeLater(() -> panel.update(itemManager, data));
	}

	private void saveData()
	{
		configService.saveLocalData(data);
		cloudService.savePlayerData(player);
	}
}
