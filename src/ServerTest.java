package application;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ServerTest {
	private Server server;

    @Before
    public void setUp() {
        try {
        	if (server == null) {
        		server = new Server();
        	}
        } catch (IOException e) {
            fail("Failed to create the server: " + e.getMessage());
        }
    }
    
    @After
    public void tearDown() {
        if (server != null) {
            server.stopServer();
        }
    }
    
    @Test
    public void testRemoveClient() throws IOException {
    	Socket mockSocket = mock(Socket.class);
    	
    	InputStream mockInputStream = mock(InputStream.class);
    	OutputStream mockOutputStream = mock(OutputStream.class);
    	
    	when(mockSocket.getInputStream()).thenReturn(mockInputStream);
    	when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
    	
    	// create bunch of client handlers
        ClientHandler client1 = new ClientHandler(mockSocket, server, 1);
        ClientHandler client2 = new ClientHandler(mockSocket, server, 2);

        server.addClient(client1);
        server.addClient(client2);

        int initialSize = server.getNumClients();
        
        server.removeClient(client1);
        
        int finalSize = server.getNumClients();
        
        assertEquals(initialSize - 1, finalSize);
        
        assertFalse(server.getClients().contains(client1));
        
        assertTrue(server.getClients().contains(client2));
    }
    
    @Test
    public void testAddClient() throws IOException {
    	Socket mockSocket = mock(Socket.class);
    	
    	InputStream mockInputStream = mock(InputStream.class);
    	OutputStream mockOutputStream = mock(OutputStream.class);
    	
    	when(mockSocket.getInputStream()).thenReturn(mockInputStream);
    	when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
    	
    	int initialSize = server.getNumClients();
    	
    	ClientHandler client = new ClientHandler(mockSocket, server, 1);
    	server.addClient(client);
    	
    	int finalSize = server.getNumClients();
    	
    	assertTrue(server.getClients().contains(client));
    	
    	assertEquals(initialSize + 1, finalSize);
    }
    
    @Test
    public void testHandleClientDisconnection() throws IOException {
        Socket mockSocket = mock(Socket.class);
        
        InputStream mockInputStream = mock(InputStream.class);
        OutputStream mockOutputStream = mock(OutputStream.class);
        
        when(mockSocket.getInputStream()).thenReturn(mockInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);
        
        ClientHandler client = new ClientHandler(mockSocket, server, 1);
        server.addClient(client);

        client.stop();

        assertFalse(server.getClients().contains(client));
        assertEquals(0, server.getNumClients());
    }
    
    @Test
    public void testClientColorAssignment() {
        Socket mockSocket1 = mock(Socket.class);
        InputStream mockInputStream1 = mock(InputStream.class);
        OutputStream mockOutputStream1 = mock(OutputStream.class);
        Socket mockSocket2 = mock(Socket.class);
        InputStream mockInputStream2 = mock(InputStream.class);
        OutputStream mockOutputStream2 = mock(OutputStream.class);
        
        try {
            when(mockSocket1.getInputStream()).thenReturn(mockInputStream1);
            when(mockSocket1.getOutputStream()).thenReturn(mockOutputStream1);
            when(mockSocket2.getInputStream()).thenReturn(mockInputStream2);
            when(mockSocket2.getOutputStream()).thenReturn(mockOutputStream2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ClientHandler client1 = new ClientHandler(mockSocket1, server, 1);
        ClientHandler client2 = new ClientHandler(mockSocket2, server, 2);
        
        server.addClient(client1);
        server.addClient(client2);
        
        server.changeColor(client1, "r");
        assertEquals("r", client1.getColor());
        
        server.changeColor(client2, "b");
        assertEquals("b", client2.getColor());
        
        assertNotEquals(client1.getColor(), client2.getColor());
    }
    
    @Test
    public void testIsValidUserName() {
        // valid usernames
        assertTrue(server.isValidUserName("user1"));
        assertTrue(server.isValidUserName("user_123"));
        
        // invalid usernames
        assertFalse(server.isValidUserName("user name"));
        assertFalse(server.isValidUserName("user 123"));
        assertFalse(server.isValidUserName("123456789asdfghjklzxcvbnmasdfghjklasdfghjklasdfghjk"));
    }
}