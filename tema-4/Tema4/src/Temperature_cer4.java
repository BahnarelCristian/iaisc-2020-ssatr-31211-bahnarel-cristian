import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;


public class Temperature_cer4 {
    public static void main(String[] args) throws Exception {
        String filename = "G:/Meh -.-/MAS-IAISC An 1/SSTR/Tema4/src/Temperature_cer3.fcl";
        FIS fis = FIS.load(filename, true);

        if (fis == null) {
            System.err.println("Can't load file: '" + filename + "'");
            System.exit(1);
        }

        // Get default function block
        FunctionBlock fb = fis.getFunctionBlock(null);

        // Set inputs
        fb.setVariable("temperature", 50);
        fb.setVariable("target", 3);

        // Evaluate
        fb.evaluate();

        // Show output variable's chart
        fb.getVariable("command").defuzzify();

        // Print ruleSet
        System.out.println(fb);
        System.out.println("Command: " + fb.getVariable("command").getValue());


        // Show
        JFuzzyChart.get().chart(fb);

        // Set inputs
        fis.setVariable("temperature", 3);
        fis.setVariable("target", 7);

        // Evaluate
        fis.evaluate();

        // Show output variable's chart
        Variable command = fb.getVariable("command");
        JFuzzyChart.get().chart(command, command.getDefuzzifier(), true);

        // Print ruleSet
        System.out.println(fis);


    }

}