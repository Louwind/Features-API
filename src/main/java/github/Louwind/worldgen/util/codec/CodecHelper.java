package github.Louwind.worldgen.util.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.DoubleStream;

public class CodecHelper {

    public static final Codec<BlockRotation> ROTATION = createEnum(BlockRotation::valueOf);

    public static final Codec<Vec3d> VEC3D = PrimitiveCodecHelper.DOUBLE_STREAM.comapFlatMap(stream -> toArray(stream, 3).map(doubles -> new Vec3d(doubles[0], doubles[1], doubles[2])), vec3d -> DoubleStream.of(vec3d.getX(), vec3d.getY(), vec3d.getZ()));

    public static <E extends Enum<?>> Codec<E> createEnum(Function<String, E> valueOf) {
        return Codec.STRING.comapFlatMap(str -> {
            E value = valueOf.apply(str.toUpperCase());

            return DataResult.success(value);
        }, enumValue -> enumValue.name().toLowerCase()).stable();
    }

    public static DataResult<double[]> toArray(DoubleStream stream, int length) {
        double[] doubles = stream.limit(length + 1).toArray();

        if (doubles.length != length) {
            String message = "Input is not a list of " + length + " doubles";

            return doubles.length >= length ? DataResult.error(message, Arrays.copyOf(doubles, length)) : DataResult.error(message);
        } else
            return DataResult.success(doubles);
    }

}
