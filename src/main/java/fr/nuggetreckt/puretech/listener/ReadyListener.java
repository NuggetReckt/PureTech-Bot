package fr.nuggetreckt.puretech.listener;

import fr.nuggetreckt.puretech.PureTech;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public class ReadyListener implements EventListener {

    private final PureTech instance;

    public ReadyListener(PureTech instance) {
        this.instance = instance;
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (!(event instanceof ReadyEvent)) return;

        instance.getLogger().info(instance.getJDA().getSelfUser().getName() + " v" + instance.getVersion() + " lancé avec succès.");
        instance.getLogger().info(instance.getJDA().getEventManager().getRegisteredListeners().size() + " listeners chargés.");
        instance.getLogger().info(instance.getJDA().getGuilds().size() + " serveurs utilisent le 1.2L PureTech");

        System.out.println("""
                                      -#%@#=                      \s
                    %%#**+==============================++*#%%    \s
                 *-==+*##%@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%%#*++=-+@\s
                 ++@@@@@@@@@@@%%@%@@@%@#***@@####@@%@@@@@@@@@@@#-@\s
                 ++@@%+**+%++++#@=@@@=@=@*+##-++*@=#**=@**=*%@@#-@\s
                =++@@%=**+%=+++#@=****@+**+*%+***@+**#-@@%+@@@@#:@\s
                %=+@@%#@@@@%%@@@@@@@@@@@@@@@@@@@@@@@@%@@@@#@@@@%:@*
                #=+@@@@@@@@@@@@@@@@@@@@@%%%@@@@@@@@@@@@@@@@@@@@%:@\s
                 ++@@@@@@@@@@@@@@@@#.-=++==--:.:+%@@@@@@@@@@@@@#-@\s
                 ++@@@@@@@@@@@@@@%*+**@@@@@@@@*=::-+%@@@@@@@@@@#-@\s
                 *=@@@@@@@@@@@@@%=:  *@@%#*+#@@@@#+*=+@@@@@@@@@*=@\s
                 #-@@@@@@@@@@%**-.-+#@@*-+#@=@@#*#+.:+#@@@@@@@@++ \s
                 @.@@@@@@@@@*-#+#@@@@@@@@@@=%@@@@@@@+. -#@@@@@@-# \s
                 @.%@@@@@@+.:-*%@@@@@@@@@@@+@@@@%@@@@@%=.@@@@@@.% \s
                 @=*@@@@*-..:=@@@@@@@%@@@@@%%@@@+-%@@@@@.%@@@@%:@ \s
                  *-@@@@#:.-=+#%%@@@@*%@@@@@@@@@% .#@@@@-%@@@@+== \s
                  @.@@@@@++#@@@@%+=@@=%@@@@@@@@@@=  *@@@-@@@@@:#  \s
                  ==+@@@@@@#@@@%#-#@-*@@@@@@@@@@@@#. @@#+@@@@#:@  \s
                   %:@@@@@@@%%#-:#%:-@@@@@@@@@@@@@@* %%+@@@@@-*   \s
                   :=+@@@@@@%:*%@%.:@@@@=#@@@@@@@@@# #=@@@@@#:@   \s
                    @:%@@@@@@=#%*.=%@@@%.@@@@@@@@@@# @@@@@@@:%    \s
                     #:@@@@@@@@*.#@@@@@-=@@@@@@@@@@#:@@@@@@=+     \s
                      +=@@@@@@@-+@@@@@# #@@@@@@@@@@++@@@@@*-%     \s
                      :==@@@@@@==@@@@@-.@@@@#@@@@%=+@@@@@*-@      \s
                       -==@@@@@@==@@@@.-@@@*%@@%++@@@@@@*-@       \s
                         +-@@@@@@*-@@@#:@@%-@@++@@@@@@@+-%        \s
                          #:#@@@@@%-#@@+*@+-#+@@@@@@@%-+          \s
                           %-+@@@@@@+*@@=%@:#@@@@@@@*-#           \s
                             *-#@@@@@%*%@+@@@@@@@@%-++            \s
                              #+-#@@@@@@@@@@@@@@%==%              \s
                                %+-*@@@@@@@@@@#==%                \s
                                  =*-+%@@@@%+-+%                  \s
                                     %=-++-=#                     \s
                                        %#                        \s
                                                                  \s
                                  1.2L PureTech
                """);
    }
}
