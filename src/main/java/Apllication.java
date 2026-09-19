import Controller.RacingController;
import Domain.RandomAdvanceDecider;
import View.ConsoleReader;
import View.RacingFormView;

public final class Apllication {

    private Apllication() {
    }

    public static void main(String[] args) {
        var formView = new RacingFormView(ConsoleReader.system());
        var decider = new RandomAdvanceDecider();
        var controller = new RacingController(formView, decider);

        controller.run();
    }
}
