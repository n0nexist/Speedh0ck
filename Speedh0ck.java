import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Speedh0ck {

    // Variabili globali per il colore di sfondo e di primo piano
    public static Color globalBackground = new Color(168, 173, 181); 
    public static Color globalForeground = new Color(15, 16, 18); 

    // Abbiamo già fatto partire un processo?
    public static boolean isRunning = false;

    // Dimensioni dei bottoni
    public static Dimension globalButtonDimension = new Dimension(60, 20);

    // controlla se la stringa passata è un numero
    public static boolean isNumber(String x){
        try{
            double temp = Double.parseDouble(x);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    // legge il file di log
    public static String readLogFile(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "[ERROR] "+e.getMessage();
        }
        return content;
    }

    // esegue un comando specificato in una determinata directory
    public static String execInDir(String cmd, String directory) {
        if (isRunning) return "";
        isRunning = true;
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", cmd);
            processBuilder.directory(new java.io.File(directory));
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Command execution failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isRunning = false;
        return output.toString();
    }

    // esegue il comando specificato
    public static String exec(String cmd) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", cmd);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Command execution failed with exit code: " + exitCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    // restituisce il timestamp
    public static String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return "["+dateFormat.format(date)+"]";
    }

    public static void main(String[] args){

        JFrame f = new JFrame("Speedh0ck v1.0");
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        // Label e textbox del comando
        JPanel commandPanel = new JPanel();
        JLabel commandLabel = new JLabel("Command:");
        JTextField commandTextbox = new JTextField(10);
        JButton buttonStart = new JButton("Start");
        buttonStart.setPreferredSize(new Dimension(70, 20));
        buttonStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(){
                    @Override
                    public void run(){
                        execInDir("./speedhack "+commandTextbox.getText(), "lib");
                    }
                }.start();
            }
        });

        commandPanel.setBackground(globalBackground);
        commandPanel.setForeground(globalForeground);
        commandLabel.setBackground(globalBackground);
        commandLabel.setForeground(globalForeground);
        commandTextbox.setBackground(globalBackground);
        commandTextbox.setForeground(globalForeground);
        buttonStart.setBackground(globalBackground);
        buttonStart.setForeground(globalForeground);
        commandPanel.add(commandLabel);
        commandPanel.add(commandTextbox);
        commandPanel.add(buttonStart);

        // Textarea dei log
        JTextArea logTextarea = new JTextArea(10, 20);
        logTextarea.setEditable(false);
        logTextarea.setBackground(globalBackground);
        logTextarea.setForeground(globalForeground);
        JScrollPane logScrollPane = new JScrollPane(logTextarea);

        // Area per regolare la velocità del comando
        JTextField speedTextbox = new JTextField(5);
        speedTextbox.setText("1");

        JPanel speedPanel = new JPanel();
        JLabel speedLabel = new JLabel("Speed:");
        JButton setSpeedButton = new JButton("Set");
        setSpeedButton.setPreferredSize(globalButtonDimension);
        setSpeedButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String x = speedTextbox.getText();
                if (isRunning && isNumber(x)){
                    exec("echo "+x+" > /tmp/speedhack_pipe");
                }else{
                    speedTextbox.setText("1");
                }
                logTextarea.append(getTimestamp()+" Speed set to "+x+" for '"+commandTextbox.getText()+"'\n");
            }
        });
        
        speedTextbox.setBackground(globalBackground);
        speedTextbox.setForeground(globalForeground);
        speedLabel.setBackground(globalBackground);
        speedLabel.setForeground(globalForeground);
        speedPanel.setBackground(globalBackground);
        speedPanel.setForeground(globalForeground);
        setSpeedButton.setBackground(globalBackground);
        setSpeedButton.setForeground(globalForeground);
        speedPanel.add(speedLabel);
        speedPanel.add(speedTextbox);
        speedPanel.add(setSpeedButton);

        // Aggiungere componenti al panel
        p.add(commandPanel, BorderLayout.NORTH);
        p.add(logScrollPane, BorderLayout.CENTER);
        p.add(speedPanel, BorderLayout.SOUTH);

        // Aggiungere panel al frame
        f.add(p);

        // Opzioni del frame
        f.setSize(400,200);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
