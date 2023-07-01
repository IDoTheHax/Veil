package foundry.veil.shader.compiler;

import foundry.veil.shader.ProgramDefinition;
import foundry.veil.shader.ShaderException;
import foundry.veil.shader.definition.ShaderPreDefinitions;
import foundry.veil.shader.processor.ShaderPreProcessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeResource;

import java.io.IOException;

/**
 * <p>Creates and compiles shaders for shader programs.</p>
 * <p>Create a compiler using {@link #direct(ResourceProvider)} for a single program,
 * or {@link #cached(ResourceProvider)} if compiling multiple.</p>
 *
 * @author Ocelot
 */
public interface ShaderCompiler extends NativeResource {

    /**
     * Creates a new shader and attempts to attach sources read from file to it.
     * The sources are read from
     * The shader will automatically be deleted at some point in the future.
     *
     * @param context The context for compiling the shader
     * @param type    The type of shader to create
     * @param id      The id of the shader to attach
     * @return A new shader that can be attached to programs
     * @throws IOException     If the file could not be found.
     * @throws ShaderException If an error occurs compiling the shader
     */
    CompiledShader compile(Context context, int type, ResourceLocation id) throws IOException, ShaderException;

    /**
     * Creates a new shader and attempts to attach the specified sources to it.
     * The shader will automatically be deleted at some point in the future.
     *
     * @param context The context for compiling the shader
     * @param type    The type of shader to create
     * @param source  The source of the shader to attach
     * @return A new shader that can be attached to programs
     * @throws IOException     If an error occurs processing the shader source
     * @throws ShaderException If an error occurs compiling the shader
     */
    CompiledShader compile(Context context, int type, String source) throws IOException, ShaderException;

    /**
     * Adds the specified pre-processor to the end of the stack.
     *
     * @param processor The processor to add
     */
    void addPreprocessor(ShaderPreProcessor processor);

    /**
     * Adds the default preprocessors for shader code.
     */
    ShaderCompiler addDefaultProcessors();

    /**
     * Constructs a shader compiler that creates a new shader for each requested type.
     *
     * @param provider The source of shader files
     * @return shader compiler
     */
    static ShaderCompiler direct(@Nullable ResourceProvider provider) {
        return new DirectShaderCompiler(provider);
    }

    /**
     * Constructs a shader compiler that caches duplicate shader sources.
     *
     * @param provider The source of shader files
     * @return cached shader compiler
     */
    static ShaderCompiler cached(@Nullable ResourceProvider provider) {
        return new CachedShaderCompiler(provider);
    }

    /**
     * Context for compiling shaders and programs.
     *
     * @param preDefinitions The set of all shader pre-definitions
     * @param definition     The definition the shader is being compiled for or <code>null</code> if there is no program
     */
    record Context(ShaderPreDefinitions preDefinitions, @Nullable ProgramDefinition definition) {
    }
}
