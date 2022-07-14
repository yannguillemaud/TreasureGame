package treasuregame.main;

import treasuregame.factory.game.FileGameFactory;
import treasuregame.factory.component.GameComponentFactory;
import treasuregame.factory.game.GameFactory;
import treasuregame.factory.component.StringComponentFactory;
import treasuregame.observer.ConsoleWriterObserver;
import treasuregame.observer.FileWriterObserver;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1) throw new IllegalArgumentException();

        GameComponentFactory<String> stringComponentFactory = new StringComponentFactory();
        GameFactory<Path> fileGameFactory = new FileGameFactory(stringComponentFactory);

        fileGameFactory
            .fromSource(Path.of(args[0]))
            .ifPresent(iGame -> {
                iGame.registerObserver(new ConsoleWriterObserver());
                iGame.registerObserver(new FileWriterObserver());
                iGame.runGame();
            });
    }
}
