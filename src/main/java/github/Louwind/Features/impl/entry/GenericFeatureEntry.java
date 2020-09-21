package github.Louwind.Features.impl.entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import github.Louwind.Features.context.setter.FeatureContextSetter;
import github.Louwind.Features.entry.FeatureEntry;
import github.Louwind.Features.entry.FeatureEntryType;
import github.Louwind.Features.function.FeatureFunction;
import github.Louwind.Features.impl.FeatureEntryTypes;
import github.Louwind.Features.util.FeaturesJsonHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;

import java.util.Arrays;
import java.util.List;

public class GenericFeatureEntry implements FeatureEntry {

	private final List<FeatureFunction> functions;
	private final List<FeatureContextSetter> setters;
	private final Identifier structureId;

	public GenericFeatureEntry(Identifier structureId, FeatureFunction[] functions, FeatureContextSetter[] setters) {
		this.functions = Arrays.asList(functions);
		this.setters = Arrays.asList(setters);
		this.structureId = structureId;
	}

	@Override
	public List<FeatureFunction> getFunctions() {
		return this.functions;
	}

	@Override
	public List<FeatureContextSetter> getSetters() {
		return this.setters;
	}

	@Override
	public Identifier getStructureId() {
		return this.structureId;
	}

	@Override
	public FeatureEntryType getType() {
		return FeatureEntryTypes.ENTRY;
	}

	public static class Serializer implements JsonSerializer<GenericFeatureEntry> {

		@Override
		public void toJson(JsonObject json, GenericFeatureEntry object, JsonSerializationContext context) {
			// TODO toJson
		}

		@Override
		public GenericFeatureEntry fromJson(JsonObject json, JsonDeserializationContext context) {
			Identifier structureId = FeaturesJsonHelper.getIdentifier(json, "structure");

			FeatureFunction[] functions = FeaturesJsonHelper.getFunction(json, context, "functions");
			FeatureContextSetter[] setters = FeaturesJsonHelper.getSetters(json, context, "setters");

			return new GenericFeatureEntry(structureId, functions, setters);
		}

	}

}
