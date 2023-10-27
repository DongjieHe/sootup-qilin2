package pta.selectiveobjectsensitivity;

abstract interface ANTLRGrammarParseBehavior {
    public abstract void setFileOption();

    public abstract void beginExceptionSpec();
}

public class Antlr {
    public static void main(String[] args) {
        Tool.main(args);
    }
}

class Tool {
    public static void main(String[] args) {
        Tool theTool = new Tool();
        theTool.doEverything(args);
    }

    private void doEverything(String[] args) {
        MakeGrammar behavior = new MakeGrammar();
        ANTLRParser p = new ANTLRParser(behavior);
        p.grammar();
    }
}

class MakeGrammar extends DefineGrammarSymbols {
    public void beginExceptionSpec() {
        StringUtils.stripFront(StringUtils.stripBack("", " \n\r\t"), " \n\r\t");
    }
}

class ANTLRParser {

    private ANTLRGrammarParseBehavior behavior;

    public ANTLRParser(ANTLRGrammarParseBehavior behavior) {
        this.behavior = behavior;
    }

    public void grammar() {
        this.behavior.setFileOption();
        this.behavior.beginExceptionSpec();
    }
}

class DefineGrammarSymbols implements ANTLRGrammarParseBehavior {

    @Override
    public void setFileOption() {
        StringUtils.stripBack(StringUtils.stripFront("", '"'), '"');
    }

    @Override
    public void beginExceptionSpec() {
    }

}

class StringUtils {
    public static String stripBack(String s, char c) {
        while ((s.length() > 0) && (s.charAt(s.length() - 1) == c)) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static String stripBack(String s, String remove) {
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < remove.length(); i++) {
                char c = remove.charAt(i);
                while ((s.length() > 0) && (s.charAt(s.length() - 1) == c)) {
                    changed = true;
                    s = s.substring(0, s.length() - 1);
                }
            }
        } while (changed);
        return s;
    }

    public static String stripFront(String s, char c) {
        while ((s.length() > 0) && (s.charAt(0) == c)) {
            s = s.substring(1);
        }
        return s;
    }

    public static String stripFront(String s, String remove) {
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < remove.length(); i++) {
                char c = remove.charAt(i);
                while ((s.length() > 0) && (s.charAt(0) == c)) {
                    changed = true;
                    s = s.substring(1);
                }
            }
        } while (changed);
        return s;
    }
}