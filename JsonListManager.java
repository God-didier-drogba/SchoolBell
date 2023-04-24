import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonListManager extends JFrame {

    private static final String FILE_NAME = "data.json"; // Json 파일 이름
    private static final Gson GSON = new Gson();

    private JList<JsonObject> list;
    private DefaultListModel<JsonObject> listModel;

    public JsonListManager() {
        initializeUI();
        loadJsonFile();
    }

    private void initializeUI() {
        setSize(720, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton addButton = new JButton("추가");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addJsonItem();
            }
        });

        JButton editButton = new JButton("수정");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editJsonItem();
            }
        });

        JButton deleteButton = new JButton("삭제");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteJsonItem();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        getContentPane().add(listScrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadJsonFile() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            byte[] encoded = Files.readAllBytes(file.toPath());
            String jsonString = new String(encoded, StandardCharsets.UTF_8);

            JsonArray jsonArray = GSON.fromJson(jsonString, JsonArray.class);

            for (JsonElement element : jsonArray) {
                if (element.isJsonObject()) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    listModel.addElement(jsonObject);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveJsonFile() {
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME);
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < listModel.size(); i++) {
                JsonObject jsonObject = listModel.getElementAt(i);
                jsonArray.add(jsonObject);
            }
            String jsonString = GSON.toJson(jsonArray);
            fileWriter.write(jsonString);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void addJsonItem() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("이름", "1교시");
        jsonObject.addProperty("시작시간","0:00");
        // jsonObject.addProperty("종료시간","0:00");
        jsonObject.addProperty("종", "시간");
        listModel.addElement(jsonObject);
        saveJsonFile();
    }
    
    private void editJsonItem() {
        int index = list.getSelectedIndex();
        if (index == -1) {
            return;
        }
        JsonObject jsonObject = listModel.getElementAt(index);
        String name = jsonObject.get("이름").getAsString();
        
        String sttime = jsonObject.get("시작시간").getAsString();
        
        // String edtime = jsonObject.get("종료시간").getAsString();
        JRadioButton startRadioButton = new JRadioButton("시작할때");
        // JRadioButton endRadioButton = new JRadioButton("끝날때");
        // JRadioButton bothRadioButton = new JRadioButton("둘다");

        ButtonGroup  group = new ButtonGroup();

        group.add(startRadioButton);
        // group.add(endRadioButton);
        // group.add(bothRadioButton);
    
        String newName = JOptionPane.showInputDialog(this, "수업의 이름을 입력해주세요:", name);
        if (newName == null) {
            return;
        }
        String newStTime = JOptionPane.showInputDialog(this, "시작 시간을 입력해 주세요 (콜론으로 시와 분을 구분해 주세요):", sttime);
        if (newStTime == null) {
            return;
        }
        // String newEdTime = JOptionPane.showInputDialog(this, "종료 시간을 입력해 주세요 (스페이스바로 시와 분을 구분해 주세요):", edtime);
        // if (newEdTime == null) {
        //     return;
        // }

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.add(startRadioButton);
        // checkBoxPanel.add(endRadioButton);
        // checkBoxPanel.add(bothRadioButton);
    
        int choice = JOptionPane.showConfirmDialog(this, checkBoxPanel, "종이 울릴 시간을 골라주세요", JOptionPane.OK_CANCEL_OPTION);
        boolean startSelected = startRadioButton.isSelected();
        // boolean endSelected = endRadioButton.isSelected();
        // boolean bothSelected = bothRadioButton.isSelected();
        
        String whatSelectd = "";

        if (startSelected == true){
            whatSelectd = "시작할때";
            System.out.println(whatSelectd);
        }// }else if (endSelected == true){
        //     whatSelectd = "끝날때";
        //     System.out.println(whatSelectd);
        // }else if (bothSelected == true){
        //     whatSelectd = "둘다";
        //     System.out.println(whatSelectd);
        // }

        jsonObject.addProperty("이름", newName);
        jsonObject.addProperty("시작시간", newStTime);
        // jsonObject.addProperty("종료시간", newEdTime);
        jsonObject.addProperty("종", whatSelectd);
        listModel.setElementAt(jsonObject, index);

        String SHM[] = newStTime.split(":");
        // String EHM[] = newEdTime.split(":");
        
        if (whatSelectd == "시작할때"){
            startTimer(Integer.parseInt(SHM[0]), Integer.parseInt(SHM[1]));
        }// }else if (whatSelectd == "끝날때"){
        //     startTimer(Integer.parseInt(EHM[0]), Integer.parseInt(EHM[1]));
        // }else if (whatSelectd == "둘다"){
        //     startTimer(Integer.parseInt(SHM[0]), Integer.parseInt(SHM[1]));
        //     startTimer(Integer.parseInt(EHM[0]), Integer.parseInt(EHM[1]));
        // }

        saveJsonFile();
    }
    
    private void deleteJsonItem() {
        int index = list.getSelectedIndex();
        if (index == -1) {
            return;
        }
        listModel.removeElementAt(index);
        saveJsonFile();
    }

    private void playSound() {
        try {
            URL url = getClass().getClassLoader().getResource("./audio/sample1.wav");
            if (url == null) {
                throw new IOException("Cannot find sound file.");
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            System.out.println("Yeah");
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("danger");
        }
    }
    
    private void startTimer(int hour, int minute) {
        System.out.println(hour+":"+minute);
        Timer m = new Timer();
        TimerTask task = new TimerTask(){
            @Override 
            public void run(){
                Long H = getTimeHour();
                Long M = getTimeMin();
                if (H == hour && M == minute){
                    System.out.println("Buck Yeah");
                    playSound();
                }
            }
        };
        m.schedule(task, 1000);
    }

    
    private long getTimeHour() {
        long TimeHour = LocalDateTime.now().getHour();
        return (TimeHour);
    }

    private long getTimeMin() {
        long TimeMin = LocalDateTime.now().getMinute();
        return (TimeMin + 1);
    }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JsonListManager manager = new JsonListManager();
                manager.setVisible(true);
            }
        });
    }
}