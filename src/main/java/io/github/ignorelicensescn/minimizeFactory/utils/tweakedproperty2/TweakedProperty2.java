package io.github.ignorelicensescn.minimizeFactory.utils.tweakedproperty2;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.minimizeFactoryInstance;
import static org.bukkit.ChatColor.translateAlternateColorCodes;

/**
 * maybe I should consider slimefun-like yml way
 */
public class TweakedProperty2 extends Properties {

    public String getReplacedProperty(String key) {
        return translateAlternateColorCodes('&',getReplacedProperty(key, 0));
    }

    /**
     * replace ${XXXXXX} AND replace @{XXXXXX} from config
     **/
    public String getReplacedProperty(String key, int times) {
        String sval = getReplacedPropertyFromConfig(key);
        String regex = "\\$\\{[^}]+}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sval);
        if (times <= 30){
            while (matcher.find()) {
                sval = matcher.replaceFirst(
                        getReplacedProperty(matcher.group().substring(2, matcher.group().length() - 1)
                                , times + 1)
                );
                matcher = pattern.matcher(sval);
            }
        }
        return sval;
    }
    /**
     * replace @{XXXXXX} from config
     **/
    public String getReplacedPropertyFromConfig(String key, int times) {
        String sval = getProperty(key);
        String regex = "@\\{[^}]+}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sval);
        if (times <= 30){
            while (matcher.find()) {
                sval = matcher.replaceFirst(
                        String.valueOf(minimizeFactoryInstance.getConfig().get(matcher.group().substring(2, matcher.group().length() - 1),"NULL"))
                );
                matcher = pattern.matcher(sval);
            }
        }
        return sval;
    }
    public String getReplacedPropertyFromConfig(String key) {
        return getReplacedPropertyFromConfig(key,0);
    }

    /**
     * replace ${XXXXXX} and %{XXXXXX}.
     * Strings in %{XXXXXX} will be added to next line
     * (only the first works)
     **/
    public String getReplacedPropertiesAsString(String key, ChatColor color) {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> strList = getReplacedProperties0(key, 0, color);
        String lastString = strList.get(strList.size() -1);
        strList.remove(strList.size() -1);
        for (String str:strList){
            stringBuilder.append(translateAlternateColorCodes('&',str)).append("\n");
        }
        stringBuilder.append(translateAlternateColorCodes('&',lastString));

        strList.clear();
        return stringBuilder.toString();
    }
    public List<String> getReplacedProperties(String key, ChatColor color) {
        List<String> result = getReplacedProperties0(key, 0, color);
        result.replaceAll(textToTranslate -> translateAlternateColorCodes('&', textToTranslate));
        return result;
    }
    public List<String> getReplacedProperties0(String key, int times, ChatColor color) {
        List<String> svals = new ArrayList<>();

        String sval = getReplacedProperty(key);

        String regex = "%+\\{[^}]+}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sval);
        if (!matcher.find()){
            svals.add(translateAlternateColorCodes('&',sval));
            return svals;
        }
        sval = matcher.replaceFirst("");
        if (color != null){
            sval = color + sval;
        }
        svals.add(sval);
//        System.out.println(sval);
        svals.addAll(getReplacedProperties0(matcher.group().substring(2, matcher.group().length() - 1)
                , times + 1, color));
        return svals;
    }
}
