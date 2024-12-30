package fr.nuggetreckt.puretech.util;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static final int TRIGGER_COUNT = 40;

    private final List<String> messages;

    public Config() {
        this.messages = new ArrayList<>();
        setMessages();
    }

    private void setMessages() {
        messages.clear();

        messages.add("Zut ! Mon carter d’huile est vide !");
        messages.add("Oups ! Des morceaux de ma courroie de distribution ont annihilé ma segmentation !");
        messages.add("Aïe, je viens de faire ma culasse.");
        messages.add("Bon sang ! Il y a trop de SP95 dans mon huile !");
        messages.add("Faudrait peut-être faire ma vidange. La dernière date d’il y a 400km !");
        messages.add("Merde ! Faut changer ma distrib ! La courroie est craquelée de partout !");
        messages.add("Aidez moi ! La crépine de ma pompe à huile est bouchée !");
        messages.add("Regardez moi ! J’ai été élu « Moteur de l’année » entre 2015 et 2018 !");
        messages.add("Oh ! Encore un voyant moteur !");
    }

    public List<String> getMessages() {
        return messages;
    }
}
