
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.nio.file.Files.list;
import static java.rmi.Naming.list;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Collections.list;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author junyo
 */
public class MainFrame extends javax.swing.JFrame {

String strSQL=null;
DB_MAN DBM=new DB_MAN();
    public MainFrame() {
        initComponents();
        //출력 위치 지정
        this.setLocationRelativeTo(null);
        
        

        
        
        this.jDialog_jointeam.setLocationRelativeTo(this);
        this.jDialog_Viewteam.setLocationRelativeTo(this);
        //DB오픈해서 게임 명가져옴
        try{
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select * from GameOption";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
            do{
               lbl_GameName.setText(DBM.DB_rs.getString("Game_Name").trim());
            }
            while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("메인실행 오류 : "+e.getMessage());
        }
    }

public boolean isNullOrSpace(String str) {
    String str_replace=str.replace(" ", "");
    if(str.length()!=str_replace.length()){//공백을 제거 했을때와 아닐때 문자열길이가 달라지면 공백이있음
            return true;
        }
    else if(str.trim().equals(null)){//null값이면 정상데이터아님
            return true;
    }
    else if(str.trim().equals("")){//빈문자열
        return true;
    }
    return false;//정상값
}
// 한국어 검사기
public boolean user_Check(){
    if(lbl_Log_User_Id.getText().trim().equals("No User")){//아이디 라벨이 No User면 트루
        return true; //트루면 if문에서 피드백
    }
    return false;//정상
}
public boolean master_Check(){
    if(lbl_Log_User_Id.getText().trim().equals("admin")){//아이디 라벨이 No User면 트루
        return false; // if문에서 피드백
    }
    return true;//정상 운영자
}
public boolean isLength(String str,int a, int b) {
    if(str.length()<a || str.length()>b){//문자열길이비교
            return true;
        }
    return false;
}
public boolean isUpper(String str){
    for(int i=0;i<str.length();i++){//대문자비교
            char ch = str.charAt(i);//인덱스에 맞는 문자뽑아옴 0부터시작
		if(ch>='A' && ch<='Z'){//아스키코드 값으로 비교
                    return true; 
                }
        }
    return false;
}
public boolean isKorean(String str){
    if(str.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) { //https://ooz.co.kr/254 표현식 공부 참고함
            // 한글이 포함된 문자열
            return true;
        } 
    return false;
}
public boolean time_compare(String temp1,String temp2){
    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date date1=null;
    Date date2=null;
    try{
        date1=df.parse(temp1);
        date2=df.parse(temp2);
    }catch (ParseException ex) {
        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
    boolean after=date1.before(date2);//비교할시간.before(기준시간) date1이 date2전이냐?  
    return after;
}
public void stagelist_Initialize(JComboBox list){
    ArrayList<String> stageName = new ArrayList<String>();//스테이지 리스트
        stageName.clear();//리스트 초기화
        stageName.add("no choice");
        try{ //DB읽어서 리스트 가져옴
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select stage from STAGE_LIST";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
            do{
                stageName.add(DBM.DB_rs.getString("STAGE").trim());//리스트에 추가
            }
            while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("stage 처리 중 오류 : "+e.getMessage());
        }
        ComboBoxModel stageList_Model = new DefaultComboBoxModel(new Vector<String>(stageName)); //arraylist 캐스팅 오류로 Vector로 캐스팅 
        //https://www.tabnine.com/code/java/methods/javax.swing.JComboBox/setModel 참고함
        list.setModel(stageList_Model);
        
}

public void joblist_Initialize(JComboBox list){
    ArrayList<String> jobName = new ArrayList<String>();//직업 리스트
        jobName.clear();//리스트 초기화
        jobName.add("no choice");
        try{ //DB읽어서 리스트 가져옴
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select job_name from JOB_LIST";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
            do{
                jobName.add(DBM.DB_rs.getString("job_name").trim());//리스트에 추가
            }
            while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("직업 처리 중 오류 : "+e.getMessage());
        }
        ComboBoxModel jobList_Model = new DefaultComboBoxModel(new Vector<String>(jobName)); //arraylist 캐스팅 오류로 Vector로 캐스팅 
        //https://www.tabnine.com/code/java/methods/javax.swing.JComboBox/setModel 참고함
        list.setModel(jobList_Model);
}
public void team_search(){
    String team_title=null;
    String start_time=null;
    String stage=null;
    int max=0;
    int min=0;
    String min_max=null;
    String leader_id=null;
    
    
   int row=0;  
   int rowCount=jTable_Search.getRowCount(); //읽어오기전 초기화
   System.out.println(rowCount);
   for(int i=0; i<rowCount; i++){
       for(int j=0; j<5; j++){
            jTable_Search.setValueAt(null,i,j);
       }
       
   }
        try{ //DB읽어서 테이블에 추가
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select maxnumber from stage_list where stage='";
            strSQL+=jcb_Search_Stage.getSelectedItem().toString();
            strSQL+="'";
            System.out.println(strSQL);
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL); 
            DBM.DB_rs.next();
            max=Integer.parseInt(DBM.DB_rs.getString("maxnumber").trim());//최대 인원수 가져옴
            
            strSQL="select * from team_info join member_";
            strSQL+=jcb_Search_Stage.getSelectedItem().toString();
            strSQL+=" using(leader_id,start_time) order by start_time"; //팀테이플과 멤버테이플 조인으로 select
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
                do{
                    System.out.println(DBM.DB_rs.getString("START_TIME").trim());
                    if(!(time_compare(lbl_Search_Set_Date.getText().trim(),DBM.DB_rs.getString("START_TIME").trim()))){//과거 데이터 불러올필요x
                        continue;
                    }
                    team_title=DBM.DB_rs.getString("team_title").trim();
                    start_time=DBM.DB_rs.getString("start_time").trim();
                    stage=DBM.DB_rs.getString("stage").trim();
                    min=0;
                    for(int i=1; i<=max; i++){
                        String temp="MEMBER"+i+"_ID";
                        if(!(DBM.DB_rs.getString(temp).trim().equals("no"))){
                            min++;
                        }
                    }
                    min_max=String.valueOf(min); //String으로 변환
                    min_max+="/"+String.valueOf(max);
                    leader_id=DBM.DB_rs.getString("leader_id");
                    Object tbData[]={team_title,start_time,stage,min_max,leader_id};
                
                    if(rowCount<=row){
                        DefaultTableModel tbl=(DefaultTableModel)jTable_Search.getModel();
                        tbl.addRow(tbData);
                        jTable_Search.setModel(tbl);
                    }
                    else{
                        for(int i=0; i<5; i++){
                            jTable_Search.setValueAt(tbData[i], row, i);   
                        }
                        row++;
                    }
                
                }
                while(DBM.DB_rs.next());
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("테이블불러오는중오류 : "+e.getMessage());
        }
        if(start_time==null){//db작업완료했는데 가져온 값이 없으면 검색된 결과가 없음
            JOptionPane.showMessageDialog(null, "검색된 결과가 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog_gamename_set = new javax.swing.JDialog();
        lbl_Gns_Name = new javax.swing.JLabel();
        txt_Gns_Name = new javax.swing.JTextField();
        btn_Gns_Complete = new javax.swing.JButton();
        lbl_Gns_title = new javax.swing.JLabel();
        lbl_Gns_title2 = new javax.swing.JLabel();
        lbl_Gns_title3 = new javax.swing.JLabel();
        jDialog_job_set = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        lbl_Js_JobName = new javax.swing.JLabel();
        txt_Js_JobName = new javax.swing.JTextField();
        lbl_Js_JobNameInfo = new javax.swing.JLabel();
        lbl_Js_JobNameInfo2 = new javax.swing.JLabel();
        lbl_Js_Job_title = new javax.swing.JLabel();
        btn_Js_Create = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btn_Js_Delete = new javax.swing.JButton();
        jcb_Js_Joblist = new javax.swing.JComboBox<>();
        lbl_Js_StageDeleteInfo = new javax.swing.JLabel();
        jDialog_Viewteam = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Member = new javax.swing.JTable();
        lbl_Vt_Teamtitle = new javax.swing.JLabel();
        lbl_Vt_Stage = new javax.swing.JLabel();
        jDialog_stage_set = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        lbl_Ss_StageName = new javax.swing.JLabel();
        txt_Ss_StageName = new javax.swing.JTextField();
        lbl_Ss_StageNameInfo = new javax.swing.JLabel();
        lbl_Ss_StageNameInfo2 = new javax.swing.JLabel();
        lbl_Ss_Stage_title = new javax.swing.JLabel();
        btn_Ss_Create = new javax.swing.JButton();
        lbl_Ss_Stagemcount = new javax.swing.JLabel();
        jcb_Ss_Stagemcount = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        btn_Ss_Delete = new javax.swing.JButton();
        jcb_Ss_Stagelist = new javax.swing.JComboBox<>();
        lbl_Ss_StageDeleteInfo = new javax.swing.JLabel();
        jDialog_jointeam = new javax.swing.JDialog();
        lbl_Jt_job = new javax.swing.JLabel();
        jcb_Jt_Job = new javax.swing.JComboBox<>();
        btn_Jt_Join = new javax.swing.JButton();
        jDialog_create_date_set = new javax.swing.JDialog();
        jdc_Ct_Date_Set = new com.toedter.calendar.JDateChooser();
        jcb_Ct_Hour = new javax.swing.JComboBox<>();
        lbl_Ct_Date_Set_Hour = new javax.swing.JLabel();
        lbl_Ct_Date_Set_Minute = new javax.swing.JLabel();
        jcb_Ct_Minute = new javax.swing.JComboBox<>();
        lbl_Ct_Date_Set_Title = new javax.swing.JLabel();
        btn_Ct_Date_Set_Complete = new javax.swing.JButton();
        jDialog_create_team = new javax.swing.JDialog();
        lbl_Ct_Name = new javax.swing.JLabel();
        txt_Ct_Title = new javax.swing.JTextField();
        lbl_Ct_Name_Info = new javax.swing.JLabel();
        lbl_Ct_Set_Date = new javax.swing.JLabel();
        btn_Ct_Date = new javax.swing.JButton();
        lbl_Ct_Stage_Choice = new javax.swing.JLabel();
        jcb_Ct_Stage = new javax.swing.JComboBox<>();
        lbl_Ct_Leaderjob = new javax.swing.JLabel();
        jcb_Ct_Leaderjob = new javax.swing.JComboBox<>();
        btn_Ct_Complete = new javax.swing.JButton();
        jDialog_search_date_set = new javax.swing.JDialog();
        jdc_S_Date_Set = new com.toedter.calendar.JDateChooser();
        jcb_S_Hour1 = new javax.swing.JComboBox<>();
        lbl_S_Date_Set_Hour = new javax.swing.JLabel();
        lbl_S_Date_Set_Minute = new javax.swing.JLabel();
        jcb_S_Minute = new javax.swing.JComboBox<>();
        lbl_S_Date_Set_Title = new javax.swing.JLabel();
        btn_S_Date_Set_Complete = new javax.swing.JButton();
        jDialog_search = new javax.swing.JDialog();
        btn_Search_Date = new javax.swing.JButton();
        lbl_Search_Stage_Choice = new javax.swing.JLabel();
        jcb_Search_Stage = new javax.swing.JComboBox<>();
        lbl_Search_Set_Date = new javax.swing.JLabel();
        btn_Search_Complete = new javax.swing.JButton();
        jDialog_reg = new javax.swing.JDialog();
        lbl_Reg_Id = new javax.swing.JLabel();
        lbl_Reg_Id_Info = new javax.swing.JLabel();
        lbl_Reg_Password_Check = new javax.swing.JLabel();
        lbl_Reg_NickName = new javax.swing.JLabel();
        lbl_Reg_Password = new javax.swing.JLabel();
        lbl_Reg_Password_Info = new javax.swing.JLabel();
        lbl_Reg_NickName_Info = new javax.swing.JLabel();
        txt_Reg_Id = new javax.swing.JTextField();
        txt_Reg_NickName = new javax.swing.JTextField();
        txt_Reg_Password_Check = new javax.swing.JPasswordField();
        btn_Reg_Complete = new javax.swing.JButton();
        txt_Reg_Password = new javax.swing.JPasswordField();
        btn_Reg_Id_Check = new javax.swing.JButton();
        jcb_Reg_Idcheck = new javax.swing.JCheckBox();
        jDialog_login = new javax.swing.JDialog();
        btn_Log_Complete = new javax.swing.JButton();
        txt_Log_Password = new javax.swing.JPasswordField();
        txt_Log_Id = new javax.swing.JTextField();
        lbl_Log_Password = new javax.swing.JLabel();
        lbl_Log_Id = new javax.swing.JLabel();
        lbl_GameName = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable_Search = new javax.swing.JTable();
        btn_Search = new javax.swing.JButton();
        btn_Create = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_Reg = new javax.swing.JButton();
        btn_Login = new javax.swing.JButton();
        lbl_Log_User_Id = new javax.swing.JLabel();
        btn_Set_GameName = new javax.swing.JButton();
        btn_Set_Stage = new javax.swing.JButton();
        btn_Set_Job = new javax.swing.JButton();
        lbl_Log_User_NickName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btn_Join = new javax.swing.JButton();
        btn_Out = new javax.swing.JButton();
        btn_Memeber = new javax.swing.JButton();
        btn_Logout = new javax.swing.JButton();

        jDialog_gamename_set.setTitle("게임 명 설정");
        jDialog_gamename_set.setModal(true);
        jDialog_gamename_set.setResizable(false);

        lbl_Gns_Name.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Gns_Name.setText("게임 명 :");

        btn_Gns_Complete.setText(" 게임 명 변경");
        btn_Gns_Complete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Gns_CompleteActionPerformed(evt);
            }
        });

