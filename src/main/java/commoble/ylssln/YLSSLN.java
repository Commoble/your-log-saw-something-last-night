package commoble.ylssln;

import java.util.Locale;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.neoforged.neoforge.event.level.BlockEvent.BreakEvent;
import net.neoforged.neoforge.event.level.BlockEvent.EntityPlaceEvent;

@Mod(YLSSLN.MODID)
public class YLSSLN
{
	public static final String MODID = "ylssln";
	
	private final CommonConfig commonConfig;
	
	public YLSSLN()
	{
		IEventBus forgeBus = NeoForge.EVENT_BUS;
		
		// define configs
		this.commonConfig = ConfigHelper.register(MODID, ModConfig.Type.COMMON, CommonConfig::create);
		
		// subscribe events
		forgeBus.addListener(this::onBlockBreak);
		forgeBus.addListener(this::onBlockPlace);
		forgeBus.addListener(this::onBlockInteract);
		forgeBus.addListener(this::onItemInteract);
		forgeBus.addListener(this::onEntityInteractSpecific);
		forgeBus.addListener(this::onLivingDeath);
	}
	
	private boolean shouldPlayerBeLogged(ServerPlayer player)
	{
		return !(player instanceof FakePlayer);
	}
	
	public static String logPos(BlockPos pos)
	{
		return String.format(Locale.ENGLISH, "{x=%d,y=%d,z=%d}", pos.getX(), pos.getY(), pos.getZ());
	}
	
	@SuppressWarnings("deprecation")
	public static String logBlock(Block block)
	{
		return block.builtInRegistryHolder().key().location().toString();
	}
	
	@SuppressWarnings("deprecation")
	public static String logItem(Item item)
	{
		return item.builtInRegistryHolder().key().location().toString();
	}
	
	@SuppressWarnings("deprecation")
	public static String logEntity(Entity entity)
	{
		return entity.getType().builtInRegistryHolder().key().location().toString();
	}
	
	private void onBlockBreak(BreakEvent event)
	{
		if (event.getPlayer() instanceof ServerPlayer serverPlayer && this.shouldPlayerBeLogged(serverPlayer))
		{
			this.commonConfig.blockBreakConfig().get().log(serverPlayer,
				() -> String.format(Locale.ENGLISH, "[ylssln]event=BLOCK_BREAK;player=%s;pos=%s;block={%s}",
					serverPlayer.getDisplayName().getString(), logPos(event.getPos()), logBlock(event.getState().getBlock())));
		}
	}
	
	private void onBlockPlace(EntityPlaceEvent event)
	{
		if (event.getEntity() instanceof ServerPlayer serverPlayer && this.shouldPlayerBeLogged(serverPlayer))
		{
			this.commonConfig.blockPlaceConfig().get().log(serverPlayer,
				() -> String.format(Locale.ENGLISH, "[ylssln]event=BLOCK_PLACE;player=%s;pos=%s;block={%s}",
					serverPlayer.getDisplayName().getString(), logPos(event.getPos()), logBlock(event.getState().getBlock())));
		}
	}
	
	private void onBlockInteract(RightClickBlock event)
	{
		if (event.getEntity() instanceof ServerPlayer serverPlayer && this.shouldPlayerBeLogged(serverPlayer))
		{
			this.commonConfig.blockInteractConfig().get().log(serverPlayer,
				() -> String.format(Locale.ENGLISH, "[ylssln]event=BLOCK_INTERACT;player=%s;pos=%s;block={%s};hand=%s;item={%s}",
					serverPlayer.getDisplayName().getString(), logPos(event.getPos()), logBlock(event.getEntity().level().getBlockState(event.getPos()).getBlock()), event.getHand(), logItem(event.getItemStack().getItem())));
		}
	}
	
	private void onItemInteract(RightClickItem event)
	{
		if (event.getEntity() instanceof ServerPlayer serverPlayer && this.shouldPlayerBeLogged(serverPlayer))
		{
			this.commonConfig.itemInteractConfig().get().log(serverPlayer,
				() -> String.format(Locale.ENGLISH, "[ylssln]event=ITEM_INTERACT;player=%s;pos=%s;item={%s};hand=%s",
					serverPlayer.getDisplayName().getString(), logPos(event.getPos()), logItem(event.getItemStack().getItem()), event.getHand()));
		}
	}
	
	private void onEntityInteractSpecific(EntityInteractSpecific event)
	{
		if (event.getEntity() instanceof ServerPlayer serverPlayer && this.shouldPlayerBeLogged(serverPlayer))
		{
			this.commonConfig.entityInteractConfig().get().log(serverPlayer,
				() -> String.format(Locale.ENGLISH, "[ylssln]event=ENTITY_INTERACT;player=%s;pos=%s;entity={%s};hand=%s,item={%s}",
					serverPlayer.getDisplayName().getString(), logPos(event.getPos()), logEntity(event.getTarget()), event.getHand(), logItem(event.getItemStack().getItem())));
		}
	}
	
	private void onLivingDeath(LivingDeathEvent event)
	{
		DamageSource source = event.getSource();
		if (source == null)
			return;
		
		if (source.getEntity() instanceof ServerPlayer serverPlayer && this.shouldPlayerBeLogged(serverPlayer))
		{
			this.commonConfig.entityKillConfig().get().log(serverPlayer,
				() -> String.format(Locale.ENGLISH, "[ylssln]event=ENTITY_KILL;player=%s;pos=%s;entity={%s};damageSource=%s",
					serverPlayer.getDisplayName().getString(), logPos(event.getEntity().blockPosition()), logEntity(event.getEntity()), source.getMsgId()));
		}
	}
}
