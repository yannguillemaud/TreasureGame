package treasuregame.observer;

import treasuregame.game.BasicGameImpl;
import treasuregame.visitor.component.ComponentToStringVisitor;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

/**
 * Observer used to print the result of an observed game
 * Print the result in the System.console
 */
public class ConsoleWriterObserver implements BasicGameObserver {
    private static final ComponentToStringVisitor TO_STRING_VISITOR = new ComponentToStringVisitor();

    @Override
    public void onGameStop(BasicGameImpl game) {
        System.out.println(
                game.getComponents().stream()
                .map(TO_STRING_VISITOR::visit)
                .filter(not(StringBuilder::isEmpty))
                .collect(Collectors.joining("\n", game.getSizeMap().concat("\n"), ""))
        );
    }
}
