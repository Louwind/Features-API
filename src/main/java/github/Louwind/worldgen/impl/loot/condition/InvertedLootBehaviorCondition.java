package github.Louwind.worldgen.impl.loot.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.Louwind.worldgen.loot.condition.LootBehaviorCondition;
import github.Louwind.worldgen.loot.condition.LootBehaviorConditionType;
import github.Louwind.worldgen.util.LootBehaviorConditionList;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.stream.Stream;

import static github.Louwind.worldgen.impl.init.LootBehaviorConditions.INVERTED;

public class InvertedLootBehaviorCondition implements LootBehaviorCondition<BlockEntity> {

    public static final Codec<InvertedLootBehaviorCondition> CODEC = RecordCodecBuilder.create((instance) -> instance.group(LootBehaviorConditionList.CODEC.fieldOf("terms").forGetter(handler -> handler.terms)).apply(instance, InvertedLootBehaviorCondition::new));

    protected final LootBehaviorConditionList terms;

    public InvertedLootBehaviorCondition(LootBehaviorConditionList terms) {
        this.terms = terms;
    }

    @Override
    public LootBehaviorConditionType<?> getType() {
        return INVERTED;
    }

    @Override
    public boolean test(LootContext context, ServerWorld server, BlockEntity blockEntity, BlockPos pos, long seed) {
        return !Stream.of(this.terms).map(condition -> condition.test(context, server, blockEntity, pos, seed)).reduce(false, Boolean::logicalAnd);
    }

}