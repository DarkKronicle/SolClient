package io.github.solclient.client.mod.impl.hud.bedwarsoverlay;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;


@AllArgsConstructor
public enum BedwarsTeam {
    AQUA('b', 'A'),
    WHITE('f', 'W'),
    PINK('d', 'P'),
    GRAY('8', 'S'),
    RED('c', 'R'),
    BLUE('9', 'B'),
    GREEN('a', 'G'),
    YELLOW('e', 'Y')
    ;

    @Getter
    private final char code;

    @Getter
    private final char prefix;

    public static Optional<BedwarsTeam> fromPrefix(char prefix) {
        for (BedwarsTeam t : values()) {
            if (t.getPrefix() == prefix) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

}