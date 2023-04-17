
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SchoolBell {

 JFrame frame;
 JPanel jp_label, jp_btn;
 JScrollPane scroll;
 JLabel label;
 JButton btn ;
 int count =1;
 GridBagLayout Gbag = new GridBagLayout();
 GridBagConstraints gbc1;
 
 SchoolBell(){

  frame = new JFrame();
  frame.setLayout(null);    // 레이아웃을 NULL로 설정한다
  frame.setSize(720,480);
  frame.setResizable(false);


 

  jp_label = new JPanel();

  // 라벨이 들어갈 panel 은 레이아웃을 GridBag을 사용한다.
  jp_label.setLayout(Gbag);  
  jp_label.setBackground(Color.lightGray); 

  scroll = new JScrollPane(jp_label);  // 스크롤패널을 선언
  scroll.setBounds(390,20,310,380);    // 프레임에 스크롤패널의 위치를 정한다

 

  jp_btn = new JPanel();   // 버튼 패널
  jp_btn.setBounds(540,405,20,50);
  btn = new JButton("+");  // 버튼 생성
  btn.addActionListener(new Add());
  jp_btn.add(btn);

 

  frame.add(scroll);   // 스크롤패널 추가
  frame.add(jp_btn);  // 버튼 패널 추가 
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setVisible(true);

}

// 버튼 리스너

class Add implements ActionListener{

public void actionPerformed(ActionEvent arg0) {
       JLabel label = new JLabel(count+"교시:"+"ㅁ:ㅁㅁ"+"~"+"ㅁ:ㅁㅁ");
       create_form(label,0,count++*30,30,10);

  }

}



// 라벨 추가 

public void create_form(Component cmpt, int x, int y, int w, int h){

  GridBagConstraints gbc = new GridBagConstraints();
  gbc.fill = GridBagConstraints.BOTH;
  gbc.gridx = x;
  gbc.gridy = y;
  gbc.gridwidth = w;
  gbc.gridheight = h;
  this.Gbag.setConstraints(cmpt, gbc);
  jp_label.add(cmpt);
  jp_label.updateUI();

}

public static class Hancom_002 extends JFrame implements Runnable {
 
  private GregorianCalendar time;

  // frame 크기
  private int width = 300;

  // time 계산을 위한 변수
  private int hour = 0;
  private int min = 0;
  private int sec = 0;

  private Graphics gp;

  // time이 변경될 때마다 Frame 내 이미지를 재 배치하기 위한 메서드
  public void paint(Graphics gp) {

      time = new GregorianCalendar();

      // 시간 정보를 가져온다.
      min = time.get(Calendar.MINUTE);
      hour = time.get(Calendar.HOUR);
      sec = time.get(Calendar.SECOND);

      if (sec == 60) {
          sec = 0;
          min++;
      }
      if (min == 60) {
          min = 0;
          hour++;
      }
      if (min == 60 && hour == 12) {
          hour = 0;
      }

      // Frame 내부에 그려진 모든 부분을 지운다.
      gp.clearRect(0, 0, width, width);
      
      // 시계 테두리를 표현하기 위해
      rec_draw();


      // 초 / 분 / 시 바늘을 그린다.
      draw(150, 150, 150, 50, 100, sec * 6);
      draw(150, 150, 150, 50, 70, min * 6);
      draw(150, 150, 150, 50, 50, hour * 30 + min / 2);
  }

  public void rec_draw() {
    int j = 1;
    for (int i = 1; i <= 60; i++) {
        // 5 분 단위로 시간을 표시하며, 점을 크게 표시하여 구분을 한다.
        if (i % 5 == 0) {
            draw_t(150, 50, i * 6, 3);
            draw_time(150, 50, i * 6, j + "");
            j++;
        } else {
            draw_t(150, 50, i * 6, 1);
        }
    }
  }

  public void draw_t(int x, int y, int angle, int width) {
    x = 150 + (int) (120 * Math.sin(angle * Math.PI / 180));
    y = 150 - (int) (120 * Math.cos(angle * Math.PI / 180));
    gp.fillRect(x, y, width, width);
}

// 시계 테두리 부분에 5분 단위로 시간을 표시하기 위한 메서드
public void draw_time(int x, int y, int angle, String i) {
    x = 147 + (int) (105 * Math.sin(angle * Math.PI / 180));
    y = 155 - (int) (100 * Math.cos(angle * Math.PI / 180));
    gp.drawString(i, x, y);
}

// 초 / 분 / 시 를 표시하기 위해 drawLine(선을 그리는 메서드) 사용
public void draw(int ox, int oy, int x, int y, int l, int angle) {
    x = ox + (int) (l * Math.sin(angle * Math.PI / 180));
    y = oy - (int) (l * Math.cos(angle * Math.PI / 180));
    gp.drawLine(ox, oy, x, y);
}
@Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}


 public static void main(String[] args) {
  SchoolBell bar = new SchoolBell();
  ClockStart();
 }

 public static void ClockStart() {
  Hancom_002 h2 = new Hancom_002();
  Thread th = new Thread(h2);
  th.start();
}

}