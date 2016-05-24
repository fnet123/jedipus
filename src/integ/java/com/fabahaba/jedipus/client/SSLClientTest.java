package com.fabahaba.jedipus.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.junit.Test;

import com.fabahaba.jedipus.cmds.Cmds;
import com.fabahaba.jedipus.primitive.RedisClientFactory;

public class SSLClientTest extends BaseRedisClientTest {

  @Test
  public void connectAndPing() {

    try (final RedisClient client = DEFAULT_SSL_CLIENT_FACTORY_BUILDER.create(DEFAULT_SSL_NODE)) {

      final String ssl = client.sendCmd(Cmds.PING, "SSL");
      assertEquals("SSL", ssl);
    }
  }

  @Test
  public void useNonDefaultSocketFactory() {

    final SSLSocketFactory sslSocketFactory = createTrustStoreSslSocketFactory();

    try (final RedisClient client = RedisClientFactory.startBuilding().withAuth(REDIS_PASS)
        .withSslSocketFactory(sslSocketFactory).withSsl(true).create(DEFAULT_SSL_NODE)) {

      final String ssl = client.sendCmd(Cmds.PING, "SSL");
      assertEquals("SSL", ssl);
    }
  }

  private static SSLSocketFactory createTrustStoreSslSocketFactory() {

    try {
      final KeyStore trustStore = KeyStore.getInstance("jceks");

      try (final InputStream inputStream = Files.newInputStream(JCEKS_TRUSTSTORE)) {
        trustStore.load(inputStream, null);
      } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
        throw new RuntimeException(e);
      }

      final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
      trustManagerFactory.init(trustStore);
      final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

      final SSLContext sslContext = SSLContext.getInstance("TLS");

      sslContext.init(null, trustManagers, new SecureRandom());
      return sslContext.getSocketFactory();
    } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}