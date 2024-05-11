package application;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

class ColorPair {
    private int id;
    private String color;

    public ColorPair(int id, String color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }
}
public class Client extends Application {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private static boolean quited = false;
    @FXML
    private TextField usernameBox;
    
    private static TextField _usernameBox;
    
    @FXML
    private Label usernamelbl;
    
    private static Label _usernamelbl;
    
    @FXML
    private Label yourTurnLbl;
    
    private static Label _yourTurnLbl;
    
    @FXML
    private Button readyButton;
    
    private static Button _readyButton;
    
    @FXML
    private Button enterButton;
    
    private static Button _enterButton;
    
    @FXML
    private Button passBtn;
    
    private static Button _passBtn;
    
    @FXML
    private Label userNameLbl;
    
    private static Label _userNameLbl;
    @FXML
    private Label serverMessageLbl;
    
    private static Label _serverMessageLbl;
    
    @FXML
    private Label mLbl11, mLbl12, mLbl13, mLbl14, mLbl15, mLbl16, mLbl17, mLbl18, mLbl19, mLbl110;
    @FXML
    private Label mLbl21, mLbl22, mLbl23, mLbl24, mLbl25, mLbl26, mLbl27, mLbl28, mLbl29, mLbl210;
    
    @FXML
    private Label mLbl31, mLbl32, mLbl33, mLbl34, mLbl35, mLbl36, mLbl37, mLbl38, mLbl39, mLbl310;
    
    @FXML
    private Label mLbl41, mLbl42, mLbl43, mLbl44, mLbl45, mLbl46, mLbl47, mLbl48, mLbl49, mLbl410;
    
    @FXML
    private Label mLbl51, mLbl52, mLbl53, mLbl54, mLbl55, mLbl56, mLbl57, mLbl58, mLbl59, mLbl510;
   
    @FXML
    private Label mLbl61, mLbl62, mLbl63, mLbl64, mLbl65, mLbl66, mLbl67, mLbl68, mLbl69, mLbl610;

    private static Label[][] labels;
    
    @FXML
    private Button freedomButton;
    
    @FXML
    private Button replaceBtn;
    
    @FXML
    private Button dMoveBtn;
    
    private static Button[] buttons;
    
    
    private static boolean canPut = false;
    
	private static boolean doubleMoveJokerUsed = false;
    private static boolean replaceJokerUsed = false;
    private static boolean freedomJokerUsed = false;
    static List<ColorPair> perechiIdCuloare = new ArrayList<>();
    private static Socket _socket;
    
