package visualizer;

import visualizer.ui.GraphPanel;
import visualizer.ui.ModeLabel;
import visualizer.ui.enums.Algorithm;
import visualizer.ui.enums.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static visualizer.ApplicationRunner.*;

public class MainFrame extends JFrame {

    private GraphPanel graphPanel = new GraphPanel();

    private ModeLabel modeLabel;

    private JLabel algorithmLabel;
    private GraphPanel.GraphState graphState;

    public void updateAlgorithmLabelText(String text) {
        algorithmLabel.setText(text);
        repaint();
    }

    private JMenuBar addMenuBar(String name) {
        JMenuBar bar = new JMenuBar();
        bar.setName(name);
        return bar;
    }

    private void addModeMenu(JMenuBar bar) {
        JMenu menu = new JMenu("Mode");

        for (var mode: Mode.values()) {
            addMenuItem(menu, mode.getText(), x -> {
                graphPanel.setMode(mode);
                algorithmLabel.setVisible(false);
                modeLabel.updateLabelText(mode);
                repaint();
            });
        }

        bar.add(menu);
    }

    private void addFileMenu(JMenuBar bar) {
        JMenu menu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("New");
        newFile.setName("New");
        newFile.addActionListener(e -> {
            algorithmLabel.setVisible(false);
            graphState = null;
            graphPanel.removeAll();
            graphPanel.reset();
            repaint();
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.setName("Exit");
        exit.addActionListener(e -> {
            dispose();
        });
        menu.add(newFile);
        menu.add(exit);

        bar.add(menu);
    }

    private void addAlgorithmMenu(JMenuBar bar) {
        JMenu menu = new JMenu("Algorithms");

        for (var algorithm: Algorithm.values()) {
            addMenuItem(menu, algorithm.getText(), x -> {
                algorithmLabel.setText("Please choose a starting vertex");
                algorithmLabel.setVisible(true);

                graphPanel.setAlgorithm(algorithm);

                graphPanel.setMode(Mode.NONE);
                modeLabel.updateLabelText(graphPanel.getMode());

                if (graphState == null) {
                    graphState = graphPanel.getState();
                } else {
                    graphPanel.setState(graphState);
                }
                LOGGER.info(algorithm.getText());

                MainFrame.this.repaint();
            });
        }

        bar.add(menu);
    }

    private void addMenuItem(JMenu menu, String name, ActionListener actionListener) {
        JMenuItem item = new JMenuItem(name);
        item.setName(name);
        item.addActionListener(actionListener);
        menu.add(item);
    }

    public MainFrame() {
        super("Graph-Algorithms Visualizer");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setName("Graph-Algorithms Visualizer");

        // String logPath = "C:\\Users\\roman.galyaminskiy\\Projects\\Graph-Algorithms Visualizer\\Graph-Algorithms Visualizer\\task";
        // Handler fileHandler = null;
        // try {
        //     fileHandler = new FileHandler(Paths.get(logPath, "test_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".log").toString());
        //     fileHandler.setFormatter(new SimpleFormatter());
        // } catch (IOException e) {
        //     LOGGER.severe(e.getMessage());
        //     System.exit(0);
        // }
        // LOGGER.addHandler(fileHandler);

        modeLabel = new ModeLabel("Mode", "Current Mode -> ", graphPanel.getMode());

        JMenuBar bar = new JMenuBar();
        bar.setName("MenuBar");
        setJMenuBar(bar);

        addModeMenu(bar);
        addFileMenu(bar);
        addAlgorithmMenu(bar);

        algorithmLabel = new JLabel("Please choose a starting vertex;");
        algorithmLabel.setName("Display");
        algorithmLabel.setForeground(Color.RED);
        algorithmLabel.setBackground(Color.GREEN);

        algorithmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        algorithmLabel.setVerticalAlignment(SwingConstants.CENTER);
        algorithmLabel.setBounds(0, FRAME_HEIGHT - 50,
                FRAME_WIDTH, 50);
        algorithmLabel.setOpaque(true);

        add(modeLabel);
        add(algorithmLabel);
        algorithmLabel.setVisible(false);


        add(graphPanel);

        // pack();

        setVisible(true);
    }
}