        lbl_Gns_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Gns_title.setText("한글 및 영소문자,숫자만 사용가능합니다.");

        lbl_Gns_title2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Gns_title2.setText("1~10자내로 입력해주세요. 공백만 사용 불가능");

        lbl_Gns_title3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Gns_title3.setText("반드시 서비스 중지를 한 상태에서 변경하세요");

        javax.swing.GroupLayout jDialog_gamename_setLayout = new javax.swing.GroupLayout(jDialog_gamename_set.getContentPane());
        jDialog_gamename_set.getContentPane().setLayout(jDialog_gamename_setLayout);
        jDialog_gamename_setLayout.setHorizontalGroup(
            jDialog_gamename_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_gamename_setLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jDialog_gamename_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_gamename_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btn_Gns_Complete, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl_Gns_title3, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog_gamename_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lbl_Gns_title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl_Gns_title2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jDialog_gamename_setLayout.createSequentialGroup()
                            .addComponent(lbl_Gns_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_Gns_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jDialog_gamename_setLayout.setVerticalGroup(
            jDialog_gamename_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_gamename_setLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbl_Gns_title3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_gamename_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Gns_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Gns_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Gns_title2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_Gns_title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Gns_Complete)
                .addGap(18, 18, 18))
        );

        jDialog_job_set.setTitle("직업 관리");
        jDialog_job_set.setModal(true);
        jDialog_job_set.setResizable(false);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbl_Js_JobName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Js_JobName.setText("직업 명:");

        lbl_Js_JobNameInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Js_JobNameInfo.setText("1~10자내로 입력해주세요. 공백만 사용 불가능");

        lbl_Js_JobNameInfo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Js_JobNameInfo2.setText("한글 및 영소문자,숫자만 사용가능합니다.");

        lbl_Js_Job_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Js_Job_title.setText("반드시 서비스 중지를 한 후 조작하세요");
        lbl_Js_Job_title.setToolTipText("");

