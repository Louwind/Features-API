package github.Louwind.Features.impl.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import github.Louwind.Features.loot.LootBehaviorType;
import github.Louwind.Features.util.LootBehaviorConditionList;
import github.Louwind.Features.util.json.FeaturesJsonHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Random;

import static github.Louwind.Features.impl.init.LootBehaviors.JUKEBOX;

public class JukeboxLootBehavior extends ConditionalLootBehavior<JukeboxBlockEntity> {

    public JukeboxLootBehavior(Identifier lootTableId, LootBehaviorConditionList conditions) {
        super(lootTableId, conditions);
    }

    @Override
    protected void process(LootContext context, ServerWorld server, JukeboxBlockEntity blockEntity, BlockPos pos, long seed) {
        var loot = this.getLootTable(server).generateLoot(context);
        var random = context.getRandom();

        var size = loot.size();
        var index = random.nextInt(size);
        var stack = loot.get(index);
        var state = server.getBlockState(pos);
        var jukebox = (JukeboxBlock) state.getBlock();

        jukebox.setRecord(server, pos, state, stack);
    }

    @Override
    public LootBehaviorType getType() {
        return JUKEBOX;
    }

    public static class Serializer extends ConditionalLootBehavior.Serializer<JukeboxLootBehavior> {

        @Override
        public void toJson(JsonObject json, JukeboxLootBehavior object, JsonSerializationContext context) {
            super.toJson(json, object, context);

            json.addProperty("loot_table", object.lootTableId.toString());
        }

        @Override
        public JukeboxLootBehavior fromJson(JsonObject json, JsonDeserializationContext context) {
            var conditions = FeaturesJsonHelper.getLootBehaviorConditions(json, context, "conditions");
            var lootTableId = FeaturesJsonHelper.getIdentifier(json, "loot_table");

            return new JukeboxLootBehavior(lootTableId, conditions);
        }

    }

}
