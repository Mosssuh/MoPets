package mv.mossuh.mopets.UTILITIES.Rewards;

import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;

public class VariableSeparator {

    private String string = "";
    private boolean countValue = false;
    private VariableArg variable = new VariableArg(null, null);
    public VariableSeparator(String string, Boolean countValue) {
        if (string != null) { this.string = string; }
        if (countValue != null) { this.countValue = countValue; }

        if (this.countValue) {
            // SET_VARIABLE -> a_variable::test
            separateNormalValueVariable();
        } else {
            // REMOVE_VARIABLE -> a_variable
            separateNormalNoValueVariable();
        }
    }

    public VariableArg getVariable() { return variable; }

    private void separateNormalValueVariable() {
        String[] stringSplit = this.string.split("::", 2);
        if (stringSplit.length == 2) {
            this.variable = new VariableArg(stringSplit[0], stringSplit[1]);
        }
    }

    private void separateNormalNoValueVariable() {
        this.variable = new VariableArg(this.string, null);
    }
}
