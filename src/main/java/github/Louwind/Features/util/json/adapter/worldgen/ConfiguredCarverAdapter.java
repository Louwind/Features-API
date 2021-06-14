package github.Louwind.Features.util.json.adapter.worldgen;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import github.Louwind.Reload.core.util.json.DynamicRegistryAdapter;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.dynamic.RegistryReadingOps;
import net.minecraft.world.gen.carver.ConfiguredCarver;

import java.lang.reflect.Type;

public class ConfiguredCarverAdapter extends DynamicRegistryAdapter<ConfiguredCarver<?>> {

    @Override
    protected ConfiguredCarver<?> fromJson(RegistryOps<JsonElement> registryOps, JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return ConfiguredCarver.CODEC.parse(registryOps, json).get().orThrow();
    }

    @Override
    protected JsonElement toJson(RegistryReadingOps<JsonElement> registryReadingOps, ConfiguredCarver<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return ConfiguredCarver.CODEC.encodeStart(registryReadingOps, src).get().orThrow();
    }

}