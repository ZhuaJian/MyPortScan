import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PortScan extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridbag;
	private GridBagConstraints constraints;
	private JPanel mainJPanel,panel1,panel1_1,panel1_2,panel2,panel2_2,panel3,panel4,panel5;
	private ButtonGroup buttonGroup; 
	private JRadioButton scanType1,scanType2; 
	private JLabel startJLabel,endJLabel,threadNum; 
	private JLabel progressJLabel,resultJLabel; 
	private JTextField customPorts,startPort,endPort,customDomain,customThreadNum;  
	private JButton beginJButton; 
	private JScrollPane progressPane,resultPane;  
	private JTextArea progressJtJTextArea,resultJTextArea; 
	
	private JMenuBar jMenuBar;
	private JMenu help;
	private JMenuItem author,contact,version,readme;
	
	private Font menuFont = new Font("宋体", Font.LAYOUT_NO_LIMIT_CONTEXT, 14);  
	private Font contentFont = new Font("宋体", Font.LAYOUT_NO_LIMIT_CONTEXT, 16); 

	public PortScan(){
		super("小朱多线程端口扫描工具_V1.0");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(900, 600));
		int frameWidth = this.getPreferredSize().width;  
		int frameHeight = this.getPreferredSize().height; 
		setSize(frameWidth,frameHeight);
		setLocation((screenSize.width - frameWidth) / 2,(screenSize.height - frameHeight) / 2);
		
		mainJPanel = new JPanel();
		panel1 = new JPanel();
		panel1_1 = new JPanel();
		panel1_2 = new JPanel();
		panel2 = new JPanel();
		panel2_2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel5 = new JPanel();
		buttonGroup = new ButtonGroup();
		scanType1 = new JRadioButton("扫描常见端口:");
		scanType2 = new JRadioButton("扫描一个连续段的端口:");
		startJLabel = new JLabel("起始端口:");
		endJLabel = new JLabel("结束端口:");
		threadNum = new JLabel("线程:");
		progressJLabel = new JLabel("扫描进度");
		resultJLabel = new JLabel("扫描结果");
		customPorts = new JTextField("21,22,23,25,26,69,80,110," +
				"143,443,465,1080,1158,1433,1521,2100,3306," +
				"3389,7001,8080,8081,8888,9080,9090,43958");
		startPort = new JTextField("20", 10);
		endPort = new JTextField("9000", 10);
		customDomain = new JTextField("www.waner.cn", 25);
		customThreadNum = new JTextField("5", 5);
		beginJButton = new JButton("开始扫描");
		progressPane = new JScrollPane();
		resultPane = new JScrollPane();
		progressJtJTextArea = new JTextArea(18, 20);
		resultJTextArea = new JTextArea(18, 20);
		
		buttonGroup.add(scanType1);
		buttonGroup.add(scanType2);
		scanType1.setSelected(true);
		
		gridbag = new GridBagLayout();
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		mainJPanel.setLayout(gridbag);
		
		constraints.gridwidth = 0; 
		constraints.gridheight = 1;
		constraints.weightx = 1;  
		constraints.weighty = 0;  
		gridbag.setConstraints(scanType1, constraints);
		scanType1.setFont(contentFont);
		mainJPanel.add(scanType1);
		
		gridbag.setConstraints(customPorts, constraints);
		customPorts.setFont(contentFont);
		mainJPanel.add(customPorts);
		
		gridbag.setConstraints(scanType2, constraints);
		scanType2.setFont(contentFont);
		mainJPanel.add(scanType2);
		
		gridbag.setConstraints(panel1, constraints);
		mainJPanel.add(panel1);
		
		gridbag.setConstraints(panel2, constraints);
		mainJPanel.add(panel2);
		
		constraints.weighty = 1;
		gridbag.setConstraints(panel3, constraints);
		mainJPanel.add(panel3);
		
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT,30,5));
		panel1.add(panel1_1);
		panel1.add(panel1_2);
		panel1_1.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
		startJLabel.setFont(contentFont);
		panel1_1.add(startJLabel);
		startPort.setFont(contentFont);
		panel1_1.add(startPort);
		panel1_2.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
		endJLabel.setFont(contentFont);
		panel1_2.add(endJLabel);
		endPort.setFont(contentFont);
		panel1_2.add(endPort);
		
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		customDomain.setFont(contentFont);
		panel2.add(customDomain);
		panel2.add(panel2_2);
		panel2_2.setLayout(new FlowLayout());
		threadNum.setFont(contentFont);
		panel2_2.add(threadNum);
		customThreadNum.setFont(contentFont);
		panel2_2.add(customThreadNum);	
		beginJButton.setFont(contentFont);
		panel2.add(beginJButton);
		
		panel3.setLayout(new GridLayout(1, 2));
		panel3.add(panel4);
		panel3.add(panel5);
		panel4.setLayout(new BorderLayout());
		progressJLabel.setFont(contentFont);
		progressJLabel.setHorizontalAlignment(JLabel.CENTER);
		panel4.add(progressJLabel,BorderLayout.NORTH);
		panel4.add(progressPane,BorderLayout.CENTER);
		progressJtJTextArea.setFont(contentFont);
		progressPane.setViewportView(progressJtJTextArea);
		progressJtJTextArea.setEditable(false);
		progressJtJTextArea.setLineWrap(true);
		progressJtJTextArea.setWrapStyleWord(true);
		panel5.setLayout(new BorderLayout());
		resultJLabel.setFont(contentFont);
		resultJLabel.setHorizontalAlignment(JLabel.CENTER);
		panel5.add(resultJLabel,BorderLayout.NORTH);
		panel5.add(resultPane,BorderLayout.CENTER);
		resultJTextArea.setFont(contentFont);
		resultPane.setViewportView(resultJTextArea);
		resultJTextArea.setEditable(false);
		resultJTextArea.setLineWrap(true);
		resultJTextArea.setWrapStyleWord(true);
		
		jMenuBar = new JMenuBar();
		help = new JMenu("关于");
		author = new JMenuItem("作者");
		contact = new JMenuItem("联系方式");
		version = new JMenuItem("版本");
		readme = new JMenuItem("说明");
		help.setFont(menuFont);
		jMenuBar.add(help);
		author.setFont(menuFont);
		help.add(author);
		contact.setFont(menuFont);
		help.add(contact);
		version.setFont(menuFont);
		help.add(version);
		readme.setFont(menuFont);
		help.add(readme);
			
		add(mainJPanel);
		setJMenuBar(jMenuBar);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		beginJButton.addActionListener(this);
		author.addActionListener(this);
		contact.addActionListener(this);
		version.addActionListener(this);
		readme.addActionListener(this);
	}	

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == beginJButton){
			progressJtJTextArea.setText("");
			resultJTextArea.setText("");
			String domain = getDomainString(customDomain.getText().trim());
			int threadNumber = Integer.parseInt(customThreadNum.getText().trim());
			if(domain == null|| domain.equals(""))
				return ;
			if(scanType1.isSelected()){
				String[] portsString = customPorts.getText().split(",");
				int[] ports = new int[portsString.length];
				for(int i=0;i<portsString.length;i++)
					ports[i] = Integer.parseInt(portsString[i].trim());
				ExecutorService threadPool = Executors.newCachedThreadPool();
				for (int i = 0; i < threadNumber; i++) {
					ScanThread1 scanThread1 = new ScanThread1(domain, ports,
							threadNumber, i, 800);
					threadPool.execute(scanThread1);
				}
				threadPool.shutdown();
			}
			else if(scanType2.isSelected()){
				int startPortInt = Integer.parseInt(startPort.getText().trim());
				int endPortInt = Integer.parseInt(endPort.getText().trim());
				
				ExecutorService threadPool = Executors.newCachedThreadPool();
				for (int i = 0; i < threadNumber; i++) {
					ScanThread2 scanThread2 = new ScanThread2(domain, startPortInt,endPortInt,
							threadNumber, i, 800);
					threadPool.execute(scanThread2);
				}
				threadPool.shutdown();
			}	
		}
		else if(e.getSource() == author){
			JOptionPane.showMessageDialog(this, "ZhuaJian","作者：",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == contact){
			JOptionPane.showMessageDialog(this, "E-mail:732718545@qq.com\n" +
					"https://github.com/ZhuaJian","联系方式：",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == version){
			JOptionPane.showMessageDialog(this, "version_1.0","版本：",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource() == readme){
			JOptionPane.showMessageDialog(this, "一个简单的多线程端口扫描工具，支持两种扫描方式","说明：",JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	class ScanThread1 implements Runnable{
		private String domain;
		private int[] ports; 
		private int threadNumber, serial, timeout; 
		
		public ScanThread1(String domain,int[] ports, int threadNumber, int serial,
				int timeout) {
			this.domain = domain;
			this.ports = ports;
			this.threadNumber = threadNumber;
			this.serial = serial;
			this.timeout = timeout;
		}

		public void run() {
			int port = 0;
			try {
				InetAddress address = InetAddress.getByName(domain);
				Socket socket;
				SocketAddress socketAddress;
				if (ports.length < 1)
					return;
				for (port = 0 + serial; port <= ports.length - 1; port += threadNumber) {
					SwingUtilities.invokeLater(new ProgressRunnable( ports[port]));  
				
					socket = new Socket();
					socketAddress = new InetSocketAddress(address, ports[port]);
					try {
						socket.connect(socketAddress, timeout);
						socket.close();
						SwingUtilities.invokeLater(new ResultRunnable( ports[port])); 				
					} catch (IOException e) {
						
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	class ScanThread2 implements Runnable{
		private String domain;
		private int startPort = 20,endPort = 100; 
		private int threadNumber, serial, timeout; 
		
		public ScanThread2(String domain, int startPort, int endPort,
				int threadNumber, int serial, int timeout) {
			this.domain = domain;
			this.startPort = startPort;
			this.endPort = endPort;
			this.threadNumber = threadNumber;
			this.serial = serial;
			this.timeout = timeout;
		}

		public void run() {
			int port = 0;
			try {
				InetAddress address = InetAddress.getByName(domain);
				Socket socket;
				SocketAddress socketAddress;
				for (port = startPort + serial; port <= endPort; port += threadNumber) {
					SwingUtilities.invokeLater(new ProgressRunnable(port)); 
					
					socket = new Socket();
					socketAddress = new InetSocketAddress(address, port);
					try {
						socket.connect(socketAddress, timeout); 
						socket.close();
						SwingUtilities.invokeLater(new ResultRunnable(port)); 
					} catch (IOException e) {

					}
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	class ProgressRunnable implements Runnable{
		private int currentPort = 0; 
		
		public ProgressRunnable(int currentPort) {
			this.currentPort = currentPort;
		}

		public void run() {
			progressJtJTextArea.setEditable(true);
			progressJtJTextArea.append("正在扫描端口：" + currentPort + "\n");
			progressJtJTextArea.setEditable(false);	
			progressJtJTextArea.selectAll();
			progressJtJTextArea.setCaretPosition(progressJtJTextArea.getSelectionEnd());
		}

	}
	
	class ResultRunnable implements Runnable{
		private int currentPort = 0; 
		
		public ResultRunnable(int currentPort) {
			this.currentPort = currentPort;
		}
		public void run() {
			resultJTextArea.setEditable(true);
			resultJTextArea.append("端口：" + currentPort + "    开放\n");
			resultJTextArea.setEditable(false);
			resultJTextArea.selectAll();
			resultJTextArea.setCaretPosition(resultJTextArea.getSelectionEnd());
		}
	}
	
	public static String getDomainString(String str){
		String reg = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()){
			return matcher.group();
		}
		return "";	
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new PortScan();	
			}
		});
	}
}

