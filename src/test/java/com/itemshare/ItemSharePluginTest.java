package com.itemshare;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

@SuppressWarnings("unchecked")
public class ItemSharePluginTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(ItemSharePlugin.class);
        RuneLite.main(args);
    }
}