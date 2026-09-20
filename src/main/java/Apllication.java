import Common.ConsoleReader;
import Common.ConsoleWriter;
import Controller.RacingController;
import Domain.RandomAdvanceDecider;
import View.RacingFormView;
import View.RacingResultView;

public final class Apllication {

    private Apllication() {
    }

    public static void main(String[] args) {
        var reader = ConsoleReader.system();
        var writer = ConsoleWriter.system();
        var formView = new RacingFormView(reader, writer);
        var resultView = new RacingResultView(writer);
        var decider = new RandomAdvanceDecider();
        var controller = new RacingController(formView, resultView, decider);

        controller.run();
    }
}
