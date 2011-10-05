package com.filipovskii.jwget.http;

import com.filipovskii.jwget.common.*;
import com.filipovskii.jwget.exception.ConnectionFailed;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * @author filipovskii_off
 */
public class DownloadControllerTest {

  final String url = "http://www.java.com";
  final String path = "~/Downloads/java.html";

  private final Map<String, String> properties = new HashMap<String, String>();
  private IProtocol protocol;
  private IConnection connection;
  private IDownloadController controller;

  private InputStream in;
  private OutputStream out;
  private IDownloadRequest req;
  private IDownloadResponse resp;

  @Before
  public void setUp() {
    properties.put("url", url);
    properties.put("path", path);

    protocol = createNiceMock(IProtocol.class);
    connection = createMock(IConnection.class);

    in = createMock(InputStream.class);
    out = createMock(OutputStream.class);

    req = createMock(IDownloadRequest.class);
    resp = createMock(IDownloadResponse.class);
    expect(req.getOutputStream()).andReturn(out);
    expect(resp.getInputStream()).andReturn(in);

    controller = new DownloadController(protocol);

    expect(protocol.createConnection()).andReturn(connection);
    expect(protocol.createRequest()).andReturn(req);
    expect(protocol.createResponse()).andReturn(resp);
  }

  @Test
  public void testControllerGetsProtocolData() throws Exception {
    // expectations are in setUp

    replay(protocol, req, resp);

    controller.call();

    verify(protocol);
  }

  @Test
  public void testConnectionOpensAndCloses() throws Exception {
    connection.open();
    connection.send(
        anyObject(IDownloadRequest.class), anyObject(IDownloadResponse.class));
    connection.close();
    replay(connection, protocol, req, resp);

    controller.call();

    verify(connection);
  }

  @Test
  public void testConnectionClosesIfExceptionIsThrown() throws Exception {
    connection.open();
    expectLastCall().andThrow(new ConnectionFailed("bla bla"));
    connection.close();
    replay(connection, protocol, req, resp);

    controller.call();

    verify(connection);
  }

  @Test
  public void testControllerWritesDataToOutputStream() throws Exception {
    expect(in.read((byte[]) anyObject())).andReturn(1).times(2);
    expect(in.read((byte[]) anyObject())).andReturn(-1);

    out.write((byte[]) anyObject());
    expectLastCall().times(2);
    in.close();
    out.close();
    replay(protocol, req, resp, in, out);

    // act
    controller.call();

    verify(in, out);
  }

}