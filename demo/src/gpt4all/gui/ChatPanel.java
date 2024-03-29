package gpt4all.gui;

import gpt4all.ChatApplication;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;

public class ChatPanel {
    private static ChatApplication app;
    private static ChatPanel instance;
    private static JFrame frame;
    
    private static final Dimension FRAME_DIMENSION = new Dimension (796, 574);
    
    private JComboBox modelOptionsComboBox;
    private JLabel botAvatarJLabel;
    private static JProgressBar displayBotStatusJProgressBar;
    
    private JScrollPane scrollPaneChatTextArea;
    private static JTextArea chatTextArea;
    
    private JLabel userAvatarJLabel;
    private static JTextField promptInputJTextField;
    private static JButton sendPromptInputBtn;
    
    String[] modelOptionsComboBoxItems = {"ggml-gpt4all-j-v1.3-groovy.bin"};
    
    private void initComponents(Container appPane) {
        sendPromptInputBtn = new JButton ("Send >>");
        modelOptionsComboBox = new JComboBox (modelOptionsComboBoxItems);
        botAvatarJLabel = new JLabel ("");
        botAvatarJLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,25));
        botAvatarJLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        botAvatarJLabel.setVerticalTextPosition(SwingConstants.CENTER);
        displayBotStatusJProgressBar = new JProgressBar(); //  𝌀 
        displayBotStatusJProgressBar.setBorderPainted(true);
        displayBotStatusJProgressBar.setIndeterminate(false);
        
        chatTextArea = new JTextArea(5, 5);
        chatTextArea.setEditable(false);
        chatTextArea.setWrapStyleWord(true);
        chatTextArea.setLineWrap(true);
        ((DefaultCaret) chatTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPaneChatTextArea = new JScrollPane(chatTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneChatTextArea.setHorizontalScrollBar(null);
        
        userAvatarJLabel = new JLabel ("");
        userAvatarJLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,25));
        userAvatarJLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        userAvatarJLabel.setVerticalTextPosition(SwingConstants.CENTER);
        promptInputJTextField = new JTextField(5);
        promptInputJTextField.setText("What is the meaning of life?");
        
        appPane.setPreferredSize(FRAME_DIMENSION);
        appPane.setLayout(null);
        
        appPane.add(sendPromptInputBtn);
        appPane.add(modelOptionsComboBox);
        appPane.add(botAvatarJLabel);
        appPane.add(displayBotStatusJProgressBar);
        appPane.add(scrollPaneChatTextArea);
        appPane.add(userAvatarJLabel);
        appPane.add(promptInputJTextField);
        
        sendPromptInputBtn.setBounds (665, 480, 100, 35);
        modelOptionsComboBox.setBounds (95, 35, 320, 35);
        botAvatarJLabel.setBounds (25, 35, 65, 35);
        displayBotStatusJProgressBar.setBounds (435, 35, 330, 35);
        scrollPaneChatTextArea.setBounds (25, 115, 740, 310);
        userAvatarJLabel.setBounds (25, 480, 65, 35);
        promptInputJTextField.setBounds (95, 480, 560, 35);
    }

    
    public static void main (String[] args) {
        app = new ChatApplication();
        class Task extends SwingWorker<String, String> {
            String prompt;
            String response;
            Task(String prompt) {
                this.prompt = prompt;
            }
            
            @Override
            public String doInBackground() { // Main task. Executed in background thread.
                response = app.getResponse(prompt);
                return response;
            }
            @Override
            public void done() { // Can safely update the GUI from this method.
                 try {
                    displayBotStatusJProgressBar.setIndeterminate(false);
                    String modelResponse = get(); // Retrieve the return value of doInBackground.
                    chatTextArea.append(System.lineSeparator() + " " + modelResponse + System.lineSeparator());
                } catch (InterruptedException | ExecutionException ex) {
                    
                }
            }
        } 
        
        SwingUtilities.invokeLater(() -> {
            instance=new ChatPanel();
            frame = new JFrame ("[GPT4ALL] :: An offline GPT bot assistant");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            instance.initComponents(frame.getContentPane());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            /* TO DO LOGIC - When [Send >>] button is selected */
            sendPromptInputBtn.addActionListener((ActionEvent evt) -> {
                displayBotStatusJProgressBar.setIndeterminate(true);
                String promptInput=promptInputJTextField.getText();
                chatTextArea.append(System.lineSeparator() + " " + promptInput + System.lineSeparator());
                promptInputJTextField.setText("");
                new Task(promptInput).execute();
            });
        });
    }
}