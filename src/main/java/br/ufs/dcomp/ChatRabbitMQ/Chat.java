package br.ufs.dcomp.ChatRabbitMQ;
import java.util.*;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Chat {

  public static String usuario = "";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("34.224.95.66"); // Alterar
    factory.setUsername("Arthur2"); // Alterar
    factory.setPassword("SD@2704"); // Alterar
    factory.setVirtualHost("/");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
   
    Scanner dado = new Scanner(System.in);
    
    System.out.print("User: ");
    String user = dado.nextLine();
    String QUEUE_NAME = user;
                      //(queue-name, durable, exclusive, auto-delete, params); 
    channel.queueDeclare(QUEUE_NAME, false,   false,     false,       null);
    
    Consumer consumer = new DefaultConsumer(channel) {
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
      throws IOException {
        
        String message = new String(body, "UTF-8");
        System.out.println("");
        System.out.println(message);
        System.out.println(Chat.usuario + ">p> ");

      }
    };

    DateFormat day = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat hour = new SimpleDateFormat("HH:mm");
  
    Date date = new Date();
    String data = day.format(date);
    String hora = hour.format(date);
    String mensagem;
    
    while(true){
      channel.basicConsume(QUEUE_NAME, true, consumer);
      System.out.println(">> ");
      mensagem = dado.nextLine();
      
      while(mensagem.charAt(0) == '@'){
        Chat.usuario = mensagem;
        do{
          System.out.print(Chat.usuario + ">> ");
          mensagem = dado.nextLine();
          if(mensagem.charAt(0) != '@'){
            String message = "(" + data + " às " + hora + ") " + Chat.usuario.substring(1, Chat.usuario.length()) + " diz: " + mensagem;
            channel.basicPublish("",Chat.usuario.substring(1, Chat.usuario.length()), null,  message.getBytes("UTF-8"));
          }
        }while(mensagem.charAt(0) != '@');
      }
    }
  }
  
}