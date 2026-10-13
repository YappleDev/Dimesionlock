package de.dimensionlock;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DimensionLockMod implements ModInitializer {
    public static final String MOD_ID = "dimensionlock";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final List<String> DIMENSIONS = List.of("nether", "end");

    @Override
    public void onInitialize() {
        DimensionLockState.load();

        // /dimension <dimension> lock|unlock
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            dispatcher.register(Commands.literal("dimension")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("dimension", StringArgumentType.word())
                    .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(DIMENSIONS, builder))
                    .then(Commands.literal("lock").executes(ctx -> set(ctx, true)))
                    .then(Commands.literal("unlock").executes(ctx -> set(ctx, false)))))
        );
    }

    private static int set(CommandContext<CommandSourceStack> ctx, boolean lock) {
        String dim = StringArgumentType.getString(ctx, "dimension").toLowerCase();
        if (!DIMENSIONS.contains(dim)) {
            ctx.getSource().sendFailure(Component.literal("Unbekannte Dimension '" + dim + "'. Erlaubt: nether, end"));
            return 0;
        }
        DimensionLockState.setLocked(dim, lock);
        String label = dim.equals("nether") ? "Nether" : "End";
        ctx.getSource().sendSuccess(
            () -> Component.literal(label + " ist jetzt " + (lock ? "gesperrt." : "freigegeben.")), true);
        return 1;
    }
}