    @FXML
    public void setPass() {
    	try{
    		
    			PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                
                
                String message = "pass";
                out.println(message);

                System.out.println("Ai trimis ca faci " + message + ".");
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + HOST);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + HOST);
            System.exit(1);
        }
    }
    @FXML
    public void sendUsername() {
    	
    	try{
    		
    		PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            
            
            String userName = usernameBox.getText();
            userNameLbl.setText("Username: "+userName);
            out.println(userName);
            System.out.println("Connected as " + userName + ". Type 'quit' to exit.");
            setReadyPicker(1f);
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + HOST);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + HOST);
            System.exit(1);
        }
    	
    }
    public void setMatrice(String[] rows, int[][] reconstructedMatrix) {
    	for (int i = 0; i < rows.length; i++) {
		    String[] numbers = rows[i].split(" ");
		    reconstructedMatrix[i] = new int[numbers.length];
		    for (int j = 0; j < numbers.length; j++) {
		        reconstructedMatrix[i][j] = Integer.parseInt(numbers[j]);
		        
		    }
		}
    }
    @FXML
    public void setDMove() {
    	try{
    		if(doubleMoveJokerUsed == false && canPut == true) {
    			PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                // Start a new thread to handle reading messages from the server
                
                String message = "double move";
                out.println(message);

                System.out.println("Ai trimis ca folosesti " + message + ".");
                
    		}
    		
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + HOST);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + HOST);
            System.exit(1);
        }
    }
    @FXML
    public void setReplaceJoker() {
    	try{
    		if(replaceJokerUsed == false && canPut == true) {
    			PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                // Start a new thread to handle reading messages from the server
                
                String message = "replace";
                out.println(message);

                System.out.println("Ai trimis ca folosesti " + message + ".");
                
    		}
    		
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + HOST);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + HOST);
            System.exit(1);
        }
    }
    @FXML
    public void setFreedomJoker() {
    	
    	try{
    		if(freedomJokerUsed == false && canPut == true) {
    			PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                // Start a new thread to handle reading messages from the server
                
                String message = "freedom";
                out.println(message);

                System.out.println("Ai trimis ca folosesti " + message + ".");
                
    		}
    		
    		
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + HOST);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + HOST);
            System.exit(1);
        }
    	
    }
    
    
    @FXML
    public void sendReady() {
    	
    	try{
    		
    		PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            // Start a new thread to handle reading messages from the server
            
            String message = "ready";
            out.println(message);

            System.out.println("Ai trimis ca esti " + message + ".");
            
            
            
            //String userName = stdIn.readLine();
            //out.println(userName);

            
            // Continuously read user input and send it to the server
            /*
            while (true) {
                userInput = stdIn.readLine();
                if (userInput != null) {
                    out.println(userInput);
                    if ("quit".equalsIgnoreCase(userInput.trim())) {
                        break; // Exit the loop if user types 'quit'
                    }
                }
            }
            */
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + HOST);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + HOST);
            System.exit(1);
        }
    	
    }
    
    @Override
    public void start(Stage primaryStage) {
    	try {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("Client.fxml"));
            Parent root = loader.load();
            
            Scene scene = new Scene(root, 680, 490);
            Stage stage = new Stage();
            stage.setTitle("Client Application");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            
            
            
        }catch (IOException e) {
                e.printStackTrace();
        }
    }
    @FXML
    private void initialize() {
        
        buttons = new Button[] {
            	freedomButton,
            	replaceBtn,
            	dMoveBtn
            };
        labels = new Label[][] {
            {mLbl11, mLbl12, mLbl13, mLbl14, mLbl15, mLbl16, mLbl17, mLbl18, mLbl19, mLbl110},
            {mLbl21, mLbl22, mLbl23, mLbl24, mLbl25, mLbl26, mLbl27, mLbl28, mLbl29, mLbl210},
            {mLbl31, mLbl32, mLbl33, mLbl34, mLbl35, mLbl36, mLbl37, mLbl38, mLbl39, mLbl310},
            {mLbl41, mLbl42, mLbl43, mLbl44, mLbl45, mLbl46, mLbl47, mLbl48, mLbl49, mLbl410},
            {mLbl51, mLbl52, mLbl53, mLbl54, mLbl55, mLbl56, mLbl57, mLbl58, mLbl59, mLbl510},
            {mLbl61, mLbl62, mLbl63, mLbl64, mLbl65, mLbl66, mLbl67, mLbl68, mLbl69, mLbl610}
        };
        _usernameBox = usernameBox;
        _usernamelbl = usernamelbl;
        _readyButton = readyButton;
        _yourTurnLbl = yourTurnLbl;
        _serverMessageLbl = serverMessageLbl;
        _passBtn = passBtn;
        _userNameLbl = userNameLbl;
        _enterButton = enterButton;
        _readyButton.setOpacity(0f);
        _readyButton.setDisable(true);
        _serverMessageLbl.setOpacity(0f);
        _yourTurnLbl.setVisible(false);
        
        setGameOpacity(0f);
        
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                int finalI = i;
                int finalJ = j;
                
                labels[i][j].setOnMouseClicked(event -> {
                    if(canPut == true) {
                    	 System.out.println("Label apăsat: [" + finalI + "][" + finalJ + "]");
                    	 
                    	 PrintWriter out = null;
						try {
							out = new PrintWriter(_socket.getOutputStream(), true);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                         try {
							BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                         BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                         // Start a new thread to handle reading messages from the server
                         
                         String message = "put " + finalI +" "+ finalJ;
                         out.println(message);

                         System.out.println("Ai trimis ca muti " + message + ".");
                         canPut = false;
                    }
                   
                    
                });
            }
        }
    }

    public static void main(String[] args) {
    	System.out.println("Aici e main1");
    	try (Socket socket = new Socket(HOST, PORT)) {
    		_socket = socket;
        	System.out.println("Am trimis socket");
        	PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            // Start a new thread to handle reading messages from the server
            
            Thread serverListener = new Thread(() -> {
                try {
                    String fromServer;
                    while ((fromServer = in.readLine()) != "") {
                    	
                    	String[] parts = fromServer.split("\\|\\|");
                    	
                    	boolean yourTurn = parts.length > 1;
                    	if(fromServer.contains("You are now ready")) {
                    		Platform.runLater(() -> {
                    			_serverMessageLbl.setText("Esti ready!");
                        		_serverMessageLbl.setOpacity(1f);
                			});
                    	}
                    	if(fromServer.contains("You are now not ready")) {
                    		Platform.runLater(() -> {
                    			_serverMessageLbl.setText("Nu esti ready!");
                        		_serverMessageLbl.setOpacity(1f);
                			});
                    	}
                    	if(fromServer.contains("Invalid move. You can only place a piece in an empty cell with at least one neighboring cell of the same color."))
                    	{
                    		canPut = true;
                    		Platform.runLater(() -> {
                    			_serverMessageLbl.setText("Nu poti sa pui piesa acolo!");
                        		_serverMessageLbl.setOpacity(1f);
                			});
                    		
                    	}
                    	if(fromServer.contains("Piece was placed.")) {
                    		Platform.runLater(() -> {
                    			//_serverMessageLbl.setText("Nu poti sa pui piesa acolo!");
                        		_serverMessageLbl.setOpacity(0f);
                			});
                    	}
                    	
                    	if(fromServer.contains("Freedom joker is now off.")) {
                    		freedomJokerUsed = false;
                    	}
                    	if(fromServer.contains("You don't have freedom joker")) {
                    		freedomJokerUsed = true;
                    	}
                    	if(fromServer.contains("Replace joker is now off.")) {
                    		replaceJokerUsed = false;
                    	}
                    	if(fromServer.contains("You don't have replace joker")) {
                    		replaceJokerUsed = true;
                    	}
                    	if(fromServer.contains("Double move joker is now off.")) {
                    		doubleMoveJokerUsed = false;
                    	}
                    	if(fromServer.contains("You don't have double move joker")) {
                    		doubleMoveJokerUsed = true;
                    	}
                    	if(fromServer.contains("The winner is")) {
                    		String messaj = fromServer;
                    		Platform.runLater(() -> {
                    			_serverMessageLbl.setText(messaj);
                        		_serverMessageLbl.setOpacity(1f);
                			});
                    	}
                    	if(fromServer.contains("Color changed to")) {
                    		int startIndex = fromServer.indexOf("Color changed to");
                    		if (startIndex != -1) {
                    		    
                    		    char character = fromServer.charAt(startIndex + "Color changed to".length()+1);
                    		    switch(character) {
    		                	case 'w':
    		                		//labels[i][j].setTextFill()
    		                		_userNameLbl.setTextFill(Color.WHITE);
    		                		break;
    		                	case 'r':
    		                		_userNameLbl.setTextFill(Color.RED);
    		                		//labels[i][j].setStyle("-fx-background-color: red;");
    		                		break;
    		                	case 'g':
    		                		_userNameLbl.setTextFill(Color.GREEN);
    		                		break;
    		                	case 'b':
    		                		_userNameLbl.setTextFill(Color.BLUE);
    		                		break;
    		                	case 'y':
    		                		_userNameLbl.setTextFill(Color.YELLOW);
    		                		break;
    		                	case 'p':
    		                		_userNameLbl.setTextFill(Color.PURPLE);
    		                		break;
    		                		
    		                	default:
    		                		break;
                    		    }
                    		} 
                    	}
                    	if(fromServer.contains("Colors:")) {
                    		String bucataFaraPrefix = fromServer.substring(fromServer.indexOf(":") + 1);
                    		String[] colorparts = bucataFaraPrefix.split(",");
                    		perechiIdCuloare.add(new ColorPair(Integer.parseInt("0"), "w"));
                    		for (int i = 0; i < colorparts.length; i++) {
                    	        String[] idColor = colorparts[i].split("\\|\\|");
                    	        perechiIdCuloare.add(new ColorPair(Integer.parseInt(idColor[0]), idColor[1]));
                    	        
                    	    }
                    	}
                    	if(Character.isDigit(fromServer.charAt(0))) {
                    		//Este matrice
                    		
                    		Platform.runLater(() -> {
                				setGameOpacity(1f);
                				setUserPicker(0f);
                				_readyButton.setOpacity(0f);
                				_readyButton.setDisable(true);
                				
                                		_serverMessageLbl.setOpacity(0f);
                        		
                			});
                    		
                    		
                    		
                    		System.out.println(parts[0]);
                    		String matrice = parts[0];
                    		String[] rows = matrice.split("\\|");
                    		int[][] reconstructedMatrix = new int[rows.length][];
                            
                    		for (int i = 0; i < rows.length; i++) {
                    		    String[] numbers = rows[i].split(" ");
                    		    reconstructedMatrix[i] = new int[numbers.length];
                    		    for (int j = 0; j < numbers.length; j++) {
                    		        reconstructedMatrix[i][j] = Integer.parseInt(numbers[j]);
                    		    }
                    		}
                    		Platform.runLater(() -> {
                    			Map<Integer, String> idToColorMap = new HashMap<>();
                    			for (ColorPair pair : perechiIdCuloare) {
                    			    idToColorMap.put(pair.getId(), pair.getColor());
                    			}
                    			
                    		    for (int i = 0; i < reconstructedMatrix.length; i++) {
                    		        for (int j = 0; j < reconstructedMatrix[i].length; j++) {
                    		            int idCautat = reconstructedMatrix[i][j]; // Id-ul din matrice
                    		            if (idToColorMap.containsKey(idCautat)) {
                    		                String culoare = idToColorMap.get(idCautat);
                    		                switch(culoare) {
                    		                	case "w":
                    		                		//labels[i][j].setTextFill()
                    		                		labels[i][j].setTextFill(Color.WHITE);
                    		                		break;
                    		                	case "r":
                    		                		labels[i][j].setTextFill(Color.RED);
                    		                		//labels[i][j].setStyle("-fx-background-color: red;");
                    		                		break;
                    		                	case "g":
                    		                		labels[i][j].setTextFill(Color.GREEN);
                    		                		break;
                    		                	case "b":
                    		                		labels[i][j].setTextFill(Color.BLUE);
                    		                		break;
                    		                	case "y":
                    		                		labels[i][j].setTextFill(Color.YELLOW);
                    		                		break;
                    		                	case "p":
                    		                		labels[i][j].setTextFill(Color.PURPLE);
                    		                		break;
                    		                		
                    		                	default:
                    		                		break;
                    		                }
                    		            }
                    		            labels[i][j].setText(String.valueOf(reconstructedMatrix[i][j]));
                    		        }
                    		    }
                    		    if(freedomJokerUsed == true) {
                    		    	
                        			//System.out.println("Test:"+buttons[0].getStyle());
                        			buttons[0].setStyle(buttons[0].getStyle()+"-fx-border-color: red;");
                            	}
                    		    if(replaceJokerUsed == true) {
                    		    	
                        			//System.out.println("Test:"+buttons[0].getStyle());
                        			buttons[1].setStyle(buttons[1].getStyle()+"-fx-border-color: red;");
                            	}
                    		    if(doubleMoveJokerUsed == true) {
                    		    	
                        			//System.out.println("Test:"+buttons[0].getStyle());
                        			buttons[2].setStyle(buttons[2].getStyle()+"-fx-border-color: red;");
                            	}
                    		});
                    		
                    		
                    		
                    		if(yourTurn == true) 
                    		{
                    			System.out.println("Este randul tau");
                    			//Toggle click buttons
                    			canPut = true;
                    			_yourTurnLbl.setVisible(true);
                    		}
                    		else {
                    			//Toggle unclick buttons
                    			canPut = false;
                    			_yourTurnLbl.setVisible(false);
                    		}
                    	}
                    	else
                    	{
                    		if(canPut == true) {
                    			if(fromServer.contains("Freedom joker is now on")) {
                        			
                            		Platform.runLater(() -> {
                                        
                            			buttons[0].setStyle(buttons[0].getStyle()+"-fx-border-color: yellow;");
                                    });
                            	
                            	}
                    			if(fromServer.contains("Freedom joker is now off")) {
                        			
                            		Platform.runLater(() -> {
                                        
                            			buttons[0].setStyle(buttons[0].getStyle()+"-fx-border-color: none;");
                                    });
                            	
                            	}
                    			if(fromServer.contains("Replace joker is now on")) {
    
                            		Platform.runLater(() -> {
                                        
                            			buttons[1].setStyle(buttons[1].getStyle()+"-fx-border-color: yellow;");
                                    });
                            	
                            	}
                    			if(fromServer.contains("Replace joker is now off")) {
                        			
                            		Platform.runLater(() -> {
                                        
                            			buttons[1].setStyle(buttons[1].getStyle()+"-fx-border-color: none;");
                                    });
                            	
                            	}
                    			if(fromServer.contains("Double move joker is now on")) {
                    			    
                            		Platform.runLater(() -> {
                                        
                            			buttons[2].setStyle(buttons[2].getStyle()+"-fx-border-color: yellow;");
                                    });
                            	
                            	}
                    			if(fromServer.contains("Double move joker is now off")) {
                        			
                            		Platform.runLater(() -> {
                                        
                            			buttons[2].setStyle(buttons[2].getStyle()+"-fx-border-color: none;");
                                    });
                            	
                            	}
                    		}
                    		
                    		
                    		System.out.println(fromServer);
                    	}
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed by server.");
                }
            });
            serverListener.start();
    		launch(args);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + HOST);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + HOST);
            System.exit(1);
        }	
    }
    public static void setGameOpacity(double opac){
    	 for (int i = 0; i < labels.length; i++) {
             for (int j = 0; j < labels[i].length; j++) {
                 int finalI = i;
                 int finalJ = j;
                 labels[i][j].setOpacity(opac);
                 if(opac == 0f) 
                	 labels[i][j].setDisable(true);
                 else
                	 labels[i][j].setDisable(false);
             }
    	 }
    	 for(int i=0;i<3;i++) {
    		 buttons[i].setOpacity(opac);
    	 }
    	 _passBtn.setOpacity(opac);
    }
    public static void setUserPicker(double opac){
   	 	_usernameBox.setOpacity(opac);
   	 	_usernamelbl.setOpacity(opac);
   	 	_enterButton.setOpacity(opac);
   	 	if(opac == 0f) {
   	 		_usernameBox.setDisable(true);
   	 		_usernamelbl.setDisable(true);
   	 		_enterButton.setDisable(true);
   	 		
   	 	}
   }
    public static void setReadyPicker(double opac){
   	 	_readyButton.setOpacity(opac);
   	 	if(opac == 0f) {
   	 		_readyButton.setDisable(true);
   	 		setUserPicker(1f);
   	 	}else
   	 	{
   	 		_readyButton.setDisable(false);
   	 		setUserPicker(0f);
   	 	}
   	 		
   	 	
   }
    
    @FXML
    public void exitFunction() {
    	Platform.exit();
    }
    
}
