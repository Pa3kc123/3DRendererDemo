package sk.pa3kc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parameters {
    private final String[] parameters;
    private final String[] optionNames;
    private final String[] optionValues;

    public Parameters(String[] args) {
        final List<String> parameterList = new ArrayList<String>();
        final List<String> optionNameList = new ArrayList<String>();
        final List<String> optionValueList = new ArrayList<String>();

        final Pattern fullNamePattern = Pattern.compile("--(.*?)=(.*)");
        final Pattern shortNamePattern = Pattern.compile("-(.*?)=(.*)");

        Matcher matcher;
        for (String arg : args) {
            if (arg.startsWith("--")) {
                matcher = fullNamePattern.matcher(arg);
            } else if (arg.startsWith("-")) {
                matcher = shortNamePattern.matcher(arg);
            } else {
                parameterList.add(arg);
                continue;
            }

            if (matcher.find() && matcher.groupCount() == 2) {
                final String name = matcher.group(1);
                final String value = matcher.group(2);

                if (!optionNameList.contains(name)) {
                    optionNameList.add(name);
                    optionValueList.add(value);
                }
            }
        }

        final String[] type = new String[0];
        this.parameters = parameterList.toArray(type);
        this.optionNames = optionNameList.toArray(type);
        this.optionValues = optionValueList.toArray(type);
    }

    public String[] getParams() {
        return this.parameters;
    }
    public String[] getOptionNames() {
        return this.optionNames;
    }

    public String getOption(String name) {
        for (int i = 0; i < this.optionNames.length; i++) {
            if (this.optionNames[i].equals(name)) {
                return this.optionValues[i];
            }
        }
        return null;
    }
}
