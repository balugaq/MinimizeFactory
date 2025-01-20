*Slimefun extension*

**汉化组的粘液我没做兼容,用的是CookieSlime的api,汉化版暂时用不了**

**I didn't translate into english**,but you can try it yourself.See [/src/main/resources/language/](/src/main/resources/language).You may want to add en_US.properties or st.else.
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

It's an idea about 2 years ago(I mean 2023),when I consider all machines as some inputs-and-outputs,the calculation would be extremely fast.It reduces lagging!

Use in this way:Put all machines into one box(called MachineNetworkContainer) or several,make sure it can run forever by its inputs and outputs.Launch the boxes(via MachineNetworkCore).OFF THE SERVER AND GO TO BED.It will be calculated as running when getting outputs.

* For developers:Serializables can be calculated into this system provided by this plugin .If you want to add your own [Serializable](/src/main/java/io/github/ignorelicensescn/minimizeFactory/Items/serializable/SerializeOnly.java#L3),do implements [SerializedRecipeProvider](src/main/java/io/github/ignorelicensescn/minimizeFactory/utils/recipesupport/SerializedRecipeProvider.java),examples like [AUTO_CACTUS_FOR_REGISTER](/src/main/java/io/github/ignorelicensescn/minimizeFactory/Items/Registers.java#L203) are given in source code.Maybe you also like things under [this file](/src/main/java/io/github/ignorelicensescn/minimizeFactory/utils/compatibilities/InfinityExpansion/InfinityExpansionSerializedMachineRecipes.java)

![img-reactor-based-factory](/images/example-nuclear-reactor-and-ores.png)
![img example](/images/machine-serializer.png)
![img example](/images/machine-network-container.png)
![img example](/images/machine-network-storage.png)