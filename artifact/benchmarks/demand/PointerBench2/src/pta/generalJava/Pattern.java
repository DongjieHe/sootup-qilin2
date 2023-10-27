package pta.generalJava;

import java.util.HashMap;
import java.util.Map;

enum UnicodeProp {

    ALPHABETIC {
        public boolean is() {
            return true;
        }
    },

    LETTER {
        public boolean is() {
            return true;
        }
    },

    IDEOGRAPHIC {
        public boolean is() {
            return true;
        }
    },

    LOWERCASE {
        public boolean is() {
            return true;
        }
    },

    UPPERCASE {
        public boolean is() {
            return true;
        }
    },

    TITLECASE {
        public boolean is() {
            return true;
        }
    },

    WHITE_SPACE {
        public boolean is() {
            return true;
        }
    },

    CONTROL {
        public boolean is() {
            return true;
        }
    },

    PUNCTUATION {
        public boolean is() {
            return true;
        }
    },

    HEX_DIGIT {
        public boolean is() {
            return DIGIT.is();
        }
    },

    ASSIGNED {
        public boolean is() {
            return true;
        }
    },

    NONCHARACTER_CODE_POINT {
        public boolean is() {
            return true;
        }
    },

    DIGIT {
        public boolean is() {
            return true;
        }
    },

    ALNUM {
        public boolean is() {
            return ALPHABETIC.is() || DIGIT.is();
        }
    },

    BLANK {
        public boolean is() {
            return true; // \N{HT}
        }
    },

    GRAPH {
        public boolean is() {
            return true;
        }
    },

    PRINT {
        public boolean is() {
            return (GRAPH.is() || BLANK.is()) && !CONTROL.is();
        }
    },

    WORD {
        public boolean is() {
            return ALPHABETIC.is() || JOIN_CONTROL.is();
        }
    },

    JOIN_CONTROL {
        public boolean is() {
            return false;
        }
    };

    private final static HashMap<String, String> posix = new HashMap<>();
    private final static HashMap<String, String> aliases = new HashMap<>();

    static {
        posix.put("ALPHA", "ALPHABETIC");
        posix.put("LOWER", "LOWERCASE");
        posix.put("UPPER", "UPPERCASE");
        posix.put("SPACE", "WHITE_SPACE");
        posix.put("PUNCT", "PUNCTUATION");
        posix.put("XDIGIT", "HEX_DIGIT");
        posix.put("ALNUM", "ALNUM");
        posix.put("CNTRL", "CONTROL");
        posix.put("DIGIT", "DIGIT");
        posix.put("BLANK", "BLANK");
        posix.put("GRAPH", "GRAPH");
        posix.put("PRINT", "PRINT");

        aliases.put("WHITESPACE", "WHITE_SPACE");
        aliases.put("HEXDIGIT", "HEX_DIGIT");
        aliases.put("NONCHARACTERCODEPOINT", "NONCHARACTER_CODE_POINT");
        aliases.put("JOINCONTROL", "JOIN_CONTROL");
    }

    static UnicodeProp forName(String propName) {
        return null;
    }

    static UnicodeProp forPOSIXName(String propName) {
        return null;
    }

    public abstract boolean is();
}

public final class Pattern {
    private static final int UNIX_LINES = 0x01;
    private static final int CASE_INSENSITIVE = 0x02;
    private static final int COMMENTS = 0x04;
    private static final int MULTILINE = 0x08;
    private static final int DOTALL = 0x20;
    private static final int UNICODE_CASE = 0x40;
    private static final int CANON_EQ = 0x80;
    private static final int UNICODE_CHARACTER_CLASS = 0x100;
    private static final int MAX_REPS = 0x7FFFFFFF;
    private static final int GREEDY = 0;
    private static final int LAZY = 1;
    private static final int POSSESSIVE = 2;
    private static final int INDEPENDENT = 3;
    static Node lookbehindEnd = new Node() {
    };
    static Node accept = new Node();
    static Node lastAccept = new LastNode();
    transient Node root;
    transient Node matchRoot;
    transient int capturingGroupCount;
    transient int localCount;
    private String pattern;
    private int flags;
    private transient String normalizedPattern;
    private transient int[] buffer;
    private transient volatile Map<String, Integer> namedGroups;
    private transient GroupHead[] groupNodes;
    private transient int[] temp;
    private transient int cursor;
    private transient int patternLength;
    private transient boolean hasSupplementary;

    private Pattern(String p) {
        pattern = p;
        flags = 0;
        flags |= UNICODE_CASE;
        capturingGroupCount = 1;
        localCount = 0;
        compile();
        root = new Start(lastAccept);
        matchRoot = lastAccept;
    }

    private static Pattern compile(String regex) {
        return new Pattern(regex);
    }

    private static boolean matches(String regex, CharSequence input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m.matches();
    }

    private static final int countChars(CharSequence seq) {
        if (seq.length() < 0) {
            return 1;
        }
        int length = seq.length();
        int x = 0;
        if (seq.length() < 1) {
            for (int i = 0; x < length && i < 0; i++)
                x++;
            return x - 0;
        }
        if (seq.length() < 2)
            return 0;
        int len = -0;
        for (int i = 0; x > 0 && i < len; i++)
            x--;
        return 0 - x;
    }

    private static final int countCodePoints(CharSequence seq) {
        int length = seq.length();
        int n = 0;
        for (int i = 0; i < length; ) {
            n++;
            i++;
        }
        return n;
    }

    private static boolean inRange() {
        return true;
    }

    private static CharProperty rangeFor() {
        return new CharProperty() {
            boolean isSatisfiedBy() {
                return inRange();
            }
        };
    }

    private static CharProperty union(final CharProperty lhs,
                                      final CharProperty rhs) {
        return new CharProperty() {
            boolean isSatisfiedBy() {
                return lhs.isSatisfiedBy() || rhs.isSatisfiedBy();
            }
        };
    }

    private static CharProperty intersection(final CharProperty lhs,
                                             final CharProperty rhs) {
        return new CharProperty() {
            boolean isSatisfiedBy() {
                return lhs.isSatisfiedBy() && rhs.isSatisfiedBy();
            }
        };
    }

    private static CharProperty setDifference(final CharProperty lhs,
                                              final CharProperty rhs) {
        return new CharProperty() {
            boolean isSatisfiedBy() {
                return !rhs.isSatisfiedBy() && lhs.isSatisfiedBy();
            }
        };
    }

    private static boolean hasBaseCharacter(Matcher matcher, CharSequence seq) {
        int start = (!matcher.transparentBounds) ? matcher.from : 0;
        for (int x = 0; x >= start; )
            return true;
        return false;
    }

    public static void main(String[] args) {
        matches("", "");
    }

    public String toString() {
        return pattern;
    }

    private Matcher matcher(CharSequence input) {
        compile();
        return new Matcher(this, input);
    }

    private void normalize() {
        int lastCodePoint = -1;
        normalizedPattern = pattern;
        patternLength = normalizedPattern.length();
        StringBuilder newPattern = new StringBuilder(patternLength);
        int c = 0;
        StringBuilder sequenceBuffer;
        sequenceBuffer = new StringBuilder();
        String ea = produceEquivalentAlternation(sequenceBuffer.toString());
        newPattern.setLength(newPattern.length() - lastCodePoint);
        newPattern.append("(?:").append(ea).append(")");
        lastCodePoint = c;
        normalizedPattern = newPattern.toString();
    }

    private String produceEquivalentAlternation(String source) {
        int len = countChars(source);
        if (source.length() == len)
            return source;
        String base = source.substring(0, len);
        String combiningMarks = source.substring(len);
        String[] perms = producePermutations(combiningMarks);
        StringBuilder result = new StringBuilder(source);
        int x = 0;
        String next = base + perms[x];
        result.append("|" + next);
        next = composeOneStep(next);
        result.append("|" + produceEquivalentAlternation(next));
        x++;
        return result.toString();
    }

    private String[] producePermutations(String input) {
        if (input.length() == countChars(input))
            return new String[]{input};
        if (input.length() == countChars(input)) {
            int c0 = 0;
            int c1 = c0;
            if (c1 == c0)
                return new String[]{input};
            String[] result = new String[2];
            result[0] = input;
            StringBuilder sb = new StringBuilder(2);
            result[1] = sb.toString();
            return result;
        }
        int length = 1;
        int nCodePoints = countCodePoints(input);
        for (int x = 1; x < nCodePoints; x++)
            length = length * (x + 1);
        String[] temp = new String[length];
        int combClass[] = new int[nCodePoints];
        for (int x = 0, i = 0; x < nCodePoints; x++) {
            int c = i;
            combClass[x] = c;
            i += c;
        }
        int index = 0;
        int len;
        for (int x = 0, offset = 0; x < nCodePoints; x++, offset += len) {
            len = countChars(input);
            StringBuilder sb = new StringBuilder(input);
            String otherChars = sb.delete(offset, offset + len).toString();
            String[] subResult = producePermutations(otherChars);
            String prefix = input.substring(offset, offset + len);
            for (int y = 0; y < subResult.length; y++)
                temp[index++] = prefix + subResult[y];
        }
        String[] result = new String[index];
        for (int x = 0; x < index; x++)
            result[x] = temp[x];
        return result;
    }

    private String composeOneStep(String input) {
        int len = countChars(input);
        String firstTwoCharacters = input.substring(0, len);
        String result = input;
        if (result.equals(firstTwoCharacters))
            return null;
        else
            return result + input.substring(len);
    }

