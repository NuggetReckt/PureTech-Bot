package fr.nuggetreckt.puretech.listener;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntiSpamListener extends ListenerAdapter {

    private static final int MAX_MESSAGES = 8;
    private static final long TIME_WINDOW_MS = 5000;
    private static final long MUTE_DURATION_SEC = 30;

    private final Map<String, Deque<Long>> userMessageTimestamps;
    private final Map<String, Boolean> mutedUsers;
    private final ScheduledExecutorService scheduler;

    private final List<String> messages;
    private final Random random;

    public AntiSpamListener() {
        this.userMessageTimestamps = new ConcurrentHashMap<>();
        this.mutedUsers = new ConcurrentHashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.messages = new ArrayList<>();
        this.random = new Random();

        messages.add("Tu spam trop ! Tu vas attendre que je refasse ma segmentation avant de reparler !");
        messages.add("Tototo le spam là ! Tu vas me regarder faire ma vidange, mais c’est en silence !");
        messages.add("Doucement le spam ! Attends que je débouche ma crépine d’huile avant de reparler !");
        messages.add("Tu spam trop, alors tu vas attendre jusqu’à ce que je change ma courroie de distribution !");
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        Member member = event.getMember();

        if (member == null || member.hasPermission(Permission.ADMINISTRATOR)) return;
        Message message = event.getMessage();
        String userId = event.getAuthor().getId();

        if (Boolean.TRUE.equals(mutedUsers.get(userId))) {
            message.delete().queue();
            return;
        }
        if (isSpamming(userId)) {
            message.delete().queue();
            applyMute(member, message, userId);
        }
    }

    private boolean isSpamming(String userId) {
        long now = System.currentTimeMillis();

        userMessageTimestamps.putIfAbsent(userId, new ArrayDeque<>());
        Deque<Long> timestamps = userMessageTimestamps.get(userId);

        timestamps.removeIf(t -> now - t > TIME_WINDOW_MS);

        timestamps.addLast(now);
        return timestamps.size() > MAX_MESSAGES;
    }

    private void applyMute(@NotNull Member member, Message message, String userId) {
        mutedUsers.put(userId, true);

        member.timeoutFor(MUTE_DURATION_SEC, TimeUnit.SECONDS)
            .queue(
                success -> {
                    message.getChannel()
                        .sendMessage("> " + member.getAsMention() + " " + messages.get(random.nextInt(messages.size())))
                        .queue();
                },
                error -> System.err.println("Impossible de mute : " + error.getMessage())
            );

        scheduler.schedule(
            () -> mutedUsers.remove(userId),
            MUTE_DURATION_SEC, TimeUnit.SECONDS
        );
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        scheduler.shutdown();
    }
}