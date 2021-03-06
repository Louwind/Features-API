package github.Louwind.worldgen.metadata;

import com.mojang.serialization.Codec;

import static github.Louwind.worldgen.registry.Registries.METADATA_HANDLER_TYPE;

public interface MetadataHandlerType<T extends MetadataHandler> {

    Codec<MetadataHandler> CODEC = METADATA_HANDLER_TYPE.dispatch("metadata_type", MetadataHandler::getType, MetadataHandlerType::codec);

    Codec<T> codec();

}