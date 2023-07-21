package visualizer.ui;

import visualizer.ui.enums.Mode;

import javax.swing.*;
import java.awt.*;

import static visualizer.ApplicationRunner.FRAME_WIDTH;

public class ModeLabel extends JLabel {

    final static String PREFIX = "Current Mode -> ";

    String labelPrefix;

    public void updateLabelText(Mode mode) {
        setText(PREFIX + mode.getText());
        // System.out.println(this.getText() + " " + this.getText().length());
    }

    public ModeLabel(String name, String labelPrefix, Mode mode) {
        this.labelPrefix = labelPrefix;
        updateLabelText(mode);
        setName(name);
        setForeground(Color.WHITE);
        setBounds(FRAME_WIDTH - 100 - this.getPreferredSize().width, 0, this.getPreferredSize().width + 100, this.getPreferredSize().height);
    }


}
