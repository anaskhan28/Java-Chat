import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username){
        try{
           
                this.socket = socket;
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.username = username;

            
        }catch (IOException e){
        closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);

            while(socket.isConnected()){
                String messageToSend = sc.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }
        } catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);

        }
    }

    public void readMessage(){
        new Thread( new Runnable() {

            @Override
            public void run() {
                String msfFromGroupChat;

                while(socket.isConnected()){
                try{
                    msfFromGroupChat = bufferedReader.readLine();
                    System.out.println(msfFromGroupChat);
                } catch (IOException e){
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }

                }
                
            }
            
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader!= null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.getStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException{
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter Your username for the group chat");
    String username = sc.nextLine();
    Socket socket = new Socket("localhost", 1234);
    Client client = new Client(socket, username);
    client.readMessage();
    client.sendMessage();
    }

}
