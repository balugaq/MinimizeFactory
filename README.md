*Slimefun extenssion*

**I didn't translate into english**,but you can try it yourself.See /src/main/resources/language/.You may want to add en_US.properties or st.else.
**Remember to change the config when switching language!**

And I'm too lazy to test all the versions,you may have to test it yourself!
*(tested version:purpur-1.20.1-2062)*

**NOT** tested with:
* InfinityExpansion,
* InfinityCompress,
* LiteXpansion,
* FNAmplifications,
* DynaTech,
* FluffyMachines

It's an idea about 2 years ago,when I consider all machines as some inputs-and-outputs,the calculation would be extremely fast.It reduces lagging!

Use in this way:Put all machines into one box(called MachineNetworkContainer) or several,make sure it can run forever by its inputs and outputs.Launch the boxes(via MachineNetworkCore).OFF THE SERVER AND GO TO BED.It will be calculated as running when getting outputs.

* For developers:If you want to add your own [Serializable](/src/main/java/io/github/ignorelicensescn/minimizeFactory/Items/serializable/SerializeOnly.java#L3),do implements SerializedRecipeProvider,examples like [AUTO_CACTUS_FOR_REGISTER](/src/main/java/io/github/ignorelicensescn/minimizeFactory/Items/Registers.java#L203) are given in source code