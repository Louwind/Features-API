package github.Louwind.Features.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import github.Louwind.Features.condition.FeatureCondition;
import github.Louwind.Features.context.getter.FeatureContextGetter;
import github.Louwind.Features.context.override.FeatureContextOverride;
import github.Louwind.Features.context.parameter.FeatureContextParameter;
import github.Louwind.Features.context.parameter.OptionalContextParameter;
import github.Louwind.Features.context.provider.FeatureContextProvider;
import github.Louwind.Features.entry.FeatureEntry;
import github.Louwind.Features.function.FeatureFunction;
import github.Louwind.Features.impl.context.provider.GenericContextProvider;
import github.Louwind.Features.pool.FeaturePool;
import github.Louwind.Features.registry.FeaturesRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class FeaturesJsonHelper {

    public static Block getBlock(JsonObject object, String name) {
        Identifier id = FeaturesJsonHelper.getIdentifier(object, name);

        return Registry.BLOCK.get(id);
    }

    public static <T extends Enum> T getEnum(JsonObject object, Class<T>  clazz, String name) {
        String string = JsonHelper.getString(object, name);

        return (T) Enum.valueOf(clazz, string.toUpperCase());
    }

    public static Identifier getIdentifier(JsonObject object, String name) {
        String id = JsonHelper.getString(object, name);

        return new Identifier(id);
    }

    public static StructurePool.Projection getProjection(JsonObject object, String name) {
        String id = JsonHelper.getString(object, name);

        return StructurePool.Projection.getById(id);
    }

    public static FeatureCondition[] getConditions(JsonObject object, FeatureCondition[] defaultValue, JsonDeserializationContext context, String name) {
        return JsonHelper.deserialize(object, name, defaultValue, context, FeatureCondition[].class);
    }

    public static FeatureCondition[] getConditions(JsonObject object, JsonDeserializationContext context, String name) {
        return FeaturesJsonHelper.getConditions(object, new FeatureCondition[]{}, context, name);
    }

    public static FeatureContextParameter getContextParameter(JsonObject object, String name) {
        Identifier id = FeaturesJsonHelper.getIdentifier(object, name);

        return FeaturesRegistry.FEATURE_CONTEXT_PARAMETER.get(id);
    }

    public static FeatureContextProvider getContextProvider(JsonObject object, JsonDeserializationContext context, String name) {
        return JsonHelper.deserialize(object, name, GenericContextProvider.EMPTY, context, FeatureContextProvider.class);
    }

    public static <T> OptionalContextParameter<T> getOptionalContextParameter(JsonObject object, String name, Function<JsonElement, T> function) {

        if(object.has(name)) {
            JsonElement element = object.get(name);

            if(element.isJsonObject()) {
                FeatureContextParameter<T> parameter = FeaturesJsonHelper.getContextParameter(element.getAsJsonObject(), "parameter");

                return OptionalContextParameter.of(parameter);
            }

            return OptionalContextParameter.of(function.apply(element));
        }

        return OptionalContextParameter.empty();
    }

    public static OptionalBlockPos getOptionalBlockPos(JsonObject object, String name) {

        if(object.has(name)) {
            JsonElement element = object.get(name);

            if(element.isJsonObject()) {
                JsonObject pos = element.getAsJsonObject();

                if(pos.has("parameter")) {
                    FeatureContextParameter<BlockPos> parameter = FeaturesJsonHelper.getContextParameter(pos, "parameter");
                    OptionalContextParameter<BlockPos> optional = OptionalContextParameter.of(parameter);

                    return OptionalBlockPos.of(optional);
                }

                if(pos.has("x") || pos.has("y") || pos.has("z")) {
                    OptionalContextParameter<Integer> x = FeaturesJsonHelper.getOptionalContextParameter(pos, "x", JsonElement::getAsInt);
                    OptionalContextParameter<Integer> y = FeaturesJsonHelper.getOptionalContextParameter(pos, "y", JsonElement::getAsInt);
                    OptionalContextParameter<Integer> z = FeaturesJsonHelper.getOptionalContextParameter(pos, "z", JsonElement::getAsInt);

                    return OptionalBlockPos.of(x, y, z);
                }

            }

        }

        return OptionalBlockPos.empty();
    }

    public static FeatureEntry[] getEntries(JsonObject object, JsonDeserializationContext context, String name) {
        return JsonHelper.deserialize(object, name, new FeatureEntry[]{}, context, FeatureEntry[].class);
    }

    public static FeatureContextGetter[] getFrom(JsonObject object, FeatureContextGetter[] defaultValue, JsonDeserializationContext context, String name) {
        return JsonHelper.deserialize(object, "from", defaultValue, context, FeatureContextGetter[].class);
    }

    public static FeatureContextGetter[] getFrom(JsonObject object, JsonDeserializationContext context, String name) {
        return FeaturesJsonHelper.getFrom(object, new FeatureContextGetter[]{}, context, name);
    }

    public static FeatureFunction[] getFunction(JsonObject object, FeatureFunction[] defaultValue, JsonDeserializationContext context, String name) {
        return JsonHelper.deserialize(object, name, defaultValue, context, FeatureFunction[].class);
    }

    public static FeatureFunction[] getFunction(JsonObject object, JsonDeserializationContext context, String name) {
        return FeaturesJsonHelper.getFunction(object, new FeatureFunction[]{}, context, name);
    }

    public static StructureProcessorList getProcessors(JsonObject object, StructureProcessor[] defaultValue, JsonDeserializationContext context, String name) {

        if(object.has(name)) {
            JsonElement element = object.get(name);

            if(element.isJsonPrimitive()) {
                JsonPrimitive primitive = element.getAsJsonPrimitive();

                if(primitive.isString()) {
                    Identifier id = FeaturesJsonHelper.getIdentifier(object, name);

                    return BuiltinRegistries.STRUCTURE_PROCESSOR_LIST.get(id);
                }

            }
        }

        StructureProcessor[] processors = JsonHelper.deserialize(object, name, defaultValue, context, StructureProcessor[].class);

        return new StructureProcessorList(Arrays.asList(processors));
    }

    public static StructureProcessorList getProcessors(JsonObject object, JsonDeserializationContext context, String name) {
        return FeaturesJsonHelper.getProcessors(object, new StructureProcessor[]{}, context, name);
    }

    public static BlockRotation[] getRotations(JsonObject object, JsonDeserializationContext context, String name) {

        if(object.has(name)) {
            JsonElement element = object.get(name);

            if (element.isJsonPrimitive()) {
                JsonPrimitive primitive = element.getAsJsonPrimitive();

                if (primitive.isString()) {
                    String string = primitive.getAsString();

                    if (string.equals("all"))
                        return BlockRotation.values();

                    BlockRotation rotation = FeaturesJsonHelper.getEnum(object, BlockRotation.class, name);

                    if(rotation != null)
                        return new BlockRotation[] { rotation };
                }
            }
        }

        return new BlockRotation[]{};
    }

    public static FeatureContextOverride[] getContextOverrides(JsonObject object, FeatureContextOverride[] defaultValue, JsonDeserializationContext context, String name) {
        return JsonHelper.deserialize(object, name, defaultValue, context, FeatureContextOverride[].class);
    }

    public static FeatureContextOverride[] getContextOverrides(JsonObject object, JsonDeserializationContext context, String name) {
        return FeaturesJsonHelper.getContextOverrides(object, new FeatureContextOverride[]{}, context, name);
    }

    public static StructurePool getStructurePool(JsonObject object, String name) {
        Identifier id = FeaturesJsonHelper.getIdentifier(object, name);

        return BuiltinRegistries.STRUCTURE_POOL.get(id);
    }

    public static FeaturePool[] getPools(JsonObject object, JsonDeserializationContext context, String name) {
        return JsonHelper.deserialize(object, name, new FeaturePool[]{}, context, FeaturePool[].class);
    }

    public static StructureProcessorRule[] getProcessorRules(JsonObject object, JsonDeserializationContext context, String name) {
        return FeaturesJsonHelper.getProcessorRules(object, name, new StructureProcessorRule[]{}, context);
    }

    public static StructureProcessorRule[] getProcessorRules(JsonObject object, String name, StructureProcessorRule[] defaultValue, JsonDeserializationContext context) {
        return JsonHelper.deserialize(object, name, defaultValue, context, StructureProcessorRule[].class);
    }

    @Deprecated
    public static BlockState getBlockState(JsonObject object, String name) {
        JsonObject blockstate = object.getAsJsonObject(name);

        Block block = FeaturesJsonHelper.getBlock(blockstate, "block");
        JsonObject properties = JsonHelper.getObject(blockstate, "properties");

        BlockState state = block.getDefaultState();
        StateManager<?, ?> manager = block.getStateManager();

        for (Map.Entry<String, JsonElement> entry : properties.entrySet()) {
            Property<?> property = manager.getProperty(entry.getKey());

            if(property != null) {
                JsonElement element = entry.getValue();

                if(element.isJsonPrimitive()) {
                    JsonPrimitive primitive = element.getAsJsonPrimitive();

                    if(primitive.isString())
                        FeaturesJsonHelper.parsePropertyValue(property, state, primitive.getAsString());

                    if(primitive.isBoolean())
                        FeaturesJsonHelper.parsePropertyValue(property, state, String.valueOf(primitive.getAsBoolean()));

                    if(primitive.isNumber())
                        FeaturesJsonHelper.parsePropertyValue(property, state, String.valueOf(primitive.getAsInt()));
                }

            }

        }

        return state;
    }

    @Deprecated
    private static <T extends Comparable<T>> void parsePropertyValue(Property<T> property, BlockState state, String string) {
        property.parse(string).ifPresent(value -> state.with(property, value));
    }

}
