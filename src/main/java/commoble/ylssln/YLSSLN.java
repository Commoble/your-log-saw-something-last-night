package commoble.ylssln;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commoble.databuddy.config.ConfigHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;

@Mod(YLSSLN.MODID)
public class YLSSLN
{
	public static final String MODID = "ylssln";
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final CommonConfig commonConfig;
	
	public YLSSLN()
	{
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		
		// mark mod as not required on clients
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
			() -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (remote, isServer)->true));
		
		// define configs
		this.commonConfig = ConfigHelper.register(ModConfig.Type.COMMON, CommonConfig::create);
		
		// subscribe events
		forgeBus.addListener(this::onBlockBreak);
		forgeBus.addListener(this::onBlockPlace);
		forgeBus.addListener(this::onBlockInteract);
	}
	
	private boolean playerShouldBeLogged(ServerPlayer player)
	{
		if (player instanceof FakePlayer)
			return false;
		
		int permLevel = player.getLevel().getServer().getProfilePermissions(player.getGameProfile());
		return permLevel >= this.commonConfig.minPermLevel().get() && permLevel <= this.commonConfig.maxPermLevel().get();
	}
	
	private void log(String message)
	{
		for (LogRule rule : this.commonConfig.rules().get())
		{
			if (rule.regexFilter().map(p->p.asMatchPredicate().test(message)).orElse(true))
			{
				LOGGER.log(rule.logLevel(), message);
				break;
			}
		}
	}
	
	private void onBlockBreak(BreakEvent event)
	{
		if (Boolean.FALSE.equals(this.commonConfig.logBlockBreak().get()))
			return;
		
		Player player = event.getPlayer();
		if (player instanceof ServerPlayer serverPlayer && this.playerShouldBeLogged(serverPlayer))
		{
			BlockPos pos = event.getPos();
			Block block = event.getState().getBlock();
			String message = String.format("[ylssln]event=BLOCK_BREAK;player=%s;pos=%s;block=%s",
				player.getDisplayName().getString(), pos, block);
			this.log(message);
		}
	}
	
	private void onBlockPlace(EntityPlaceEvent event)
	{
		if (Boolean.FALSE.equals(this.commonConfig.logBlockPlace().get()))
			return;
		
		Entity entity = event.getEntity();
		if (entity instanceof ServerPlayer serverPlayer && this.playerShouldBeLogged(serverPlayer))
		{
			BlockPos pos = event.getPos();
			Block block = event.getState().getBlock();
			String message = String.format("[ylssln]event=BLOCK_PLACE;player=%s;pos=%s;block=%s",
				serverPlayer.getDisplayName().getString(), pos, block);
			this.log(message);
		}
	}
	
	private void onBlockInteract(RightClickBlock event)
	{
		if (Boolean.FALSE.equals(this.commonConfig.logBlockInteract().get()))
			return;
		
		Player player = event.getPlayer();
		if (player instanceof ServerPlayer serverPlayer && this.playerShouldBeLogged(serverPlayer))
		{
			BlockPos pos = event.getPos();
			Block block = event.getEntity().getLevel().getBlockState(pos).getBlock();
			String message = String.format("[ylssln]event=BLOCK_INTERACT;player=%s;pos=%s;block=%s",
				serverPlayer.getDisplayName().getString(), pos, block);
			this.log(message);
		}
		
	}
}
