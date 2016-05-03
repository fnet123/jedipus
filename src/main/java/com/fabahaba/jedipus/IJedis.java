package com.fabahaba.jedipus;

import redis.clients.jedis.AdvancedBinaryJedisCommands;
import redis.clients.jedis.AdvancedJedisCommands;
import redis.clients.jedis.BasicCommands;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.BinaryScriptingCommands;
import redis.clients.jedis.ClusterCommands;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.MultiKeyBinaryCommands;
import redis.clients.jedis.MultiKeyCommands;
import redis.clients.jedis.ScriptingCommands;

public interface IJedis extends JedisClient, JedisCommands, MultiKeyCommands, AdvancedJedisCommands,
    ScriptingCommands, BasicCommands, ClusterCommands, BinaryJedisCommands, MultiKeyBinaryCommands,
    AdvancedBinaryJedisCommands, BinaryScriptingCommands {

  public String clientSetname(final String name);

  public String clientSetname(final byte[] name);

  public JedisPipeline createPipeline();

  public String asking();

  @Override
  public void close();
}