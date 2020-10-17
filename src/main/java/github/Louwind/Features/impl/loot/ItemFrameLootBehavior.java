package github.Louwind.Features.impl.loot;

import github.Louwind.Features.loot.LootBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Random;

import static net.minecraft.loot.context.LootContextParameters.ORIGIN;

public class ItemFrameLootBehavior implements LootBehavior<LootableContainerBlockEntity> {

    @Override
    public void setLootTable(Identifier lootTable, ServerWorld server, LootableContainerBlockEntity blockEntity, BlockPos pos, long seed) {
        LootContext context = new LootContext.Builder(server)
                .parameter(ORIGIN, Vec3d.ofCenter(pos))
                .random(seed)
                .build(LootContextTypes.CHEST);

        LootTable table = this.getLootTable(server, lootTable);

        List<ItemStack> loot = table.generateLoot(context);
        Random random = context.getRandom();

        Box box = new Box(pos);
        int size = loot.size();

        for (ItemFrameEntity itemFrame : server.getEntitiesByType(EntityType.ITEM_FRAME, box, entity -> true)) {
            int index = random.nextInt(size);
            ItemStack stack = loot.get(index);

            itemFrame.setHeldItemStack(stack);
        }

    }

}