        btn_Js_Create.setText("직업 생성");
        btn_Js_Create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Js_CreateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_Js_JobNameInfo2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Js_Create, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lbl_Js_JobName, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_Js_JobName, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbl_Js_Job_title, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
            .addComponent(lbl_Js_JobNameInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_Js_Job_title, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Js_JobName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Js_JobName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Js_JobNameInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Js_JobNameInfo2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Js_Create, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btn_Js_Delete.setText("직업 삭제");
        btn_Js_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Js_DeleteActionPerformed(evt);
            }
        });

        jcb_Js_Joblist.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice" }));

        lbl_Js_StageDeleteInfo.setText("삭제할 직업을 선택해주세요");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Js_StageDeleteInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcb_Js_Joblist, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Js_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lbl_Js_StageDeleteInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Js_Delete, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jcb_Js_Joblist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jDialog_job_setLayout = new javax.swing.GroupLayout(jDialog_job_set.getContentPane());
        jDialog_job_set.getContentPane().setLayout(jDialog_job_setLayout);
        jDialog_job_setLayout.setHorizontalGroup(
            jDialog_job_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog_job_setLayout.setVerticalGroup(
            jDialog_job_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_job_setLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog_Viewteam.setModal(true);
        jDialog_Viewteam.setResizable(false);

        jTable_Member.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "닉네임", "직업"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_Member);

        lbl_Vt_Teamtitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Vt_Teamtitle.setText("팀 제목");
        lbl_Vt_Teamtitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbl_Vt_Stage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Vt_Stage.setText("스테이지 이름");
        lbl_Vt_Stage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jDialog_ViewteamLayout = new javax.swing.GroupLayout(jDialog_Viewteam.getContentPane());
        jDialog_Viewteam.getContentPane().setLayout(jDialog_ViewteamLayout);
        jDialog_ViewteamLayout.setHorizontalGroup(
            jDialog_ViewteamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
            .addComponent(lbl_Vt_Stage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_Vt_Teamtitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog_ViewteamLayout.setVerticalGroup(
            jDialog_ViewteamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog_ViewteamLayout.createSequentialGroup()
                .addComponent(lbl_Vt_Teamtitle, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Vt_Stage, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jDialog_stage_set.setTitle("Stage 관리");
        jDialog_stage_set.setModal(true);
        jDialog_stage_set.setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbl_Ss_StageName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Ss_StageName.setText("Stage 명:");

        lbl_Ss_StageNameInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Ss_StageNameInfo.setText("1~10자내로 입력해주세요. 공백만 사용 불가능");

        lbl_Ss_StageNameInfo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Ss_StageNameInfo2.setText("한글 및 영소문자,숫자만 사용가능합니다.");
        lbl_Ss_StageNameInfo2.setToolTipText("");

        lbl_Ss_Stage_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Ss_Stage_title.setText("반드시 서비스 중지를 한 후 조작하세요");
        lbl_Ss_Stage_title.setToolTipText("");

        btn_Ss_Create.setText("Stage 생성");
        btn_Ss_Create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Ss_CreateActionPerformed(evt);
            }
        });

        lbl_Ss_Stagemcount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Ss_Stagemcount.setText("인원 수 :");

        jcb_Ss_Stagemcount.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_Ss_StageNameInfo2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lbl_Ss_StageNameInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_Ss_Stagemcount, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcb_Ss_Stagemcount, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Ss_Create, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbl_Ss_StageName, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_Ss_StageName, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbl_Ss_Stage_title, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(35, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_Ss_Stage_title, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Ss_StageName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Ss_StageName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Ss_StageNameInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Ss_StageNameInfo2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Ss_Create, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_Ss_Stagemcount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcb_Ss_Stagemcount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btn_Ss_Delete.setText("Stage 삭제");
        btn_Ss_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Ss_DeleteActionPerformed(evt);
            }
        });

        jcb_Ss_Stagelist.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice" }));

        lbl_Ss_StageDeleteInfo.setText("삭제할 Stage를 선택해주세요");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Ss_StageDeleteInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcb_Ss_Stagelist, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Ss_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(lbl_Ss_StageDeleteInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Ss_Delete, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jcb_Ss_Stagelist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jDialog_stage_setLayout = new javax.swing.GroupLayout(jDialog_stage_set.getContentPane());
        jDialog_stage_set.getContentPane().setLayout(jDialog_stage_setLayout);
        jDialog_stage_setLayout.setHorizontalGroup(
            jDialog_stage_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog_stage_setLayout.setVerticalGroup(
            jDialog_stage_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_stage_setLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog_jointeam.setTitle("팀 참가");
        jDialog_jointeam.setModal(true);
        jDialog_jointeam.setResizable(false);

        lbl_Jt_job.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Jt_job.setText("직업 선택 :");

        jcb_Jt_Job.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice" }));

        btn_Jt_Join.setText("팀 참가");
        btn_Jt_Join.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Jt_JoinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_jointeamLayout = new javax.swing.GroupLayout(jDialog_jointeam.getContentPane());
        jDialog_jointeam.getContentPane().setLayout(jDialog_jointeamLayout);
        jDialog_jointeamLayout.setHorizontalGroup(
            jDialog_jointeamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_jointeamLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jDialog_jointeamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_Jt_Join)
                    .addGroup(jDialog_jointeamLayout.createSequentialGroup()
                        .addComponent(lbl_Jt_job, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jcb_Jt_Job, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jDialog_jointeamLayout.setVerticalGroup(
            jDialog_jointeamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_jointeamLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jDialog_jointeamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Jt_job, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcb_Jt_Job, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_Jt_Join)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog_create_date_set.setTitle("그룹 생성 시간설정");
        jDialog_create_date_set.setModal(true);
        jDialog_create_date_set.setResizable(false);

        jcb_Ct_Hour.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice", "00", "01 ", "02 ", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        lbl_Ct_Date_Set_Hour.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Ct_Date_Set_Hour.setText("시간 :");

        lbl_Ct_Date_Set_Minute.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Ct_Date_Set_Minute.setText("분  :");

        jcb_Ct_Minute.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice", "00", "10", "20", "30", "40", "50" }));

        lbl_Ct_Date_Set_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Ct_Date_Set_Title.setText("1시간 이후만 설정가능합니다.");

        btn_Ct_Date_Set_Complete.setText("설정 완료");
        btn_Ct_Date_Set_Complete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Ct_Date_Set_CompleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_create_date_setLayout = new javax.swing.GroupLayout(jDialog_create_date_set.getContentPane());
        jDialog_create_date_set.getContentPane().setLayout(jDialog_create_date_setLayout);
        jDialog_create_date_setLayout.setHorizontalGroup(
            jDialog_create_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_create_date_setLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jDialog_create_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Ct_Date_Set_Title, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDialog_create_date_setLayout.createSequentialGroup()
                        .addComponent(lbl_Ct_Date_Set_Hour, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcb_Ct_Hour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(lbl_Ct_Date_Set_Minute, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog_create_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcb_Ct_Minute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Ct_Date_Set_Complete)))
                    .addComponent(jdc_Ct_Date_Set, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jDialog_create_date_setLayout.setVerticalGroup(
            jDialog_create_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_create_date_setLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lbl_Ct_Date_Set_Title, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jdc_Ct_Date_Set, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jDialog_create_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcb_Ct_Hour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Ct_Date_Set_Hour, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_Ct_Date_Set_Minute, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcb_Ct_Minute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(btn_Ct_Date_Set_Complete)
                .addGap(24, 24, 24))
        );

        jDialog_create_team.setTitle("그룹 생성");
        jDialog_create_team.setModal(true);
        jDialog_create_team.setResizable(false);

        lbl_Ct_Name.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Ct_Name.setText("그룹 제목 :");

        lbl_Ct_Name_Info.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Ct_Name_Info.setText("팀 제목은 2자 이상 20자 이내로 입력해주세요. 모든 문자 가능 ");

        lbl_Ct_Set_Date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Ct_Set_Date.setText("no set");
        lbl_Ct_Set_Date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btn_Ct_Date.setText("그룹 생성 시간 설정");
        btn_Ct_Date.setFocusable(false);
        btn_Ct_Date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Ct_DateActionPerformed(evt);
            }
        });

        lbl_Ct_Stage_Choice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Ct_Stage_Choice.setText("Stage를 선택해주세요");

        jcb_Ct_Stage.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice" }));

        lbl_Ct_Leaderjob.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Ct_Leaderjob.setText("생성자의 직업을 선택해주세요");

        jcb_Ct_Leaderjob.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice" }));

        btn_Ct_Complete.setText("그룹 생성");
        btn_Ct_Complete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Ct_CompleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_create_teamLayout = new javax.swing.GroupLayout(jDialog_create_team.getContentPane());
        jDialog_create_team.getContentPane().setLayout(jDialog_create_teamLayout);
        jDialog_create_teamLayout.setHorizontalGroup(
            jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_create_teamLayout.createSequentialGroup()
                .addGroup(jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog_create_teamLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialog_create_teamLayout.createSequentialGroup()
                                .addComponent(lbl_Ct_Leaderjob, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jcb_Ct_Leaderjob, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDialog_create_teamLayout.createSequentialGroup()
                                .addComponent(lbl_Ct_Stage_Choice, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jDialog_create_teamLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Ct_Complete, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog_create_teamLayout.createSequentialGroup()
                        .addGroup(jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog_create_teamLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jcb_Ct_Stage, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jDialog_create_teamLayout.createSequentialGroup()
                                        .addComponent(lbl_Ct_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbl_Ct_Name_Info, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txt_Ct_Title, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog_create_teamLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(lbl_Ct_Set_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_Ct_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(29, 29, 29))
        );
        jDialog_create_teamLayout.setVerticalGroup(
            jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_create_teamLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Ct_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Ct_Title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Ct_Name_Info)
                .addGap(18, 18, 18)
                .addGroup(jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Ct_Stage_Choice, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcb_Ct_Stage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Ct_Date)
                    .addComponent(lbl_Ct_Set_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialog_create_teamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Ct_Leaderjob, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcb_Ct_Leaderjob, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btn_Ct_Complete, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog_search_date_set.setTitle("그룹 검색 시간설정");
        jDialog_search_date_set.setModal(true);
        jDialog_search_date_set.setResizable(false);

        jcb_S_Hour1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice", "0", "1 ", "2 ", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        lbl_S_Date_Set_Hour.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_S_Date_Set_Hour.setText("시간 :");

        lbl_S_Date_Set_Minute.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_S_Date_Set_Minute.setText("분  :");

        jcb_S_Minute.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice", "0", "10", "20", "30", "40", "50" }));

        lbl_S_Date_Set_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_S_Date_Set_Title.setText("현재 이후만 설정가능합니다.");

        btn_S_Date_Set_Complete.setText("설정 완료");
        btn_S_Date_Set_Complete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_S_Date_Set_CompleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_search_date_setLayout = new javax.swing.GroupLayout(jDialog_search_date_set.getContentPane());
        jDialog_search_date_set.getContentPane().setLayout(jDialog_search_date_setLayout);
        jDialog_search_date_setLayout.setHorizontalGroup(
            jDialog_search_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_search_date_setLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jDialog_search_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_S_Date_Set_Title, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdc_S_Date_Set, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDialog_search_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btn_S_Date_Set_Complete)
                        .addGroup(jDialog_search_date_setLayout.createSequentialGroup()
                            .addComponent(lbl_S_Date_Set_Hour, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jcb_S_Hour1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30)
                            .addComponent(lbl_S_Date_Set_Minute, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jcb_S_Minute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jDialog_search_date_setLayout.setVerticalGroup(
            jDialog_search_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_search_date_setLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lbl_S_Date_Set_Title, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jdc_S_Date_Set, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jDialog_search_date_setLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcb_S_Hour1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_S_Date_Set_Hour, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_S_Date_Set_Minute, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcb_S_Minute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(btn_S_Date_Set_Complete)
                .addGap(24, 24, 24))
        );

        jDialog_search.setTitle("그룹 검색");
        jDialog_search.setModal(true);
        jDialog_search.setResizable(false);

        btn_Search_Date.setText("그룹 검색 시간 설정");
        btn_Search_Date.setFocusable(false);
        btn_Search_Date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Search_DateActionPerformed(evt);
            }
        });

        lbl_Search_Stage_Choice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Search_Stage_Choice.setText("Stage를 선택해주세요");

        jcb_Search_Stage.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no choice" }));

        lbl_Search_Set_Date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Search_Set_Date.setText("no set");
        lbl_Search_Set_Date.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btn_Search_Complete.setText("검색");
        btn_Search_Complete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Search_CompleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_searchLayout = new javax.swing.GroupLayout(jDialog_search.getContentPane());
        jDialog_search.getContentPane().setLayout(jDialog_searchLayout);
        jDialog_searchLayout.setHorizontalGroup(
            jDialog_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_searchLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jDialog_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog_searchLayout.createSequentialGroup()
                        .addComponent(lbl_Search_Stage_Choice, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jcb_Search_Stage, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog_searchLayout.createSequentialGroup()
                        .addComponent(lbl_Search_Set_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jDialog_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialog_searchLayout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(btn_Search_Complete, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDialog_searchLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btn_Search_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog_searchLayout.setVerticalGroup(
            jDialog_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_searchLayout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addGroup(jDialog_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Search_Stage_Choice, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcb_Search_Stage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialog_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Search_Set_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Search_Date))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Search_Complete)
                .addGap(24, 24, 24))
        );

        jDialog_reg.setTitle("회원가입");
        jDialog_reg.setModal(true);
        jDialog_reg.setResizable(false);

        lbl_Reg_Id.setBackground(new java.awt.Color(255, 255, 255));
        lbl_Reg_Id.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Reg_Id.setText("아이디 :");

        lbl_Reg_Id_Info.setText("아이디는 5~15자의 소문자와 숫자 특수기호는(_),(-)만 사용 가능합니다.");

        lbl_Reg_Password_Check.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Reg_Password_Check.setText("비밀번호 확인 :");

        lbl_Reg_NickName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Reg_NickName.setText("닉네임 :");

        lbl_Reg_Password.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Reg_Password.setText("비밀번호 :");

        lbl_Reg_Password_Info.setText("비밀번호는 5~15자 영문 대 소문자, 숫자, 특수문자를 사용 가능합니다");

        lbl_Reg_NickName_Info.setText("닉네임은 5~15자를 입력해주세요 띄어쓰기를 제외한 모든 문자 사용가능합니다");

        txt_Reg_Id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_Reg_IdKeyPressed(evt);
            }
        });

        btn_Reg_Complete.setText("희원가입");
        btn_Reg_Complete.setActionCommand("회원가입");
        btn_Reg_Complete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Reg_CompleteActionPerformed(evt);
            }
        });

        btn_Reg_Id_Check.setText("ID확인");
        btn_Reg_Id_Check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Reg_Id_CheckActionPerformed(evt);
            }
        });

        jcb_Reg_Idcheck.setText("ID확인여부");
        jcb_Reg_Idcheck.setEnabled(false);

        javax.swing.GroupLayout jDialog_regLayout = new javax.swing.GroupLayout(jDialog_reg.getContentPane());
        jDialog_reg.getContentPane().setLayout(jDialog_regLayout);
        jDialog_regLayout.setHorizontalGroup(
            jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_regLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog_regLayout.createSequentialGroup()
                        .addComponent(lbl_Reg_Password_Check, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_Reg_Password_Info, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Reg_Password_Check, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog_regLayout.createSequentialGroup()
                        .addComponent(lbl_Reg_Password, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_Reg_Password, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog_regLayout.createSequentialGroup()
                        .addComponent(lbl_Reg_NickName, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_Reg_NickName_Info, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Reg_NickName, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog_regLayout.createSequentialGroup()
                        .addComponent(lbl_Reg_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_Reg_Id_Info, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jDialog_regLayout.createSequentialGroup()
                                .addComponent(txt_Reg_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btn_Reg_Id_Check)
                                    .addComponent(jcb_Reg_Idcheck))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog_regLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Reg_Complete, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jDialog_regLayout.setVerticalGroup(
            jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_regLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jcb_Reg_Idcheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Reg_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Reg_Id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Reg_Id_Check))
                .addGap(2, 2, 2)
                .addComponent(lbl_Reg_Id_Info, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_Reg_Password, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Reg_Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Reg_Password_Check, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Reg_Password_Check, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Reg_Password_Info, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog_regLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Reg_NickName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Reg_NickName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Reg_NickName_Info, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Reg_Complete, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jDialog_login.setTitle("로그인");
        jDialog_login.setModal(true);
        jDialog_login.setResizable(false);

        btn_Log_Complete.setText("로그인");
        btn_Log_Complete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Log_CompleteActionPerformed(evt);
            }
        });

        lbl_Log_Password.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Log_Password.setText("비밀번호 :");

        lbl_Log_Id.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_Log_Id.setText("아이디 :");

        javax.swing.GroupLayout jDialog_loginLayout = new javax.swing.GroupLayout(jDialog_login.getContentPane());
        jDialog_login.getContentPane().setLayout(jDialog_loginLayout);
        jDialog_loginLayout.setHorizontalGroup(
            jDialog_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_loginLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jDialog_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbl_Log_Password, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Log_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_Log_Id)
                    .addComponent(txt_Log_Password, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                .addContainerGap(41, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog_loginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Log_Complete, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jDialog_loginLayout.setVerticalGroup(
            jDialog_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_loginLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jDialog_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Log_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Log_Id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_Log_Password, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Log_Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Log_Complete)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("실시간 그룹 구성 시스템");
        setResizable(false);

        lbl_GameName.setFont(new java.awt.Font("굴림", 0, 36)); // NOI18N
        lbl_GameName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_GameName.setText("Game Name");
        lbl_GameName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jTable_Search.setAutoCreateRowSorter(true);
        jTable_Search.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable_Search.setFont(new java.awt.Font("굴림", 0, 14)); // NOI18N
        jTable_Search.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "그룹 제목", "출발 시간", "Stage", "현재/최대 인원", "생성자"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_Search.setToolTipText("");
        jTable_Search.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable_Search.setSelectionForeground(new java.awt.Color(255, 51, 51));
        jTable_Search.setShowVerticalLines(false);
        jScrollPane3.setViewportView(jTable_Search);
        jTable_Search.getAccessibleContext().setAccessibleName("");

        btn_Search.setText("그룹 검색");
        btn_Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SearchActionPerformed(evt);
            }
        });

        btn_Create.setText("그룹 생성");
        btn_Create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CreateActionPerformed(evt);
            }
        });

        btn_delete.setText("그룹 삭제");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        btn_Reg.setText("회원가입");
        btn_Reg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RegActionPerformed(evt);
            }
        });

        btn_Login.setText("로그인");
        btn_Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LoginActionPerformed(evt);
            }
        });

        lbl_Log_User_Id.setText("No User");

        btn_Set_GameName.setText("게임 명 설정");
        btn_Set_GameName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Set_GameNameActionPerformed(evt);
            }
        });

        btn_Set_Stage.setText("Stage 관리");
        btn_Set_Stage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Set_StageActionPerformed(evt);
            }
        });

        btn_Set_Job.setText("직업 관리");
        btn_Set_Job.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Set_JobActionPerformed(evt);
            }
        });

        lbl_Log_User_NickName.setText("No User");

        jLabel1.setText("아이디 :");

        jLabel2.setText("닉네임 :");

        btn_Join.setText("그룹 참가");
        btn_Join.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_JoinActionPerformed(evt);
            }
        });

        btn_Out.setText("그룹 탈퇴");
        btn_Out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_OutActionPerformed(evt);
            }
        });

        btn_Memeber.setText("구성원 보기");
        btn_Memeber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_MemeberActionPerformed(evt);
            }
        });

        btn_Logout.setText("로그아웃");
        btn_Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_LogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 882, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_Join, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btn_Out, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btn_Memeber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbl_GameName, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_Set_GameName, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_Set_Stage, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_Set_Job, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(122, 122, 122)
                                .addComponent(btn_Create, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_Search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(lbl_Log_User_NickName, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(44, 44, 44)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_Login, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbl_Log_User_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btn_Logout, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn_Reg, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_Log_User_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_Log_User_NickName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Reg)
                            .addComponent(btn_Login)
                            .addComponent(btn_Logout)))
                    .addComponent(lbl_GameName, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Create)
                            .addComponent(btn_delete)
                            .addComponent(btn_Search)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Set_GameName)
                            .addComponent(btn_Set_Stage)
                            .addComponent(btn_Set_Job))))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_Out, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_Join, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addComponent(btn_Memeber, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_Set_GameNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Set_GameNameActionPerformed
        if(user_Check()){
            JOptionPane.showMessageDialog(null, "로그인 해주세요.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(master_Check()){
            JOptionPane.showMessageDialog(null, "운영자 권한입니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txt_Gns_Name.setText("");
        jDialog_gamename_set.setSize(400, 190);//Frame 크기 지정
        this.jDialog_gamename_set.setLocationRelativeTo(this);
        jDialog_gamename_set.setVisible(true);
    }//GEN-LAST:event_btn_Set_GameNameActionPerformed

    private void btn_Set_StageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Set_StageActionPerformed
        if(user_Check()){
            JOptionPane.showMessageDialog(null, "로그인 해주세요.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(master_Check()){
            JOptionPane.showMessageDialog(null, "운영자 권한입니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txt_Ss_StageName.setText(null); 
        ArrayList<String> stage_Max_User = new ArrayList<String>();//인원수 리스트
        stage_Max_User.clear();//리스트 초기화
        stage_Max_User.add("no choice");
        for(int i=1; i<=20; i++){
            stage_Max_User.add(String.valueOf(i));
        }
        ComboBoxModel stage_Max_User_Model = new DefaultComboBoxModel(new Vector<String>(stage_Max_User));
        jcb_Ss_Stagemcount.setModel(stage_Max_User_Model);
        stagelist_Initialize(jcb_Ss_Stagelist);
        
        
        jDialog_stage_set.setSize(420, 288);//Frame 크기 지정
        this.jDialog_stage_set.setLocationRelativeTo(this);
        jDialog_stage_set.setVisible(true);
        
    }//GEN-LAST:event_btn_Set_StageActionPerformed

    private void btn_Set_JobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Set_JobActionPerformed
        if(user_Check()){
            JOptionPane.showMessageDialog(null, "로그인 해주세요.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(master_Check()){
            JOptionPane.showMessageDialog(null, "운영자 권한입니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txt_Js_JobName.setText(null); //초기화
        joblist_Initialize(jcb_Js_Joblist);//초기화
        
        jDialog_job_set.setSize(420, 288);//Frame 크기 지정
        this.jDialog_job_set.setLocationRelativeTo(this);
        jDialog_job_set.setVisible(true);
        
    }//GEN-LAST:event_btn_Set_JobActionPerformed

    private void btn_CreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CreateActionPerformed
        if(user_Check()){
            JOptionPane.showMessageDialog(null, "로그인 해주세요.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        txt_Ct_Title.setText(null);//입력창 초기화
        lbl_Ct_Set_Date.setText("no set"); //시간설정 초기화
        stagelist_Initialize(jcb_Ct_Stage); //팀생성에 선택할 stage 초기화
        joblist_Initialize(jcb_Ct_Leaderjob); //생성자의 직업 초기화
        
        
        
        jDialog_create_team.setSize(524, 298);//Frame 크기 지정
        this.jDialog_create_team.setLocationRelativeTo(this);
        jDialog_create_team.setVisible(true);
        
    }//GEN-LAST:event_btn_CreateActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
    if(user_Check()){
            JOptionPane.showMessageDialog(null, "로그인 해주세요.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    String start_time=null;
    String stage=null;
    String leader_id=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 4);
    try{
    if(jTable_Search.getSelectedRow() != -1){//선택된 열이 있어야만 실행 없으면 -1반환임
        if(!(lbl_Log_User_Id.getText().trim().equals(leader_id.trim()))){ //생성자가 아니면 삭제 불가능
            JOptionPane.showMessageDialog(null, "팀 리더가 아닙니다. 팀을 해산할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        start_time=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 1);
        stage=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 2);
        try{ //DB열어서 DELETE
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="DELETE FROM team_info WHERE LEADER_ID='"+lbl_Log_User_Id.getText().trim();
            strSQL+="' and start_TIME='"+start_time.trim()+"'";
            System.out.println(strSQL);
            DBM.DB_stmt.executeUpdate(strSQL);
            strSQL="DELETE FROM MEMBER_"+stage.trim()+" WHERE LEADER_ID='"+lbl_Log_User_Id.getText().trim();
            strSQL+="' and start_TIME='"+start_time.trim()+"'";
            System.out.println(strSQL);
            DBM.DB_stmt.executeUpdate(strSQL);
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("팀삭제중 오류 : "+e.getMessage());
            return;
        }
    }
    else if(jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 0).equals(null)){
            JOptionPane.showMessageDialog(null, "삭제할 팀을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }catch(NullPointerException e){
        JOptionPane.showMessageDialog(null, "삭제할 팀을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    team_search();
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SearchActionPerformed
        if(user_Check()){
            JOptionPane.showMessageDialog(null, "로그인 해주세요.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        stagelist_Initialize(jcb_Search_Stage); //팀검색에 선택할 stage 초기화
        lbl_Search_Set_Date.setText("no set");
        
        jDialog_search.setSize(475, 223);//Frame 크기 지정
        this.jDialog_search.setLocationRelativeTo(this);
        jDialog_search.setVisible(true);
        
        
    }//GEN-LAST:event_btn_SearchActionPerformed

    private void btn_LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LoginActionPerformed
        //초기화
        txt_Log_Id.setText(null);
        txt_Log_Password.setText(null);
        
        jDialog_login.setSize(407, 177);//Frame 크기 지정
        this.jDialog_login.setLocationRelativeTo(this);
        jDialog_login.setVisible(true);
        
        
    }//GEN-LAST:event_btn_LoginActionPerformed

    private void btn_RegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RegActionPerformed
        //초기화
        txt_Reg_Id.setText(null);
        txt_Reg_Password.setText(null);
        txt_Reg_Password_Check.setText(null);
        txt_Reg_NickName.setText(null);
        
        jDialog_reg.setSize(566, 393);//Frame 크기 지정
        this.jDialog_reg.setLocationRelativeTo(this);
        jDialog_reg.setVisible(true);
        
        
    }//GEN-LAST:event_btn_RegActionPerformed

    private void btn_JoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_JoinActionPerformed
    try{
        if(user_Check()){ //로그인 후
            JOptionPane.showMessageDialog(null, "로그인 해주세요.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        else if(jTable_Search.getSelectedRow() == -1){//선택된 열이 있어야만 실행 없으면 -1반환임
            JOptionPane.showMessageDialog(null, "참가할 그룹을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else if(jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 0).equals(null)){
            JOptionPane.showMessageDialog(null, "참가할 그룹을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        joblist_Initialize(jcb_Jt_Job);
    
        jDialog_jointeam.setSize(260, 135);//Frame 크기 지정
        this.jDialog_jointeam.setLocationRelativeTo(this);
        jDialog_jointeam.setVisible(true);
        
    }catch(NullPointerException e){
        JOptionPane.showMessageDialog(null, "참가할 그룹을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    }//GEN-LAST:event_btn_JoinActionPerformed

    private void btn_OutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_OutActionPerformed
    try{   
        if(user_Check()){ //아이디있어야 기능활용가능
                JOptionPane.showMessageDialog(null, "로그인 해주세요.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

        if(jTable_Search.getSelectedRow() == -1){//선택된 열이 있어야만 실행 없으면 -1반환임
            JOptionPane.showMessageDialog(null, "탈퇴할 그룹을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else if(jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 0).equals(null)){
            JOptionPane.showMessageDialog(null, "탈퇴할 그룹을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //db읽어서 같은 id있으면 탈퇴 but leader_id와 같으면 불가능 생성자이기때문에 단독 탈퇴 불가능
        String start_time=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 1);
        String stage=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 2);
        String leader_id=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 4);
        int max=0;
        int mynumber=0;
        try{ //DB열어서 빈자리 탐색 후 탈퇴
                DBM.dbOpen();
                strSQL="select maxnumber from stage_list where stage='";
                strSQL+=stage.trim();
                strSQL+="'";
                System.out.println(strSQL);
                DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
                DBM.DB_rs.next();
                max=Integer.parseInt(DBM.DB_rs.getString("maxnumber").trim());//최대 인원수 가져옴

                strSQL="select * From member_"+stage.trim()+" where leader_id='"+leader_id+"'";
                strSQL+=" and start_TIME='"+start_time.trim()+"'"; //멤버테이블  기본키로 검색 튜플하나만나옴
                System.out.println(strSQL);
                DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
                if(DBM.DB_rs.next()){
                do{
                    if(DBM.DB_rs.getString("leader_id").trim().equals(lbl_Log_User_Id.getText().trim())){//생성자는 탈퇴 불가능
                        JOptionPane.showMessageDialog(null, "생성자는 단독으로 탈퇴할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    for(int i=2; i<=max; i++){
                        String temp="MEMBER"+i+"_ID";
                        if((DBM.DB_rs.getString(temp).trim().equals(lbl_Log_User_Id.getText().trim()))){ //자신의 인덱스 탐색
                            mynumber=i;
                            break;
                        }
                    }
                }
                while(DBM.DB_rs.next());
                }
                else{
                    System.out.println("읽은 데이터가 없습니다.");
                }
                if(mynumber==0){
                    JOptionPane.showMessageDialog(null, "참가한 그룹이 아닙니다.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //탈퇴로 업데이트
                strSQL="update member_"+stage.trim()+" set ";
                strSQL+="member"+mynumber+"_id='"+"no"+"',";
                strSQL+="member"+mynumber+"_nickname='"+"no"+"',";
                strSQL+="member"+mynumber+"_job='"+"no"+"'";
                strSQL+=" where leader_id='"+leader_id+"'";
                strSQL+=" and start_TIME='"+start_time.trim()+"'";
                System.out.println(strSQL);
                DBM.DB_stmt.executeUpdate(strSQL);
                DBM.dbClose();
            }catch(Exception e){
                System.out.println("그룹 탈퇴 중 오류 : "+e.getMessage());
            }
        team_search();
        JOptionPane.showMessageDialog(null, "그룹 탈퇴가 완료되었습니다.", "확인", JOptionPane.WARNING_MESSAGE);
    }catch(NullPointerException e){
        JOptionPane.showMessageDialog(null, "탈퇴할 그룹을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    }//GEN-LAST:event_btn_OutActionPerformed

    private void btn_MemeberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_MemeberActionPerformed
    try{
        if(user_Check()){ //아이디있어야 기능활용가능
                JOptionPane.showMessageDialog(null, "로그인 해주세요.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

        else if(jTable_Search.getSelectedRow() == -1){//선택된 열이 있어야만 실행 없으면 -1반환임
            JOptionPane.showMessageDialog(null, "구성원을 볼 그룹을 선택해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else if(jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 0).equals(null)){
            JOptionPane.showMessageDialog(null, "구성원을 볼 그룹을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }


       lbl_Vt_Teamtitle.setText(null);
       lbl_Vt_Stage.setText(null);
       int max;
       int row=0;  
       int rowCount=jTable_Member.getRowCount(); //읽어오기전 초기화
       for(int i=0; i<rowCount; i++){
           for(int j=0; j<2; j++){
                jTable_Member.setValueAt(null,i,j);
           }  
       }

       String team_title=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 0);
       String start_time=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 1);
       String stage=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 2);
       String leader_id=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 4);
       String nick_name;
       String job;
       try{ //DB열어서 구성원 보기
                DBM.dbOpen();
                strSQL="select maxnumber from stage_list where stage='";
                strSQL+=stage.trim();
                strSQL+="'";
                System.out.println(strSQL);
                DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
                DBM.DB_rs.next();
                max=Integer.parseInt(DBM.DB_rs.getString("maxnumber").trim());//최대 인원수 가져옴
                System.out.println(max);
                strSQL="select * From member_"+stage.trim()+" where leader_id='"+leader_id+"'";
                strSQL+=" and start_TIME='"+start_time.trim()+"'"; //멤버테이블  기본키로 검색 튜플하나만나옴
                System.out.println(strSQL);
                DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
                if(DBM.DB_rs.next()){
                do{
                    for(int i=1; i<=max; i++){
                        String temp="MEMBER"+i+"_ID";
                        String temp_nickname="MEMBER"+i+"_nickname";
                        String temp_job="MEMBER"+i+"_job";
                        if((DBM.DB_rs.getString(temp).trim().equals("no"))){ //아이디로 비교해서 없으면 continue
                            continue;
                        }
                        //있는 경우 리스트에 추가
                        nick_name=DBM.DB_rs.getString(temp_nickname).trim();
                        job=DBM.DB_rs.getString(temp_job);
                        Object tbData[]={nick_name,job};

                        if(rowCount<=row){
                            DefaultTableModel tbl=(DefaultTableModel)jTable_Search.getModel();
                            tbl.addRow(tbData);
                            jTable_Member.setModel(tbl);
                        }
                        else{
                            for(int k=0; k<2; k++){
                                jTable_Member.setValueAt(tbData[k], row, k);   
                            }
                            row++;
                       }

                    }
                }
                while(DBM.DB_rs.next());
                }
                else{
                    System.out.println("읽은 데이터가 없습니다.");
                }
                DBM.dbClose();
            }catch(Exception e){
                System.out.println("구성원 보기 중 오류 : "+e.getMessage());
            }
        lbl_Vt_Teamtitle.setText(team_title);
        lbl_Vt_Stage.setText(stage);
        jDialog_Viewteam.setSize(380, 265);//Frame 크기 지정
        this.jDialog_Viewteam.setLocationRelativeTo(this);//출력 위치 지정
        jDialog_Viewteam.setVisible(true);
    }catch(NullPointerException e){
        JOptionPane.showMessageDialog(null, "구성원을 볼 그룹을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    }//GEN-LAST:event_btn_MemeberActionPerformed

    private void btn_Log_CompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Log_CompleteActionPerformed
    ///txt_Log_Id  txt_Log_Password
        //lbl_Log_User
        String user_id=txt_Log_Id.getText().trim();
        char[] char_pw=txt_Log_Password.getPassword();
        String user_pw="";
        for(char a : char_pw){//문자배열에서 하나씩 문자를 꺼내옴
            Character.toString(a);//char=> string으로 변환
            user_pw+= a; //
        }

        if(isNullOrSpace(user_id)){//(공백 및 빈문자열)x 처리
            JOptionPane.showMessageDialog(null, "아이디에 띄어쓰기 및 공백은 사용 할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        else if(isLength(user_id,5,15)){ //자릿 수 처리 (문자열, 최소,최대)
            JOptionPane.showMessageDialog(null, "아이디는 5자이상 15자이하로 입력해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else if(isNullOrSpace(user_pw)){//(공백 및 빈문자열)x 처리
            JOptionPane.showMessageDialog(null, "비밀번호에 띄어쓰기 및 공백은 사용 할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{ //DB읽어서 로그인
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select * from USER_INFO";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
                do{
                    if(user_id.equals(DBM.DB_rs.getString("USER_ID").trim())){//아이디 탐색
                        if(user_pw.equals(DBM.DB_rs.getString("USER_PW"))){
                            JOptionPane.showMessageDialog(null, DBM.DB_rs.getString("User_NICKNAME")+"님이 로그인에 성공하셨습니다.", "확인", JOptionPane.WARNING_MESSAGE);
                            lbl_Log_User_Id.setText(DBM.DB_rs.getString("User_ID"));
                            lbl_Log_User_NickName.setText(DBM.DB_rs.getString("User_NICKNAME"));
                            jDialog_login.dispose();
                            return;
                        }
                        else{//아이디는 맞으나 비밀번호가 틀린경우
                            JOptionPane.showMessageDialog(null,"비밀번호가 틀립니다." , "Error", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                    }
                }
                while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("로그인 처리 중 오류 : "+e.getMessage());
        }
        JOptionPane.showMessageDialog(null,"존재하지 않는 회원입니다. ", "Error", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_btn_Log_CompleteActionPerformed

    private void txt_Reg_IdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Reg_IdKeyPressed
       jcb_Reg_Idcheck.setSelected(false); //아이디에 키입력되면 확인체크 해제
    }//GEN-LAST:event_txt_Reg_IdKeyPressed

    private void btn_Reg_CompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Reg_CompleteActionPerformed
        char[] char_pw=txt_Reg_Password.getPassword();
        char[] char_pw_c=txt_Reg_Password_Check.getPassword();
        String temp="";
        String temp2="";
        for(char a : char_pw){//문자배열에서 하나씩 문자를 꺼내옴
            Character.toString(a);//char=> string으로 변환
            temp += a; //
        }
        for(char b : char_pw_c){//문자배열에서 하나씩 문자를 꺼내옴
            Character.toString(b);//char=> string으로 변환
            temp2 += b; //
        }
        System.out.println(temp); //비밀번호 값 추출완료
        System.out.println(temp2); //비밀번호 확인 값 추출완료

        if(!(jcb_Reg_Idcheck.isSelected())){//ID확인 체크여부
            JOptionPane.showMessageDialog(null, "ID확인 버튼을 눌러서 확인해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            System.out.println("아이디 체크 통화못함");
            return;
        }
        else if(isNullOrSpace(temp)){//비밀번호 공백 및 띄어쓰기 x
            JOptionPane.showMessageDialog(null, "비밀번호에 띄어쓰기 및 공백은 사용할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        else if(!(temp.equals(temp2))){//비밀==비밀번호확인
            JOptionPane.showMessageDialog(null, "비밀번호와 비밀번호 확인이 다릅니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        else if(isLength(temp,5,15)){//비밀번호는 5~15자여야함
            JOptionPane.showMessageDialog(null, "비밀번호는 5자이상 15자이하로 입력해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        }
        else if(isNullOrSpace(txt_Reg_NickName.getText())){ //닉네임은 공백 및 띄어쓰기 사용불가
            JOptionPane.showMessageDialog(null, "닉네임에 띄어쓰기 및 공백은 사용할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        else if(isLength(txt_Reg_NickName.getText(),5,15)){//닉네임은 5~15자여야함
            JOptionPane.showMessageDialog(null, "닉네임은 5자이상 15자이하로 입력해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try{ //DB열어서 insert
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="INSERT INTO USER_INFO VALUES (";
            strSQL+="'"+txt_Reg_Id.getText()+"',";
            strSQL+="'"+temp+"',";
            strSQL+="'"+txt_Reg_NickName.getText()+"')";
            System.out.println(strSQL);
            DBM.DB_stmt.executeUpdate(strSQL);
            DBM.dbClose();
            JOptionPane.showMessageDialog(null, "회원가입에 성공하셨습니다.", "확인", JOptionPane.WARNING_MESSAGE);
            jDialog_reg.dispose();        
        }catch(Exception e){
            System.out.println("회원가입 데이터넘기는 중 오류 : "+e.getMessage());
        }
    }//GEN-LAST:event_btn_Reg_CompleteActionPerformed

    private void btn_Reg_Id_CheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Reg_Id_CheckActionPerformed
        //(공백 및 빈문자열)x 처리

        String temp=txt_Reg_Id.getText();

        if(isNullOrSpace(temp)){//(공백 및 빈문자열)x 처리
            JOptionPane.showMessageDialog(null, "띄어쓰기 및 공백은 사용 할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(isLength(temp,5,15)){ //자릿 수 처리 (문자열, 최소,최대)
            JOptionPane.showMessageDialog(null, "아이디는 5자이상 15자이하로 입력해주세요", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(isUpper(temp)){//대문자X처리
            JOptionPane.showMessageDialog(null, "아이디에 대문자는 사용할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(isKorean(temp)){
            JOptionPane.showMessageDialog(null, "아이디에 한글은 사용할 수 없습니다", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ///-_특수문자만 가능하게함
        String temp_replace=temp.replace("-", "").replace("_", "").trim();//- _는 검증때 없다고 가정
        System.out.println(temp_replace);
        System.out.println(temp_replace.matches("^[a-z0-9]*$"));
        //나머지 경우는 특수문자 _ - 제외 문제 입력일 때//
        if(!(temp_replace.matches("^[a-z0-9]*$"))) {
            JOptionPane.showMessageDialog(null, "특수문자는 - _ 만 입력가능합니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{ //DB읽어서 중복 처리
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select user_id from USER_INFO";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
                do{
                    System.out.println(DBM.DB_rs.getString("USER_ID").trim());
                    if(temp.equals(DBM.DB_rs.getString("USER_ID").trim())){
                        JOptionPane.showMessageDialog(null, "중복된 아이디 입니다.", "Error", JOptionPane.WARNING_MESSAGE);
                        DBM.DB_rs.close();
                        DBM.dbClose();
                        return;
                    }
                }
                while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("중복 처리 중 오류 : "+e.getMessage());
        }
        jcb_Reg_Idcheck.setSelected(true);//다 통과하고나서 아이디 확인 체크
        JOptionPane.showMessageDialog(null, "사용 가능한 아이디 입니다.", "확인", JOptionPane.WARNING_MESSAGE);

    }//GEN-LAST:event_btn_Reg_Id_CheckActionPerformed

    private void btn_Search_DateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Search_DateActionPerformed
        //오늘부터 7일까지 입력가능
        Date today=new Date();
        Date today_seven;
        Calendar cal=Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 7);
        today_seven=new Date(cal.getTimeInMillis());
        jdc_S_Date_Set.setMaxSelectableDate(today_seven);
        jdc_S_Date_Set.setMinSelectableDate(today);
        
        
        
        jDialog_search_date_set.setSize(380, 265);//Frame 크기 지정
        this.jDialog_search_date_set.setLocationRelativeTo(this);
        jDialog_search_date_set.setVisible(true);
        
    }//GEN-LAST:event_btn_Search_DateActionPerformed

    private void btn_Search_CompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Search_CompleteActionPerformed
    team_search();
    jDialog_search.dispose();
    }//GEN-LAST:event_btn_Search_CompleteActionPerformed

    private void btn_S_Date_Set_CompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_S_Date_Set_CompleteActionPerformed
    String hour=jcb_S_Hour1.getSelectedItem().toString().trim();
    String minute=jcb_S_Minute.getSelectedItem().toString().trim();
    if(jdc_S_Date_Set.getDate()==null){
        JOptionPane.showMessageDialog(null, "날짜를 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    else if(hour.equals("no choice")){
        JOptionPane.showMessageDialog(null, "시간을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    else if(minute.equals("no choice")){
        JOptionPane.showMessageDialog(null, "분을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    //SimpleDateFormat da=new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar=jdc_S_Date_Set.getCalendar();
    int year=calendar.get(Calendar.YEAR);
    int month=calendar.get(Calendar.MONTH)+1; //1월은 0으로 반환 0부터스타트
    int day=calendar.get(Calendar.DAY_OF_MONTH);
    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date dn=new Date();
    String date_select=year+"-"+month+"-"+day+" "+hour+":"+minute; //선택한것
    String date_now=df.format(dn);//현재
    System.out.println("현재시간 : "+date_now);
    System.out.println("선택한시간 : "+date_select);
    
    if(!(time_compare(date_now,date_select))){
        JOptionPane.showMessageDialog(null, "과거는 선택할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    lbl_Search_Set_Date.setText(date_select);
    jDialog_search_date_set.dispose();
    }//GEN-LAST:event_btn_S_Date_Set_CompleteActionPerformed

    private void btn_Ct_DateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Ct_DateActionPerformed
        //오늘부터 7일까지 입력가능
        Date today=new Date();
        Date today_seven;
        Calendar cal=Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 7);
        today_seven=new Date(cal.getTimeInMillis());
        jdc_Ct_Date_Set.setMaxSelectableDate(today_seven);
        jdc_Ct_Date_Set.setMinSelectableDate(today);
        
        jDialog_create_date_set.setSize(385, 265);//Frame 크기 지정
        this.jDialog_create_date_set.setLocationRelativeTo(this);
        jDialog_create_date_set.setVisible(true);
        
        
    }//GEN-LAST:event_btn_Ct_DateActionPerformed

    private void btn_Ct_CompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Ct_CompleteActionPerformed
    String temp=txt_Ct_Title.getText();
    //10자 초과인 경우    
    if(isLength(temp,2,20)){ //자릿 수 처리 (문자열, 최소,최대)   
        JOptionPane.showMessageDialog(null, "2자이상 20자이하로 입력해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
    else if(jcb_Ct_Stage.getSelectedItem().toString().equals("no choice")){ //stage선택 안된 경우
        JOptionPane.showMessageDialog(null, "Stage를 선택해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    else if(lbl_Ct_Set_Date.getText().equals("no set")){//시간설정 안된 경우
        JOptionPane.showMessageDialog(null, "시간 설정을 완료해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    else if(jcb_Ct_Leaderjob.getSelectedItem().equals("no choice")){//직업설정 안된 경우
        JOptionPane.showMessageDialog(null, "당신의 직업을 정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    ///중복 확인 생성자id와 시간
    try{ //DB읽어서 중복 확인 생성자id와 시간
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select * from TEAM_INFO";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
                do{
                    if(lbl_Log_User_Id.getText().equals(DBM.DB_rs.getString("LEADER_ID").trim())){//생성자 아이디와
                        if(lbl_Ct_Set_Date.getText().equals(DBM.DB_rs.getString("START_TIME").trim())){//출발시간 같으면 
                            JOptionPane.showMessageDialog(null, "같은 출발시간에 다른 그룹에 소속되었습니다.", "Error", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }
            while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("로그인 처리 중 오류 : "+e.getMessage());
        }
    
    //올바른 입력
    
    try{ //DB열어서 insert
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="INSERT INTO TEAM_INFO VALUES ("; //TEAM_INFO테이블
            strSQL+="'"+lbl_Log_User_Id.getText().trim()+"',";
            strSQL+="'"+txt_Ct_Title.getText().trim()+"',";
            strSQL+="'"+jcb_Ct_Stage.getSelectedItem().toString().trim()+"',";
            strSQL+="'"+lbl_Ct_Set_Date.getText().trim()+"')";
            DBM.DB_stmt.executeUpdate(strSQL);
            strSQL="SELECT MAXNUMBER FROM STAGE_LIST WHERE STAGE='"+jcb_Ct_Stage.getSelectedItem().toString().trim()+"'";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            DBM.DB_rs.next(); //이미 db에있는것 이어서 잘못된 오타가아닌이상 반드시 있음
            String max_string=DBM.DB_rs.getString("MAXNUMBER");
            int max=Integer.parseInt(max_string);
            strSQL="INSERT INTO MEMBER_"+jcb_Ct_Stage.getSelectedItem().toString().trim()+" VALUES ("; //MEMBER_stage명테이블
            strSQL+="'"+lbl_Log_User_Id.getText().trim()+"',";
            strSQL+="'"+lbl_Ct_Set_Date.getText().trim()+"',";
            strSQL+="'"+lbl_Log_User_Id.getText().trim()+"',";
            strSQL+="'"+lbl_Log_User_NickName.getText().trim()+"',";
            strSQL+="'"+jcb_Ct_Leaderjob.getSelectedItem().toString().trim()+"'";
            for(int i=2; i<=max; i++){
                strSQL+=",'no'";
                strSQL+=",'no'";
                strSQL+=",'no'";
            }
            strSQL+=")";
            DBM.DB_stmt.executeUpdate(strSQL);
            DBM.dbClose();
            JOptionPane.showMessageDialog(null, "그룹 생성에 성공하셨습니다.", "확인", JOptionPane.WARNING_MESSAGE);
            jDialog_create_team.dispose();
        }catch(Exception e){
            System.out.println("TEAM생성 중 데이터넘기는 중 오류 : "+e.getMessage());
        } 
    team_search();
    }//GEN-LAST:event_btn_Ct_CompleteActionPerformed

    private void btn_Ct_Date_Set_CompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Ct_Date_Set_CompleteActionPerformed
    String hour=jcb_Ct_Hour.getSelectedItem().toString().trim();
    String minute=jcb_Ct_Minute.getSelectedItem().toString().trim();
    if(jdc_Ct_Date_Set.getDate()==null){
        JOptionPane.showMessageDialog(null, "날짜를 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    else if(hour.equals("no choice")){
        JOptionPane.showMessageDialog(null, "시간을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    else if(minute.equals("no choice")){
        JOptionPane.showMessageDialog(null, "분을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    //SimpleDateFormat da=new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar=jdc_Ct_Date_Set.getCalendar();
    int year=calendar.get(Calendar.YEAR);
    int month=calendar.get(Calendar.MONTH)+1; //1월은 0으로 반환 0부터스타트
    int day=calendar.get(Calendar.DAY_OF_MONTH);
    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date dn=new Date();
    String date_select=year+"-"+month+"-"+day+" "+hour+":"+minute; //선택한것
    String date_now=df.format(dn);//현재
    String dn_plus_hour=null;
    Date dph=null;
    

    //선택한것 +1시간 구현
    try {//트라이 캐치 안쓰면 오류
        
        dph=df.parse(date_now);
    } catch (ParseException ex) {
        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
    Calendar cal=Calendar.getInstance();
    cal.setTime(dph);
    cal.add(Calendar.HOUR, 1);//1시간 추가
    dn_plus_hour=df.format(cal.getTime());
    System.out.println("현재시간 : "+date_now);
    System.out.println("선택한시간 : "+date_select);
    System.out.println("현재시간+1시간 : "+dn_plus_hour);
    
    if(!(time_compare(date_now,date_select))){
        JOptionPane.showMessageDialog(null, "과거는 선택할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    System.out.println(time_compare(dn_plus_hour,date_select));  //현재시간+1시간<선택한시간 이어야함
    if(!(time_compare(dn_plus_hour,date_select))){
        JOptionPane.showMessageDialog(null, "1시간 이후로만 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    lbl_Ct_Set_Date.setText(date_select);
    jDialog_create_date_set.dispose();
    }//GEN-LAST:event_btn_Ct_Date_Set_CompleteActionPerformed

    private void btn_Jt_JoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Jt_JoinActionPerformed
    String job=jcb_Jt_Job.getSelectedItem().toString().trim();
    if(job.equals("no choice")){ //직업설정 안할 시 피드백
        JOptionPane.showMessageDialog(null, "직업을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    String start_time=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 1);
    String stage=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 2);
    String leader_id=(String) jTable_Search.getValueAt(jTable_Search.getSelectedRow(), 4);
    int max=0;
    int empty=0;
    try{ //DB열어서 빈자리 탐색 후 참가
            DBM.dbOpen();
            strSQL="select maxnumber from stage_list where stage='";
            strSQL+=stage.trim();
            strSQL+="'";
            System.out.println(strSQL);
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            DBM.DB_rs.next();
            max=Integer.parseInt(DBM.DB_rs.getString("maxnumber").trim());//최대 인원수 가져옴
            
            strSQL="select * From member_"+stage.trim()+" where leader_id='"+leader_id+"'";
            strSQL+=" and start_TIME='"+start_time.trim()+"'"; //멤버테이블  기본키로 검색 튜플하나만나옴
            System.out.println(strSQL);
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
            do{
                for(int i=1; i<=max; i++){
                    String temp="MEMBER"+i+"_ID";
                    if((DBM.DB_rs.getString(temp).trim().equals(lbl_Log_User_Id.getText().trim()))){ //빈자리 탐색 중 같은 아이디 발견
                        JOptionPane.showMessageDialog(null, "이미 참가된 그룹입니다.", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                }
                for(int i=1; i<=max; i++){
                    String temp="MEMBER"+i+"_ID";
                    if((DBM.DB_rs.getString(temp).trim().equals("no"))){ //빈자리 탐색 후 그 자리번호 반환
                        empty=i;
                        break;
                    }
                    if(i==max){//max인원까지 탐색했는데 "no"를 찾지 못함 인원이 꽉찬 상태
                        JOptionPane.showMessageDialog(null, "그룹에 남는 자리가 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }
            while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            //참가로 업데이트
            strSQL="update member_"+stage.trim()+" set ";
            strSQL+="member"+empty+"_id='"+lbl_Log_User_Id.getText().trim()+"',";
            strSQL+="member"+empty+"_nickname='"+lbl_Log_User_NickName.getText().trim()+"',";
            strSQL+="member"+empty+"_job='"+job+"'";
            strSQL+=" where leader_id='"+leader_id+"'";
            strSQL+=" and start_TIME='"+start_time.trim()+"'";
            System.out.println(strSQL);
            DBM.DB_stmt.executeUpdate(strSQL);
            
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("그룹 참가 중 오류 : "+e.getMessage());
        }
    team_search();
    JOptionPane.showMessageDialog(null, "그룹 참가가 완료되었습니다.", "확인", JOptionPane.WARNING_MESSAGE);
    jDialog_jointeam.dispose();
    }//GEN-LAST:event_btn_Jt_JoinActionPerformed

    private void btn_Ss_CreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Ss_CreateActionPerformed
    //공백문자 및 null인경우
    String temp=txt_Ss_StageName.getText();
    if(isNullOrSpace(temp)){//(공백 및 빈문자열)x 처리
        JOptionPane.showMessageDialog(null, "띄어쓰기 및 공백은 사용 할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
       
    //10자 초과인 경우 
        
    else if(isLength(temp,1,10)){ //자릿 수 처리 (문자열, 최소,최대)   
        JOptionPane.showMessageDialog(null, "10자 이하로 입력해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        } 
    else if(isUpper(temp)){//대문자X처리
        JOptionPane.showMessageDialog(null, "stage명에 대문자는 사용할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
    //특수문자가 있는 경우" 한글 및 영문자, 숫자만 사용 가능합니다"
    //else if(!(temp.matches("^[a-z0-9]*$") || isKorean(temp))) { 
    else if(!(temp.matches("^[a-z0-9가-힣]*$"))) { 
        JOptionPane.showMessageDialog(null, "한글 및 영소문자 숫자만 사용가능합니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
    
    try{ //DB읽어서 중복 처리
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select stage from STAGE_LIST";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
            do{
                System.out.println(DBM.DB_rs.getString("STAGE").trim());
                if(temp.equals(DBM.DB_rs.getString("STAGE").trim())){
                    JOptionPane.showMessageDialog(null, "이미 등록된 Stage입니다.", "Error", JOptionPane.WARNING_MESSAGE);
                    DBM.DB_rs.close();
                    DBM.dbClose();
                    return;
                }
            }
            while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("중복 처리 중 오류 : "+e.getMessage());
        }
        
        
    
    //인원수가 선택이 안될 경우" 인원수를 정해 주세요"
    if(jcb_Ss_Stagemcount.getSelectedItem().toString().equals("no choice")){
        JOptionPane.showMessageDialog(null, "인원 수를 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int membercount=Integer.parseInt(jcb_Ss_Stagemcount.getSelectedItem().toString());
    try{ //DB열어서 insert 올바른 입력일 경우 stage추가
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="INSERT INTO STAGE_LIST VALUES (";
            strSQL+="'"+temp+"',";
            strSQL+="'"+jcb_Ss_Stagemcount.getSelectedItem().toString()+"')";
            System.out.println(strSQL);
            DBM.DB_stmt.executeUpdate(strSQL);
            //멤버_STAGE명 테이블
            strSQL="CREATE TABLE MEMBER_";
            strSQL+=temp;
            strSQL+="( LEADER_ID VARCHAR2(15) , START_TIME VARCHAR2(30) ";
            for(int i=1; i<=membercount; i++){
                strSQL+=", MEMBER"+i+"_ID VARCHAR2(15) NOT NULL";
                strSQL+=", MEMBER"+i+"_NICKNAME VARCHAR2(45) NOT NULL";
                strSQL+=", MEMBER"+i+"_JOB VARCHAR2(30) NOT NULL";
            }
            strSQL+=", constraint m_s_"+temp+" primary key(LEADER_ID,START_TIME))";
            DBM.DB_stmt.execute(strSQL);
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("Stage생성중 오류 : "+e.getMessage());
            return;
        }
    JOptionPane.showMessageDialog(null, "Stage를 생성하였습니다.", "확인", JOptionPane.WARNING_MESSAGE);
    stagelist_Initialize(jcb_Ss_Stagelist);
    }//GEN-LAST:event_btn_Ss_CreateActionPerformed

    private void btn_Ss_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Ss_DeleteActionPerformed
    String temp=jcb_Ss_Stagelist.getSelectedItem().toString();
    if(temp.equals("no choice")){//삭제할 stage 선택안함
        JOptionPane.showMessageDialog(null, "삭제할 Stage를 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    try{ //DB열어서 DELETE
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="DELETE FROM STAGE_LIST WHERE STAGE=";
            strSQL+="'"+temp+"'";
            System.out.println(strSQL);
            DBM.DB_stmt.executeUpdate(strSQL);
            strSQL="DROP TABLE MEMBER_"+temp;
            DBM.DB_stmt.execute(strSQL);
            strSQL="DELETE FROM stage_list where stage='";
            strSQL+=temp+"'";
            DBM.DB_stmt.execute(strSQL);
            strSQL="DELETE FROM team_info where stage='";
            strSQL+=temp+"'";
            DBM.DB_stmt.execute(strSQL);
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("STAGE삭제중 오류 : "+e.getMessage());
        }
    stagelist_Initialize(jcb_Ss_Stagelist);
    JOptionPane.showMessageDialog(null, "Stage를 삭제하였습니다.", "Error", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_btn_Ss_DeleteActionPerformed

    private void btn_Js_CreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Js_CreateActionPerformed
    //공백문자 및 null인경우
    String temp=txt_Js_JobName.getText();
    if(isNullOrSpace(temp)){//(공백 및 빈문자열)x 처리
        JOptionPane.showMessageDialog(null, "띄어쓰기 및 공백은 사용 할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
       
    //10자 초과인 경우    
    else if(isLength(temp,1,10)){ //자릿 수 처리 (문자열, 최소,최대)   
        JOptionPane.showMessageDialog(null, "10자 이하로 입력해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        } 
    else if(isUpper(temp)){//대문자X처리
        JOptionPane.showMessageDialog(null, "직업명에 대문자는 사용할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
    //특수문자가 있는 경우" 한글 및 영문자, 숫자만 사용 가능합니다"
    else if(!(temp.matches("^[a-z0-9가-힣]*$"))) { 
        JOptionPane.showMessageDialog(null, "한글 및 영소문자 숫자만 사용가능합니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
    
    try{ //DB읽어서 중복 처리
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select job_name from JOB_LIST";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
            do{
                System.out.println(DBM.DB_rs.getString("job_name").trim());
                if(temp.equals(DBM.DB_rs.getString("job_name").trim())){
                    JOptionPane.showMessageDialog(null, "이미 등록된 직업입니다.", "Error", JOptionPane.WARNING_MESSAGE);
                    DBM.DB_rs.close();
                    DBM.dbClose();
                    return;
                }
            }
            while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("중복 처리 중 오류 : "+e.getMessage());
        }
        
        
    
    try{ //DB열어서 insert 올바른 입력일 경우 직업추가
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="INSERT INTO JOB_LIST VALUES (";
            strSQL+="'"+temp+"')";
            System.out.println(strSQL);
            DBM.DB_stmt.executeUpdate(strSQL);
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("직업생성중 오류 : "+e.getMessage());
        }
    joblist_Initialize(jcb_Js_Joblist);
    JOptionPane.showMessageDialog(null, "직업을 생성하였습니다.", "확인", JOptionPane.WARNING_MESSAGE);
    
    }//GEN-LAST:event_btn_Js_CreateActionPerformed

    private void btn_Js_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Js_DeleteActionPerformed
    String temp=jcb_Js_Joblist.getSelectedItem().toString();
    if(temp.equals("no choice")){//삭제할 stage 선택안함
        JOptionPane.showMessageDialog(null, "삭제할 직업을 설정해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }
    try{ //DB열어서 DELETE
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="DELETE FROM JOB_LIST WHERE job_name=";
            strSQL+="'"+temp+"'";
            System.out.println(strSQL);
            DBM.DB_stmt.executeUpdate(strSQL);
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("job삭제중 오류 : "+e.getMessage());
        }
    joblist_Initialize(jcb_Js_Joblist);
    JOptionPane.showMessageDialog(null, "직업을 삭제하였습니다.", "확인", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_btn_Js_DeleteActionPerformed

    private void btn_Gns_CompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Gns_CompleteActionPerformed
    //공백문자 및 null인경우
    String temp=txt_Gns_Name.getText();
    if(isNullOrSpace(temp)){//(공백 및 빈문자열)x 처리
        JOptionPane.showMessageDialog(null, "띄어쓰기 및 공백은 사용 할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
       
    //10자 초과인 경우 
        
    else if(isLength(temp,1,10)){ //자릿 수 처리 (문자열, 최소,최대)   
        JOptionPane.showMessageDialog(null, "10자 이하로 입력해주세요", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        } 
    else if(isUpper(temp)){//대문자X처리
        JOptionPane.showMessageDialog(null, "stage명에 대문자는 사용할 수 없습니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
    //특수문자가 있는 경우" 한글 및 영문자, 숫자만 사용 가능합니다"
    //else if(!(temp.matches("^[a-z0-9]*$") || isKorean(temp))) { 
    else if(!(temp.matches("^[a-z0-9가-힣]*$"))) { 
        JOptionPane.showMessageDialog(null, "한글 및 영소문자 숫자만 사용가능합니다.", "Error", JOptionPane.WARNING_MESSAGE);
        return;
        }
    
    try{ //DB읽어서 중복 처리
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="select * from GameOption";
            DBM.DB_rs=DBM.DB_stmt.executeQuery(strSQL);
            if(DBM.DB_rs.next()){
            do{
                System.out.println(DBM.DB_rs.getString("Game_Name").trim());
                if(temp.equals(DBM.DB_rs.getString("Game_Name").trim())){
                    JOptionPane.showMessageDialog(null, "이전 게임 명과 동일합니다", "Error", JOptionPane.WARNING_MESSAGE);
                    DBM.DB_rs.close();
                    DBM.dbClose();
                    return;
                }
            }
            while(DBM.DB_rs.next());
            }
            else{
                System.out.println("읽은 데이터가 없습니다.");
            }
            DBM.DB_rs.close();
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("중복 처리 중 오류 : "+e.getMessage());
        }
        
        
    try{ //DB열어서 insert 올바른 입력일 경우 게임 명 변경
            DBM.dbOpen();
            System.out.println("DB열림");
            strSQL="UPDATE GameOption set Game_Name = ";
            strSQL+="'"+temp+"'";
            DBM.DB_stmt.executeUpdate(strSQL);
            //멤버_STAGE명 테이블
            DBM.dbClose();
        }catch(Exception e){
            System.out.println("게임 명 변경 중 오류 : "+e.getMessage());
            return;
        }
    JOptionPane.showMessageDialog(null, "게임 명을 변경되었습니다.", "확인", JOptionPane.WARNING_MESSAGE);
    jDialog_gamename_set.dispose();
    lbl_GameName.setText(temp);
    }//GEN-LAST:event_btn_Gns_CompleteActionPerformed

    private void btn_LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_LogoutActionPerformed
        if(lbl_Log_User_NickName.getText().equals("No User")|| lbl_Log_User_Id.getText().equals("No User")){
            JOptionPane.showMessageDialog(null, "이미 로그아웃상태 입니다.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        lbl_Log_User_NickName.setText("No User");
        lbl_Log_User_Id.setText("No User");
    }//GEN-LAST:event_btn_LogoutActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
                
            }
            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Create;
    private javax.swing.JButton btn_Ct_Complete;
    private javax.swing.JButton btn_Ct_Date;
    private javax.swing.JButton btn_Ct_Date_Set_Complete;
    private javax.swing.JButton btn_Gns_Complete;
    private javax.swing.JButton btn_Join;
    private javax.swing.JButton btn_Js_Create;
    private javax.swing.JButton btn_Js_Delete;
    private javax.swing.JButton btn_Jt_Join;
    private javax.swing.JButton btn_Log_Complete;
    private javax.swing.JButton btn_Login;
    private javax.swing.JButton btn_Logout;
    private javax.swing.JButton btn_Memeber;
    private javax.swing.JButton btn_Out;
    private javax.swing.JButton btn_Reg;
    private javax.swing.JButton btn_Reg_Complete;
    private javax.swing.JButton btn_Reg_Id_Check;
    private javax.swing.JButton btn_S_Date_Set_Complete;
    private javax.swing.JButton btn_Search;
    private javax.swing.JButton btn_Search_Complete;
    private javax.swing.JButton btn_Search_Date;
    private javax.swing.JButton btn_Set_GameName;
    private javax.swing.JButton btn_Set_Job;
    private javax.swing.JButton btn_Set_Stage;
    private javax.swing.JButton btn_Ss_Create;
    private javax.swing.JButton btn_Ss_Delete;
    private javax.swing.JButton btn_delete;
    private javax.swing.JDialog jDialog_Viewteam;
    private javax.swing.JDialog jDialog_create_date_set;
    private javax.swing.JDialog jDialog_create_team;
    private javax.swing.JDialog jDialog_gamename_set;
    private javax.swing.JDialog jDialog_job_set;
    private javax.swing.JDialog jDialog_jointeam;
    private javax.swing.JDialog jDialog_login;
    private javax.swing.JDialog jDialog_reg;
    private javax.swing.JDialog jDialog_search;
    private javax.swing.JDialog jDialog_search_date_set;
    private javax.swing.JDialog jDialog_stage_set;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable_Member;
    private javax.swing.JTable jTable_Search;
    private javax.swing.JComboBox<String> jcb_Ct_Hour;
    private javax.swing.JComboBox<String> jcb_Ct_Leaderjob;
    private javax.swing.JComboBox<String> jcb_Ct_Minute;
    private javax.swing.JComboBox<String> jcb_Ct_Stage;
    private javax.swing.JComboBox<String> jcb_Js_Joblist;
    private javax.swing.JComboBox<String> jcb_Jt_Job;
    private javax.swing.JCheckBox jcb_Reg_Idcheck;
    private javax.swing.JComboBox<String> jcb_S_Hour1;
    private javax.swing.JComboBox<String> jcb_S_Minute;
    private javax.swing.JComboBox<String> jcb_Search_Stage;
    private javax.swing.JComboBox<String> jcb_Ss_Stagelist;
    private javax.swing.JComboBox<String> jcb_Ss_Stagemcount;
    private com.toedter.calendar.JDateChooser jdc_Ct_Date_Set;
    private com.toedter.calendar.JDateChooser jdc_S_Date_Set;
    private javax.swing.JLabel lbl_Ct_Date_Set_Hour;
    private javax.swing.JLabel lbl_Ct_Date_Set_Minute;
    private javax.swing.JLabel lbl_Ct_Date_Set_Title;
    private javax.swing.JLabel lbl_Ct_Leaderjob;
    private javax.swing.JLabel lbl_Ct_Name;
    private javax.swing.JLabel lbl_Ct_Name_Info;
    private javax.swing.JLabel lbl_Ct_Set_Date;
    private javax.swing.JLabel lbl_Ct_Stage_Choice;
    private javax.swing.JLabel lbl_GameName;
    private javax.swing.JLabel lbl_Gns_Name;
    private javax.swing.JLabel lbl_Gns_title;
    private javax.swing.JLabel lbl_Gns_title2;
    private javax.swing.JLabel lbl_Gns_title3;
    private javax.swing.JLabel lbl_Js_JobName;
    private javax.swing.JLabel lbl_Js_JobNameInfo;
    private javax.swing.JLabel lbl_Js_JobNameInfo2;
    private javax.swing.JLabel lbl_Js_Job_title;
    private javax.swing.JLabel lbl_Js_StageDeleteInfo;
    private javax.swing.JLabel lbl_Jt_job;
    private javax.swing.JLabel lbl_Log_Id;
    private javax.swing.JLabel lbl_Log_Password;
    private javax.swing.JLabel lbl_Log_User_Id;
    private javax.swing.JLabel lbl_Log_User_NickName;
    private javax.swing.JLabel lbl_Reg_Id;
    private javax.swing.JLabel lbl_Reg_Id_Info;
    private javax.swing.JLabel lbl_Reg_NickName;
    private javax.swing.JLabel lbl_Reg_NickName_Info;
    private javax.swing.JLabel lbl_Reg_Password;
    private javax.swing.JLabel lbl_Reg_Password_Check;
    private javax.swing.JLabel lbl_Reg_Password_Info;
    private javax.swing.JLabel lbl_S_Date_Set_Hour;
    private javax.swing.JLabel lbl_S_Date_Set_Minute;
    private javax.swing.JLabel lbl_S_Date_Set_Title;
    private javax.swing.JLabel lbl_Search_Set_Date;
    private javax.swing.JLabel lbl_Search_Stage_Choice;
    private javax.swing.JLabel lbl_Ss_StageDeleteInfo;
    private javax.swing.JLabel lbl_Ss_StageName;
    private javax.swing.JLabel lbl_Ss_StageNameInfo;
    private javax.swing.JLabel lbl_Ss_StageNameInfo2;
    private javax.swing.JLabel lbl_Ss_Stage_title;
    private javax.swing.JLabel lbl_Ss_Stagemcount;
    private javax.swing.JLabel lbl_Vt_Stage;
    private javax.swing.JLabel lbl_Vt_Teamtitle;
    private javax.swing.JTextField txt_Ct_Title;
    private javax.swing.JTextField txt_Gns_Name;
    private javax.swing.JTextField txt_Js_JobName;
    private javax.swing.JTextField txt_Log_Id;
    private javax.swing.JPasswordField txt_Log_Password;
    private javax.swing.JTextField txt_Reg_Id;
    private javax.swing.JTextField txt_Reg_NickName;
    private javax.swing.JPasswordField txt_Reg_Password;
    private javax.swing.JPasswordField txt_Reg_Password_Check;
    private javax.swing.JTextField txt_Ss_StageName;
    // End of variables declaration//GEN-END:variables

    
}
