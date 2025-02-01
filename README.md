*Slimefun extension*

**过年我估计会摸鱼一段时间,不在.有bug放issue/pr(I may absent during Chinese new year,just put problems in issue/pr if exists.)**

**现在汉化组粘液也能用了,[GuiZhanLibPlugin](https://github.com/ybw0014/GuizhanLibPlugin)不是必须的,但是可以提供对原版物品的中文翻译(在序列化机器时体现)**

**I didn't translate into english**,but you can try it yourself.See [/src/main/resources/language/](/src/main/resources/language).You may want to add en_US.properties or st.else.
**Remember to change the config when switching language!**

And I'm too lazy to test all the versions,you may have to test it yourself!
*(tested version:purpur-1.20.1-2062,Java 21,launch argument: (java.exe path) -jar purpur-1.20.1-2062.jar )*

**NOT** fully tested with:
* InfinityExpansion
* InfinityCompress
* LiteXpansion
* FNAmplifications
* DynaTech
* FluffyMachines

tested a bit with:
* Cultivation(GardenCloche)

It's an idea about 2 years ago(I mean 2023),when I consider all machines as some inputs-and-outputs,the calculation would be extremely fast.It reduces lagging!

Use in this way:Put all machines into one box(called MachineNetworkContainer) or several,make sure it can run forever by its inputs and outputs.Launch the boxes(via MachineNetworkCore).OFF THE SERVER AND GO TO BED.It will be calculated as running when getting outputs.

* For developers:Serializables can be calculated into this system provided by this plugin .If you want to add your own [Serializable](/src/main/java/io/github/ignorelicensescn/minimizefactory/items/serializable/SerializeOnly.java#L3),do implements [SerializedRecipeProvider](src/main/java/io/github/ignorelicensescn/minimizefactory/utils/recipesupport/SerializedRecipeProvider.java),examples like [AUTO_CACTUS_FOR_REGISTER](/src/main/java/io/github/ignorelicensescn/minimizefactory/items/Registers.java#L203) are given in source code.Maybe you also like things under [this file](/src/main/java/io/github/ignorelicensescn/minimizefactory/utils/compatibilities/InfinityExpansion/InfinityExpansionSerializedMachineRecipes.java)

![img-reactor-based-factory](/images/example-nuclear-reactor-and-ores.png)
![img example](/images/machine-serializer.png)
![img example](/images/machine-network-container.png)
![img example](/images/machine-network-storage.png)



* and [Kryo](https://github.com/EsotericSoftware/kryo) is really fast.
* needs sun.misc.Unsafe for compatibility,don't disable it.If you are using Java version >= 22,you need to find a way to enable it.(I didn't use JNI because it's also for removal.)