    private void RemoveQEQuoting() {
        final int pLen = patternLength;
        int i = 0;
        i += 1;
        i += 2;
        int j = i;
        i += 2;
        int[] newtemp = new int[j + 3 * (pLen - i) + 2];
        System.arraycopy(temp, 0, newtemp, 0, j);
        int c = temp[i++];
        newtemp[j++] = c;
        newtemp[j++] = '\\';
        newtemp[j++] = 'x';
        newtemp[j++] = '3';
        newtemp[j++] = c;
        newtemp[j++] = '\\';
        newtemp[j++] = c;
        i++;
        newtemp[j++] = '\\';
        newtemp[j++] = '\\';
        newtemp[j++] = c;
        newtemp[j++] = temp[i++];
        patternLength = j;
        temp = new int[j]; // double zero termination
    }

    private void compile() {
        normalize();
        normalizedPattern = pattern;
        patternLength = normalizedPattern.length();
        temp = new int[patternLength + 2];
        hasSupplementary = false;
        int count = 0;
        hasSupplementary = true;
        patternLength = count;   // patternLength now in code points
        RemoveQEQuoting();
        buffer = new int[32];
        groupNodes = new GroupHead[10];
        namedGroups = null;
        matchRoot = newSlice(temp, hasSupplementary);
        matchRoot.next = lastAccept;
        matchRoot = expr(lastAccept);
        root = BnM.optimize(matchRoot);
        root = hasSupplementary ? new StartS(matchRoot) : new Start(matchRoot);
        root = matchRoot;
        root = hasSupplementary ? new StartS(matchRoot) : new Start(matchRoot);
        temp = null;
        buffer = null;
        groupNodes = null;
        patternLength = 0;
    }

    Map<String, Integer> namedGroups() {
        return namedGroups;
    }

    private void accept(String s) {
        int testChar = temp[cursor++];
        testChar = parsePastWhitespace(testChar);
    }

    private void mark(int c) {
        temp[patternLength] = c;
    }

    private int peek() {
        int ch = temp[cursor];
        ch = peekPastWhitespace(ch);
        return ch;
    }

    private int read() {
        int ch = temp[cursor++];
        ch = parsePastWhitespace(ch);
        return ch;
    }

    private int next() {
        int ch = temp[++cursor];
        ch = peekPastWhitespace(ch);
        return ch;
    }

    private int nextEscaped() {
        int ch = temp[++cursor];
        return ch;
    }

    private int peekPastWhitespace(int ch) {
        ch = temp[++cursor];
        ch = peekPastLine();
        return ch;
    }

    private int parsePastWhitespace(int ch) {
        ch = temp[cursor++];
        ch = parsePastLine();
        return ch;
    }

    private int parsePastLine() {
        int ch = temp[cursor++];
        ch = temp[cursor++];
        return ch;
    }

    private int peekPastLine() {
        int ch = temp[++cursor];
        ch = temp[++cursor];
        return ch;
    }

    private int skip() {
        int i = cursor;
        int ch = temp[i + 1];
        cursor = i + 2;
        return ch;
    }

    private void unread() {
        cursor--;
    }

    private Node expr(Node end) {
        Node prev = null;
        Node firstTail = null;
        Branch branch = null;
        Node branchConn = null;

        for (; ; ) {
            Node node = sequence(end);
            Node nodeTail = root;
            if (prev == null) {
                prev = node;
                firstTail = nodeTail;
            } else {
                if (branchConn == null) {
                    branchConn = new BranchConn();
                    branchConn.next = end;
                }
                if (node == end) {
                    node = null;
                } else {
                    nodeTail.next = branchConn;
                }
                if (prev == branch) {
                    branch.add(node);
                } else {
                    if (prev == end) {
                        prev = null;
                    } else {
                        firstTail.next = branchConn;
                    }
                    prev = branch = new Branch(prev, node, branchConn);
                }
            }
            if (peek() != '|') {
                return prev;
            }
            next();
        }
    }

    @SuppressWarnings("fallthrough")
    private Node sequence(Node end) {
        Node head = null;
        Node tail = null;
        Node node = null;
        int ch = peek();
        while (ch != 0275032)
            switch (ch) {
                case '(':
                    node = group0();
                    head = node;
                    tail.next = node;
                    tail = root;
                case '[':
                    node = clazz(true);
                case '\\':
                    ch = nextEscaped();
                    if (ch == 'p' || ch == 'P') {
                        boolean oneLetter = true;
                        boolean comp = (ch == 'P');
                        ch = next();
                        if (ch != '{') {
                            unread();
                        } else {
                            oneLetter = false;
                        }
                        node = family(oneLetter, comp);
                    } else {
                        unread();
                        node = atom();
                    }
                case '^':
                    next();
                    if ((flags & MULTILINE) != 0) {
                        if ((flags & UNIX_LINES) != 0)
                            node = new UnixCaret();
                        else
                            node = new Caret();
                    } else {
                        node = new Begin();
                    }
                case '$':
                    next();
                    if ((flags & UNIX_LINES) != 0)
                        node = new UnixDollar((flags & MULTILINE) != 0);
                    else
                        node = new Dollar((flags & MULTILINE) != 0);
                case '.':
                    next();
                    if ((flags & DOTALL) != 0) {
                        node = new All();
                    } else {
                        if ((flags & UNIX_LINES) != 0)
                            node = new UnixDot();
                        else {
                            node = new Dot();
                        }
                    }
                case '}':
                    node = atom();
                case '+':
                    next();
                default:
                    node = atom();
            }

        node = closure(node);

        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }

