package me.ixwavey.utilities.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unused")
public abstract class PluginCommand implements TabExecutor {

    protected final String command;
    private boolean allowConsole = true;
    private boolean allowPlayer = true;

    private final Set<String> permissions = new HashSet<>();
    private final Map<List<String>,Runnable> subCommands = new HashMap<>();

    protected abstract void execute(@NotNull final CommandSender sender, @NotNull final String[] args);

    protected PluginCommand(@NotNull final String command) {
        this.command = command;
    }

    /**
     * Registers the command on the server. This method should be called in the onEnable method in your main class.
     * @param plugin The plugin instance.
     */
    public void register(@NotNull Plugin plugin) {
        plugin.getServer().getPluginCommand(command).setExecutor(this);
    }

    /**
     * Whether the console should be allowed to perform this command.
     * @param allow True | False
     */
    protected void allowConsole(final boolean allow) {
        this.allowConsole = allow;
    }

    /**
     * Whether {@link Player}s should be allowed to perform this command.
     * @param allow True | False
     */
    protected void allowPlayer(final boolean allow) {
        this.allowPlayer = allow;
    }

    protected void addRequiredPermission(@NotNull String permission) {
        permissions.add(permission);
    }

    protected void addRequiredPermission(@NotNull String... permission) {
        permissions.addAll(List.of(permission));
    }

    protected void addSubCommand(@NotNull final Runnable runnable, @NotNull final String... commandPrefixes) {
        subCommands.put(List.of(commandPrefixes), runnable);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!allowConsole && sender instanceof ConsoleCommandSender) return true;
        if (!allowPlayer && sender instanceof Player) return true;
        for(String permission : permissions) if(!sender.hasPermission(permission)) return true;
        final Runnable subCommand = getSubCommand(args);

        if (subCommand != null) {
            subCommand.run();
            return true;
        }

        execute(sender, args);
        return true;
    }

    private Runnable getSubCommand(String[] args) {
        final int length = args.length;
        boolean match = true;
        Runnable matchFound = null;
        int matchCount = 0;
        int i = 0;
        for (var index : subCommands.entrySet()) {
            if (index.getKey().equals(Arrays.stream(args).toList())) return subCommands.get(index.getKey());
            for (String s : index.getKey()) {
                if (i < length && s.equalsIgnoreCase(args[i++])) continue;
                match = false; break;
            }
            if (match) {
                if (i+1 < length) {
                    if (i > matchCount) {
                        matchFound = subCommands.get(index.getKey());
                        matchCount = i;
                    }
                }
                else return subCommands.get(index.getKey());
            }
        }
        return matchFound;
    }
}