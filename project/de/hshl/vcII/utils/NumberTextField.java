package project.de.hshl.vcII.utils;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {
    @Override
    public void replaceText(int beg, int end, String input) {
        if(input.matches("[\\-]?[0-9]+"))
            super.replaceText(beg, end, input);
        else
            super.replaceText(beg, end, "0.0");
    }

    @Override
    public void replaceSelection(String input) {
        if(input.matches("[\\-]?[0-9]+"))
            super.replaceSelection(input);
        else
            super.replaceSelection("0.0");
    }
}