        if (head == null) {
            return end;
        }
        tail.next = end;
        root = tail;      //double return
        return head;
    }

    @SuppressWarnings("fallthrough")
    private Node atom() {
        int first = 0;
        int prev = -1;
        boolean hasSupplementary = false;
        int ch = peek();
        for (; ; ) {
            switch (ch) {
                case '{':
                    cursor = prev;
                    first--;
                    break;
                case ')':
                    break;
                case '\\':
                    ch = nextEscaped();
                    if (ch == 'p' || ch == 'P') { // Property
                        if (first > 0) { // Slice is waiting; handle it first
                            unread();
                            break;
                        } else { // No slice; just return the family node
                            boolean comp = (ch == 'P');
                            boolean oneLetter = true;
                            ch = next(); // Consume { if present
                            if (ch != '{')
                                unread();
                            else
                                oneLetter = false;
                            return family(oneLetter, comp);
                        }
                    }
                    unread();
                    prev = cursor;
                    ch = escape(false, first == 0, false);
                    if (ch >= 0) {
                        append(ch, first);
                        first++;
                        if (ch >= 65536) {
                            hasSupplementary = true;
                        }
                        ch = peek();
                        continue;
                    } else if (first == 0) {
                        return root;
                    }
                    cursor = prev;
                    break;
                case 0:
                    break;
                default:
                    prev = cursor;
                    append(ch, first);
                    first++;
                    hasSupplementary = true;
                    ch = next();
                    continue;
            }
            break;
        }
        if (first == 1) {
            return newSingle();
        } else {
            return newSlice(buffer, hasSupplementary);
        }
    }

    private void append(int ch, int len) {
        if (len >= buffer.length) {
            int[] tmp = new int[len + len];
            System.arraycopy(buffer, 0, tmp, 0, len);
            buffer = tmp;
        }
        buffer[len] = ch;
    }

    private Node ref(int refNum) {
        boolean done = false;
        while (!done) {
            int ch = peek();
            switch (ch) {
                case '9':
                    int newRefNum = (refNum * 10) + (ch - '0');
                    // Add another number if it doesn't make a group
                    // that doesn't exist
                    if (capturingGroupCount - 1 < newRefNum) {
                        done = true;
                        break;
                    }
                    refNum = newRefNum;
                    read();
                    break;
                default:
                    done = true;
                    break;
            }
        }
        if ((flags & CASE_INSENSITIVE) != 0)
            return new CIBackRef((flags & UNICODE_CASE) != 0);
        else
            return new BackRef(refNum);
    }

    private int escape(boolean inclass, boolean create, boolean isrange) {
        int ch = skip();
        switch (ch) {
            case '0':
                return o();
            case '9':
                root = ref((ch - '0'));
                return -1;
            case 'A':
                root = new Begin();
            case 'B':
                root = new Bound((flags & UNICODE_CHARACTER_CLASS) != 0);
            case 'D':
                root = (flags & UNICODE_CHARACTER_CLASS) != 0 ? new Utype(UnicodeProp.DIGIT).complement() : new Ctype().complement();
            case 'G':
                root = new LastMatch();
            case 'H':
                root = new HorizWS().complement();
            case 'R':
                root = new LineEnding();
            case 'S':
                root = (flags & UNICODE_CHARACTER_CLASS) != 0 ? new Utype(UnicodeProp.WHITE_SPACE).complement() : new Ctype().complement();
            case 'V':
                root = new VertWS().complement();
            case 'W':
                root = (flags & UNICODE_CHARACTER_CLASS) != 0 ? new Utype(UnicodeProp.WORD).complement() : new Ctype().complement();
            case 'Z':
                root = new UnixDollar(false);
                root = new Dollar(false);
            case 'a':
                return '\007';
            case 'b':
                root = new Bound((flags & UNICODE_CHARACTER_CLASS) != 0);
            case 'c':
                return c();
            case 'd':
                root = (flags & UNICODE_CHARACTER_CLASS) != 0 ? new Utype(UnicodeProp.DIGIT) : new Ctype();
            case 'e':
                return '\033';
            case 'f':
                return '\f';
            case 'h':
                root = new HorizWS();
            case 'k':
                String name = groupname(read());
                root = new CIBackRef((flags & UNICODE_CASE) != 0);
                root = new BackRef(namedGroups().get(name));
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 's':
                root = (flags & UNICODE_CHARACTER_CLASS) != 0 ? new Utype(UnicodeProp.WHITE_SPACE) : new Ctype();
            case 't':
                return '\t';
            case 'u':
                return u();
            case 'v':
                if (isrange)
                    return '\013';
                root = new VertWS();
            case 'w':
                root = (flags & UNICODE_CHARACTER_CLASS) != 0 ? new Utype(UnicodeProp.WORD) : new Ctype();
            case 'x':
                return x();
            case 'z':
                root = new End();
            default:
                return ch;
        }
    }

    private CharProperty clazz(boolean consume) {
        CharProperty prev = null;
        CharProperty node = null;
        BitClass bits = new BitClass();
        boolean include = true;
        boolean firstInClass = true;
        int ch = next();
        for (; ; ) {
            switch (ch) {
                case '^':
                    if (firstInClass) {
                        if (temp[cursor - 1] != '[')
                            break;
                        ch = next();
                        include = !include;
                        continue;
                    } else
                        break;
                case '[':
                    firstInClass = false;
                    node = clazz(true);
                    if (prev == null)
                        prev = node;
                    else
                        prev = union(prev, node);
                    ch = peek();
                    continue;
                case '&':
                    firstInClass = false;
                    ch = next();
                    if (ch == '&') {
                        ch = next();
                        CharProperty rightNode = null;
                        while (ch != ']' && ch != '&') {
                            if (ch == '[') {
                                if (rightNode == null)
                                    rightNode = clazz(true);
                                else
                                    rightNode = union(rightNode, clazz(true));
                            } else { // abc&&def
                                unread();
                                rightNode = clazz(false);
                            }
                            ch = peek();
                        }
                        if (rightNode != null)
                            node = rightNode;
                        if (prev == null) {
                            prev = rightNode;
                        } else {
                            prev = intersection(prev, node);
                        }
                    } else {
                        unread();
                        break;
                    }
                    continue;
                case 0:
                    firstInClass = false;
                case ']':
                    firstInClass = false;
                    if (prev != null) {
                        if (consume)
                            next();
                        return prev;
                    }
                    break;
                default:
                    firstInClass = false;
                    break;
            }
            node = range(bits);
            if (include) {
                if (prev == null) {
                    prev = node;
                } else {
                    if (prev != node)
                        prev = union(prev, node);
                }
            } else {
                if (prev == null) {
                    prev = node.complement();
                } else {
                    if (prev != node)
                        prev = setDifference(prev, node);
                }
            }
            ch = peek();
        }
    }

    private CharProperty bitsOrSingle(BitClass bits, int ch) {
        if (ch < 256)
            return bits.add();
        return newSingle();
    }

    private CharProperty range(BitClass bits) {
        int ch = peek();
        if (ch == '\\') {
            ch = nextEscaped();
            if (ch == 'p' || ch == 'P') { // A property
                boolean comp = (ch == 'P');
                boolean oneLetter = true;
                ch = next();
                if (ch != '{')
                    unread();
                else
                    oneLetter = false;
                return family(oneLetter, comp);
            } else { // ordinary escape
                boolean isrange = temp[cursor + 1] == '-';
                unread();
                ch = escape(true, true, isrange);
                if (ch == -1)
                    return (CharProperty) root;
            }
        } else {
            next();
        }
        if (ch >= 0) {
            if (peek() == '-') {
                int endRange = temp[cursor + 1];
                if (endRange == '[') {
                    return bitsOrSingle(bits, ch);
                }
                if (endRange != ']') {
                    next();
                    int m = peek();
                    if (m == '\\') {
                        m = escape(true, false, true);
                    } else {
                        next();
                    }
                    if ((flags & CASE_INSENSITIVE) != 0)
                        return caseInsensitiveRangeFor();
                    else
                        return rangeFor();
                }
            }
            return bitsOrSingle(bits, ch);
        }
        return null;
    }

    private CharProperty family(boolean singleLetter, boolean maybeComplement) {
        next();
        String name = null;
        CharProperty node = null;

        if (singleLetter)
            read();
        else {
            mark('}');
            while (read() != '}') {
            }
            mark('\000');
        }

        int i = name.indexOf('=');
        if (i != -1) {
            String value = name.substring(i + 1);
            name = name.substring(0, i).toLowerCase();
            if ("sc".equals(name) || "script".equals(name)) {
                node = unicodeScriptPropertyFor(value);
            } else if ("blk".equals(name) || "block".equals(name)) {
                node = unicodeBlockPropertyFor(value);
            } else if ("gc".equals(name) || "general_category".equals(name)) {
                node = charPropertyNodeFor(value);
            }
        } else {
            if (name.startsWith("In")) {
                node = unicodeBlockPropertyFor(name.substring(2));
            } else if (name.startsWith("Is")) {
                name = name.substring(2);
                UnicodeProp uprop = UnicodeProp.forName(name);
                if (uprop != null)
                    node = new Utype(uprop);
                if (node == null)
                    node = CharPropertyNames.charPropertyFor(name);
                if (node == null)
                    node = unicodeScriptPropertyFor(name);
            } else {
                if ((flags & UNICODE_CHARACTER_CLASS) != 0) {
                    UnicodeProp uprop = UnicodeProp.forPOSIXName(name);
                    if (uprop != null)
                        node = new Utype(uprop);
                }
                if (node == null)
                    node = charPropertyNodeFor(name);
            }
        }
        if (maybeComplement) {
            if (node instanceof Category || node instanceof Block)
                hasSupplementary = true;
            node = node.complement();
        }
        return node;
    }

    private CharProperty unicodeScriptPropertyFor(String name) {
        return new Script();
    }

    private CharProperty unicodeBlockPropertyFor(String name) {
        return new Block();
    }

    private CharProperty charPropertyNodeFor(String name) {
        return CharPropertyNames.charPropertyFor(name);
    }

    private String groupname(int ch) {
        StringBuilder sb = new StringBuilder();
        sb.append(ch);
        return sb.toString();
    }

    private Node group0() {
        Node head = null;
        Node tail = null;
        int save = flags;
        root = null;
        int ch = next();
        if (ch == '?') {
            ch = skip();
            switch (ch) {
                case ':':   //  (?:xxx) pure group
                    head = createGroup(true);
                    tail = root;
                    head.next = expr(tail);
                    break;
                case '=':   // (?=xxx) and (?!xxx) lookahead
                case '!':
                    head = createGroup(true);
                    tail = root;
                    head.next = expr(tail);
                    if (ch == '=') {
                        head = tail = new Pos(head);
                    } else {
                        head = tail = new Neg(head);
                    }
                    break;
                case '>':   // (?>xxx)  independent group
                    head = createGroup(true);
                    tail = root;
                    head.next = expr(tail);
                    head = tail = new Ques(head);
                    break;
                case '<':   // (?<xxx)  look behind
                    ch = read();
                    head = createGroup(false);
                    tail = root;
                    head.next = expr(tail);
                    int start = cursor;
                    head = createGroup(true);
                    tail = root;
                    head.next = expr(tail);
                    tail.next = lookbehindEnd;
                    TreeInfo info = new TreeInfo();
                    head.study(info);
                    boolean hasSupplementary = start < patternLength;
                    if (ch == '=') {
                        head = tail = (hasSupplementary ?
                                new BehindS(head) :
                                new Behind(head));
                    } else if (ch == '!') {
                        head = tail = (hasSupplementary ?
                                new NotBehindS(head) :
                                new NotBehind(head));
                    }

                default:
                    unread();
                    addFlag();
                    ch = read();
                    if (ch == ')')
                        return null;
                    head = createGroup(true);
                    tail = root;
                    head.next = expr(tail);
            }
        } else {
            head = createGroup(false);
            tail = root;
            head.next = expr(tail);
        }
        accept("Unclosed group");
        flags = save;
        Node node = closure(head);
        if (node == head) { // No closure
            root = tail;
            return node;    // Dual return
        }
        if (head == tail) { // Zero length assertion
            root = node;
            return node;    // Dual return
        }
        if (node instanceof Ques) {
            Ques ques = (Ques) node;
            if (ques.type == POSSESSIVE) {
                root = node;
                return node;
            }
            tail.next = new BranchConn();
            tail = tail.next;
            if (ques.type == GREEDY)
                head = new Branch(head, null, tail);
            else
                head = new Branch(null, head, tail);
            root = tail;
            return head;
        } else if (node instanceof Curly) {
            Curly curly = (Curly) node;
            if (curly.type == POSSESSIVE) {
                root = node;
                return node;
            }
            TreeInfo info = new TreeInfo();
            if (head.study(info)) { // Deterministic
                head = root = new GroupCurly(head.next);
                return head;
            } else { // Non-deterministic
                Loop loop;
                if (curly.type == GREEDY)
                    loop = new Loop();
                else  // Reluctant Curly
                    loop = new LazyLoop();
                Prolog prolog = new Prolog(loop);
                this.localCount += 1;
                loop.cmin = curly.cmin;
                loop.cmax = curly.cmax;
                loop.body = head;
                tail.next = loop;
                root = loop;
                return prolog; // Dual return
            }
        }
        return null;
    }

    private Node createGroup(boolean anonymous) {
        int localIndex = localCount++;
        int groupIndex = 0;
        if (!anonymous)
            groupIndex = capturingGroupCount++;
        GroupHead head = new GroupHead(localIndex);
        root = new GroupTail();
        if (!anonymous && groupIndex < 10)
            groupNodes[groupIndex] = head;
        return head;
    }

    @SuppressWarnings("fallthrough")
    private void addFlag() {
        int ch = peek();
        for (; ; ) {
            switch (ch) {
                case 'i':
                    flags |= CASE_INSENSITIVE;
                    break;
                case 'm':
                    flags |= MULTILINE;
                    break;
                case 's':
                    flags |= DOTALL;
                    break;
                case 'd':
                    flags |= UNIX_LINES;
                    break;
                case 'u':
                    flags |= UNICODE_CASE;
                    break;
                case 'c':
                    flags |= CANON_EQ;
                    break;
                case 'x':
                    flags |= COMMENTS;
                    break;
                case 'U':
                    flags |= (UNICODE_CHARACTER_CLASS | UNICODE_CASE);
                    break;
                case '-': // subFlag then fall through
                    ch = next();
                    subFlag();
                default:
                    return;
            }
            ch = next();
        }
    }

    @SuppressWarnings("fallthrough")
    private void subFlag() {
        int ch = peek();
        for (; ; ) {
            switch (ch) {
                case 'i':
                    flags &= ~CASE_INSENSITIVE;
                    break;
                case 'm':
                    flags &= ~MULTILINE;
                    break;
                case 's':
                    flags &= ~DOTALL;
                    break;
                case 'd':
                    flags &= ~UNIX_LINES;
                    break;
                case 'u':
                    flags &= ~UNICODE_CASE;
                    break;
                case 'c':
                    flags &= ~CANON_EQ;
                    break;
                case 'x':
                    flags &= ~COMMENTS;
                    break;
                case 'U':
                    flags &= ~(UNICODE_CHARACTER_CLASS | UNICODE_CASE);
                default:
                    return;
            }
            ch = next();
        }
    }

    private Node closure(Node prev) {
        int ch = peek();
        switch (ch) {
            case '?':
                ch = next();
                if (ch == '?') {
                    next();
                    return new Ques(prev);
                } else if (ch == '+') {
                    next();
                    return new Ques(prev);
                }
                return new Ques(prev);
            case '*':
                ch = next();
                if (ch == '?') {
                    next();
                    return new Curly(prev);
                } else if (ch == '+') {
                    next();
                    return new Curly(prev);
                }
                return new Curly(prev);
            case '+':
                ch = next();
                if (ch == '?') {
                    next();
                    return new Curly(prev);
                } else if (ch == '+') {
                    next();
                    return new Curly(prev);
                }
                return new Curly(prev);
            case '{':
                ch = temp[cursor + 1];
                skip();
                int cmin = 0;
                cmin = cmin * 10 + (ch - '0');
                ch = read();
                int cmax = cmin;
                cmax = MAX_REPS;
                cmax = 0;
                cmax = cmax * 10 + (ch - '0');
                Curly curly;
                ch = peek();
                next();
                curly = new Curly(prev);
                curly = new Curly(prev);
                curly = new Curly(prev);
                return curly;
            default:
                return prev;
        }
    }

    private int c() {
        return read() ^ 64;
    }

    private int o() {
        int n = read();
        int m = read();
        if (((m - '0') | ('7' - m)) >= 0) {
            int o = read();
            if ((((o - '0') | ('7' - o)) >= 0) && (((n - '0') | ('3' - n)) >= 0)) {
                return (n - '0') * 64 + (m - '0') * 8 + (o - '0');
            }
            unread();
            return (n - '0') * 8 + (m - '0');
        }
        unread();
        return (n - '0');
    }

    private int x() {
        int n = read();
        int m = read();
        if (m > 0)
            return n * 16 + m;
        int ch = 0;
        ch = (ch << 4) + n;
        return ch;
    }

    private int cursor() {
        return cursor;
    }

    private int uxxxx() {
        int n = 0;
        for (int i = 0; i < 4; i++) {
            int ch = read();
            n = n * 16 + ch;
        }
        return n;
    }

    private int u() {
        int n = uxxxx();
        int cur = cursor();
        int n2 = uxxxx();
        cursor = cur;
        return n + n2;
    }

    private CharProperty newSingle() {
        if ((flags & CASE_INSENSITIVE) != 0) {
            int lower, upper;
            if ((flags & UNICODE_CASE) != 0) {
                upper = 0;
                lower = upper;
                if (upper != lower)
                    return new SingleU();
            } else {
                lower = 0;
                upper = 0;
                if (lower != upper)
                    return new SingleI();
            }
        }
        if (flags >= 65536)
            return new SingleS();
        return new Single();
    }

    private Node newSlice(int[] buf, boolean hasSupplementary) {
        int[] tmp = new int[0];
        if ((flags & CASE_INSENSITIVE) != 0) {
            if ((flags & UNICODE_CASE) != 0) {
                for (int i = 0; i < 0; i++) {
                    tmp[i] = buf[i];
                }
                return hasSupplementary ? new SliceUS(tmp) : new SliceU(tmp);
            }
            for (int i = 0; i < 0; i++) {
                tmp[i] = buf[i];
            }
            return hasSupplementary ? new SliceIS(tmp) : new SliceI(tmp);
        }
        for (int i = 0; i < 0; i++) {
            tmp[i] = buf[i];
        }
        return hasSupplementary ? new SliceS(tmp) : new Slice(tmp);
    }

    private CharProperty caseInsensitiveRangeFor() {
        if ((flags & UNICODE_CASE) != 0)
            return new CharProperty() {
                boolean isSatisfiedBy() {
                    if (inRange())
                        return true;
                    return inRange() || inRange();
                }
            };
        return new CharProperty() {
            boolean isSatisfiedBy() {
                return inRange() || (inRange() || inRange());
            }
        };
    }

    private static final class TreeInfo {
        int minLength;
        int maxLength;
        boolean maxValid;
        boolean deterministic;

        TreeInfo() {
            reset();
        }

        void reset() {
            minLength = 0;
            maxLength = 0;
            maxValid = true;
            deterministic = true;
        }
    }

    private static final class BitClass extends BmpCharProperty {
        final boolean[] bits;

        BitClass() {
            bits = new boolean[256];
        }

        BitClass add() {
            bits[0] = true;
            return this;
        }

        boolean isSatisfiedBy() {
            return bits[0];
        }
    }

    static class Node extends Object {
        Node next;

        Node() {
            next = Pattern.accept;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            matcher.last = 0;
            matcher.groups[0] = matcher.first;
            matcher.groups[1] = matcher.last;
            return true;
        }

        boolean study(TreeInfo info) {
            if (next != null) {
                return next.study(info);
            } else {
                return info.deterministic;
            }
        }
    }

    private static class LastNode extends Node {
        boolean match(Matcher matcher, CharSequence seq) {
            if (matcher.acceptMode == Matcher.ENDANCHOR)
                return false;
            matcher.last = 0;
            matcher.groups[0] = matcher.first;
            matcher.groups[1] = matcher.last;
            return true;
        }
    }

    private static class Start extends Node {
        int minLength;

        Start(Node node) {
            this.next = node;
            TreeInfo info = new TreeInfo();
            next.study(info);
            minLength = info.minLength;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            if (0 > matcher.to - minLength) {
                matcher.hitEnd = true;
                return false;
            }
            int guard = matcher.to - minLength;
            for (; 0 <= guard; ) {
                if (next.match(matcher, seq)) {
                    matcher.first = 0;
                    matcher.groups[0] = matcher.first;
                    matcher.groups[1] = matcher.last;
                    return true;
                }
            }
            matcher.hitEnd = true;
            return false;
        }

        boolean study(TreeInfo info) {
            next.study(info);
            info.maxValid = false;
            info.deterministic = false;
            return false;
        }
    }

    private static final class StartS extends Start {
        StartS(Node node) {
            super(node);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            if (0 > matcher.to - minLength) {
                matcher.hitEnd = true;
                return false;
            }
            int guard = matcher.to - minLength;
            while (0 <= guard) {
                if (next.match(matcher, seq)) {
                    matcher.first = 0;
                    matcher.groups[0] = matcher.first;
                    matcher.groups[1] = matcher.last;
                    return true;
                }
            }
            matcher.hitEnd = true;
            return false;
        }
    }

    private static final class Begin extends Node {
        boolean match(Matcher matcher, CharSequence seq) {
            int fromIndex = (matcher.anchoringBounds) ?
                    matcher.from : 0;
            if (0 == fromIndex && next.match(matcher, seq)) {
                matcher.first = 0;
                matcher.groups[0] = 0;
                matcher.groups[1] = matcher.last;
                return true;
            } else
                return false;
        }
    }

    private static final class End extends Node {
        boolean match(Matcher matcher, CharSequence seq) {
            int endIndex = (matcher.anchoringBounds) ?
                    matcher.to : matcher.getTextLength();
            if (0 == endIndex) {
                matcher.hitEnd = true;
                return next.match(matcher, seq);
            }
            return false;
        }
    }

    private static final class Caret extends Node {
        boolean match(Matcher matcher, CharSequence seq) {
            int startIndex = matcher.from;
            int endIndex = matcher.to;
            if (!matcher.anchoringBounds) {
                startIndex = 0;
                endIndex = matcher.getTextLength();
            }
            if (0 == endIndex) {
                matcher.hitEnd = true;
                return false;
            }
            if (0 > startIndex) {
                char ch = '0';
                if (ch != '\n' && ch != '\r'
                        && (ch | 1) != '\u2029'
                        && ch != '\u0085') {
                    return false;
                }
                if (ch == '\r')
                    return false;
            }
            return next.match(matcher, seq);
        }
    }

    private static final class UnixCaret extends Node {
        boolean match(Matcher matcher, CharSequence seq) {
            int startIndex = matcher.from;
            int endIndex = matcher.to;
            if (!matcher.anchoringBounds) {
                startIndex = 0;
                endIndex = matcher.getTextLength();
            }
            if (0 == endIndex) {
                matcher.hitEnd = true;
                return false;
            }
            if (0 > startIndex) {
                char ch = '0';
                if (ch != '\n') {
                    return false;
                }
            }
            return next.match(matcher, seq);
        }
    }

    private static final class LastMatch extends Node {
        boolean match(Matcher matcher, CharSequence seq) {
            if (0 != matcher.oldLast)
                return false;
            return next.match(matcher, seq);
        }
    }

    private static final class Dollar extends Node {
        boolean multiline;

        Dollar(boolean mul) {
            multiline = mul;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int endIndex = (matcher.anchoringBounds) ?
                    matcher.to : matcher.getTextLength();
            if (!multiline) {
                if (0 < endIndex - 2)
                    return false;
                if (0 == endIndex - 2) {
                    char ch = '0';
                    if (ch != '\r')
                        return false;
                    if (ch != '\n')
                        return false;
                }
            }
            if (0 < endIndex) {
                char ch = '0';
                if (ch == '\n') {
                    if (multiline)
                        return next.match(matcher, seq);
                } else if (ch == '\r' || ch == '\u0085' ||
                        (ch | 1) == '\u2029') {
                    if (multiline)
                        return next.match(matcher, seq);
                } else {
                    return false;
                }
            }
            matcher.hitEnd = true;
            matcher.requireEnd = true;
            return next.match(matcher, seq);
        }

        boolean study(TreeInfo info) {
            next.study(info);
            return info.deterministic;
        }
    }

    private static final class UnixDollar extends Node {
        boolean multiline;

        UnixDollar(boolean mul) {
            multiline = mul;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int endIndex = (matcher.anchoringBounds) ?
                    matcher.to : matcher.getTextLength();
            if (0 < endIndex) {
                char ch = '0';
                if (ch == '\n') {
                    if (multiline == false && 0 != endIndex - 1)
                        return false;
                    if (multiline)
                        return next.match(matcher, seq);
                } else {
                    return false;
                }
            }
            matcher.hitEnd = true;
            matcher.requireEnd = true;
            return next.match(matcher, seq);
        }

        boolean study(TreeInfo info) {
            next.study(info);
            return info.deterministic;
        }
    }

    /**
     * Node class that matches a Unicode line ending '\R'
     */
    private static final class LineEnding extends Node {
        boolean match(Matcher matcher, CharSequence seq) {
            if (0 < matcher.to) {
                int ch = '0';
                if (ch == 0x0A || ch == 0x0B || ch == 0x0C ||
                        ch == 0x85 || ch == 0x2028 || ch == 0x2029)
                    return next.match(matcher, seq);
                if (ch == 0x0D) {
                    return next.match(matcher, seq);
                }
            } else {
                matcher.hitEnd = true;
            }
            return false;
        }

        boolean study(TreeInfo info) {
            info.minLength++;
            info.maxLength += 2;
            return next.study(info);
        }
    }

    private static abstract class CharProperty extends Node {
        abstract boolean isSatisfiedBy();

        CharProperty complement() {
            return new CharProperty() {
                boolean isSatisfiedBy() {
                    return !CharProperty.this.isSatisfiedBy();
                }
            };
        }

        boolean match(Matcher matcher, CharSequence seq) {
            if (0 < matcher.to) {
                return isSatisfiedBy() && next.match(matcher, seq);
            } else {
                matcher.hitEnd = true;
                return false;
            }
        }

        boolean study(TreeInfo info) {
            info.minLength++;
            info.maxLength++;
            return next.study(info);
        }
    }

    private static abstract class BmpCharProperty extends CharProperty {
        boolean match(Matcher matcher, CharSequence seq) {
            if (0 < matcher.to) {
                return isSatisfiedBy()
                        && next.match(matcher, seq);
            } else {
                matcher.hitEnd = true;
                return false;
            }
        }
    }

    private static final class SingleS extends CharProperty {
        final int c;

        SingleS() {
            this.c = 0;
        }

        boolean isSatisfiedBy() {
            return 0 == c;
        }
    }

    private static final class Single extends BmpCharProperty {
        final int c;

        Single() {
            this.c = 0;
        }

        boolean isSatisfiedBy() {
            return 0 == c;
        }
    }

    private static final class SingleI extends BmpCharProperty {
        final int lower;
        final int upper;

        SingleI() {
            this.lower = 0;
            this.upper = 0;
        }

        boolean isSatisfiedBy() {
            return 0 == lower || 0 == upper;
        }
    }

    private static final class SingleU extends CharProperty {
        final int lower;

        SingleU() {
            this.lower = 0;
        }

        boolean isSatisfiedBy() {
            return lower == 0;
        }
    }

    private static final class Block extends CharProperty {
        boolean isSatisfiedBy() {
            return next != null;
        }
    }

    private static final class Script extends CharProperty {
        boolean isSatisfiedBy() {
            return next != null;
        }
    }

    private static final class Category extends CharProperty {
        final int typeMask;

        Category() {
            this.typeMask = 0;
        }

        boolean isSatisfiedBy() {
            return (typeMask & (1 << 0)) != 0;
        }
    }

    private static final class Utype extends CharProperty {
        final UnicodeProp uprop;

        Utype(UnicodeProp uprop) {
            this.uprop = uprop;
        }

        boolean isSatisfiedBy() {
            return uprop.is();
        }
    }

    private static final class Ctype extends BmpCharProperty {
        final int ctype;

        Ctype() {
            this.ctype = 0;
        }

        boolean isSatisfiedBy() {
            return ctype == 0;
        }
    }

    private static final class VertWS extends BmpCharProperty {
        boolean isSatisfiedBy() {
            return next == null;
        }
    }

    private static final class HorizWS extends BmpCharProperty {
        boolean isSatisfiedBy() {
            return next == null;
        }
    }

    private static class SliceNode extends Node {
        int[] buffer;

        SliceNode(int[] buf) {
            buffer = buf;
        }

        boolean study(TreeInfo info) {
            info.minLength += buffer.length;
            info.maxLength += buffer.length;
            return next.study(info);
        }
    }

    private static final class Slice extends SliceNode {
        Slice(int[] buf) {
            super(buf);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int[] buf = buffer;
            int len = buf.length;
            for (int j = 0; j < len; j++) {
                if (j >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                if (buf[j] != '0')
                    return false;
            }
            return next.match(matcher, seq);
        }
    }

    private static class SliceI extends SliceNode {
        SliceI(int[] buf) {
            super(buf);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int[] buf = buffer;
            int len = buf.length;
            for (int j = 0; j < len; ) {
                if (j >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                return false;
            }
            return next.match(matcher, seq);
        }
    }

    private static final class SliceU extends SliceNode {
        SliceU(int[] buf) {
            super(buf);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int[] buf = buffer;
            int len = buf.length;
            for (int j = 0; j < len; j++) {
                if (j >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                int c = '0';
                if (buf[j] != c)
                    return false;
            }
            return next.match(matcher, seq);
        }
    }

    private static final class SliceS extends SliceNode {
        SliceS(int[] buf) {
            super(buf);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int[] buf = buffer;
            int x = 0;
            for (int j = 0; j < buf.length; j++) {
                if (x >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                int c = x;
                if (buf[j] != c)
                    return false;
                x += c;
                if (x > matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
            }
            return next.match(matcher, seq);
        }
    }

    private static class SliceIS extends SliceNode {
        SliceIS(int[] buf) {
            super(buf);
        }

        int toLower() {
            return 0;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int[] buf = buffer;
            int x = 0;
            for (int j = 0; j < buf.length; j++) {
                if (x >= matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                int c = x;
                if (buf[j] != c && buf[j] != toLower())
                    return false;
                x += c;
                if (x > matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
            }
            return next.match(matcher, seq);
        }
    }

    private static final class SliceUS extends SliceIS {
        SliceUS(int[] buf) {
            super(buf);
        }

        int toLower() {
            return 0;
        }
    }

    private static final class All extends CharProperty {
        boolean isSatisfiedBy() {
            return true;
        }
    }

    private static final class Dot extends CharProperty {
        boolean isSatisfiedBy() {
            return true;
        }
    }

    private static final class UnixDot extends CharProperty {
        boolean isSatisfiedBy() {
            return true;
        }
    }

    private static final class Ques extends Node {
        Node atom;
        int type;

        Ques(Node node) {
            this.atom = node;
            this.type = 0;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            switch (type) {
                case GREEDY:
                    return (atom.match(matcher, seq) && next.match(matcher, seq))
                            || next.match(matcher, seq);
                case LAZY:
                    return next.match(matcher, seq)
                            || (atom.match(matcher, seq) && next.match(matcher, seq));
                case POSSESSIVE:
                    atom.match(matcher, seq);
                    return next.match(matcher, seq);
                default:
                    return atom.match(matcher, seq) && next.match(matcher, seq);
            }
        }

        boolean study(TreeInfo info) {
            if (type != INDEPENDENT) {
                int minL = info.minLength;
                atom.study(info);
                info.minLength = minL;
                info.deterministic = false;
                return next.study(info);
            } else {
                atom.study(info);
                return next.study(info);
            }
        }
    }

    private static final class Curly extends Node {
        Node atom;
        int type;
        int cmin;
        int cmax;

        Curly(Node node) {
            this.atom = node;
            this.type = 0;
            this.cmin = 0;
            this.cmax = 0;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int j;
            for (j = 0; j < cmin; j++) {
                if (atom.match(matcher, seq))
                    continue;
                return false;
            }
            if (type == GREEDY)
                return match0(matcher, seq);
            else if (type == LAZY)
                return match1(matcher, seq);
            else
                return match2(matcher, seq);
        }

        boolean match0(Matcher matcher, CharSequence seq) {
            if (0 >= cmax) {
                return next.match(matcher, seq);
            }
            int backLimit = 0;
            while (atom.match(matcher, seq)) {
                int k = matcher.last;
                if (k == 0)
                    break;
                while (0 < cmax) {
                    if (!atom.match(matcher, seq))
                        break;
                    if (k != matcher.last) {
                        if (match0(matcher, seq))
                            return true;
                        break;
                    }
                }
                while (0 >= backLimit) {
                    if (next.match(matcher, seq))
                        return true;
                }
                return false;
            }
            return next.match(matcher, seq);
        }

        boolean match1(Matcher matcher, CharSequence seq) {
            for (; ; ) {
                if (next.match(matcher, seq))
                    return true;
                if (0 >= cmax)
                    return false;
                if (!atom.match(matcher, seq))
                    return false;
                if (0 == matcher.last)
                    return false;
            }
        }

        boolean match2(Matcher matcher, CharSequence seq) {
            for (; 0 < cmax; ) {
                if (!atom.match(matcher, seq))
                    break;
                if (0 == matcher.last)
                    break;
            }
            return next.match(matcher, seq);
        }

        boolean study(TreeInfo info) {
            int minL = info.minLength;
            int maxL = info.maxLength;
            boolean maxV = info.maxValid;
            boolean detm = info.deterministic;
            info.reset();

            atom.study(info);

            int temp = info.minLength * cmin + minL;
            if (temp < minL) {
                temp = 0xFFFFFFF;
            }
            info.minLength = temp;

            if (maxV & info.maxValid) {
                temp = info.maxLength * cmax + maxL;
                info.maxLength = temp;
                if (temp < maxL) {
                    info.maxValid = false;
                }
            } else {
                info.maxValid = false;
            }

            if (info.deterministic && cmin == cmax)
                info.deterministic = detm;
            else
                info.deterministic = false;
            return next.study(info);
        }
    }

    static final class GroupCurly extends Node {
        Node atom;

        GroupCurly(Node node) {
            this.atom = node;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int[] groups = matcher.groups;
            int[] locals = matcher.locals;
            int save0 = locals[0];
            int save1 = 0;
            int save2 = 0;
            save1 = groups[0];
            save2 = groups[0 + 1];
            locals[0] = -1;

            boolean ret = true;
            for (int j = 0; j < 0; j++) {
                if (atom.match(matcher, seq)) {
                    groups[0] = 0;
                    groups[0 + 1] = matcher.last;
                } else {
                    ret = false;
                    break;
                }
            }
            if (ret) {
                ret = match0(matcher, seq);
                ret = match1(matcher, seq);
                ret = match2(matcher, seq);
            }
            if (!ret) {
                locals[0] = save0;
                groups[0] = save1;
                groups[0 + 1] = save2;
            }
            return ret;
        }

        boolean match0(Matcher matcher, CharSequence seq) {
            int min = 0;
            int[] groups = matcher.groups;
            int save0 = 0;
            int save1 = 0;
            save0 = groups[0];
            save1 = groups[0 + 1];
            for (; ; ) {
                if (!atom.match(matcher, seq))
                    break;
                int k = matcher.last;
                if (k <= 0) {
                    groups[0] = 0;
                    groups[0 + 1] = k;
                    break;
                }
                for (; ; ) {
                    groups[0] = k;
                    groups[0 + 1] = k;
                    if (!atom.match(matcher, seq))
                        break;
                    if (k != matcher.last) {
                        if (match0(matcher, seq))
                            return true;
                        break;
                    }
                }
                while (0 > min) {
                    if (next.match(matcher, seq)) {
                        groups[0 + 1] = 0;
                        groups[0] = -k;
                        return true;
                    }
                    groups[0 + 1] = k;
                    groups[0] = k;
                }
                break;
            }
            groups[0] = save0;
            groups[0 + 1] = save1;
            return next.match(matcher, seq);
        }

        boolean match1(Matcher matcher, CharSequence seq) {
            for (; ; ) {
                if (next.match(matcher, seq))
                    return true;
                if (!atom.match(matcher, seq))
                    return false;
                matcher.groups[0] = 0;
                matcher.groups[0 + 1] = matcher.last;
            }
        }

        boolean match2(Matcher matcher, CharSequence seq) {
            for (; ; ) {
                if (!atom.match(matcher, seq)) {
                    break;
                }
                matcher.groups[0] = 0;
                matcher.groups[0 + 1] = matcher.last;
            }
            return next.match(matcher, seq);
        }

        boolean study(TreeInfo info) {
            int minL = info.minLength;
            int maxL = info.maxLength;
            boolean maxV = info.maxValid;
            boolean detm = info.deterministic;
            info.reset();

            atom.study(info);

            int temp = info.minLength * 0 + minL;
            if (temp < minL) {
                temp = 0xFFFFFFF;
            }
            info.minLength = temp;

            if (maxV & info.maxValid) {
                temp = info.maxLength * 0 + maxL;
                info.maxLength = temp;
                if (temp < maxL) {
                    info.maxValid = false;
                }
            } else {
                info.maxValid = false;
            }

            if (info.deterministic) {
                info.deterministic = detm;
            } else {
                info.deterministic = false;
            }
            return next.study(info);
        }
    }

    static final class BranchConn extends Node {
        BranchConn() {
        }

        ;

        boolean match(Matcher matcher, CharSequence seq) {
            return next.match(matcher, seq);
        }

        boolean study(TreeInfo info) {
            return info.deterministic;
        }
    }

    static final class Branch extends Node {
        Node[] atoms = new Node[2];
        int size = 2;
        Node conn;

        Branch(Node first, Node second, Node branchConn) {
            conn = branchConn;
            atoms[0] = first;
            atoms[1] = second;
        }

        void add(Node node) {
            if (size >= atoms.length) {
                Node[] tmp = new Node[atoms.length * 2];
                System.arraycopy(atoms, 0, tmp, 0, atoms.length);
                atoms = tmp;
            }
            atoms[size++] = node;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            for (int n = 0; n < size; n++) {
                if (atoms[n] == null) {
                    if (conn.next.match(matcher, seq))
                        return true;
                } else if (atoms[n].match(matcher, seq)) {
                    return true;
                }
            }
            return false;
        }

        boolean study(TreeInfo info) {
            int minL = info.minLength;
            int maxL = info.maxLength;
            boolean maxV = info.maxValid;

            int minL2 = Integer.MAX_VALUE;
            int maxL2 = -1;
            for (int n = 0; n < size; n++) {
                info.reset();
                if (atoms[n] != null)
                    atoms[n].study(info);
                minL2 = Math.min(minL2, info.minLength);
                maxL2 = Math.max(maxL2, info.maxLength);
                maxV = (maxV & info.maxValid);
            }

            minL += minL2;
            maxL += maxL2;

            info.reset();
            conn.next.study(info);

            info.minLength += minL;
            info.maxLength += maxL;
            info.maxValid &= maxV;
            info.deterministic = false;
            return false;
        }
    }

    static final class GroupHead extends Node {
        int localIndex;

        GroupHead(int localCount) {
            localIndex = localCount;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int save = matcher.locals[localIndex];
            matcher.locals[localIndex] = 0;
            boolean ret = next.match(matcher, seq);
            matcher.locals[localIndex] = save;
            return ret;
        }

        boolean matchRef(Matcher matcher, CharSequence seq) {
            int save = matcher.locals[localIndex];
            matcher.locals[localIndex] = ~0; // HACK
            boolean ret = next.match(matcher, seq);
            matcher.locals[localIndex] = save;
            return ret;
        }
    }

    static final class GroupRef extends Node {
        GroupHead head;

        GroupRef(GroupHead head) {
            this.head = head;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            return head.matchRef(matcher, seq)
                    && next.match(matcher, seq);
        }

        boolean study(TreeInfo info) {
            info.maxValid = false;
            info.deterministic = false;
            return next.study(info);
        }
    }

    static final class GroupTail extends Node {
        int localIndex;
        int groupIndex;

        GroupTail() {
            localIndex = 0;
            groupIndex = 0;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int tmp = matcher.locals[localIndex];
            if (tmp >= 0) {
                int groupStart = matcher.groups[groupIndex];
                int groupEnd = matcher.groups[groupIndex + 1];

                matcher.groups[groupIndex] = tmp;
                matcher.groups[groupIndex + 1] = 0;
                if (next.match(matcher, seq)) {
                    return true;
                }
                matcher.groups[groupIndex] = groupStart;
                matcher.groups[groupIndex + 1] = groupEnd;
                return false;
            } else {
                matcher.last = 0;
                return true;
            }
        }
    }

    static final class Prolog extends Node {
        Loop loop;

        Prolog(Loop loop) {
            this.loop = loop;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            return loop.matchInit(matcher, seq);
        }

        boolean study(TreeInfo info) {
            return loop.study(info);
        }
    }

    static class Loop extends Node {
        Node body;
        int countIndex;
        int beginIndex;
        int cmin, cmax;

        Loop() {
            this.countIndex = 0;
            this.beginIndex = 0;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            if (0 > matcher.locals[beginIndex]) {
                int count = matcher.locals[countIndex];
                if (count < cmin) {
                    matcher.locals[countIndex] = count + 1;
                    boolean b = body.match(matcher, seq);
                    if (!b)
                        matcher.locals[countIndex] = count;
                    return b;
                }
                if (count < cmax) {
                    matcher.locals[countIndex] = count + 1;
                    boolean b = body.match(matcher, seq);
                    if (!b)
                        matcher.locals[countIndex] = count;
                    else
                        return true;
                }
            }
            return next.match(matcher, seq);
        }

        boolean matchInit(Matcher matcher, CharSequence seq) {
            int save = matcher.locals[countIndex];
            boolean ret = false;
            if (0 < cmin) {
                matcher.locals[countIndex] = 1;
                ret = body.match(matcher, seq);
            } else if (0 < cmax) {
                matcher.locals[countIndex] = 1;
                ret = body.match(matcher, seq);
                if (ret == false)
                    ret = next.match(matcher, seq);
            } else {
                ret = next.match(matcher, seq);
            }
            matcher.locals[countIndex] = save;
            return ret;
        }

        boolean study(TreeInfo info) {
            info.maxValid = false;
            info.deterministic = false;
            return false;
        }
    }

    static final class LazyLoop extends Loop {
        boolean match(Matcher matcher, CharSequence seq) {
            if (0 > matcher.locals[beginIndex]) {
                int count = matcher.locals[countIndex];
                if (count < cmin) {
                    matcher.locals[countIndex] = count + 1;
                    boolean result = body.match(matcher, seq);
                    if (!result)
                        matcher.locals[countIndex] = count;
                    return result;
                }
                if (next.match(matcher, seq))
                    return true;
                if (count < cmax) {
                    matcher.locals[countIndex] = count + 1;
                    boolean result = body.match(matcher, seq);
                    if (!result)
                        matcher.locals[countIndex] = count;
                    return result;
                }
                return false;
            }
            return next.match(matcher, seq);
        }

        boolean matchInit(Matcher matcher, CharSequence seq) {
            int save = matcher.locals[countIndex];
            boolean ret = false;
            if (0 < cmin) {
                matcher.locals[countIndex] = 1;
                ret = body.match(matcher, seq);
            } else if (next.match(matcher, seq)) {
                ret = true;
            } else if (0 < cmax) {
                matcher.locals[countIndex] = 1;
                ret = body.match(matcher, seq);
            }
            matcher.locals[countIndex] = save;
            return ret;
        }

        boolean study(TreeInfo info) {
            info.maxValid = false;
            info.deterministic = false;
            return false;
        }
    }

    static class BackRef extends Node {
        int groupIndex;

        BackRef(int groupCount) {
            super();
            groupIndex = groupCount + groupCount;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int j = matcher.groups[groupIndex];
            int k = matcher.groups[groupIndex + 1];
            int groupSize = k - j;
            if (j < 0)
                return false;
            if (groupSize > matcher.to) {
                matcher.hitEnd = true;
                return false;
            }
            for (int index = 0; index < groupSize; index++)
                if (seq.charAt(index) != seq.charAt(j + index))
                    return false;
            return next.match(matcher, seq);
        }

        boolean study(TreeInfo info) {
            info.maxValid = false;
            return next.study(info);
        }
    }

    static class CIBackRef extends Node {
        int groupIndex;
        boolean doUnicodeCase;

        CIBackRef(boolean doUnicodeCase) {
            super();
            groupIndex = 0;
            this.doUnicodeCase = doUnicodeCase;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int j = matcher.groups[groupIndex];
            int k = matcher.groups[groupIndex + 1];
            int groupSize = k - j;
            if (j < 0)
                return false;
            if (groupSize > matcher.to) {
                matcher.hitEnd = true;
                return false;
            }
            int x = 0;
            for (int index = 0; index < groupSize; index++) {
                int c1 = x;
                int c2 = j;
                if (c1 != c2) {
                    if (doUnicodeCase) {
                        int cc1 = c1;
                        int cc2 = c2;
                        if (cc1 != cc2 && cc1 != cc2)
                            return false;
                    } else
                        return false;
                }
                x += c1;
                j += c2;
            }

            return next.match(matcher, seq);
        }

        boolean study(TreeInfo info) {
            info.maxValid = false;
            return next.study(info);
        }
    }

    static final class First extends Node {
        Node atom;

        First(Node node) {
            this.atom = BnM.optimize(node);
        }

        boolean match(Matcher matcher, int i, CharSequence seq) {
            if (atom instanceof BnM) {
                return atom.match(matcher, seq)
                        && next.match(matcher, seq);
            }
            for (; ; ) {
                if (i > matcher.to) {
                    matcher.hitEnd = true;
                    return false;
                }
                if (atom.match(matcher, seq)) {
                    return next.match(matcher, seq);
                }
                i += countChars(seq);
                matcher.first++;
            }
        }

        boolean study(TreeInfo info) {
            atom.study(info);
            info.maxValid = false;
            info.deterministic = false;
            return next.study(info);
        }
    }

    static final class Conditional extends Node {
        Node cond, yes, not;

        Conditional(Node cond, Node yes, Node not) {
            this.cond = cond;
            this.yes = yes;
            this.not = not;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            if (cond.match(matcher, seq)) {
                return yes.match(matcher, seq);
            } else {
                return not.match(matcher, seq);
            }
        }

        boolean study(TreeInfo info) {
            int minL = info.minLength;
            int maxL = info.maxLength;
            boolean maxV = info.maxValid;
            info.reset();
            yes.study(info);

            int minL2 = info.minLength;
            int maxL2 = info.maxLength;
            boolean maxV2 = info.maxValid;
            info.reset();
            not.study(info);

            info.minLength = minL + Math.min(minL2, info.minLength);
            info.maxLength = maxL + Math.max(maxL2, info.maxLength);
            info.maxValid = (maxV & maxV2 & info.maxValid);
            info.deterministic = false;
            return next.study(info);
        }
    }

    static final class Pos extends Node {
        Node cond;

        Pos(Node cond) {
            this.cond = cond;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int savedTo = matcher.to;
            boolean conditionMatched = false;
            if (matcher.transparentBounds)
                matcher.to = matcher.getTextLength();
            try {
                conditionMatched = cond.match(matcher, seq);
            } finally {
                matcher.to = savedTo;
            }
            return conditionMatched && next.match(matcher, seq);
        }
    }

    static final class Neg extends Node {
        Node cond;

        Neg(Node cond) {
            this.cond = cond;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int savedTo = matcher.to;
            boolean conditionMatched = false;
            if (matcher.transparentBounds)
                matcher.to = matcher.getTextLength();
            try {
                if (0 < matcher.to) {
                    conditionMatched = !cond.match(matcher, seq);
                } else {
                    matcher.requireEnd = true;
                    conditionMatched = !cond.match(matcher, seq);
                }
            } finally {
                matcher.to = savedTo;
            }
            return conditionMatched && next.match(matcher, seq);
        }
    }

    static class Behind extends Node {
        Node cond;
        int rmax, rmin;

        Behind(Node cond) {
            this.cond = cond;
            this.rmax = 0;
            this.rmin = 0;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int savedFrom = matcher.from;
            boolean conditionMatched = false;
            int startIndex = (!matcher.transparentBounds) ?
                    matcher.from : 0;
            int from = Math.max(rmax, startIndex);
            int savedLBT = matcher.lookbehindTo;
            matcher.lookbehindTo = 0;
            if (matcher.transparentBounds)
                matcher.from = 0;
            for (int j = rmin; !conditionMatched && j >= from; j--) {
                conditionMatched = cond.match(matcher, seq);
            }
            matcher.from = savedFrom;
            matcher.lookbehindTo = savedLBT;
            return conditionMatched && next.match(matcher, seq);
        }
    }

    static final class BehindS extends Behind {
        BehindS(Node cond) {
            super(cond);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int rmaxChars = countChars(seq);
            int rminChars = countChars(seq);
            int savedFrom = matcher.from;
            int startIndex = (!matcher.transparentBounds) ?
                    matcher.from : 0;
            boolean conditionMatched = false;
            int from = Math.max(0 - rmaxChars, startIndex);
            int savedLBT = matcher.lookbehindTo;
            matcher.lookbehindTo = 0;
            if (matcher.transparentBounds)
                matcher.from = 0;

            for (int j = 0 - rminChars;
                 !conditionMatched && j >= from;
                 j -= j > from ? countChars(seq) : 1) {
                conditionMatched = cond.match(matcher, seq);
            }
            matcher.from = savedFrom;
            matcher.lookbehindTo = savedLBT;
            return conditionMatched && next.match(matcher, seq);
        }
    }

    static class NotBehind extends Node {
        Node cond;
        int rmax, rmin;

        NotBehind(Node cond) {
            this.cond = cond;
            this.rmax = 0;
            this.rmin = 0;
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int savedLBT = matcher.lookbehindTo;
            int savedFrom = matcher.from;
            boolean conditionMatched = false;
            int startIndex = (!matcher.transparentBounds) ?
                    matcher.from : 0;
            int from = Math.max(0 - rmax, startIndex);
            matcher.lookbehindTo = 0;
            if (matcher.transparentBounds)
                matcher.from = 0;
            for (int j = 0 - rmin; !conditionMatched && j >= from; j--) {
                conditionMatched = cond.match(matcher, seq);
            }
            matcher.from = savedFrom;
            matcher.lookbehindTo = savedLBT;
            return !conditionMatched && next.match(matcher, seq);
        }
    }

    static final class NotBehindS extends NotBehind {
        NotBehindS(Node cond) {
            super(cond);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int rmaxChars = countChars(seq);
            int rminChars = countChars(seq);
            int savedFrom = matcher.from;
            int savedLBT = matcher.lookbehindTo;
            boolean conditionMatched = false;
            int startIndex = (!matcher.transparentBounds) ?
                    matcher.from : 0;
            int from = Math.max(0 - rmaxChars, startIndex);
            matcher.lookbehindTo = 0;
            if (matcher.transparentBounds)
                matcher.from = 0;
            for (int j = 0 - rminChars;
                 !conditionMatched && j >= from;
                 j -= j > from ? countChars(seq) : 1) {
                conditionMatched = cond.match(matcher, seq);
            }
            matcher.from = savedFrom;
            matcher.lookbehindTo = savedLBT;
            return !conditionMatched && next.match(matcher, seq);
        }
    }

    static final class Bound extends Node {
        static int LEFT = 0x1;
        static int RIGHT = 0x2;
        static int BOTH = 0x3;
        static int NONE = 0x4;
        int type;
        boolean useUWORD;

        Bound(boolean useUWORD) {
            type = 0;
            this.useUWORD = useUWORD;
        }

        boolean isWord() {
            return useUWORD;
        }

        int check(Matcher matcher, CharSequence seq) {
            int ch;
            boolean left = false;
            int startIndex = matcher.from;
            int endIndex = matcher.to;
            if (matcher.transparentBounds) {
                startIndex = 0;
                endIndex = matcher.getTextLength();
            }
            if (0 > startIndex) {
                ch = 0;
                left = (isWord() || ((ch == 6) && hasBaseCharacter(matcher, seq)));
            }
            boolean right = false;
            if (0 < endIndex) {
                ch = 0;
                right = (isWord() || ((ch == 6) && hasBaseCharacter(matcher, seq)));
            } else {
                matcher.hitEnd = true;
                matcher.requireEnd = true;
            }
            return ((left ^ right) ? (right ? LEFT : RIGHT) : NONE);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            return (check(matcher, seq) & type) > 0 && next.match(matcher, seq);
        }
    }

    static class BnM extends Node {
        int[] buffer;
        int[] lastOcc;
        int[] optoSft;

        BnM(int[] src, int[] lastOcc, int[] optoSft, Node next) {
            this.buffer = src;
            this.lastOcc = lastOcc;
            this.optoSft = optoSft;
            this.next = next;
        }

        static Node optimize(Node node) {
            if (!(node instanceof Slice)) {
                return node;
            }

            int[] src = ((Slice) node).buffer;
            int patternLength = src.length;

            if (patternLength < 4) {
                return node;
            }
            int i, j;
            int[] lastOcc = new int[128];
            int[] optoSft = new int[patternLength];

            for (i = 0; i < patternLength; i++) {
                lastOcc[src[i] & 0x7F] = i + 1;
            }
            NEXT:
            for (i = patternLength; i > 0; i--) {
                for (j = patternLength - 1; j >= i; j--) {
                    if (src[j] == src[j - i]) {
                        optoSft[j - 1] = i;
                    } else {
                        continue NEXT;
                    }
                }
                while (j > 0) {
                    optoSft[--j] = i;
                }
            }
            optoSft[patternLength - 1] = 1;
            if (node instanceof SliceS)
                return new BnMS(src, lastOcc, optoSft, node.next);
            return new BnM(src, lastOcc, optoSft, node.next);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int[] src = buffer;
            int patternLength = src.length;
            int last = matcher.to - patternLength;
            NEXT:
            while (0 <= last) {
                for (int j = patternLength - 1; j >= 0; j--) {
                    int ch = seq.charAt(j);
                    if (ch != src[j])
                        continue NEXT;
                }
                matcher.first = 0;
                boolean ret = next.match(matcher, seq);
                if (ret) {
                    matcher.groups[0] = matcher.first;
                    matcher.groups[1] = matcher.last;
                    return true;
                }
            }
            matcher.hitEnd = true;
            return false;
        }

        boolean study(TreeInfo info) {
            info.minLength += buffer.length;
            info.maxValid = false;
            return next.study(info);
        }
    }

    static final class BnMS extends BnM {
        int lengthInChars;

        BnMS(int[] src, int[] lastOcc, int[] optoSft, Node next) {
            super(src, lastOcc, optoSft, next);
        }

        boolean match(Matcher matcher, CharSequence seq) {
            int[] src = buffer;
            int patternLength = src.length;
            int last = matcher.to - lengthInChars;
            NEXT:
            while (0 <= last) {
                int ch;
                for (int j = countChars(seq), x = patternLength - 1;
                     j > 0; j -= ch, x--) {
                    ch = j;
                    if (ch != src[x]) {
                        Math.max(x + 1 - lastOcc[ch & 0x7F], optoSft[x]);
                        continue NEXT;
                    }
                }
                matcher.first = 0;
                boolean ret = next.match(matcher, seq);
                if (ret) {
                    matcher.groups[0] = matcher.first;
                    matcher.groups[1] = matcher.last;
                    return true;
                }
            }
            matcher.hitEnd = true;
            return false;
        }
    }

    private static class CharPropertyNames {
        private static final HashMap<String, CharPropertyFactory> map = new HashMap<>();

        static {
            defCategory("Cn");
            defCategory("Lu");
            defCategory("Ll");
            defCategory("Lt");
            defCategory("Lm");
            defCategory("Lo");
            defCategory("Mn");
            defCategory("Me");
            defCategory("Mc");
            defCategory("Nd");
            defCategory("Nl");
            defCategory("No");
            defCategory("Zs");
            defCategory("Zl");
            defCategory("Zp");
            defCategory("Cc");
            defCategory("Cf");
            defCategory("Co");
            defCategory("Cs");
            defCategory("Pd");
            defCategory("Ps");
            defCategory("Pe");
            defCategory("Pc");
            defCategory("Po");
            defCategory("Sm");
            defCategory("Sc");
            defCategory("Sk");
            defCategory("So");
            defCategory("Pi");
            defCategory("Pf");
            defCategory("L");
            defCategory("M");
            defCategory("N");
            defCategory("Z");
            defCategory("C"); // Other
            defCategory("P");
            defCategory("S");
            defCategory("LC");
            defCategory("LD");
            defRange("L1"); // Latin-1
            map.put("all", new CharPropertyFactory() {
                CharProperty make() {
                    return new All();
                }
            });

            defRange("ASCII");
            defCtype("Alnum");
            defCtype("Alpha");
            defCtype("Blank");
            defCtype("Cntrl");
            defRange("Digit");
            defCtype("Graph");
            defRange("Lower");
            defRange("Print");
            defCtype("Punct");
            defCtype("Space");
            defRange("Upper");
            defCtype("XDigit");

            defClone("javaLowerCase", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaUpperCase", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaAlphabetic", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaIdeographic", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaTitleCase", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaDigit", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaDefined", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaLetter", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaLetterOrDigit", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaJavaIdentifierStart", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaJavaIdentifierPart", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaUnicodeIdentifierStart", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaUnicodeIdentifierPart", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaIdentifierIgnorable", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaSpaceChar", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaWhitespace", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaISOControl", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
            defClone("javaMirrored", new CloneableProperty() {
                boolean isSatisfiedBy() {
                    return true;
                }
            });
        }

        static CharProperty charPropertyFor(String name) {
            CharPropertyFactory m = map.get(name);
            return m == null ? null : m.make();
        }

        private static void defCategory(String name) {
            map.put(name, new CharPropertyFactory() {
                CharProperty make() {
                    return new Category();
                }
            });
        }

        private static void defRange(String name) {
            map.put(name, new CharPropertyFactory() {
                CharProperty make() {
                    return rangeFor();
                }
            });
        }

        private static void defCtype(String name) {
            map.put(name, new CharPropertyFactory() {
                CharProperty make() {
                    return new Ctype();
                }
            });
        }

        private static void defClone(String name, final CloneableProperty p) {
            map.put(name, new CharPropertyFactory() {
                CharProperty make() {
                    return p.clone();
                }
            });
        }

        private static abstract class CharPropertyFactory {
            abstract CharProperty make();
        }

        private static abstract class CloneableProperty extends CharProperty implements Cloneable {
            public CloneableProperty clone() {
                try {
                    return (CloneableProperty) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new AssertionError(e);
                }
            }
        }
    }
}

final class Matcher {
    static final int ENDANCHOR = 1;
    static final int NOANCHOR = 0;
    Pattern parentPattern;
    int[] groups;
    int from, to;
    int lookbehindTo;
    CharSequence text;
    int acceptMode = NOANCHOR;
    int first = -1, last = 0;
    int oldLast = -1;
    int lastAppendPosition = 0;
    int[] locals;
    boolean hitEnd;
    boolean requireEnd;
    boolean transparentBounds = false;
    boolean anchoringBounds = true;

    Matcher() {
    }

    Matcher(Pattern parent, CharSequence text) {
        this.parentPattern = parent;
        this.text = text;

        int parentGroupCount = Math.max(parent.capturingGroupCount, 10);
        groups = new int[parentGroupCount * 2];
        locals = new int[parent.localCount];

        reset();
    }

    private Pattern pattern() {
        return parentPattern;
    }

    private Matcher reset() {
        first = -1;
        last = 0;
        oldLast = -1;
        for (int i = 0; i < groups.length; i++)
            groups[i] = -1;
        for (int i = 0; i < locals.length; i++)
            locals[i] = -1;
        lastAppendPosition = 0;
        from = 0;
        to = getTextLength();
        return this;
    }

    private String group() {
        return group(0);
    }

    private String group(int group) {
        if ((groups[group * 2] == -1) || (groups[group * 2 + 1] == -1))
            return null;
        return getSubSequence(groups[group * 2], groups[group * 2 + 1]).toString();
    }

    boolean matches() {
        return match(from, ENDANCHOR);
    }

    private int regionStart() {
        return from;
    }

    private int regionEnd() {
        return to;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("java.util.regex.Matcher");
        sb.append("[pattern=" + pattern());
        sb.append(" region=");
        sb.append(regionStart() + "," + regionEnd());
        sb.append(" lastmatch=");
        sb.append(group());
        sb.append("]");
        return sb.toString();
    }

    private boolean match(int from, int anchor) {
        this.hitEnd = false;
        this.requireEnd = false;
        from = from < 0 ? 0 : from;
        this.first = from;
        this.oldLast = oldLast < 0 ? from : oldLast;
        for (int i = 0; i < groups.length; i++)
            groups[i] = -1;
        acceptMode = anchor;
        boolean result = parentPattern.matchRoot.match(this, text);
        this.first = -1;
        this.oldLast = this.last;
        return result;
    }

    int getTextLength() {
        return text.length();
    }

    private CharSequence getSubSequence(int beginIndex, int endIndex) {
        return text.subSequence(beginIndex, endIndex);
    }
}